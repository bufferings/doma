package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;

/**
 * Indicates a batch insert.
 *
 * <p>The annotated method must be a member of a {@link Dao} annotated interface.
 *
 * <p>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 * }
 *
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;BatchInsert
 *     int[] insert(List&lt;Employee&gt; employee);
 * }
 * </pre>
 *
 * The method may throw following exceptions:
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} if any of the method parameters are {@code null}
 *   <li>{@link UniqueConstraintException} if an unique constraint is violated
 *   <li>{@link SqlFileNotFoundException} if {@link Sql#useFile()} is {@code true} and the SQL file
 *       is not found
 *   <li>{@link JdbcException} if a JDBC related error occurs
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface BatchInsert {

  /**
   * The query timeout in seconds.
   *
   * <p>If not specified, {@link Config#getQueryTimeout()} is used.
   *
   * @see Statement#setQueryTimeout(int)
   */
  int queryTimeout() default -1;

  /**
   * The batch size.
   *
   * <p>If not specified, {@link Config#getBatchSize()} is used.
   *
   * <p>This value is used when {@link PreparedStatement#executeBatch()} is executed.
   *
   * @see PreparedStatement#addBatch()
   */
  int batchSize() default -1;

  /**
   * The properties whose mapped columns are included in SQL INSERT statements.
   *
   * <p>When this annotation is used in combination with {@link Sql}, this element value is ignored.
   */
  String[] include() default {};

  /**
   * The properties whose mapped columns are excluded from SQL INSERT statements.
   *
   * <p>When this annotation is used in combination with {@link Sql}, this element value is ignored.
   */
  String[] exclude() default {};

  /** The output format of SQL logs. */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
