package org.seasar.doma.jdbc.query;

import example.entity.Emp;
import example.entity._Emp;
import java.math.BigDecimal;
import java.util.Arrays;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoBatchDeleteQueryTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testPrepare() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");

    var query = new AutoBatchDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    BatchDeleteQuery batchDeleteQuery = query;
    assertEquals(2, batchDeleteQuery.getSqls().size());
  }

  public void testOption_default() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(10);

    var query = new AutoBatchDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSqls().get(0);
    assertEquals("delete from EMP where ID = ? and VERSION = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(2, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertTrue(parameters.get(1).getWrapper().get() == null);

    sql = query.getSqls().get(1);
    assertEquals("delete from EMP where ID = ? and VERSION = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(2, parameters.size());
    assertEquals(20, parameters.get(0).getWrapper().get());
    assertEquals(10, parameters.get(1).getWrapper().get());
  }

  public void testOption_ignoreVersion() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(10);

    var query = new AutoBatchDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setVersionIgnored(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSqls().get(0);
    assertEquals("delete from EMP where ID = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(1, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals("delete from EMP where ID = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(1, parameters.size());
    assertEquals(20, parameters.get(0).getWrapper().get());
  }
}
