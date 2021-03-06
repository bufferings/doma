package org.seasar.doma.internal.apt.processor.holder;

import java.math.BigDecimal;
import org.seasar.doma.Holder;

public class Outer_nonPublicMiddle {

  static class Middle {

    @Holder(valueType = BigDecimal.class)
    public static class Inner {

      private final BigDecimal value;

      public Inner(BigDecimal value) {
        this.value = value;
      }

      public BigDecimal getValue() {
        return value;
      }
    }
  }
}
