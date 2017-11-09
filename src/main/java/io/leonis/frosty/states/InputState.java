package io.leonis.frosty.states;

import io.leonis.frosty.Input;

public class InputState<T> implements Input<T> {

  private final T value;

  public InputState(final T value) {
    this.value = value;
  }

  @Override
  public T getValue() {
    return value;
  }
}
