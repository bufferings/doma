package org.seasar.doma.jdbc.command;

import example.entity.Emp;
import example.entity._Emp;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoUpdateQuery;

public class UpdateCommandTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testExecute() throws Exception {
    var emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setVersion(10);
    emp.originalStates = new Emp();

    var query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new UpdateCommand(query).execute();
    query.complete();

    assertEquals(1, rows);
    var sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql);

    var bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(4, bindValues.size());
    assertEquals("hoge", bindValues.get(0).getValue());
    assertEquals(10, bindValues.get(1).getValue());
    assertEquals(1, bindValues.get(2).getValue());
    assertEquals(10, bindValues.get(3).getValue());
  }

  public void testExecute_throwsOptimisticLockException() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 0;

    var query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    var command = new UpdateCommand(query);
    try {
      command.execute();
      fail();
    } catch (OptimisticLockException expected) {
    }
  }

  public void testExecute_suppressesOptimisticLockException() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 0;

    var query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setOptimisticLockExceptionSuppressed(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    new UpdateCommand(query).execute();
    query.complete();
  }

  public void testExecute_OriginalStates() throws Exception {
    var emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setVersion(10);

    var states = new Emp();
    states.setId(1);
    states.setName("foo");
    states.setVersion(10);

    emp.originalStates = states;

    var query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new UpdateCommand(query).execute();
    query.complete();

    assertEquals(1, rows);
    var sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql);

    var bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(4, bindValues.size());
    assertEquals("hoge", bindValues.get(0).getValue());
    assertEquals(10, bindValues.get(1).getValue());
    assertEquals(1, bindValues.get(2).getValue());
    assertEquals(10, bindValues.get(3).getValue());

    var updatedStates = emp.originalStates;
    assertEquals(Integer.valueOf(1), updatedStates.getId());
    assertEquals("hoge", updatedStates.getName());
    assertNull(updatedStates.getSalary());
    assertEquals(Integer.valueOf(11), updatedStates.getVersion());
  }
}
