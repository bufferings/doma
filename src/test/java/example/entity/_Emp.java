package example.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.processing.Generated;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.AbstractEntityDesc;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyDesc;
import org.seasar.doma.jdbc.entity.DefaultPropertyDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

@Generated("")
public class _Emp extends AbstractEntityDesc<Emp> {

  private static _Emp singleton = new _Emp();

  private static final org.seasar.doma.jdbc.entity.OriginalStatesAccessor<Emp>
      __originalStatesAccessor =
          new org.seasar.doma.jdbc.entity.OriginalStatesAccessor<>(Emp.class, "originalStates");

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public final AssignedIdPropertyDesc<Emp, Integer, Integer> id =
      new AssignedIdPropertyDesc<>(
          Emp.class,
          () -> new BasicScalar<>(new IntegerWrapper(), false),
          "id",
          "ID",
          __namingType,
          false);

  public final DefaultPropertyDesc<Emp, String, String> name =
      new DefaultPropertyDesc<>(
          Emp.class,
          () -> new BasicScalar<>(new StringWrapper(), false),
          "name",
          "NAME",
          __namingType,
          true,
          true,
          false);

  public final DefaultPropertyDesc<Emp, BigDecimal, BigDecimal> salary =
      new DefaultPropertyDesc<>(
          Emp.class,
          () -> new BasicScalar<>(new BigDecimalWrapper(), false),
          "salary",
          "SALARY",
          __namingType,
          true,
          true,
          false);

  public final VersionPropertyDesc<Emp, Integer, Integer> version =
      new VersionPropertyDesc<>(
          Emp.class,
          () -> new BasicScalar<>(new IntegerWrapper(), false),
          "version",
          "VERSION",
          __namingType,
          false);

  private final String __name = "Emp";

  private final String __catalogName = null;

  private final String __schemaName = null;

  private final String __tableName = "";

  private final List<EntityPropertyDesc<Emp, ?>> __idPropertyDescs;

  private final List<EntityPropertyDesc<Emp, ?>> __entityPropertyDescs;

  private final Map<String, EntityPropertyDesc<Emp, ?>> __entityPropertyDescMap;

  private _Emp() {
    List<EntityPropertyDesc<Emp, ?>> __idList = new ArrayList<>();
    __idList.add(id);
    __idPropertyDescs = Collections.unmodifiableList(__idList);
    List<EntityPropertyDesc<Emp, ?>> __list = new ArrayList<>();
    __list.add(id);
    __list.add(name);
    __list.add(salary);
    __list.add(version);
    __entityPropertyDescs = Collections.unmodifiableList(__list);
    Map<String, EntityPropertyDesc<Emp, ?>> __map = new HashMap<>();
    __map.put("id", id);
    __map.put("name", name);
    __map.put("salary", salary);
    __map.put("version", version);
    __entityPropertyDescMap = Collections.unmodifiableMap(__map);
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public Emp newEntity(Map<String, Property<Emp, ?>> args) {
    var entity = new Emp();
    args.values().forEach(v -> v.save(entity));
    return entity;
  }

  @Override
  public Class<Emp> getEntityClass() {
    return Emp.class;
  }

  @Override
  public String getName() {
    return __name;
  }

  @Override
  public List<EntityPropertyDesc<Emp, ?>> getEntityPropertyDescs() {
    return __entityPropertyDescs;
  }

  @Override
  public EntityPropertyDesc<Emp, ?> getEntityPropertyDesc(String propertyName) {
    return __entityPropertyDescMap.get(propertyName);
  }

  @Override
  public void saveCurrentStates(Emp __entity) {
    var __currentStates = new Emp();
    id.copy(__currentStates, __entity);
    name.copy(__currentStates, __entity);
    salary.copy(__currentStates, __entity);
    version.copy(__currentStates, __entity);
    __originalStatesAccessor.set(__entity, __currentStates);
  }

  @Override
  public Emp getOriginalStates(Emp entity) {
    if (entity.originalStates instanceof Emp) {
      var originalStates = (Emp) entity.originalStates;
      return originalStates;
    }
    return null;
  }

  @Override
  public GeneratedIdPropertyDesc<Emp, ?, ?> getGeneratedIdPropertyDesc() {
    return null;
  }

  @Override
  public VersionPropertyDesc<Emp, ?, ?> getVersionPropertyDesc() {
    return version;
  }

  @Override
  public List<EntityPropertyDesc<Emp, ?>> getIdPropertyDescs() {
    return __idPropertyDescs;
  }

  @Override
  public void preInsert(Emp entity, PreInsertContext<Emp> context) {}

  @Override
  public void preUpdate(Emp entity, PreUpdateContext<Emp> context) {}

  @Override
  public void preDelete(Emp entity, PreDeleteContext<Emp> context) {}

  @Override
  public void postInsert(Emp entity, PostInsertContext<Emp> context) {}

  @Override
  public void postUpdate(Emp entity, PostUpdateContext<Emp> context) {}

  @Override
  public void postDelete(Emp entity, PostDeleteContext<Emp> context) {}

  @Override
  public String getCatalogName() {
    return __catalogName;
  }

  @Override
  public String getSchemaName() {
    return __schemaName;
  }

  @Override
  public String getTableName(BiFunction<NamingType, String, String> namingFunction) {
    if (__tableName.isEmpty()) {
      return namingFunction.apply(getNamingType(), getName());
    }
    return __tableName;
  }

  @Override
  public NamingType getNamingType() {
    return __namingType;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }

  public static _Emp getSingletonInternal() {
    return singleton;
  }
}
