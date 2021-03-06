package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Holder;

@Holder(valueType = Integer.class)
public class Height<T> {

  private final Integer value;

  public Height(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
