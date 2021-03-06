package org.seasar.doma.wrapper;

import junit.framework.TestCase;

public class DoubleWrapperTest extends TestCase {

  /** */
  public void testIncrement() {
    var wrapper = new DoubleWrapper(10d);
    wrapper.increment();
    assertEquals(11d, wrapper.get());
  }

  /** */
  public void testDecrement() {
    var wrapper = new DoubleWrapper(10d);
    wrapper.decrement();
    assertEquals(9d, wrapper.get());
  }
}
