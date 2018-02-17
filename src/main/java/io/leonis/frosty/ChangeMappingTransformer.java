package io.leonis.frosty;

import static java.util.stream.Collectors.*;

import io.reactivex.*;
import io.reactivex.functions.Function;
import io.reactivex.processors.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import lombok.Value;
import lombok.experimental.Delegate;
import org.reactivestreams.Publisher;

/**
 * The Class ChangeMappingTransformer.
 *
 * A class that allows for dynamic rebinding of a controller mapping based on controller input. This
 * class needs a mapping of controllers and a set of controller inputs and returns a new mapping of
 * controllers. The remapping is done based on the provided {@link BiFunction}.
 *
 * @author Jeroen de Jong
 * @param <A> Type of the assigned entity.
 * @param <C> Type of the controller.
 * @param <I> The input parameter.
 */
public class ChangeMappingTransformer<A, C extends Controller, I extends Controller.SetSupplier<C>>
    implements Function<Flowable<I>, Publisher<Controller.MapSupplier<A>>> {

  private final PublishProcessor<Controller.MapSupplier<A>> processor;
  private final BiFunction<Map<A, Set<Integer>>, C, Map<A, Set<Integer>>> remapper;

  /**
   * @param remapper Returns a mapping based on the previous mapping and a controller.
   * @param map      The initial mapping.
   */
  public ChangeMappingTransformer(
      final BiFunction<Map<A, Set<Integer>>, C, Map<A, Set<Integer>>> remapper,
      final Map<A, Set<Integer>> map
  ) {
    this(remapper, Flowable.just(() -> map));
  }

  /**
   * @param remapper     Returns a mapping based on the previous mapping and a controller.
   * @param mapPublisher Publisher of the initial mapping.
   */
  public ChangeMappingTransformer(
      final BiFunction<Map<A, Set<Integer>>, C, Map<A, Set<Integer>>> remapper,
      final Flowable<Controller.MapSupplier<A>> mapPublisher
  ) {
    this.remapper = remapper;
    this.processor = PublishProcessor.create();
    mapPublisher.subscribe(processor::onNext);
  }

  @Override
  public Publisher<Controller.MapSupplier<A>> apply(final Flowable<I> setPublisher) {
    return Flowable.combineLatest(
        this.processor.distinctUntilChanged(Controller.MapSupplier::getControllerMapping),
        setPublisher, InputFrame::new)
      .map(c -> c.getControllerSet().stream()
          .reduce(c.getControllerMapping(),
              remapper,
              (a, b) -> Stream.concat(a.entrySet().stream(), b.entrySet().stream())
                  .flatMap(entry -> entry.getValue().stream()
                      .map(controller -> new SimpleImmutableEntry<>(entry.getKey(), controller)))
                  .collect(groupingBy(Entry::getKey,
                      mapping(Entry::getValue, toSet())))))
      .<Controller.MapSupplier<A>>map(map -> () -> map)
      .doOnNext(processor::onNext);
  }

  @Value
  private static class InputFrame<A, C extends Controller>
      implements Controller.MapSupplier<A>, Controller.SetSupplier<C> {
    @Delegate
    private final Controller.MapSupplier<A> mapSupplier;
    @Delegate
    private final Controller.SetSupplier<C> setSupplier;
  }
}
