package org.seasar.doma.internal.apt;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class ElementsTest extends AptTestCase {

  public void testHierarchy() throws Exception {
    addCompilationUnit(getClass());
    addProcessor(
        new AptProcessor(
            ctx -> {
              var elements = ctx.getElements();
              var arrayListElement = elements.getTypeElement(ArrayList.class);
              var names =
                  elements
                      .hierarchy(arrayListElement)
                      .stream()
                      .map(TypeElement::getQualifiedName)
                      .map(Name::toString)
                      .collect(Collectors.toList());
              assertEquals(4, names.size());
              assertEquals(ArrayList.class.getName(), names.get(0));
              assertEquals(AbstractList.class.getName(), names.get(1));
              assertEquals(AbstractCollection.class.getName(), names.get(2));
              assertEquals(Object.class.getName(), names.get(3));
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testHierarchy_Object() throws Exception {
    addCompilationUnit(getClass());
    addProcessor(
        new AptProcessor(
            ctx -> {
              var elements = ctx.getElements();
              var objectElement = elements.getTypeElement(Object.class);
              var names =
                  elements
                      .hierarchy(objectElement)
                      .stream()
                      .map(TypeElement::getQualifiedName)
                      .map(Name::toString)
                      .collect(Collectors.toList());
              assertEquals(1, names.size());
              assertEquals(Object.class.getName(), names.get(0));
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testUnhiddenFileds() throws Exception {
    addCompilationUnit(getClass());
    addProcessor(
        new AptProcessor(
            ctx -> {
              var elements = ctx.getElements();
              var childTypeElement = elements.getTypeElement(Ccc.class);
              var fields =
                  elements.getUnhiddenFields(
                      childTypeElement, t -> !t.getSimpleName().contentEquals("Bbb"));
              assertEquals(3, fields.size());
              var field0 = fields.get(0);
              assertTrue(field0.getSimpleName().contentEquals("aaa"));
              assertTrue(field0.getEnclosingElement().getSimpleName().contentEquals("Aaa"));
              var field1 = fields.get(1);
              assertTrue(field1.getSimpleName().contentEquals("bbb"));
              assertTrue(field1.getEnclosingElement().getSimpleName().contentEquals("Ccc"));
              var field2 = fields.get(2);
              assertTrue(field2.getSimpleName().contentEquals("ddd"));
              assertTrue(field2.getEnclosingElement().getSimpleName().contentEquals("Ccc"));
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testGetMethodElement() throws Exception {
    addCompilationUnit(getClass());
    addProcessor(
        new AptProcessor(
            ctx -> {
              var elements = ctx.getElements();
              var methodElement = elements.getMethodElement(Ddd.class, "aaa", Integer.class);
              assertNotNull(methodElement);
              assertTrue(methodElement.getSimpleName().contentEquals("aaa"));
              var paramElements = methodElement.getParameters();
              var paramElement = paramElements.get(0);
              assertEquals(
                  Integer.class.getName(), ctx.getTypes().getTypeName(paramElement.asType()));
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public void testGetParameterTypeMap() throws Exception {
    addCompilationUnit(getClass());
    addProcessor(
        new AptProcessor(
            ctx -> {
              var elements = ctx.getElements();
              var methodElement =
                  elements.getMethodElement(
                      Eee.class, "aaa", String.class, Integer.class, Long.class);
              Map<String, TypeMirror> map = elements.getParameterTypeMap(methodElement);
              assertEquals(3, map.size());
              assertEquals(String.class.getName(), ctx.getTypes().getTypeName(map.get("a")));
              assertEquals(Integer.class.getName(), ctx.getTypes().getTypeName(map.get("b")));
              assertEquals(Long.class.getName(), ctx.getTypes().getTypeName(map.get("c")));
            }));
    compile();
    assertTrue(getCompiledResult());
  }

  public static class Aaa {
    String aaa;
    String bbb;
  }

  public static class Bbb extends Aaa {
    String bbb;
    String ccc;
  }

  public static class Ccc extends Bbb {
    String bbb;
    String ddd;
  }

  public static class Ddd {
    void aaa(String a) {}

    void aaa(Integer a) {}
  }

  public static class Eee {
    void aaa(String a, Integer b, Long c) {}
  }
}
