package org.seasar.doma.internal.util;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;

public class IOUtilTest extends TestCase {

  public void test() throws Exception {
    IOUtil.close(
        () -> {
          throw new IOException();
        });
  }

  public void testEndWith_true() throws Exception {
    var file = new File("/fuga/META-INF/piyo/HogeDao/selectById.sql");
    var pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertTrue(IOUtil.endsWith(file, pathname));
  }

  public void testEndWith_false() throws Exception {
    var file = new File("/fuga/META-INF/piyo/hogeDao/selectById.sql");
    var pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertFalse(IOUtil.endsWith(file, pathname));
  }
}
