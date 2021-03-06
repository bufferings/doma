package org.seasar.doma.internal.apt.expr;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.expr.node.AddOperatorNode;
import org.seasar.doma.internal.expr.node.AndOperatorNode;
import org.seasar.doma.internal.expr.node.ArithmeticOperatorNode;
import org.seasar.doma.internal.expr.node.CommaOperatorNode;
import org.seasar.doma.internal.expr.node.ComparisonOperatorNode;
import org.seasar.doma.internal.expr.node.DivideOperatorNode;
import org.seasar.doma.internal.expr.node.EmptyNode;
import org.seasar.doma.internal.expr.node.EqOperatorNode;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.expr.node.ExpressionNodeVisitor;
import org.seasar.doma.internal.expr.node.FieldOperatorNode;
import org.seasar.doma.internal.expr.node.FunctionOperatorNode;
import org.seasar.doma.internal.expr.node.GeOperatorNode;
import org.seasar.doma.internal.expr.node.GtOperatorNode;
import org.seasar.doma.internal.expr.node.LeOperatorNode;
import org.seasar.doma.internal.expr.node.LiteralNode;
import org.seasar.doma.internal.expr.node.LogicalBinaryOperatorNode;
import org.seasar.doma.internal.expr.node.LtOperatorNode;
import org.seasar.doma.internal.expr.node.MethodOperatorNode;
import org.seasar.doma.internal.expr.node.ModOperatorNode;
import org.seasar.doma.internal.expr.node.MultiplyOperatorNode;
import org.seasar.doma.internal.expr.node.NeOperatorNode;
import org.seasar.doma.internal.expr.node.NewOperatorNode;
import org.seasar.doma.internal.expr.node.NotOperatorNode;
import org.seasar.doma.internal.expr.node.OrOperatorNode;
import org.seasar.doma.internal.expr.node.ParensNode;
import org.seasar.doma.internal.expr.node.StaticFieldOperatorNode;
import org.seasar.doma.internal.expr.node.StaticMethodOperatorNode;
import org.seasar.doma.internal.expr.node.SubtractOperatorNode;
import org.seasar.doma.internal.expr.node.VariableNode;
import org.seasar.doma.message.Message;

public class ExpressionValidator implements ExpressionNodeVisitor<TypeDeclaration, Void> {

  private final Context ctx;

  private final Map<String, TypeMirror> originalParameterTypeMap;

  private final Map<String, TypeMirror> parameterTypeMap;

  private final Set<String> validatedParameterNames;

  private final TypeDeclaration unknownTypeDeclaration;

  private final String exprFunctionsClassName;

  public ExpressionValidator(Context ctx, Map<String, TypeMirror> parameterTypeMap) {
    this(ctx, parameterTypeMap, ctx.getOptions().getExprFunctions());
  }

  public ExpressionValidator(
      Context ctx, Map<String, TypeMirror> parameterTypeMap, String exprFunctionsClassName) {
    assertNotNull(ctx, parameterTypeMap);
    this.ctx = ctx;
    this.originalParameterTypeMap = parameterTypeMap;
    this.parameterTypeMap = new HashMap<>(parameterTypeMap);
    this.validatedParameterNames = new HashSet<>();
    this.unknownTypeDeclaration = ctx.getDeclarations().newUnknownTypeDeclaration();
    this.exprFunctionsClassName = exprFunctionsClassName;
  }

  public TypeMirror removeParameterType(String parameterName) {
    return parameterTypeMap.remove(parameterName);
  }

  public void putParameterType(String parameterName, TypeMirror parameterType) {
    parameterTypeMap.put(parameterName, parameterType);
  }

  public void recognizeFirstParameterValid() {
    var it = originalParameterTypeMap.keySet().iterator();
    if (it.hasNext()) {
      validatedParameterNames.add(it.next());
    }
  }

  public Set<String> getValidatedParameterNames() {
    return validatedParameterNames;
  }

  public TypeDeclaration validate(ExpressionNode node) {
    return validateInternal(node);
  }

  private TypeDeclaration validateInternal(ExpressionNode node) {
    return node.accept(this, null);
  }

