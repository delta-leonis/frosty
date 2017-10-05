package com.copilot.yagl4j;

/**
 * Game-pad manager class.
 *
 * @author Mark Lefering
 */
public class GamePadManager {

  static {
    System.loadLibrary("yagl4j");
  }

  /**
   * Hooks a GamePadListener to the GamePadManager.
   *
   * @param gamePadListener The GamePadListener to hook to the GamePadManager.
   * @return True if the listener is hooked, false if not.
   */
  public final native boolean hookGamePadListener(
      final GamePadListener gamePadListener
  );

  /**
   * Unhooks a GamePadListener from the GamePadManager.
   *
   * @return True if the listener is unhooked, false if not.
   */
  public final native boolean unhookGamePadListener();

  /**
   * Starts the GamePadManager (starts the SDL thread in the native lib).
   *
   * @return True if started, false if not.
   */
  public final native boolean start();

  /**
   * Stops the GamePadManager (stops the SDL thread in the native lib).
   *
   * Note: waits for the the SDL thread to finish.
   *
   * @return True if stopped, false if not.
   */
  public final native boolean stop();

  /**
   * Sets the game-pad mapping database.
   *
   * @param filePath The path to the database file.
   * @return True if the database is loaded, false if not.
   */
  public final native boolean setGamePadMappingDatabase(final String filePath);
}
