package ch.fhnw.comgr.citysim.action;

import ch.fhnw.comgr.citysim.model.map.layer.InteractionObject;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler;

public class ConstructionSiteAction implements IEventScheduler.IAnimationAction {

    private final InteractionObject interactionObject;
    private final IController controller;

    public ConstructionSiteAction(InteractionObject interactionObject, IController controller) {
        this.interactionObject = interactionObject;
        this.controller = controller;
    }

    @Override
    public void run(double time, double interval) {
        if (time - interactionObject.getActivationTime() > 5) {
            interactionObject.deactivate();
            System.out.println("Time is up! Resetting traffic light");
            controller.kill(this);
        } else if (interactionObject.isActive()) {

        } else {
            interactionObject.activate();
        }
    }
}
