package com.copilot.yagl4j.test;

import com.copilot.yagl4j.GamePadAxis;
import com.copilot.yagl4j.GamePadButton;
import com.copilot.yagl4j.GamePadListener;
import com.copilot.yagl4j.GamePadManager;

public class Main implements GamePadListener {

  public static void main(final String[] args) {
    new Main();
    while (true);
  }

  public Main() {
    final GamePadManager gamePadManager = new GamePadManager();
    gamePadManager.start();
    gamePadManager.hookGamePadListener(this);
    System.out.print("\033[2J\033[;H");
  }

  @Override
  public void onGamePadAdded(final long gamePadID) {
    System.out.println(String.format("Controller %d added", gamePadID));
  }

  @Override
  public void onGamePadRemoved(final long gamePadID) {
    System.out.println(String.format("Controller %d removed", gamePadID));
  }

  @Override
  public void onGamePadAxisMotion(
      final long gamePadID, final byte axis, final short value
  ) {
    System.out.print(String.format(
        "%c[%d;0f Controller %d moved axis %s: %d    ", 0x1B, axis + 1,
        gamePadID, GamePadAxis.fromValue(axis), value
    ));
    System.out.flush();
  }

  @Override
  public void onGamePadButtonDown(final long gamePadID, final byte button) {
    System.out.println(String.format(
        "%c[%d;0f Controller %d %s: [x]", 0x1B, 7 + button, gamePadID,
        GamePadButton.fromValue(button)
    ));
  }

  @Override
  public void onGamePadButtonUp(final long gamePadID, final byte button) {
    System.out.println(String.format(
        "%c[%d;0f Controller %d %s: [ ]", 0x1B, 7 + button, gamePadID,
        GamePadButton.fromValue(button)
    ));
  }

  @Override
  public void onGamePadRemapped(final long gamePadID) {
    System.out.println(String.format("Controller %d remapped", gamePadID));
  }

  @Override
  public void onGamePadError(final String error) {
    System.out.println(String.format("An error occured: %s", error));
  }
}
