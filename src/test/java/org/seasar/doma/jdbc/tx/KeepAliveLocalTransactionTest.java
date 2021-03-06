package org.seasar.doma.jdbc.tx;

import java.sql.SQLException;
import junit.framework.TestCase;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

public class KeepAliveLocalTransactionTest extends TestCase {

  private final MockConnection connection = new MockConnection();

  private final LocalTransactionDataSource dataSource =
      new LocalTransactionDataSource(new MockDataSource(connection));

  private final UtilLoggingJdbcLogger jdbcLogger = new UtilLoggingJdbcLogger();

  private final KeepAliveLocalTransaction transaction =
      dataSource.getKeepAliveLocalTransaction(jdbcLogger);

  public void testBegin() throws Exception {
    transaction.begin();
    assertTrue(transaction.isActive());
    dataSource.getConnection();
    assertFalse(connection.autoCommit);
    assertEquals(TransactionIsolationLevel.READ_COMMITTED.getLevel(), connection.isolationLevel);
  }

  public void testBeginImlicitDefaultTransactionIsolationLevel() throws Exception {
    LocalTransaction transaction =
        dataSource.getKeepAliveLocalTransaction(jdbcLogger, TransactionIsolationLevel.SERIALIZABLE);
    transaction.begin();
    assertTrue(transaction.isActive());
    dataSource.getConnection();
    assertFalse(connection.autoCommit);
    assertEquals(TransactionIsolationLevel.SERIALIZABLE.getLevel(), connection.isolationLevel);
  }

  public void testBeginWithTransactionIsolationLevel() throws Exception {
    transaction.begin(TransactionIsolationLevel.SERIALIZABLE);
    assertTrue(transaction.isActive());
    dataSource.getConnection();
    assertFalse(connection.autoCommit);
    assertEquals(TransactionIsolationLevel.SERIALIZABLE.getLevel(), connection.isolationLevel);
  }

  public void testBegin_alreadyBegun() throws Exception {
    transaction.begin();
    try {
      transaction.begin();
      fail();
    } catch (TransactionAlreadyBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testBegin_failedToBegin() throws Exception {
    final var exception = new SQLException();
    MockConnection connection =
        new MockConnection() {

          @Override
          public void setAutoCommit(boolean autoCommit) throws SQLException {
            throw exception;
          }
        };
    var dataSource = new LocalTransactionDataSource(new MockDataSource(connection));
    LocalTransaction transaction = dataSource.getKeepAliveLocalTransaction(jdbcLogger);
    try {
      transaction.begin();
      dataSource.getConnection();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(exception, expected.getCause());
    }
  }

  public void testBegin_failedToSetTransactionIsolation() throws Exception {
    final var exception = new SQLException();
    MockConnection connection =
        new MockConnection() {

          @Override
          public void setTransactionIsolation(int level) throws SQLException {
            throw exception;
          }
        };
    var dataSource = new LocalTransactionDataSource(new MockDataSource(connection));
    LocalTransaction transaction = dataSource.getKeepAliveLocalTransaction(jdbcLogger);
    try {
      transaction.begin(TransactionIsolationLevel.READ_COMMITTED);
      dataSource.getConnection();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(exception, expected.getCause());
    }
  }

  public void testSetSavepoint() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertTrue(connection.savepointNames.contains("hoge"));
  }

  public void testSetSavepoint_alreadyExists() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    try {
      transaction.setSavepoint("hoge");
      fail();
    } catch (SavepointAlreadyExistsException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testSetSavepoint_notYetBegun() throws Exception {
    try {
      transaction.setSavepoint("hoge");
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testSetSavepoint_nullPointer() throws Exception {
    transaction.begin();
    try {
      transaction.setSavepoint(null);
      fail();
    } catch (DomaNullPointerException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testHasSavepoint() throws Exception {
    transaction.begin();
    assertFalse(transaction.hasSavepoint("hoge"));
    transaction.setSavepoint("hoge");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertTrue(transaction.hasSavepoint("hoge"));
  }

  public void testHasSavepoint_notYetBegun() throws Exception {
    try {
      transaction.hasSavepoint("hoge");
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testHasSavepoint_nullPointer() throws Exception {
    transaction.begin();
    try {
      transaction.hasSavepoint(null);
      fail();
    } catch (DomaNullPointerException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testReleaseSavepoint() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    transaction.setSavepoint("foo");
    transaction.setSavepoint("bar");
    transaction.releaseSavepoint("foo");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertFalse(connection.savepointNames.contains("hoge"));
    assertFalse(connection.savepointNames.contains("foo"));
    assertTrue(connection.savepointNames.contains("bar"));
  }

  public void testReleaseSavepoint_notYetBegun() throws Exception {
    try {
      transaction.releaseSavepoint("hoge");
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testReleaseSavepoint_notFound() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    try {
      transaction.releaseSavepoint("foo");
      fail();
    } catch (SavepointNotFoundException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testReleaseSavepoint_nullPointer() throws Exception {
    transaction.begin();
    try {
      transaction.releaseSavepoint(null);
      fail();
    } catch (DomaNullPointerException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testCommit() throws Exception {
    transaction.begin();
    dataSource.getConnection();
    transaction.commit();
    assertFalse(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertTrue(connection.committed);
  }

  public void testCommit_ConnectionUnused() throws Exception {
    transaction.begin();
    transaction.commit();
    assertFalse(transaction.isActive());
    assertTrue(connection.autoCommit);
    assertFalse(connection.committed);
  }

  public void testCommit_notYetBegun() throws Exception {
    try {
      transaction.commit();
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testRollback() throws Exception {
    transaction.begin();
    dataSource.getConnection();
    transaction.rollback();
    assertFalse(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertTrue(connection.rolledback);
  }

  public void testRollback_ConnectionUnused() throws Exception {
    transaction.begin();
    transaction.rollback();
    assertFalse(transaction.isActive());
    assertTrue(connection.autoCommit);
    assertFalse(connection.rolledback);
  }

  public void testRollback_notYetBegun() throws Exception {
    transaction.rollback();
  }

  public void testRollbackSavepoint() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    transaction.rollback("hoge");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertFalse(connection.savepointNames.contains("hoge"));
  }

  public void testRollbackSavepoint_notYetBegun() throws Exception {
    try {
      transaction.rollback("hoge");
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testRollbackSavepoint_notFound() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    try {
      transaction.rollback("foo");
      fail();
    } catch (SavepointNotFoundException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testInitAndDestory() throws Exception {
    assertNull(transaction.localTxContextHolder.get());
    transaction.init();
    assertNotNull(transaction.localTxContextHolder.get());
    transaction.destroy();
    assertNull(transaction.localTxContextHolder.get());
  }
}