  public void checkNonReferredParameters(
      ExecutableElement methodElement, BiConsumer<VariableElement, String> consumer) {
    var validatedParameterNames = getValidatedParameterNames();
    for (var parameterName : originalParameterTypeMap.keySet()) {
      if (!validatedParameterNames.contains(parameterName)) {
        for (VariableElement parameterElement : methodElement.getParameters()) {
          if (parameterElement.getSimpleName().contentEquals(parameterName)) {
            consumer.accept(parameterElement, parameterName);
          }
        }
      }
    }
  }

  @Override
  public TypeDeclaration visitEqOperatorNode(EqOperatorNode node, Void p) {
    return handleNullAvailableComparisonOperation(node, p);
  }

  @Override
  public TypeDeclaration visitNeOperatorNode(NeOperatorNode node, Void p) {
    return handleNullAvailableComparisonOperation(node, p);
  }

  @Override
  public TypeDeclaration visitGeOperatorNode(GeOperatorNode node, Void p) {
    return handleNullUnavailableComparisonOperation(node, p);
  }

  @Override
  public TypeDeclaration visitGtOperatorNode(GtOperatorNode node, Void p) {
    return handleNullUnavailableComparisonOperation(node, p);
  }

  @Override
  public TypeDeclaration visitLeOperatorNode(LeOperatorNode node, Void p) {
    return handleNullUnavailableComparisonOperation(node, p);
  }

  @Override
  public TypeDeclaration visitLtOperatorNode(LtOperatorNode node, Void p) {
    return handleNullUnavailableComparisonOperation(node, p);
  }

  private TypeDeclaration handleNullAvailableComparisonOperation(
      ComparisonOperatorNode node, Void p) {
    var left = node.getLeftNode().accept(this, p);
    var right = node.getRightNode().accept(this, p);
    if (left.isNullType() || right.isNullType() || left.isComparable(right)) {
      return ctx.getDeclarations().newTypeDeclaration(boolean.class);
    }
    var location = node.getLocation();
    throw new TypeNotCompatibleException(
        location.getExpression(),
        location.getPosition(),
        node.getExpression(),
        node.getLeftNode().toString(),
        left.getBinaryName(),
        node.getRightNode().toString(),
        right.getBinaryName());
  }

  private TypeDeclaration handleNullUnavailableComparisonOperation(
      ComparisonOperatorNode node, Void p) {
    var left = node.getLeftNode().accept(this, p);
    var right = node.getRightNode().accept(this, p);
    if (left.isNullType() || right.isNullType()) {
      var location = node.getLocation();
      throw new NullNotComparableException(
          location.getExpression(), location.getPosition(), node.getExpression());
    }
    if (left.isComparable(right)) {
      return ctx.getDeclarations().newTypeDeclaration(boolean.class);
    }
    var location = node.getLocation();
    throw new TypeNotCompatibleException(
        location.getExpression(),
        location.getPosition(),
        node.getExpression(),
        node.getLeftNode().toString(),
        left.getBinaryName(),
        node.getRightNode().toString(),
        right.getBinaryName());
  }

  @Override
  public TypeDeclaration visitAndOperatorNode(AndOperatorNode node, Void p) {
    return handleLogicalBinaryOperatorNode(node, p);
  }

  @Override
  public TypeDeclaration visitOrOperatorNode(OrOperatorNode node, Void p) {
    return handleLogicalBinaryOperatorNode(node, p);
  }

  private TypeDeclaration handleLogicalBinaryOperatorNode(LogicalBinaryOperatorNode node, Void p) {
    var left = node.getLeftNode().accept(this, p);
    var right = node.getRightNode().accept(this, p);
    if (!left.isBooleanType()) {
      var location = node.getLocation();
      throw new LeftOperandNotBooleanException(
          location.getExpression(),
          location.getPosition(),
          node.getExpression(),
          node.getLeftNode().toString(),
          left.getBinaryName());
    }
    if (!right.isBooleanType()) {
      var location = node.getLocation();
      throw new RightOperandNotBooleanException(
          location.getExpression(),
          location.getPosition(),
          node.getExpression(),
          node.getRightNode().toString(),
          right.getBinaryName());
    }
    return ctx.getDeclarations().newTypeDeclaration(boolean.class);
  }

