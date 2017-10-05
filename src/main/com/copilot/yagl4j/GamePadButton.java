package com.copilot.yagl4j;

/**
 * Game-pad button enumeration.
 *
 * @author Mark Lefering
 */
public enum GamePadButton {
  INVALID(-1),
  A(0),
  B(1),
  X(2),
  Y(3),
  BACK(4),
  GUIDE(5),
  START(6),
  LEFT_STICK(7),
  RIGHT_STICK(8),
  LEFT_SHOULDER(9),
  RIGHT_SHOULDER(10),
  DPAD_UP(11),
  DPAD_DOWN(12),
  DPAD_LEFT(13),
  DPAD_RIGHT(14);

  private final int value;

  /**
   * @param value The value of the controller button
   */
  GamePadButton(final int value) {
    this.value = value;
  }

  /**
   * @param value The value to get the enum for
   * @return The enum value
   */
  public static GamePadButton fromValue(final int value) {
    if (value >= -1 && value <= 14) {
      return values()[value + 1];
    } else {
      throw new IllegalArgumentException(
          String.format("Controller value %d invalid", value)
      );
    }
  }

  /**
   * @return The value.
   */
  public int getValue() {
    return value;
  }
}
