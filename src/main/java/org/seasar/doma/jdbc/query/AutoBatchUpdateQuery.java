package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.Property;

public class AutoBatchUpdateQuery<ENTITY> extends AutoBatchModifyQuery<ENTITY>
    implements BatchUpdateQuery {

  protected boolean versionIgnored;

  protected boolean optimisticLockExceptionSuppressed;

  protected BatchUpdateQueryHelper<ENTITY> helper;

  public AutoBatchUpdateQuery(EntityDesc<ENTITY> entityDesc) {
    super(entityDesc);
  }

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entities, sqls);
    var size = entities.size();
    if (size == 0) {
      return;
    }
    executable = true;
    executionSkipCause = null;
    currentEntity = entities.get(0);
    setupHelper();
    preUpdate();
    prepareIdAndVersionPropertyDescs();
    validateIdExistent();
    prepareOptions();
    prepareOptimisticLock();
    prepareTargetPropertyDescs();
    prepareSql();
    entities.set(0, currentEntity);
    for (var it = entities.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preUpdate();
      prepareSql();
      it.set(currentEntity);
    }
    assertEquals(entities.size(), sqls.size());
  }

  protected void setupHelper() {
    helper =
        new BatchUpdateQueryHelper<>(
            config,
            entityDesc,
            includedPropertyNames,
            excludedPropertyNames,
            versionIgnored,
            optimisticLockExceptionSuppressed);
  }

  protected void preUpdate() {
    var context = new AutoBatchPreUpdateContext<>(entityDesc, method, config);
    entityDesc.preUpdate(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
  }

  protected void prepareOptimisticLock() {
    if (versionPropertyDesc != null && !versionIgnored) {
      if (!optimisticLockExceptionSuppressed) {
        optimisticLockCheckRequired = true;
      }
    }
  }

  protected void prepareTargetPropertyDescs() {
    targetPropertyDescs = helper.getTargetPropertyDescs();
  }

  protected void prepareSql() {
    var naming = config.getNaming();
    var dialect = config.getDialect();
    var builder = new PreparedSqlBuilder(config, SqlKind.BATCH_UPDATE, sqlLogType);
    builder.appendSql("update ");
    builder.appendSql(entityDesc.getQualifiedTableName(naming::apply, dialect::applyQuote));
    builder.appendSql(" set ");
    helper.populateValues(currentEntity, targetPropertyDescs, versionPropertyDesc, builder);
    if (idPropertyDescs.size() > 0) {
      builder.appendSql(" where ");
      for (var propertyDesc : idPropertyDescs) {
        var property = propertyDesc.createProperty();
        property.load(currentEntity);
        builder.appendSql(propertyDesc.getColumnName(naming::apply, dialect::applyQuote));
        builder.appendSql(" = ");
        builder.appendParameter(property.asInParameter());
        builder.appendSql(" and ");
      }
      builder.cutBackSql(5);
    }
    if (versionPropertyDesc != null && !versionIgnored) {
      if (idPropertyDescs.size() == 0) {
        builder.appendSql(" where ");
      } else {
        builder.appendSql(" and ");
      }
      Property<ENTITY, ?> property = versionPropertyDesc.createProperty();
      property.load(currentEntity);
      builder.appendSql(versionPropertyDesc.getColumnName(naming::apply, dialect::applyQuote));
      builder.appendSql(" = ");
      builder.appendParameter(property.asInParameter());
    }
    var sql = builder.build(this::comment);
    sqls.add(sql);
  }

  @Override
  public void incrementVersions() {
    if (versionPropertyDesc != null && !versionIgnored) {
      for (var it = entities.listIterator(); it.hasNext(); ) {
        var newEntity = versionPropertyDesc.increment(entityDesc, it.next());
        it.set(newEntity);
      }
    }
  }

  @Override
  public void complete() {
    for (var it = entities.listIterator(); it.hasNext(); ) {
      currentEntity = it.next();
      postUpdate();
      it.set(currentEntity);
    }
  }

  protected void postUpdate() {
    var context = new AutoBatchPostUpdateContext<>(entityDesc, method, config);
    entityDesc.postUpdate(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
    entityDesc.saveCurrentStates(currentEntity);
  }

  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored |= versionIgnored;
  }

  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  protected static class AutoBatchPreUpdateContext<E> extends AbstractPreUpdateContext<E> {

    public AutoBatchPreUpdateContext(EntityDesc<E> entityDesc, Method method, Config config) {
      super(entityDesc, method, config);
    }

    @Override
    public boolean isEntityChanged() {
      return true;
    }

    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }

  protected static class AutoBatchPostUpdateContext<E> extends AbstractPostUpdateContext<E> {

    public AutoBatchPostUpdateContext(EntityDesc<E> entityDesc, Method method, Config config) {
      super(entityDesc, method, config);
    }

    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }
}