  @Override
  public TypeDeclaration visitNotOperatorNode(NotOperatorNode node, Void p) {
    var result = node.getNode().accept(this, p);
    if (result.isBooleanType()) {
      return ctx.getDeclarations().newTypeDeclaration(boolean.class);
    }
    var location = node.getLocation();
    throw new NotOperatorException(
        location.getExpression(),
        location.getPosition(),
        node.getExpression(),
        node.getNode().toString(),
        result.getBinaryName());
  }

  @Override
  public TypeDeclaration visitAddOperatorNode(AddOperatorNode node, Void p) {
    var left = node.getLeftNode().accept(this, p);
    var right = node.getRightNode().accept(this, p);
    if (left.isTextType()) {
      if (right.isTextType()) {
        return left.emulateConcatOperation(right);
      }
      var location = node.getLocation();
      throw new AddOperatorException(
          location.getExpression(),
          location.getPosition(),
          node.getExpression(),
          node.getLeftNode().toString(),
          left.getBinaryName());
    }
    return handleArithmeticOperatorNode(node, left, right, p);
  }

  @Override
  public TypeDeclaration visitSubtractOperatorNode(SubtractOperatorNode node, Void p) {
    var left = node.getLeftNode().accept(this, p);
    var right = node.getRightNode().accept(this, p);
    return handleArithmeticOperatorNode(node, left, right, p);
  }

  @Override
  public TypeDeclaration visitMultiplyOperatorNode(MultiplyOperatorNode node, Void p) {
    var left = node.getLeftNode().accept(this, p);
    var right = node.getRightNode().accept(this, p);
    return handleArithmeticOperatorNode(node, left, right, p);
  }

  @Override
  public TypeDeclaration visitDivideOperatorNode(DivideOperatorNode node, Void p) {
    var left = node.getLeftNode().accept(this, p);
    var right = node.getRightNode().accept(this, p);
    return handleArithmeticOperatorNode(node, left, right, p);
  }

  @Override
  public TypeDeclaration visitModOperatorNode(ModOperatorNode node, Void p) {
    var left = node.getLeftNode().accept(this, p);
    var right = node.getRightNode().accept(this, p);
    return handleArithmeticOperatorNode(node, left, right, p);
  }

  private TypeDeclaration handleArithmeticOperatorNode(
      ArithmeticOperatorNode node, TypeDeclaration left, TypeDeclaration right, Void p) {
    if (!left.isNumberType()) {
      var location = node.getLocation();
      throw new LeftOperandNotNumericException(
          location.getExpression(),
          location.getPosition(),
          node.getExpression(),
          node.getLeftNode().toString(),
          left.getBinaryName());
    }
    if (!right.isNumberType()) {
      var location = node.getLocation();
      throw new RightOperandNotNumericException(
          location.getExpression(),
          location.getPosition(),
          node.getExpression(),
          node.getRightNode().toString(),
          right.getBinaryName());
    }
    return left.emulateArithmeticOperation(right);
  }

  @Override
  public TypeDeclaration visitLiteralNode(LiteralNode node, Void p) {
    var type =
        node.getValueClass() == void.class
            ? ctx.getTypes().getNullType()
            : ctx.getTypes().getType(node.getValueClass());
    return ctx.getDeclarations().newTypeDeclaration(type);
  }

  @Override
  public TypeDeclaration visitParensNode(ParensNode node, Void p) {
    return node.getNode().accept(this, p);
  }

  @Override
  public TypeDeclaration visitNewOperatorNode(NewOperatorNode node, Void p) {
    node.getParametersNode().accept(this, p);

    var parameterTypeDeclarations = new ParameterCollector().collect(node.getParametersNode());

    var className = node.getClassName();
    var typeElement = ctx.getElements().getTypeElement(className);
    if (typeElement == null) {
      var location = node.getLocation();
      throw new InstantiationClassNotFoundException(
          location.getExpression(), location.getPosition(), className);
    }
    var typeDeclaration = ctx.getDeclarations().newTypeDeclaration(typeElement.asType());
    var constructorDeclarations =
        typeDeclaration.getConstructorDeclarations(parameterTypeDeclarations);
    if (constructorDeclarations.size() == 0) {
      var location = node.getLocation();
      var signature = createConstructorSignature(className, parameterTypeDeclarations);
      throw new ConstructorNotFoundException(
          location.getExpression(), location.getPosition(), signature);
    }
    if (constructorDeclarations.size() == 1) {
      return typeDeclaration;
    }
    var location = node.getLocation();
    var signature = createConstructorSignature(className, parameterTypeDeclarations);
    throw new MultipleConstructorsFoundException(
        location.getExpression(), location.getPosition(), signature);
  }

