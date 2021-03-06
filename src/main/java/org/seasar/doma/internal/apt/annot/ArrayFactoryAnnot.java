package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ArrayFactoryAnnot extends AbstractAnnot {

  public static final String TYPE_NAME = "typeName";

  private final AnnotationValue typeName;

  ArrayFactoryAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.typeName = assertNotNullValue(values, TYPE_NAME);
  }

  public String getTypeNameValue() {
    var result = AnnotationValueUtil.toString(typeName);
    if (result == null) {
      throw new AptIllegalStateException(TYPE_NAME);
    }
    return result;
  }
}
