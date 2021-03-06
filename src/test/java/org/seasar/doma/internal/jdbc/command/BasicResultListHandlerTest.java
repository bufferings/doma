package org.seasar.doma.internal.jdbc.command;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.NonSingleColumnException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.SqlTemplateSelectQuery;

public class BasicResultListHandlerTest extends TestCase {

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
    resultSet.rows.add(new RowData("aaa"));
    resultSet.rows.add(new RowData("bbb"));

    var query = new SqlTemplateSelectQuery();
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var handler =
        new ScalarResultListHandler<>(
            () -> new BasicScalar<>(new org.seasar.doma.wrapper.StringWrapper(), false));
    var results = handler.handle(resultSet, query, (__) -> {}).get();
    assertEquals(2, results.size());
    assertEquals("aaa", results.get(0));
    assertEquals("bbb", results.get(1));
  }

  public void testHandle_NonSingleColumnException() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("x"));
    metaData.columns.add(new ColumnMetaData("y"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("aaa", "bbb"));

    var query = new SqlTemplateSelectQuery();
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setResultEnsured(true);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var handler =
        new ScalarResultListHandler<>(
            () -> new BasicScalar<>(new org.seasar.doma.wrapper.StringWrapper(), false));
    try {
      handler.handle(resultSet, query, (__) -> {});
      fail();
    } catch (NonSingleColumnException expected) {
    }
  }
}
