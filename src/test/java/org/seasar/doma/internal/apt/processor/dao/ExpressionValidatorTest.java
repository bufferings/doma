package org.seasar.doma.internal.apt.processor.dao;

import java.util.Map;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.expr.ExpressionValidationException;
import org.seasar.doma.internal.apt.expr.ExpressionValidator;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.internal.apt.processor.entity.Person;
import org.seasar.doma.internal.expr.ExpressionParser;
import org.seasar.doma.message.Message;

public class ExpressionValidatorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addSourcePath("src/main/java");
    addSourcePath("src/test/java");
  }

  public void testVariable_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("notFound").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4067, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethod_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.equals(emp)").parse();
              var result = validator.validate(node);
              assertTrue(result.isBooleanType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethod_overriderFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("\"aaa\".equals(\"aaa\")").parse();
              var result = validator.validate(node);
              assertTrue(result.isBooleanType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethod_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.notFound(1, \"aaa\".length())").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4071, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethod_foundFromCandidates() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.hoge(\"aaa\")").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethod_foundWithSupertype() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.hoge(new java.lang.Integer(1))").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethod_foundWithTypeParameter() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.foo(new java.lang.Integer(1))").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testStaticMethod_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var expression = String.format("@%s@staticMethod(\"aaa\")", Emp.class.getName());
              var node = new ExpressionParser(expression).parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testStaticMethod_classNotFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("@Xxx@staticMethod(\"aaa\")").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4145, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testStaticMethod_methodNotFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var expression = String.format("@%s@getName()", Emp.class.getName());
              var node = new ExpressionParser(expression).parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4146, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testStaticField_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var expression = String.format("@%s@staticField", Emp.class.getName());
              var node = new ExpressionParser(expression).parse();
              var result = validator.validate(node);
              assertTrue(result.isTextType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testStaticField_classNotFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("@Xxx@staticField").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4145, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testStaticField_fieldNotFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var expression = String.format("@%s@name", Emp.class.getName());
              var node = new ExpressionParser(expression).parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4148, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFunction_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(ExpressionFunctions.class);
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("@prefix(emp.name)").parse();
              var result = validator.validate(node);
              assertTrue(result.isTextType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFunction_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(ExpressionFunctions.class);
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("@hoge(emp.name)").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4072, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testCustomFunction_found() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(ExpressionFunctions.class);
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new ExpressionValidator(
                      ctx, parameterTypeMap, MyExpressionFunctions.class.getName());

              var node = new ExpressionParser("@hello(emp.name)").parse();
              var result = validator.validate(node);
              assertTrue(result.isTextType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testCustomFunction_superClassMethodFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(ExpressionFunctions.class);
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new ExpressionValidator(
                      ctx, parameterTypeMap, MyExpressionFunctions.class.getName());

              var node = new ExpressionParser("@prefix(emp.name)").parse();
              var result = validator.validate(node);
              assertTrue(result.isTextType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testCustomFunction_classNotfound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(ExpressionFunctions.class);
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap, "nonExistent");

              var node = new ExpressionParser("@hello(emp.name)").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4189, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testCustomFunction_classIllegal() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(ExpressionFunctions.class);
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap, "java.lang.String");

              var node = new ExpressionParser("@hello(emp.name)").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4190, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testCustomFunction_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(ExpressionFunctions.class);
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator =
                  new ExpressionValidator(
                      ctx, parameterTypeMap, MyExpressionFunctions.class.getName());

              var node = new ExpressionParser("@hoge(emp.name)").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4072, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testConstructor_notFound() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("new java.lang.String(1, 2)").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4115, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFieldAccess() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.id").parse();
              var result = validator.validate(node);
              assertFalse(result.isUnknownType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFieldAccess_optional() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.name").parse();
              var result = validator.validate(node);
              assertTrue(result.isTextType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFieldAccess_optionalInt() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.age").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFieldAccess_optionalLong() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.salary").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFieldAccess_optionalDouble() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.temperature").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFieldAccess_static_optional() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.staticName").parse();
              var result = validator.validate(node);
              assertTrue(result.isTextType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testFieldAccess_static_optionalInt() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.staticAge").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethodAccess() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.getId()").parse();
              var result = validator.validate(node);
              assertFalse(result.isUnknownType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethodAccess_optional() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.getName()").parse();
              var result = validator.validate(node);
              assertTrue(result.isTextType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethodAccess_optionalInt() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.getAge()").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethodAccess_static_optional() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.getStaticName()").parse();
              var result = validator.validate(node);
              assertTrue(result.isTextType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethodAccess_static_optionalInt() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement =
                  ctx.getElements().getMethodElement(target, "testPerson", Person.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("person.getStaticAge()").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testConstructorAccess() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.id == new java.lang.Integer(1)").parse();
              var result = validator.validate(node);
              assertFalse(result.isUnknownType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testMethodAccess_withArguments() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.add(2, 3)").parse();
              var result = validator.validate(node);
              assertFalse(result.isUnknownType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testEqOperator() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("emp.add(2, 3) == 5").parse();
              var result = validator.validate(node);
              assertFalse(result.isUnknownType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testUnreferencedParameter() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("true").parse();
              validator.validate(node);
              assertFalse(validator.getValidatedParameterNames().contains("emp"));
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testAddOperator_number_number() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("1 + 2").parse();
              var result = validator.validate(node);
              assertTrue(result.isNumberType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testAddOperator_number_text() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("1 + \"2\"").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4121, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testAddOperator_text_text() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("\"1\" + \"2\"").parse();
              var result = validator.validate(node);
              assertTrue(result.isTextType());
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testAddOperator_text_number() throws Exception {
    Class<?> target = ExpressionValidationDao.class;
    addCompilationUnit(target);
    addProcessor(
        new AptProcessor(
            ctx -> {
              var methodElement = ctx.getElements().getMethodElement(target, "testEmp", Emp.class);
              Map<String, TypeMirror> parameterTypeMap =
                  ctx.getElements().getParameterTypeMap(methodElement);
              var validator = new ExpressionValidator(ctx, parameterTypeMap);

              var node = new ExpressionParser("\"2\" + 1").parse();
              try {
                validator.validate(node);
                fail();
              } catch (ExpressionValidationException expected) {
                System.out.println(expected);
                assertEquals(Message.DOMA4126, expected.getMessageResource());
              }
            }));
    compile();
    assertTrue(getCompiledResult());
  }
}
