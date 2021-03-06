package org.seasar.doma.internal.apt.processor.holder;

import java.math.BigDecimal;
import org.seasar.doma.Holder;

public class Outer_nonStaticInner {

  @Holder(valueType = BigDecimal.class)
  public class Inner {

    private final BigDecimal value;

    public Inner(BigDecimal value) {
      this.value = value;
    }

    public BigDecimal getValue() {
      return value;
    }
  }
}
