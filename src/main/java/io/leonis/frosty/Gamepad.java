package io.leonis.frosty;

import java.util.Collection;

public interface Gamepad {
  Collection<Input<Boolean>> getButtons();
  Collection<Input<Double>> getAnalogs();
}
