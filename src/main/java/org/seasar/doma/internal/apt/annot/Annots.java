package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.AnnotateWith;
import org.seasar.doma.ArrayFactory;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.BlobFactory;
import org.seasar.doma.ClobFactory;
import org.seasar.doma.Column;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.Holder;
import org.seasar.doma.HolderConverters;
import org.seasar.doma.Insert;
import org.seasar.doma.NClobFactory;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.SQLXMLFactory;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.Sql;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.Suppress;
import org.seasar.doma.Table;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class Annots {

  private final Context ctx;

  public Annots(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public AllArgsConstructorAnnot newAllArgsConstructorAnnot(TypeElement clazz) {
    assertNotNull(clazz);
    return newInstance(
        clazz, ctx.getOptions().getLombokAllArgsConstructor(), AllArgsConstructorAnnot::new);
  }

  public AnnotateWithAnnot newAnnotateWithAnnot(TypeElement typeElement) {
    assertNotNull(typeElement);
    var annotateWith = ctx.getElements().getAnnotationMirror(typeElement, AnnotateWith.class);
    if (annotateWith == null) {
      for (AnnotationMirror annotationMirror : typeElement.getAnnotationMirrors()) {
        var ownerElement =
            ctx.getElements().toTypeElement(annotationMirror.getAnnotationType().asElement());
        if (ownerElement == null) {
          continue;
        }
        annotateWith = ctx.getElements().getAnnotationMirror(ownerElement, AnnotateWith.class);
        if (annotateWith != null) {
          break;
        }
      }
      if (annotateWith == null) {
        return null;
      }
    }
    var values = ctx.getElements().getValuesWithDefaults(annotateWith);
    var annotations = values.get("annotations");
    ArrayList<AnnotationAnnot> annotationsValue = new ArrayList<>();
    for (var annotationMirror : AnnotationValueUtil.toAnnotationList(annotations)) {
      annotationsValue.add(newAnnotationAnnot(annotationMirror));
    }
    return new AnnotateWithAnnot(annotateWith, annotations, annotationsValue);
  }

  private AnnotationAnnot newAnnotationAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    var values = ctx.getElements().getValuesWithDefaults(annotationMirror);
    return new AnnotationAnnot(annotationMirror, values);
  }

  public ArrayFactoryAnnot newArrayFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, ArrayFactory.class, ArrayFactoryAnnot::new);
  }

  public BatchDeleteAnnot newBatchDeleteAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, BatchDelete.class, BatchDeleteAnnot::new);
  }

  public BatchInsertAnnot newBatchInsertAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, BatchInsert.class, BatchInsertAnnot::new);
  }

  public BatchUpdateAnnot newBatchUpdateAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, BatchUpdate.class, BatchUpdateAnnot::new);
  }

  public BlobFactoryAnnot newBlobFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, BlobFactory.class, BlobFactoryAnnot::new);
  }

  public ClobFactoryAnnot newClobFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, ClobFactory.class, ClobFactoryAnnot::new);
  }

  public ColumnAnnot newColumnAnnot(VariableElement field) {
    assertNotNull(field);
    return newInstance(field, Column.class, ColumnAnnot::new);
  }

  public DaoAnnot newDaoAnnot(TypeElement iface) {
    assertNotNull(iface);
    return newInstance(iface, Dao.class, DaoAnnot::new);
  }

  public DeleteAnnot newDeleteAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Delete.class, DeleteAnnot::new);
  }

  public EmbeddableAnnot newEmbeddableAnnot(TypeElement clazz) {
    assertNotNull(clazz);
    return newInstance(clazz, Embeddable.class, EmbeddableAnnot::new);
  }

  public EntityAnnot newEntityAnnot(TypeElement clazz) {
    assertNotNull(clazz);
    return newInstance(clazz, Entity.class, EntityAnnot::new);
  }

  public FunctionAnnot newFunctionAnnot(final ExecutableElement method) {
    assertNotNull(method);
    return newInstance(
        method,
        org.seasar.doma.Function.class,
        (annotationMirror, values) ->
            new FunctionAnnot(annotationMirror, method.getSimpleName().toString(), values));
  }

  public HolderConvertersAnnot newHolderConvertersAnnot(TypeElement iface) {
    assertNotNull(iface);
    return newInstance(iface, HolderConverters.class, HolderConvertersAnnot::new);
  }

  public HolderAnnot newHolderAnnot(TypeElement clazz) {
    assertNotNull(clazz);
    return newInstance(clazz, Holder.class, HolderAnnot::new);
  }

  public InsertAnnot newInsertAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Insert.class, InsertAnnot::new);
  }

  public NClobFactoryAnnot newNClobFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, NClobFactory.class, NClobFactoryAnnot::new);
  }

  public ProcedureAnnot newProcedureAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(
        method,
        Procedure.class,
        (annotationMirror, values) ->
            new ProcedureAnnot(annotationMirror, method.getSimpleName().toString(), values));
  }

  public ResultSetAnnot newResultSetAnnot(VariableElement param) {
    assertNotNull(param);
    return newInstance(param, ResultSet.class, ResultSetAnnot::new);
  }

  public ScriptAnnot newScriptAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Script.class, ScriptAnnot::new);
  }

  public SelectAnnot newSelectAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Select.class, SelectAnnot::new);
  }

  public SequenceGeneratorAnnot newSequenceGeneratorAnnot(VariableElement field) {
    assertNotNull(field);
    return newInstance(field, SequenceGenerator.class, SequenceGeneratorAnnot::new);
  }

  public SingletonConfigAnnot newSingletonConfigAnnot(TypeElement clazz) {
    assertNotNull(clazz);
    return newInstance(clazz, SingletonConfig.class, SingletonConfigAnnot::new);
  }

  public SuppressAnnot newSuppressAnnot(Element element) {
    assertNotNull(element);
    return newInstance(element, Suppress.class, SuppressAnnot::new);
  }

  public SqlAnnot newSqlAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Sql.class, SqlAnnot::new);
  }

  public SqlProcessorAnnot newSqlProcessorAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, SqlProcessor.class, SqlProcessorAnnot::new);
  }

  public SQLXMLFactoryAnnot newSQLXMLFactoryAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, SQLXMLFactory.class, SQLXMLFactoryAnnot::new);
  }

  public TableGeneratorAnnot newTableGeneratorAnnot(VariableElement field) {
    assertNotNull(field);
    return newInstance(field, TableGenerator.class, TableGeneratorAnnot::new);
  }

  public TableAnnot newTableAnnot(TypeElement clazz) {
    assertNotNull(clazz);
    return newInstance(clazz, Table.class, TableAnnot::new);
  }

  public UpdateAnnot newUpdateAnnot(ExecutableElement method) {
    assertNotNull(method);
    return newInstance(method, Update.class, UpdateAnnot::new);
  }

  public ValueAnnot newValueAnnot(TypeElement clazz) {
    assertNotNull(clazz);
    return newInstance(clazz, ctx.getOptions().getLombokValue(), ValueAnnot::new);
  }

  private <ANNOT> ANNOT newInstance(
      Element element,
      Class<? extends Annotation> annotationClass,
      Function<AnnotationMirror, ANNOT> function) {
    return newInstance(element, annotationClass, (first, second) -> function.apply(first));
  }

  private <ANNOT> ANNOT newInstance(
      Element element,
      Class<? extends Annotation> annotationClass,
      BiFunction<AnnotationMirror, Map<String, AnnotationValue>, ANNOT> biFunction) {
    var annotationMirror = ctx.getElements().getAnnotationMirror(element, annotationClass);
    if (annotationMirror == null) {
      return null;
    }
    var values = ctx.getElements().getValuesWithDefaults(annotationMirror);
    return biFunction.apply(annotationMirror, values);
  }

  private <ANNOT> ANNOT newInstance(
      Element element,
      String annotationClassName,
      BiFunction<AnnotationMirror, Map<String, AnnotationValue>, ANNOT> biFunction) {
    var annotationMirror = ctx.getElements().getAnnotationMirror(element, annotationClassName);
    if (annotationMirror == null) {
      return null;
    }
    var values = ctx.getElements().getValuesWithDefaults(annotationMirror);
    return biFunction.apply(annotationMirror, values);
  }
}
