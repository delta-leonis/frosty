package io.leonis.frosty;

import com.studiohartman.jamepad.*;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.*;

/**
 * The Class ControllerSetPublisher.
 *
 * Publishes all <b>connected</b> controllers at a provided interval.
 *
 * @param <C> Type of {@link Controller}.
 * @author Jeroen de Jong
 */
public final class ControllerSetPublisher<C extends Controller>
    implements Publisher<Controller.SetSupplier<C>> {

  /**
   * Interval between polls.
   */
  private final int interval;

  /**
   * Adapter used to transform jamepad state.
   */
  private final Function<ControllerIndex, C> adapter;

  /**
   * Jamepad controller manager.
   */
  private final ControllerManager manager;

  /**
   *
   * @param amount   amount of controllers to listen for.
   * @param interval milliseconds in between polls.
   * @param adapter  controller adapter.
   */
  public ControllerSetPublisher(
      final int amount,
      final int interval,
      final Function<ControllerIndex, C> adapter
  ) {
    this.manager = new ControllerManager(amount);
    this.interval = interval;
    this.adapter = adapter;
  }

  @Override
  public void subscribe(final Subscriber<? super Controller.SetSupplier<C>> subscriber) {
    Flowable.interval(this.interval, TimeUnit.MILLISECONDS)
        .doOnSubscribe(tick -> manager.initSDLGamepad())
        .doOnComplete(manager::quitSDLGamepad)
        .doOnNext(tick -> manager.update())
        .map(tick -> Flowable.range(0, manager.getNumControllers())
          .map(manager::getControllerIndex)
          .map(this.adapter))
        .<Controller.SetSupplier<C>>map(list -> () -> list.toList(HashSet::new).blockingGet())
        .subscribe(subscriber);
  }
}