  private String createConstructorSignature(
      String className, List<TypeDeclaration> parameterTypeDeclarations) {
    var buf = new StringBuilder();
    buf.append(className);
    buf.append("(");
    if (parameterTypeDeclarations.size() > 0) {
      for (var declaration : parameterTypeDeclarations) {
        buf.append(declaration.getType());
        buf.append(", ");
      }
      buf.setLength(buf.length() - 2);
    }
    buf.append(")");
    return buf.toString();
  }

  @Override
  public TypeDeclaration visitCommaOperatorNode(CommaOperatorNode node, Void p) {
    return unknownTypeDeclaration;
  }

  @Override
  public TypeDeclaration visitEmptyNode(EmptyNode node, Void p) {
    return unknownTypeDeclaration;
  }

  @Override
  public TypeDeclaration visitMethodOperatorNode(MethodOperatorNode node, Void p) {
    var typeDeclaration = node.getTargetObjectNode().accept(this, p);
    var parameterTypeDeclarations = new ParameterCollector().collect(node.getParametersNode());
    var methodName = node.getMethodName();
    var methodDeclarations =
        typeDeclaration.getMethodDeclarations(methodName, parameterTypeDeclarations);
    if (methodDeclarations.size() == 0) {
      var location = node.getLocation();
      var methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
      throw new MethodNotFoundException(
          location.getExpression(),
          location.getPosition(),
          node.getTargetObjectNode().getExpression(),
          typeDeclaration.getBinaryName(),
          methodSignature);
    }
    if (methodDeclarations.size() == 1) {
      var methodDeclaration = methodDeclarations.get(0);
      var returnTypeDeclaration = methodDeclaration.getReturnTypeDeclaration();
      if (returnTypeDeclaration != null) {
        return convertIfOptional(returnTypeDeclaration);
      }
    }
    var location = node.getLocation();
    var methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
    throw new MultipleMethodsFoundException(
        location.getExpression(),
        location.getPosition(),
        node.getTargetObjectNode().getExpression(),
        typeDeclaration.getBinaryName(),
        methodSignature);
  }

  @Override
  public TypeDeclaration visitStaticMethodOperatorNode(StaticMethodOperatorNode node, Void p) {
    var className = node.getClassName();
    var typeElement = ctx.getElements().getTypeElement(node.getClassName());
    if (typeElement == null) {
      var location = node.getLocation();
      throw new ClassNotFoundException(location.getExpression(), location.getPosition(), className);
    }
    var typeDeclaration = ctx.getDeclarations().newTypeDeclaration(typeElement.asType());
    var parameterTypeDeclarations = new ParameterCollector().collect(node.getParametersNode());
    var methodName = node.getMethodName();
    var methodDeclarations =
        typeDeclaration.getStaticMethodDeclarations(methodName, parameterTypeDeclarations);
    if (methodDeclarations.size() == 0) {
      var location = node.getLocation();
      var methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
      throw new StaticMethodNotFoundException(
          location.getExpression(), location.getPosition(), className, methodSignature);
    }
    if (methodDeclarations.size() == 1) {
      var methodDeclaration = methodDeclarations.get(0);
      var returnTypeDeclaration = methodDeclaration.getReturnTypeDeclaration();
      if (returnTypeDeclaration != null) {
        return convertIfOptional(returnTypeDeclaration);
      }
    }
    var location = node.getLocation();
    var methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
    throw new MultipleStaticMethodsFoundException(
        location.getExpression(), location.getPosition(), className, methodSignature);
  }

