package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

public class ProcedureAnnot extends AbstractAnnot {

  public static final String SQL_LOG = "sqlLog";

  public static final String MAP_KEY_NAMING = "mapKeyNaming";

  public static final String QUERY_TIMEOUT = "queryTimeout";

  public static final String QUOTE = "quote";

  public static final String NAME = "name";

  private static final String SCHEMA = "schema";

  public static final String CATALOG = "catalog";

  private final String defaultName;

  private final AnnotationValue catalog;

  private final AnnotationValue schema;

  private final AnnotationValue name;

  private final AnnotationValue quote;

  private final AnnotationValue queryTimeout;

  private final AnnotationValue mapKeyNaming;

  private final AnnotationValue sqlLog;

  ProcedureAnnot(
      AnnotationMirror annotationMirror, String defaultName, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(defaultName, values);

    this.defaultName = defaultName;

    this.catalog = assertNotNullValue(values, CATALOG);
    this.schema = assertNotNullValue(values, SCHEMA);
    this.name = assertNotNullValue(values, NAME);
    this.quote = assertNotNullValue(values, QUOTE);
    this.queryTimeout = assertNotNullValue(values, QUERY_TIMEOUT);
    this.mapKeyNaming = assertNotNullValue(values, MAP_KEY_NAMING);
    this.sqlLog = assertNotNullValue(values, SQL_LOG);
  }

  public AnnotationValue getQueryTimeout() {
    return queryTimeout;
  }

  public AnnotationValue getMapKeyNaming() {
    return mapKeyNaming;
  }

  public AnnotationValue getSqlLog() {
    return sqlLog;
  }

  public String getCatalogValue() {
    var value = AnnotationValueUtil.toString(catalog);
    if (value == null) {
      throw new AptIllegalStateException(CATALOG);
    }
    return value;
  }

  public String getSchemaValue() {
    var value = AnnotationValueUtil.toString(schema);
    if (value == null) {
      throw new AptIllegalStateException(SCHEMA);
    }
    return value;
  }

  public String getNameValue() {
    var value = AnnotationValueUtil.toString(name);
    if (value == null || value.isEmpty()) {
      return defaultName;
    }
    return value;
  }

  public boolean getQuoteValue() {
    var value = AnnotationValueUtil.toBoolean(quote);
    if (value == null) {
      throw new AptIllegalStateException(QUOTE);
    }
    return value;
  }

  public int getQueryTimeoutValue() {
    var value = AnnotationValueUtil.toInteger(queryTimeout);
    if (value == null) {
      throw new AptIllegalStateException(QUERY_TIMEOUT);
    }
    return value;
  }

  public MapKeyNamingType getMapKeyNamingValue() {
    var enumConstant = AnnotationValueUtil.toEnumConstant(mapKeyNaming);
    if (enumConstant == null) {
      throw new AptIllegalStateException(MAP_KEY_NAMING);
    }
    return MapKeyNamingType.valueOf(enumConstant.getSimpleName().toString());
  }

  public SqlLogType getSqlLogValue() {
    var enumConstant = AnnotationValueUtil.toEnumConstant(sqlLog);
    if (enumConstant == null) {
      throw new AptIllegalStateException(SQL_LOG);
    }
    return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
  }
}
