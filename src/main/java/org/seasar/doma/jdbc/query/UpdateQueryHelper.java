package org.seasar.doma.jdbc.query;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;

/** A helper for {@link UpdateQuery}. */
public class UpdateQueryHelper<E> {

  protected final Config config;

  protected final EntityDesc<E> entityDesc;

  protected final boolean nullExcluded;

  protected final boolean versionIgnored;

  protected final boolean optimisticLockExceptionSuppressed;

  protected final boolean unchangedPropertyIncluded;

  protected final String[] includedPropertyNames;

  protected final String[] excludedPropertyNames;

  public UpdateQueryHelper(
      Config config,
      EntityDesc<E> entityDesc,
      String[] includedPropertyNames,
      String[] excludedPropertyNames,
      boolean nullExcluded,
      boolean versionIgnored,
      boolean optimisticLockExceptionSuppressed,
      boolean unchangedPropertyIncluded) {
    this.config = config;
    this.entityDesc = entityDesc;
    this.nullExcluded = nullExcluded;
    this.versionIgnored = versionIgnored;
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    this.unchangedPropertyIncluded = unchangedPropertyIncluded;
    this.includedPropertyNames = includedPropertyNames;
    this.excludedPropertyNames = excludedPropertyNames;
  }

  public List<EntityPropertyDesc<E, ?>> getTargetPropertyDescs(E entity) {
    var capacity = entityDesc.getEntityPropertyDescs().size();
    List<EntityPropertyDesc<E, ?>> results = new ArrayList<>(capacity);
    var originalStates = entityDesc.getOriginalStates(entity);
    for (var propertyDesc : entityDesc.getEntityPropertyDescs()) {
      if (!propertyDesc.isUpdatable()) {
        continue;
      }
      if (propertyDesc.isId()) {
        continue;
      }
      if (!versionIgnored && propertyDesc.isVersion()) {
        continue;
      }
      if (nullExcluded) {
        var property = propertyDesc.createProperty();
        property.load(entity);
        if (property.getWrapper().get() == null) {
          continue;
        }
      }
      if (unchangedPropertyIncluded
          || originalStates == null
          || isChanged(entity, originalStates, propertyDesc)) {
        var name = propertyDesc.getName();
        if (!isTargetPropertyName(name)) {
          continue;
        }
        results.add(propertyDesc);
      }
    }
    return results;
  }

  protected boolean isTargetPropertyName(String name) {
    if (includedPropertyNames.length > 0) {
      for (var includedName : includedPropertyNames) {
        if (includedName.equals(name)) {
          for (var excludedName : excludedPropertyNames) {
            if (excludedName.equals(name)) {
              return false;
            }
          }
          return true;
        }
      }
      return false;
    }
    if (excludedPropertyNames.length > 0) {
      for (var excludedName : excludedPropertyNames) {
        if (excludedName.equals(name)) {
          return false;
        }
      }
      return true;
    }
    return true;
  }

  protected boolean isChanged(E entity, E originalStates, EntityPropertyDesc<E, ?> propertyDesc) {
    var wrapper = propertyDesc.createProperty().load(entity).getWrapper();
    var originalWrapper = propertyDesc.createProperty().load(originalStates).getWrapper();
    return !wrapper.hasEqualValue(originalWrapper.get());
  }

  public void populateValues(
      E entity,
      List<EntityPropertyDesc<E, ?>> targetPropertyDescs,
      VersionPropertyDesc<E, ?, ?> versionPropertyDesc,
      SqlContext context) {
    var dialect = config.getDialect();
    var naming = config.getNaming();
    for (var propertyDesc : targetPropertyDescs) {
      var property = propertyDesc.createProperty();
      property.load(entity);
      context.appendSql(propertyDesc.getColumnName(naming::apply, dialect::applyQuote));
      context.appendSql(" = ");
      context.appendParameter(property.asInParameter());
      context.appendSql(", ");
    }
    if (!versionIgnored && versionPropertyDesc != null) {
      Property<E, ?> property = versionPropertyDesc.createProperty();
      property.load(entity);
      context.appendSql(versionPropertyDesc.getColumnName(naming::apply, dialect::applyQuote));
      context.appendSql(" = ");
      context.appendParameter(property.asInParameter());
      context.appendSql(" + 1");
    } else {
      context.cutBackSql(2);
    }
  }
}