  @Override
  public TypeDeclaration visitFunctionOperatorNode(FunctionOperatorNode node, Void p) {
    var typeDeclaration = getExpressionFunctionsDeclaration(node);
    var parameterTypeDeclarations = new ParameterCollector().collect(node.getParametersNode());
    var methodName = node.getMethodName();
    var methodDeclarations =
        typeDeclaration.getMethodDeclarations(methodName, parameterTypeDeclarations);
    if (methodDeclarations.size() == 0) {
      var location = node.getLocation();
      var methodSignature = createMethodSignature(methodName, parameterTypeDeclarations);
      throw new FunctionNotFoundException(
          location.getExpression(), location.getPosition(), methodSignature);
    }
    if (methodDeclarations.size() == 1) {
      var methodDeclaration = methodDeclarations.get(0);
      var returnTypeDeclaration = methodDeclaration.getReturnTypeDeclaration();
      if (returnTypeDeclaration != null) {
        return returnTypeDeclaration;
      }
    }
    throw new AptIllegalStateException(methodName);
  }

  private TypeDeclaration getExpressionFunctionsDeclaration(FunctionOperatorNode node) {
    if (exprFunctionsClassName == null) {
      return ctx.getDeclarations().newTypeDeclaration(ExpressionFunctions.class);
    }
    var element = ctx.getElements().getTypeElement(exprFunctionsClassName);
    if (element == null) {
      var location = node.getLocation();
      throw new ExpressionFunctionsClassNotFoundException(
          location.getExpression(),
          location.getPosition(),
          node.getMethodName(),
          exprFunctionsClassName);
    }
    var type = element.asType();
    if (!ctx.getTypes().isAssignable(type, ExpressionFunctions.class)) {
      var location = node.getLocation();
      throw new IllegalExpressionFunctionsException(
          location.getExpression(),
          location.getPosition(),
          node.getMethodName(),
          exprFunctionsClassName);
    }
    return ctx.getDeclarations().newTypeDeclaration(type);
  }

  private String createMethodSignature(
      String methodName, List<TypeDeclaration> parameterTypeDeclarations) {
    var buf = new StringBuilder();
    buf.append(methodName);
    buf.append("(");
    if (parameterTypeDeclarations.size() > 0) {
      for (var declaration : parameterTypeDeclarations) {
        buf.append(declaration.getType());
        buf.append(", ");
      }
      buf.setLength(buf.length() - 2);
    }
    buf.append(")");
    return buf.toString();
  }

  @Override
  public TypeDeclaration visitFieldOperatorNode(FieldOperatorNode node, Void p) {
    var typeDeclaration = node.getTargetObjectNode().accept(this, p);
    var fieldName = node.getFieldName();
    var fieldDeclaration = typeDeclaration.getFieldDeclaration(fieldName);
    if (fieldDeclaration != null) {
      var fieldTypeDeclaration = fieldDeclaration.getTypeDeclaration();
      if (fieldTypeDeclaration != null) {
        return convertIfOptional(fieldTypeDeclaration);
      }
    }
    var location = node.getLocation();
    throw new InstanceFieldNotFoundException(
        location.getExpression(),
        location.getPosition(),
        node.getTargetObjectNode().getExpression(),
        typeDeclaration.getBinaryName(),
        fieldName);
  }

  @Override
  public TypeDeclaration visitStaticFieldOperatorNode(StaticFieldOperatorNode node, Void p) {
    var className = node.getClassName();
    var typeElement = ctx.getElements().getTypeElement(node.getClassName());
    if (typeElement == null) {
      var location = node.getLocation();
      throw new ClassNotFoundException(location.getExpression(), location.getPosition(), className);
    }
    var typeDeclaration = ctx.getDeclarations().newTypeDeclaration(typeElement.asType());
    var fieldName = node.getFieldName();
    var fieldDeclaration = typeDeclaration.getStaticFieldDeclaration(fieldName);
    if (fieldDeclaration != null) {
      var fieldTypeDeclaration = fieldDeclaration.getTypeDeclaration();
      if (fieldTypeDeclaration != null) {
        return convertIfOptional(fieldTypeDeclaration);
      }
    }
    var location = node.getLocation();
    throw new StaticFieldNotFoundException(
        location.getExpression(), location.getPosition(), className, fieldName);
  }

