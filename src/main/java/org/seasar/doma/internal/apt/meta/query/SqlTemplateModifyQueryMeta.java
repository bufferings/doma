package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlTemplateModifyQueryMeta extends AbstractSqlTemplateQueryMeta {

  private EntityCtType entityCtType;

  private String entityParameterName;

  private ModifyAnnot modifyAnnot;

  public SqlTemplateModifyQueryMeta(ExecutableElement method) {
    super(method);
  }

  public EntityCtType getEntityCtType() {
    return entityCtType;
  }

  public void setEntityCtType(EntityCtType entityCtType) {
    this.entityCtType = entityCtType;
  }

  public String getEntityParameterName() {
    return entityParameterName;
  }

  public void setEntityParameterName(String entityParameterName) {
    this.entityParameterName = entityParameterName;
  }

  public ModifyAnnot getModifyAnnot() {
    return modifyAnnot;
  }

  public void setModifyAnnot(ModifyAnnot modifyAnnot) {
    this.modifyAnnot = modifyAnnot;
  }

  public int getQueryTimeout() {
    return modifyAnnot.getQueryTimeoutValue();
  }

  public Boolean getIgnoreVersion() {
    return modifyAnnot.getIgnoreVersionValue();
  }

  public Boolean getExcludeNull() {
    return modifyAnnot.getExcludeNullValue();
  }

  public Boolean getSuppressOptimisticLockException() {
    return modifyAnnot.getSuppressOptimisticLockExceptionValue();
  }

  public Boolean getIncludeUnchanged() {
    return modifyAnnot.getIncludeUnchangedValue();
  }

  public List<String> getInclude() {
    return modifyAnnot.getIncludeValue();
  }

  public List<String> getExclude() {
    return modifyAnnot.getExcludeValue();
  }

  public SqlLogType getSqlLogType() {
    return modifyAnnot.getSqlLogValue();
  }

  @Override
  public boolean isPopulatable() {
    return entityCtType != null && queryKind == QueryKind.SQLFILE_UPDATE;
  }

  @Override
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitSqlTemplateModifyQueryMeta(this, p);
  }
}
