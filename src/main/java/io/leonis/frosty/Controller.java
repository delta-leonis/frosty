package io.leonis.frosty;

import java.util.*;

/**
 * The Interface Controller.
 *
 * This interface describes the identification of a Controller.
 *
 * @author Jeroen de Jong
 */
public interface Controller {

  /**
   * @return The index of the controller.
   */
  int getIndex();

  /**
   * @return The name of the controller.
   */
  String getName();

  /**
   * The Interface SetSupplier.
   *
   * Container of a {@link Set} of {@link Controller Controllers}.
   *
   * @param <C> type of controller
   * @author Jeroen de Jong
   */
  interface SetSupplier<C extends Controller> {

    /**
     * @return Set of connected controllers.
     */
    Set<C> getControllerSet();
  }

  /**
   * The Interface MapSupplier.
   *
   * Container of a {@link Map} of controllable objects to a {@link Set} of controller indexes.
   *
   * @param <A> assigned controllable object.
   */
  interface MapSupplier<A> {

    Map<A, Set<Integer>> getControllerMapping();
  }
}
