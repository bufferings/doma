package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.Embeddable;
import org.seasar.doma.EntityField;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AllArgsConstructorAnnot;
import org.seasar.doma.internal.apt.annot.EmbeddableAnnot;
import org.seasar.doma.internal.apt.annot.ValueAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.message.Message;

public class EmbeddableMetaFactory implements TypeElementMetaFactory<EmbeddableMeta> {

  private final Context ctx;

  private final TypeElement embeddableElement;

  private final EmbeddableAnnot embeddableAnnot;

  private boolean error;

  public EmbeddableMetaFactory(Context ctx, TypeElement embeddableElement) {
    assertNotNull(ctx, embeddableElement);
    this.ctx = ctx;
    this.embeddableElement = embeddableElement;
    embeddableAnnot = ctx.getAnnots().newEmbeddableAnnot(embeddableElement);
    if (embeddableAnnot == null) {
      throw new AptIllegalStateException("embeddableAnnot");
    }
  }

  @Override
  public EmbeddableMeta createTypeElementMeta() {
    var embeddableMeta = new EmbeddableMeta(embeddableAnnot, embeddableElement);
    var strategy = createStrategy(embeddableMeta);
    strategy.doClass(embeddableMeta);
    strategy.doFields(embeddableMeta);
    strategy.doConstructor(embeddableMeta);
    return error ? null : embeddableMeta;
  }

  private Strategy createStrategy(EmbeddableMeta embeddableMeta) {
    var valueAnnot = ctx.getAnnots().newValueAnnot(embeddableElement);
    if (valueAnnot != null) {
      return new ValueStrategy(valueAnnot);
    }
    var allArgsConstructorAnnot = ctx.getAnnots().newAllArgsConstructorAnnot(embeddableElement);
    if (allArgsConstructorAnnot != null) {
      return new AllArgsConstructorStrategy(allArgsConstructorAnnot);
    }
    return new DefaultStrategy();
  }

  private interface Strategy {

    void doClass(EmbeddableMeta embeddableMeta);

    void doFields(EmbeddableMeta embeddableMeta);

    void doConstructor(EmbeddableMeta embeddableMeta);
  }

  private class DefaultStrategy implements Strategy {

    @Override
    public void doClass(EmbeddableMeta embeddableMeta) {
      validateClass(embeddableMeta);
    }

    public void validateClass(EmbeddableMeta embeddableMeta) {
      if (embeddableElement.getKind() != ElementKind.CLASS) {
        var embeddableAnnot = embeddableMeta.getEmbeddableAnnot();
        throw new AptException(
            Message.DOMA4283, embeddableElement, embeddableAnnot.getAnnotationMirror());
      }
      if (!embeddableElement.getTypeParameters().isEmpty()) {
        throw new AptException(Message.DOMA4285, embeddableElement);
      }
      validateEnclosingElement(embeddableElement);
    }