  private TypeDeclaration convertIfOptional(TypeDeclaration typeDeclaration) {
    if (typeDeclaration.is(Optional.class)) {
      var typeParameterDeclaration =
          typeDeclaration
              .getTypeParameterDeclarations()
              .stream()
              .findFirst()
              .orElseThrow(() -> new AptIllegalStateException(typeDeclaration.toString()));
      return ctx.getDeclarations().newTypeDeclaration(typeParameterDeclaration.getActualType());
    } else if (typeDeclaration.is(OptionalInt.class)) {
      return ctx.getDeclarations().newTypeDeclaration(Integer.class);
    } else if (typeDeclaration.is(OptionalLong.class)) {
      return ctx.getDeclarations().newTypeDeclaration(Long.class);
    } else if (typeDeclaration.is(OptionalDouble.class)) {
      return ctx.getDeclarations().newTypeDeclaration(Double.class);
    }
    return typeDeclaration;
  }

  @Override
  public TypeDeclaration visitVariableNode(VariableNode node, Void p) {
    var variableName = node.getExpression();
    var type = parameterTypeMap.get(variableName);
    if (type == null) {
      var location = node.getLocation();
      throw new VariableNotFoundException(variableName, location.getPosition());
    }
    validatedParameterNames.add(variableName);
    return ctx.getDeclarations().newTypeDeclaration(type);
  }

  private class ParameterCollector implements ExpressionNodeVisitor<Void, List<TypeDeclaration>> {

    public List<TypeDeclaration> collect(ExpressionNode node) {
      List<TypeDeclaration> results = new ArrayList<>();
      node.accept(this, results);
      return results;
    }

