package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

public abstract class BatchModifyAnnot extends AbstractAnnot {

  public static final String EXCLUDE = "exclude";

  public static final String INCLUDE = "include";

  public static final String SUPPRESS_OPTIMISTIC_LOCK_EXCEPTION = "suppressOptimisticLockException";

  public static final String IGNORE_VERSION = "ignoreVersion";

  public static final String SQL_LOG = "sqlLog";

  public static final String BATCH_SIZE = "batchSize";

  public static final String QUERY_TIMEOUT = "queryTimeout";

  private final AnnotationValue queryTimeout;

  private final AnnotationValue batchSize;

  private final AnnotationValue sqlLog;

  private final AnnotationValue ignoreVersion;

  private final AnnotationValue suppressOptimisticLockException;

  private final AnnotationValue include;

  private final AnnotationValue exclude;

  BatchModifyAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(annotationMirror, values);

    // non null values
    this.queryTimeout = assertNotNullValue(values, QUERY_TIMEOUT);
    this.batchSize = assertNotNullValue(values, BATCH_SIZE);
    this.sqlLog = assertNotNullValue(values, SQL_LOG);

    // nullable values
    this.ignoreVersion = values.get(IGNORE_VERSION);
    this.suppressOptimisticLockException = values.get(SUPPRESS_OPTIMISTIC_LOCK_EXCEPTION);
    this.include = values.get(INCLUDE);
    this.exclude = values.get(EXCLUDE);
  }

  public AnnotationValue getQueryTimeout() {
    return queryTimeout;
  }

  public AnnotationValue getBatchSize() {
    return batchSize;
  }

  public AnnotationValue getIgnoreVersion() {
    return ignoreVersion;
  }

  public AnnotationValue getSuppressOptimisticLockException() {
    return suppressOptimisticLockException;
  }

  public AnnotationValue getInclude() {
    return include;
  }

  public AnnotationValue getExclude() {
    return exclude;
  }

  public AnnotationValue getSqlLog() {
    return sqlLog;
  }

  public int getQueryTimeoutValue() {
    var value = AnnotationValueUtil.toInteger(queryTimeout);
    if (value == null) {
      throw new AptIllegalStateException(QUERY_TIMEOUT);
    }
    return value;
  }

  public int getBatchSizeValue() {
    var value = AnnotationValueUtil.toInteger(batchSize);
    if (value == null) {
      throw new AptIllegalStateException(BATCH_SIZE);
    }
    return value;
  }

  public Boolean getIgnoreVersionValue() {
    return AnnotationValueUtil.toBoolean(ignoreVersion);
  }

  public Boolean getSuppressOptimisticLockExceptionValue() {
    return AnnotationValueUtil.toBoolean(suppressOptimisticLockException);
  }

  public List<String> getIncludeValue() {
    return AnnotationValueUtil.toStringList(include);
  }

  public List<String> getExcludeValue() {
    return AnnotationValueUtil.toStringList(exclude);
  }

  public SqlLogType getSqlLogValue() {
    var enumConstant = AnnotationValueUtil.toEnumConstant(sqlLog);
    if (enumConstant == null) {
      throw new AptIllegalStateException(SQL_LOG);
    }
    return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
  }
}
