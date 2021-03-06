package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.query.ArrayCreateQuery;
import org.seasar.doma.jdbc.query.AutoBatchDeleteQuery;
import org.seasar.doma.jdbc.query.AutoBatchInsertQuery;
import org.seasar.doma.jdbc.query.AutoBatchUpdateQuery;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.jdbc.query.AutoInsertQuery;
import org.seasar.doma.jdbc.query.AutoProcedureQuery;
import org.seasar.doma.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.jdbc.query.BlobCreateQuery;
import org.seasar.doma.jdbc.query.ClobCreateQuery;
import org.seasar.doma.jdbc.query.NClobCreateQuery;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.SQLXMLCreateQuery;
import org.seasar.doma.jdbc.query.SqlDeleteQuery;
import org.seasar.doma.jdbc.query.SqlInsertQuery;
import org.seasar.doma.jdbc.query.SqlProcessorQuery;
import org.seasar.doma.jdbc.query.SqlSelectQuery;
import org.seasar.doma.jdbc.query.SqlTemplateBatchDeleteQuery;
import org.seasar.doma.jdbc.query.SqlTemplateBatchInsertQuery;
import org.seasar.doma.jdbc.query.SqlTemplateBatchUpdateQuery;
import org.seasar.doma.jdbc.query.SqlTemplateDeleteQuery;
import org.seasar.doma.jdbc.query.SqlTemplateInsertQuery;
import org.seasar.doma.jdbc.query.SqlTemplateSelectQuery;
import org.seasar.doma.jdbc.query.SqlTemplateUpdateQuery;
import org.seasar.doma.jdbc.query.SqlUpdateQuery;
import org.seasar.doma.jdbc.query.StaticScriptQuery;

/** A factory for the {@link Query} implementation classes. */
public interface QueryImplementors {

  /**
   * Creates an {@link SqlTemplateSelectQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlTemplateSelectQuery createSqlTemplateSelectQuery(Method method) {
    return new SqlTemplateSelectQuery();
  }

  /**
   * Creates an {@link SqlSelectQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlSelectQuery createSqlSelectQuery(Method method) {
    return new SqlSelectQuery();
  }

  /**
   * Creates an {@link StaticScriptQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default StaticScriptQuery createStaticScriptQuery(Method method) {
    return new StaticScriptQuery();
  }

  /**
   * Creates an {@link AutoDeleteQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoDeleteQuery<ENTITY> createAutoDeleteQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoDeleteQuery<>(entityDesc);
  }

  /**
   * Creates an {@link AutoInsertQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoInsertQuery<ENTITY> createAutoInsertQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoInsertQuery<>(entityDesc);
  }

  /**
   * Creates an {@link AutoUpdateQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoUpdateQuery<ENTITY> createAutoUpdateQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoUpdateQuery<>(entityDesc);
  }

  /**
   * Creates an {@link SqlTemplateDeleteQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlTemplateDeleteQuery createSqlTemplateDeleteQuery(Method method) {
    return new SqlTemplateDeleteQuery();
  }

  /**
   * Creates an {@link SqlTemplateInsertQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlTemplateInsertQuery createSqlTemplateInsertQuery(Method method) {
    return new SqlTemplateInsertQuery();
  }

  /**
   * Creates an {@link SqlTemplateUpdateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlTemplateUpdateQuery createSqlTemplateUpdateQuery(Method method) {
    return new SqlTemplateUpdateQuery();
  }

  /**
   * Creates an {@link SqlDeleteQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlDeleteQuery createSqlDeleteQuery(Method method) {
    return new SqlDeleteQuery();
  }

  /**
   * Creates an {@link SqlInsertQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlInsertQuery createSqlInsertQuery(Method method) {
    return new SqlInsertQuery();
  }

  /**
   * Creates an {@link SqlUpdateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlUpdateQuery createSqlUpdateQuery(Method method) {
    return new SqlUpdateQuery();
  }

  /**
   * Creates an {@link AutoBatchDeleteQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoBatchDeleteQuery<ENTITY> createAutoBatchDeleteQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoBatchDeleteQuery<>(entityDesc);
  }

  /**
   * Creates an {@link AutoBatchInsertQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoBatchInsertQuery<ENTITY> createAutoBatchInsertQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoBatchInsertQuery<>(entityDesc);
  }

  /**
   * Creates an {@link AutoBatchUpdateQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoBatchUpdateQuery<ENTITY> createAutoBatchUpdateQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoBatchUpdateQuery<>(entityDesc);
  }

  /**
   * Creates an {@link SqlTemplateBatchDeleteQuery} object.
   *
   * @param method the DAO method
   * @param clazz the element class of the batch list
   * @return the query
   */
  default <ELEMENT> SqlTemplateBatchDeleteQuery<ELEMENT> createSqlTemplateBatchDeleteQuery(
      Method method, Class<ELEMENT> clazz) {
    return new SqlTemplateBatchDeleteQuery<>(clazz);
  }

  /**
   * Creates an {@link SqlTemplateBatchInsertQuery} object.
   *
   * @param method the DAO method
   * @param clazz the element class of the batch list
   * @return the query
   */
  default <ELEMENT> SqlTemplateBatchInsertQuery<ELEMENT> createSqlTemplateBatchInsertQuery(
      Method method, Class<ELEMENT> clazz) {
    return new SqlTemplateBatchInsertQuery<>(clazz);
  }

  /**
   * Creates a {@link SqlTemplateBatchUpdateQuery} object.
   *
   * @param method the DAO method
   * @param clazz the element class of the batch list
   * @return the query
   */
  default <ELEMENT> SqlTemplateBatchUpdateQuery<ELEMENT> createSqlTemplateBatchUpdateQuery(
      Method method, Class<ELEMENT> clazz) {
    return new SqlTemplateBatchUpdateQuery<>(clazz);
  }

  /**
   * Creates an {@link AutoFunctionQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default <RESULT> AutoFunctionQuery<RESULT> createAutoFunctionQuery(Method method) {
    return new AutoFunctionQuery<>();
  }

  /**
   * Creates an {@link AutoProcedureQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default AutoProcedureQuery createAutoProcedureQuery(Method method) {
    return new AutoProcedureQuery();
  }

  /**
   * Creates an {@link ArrayCreateQuery } object.
   *
   * @param method the DAO method
   * @return the query
   */
  default ArrayCreateQuery createArrayCreateQuery(Method method) {
    return new ArrayCreateQuery();
  }

  /**
   * Creates a {@link BlobCreateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default BlobCreateQuery createBlobCreateQuery(Method method) {
    return new BlobCreateQuery();
  }

  /**
   * Creates a {@link ClobCreateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default ClobCreateQuery createClobCreateQuery(Method method) {
    return new ClobCreateQuery();
  }

  /**
   * Creates a {@link NClobCreateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default NClobCreateQuery createNClobCreateQuery(Method method) {
    return new NClobCreateQuery();
  }

  /**
   * Creates an {@link SQLXMLCreateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SQLXMLCreateQuery createSQLXMLCreateQuery(Method method) {
    return new SQLXMLCreateQuery();
  }

  /**
   * Creates an {@link SqlProcessorQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlProcessorQuery createSqlProcessorQuery(Method method) {
    return new SqlProcessorQuery();
  }
}
