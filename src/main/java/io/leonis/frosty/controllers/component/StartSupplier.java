package io.leonis.frosty.controllers.component;

/**
 * The Class StartSupplier.
 *
 * Supplies the state of the start button.
 *
 * @author Jeroen de Jong
 */
public interface StartSupplier {

  /**
   * @return True if start is pressed, false otherwise.
   */
  boolean isStartPressed();
}