    private void validateEnclosingElement(Element element) {
      var typeElement = ctx.getElements().toTypeElement(element);
      if (typeElement == null) {
        return;
      }
      var simpleName = typeElement.getSimpleName().toString();
      if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
          || simpleName.contains(Constants.DESC_NAME_DELIMITER)) {
        throw new AptException(
            Message.DOMA4417, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
      var nestingKind = typeElement.getNestingKind();
      if (nestingKind == NestingKind.TOP_LEVEL) {
        //noinspection UnnecessaryReturnStatement
        return;
      } else if (nestingKind == NestingKind.MEMBER) {
        var modifiers = typeElement.getModifiers();
        if (modifiers.containsAll(Arrays.asList(Modifier.STATIC, Modifier.PUBLIC))) {
          validateEnclosingElement(typeElement.getEnclosingElement());
        } else {
          throw new AptException(
              Message.DOMA4415, typeElement, new Object[] {typeElement.getQualifiedName()});
        }
      } else {
        throw new AptException(
            Message.DOMA4416, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
    }

    @Override
    public void doFields(EmbeddableMeta embeddableMeta) {
      for (var fieldElement : getFieldElements(embeddableElement)) {
        try {
          if (fieldElement.getAnnotation(Transient.class) != null) {
            //noinspection UnnecessaryContinue
            continue;
          } else if (fieldElement.getModifiers().contains(Modifier.STATIC)) {
            //noinspection UnnecessaryContinue
            continue;
          } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
            throw new AptException(Message.DOMA4286, fieldElement);
          } else if (fieldElement.getAnnotation(Id.class) != null) {
            throw new AptException(Message.DOMA4289, fieldElement);
          } else if (fieldElement.getAnnotation(Version.class) != null) {
            throw new AptException(Message.DOMA4290, fieldElement);
          } else if (fieldElement.getAnnotation(GeneratedValue.class) != null) {
            throw new AptException(Message.DOMA4291, fieldElement);
          } else {
            doEmbeddablePropertyMeta(fieldElement, embeddableMeta);
          }
        } catch (AptException e) {
          ctx.getNotifier().send(e);
          error = true;
        }
      }
    }

    private void doEmbeddablePropertyMeta(
        VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
      validateFieldAnnotation(fieldElement, embeddableMeta);
      var propertyMetaFactory = new EmbeddablePropertyMetaFactory(ctx, fieldElement);
      var propertyMeta = propertyMetaFactory.createEmbeddablePropertyMeta();
      embeddableMeta.addEmbeddablePropertyMeta(propertyMeta);
    }

    private List<VariableElement> getFieldElements(TypeElement embeddableElement) {
      return ctx.getElements()
          .getUnhiddenFields(embeddableElement, t -> t.getAnnotation(Embeddable.class) != null);
    }

    private void validateFieldAnnotation(
        VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
      TypeElement foundAnnotationTypeElement = null;
      for (AnnotationMirror annotation : fieldElement.getAnnotationMirrors()) {
        var declaredType = annotation.getAnnotationType();
        var typeElement = ctx.getTypes().toTypeElement(declaredType);
        if (typeElement.getAnnotation(EntityField.class) != null) {
          if (foundAnnotationTypeElement != null) {
            throw new AptException(
                Message.DOMA4288,
                fieldElement,
                new Object[] {
                  foundAnnotationTypeElement.getQualifiedName(), typeElement.getQualifiedName()
                });
          }
          foundAnnotationTypeElement = typeElement;
        }
      }
    }

    @Override
    public void doConstructor(EmbeddableMeta embeddableMeta) {
      if (embeddableMeta.isAbstract()) {
        return;
      }
      var constructorMeta = getConstructorMeta(embeddableElement, embeddableMeta);
      if (constructorMeta == null) {
        throw new AptException(Message.DOMA4293, embeddableElement);
      }
      if (constructorMeta.getConstructorElement().getModifiers().contains(Modifier.PRIVATE)) {
        throw new AptException(Message.DOMA4294, embeddableElement);
      }
      embeddableMeta.setConstructorMeta(constructorMeta);
    }

    private EmbeddableConstructorMeta getConstructorMeta(
        TypeElement embeddableElement, EmbeddableMeta embeddableMeta) {
      Map<String, EmbeddablePropertyMeta> propertyMetaMap = new HashMap<>();
      for (var propertyMeta : embeddableMeta.getEmbeddablePropertyMetas()) {
        propertyMetaMap.put(propertyMeta.getName(), propertyMeta);
      }
      outer:
      for (var constructor :
          ElementFilter.constructorsIn(embeddableElement.getEnclosedElements())) {
        List<EmbeddablePropertyMeta> propertyMetaList = new ArrayList<>();
        for (VariableElement param : constructor.getParameters()) {
          var name = ctx.getElements().getParameterName(param);
          var paramType = param.asType();
          var propertyMeta = propertyMetaMap.get(name);
          if (propertyMeta == null) {
            continue outer;
          }
          var propertyType = propertyMeta.getType();
          if (!ctx.getTypes().isSameType(paramType, propertyType)) {
            continue outer;
          }
          propertyMetaList.add(propertyMeta);
        }
        if (propertyMetaMap.size() == propertyMetaList.size()) {
          return new EmbeddableConstructorMeta(constructor, propertyMetaList);
        }
      }
      return null;
    }
  }

  private class AllArgsConstructorStrategy extends DefaultStrategy {

    private final AllArgsConstructorAnnot allArgsConstructorAnnot;

    public AllArgsConstructorStrategy(AllArgsConstructorAnnot allArgsConstructorAnnot) {
      assertNotNull(allArgsConstructorAnnot);
      this.allArgsConstructorAnnot = allArgsConstructorAnnot;
    }

    @Override
    public void doConstructor(EmbeddableMeta embeddableMeta) {
      if (!allArgsConstructorAnnot.getStaticNameValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4424,
            embeddableElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getStaticName());
      }
      if (allArgsConstructorAnnot.isAccessPrivate()) {
        throw new AptException(
            Message.DOMA4425,
            embeddableElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess());
      }
      if (allArgsConstructorAnnot.isAccessNone()) {
        throw new AptException(
            Message.DOMA4427,
            embeddableElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess());
      }
    }
  }

  private class ValueStrategy extends DefaultStrategy {

    private final ValueAnnot valueAnnot;

    public ValueStrategy(ValueAnnot valueAnnot) {
      assertNotNull(valueAnnot);
      this.valueAnnot = valueAnnot;
    }

    @Override
    public void doConstructor(EmbeddableMeta embeddableMeta) {
      if (!valueAnnot.getStaticConstructorValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4423,
            embeddableElement,
            valueAnnot.getAnnotationMirror(),
            valueAnnot.getStaticConstructor());
      }
    }
  }
}
