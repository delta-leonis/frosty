package com.copilot.yagl4j;

/**
 * Game-pad axis enumeration.
 *
 * @author Mark Lefering
 */
public enum GamePadAxis {
  SDL_CONTROLLER_AXIS_INVALID(-1),
  SDL_CONTROLLER_AXIS_LEFTX(0),
  SDL_CONTROLLER_AXIS_LEFTY(1),
  SDL_CONTROLLER_AXIS_RIGHTX(2),
  SDL_CONTROLLER_AXIS_RIGHTY(3),
  SDL_CONTROLLER_AXIS_TRIGGERLEFT(4),
  SDL_CONTROLLER_AXIS_TRIGGERRIGHT(5);

  /**
   * Value of the enumeration.
   */
  private final int value;

  /**
   * Constructor of the ControllerAxis enum.
   *
   * @param value The value of the controller axis.
   */
  GamePadAxis(final int value) {
    this.value = value;
  }

  /**
   * Gets the ControllerAxis from value.
   *
   * @param value The value of the ControllerAxis.
   * @return The ControllerAxis.
   * @throws IllegalArgumentException -1 <= value <= 5.
   */
  public static GamePadAxis fromValue(final int value) {
    if (value < -1 || value > 5) {
      throw new IllegalArgumentException(
          String.format("ControllerAxis value %d out of bounds", value)
      );
    }
    return values()[value + 1];
  }

  /**
   * Gets the value of the controller axis.
   *
   * @return The value of the controller axis.
   */
  public int getValue() {
    return value;
  }
}
