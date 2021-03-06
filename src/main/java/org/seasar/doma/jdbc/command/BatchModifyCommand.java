package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.seasar.doma.internal.jdbc.command.PreparedSqlParameterBinder;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.BatchOptimisticLockException;
import org.seasar.doma.jdbc.BatchSqlExecutionException;
import org.seasar.doma.jdbc.BatchUniqueConstraintException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.query.BatchModifyQuery;

/**
 * An abstract class for batch commands.
 *
 * @param <QUERY> the query type
 */
public abstract class BatchModifyCommand<QUERY extends BatchModifyQuery> implements Command<int[]> {

  protected final QUERY query;

  protected BatchModifyCommand(QUERY query) {
    assertNotNull(query);
    this.query = query;
  }

  @Override
  public int[] execute() {
    if (!query.isExecutable()) {
      var logger = query.getConfig().getJdbcLogger();
      logger.logSqlExecutionSkipping(
          query.getClassName(), query.getMethodName(), query.getSqlExecutionSkipCause());
      return new int[] {};
    }
    var connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
    try {
      var sql = query.getSql();
      var preparedStatement = prepareStatement(connection, sql);
      try {
        setupOptions(preparedStatement);
        return executeInternal(preparedStatement, query.getSqls());
      } catch (SQLException e) {
        var dialect = query.getConfig().getDialect();
        throw new BatchSqlExecutionException(
            query.getConfig().getExceptionSqlLogType(), sql, e, dialect.getRootCause(e));
      } finally {
        JdbcUtil.close(preparedStatement, query.getConfig().getJdbcLogger());
      }
    } finally {
      JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
    }
  }

  protected PreparedStatement prepareStatement(Connection connection, PreparedSql sql) {
    if (query.isAutoGeneratedKeysSupported()) {
      var config = query.getConfig();
      var dialect = config.getDialect();
      switch (dialect.getAutoGeneratedKeysType()) {
        case FIRST_COLUMN:
          return JdbcUtil.prepareStatementForAutoGeneratedKeysOfFirstColumn(connection, sql);
        case DEFAULT:
          return JdbcUtil.prepareStatementForAutoGeneratedKeys(connection, sql);
      }
    }
    return JdbcUtil.prepareStatement(connection, sql);
  }

  protected abstract int[] executeInternal(
      PreparedStatement preparedStatement, List<PreparedSql> sqls) throws SQLException;

  protected void setupOptions(PreparedStatement preparedStatement) throws SQLException {
    if (query.getQueryTimeout() > 0) {
      preparedStatement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  protected int[] executeBatch(PreparedStatement preparedStatement, List<PreparedSql> sqls)
      throws SQLException {
    var batchSize = query.getBatchSize() > 0 ? query.getBatchSize() : 1;
    var sqlSize = sqls.size();
    var updatedRows = new int[sqlSize];
    var i = 0;
    var pos = 0;
    for (var sql : sqls) {
      log(sql);
      bindParameters(preparedStatement, sql);
      preparedStatement.addBatch();
      if (i == sqlSize - 1 || (batchSize > 0 && (i + 1) % batchSize == 0)) {
        var rows = executeBatch(preparedStatement, sql);
        validateRows(preparedStatement, sql, rows);
        System.arraycopy(rows, 0, updatedRows, pos, rows.length);
        pos = i + 1;
      }
      i++;
    }
    return updatedRows;
  }

  protected int[] executeBatch(PreparedStatement preparedStatement, PreparedSql sql)
      throws SQLException {
    try {
      return preparedStatement.executeBatch();
    } catch (SQLException e) {
      var dialect = query.getConfig().getDialect();
      if (dialect.isUniqueConstraintViolated(e)) {
        throw new BatchUniqueConstraintException(
            query.getConfig().getExceptionSqlLogType(), sql, e);
      }
      throw e;
    }
  }

  protected void log(PreparedSql sql) {
    var logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }

  protected void bindParameters(PreparedStatement preparedStatement, PreparedSql sql)
      throws SQLException {
    var binder = new PreparedSqlParameterBinder(query);
    binder.bind(preparedStatement, sql.getParameters());
  }

  protected void validateRows(PreparedStatement preparedStatement, PreparedSql sql, int[] rows)
      throws SQLException {
    var dialect = query.getConfig().getDialect();
    if (dialect.supportsBatchUpdateResults()) {
      if (!query.isOptimisticLockCheckRequired()) {
        return;
      }
      for (var row : rows) {
        if (row != 1) {
          throw new BatchOptimisticLockException(query.getConfig().getExceptionSqlLogType(), sql);
        }
      }
    } else if (preparedStatement.getUpdateCount() == rows.length) {
      Arrays.fill(rows, 1);
    } else {
      if (!query.isOptimisticLockCheckRequired()) {
        return;
      }
      throw new BatchOptimisticLockException(query.getConfig().getExceptionSqlLogType(), sql);
    }
  }
}
