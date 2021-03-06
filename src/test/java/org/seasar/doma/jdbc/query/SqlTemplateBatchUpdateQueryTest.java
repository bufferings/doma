package org.seasar.doma.jdbc.query;

import example.entity.Emp;
import example.entity._Emp;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlTemplateBatchUpdateQueryTest extends TestCase {

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

    var query = new SqlTemplateBatchUpdateQuery<>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    BatchUpdateQuery batchUpdateQuery = query;
    assertEquals(2, batchUpdateQuery.getSqls().size());
  }

  public void testOption_default() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(200);

    var query = new SqlTemplateBatchUpdateQuery<>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSqls().get(0);
    assertEquals("update emp set name = ?, salary = ? where id = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertNull(parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals("update emp set name = ?, salary = ? where id = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertNull(parameters.get(0).getWrapper().get());
    assertEquals(new BigDecimal(2000), parameters.get(1).getWrapper().get());
    assertEquals(20, parameters.get(2).getWrapper().get());
  }

  public void testIsExecutable() throws Exception {
    var query = new SqlTemplateBatchUpdateQuery<>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setParameterName("e");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setElements(Collections.<Emp>emptyList());
    query.prepare();
    assertFalse(query.isExecutable());
  }

  public void testPopulate() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    var query = new SqlTemplateBatchUpdateQuery<>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setEntityDesc(_Emp.getSingletonInternal());
    query.prepare();

    assertEquals(2, query.getSqls().size());
    Sql<?> sql = query.getSql();
    assertEquals(
        "update aaa set NAME = ?, SALARY = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
  }

  @org.seasar.doma.Sql("update aaa set /*%populate */ id = id where id = /* e.id */1")
  public void testPopulate_include() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    var query = new SqlTemplateBatchUpdateQuery<>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setEntityDesc(_Emp.getSingletonInternal());
    query.setIncludedPropertyNames("name");
    query.prepare();

    assertEquals(2, query.getSqls().size());
    Sql<?> sql = query.getSql();
    assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
  }

  @org.seasar.doma.Sql("update aaa set /*%populate */ id = id where id = /* e.id */1")
  public void testPopulate_exclude() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    var query = new SqlTemplateBatchUpdateQuery<>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setEntityDesc(_Emp.getSingletonInternal());
    query.setExcludedPropertyNames("name");
    query.prepare();

    assertEquals(2, query.getSqls().size());
    Sql<?> sql = query.getSql();
    assertEquals("update aaa set SALARY = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
  }

  @org.seasar.doma.Sql("update aaa set /*%populate */ id = id where id = /* e.id */1")
  public void testPopulate_ignoreVersion() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    var query = new SqlTemplateBatchUpdateQuery<>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setEntityDesc(_Emp.getSingletonInternal());
    query.setVersionIgnored(true);
    query.prepare();

    assertEquals(2, query.getSqls().size());
    Sql<?> sql = query.getSql();
    assertEquals("update aaa set NAME = ?, SALARY = ?, VERSION = ? where id = ?", sql.getRawSql());
  }
}
