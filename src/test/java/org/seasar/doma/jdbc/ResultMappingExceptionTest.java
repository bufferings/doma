package org.seasar.doma.jdbc;

import java.util.Arrays;
import junit.framework.TestCase;

public class ResultMappingExceptionTest extends TestCase {

  public void test() throws Exception {
    var e =
        new ResultMappingException(
            SqlLogType.FORMATTED,
            "aaa",
            Arrays.asList("bbb", "bbb2"),
            Arrays.asList("ccc", "ccc2"),
            SqlKind.SELECT,
            "ddd",
            "eee",
            "fff");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getEntityClassName());
    var unmappedPropertyNames = e.getUnmappedPropertyNames();
    assertEquals("bbb", unmappedPropertyNames.get(0));
    assertEquals("bbb2", unmappedPropertyNames.get(1));
    var expectedColumnNames = e.getExpectedColumnNames();
    assertEquals("ccc", expectedColumnNames.get(0));
    assertEquals("ccc2", expectedColumnNames.get(1));
    assertSame(SqlKind.SELECT, e.getKind());
    assertEquals("ddd", e.getRawSql());
    assertEquals("eee", e.getFormattedSql());
    assertEquals("fff", e.getSqlFilePath());
  }
}
