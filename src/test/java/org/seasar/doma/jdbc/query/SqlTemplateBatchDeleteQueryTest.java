package org.seasar.doma.jdbc.query;

import example.entity.Emp;
import java.util.Arrays;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlTemplateBatchDeleteQueryTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testPrepare() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    var query = new SqlTemplateBatchDeleteQuery<>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    BatchDeleteQuery batchDeleteQuery = query;
    assertEquals(2, batchDeleteQuery.getSqls().size());
  }

  public void testOption_default() throws Exception {
    var emp1 = new Emp();
    emp1.setName("aaa");

    var emp2 = new Emp();
    emp2.setName("bbb");

    var query = new SqlTemplateBatchDeleteQuery<>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSqls().get(0);
    assertEquals("delete from emp where name = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(1, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals("delete from emp where name = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(1, parameters.size());
    assertEquals("bbb", parameters.get(0).getWrapper().get());
  }
}
