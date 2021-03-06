package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.dialect.StandardDialect;
import org.seasar.doma.message.Message;

public class JdbcExceptionTest extends TestCase {

  private final MockConfig config = new MockConfig();

  private Method method;

  @Override
  protected void setUp() throws Exception {
    method = getClass().getMethod(getName());
  }

  public void testSqlFileNotFound() throws Exception {
    var repository = new GreedyCacheSqlTemplateRepository();
    try {
      repository.getSqlTemplate(method, new StandardDialect());
      fail();
    } catch (SqlFileNotFoundException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2011, e.getMessageResource());
    }
  }

  public void testQuotationNotClosed() throws Exception {
    var parser = new SqlParser("select * from 'aaa");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2101, e.getMessageResource());
    }
  }

  public void testBlockCommentNotClosed() throws Exception {
    var parser = new SqlParser("select * from aaa /*aaa");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2102, e.getMessageResource());
    }
  }

  public void testIfCommentNotFoundForEndComment() throws Exception {
    var parser = new SqlParser("select * from aaa/*%end*/ ");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2104, e.getMessageResource());
    }
  }

  public void testIfCommentNotFoundForSecondEndComment() throws Exception {
    var parser = new SqlParser("select * from aaa where/*%if true*//*%end*/ /*%end*/");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2104, e.getMessageResource());
    }
  }

  public void testOpenedParensNotFound() throws Exception {
    var parser = new SqlParser("select * from aaa where )");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2109, e.getMessageResource());
    }
  }

  public void testTestLiteralNotFound() throws Exception {
    var parser = new SqlParser("select * from aaa where bbb = /*bbb*/ 'ccc')");
    try {
      parser.parse();
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2110, e.getMessageResource());
    }
  }

  public void testSqlBuildingFailed() throws Exception {
    var parser = new SqlParser("select * from aaa where bbb = \n/*bbb*/'ccc'");
    var sqlNode = parser.parse();
    var builder = new NodePreparedSqlBuilder(config, SqlKind.SELECT, "dummyPath");
    try {
      builder.build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2111, e.getMessageResource());
    }
  }

  public void testBindValueTypeNotIterable() throws Exception {
    var parser = new SqlParser("select * from aaa where bbb in /*bbb*/(1,2,3)");
    var sqlNode = parser.parse();
    var evaluator = new ExpressionEvaluator();
    evaluator.add("bbb", new Value(int.class, 1));
    var builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    try {
      builder.build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2112, e.getMessageResource());
    }
  }

  public void testCollectionOfBindValueContainsNull() throws Exception {
    var parser = new SqlParser("select * from aaa where bbb in /*bbb*/(1,2,3)");
    var sqlNode = parser.parse();
    var evaluator = new ExpressionEvaluator();
    evaluator.add("bbb", new Value(List.class, Arrays.asList(1, null)));
    var builder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SELECT, "dummyPath", evaluator, SqlLogType.FORMATTED);
    try {
      builder.build(sqlNode, Function.identity());
      fail();
    } catch (JdbcException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA2115, e.getMessageResource());
    }
  }
}
