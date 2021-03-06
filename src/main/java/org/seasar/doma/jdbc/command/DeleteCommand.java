package org.seasar.doma.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.Delete;
import org.seasar.doma.jdbc.query.DeleteQuery;

/**
 * A command for an SQL DELETE statement.
 *
 * @see Delete
 */
public class DeleteCommand extends ModifyCommand<DeleteQuery> {

  public DeleteCommand(DeleteQuery query) {
    super(query);
  }

  @Override
  protected int executeInternal(PreparedStatement preparedStatement) throws SQLException {
    return executeUpdate(preparedStatement);
  }
}
