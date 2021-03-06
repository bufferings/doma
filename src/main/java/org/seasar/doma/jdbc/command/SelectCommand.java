package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.seasar.doma.FetchType;
import org.seasar.doma.Select;
import org.seasar.doma.internal.jdbc.command.PreparedSqlParameterBinder;
import org.seasar.doma.internal.jdbc.command.ResultSetState;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlExecutionException;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * A command for an SQL SELECT statement.
 *
 * @param <RESULT> the result type
 * @see Select
 */
public class SelectCommand<RESULT> implements Command<RESULT> {

  protected final SelectQuery query;

  protected final PreparedSql sql;

  protected final ResultSetHandler<RESULT> resultSetHandler;

  public SelectCommand(SelectQuery query, ResultSetHandler<RESULT> resultSetHandler) {
    assertNotNull(query, resultSetHandler);
    this.query = query;
    this.sql = query.getSql();
    this.resultSetHandler = resultSetHandler;
  }

  @Override
  public RESULT execute() {
    Supplier<RESULT> supplier = null;
    var connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
    try {
      var preparedStatement = JdbcUtil.prepareStatement(connection, sql);
      try {
        log();
        setupOptions(preparedStatement);
        bindParameters(preparedStatement);
        supplier = executeQuery(preparedStatement);
      } catch (SQLException e) {
        var dialect = query.getConfig().getDialect();
        throw new SqlExecutionException(
            query.getConfig().getExceptionSqlLogType(), sql, e, dialect.getRootCause(e));
      } finally {
        close(supplier, () -> JdbcUtil.close(preparedStatement, query.getConfig().getJdbcLogger()));
      }
    } finally {
      close(supplier, () -> JdbcUtil.close(connection, query.getConfig().getJdbcLogger()));
    }
    return supplier.get();
  }

  protected void log() {
    var logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }

  protected void setupOptions(PreparedStatement preparedStatement) throws SQLException {
    if (query.getFetchSize() > 0) {
      preparedStatement.setFetchSize(query.getFetchSize());
    }
    if (query.getMaxRows() > 0) {
      preparedStatement.setMaxRows(query.getMaxRows());
    }
    if (query.getQueryTimeout() > 0) {
      preparedStatement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  protected void bindParameters(PreparedStatement preparedStatement) throws SQLException {
    var binder = new PreparedSqlParameterBinder(query);
    binder.bind(preparedStatement, sql.getParameters());
  }

  protected Supplier<RESULT> executeQuery(PreparedStatement preparedStatement) throws SQLException {
    Supplier<RESULT> supplier = null;
    var resultSet = preparedStatement.executeQuery();
    try {
      supplier = handleResultSet(resultSet);
      return supplier;
    } finally {
      close(supplier, () -> JdbcUtil.close(resultSet, query.getConfig().getJdbcLogger()));
    }
  }

  protected Supplier<RESULT> handleResultSet(ResultSet resultSet) throws SQLException {
    return resultSetHandler.handle(
        resultSet,
        query,
        (resultSetState) -> {
          if (resultSetState == ResultSetState.NO_RESULT && query.isResultEnsured()) {
            Sql<?> sql = query.getSql();
            throw new NoResultException(query.getConfig().getExceptionSqlLogType(), sql);
          }
        });
  }

  protected void close(Supplier<RESULT> supplier, Runnable closeHandler) {
    if (supplier != null && query.isResultStream() && query.getFetchType() == FetchType.LAZY) {
      var result = supplier.get();
      if (result instanceof Stream) {
        @SuppressWarnings("resource")
        var stream = (Stream<?>) result;
        //noinspection ResultOfMethodCallIgnored
        stream.onClose(closeHandler);
      } else {
        closeHandler.run();
      }
    } else {
      closeHandler.run();
    }
  }
}
