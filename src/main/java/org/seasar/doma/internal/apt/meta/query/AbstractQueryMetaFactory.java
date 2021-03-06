package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AbstractAnnot;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.message.Message;

public abstract class AbstractQueryMetaFactory<M extends AbstractQueryMeta>
    implements QueryMetaFactory {

  protected final Context ctx;

  protected final ExecutableElement methodElement;

  protected final QueryReturnMetaFactory queryReturnMetaFactory;

  protected final SqlAnnot sqlAnnot;

  protected AbstractQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    assertNotNull(ctx, methodElement);
    this.ctx = ctx;
    this.methodElement = methodElement;
    this.queryReturnMetaFactory = new QueryReturnMetaFactory(ctx, methodElement);
    this.sqlAnnot = ctx.getAnnots().newSqlAnnot(methodElement);
  }

  protected void doAnnotation(M queryMeta, AbstractAnnot targetAnnot) {
    var sqlAnnot = ctx.getAnnots().newSqlAnnot(methodElement);
    if (sqlAnnot == null) {
      return;
    }
    throw new AptException(
        Message.DOMA4443,
        methodElement,
        sqlAnnot.getAnnotationMirror(),
        new Object[] {targetAnnot.getAnnotationMirror()});
  }

  protected void doTypeParameters(M queryMeta) {
    for (TypeParameterElement element : methodElement.getTypeParameters()) {
      var name = ctx.getTypes().getTypeParameterName(element.asType());
      queryMeta.addTypeParameterName(name);
    }
  }

  protected abstract void doReturnType(M queryMeta);

  protected abstract void doParameters(M queryMeta);

  protected void doThrowTypes(M queryMeta) {
    for (TypeMirror thrownType : methodElement.getThrownTypes()) {
      queryMeta.addThrownTypeName(ctx.getTypes().getTypeName(thrownType));
    }
  }

  protected void validateEntityPropertyNames(
      TypeMirror entityType,
      AnnotationMirror annotationMirror,
      AnnotationValue includeValue,
      AnnotationValue excludeValue) {
    List<String> includedPropertyNames =
        Objects.requireNonNullElse(
            AnnotationValueUtil.toStringList(includeValue), Collections.emptyList());
    List<String> excludedPropertyNames =
        Objects.requireNonNullElse(
            AnnotationValueUtil.toStringList(excludeValue), Collections.emptyList());
    if (!includedPropertyNames.isEmpty() || !excludedPropertyNames.isEmpty()) {
      var collector = new EntityPropertyNameCollector(ctx);
      var names = collector.collect(entityType);
      for (var included : includedPropertyNames) {
        if (!names.contains(included)) {
          throw new AptException(
              Message.DOMA4084,
              methodElement,
              annotationMirror,
              includeValue,
              new Object[] {included, entityType});
        }
      }
      for (var excluded : excludedPropertyNames) {
        if (!names.contains(excluded)) {
          throw new AptException(
              Message.DOMA4085,
              methodElement,
              annotationMirror,
              excludeValue,
              new Object[] {excluded, entityType});
        }
      }
    }
  }

  protected QueryReturnMeta createReturnMeta() {
    return queryReturnMetaFactory.createQueryReturnMeta();
  }

  protected QueryParameterMeta createParameterMeta(VariableElement parameterElement) {
    var factory = new QueryParameterMetaFactory(ctx, parameterElement);
    return factory.createQueryParameterMeta();
  }

  protected TypeElement getDaoElement() {
    var element = methodElement.getEnclosingElement();
    var typeElement = ctx.getElements().toTypeElement(element);
    if (typeElement == null) {
      throw new AptIllegalStateException(methodElement.toString());
    }
    return typeElement;
  }
}
