package org.seasar.doma.internal.apt.processor.dao;

import example.holder.PhoneNumber;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao(config = MyConfig.class)
public interface HolderParameterDao {

  @Select
  Emp select(PhoneNumber phoneNumber);
}
