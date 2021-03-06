package org.seasar.doma.internal.jdbc.command;

import example.holder._PhoneNumber;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.SqlTemplateSelectQuery;

public class HolderResultListHandlerTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @Override
  protected void setUp() throws Exception {
    method = getClass().getMethod(getName());
  }

  public void testHandle() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("x"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("01-2345-6789"));
    resultSet.rows.add(new RowData("12-3456-7890"));

    var query = new SqlTemplateSelectQuery();
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var handler =
        new ScalarResultListHandler<>(() -> _PhoneNumber.getSingletonInternal().createScalar());
    var results = handler.handle(resultSet, query, (__) -> {}).get();
    assertEquals(2, results.size());
    assertEquals("01-2345-6789", results.get(0).getValue());
    assertEquals("12-3456-7890", results.get(1).getValue());
  }
}
