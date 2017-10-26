package io.leonis.frosty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Game-pad manager class.
 *
 * @author Mark Lefering
 */
public class GamePadManager {

  static {
    try {
      System.loadLibrary("frosty");
    } catch (final UnsatisfiedLinkError error) {
      try {
        final File temp = File.createTempFile("libfrosty", ".so");

        try (
            final InputStream is = GamePadManager.class
                .getResourceAsStream("/native/libfrosty.so")
        ) {
          Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
          System.load(temp.getAbsolutePath());
        } catch (final IOException e) {
          e.printStackTrace();
        } finally {
          temp.delete();
        }
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
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
