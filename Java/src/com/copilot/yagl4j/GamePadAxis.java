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

    /// Value of the enumeration.
    private final int value;

    /**
     * Constructor of the ControllerAxis enum.
     *
     * @param value The value of the controller axis.
     */
    GamePadAxis(int value) {
        // Setting value
        this.value = value;
    }

    /**
     * Gets the ControllerAxis from value.
     *
     * @param value The value of the ControllerAxis.
     * @return The ControllerAxis.
     * @throws IllegalArgumentException -1 <= value <= 5.
     */
    public static GamePadAxis fromValue(int value) throws IllegalArgumentException {
        if (value >= -1 && value <= 5)
            return values()[value+1];
        else
            throw new IllegalArgumentException(String.format("ControllerAxis value %d out of bounds", value));
    }

    /**
     * Gets the value of the controller axis.
     *
     * @return The value of the controller axis.
     */
    public int getValue() {
        // Getting value
        return value;
    }
}
