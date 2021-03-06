package org.seasar.doma.jdbc;

import junit.framework.TestCase;

public class NonUniqueResultExceptionTest extends TestCase {

  public void test() throws Exception {
    var e = new NonUniqueResultException(SqlLogType.FORMATTED, SqlKind.SELECT, "aaa", "bbb", "ccc");
    System.out.println(e.getMessage());
    assertSame(SqlKind.SELECT, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getFormattedSql());
    assertEquals("ccc", e.getSqlFilePath());
  }
}
