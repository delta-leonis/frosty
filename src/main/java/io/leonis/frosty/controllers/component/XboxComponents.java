package io.leonis.frosty.controllers.component;

import io.leonis.frosty.controllers.component.bumper.BumperSupplier;
import io.leonis.frosty.controllers.component.trigger.TriggerSupplier;
import io.leonis.frosty.controllers.component.xbox.*;

/**
 * The Interface XboxComponents.
 *
 * A sum type of all components found on a typical xbox controller.
 *
 * @author Jeroen de Jong
 */
public interface XboxComponents extends RightClusterSupplier, BumperSupplier, Dpad.Supplier,
    BackSupplier, StartSupplier, TriggerSupplier, Stick.Supplier {

}
