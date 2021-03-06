package example.dao;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.processing.Generated;
import javax.sql.DataSource;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityStreamHandler;
import org.seasar.doma.jdbc.AbstractDao;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;

@Generated("")
public class EmpDaoImpl extends AbstractDao implements EmpDao {

  private static Method method0 =
      getDeclaredMethod(EmpDaoImpl.class, "selectById", Integer.class, SelectOptions.class);

  private static Method method1 =
      getDeclaredMethod(
          EmpDaoImpl.class,
          "selectByNameAndSalary",
          String.class,
          BigDecimal.class,
          SelectOptions.class);

  private static Method method2 = getDeclaredMethod(EmpDaoImpl.class, "selectByExample", Emp.class);

  private static Method method3 = getDeclaredMethod(EmpDaoImpl.class, "insert", Emp.class);

  private static Method method4 = getDeclaredMethod(EmpDaoImpl.class, "update", Emp.class);

  private static Method method5 = getDeclaredMethod(EmpDaoImpl.class, "delete", Emp.class);

  private static Method method6 =
      getDeclaredMethod(EmpDaoImpl.class, "iterate", IterationCallback.class);

  private static Method method7 = getDeclaredMethod(EmpDaoImpl.class, "execute");

  public EmpDaoImpl() {
    super(new ExampleConfig());
  }

  public EmpDaoImpl(Connection connection) {
    super(new ExampleConfig(), connection);
  }

  public EmpDaoImpl(DataSource dataSource) {
    super(new ExampleConfig(), dataSource);
  }

  @Override
  public Emp selectById(Integer id, SelectOptions option) {
    var query = getQueryImplementors().createSqlTemplateSelectQuery(method0);
    query.setConfig(__config);
    query.addParameter("id", Integer.class, id);
    query.setOptions(option);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("selectById");
    query.prepare();
    var command =
        getCommandImplementors()
            .createSelectCommand(
                method0, query, new EntitySingleResultHandler<>(_Emp.getSingletonInternal()));
    return command.execute();
  }

  @Override
  public List<Emp> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions option) {
    var query = getQueryImplementors().createSqlTemplateSelectQuery(method1);
    query.setConfig(__config);
    query.addParameter("name", String.class, name);
    query.addParameter("salary", BigDecimal.class, salary);
    query.setOptions(option);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("selectByNameAndSalary");
    query.prepare();
    var command =
        getCommandImplementors()
            .createSelectCommand(
                method1, query, new EntityResultListHandler<>(_Emp.getSingletonInternal()));
    return command.execute();
  }

  @Override
  public List<Emp> selectByExample(Emp emp) {
    var query = getQueryImplementors().createSqlTemplateSelectQuery(method2);
    query.setConfig(__config);
    query.addParameter("emp", Emp.class, emp);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("selectByNameAndSalary");
    query.prepare();
    var command =
        getCommandImplementors()
            .createSelectCommand(
                method2, query, new EntityResultListHandler<>(_Emp.getSingletonInternal()));
    return command.execute();
  }

  @Override
  public int insert(Emp entity) {
    var query = getQueryImplementors().createAutoInsertQuery(method3, _Emp.getSingletonInternal());
    query.setConfig(__config);
    query.setEntity(entity);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("insert");
    query.prepare();
    var command = getCommandImplementors().createInsertCommand(method3, query);
    return command.execute();
  }

  @Override
  public int update(Emp entity) {
    var query = getQueryImplementors().createAutoUpdateQuery(method4, _Emp.getSingletonInternal());
    query.setConfig(__config);
    query.setEntity(entity);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("update");
    query.prepare();
    var command = getCommandImplementors().createUpdateCommand(method4, query);
    return command.execute();
  }

  @Override
  public int delete(Emp entity) {
    var query = getQueryImplementors().createAutoDeleteQuery(method5, _Emp.getSingletonInternal());
    query.setConfig(__config);
    query.setEntity(entity);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("delete");
    query.prepare();
    var command = getCommandImplementors().createDeleteCommand(method5, query);
    return command.execute();
  }

  @Override
  public Integer stream(Function<Stream<Emp>, Integer> mapper) {
    var query = getQueryImplementors().createSqlTemplateSelectQuery(method6);
    query.setConfig(__config);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("iterate");
    query.prepare();
    var command =
        getCommandImplementors()
            .createSelectCommand(
                method6, query, new EntityStreamHandler<>(_Emp.getSingletonInternal(), mapper));
    return command.execute();
  }

  @Override
  public void execute() {
    var query = getQueryImplementors().createStaticScriptQuery(method7);
    query.setConfig(__config);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("execute");
    query.prepare();
    var command = getCommandImplementors().createScriptCommand(method7, query);
    command.execute();
  }
}
