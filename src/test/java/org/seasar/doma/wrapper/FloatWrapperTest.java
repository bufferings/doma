package org.seasar.doma.wrapper;

import junit.framework.TestCase;

public class FloatWrapperTest extends TestCase {

  /** */
  public void testIncrement() {
    var wrapper = new FloatWrapper(10f);
    wrapper.increment();
    assertEquals(11f, wrapper.get());
  }

  /** */
  public void testDecrement() {
    var wrapper = new FloatWrapper(10f);
    wrapper.decrement();
    assertEquals(9f, wrapper.get());
  }
}
