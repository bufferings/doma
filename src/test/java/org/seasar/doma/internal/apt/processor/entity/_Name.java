package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;
import org.seasar.doma.wrapper.StringWrapper;

public class _Name extends AbstractHolderDesc<String, Name> {

  private _Name() {
    super(StringWrapper::new);
  }

  @Override
  public Name newHolder(String value) {
    return null;
  }

  @Override
  public String getBasicValue(Name holder) {
    return null;
  }

  @Override
  public Class<String> getBasicClass() {
    return null;
  }

  @Override
  public Class<Name> getHolderClass() {
    return null;
  }

  public static _Name getSingletonInternal() {
    return null;
  }
}
