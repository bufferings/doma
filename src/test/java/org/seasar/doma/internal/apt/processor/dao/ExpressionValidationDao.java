package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.internal.apt.processor.entity.Person;

public interface ExpressionValidationDao {

  void testEmp(Emp emp);

  void testPerson(Person person);
}
