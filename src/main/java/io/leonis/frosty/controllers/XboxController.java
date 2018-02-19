package io.leonis.frosty.controllers;

import com.studiohartman.jamepad.*;
import io.leonis.frosty.Controller;
import io.leonis.frosty.controllers.component.*;
import lombok.*;

/**
 * The Class XboxController.
 *
 * A simple container for all button states of a xbox controller.
 *
 * @author Jeroen de Jong
 */
@Value
@AllArgsConstructor
public class XboxController implements XboxComponents, Controller {

  private final String name;
  private final int index;
  private final Dpad dpad;
  private final Stick leftStick, rightStick;
  private final boolean aPressed, bPressed, xPressed, yPressed;
  private final boolean leftBumperPressed, rightBumperPressed, backPressed, startPressed;
  private final float leftTrigger, rightTrigger;

  public XboxController(ControllerIndex controller) throws ControllerUnpluggedException {
    this(
        controller.getName(),
        controller.getIndex(),
        new Dpad(
            controller.isButtonPressed(ControllerButton.DPAD_UP),
            controller.isButtonPressed(ControllerButton.DPAD_DOWN),
            controller.isButtonPressed(ControllerButton.DPAD_LEFT),
            controller.isButtonPressed(ControllerButton.DPAD_RIGHT)),
        new Stick(
            controller.getAxisState(ControllerAxis.RIGHTX),
            controller.getAxisState(ControllerAxis.RIGHTY),
            controller.isButtonPressed(ControllerButton.RIGHTSTICK)),
        new Stick(
            controller.getAxisState(ControllerAxis.LEFTX),
            controller.getAxisState(ControllerAxis.LEFTY),
            controller.isButtonPressed(ControllerButton.LEFTSTICK)),
        controller.isButtonJustPressed(ControllerButton.A),
        controller.isButtonJustPressed(ControllerButton.B),
        controller.isButtonJustPressed(ControllerButton.X),
        controller.isButtonJustPressed(ControllerButton.Y),
        controller.isButtonJustPressed(ControllerButton.LEFTBUMPER),
        controller.isButtonJustPressed(ControllerButton.RIGHTBUMPER),
        controller.isButtonJustPressed(ControllerButton.BACK),
        controller.isButtonJustPressed(ControllerButton.START),
        controller.getAxisState(ControllerAxis.TRIGGERLEFT),
        controller.getAxisState(ControllerAxis.TRIGGERRIGHT));
  }
}

