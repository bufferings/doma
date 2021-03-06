package org.seasar.doma.internal.jdbc.command;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;

/**
 * @author taedium
 * @param <BASIC>
 * @param <CONTAINER>
 * @since 2.0.0
 */
public class ScalarResultListHandler<BASIC, CONTAINER>
    extends AbstractResultListHandler<CONTAINER> {

  public ScalarResultListHandler(Supplier<Scalar<BASIC, CONTAINER>> supplier) {
    super(new ScalarIterationHandler<>(supplier, new ResultListCallback<>()));
  }
}