    @Override
    public Void visitEqOperatorNode(EqOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitNeOperatorNode(NeOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitGeOperatorNode(GeOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitGtOperatorNode(GtOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitLeOperatorNode(LeOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitLtOperatorNode(LtOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitCommaOperatorNode(CommaOperatorNode node, List<TypeDeclaration> p) {
      for (var expressionNode : node.getNodes()) {
        expressionNode.accept(this, p);
      }
      return null;
    }

    @Override
    public Void visitLiteralNode(LiteralNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitVariableNode(VariableNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitOrOperatorNode(OrOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitAndOperatorNode(AndOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitNotOperatorNode(NotOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitAddOperatorNode(AddOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitSubtractOperatorNode(SubtractOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitMultiplyOperatorNode(MultiplyOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitDivideOperatorNode(DivideOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitModOperatorNode(ModOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitNewOperatorNode(NewOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitMethodOperatorNode(MethodOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitStaticMethodOperatorNode(
        StaticMethodOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitFunctionOperatorNode(FunctionOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitFieldOperatorNode(FieldOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitStaticFieldOperatorNode(
        StaticFieldOperatorNode node, List<TypeDeclaration> p) {
      validate(node, p);
      return null;
    }

    @Override
    public Void visitParensNode(ParensNode node, List<TypeDeclaration> p) {
      node.getNode().accept(this, p);
      return null;
    }

    @Override
    public Void visitEmptyNode(EmptyNode node, List<TypeDeclaration> p) {
      return null;
    }

    protected void validate(ExpressionNode node, List<TypeDeclaration> p) {
      var result = ExpressionValidator.this.validateInternal(node);
      p.add(result);
    }
  }

  private static class NullNotComparableException extends ExpressionValidationException {
    NullNotComparableException(String expression, int position, String operator) {
      super(Message.DOMA4139, expression, position, operator);
    }
  }

  private static class TypeNotCompatibleException extends ExpressionValidationException {
    TypeNotCompatibleException(
        String expression,
        int position,
        String operator,
        String leftOperand,
        String leftTypeName,
        String rightOperand,
        String rightTypeName) {
      super(
          Message.DOMA4116,
          expression,
          position,
          operator,
          leftOperand,
          leftTypeName,
          rightOperand,
          rightTypeName);
    }
  }

  private static class LeftOperandNotBooleanException extends ExpressionValidationException {
    LeftOperandNotBooleanException(
        String expression, int position, String operator, String operand, String className) {
      super(Message.DOMA4117, expression, position, operator, operand, className);
    }
  }

  private static class RightOperandNotBooleanException extends ExpressionValidationException {
    RightOperandNotBooleanException(
        String expression, int position, String operator, String operand, String className) {
      super(Message.DOMA4118, expression, position, operator, operand, className);
    }
  }

  private static class NotOperatorException extends ExpressionValidationException {
    NotOperatorException(
        String expression, int position, String operator, String operand, String className) {
      super(Message.DOMA4119, expression, position, operator, operand, className);
    }
  }

  private static class AddOperatorException extends ExpressionValidationException {
    AddOperatorException(
        String expression, int position, String operator, String operand, String className) {
      super(Message.DOMA4126, expression, position, operator, operand, className);
    }
  }

  private static class LeftOperandNotNumericException extends ExpressionValidationException {
    LeftOperandNotNumericException(
        String expression, int position, String operator, String operand, String className) {
      super(Message.DOMA4120, expression, position, operator, operand, className);
    }
  }

  private static class RightOperandNotNumericException extends ExpressionValidationException {
    RightOperandNotNumericException(
        String expression, int position, String operator, String operand, String className) {
      super(Message.DOMA4121, expression, position, operator, operand, className);
    }
  }

  private static class InstantiationClassNotFoundException extends ExpressionValidationException {
    InstantiationClassNotFoundException(String expression, int position, String className) {
      super(Message.DOMA4138, expression, position, className);
    }
  }

  private static class ConstructorNotFoundException extends ExpressionValidationException {
    ConstructorNotFoundException(String expression, int position, String signature) {
      super(Message.DOMA4115, expression, position, signature);
    }
  }

  private static class MultipleConstructorsFoundException extends ExpressionValidationException {
    MultipleConstructorsFoundException(String expression, int position, String signature) {
      super(Message.DOMA4127, expression, position, signature);
    }
  }

  private static class MethodNotFoundException extends ExpressionValidationException {
    MethodNotFoundException(
        String expression,
        int position,
        String variableName,
        String className,
        String methodSignature) {
      super(Message.DOMA4071, expression, position, variableName, className, methodSignature);
    }
  }

  private static class MultipleMethodsFoundException extends ExpressionValidationException {
    MultipleMethodsFoundException(
        String expression,
        int position,
        String variableName,
        String className,
        String methodSignature) {
      super(Message.DOMA4073, expression, position, variableName, className, methodSignature);
    }
  }

  private static class StaticMethodNotFoundException extends ExpressionValidationException {
    StaticMethodNotFoundException(
        String expression, int position, String className, String methodSignature) {
      super(Message.DOMA4146, expression, position, className, methodSignature);
    }
  }

  private static class MultipleStaticMethodsFoundException extends ExpressionValidationException {
    MultipleStaticMethodsFoundException(
        String expression, int position, String className, String methodSignature) {
      super(Message.DOMA4147, expression, position, className, methodSignature);
    }
  }

  private static class FunctionNotFoundException extends ExpressionValidationException {
    FunctionNotFoundException(String expression, int position, String methodSignature) {
      super(Message.DOMA4072, expression, position, methodSignature);
    }
  }

  private static class ExpressionFunctionsClassNotFoundException
      extends ExpressionValidationException {
    ExpressionFunctionsClassNotFoundException(
        String expression, int position, String methodName, String expressionFunctionsClassName) {
      super(Message.DOMA4189, expression, position, methodName, expressionFunctionsClassName);
    }
  }

  private static class IllegalExpressionFunctionsException extends ExpressionValidationException {
    IllegalExpressionFunctionsException(
        String expression, int position, String methodName, String expressionFunctionsClassName) {
      super(Message.DOMA4190, expression, position, methodName, expressionFunctionsClassName);
    }
  }

  private static class InstanceFieldNotFoundException extends ExpressionValidationException {
    InstanceFieldNotFoundException(
        String expression, int position, String variableName, String className, String fieldName) {
      super(Message.DOMA4114, expression, position, variableName, className, fieldName);
    }
  }

  private static class ClassNotFoundException extends ExpressionValidationException {
    ClassNotFoundException(String expression, int position, String className) {
      super(Message.DOMA4145, expression, position, className);
    }
  }

  private static class StaticFieldNotFoundException extends ExpressionValidationException {
    StaticFieldNotFoundException(
        String expression, int position, String className, String fieldName) {
      super(Message.DOMA4148, expression, position, className, fieldName);
    }
  }

  private static class VariableNotFoundException extends ExpressionValidationException {
    VariableNotFoundException(String variableName, int position) {
      super(Message.DOMA4067, variableName, position);
    }
  }
}
