package io.leonis.frosty.states;

import io.leonis.frosty.GamePad;
import io.leonis.frosty.Input;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GamePadState implements GamePad {

  private final Set<Input<Double>> analogs;
  private final Set<Input<Boolean>> buttons;

  public GamePadState(
      final Collection<Input<Double>> analogs,
      final Collection<Input<Boolean>> buttons
  ) {
    this.analogs = new HashSet<>(analogs);
    this.buttons = new HashSet<>(buttons);
  }

  @Override
  public Set<Input<Boolean>> getButtons() {
    return buttons;
  }

  @Override
  public Set<Input<Double>> getAnalogs() {
    return analogs;
  }
}
