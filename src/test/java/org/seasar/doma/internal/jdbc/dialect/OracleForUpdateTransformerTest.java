package org.seasar.doma.internal.jdbc.dialect;

import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;

public class OracleForUpdateTransformerTest extends TestCase {

  public void testForUpdateNormal() throws Exception {
    var expected = "select * from emp order by emp.id for update";
    var transformer = new OracleForUpdateTransformer(SelectForUpdateType.NORMAL, 0);
    var parser = new SqlParser("select * from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testForUpdateNormal_alias() throws Exception {
    var expected = "select * from emp order by emp.id for update of emp.name, emp.salary";
    var transformer =
        new OracleForUpdateTransformer(SelectForUpdateType.NORMAL, 0, "emp.name", "emp.salary");
    var parser = new SqlParser("select * from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testForUpdateNowait() throws Exception {
    var expected = "select * from emp order by emp.id for update nowait";
    var transformer = new OracleForUpdateTransformer(SelectForUpdateType.NOWAIT, 0);
    var parser = new SqlParser("select * from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  public void testForUpdateWait() throws Exception {
    var expected = "select * from emp order by emp.id for update wait 10";
    var transformer = new OracleForUpdateTransformer(SelectForUpdateType.WAIT, 10);
    var parser = new SqlParser("select * from emp order by emp.id");
    var sqlNode = transformer.transform(parser.parse());
    var sqlBuilder = new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    var sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}
