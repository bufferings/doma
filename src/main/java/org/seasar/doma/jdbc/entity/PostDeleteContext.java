package org.seasar.doma.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.Delete;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;

/**
 * A context for a post process of a delete.
 *
 * @param <E> the entity type
 */
public interface PostDeleteContext<E> {

  /**
   * Returns the entity description.
   *
   * @return the entity description
   */
  EntityDesc<E> getEntityDesc();

  /**
   * The method that is annotated with {@link Delete}.
   *
   * @return the method
   */
  Method getMethod();

  /**
   * Returns the configuration.
   *
   * @return the configuration
   */
  Config getConfig();

  /**
   * Returns the new entity.
   *
   * @return the new entity
   */
  E getNewEntity();

  /**
   * Sets the new entity.
   *
   * <p>This method is available, when the entity is immutable.
   *
   * @param newEntity the entity
   * @throws DomaNullPointerException if {@code newEntity} is {@code null}
   */
  void setNewEntity(E newEntity);
}
