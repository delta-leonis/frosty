package com.copilot.yagl4j.test;

import com.copilot.yagl4j.GamePadAxis;
import com.copilot.yagl4j.GamePadButton;
import com.copilot.yagl4j.GamePadListener;
import com.copilot.yagl4j.GamePadManager;

public class Main implements GamePadListener {

  static GamePadManager gamePadManager;

  public static void main(String[] args) {
    new Main();
    while (true);
  }

  public Main() {
    gamePadManager = new GamePadManager();
    gamePadManager.start();
    gamePadManager.hookGamePadListener(this);
    System.out.print("\033[2J\033[;H");
  }

  @Override
  public void onGamePadAdded(long gamePadID) {
    System.out.println(String.format("Controller %d added", gamePadID));
  }

  @Override
  public void onGamePadRemoved(long gamePadID) {
    System.out.println(String.format("Controller %d removed", gamePadID));
  }

  @Override
  public void onGamePadAxisMotion(long gamePadID, byte axis, short value) {
    System.out.print(String.format(
        "%c[%d;0f Controller %d moved axis %s: %d    ", 0x1B, axis + 1,
        gamePadID, GamePadAxis.fromValue(axis), value
    ));
    System.out.flush();
  }

  @Override
  public void onGamePadButtonDown(long gamePadID, byte button) {
    System.out.println(String.format(
        "%c[%d;0f Controller %d %s: [x]", 0x1B, 7 + button, gamePadID,
        GamePadButton.fromValue(button)
    ));
  }

  @Override
  public void onGamePadButtonUp(long gamePadID, byte button) {
    System.out.println(String.format(
        "%c[%d;0f Controller %d %s: [ ]", 0x1B, 7 + button, gamePadID,
        GamePadButton.fromValue(button)
    ));
  }

  @Override
  public void onGamePadRemapped(long gamePadID) {
    System.out.println(String.format("Controller %d remapped", gamePadID));
  }

  @Override
  public void onGamePadError(String error) {

  }
}
