package io.leonis.frosty;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;

/**
 * The Class ControllerTransformer.
 *
 * Transforms a {@link Set} of {@link Controller Controllers} to {@code O} to a {@link Map} grouped
 * by it's applicable identity.
 *
 * @param <C> Type of controller state
 * @param <A> Type of assignee identity
 * @param <O> Type of output
 * @author Jeroen de Jong
 */
@AllArgsConstructor
public final class ControllerTransformer<
    T extends Controller.SetSupplier<C> & Controller.MapSupplier<A>,
    C extends Controller, A, O>
    implements Function<Flowable<T>, Publisher<Map<A, O>>> {
  /**
   * Function that maps controller to the desired output type.
   */
  private final java.util.function.Function<C, O> outputFunction;

  /**
   * Function which reduces the accumulated set of O to O.
   */
  private final java.util.function.Function<Set<O>, O> outputReducer;

  /**
   * {@inheritDoc}
   *
   * todo this needs to be rewritten to use rxJava instead of streams
   */
  @Override
  public Publisher<Map<A, O>> apply(final Flowable<T> flowable) {
    return flowable
        .map(container -> container.getControllerMapping().entrySet().stream()
            .filter(entry -> container.getControllerSet().stream()
                .map(Controller::getIndex).anyMatch(entry.getValue()::contains))
            .collect(Collectors.toMap(
                Entry::getKey,
                entry -> outputReducer.apply(
                    container.getControllerSet().stream()
                        .filter(obj -> entry.getValue().contains(obj.getIndex()))
                        .map(outputFunction)
                        .collect(Collectors.toSet())))))
        .filter(mapping -> !mapping.isEmpty());
  }
}
