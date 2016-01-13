package ch.fhnw.comgr.citysim.action;

import ch.fhnw.comgr.citysim.model.map.CitySimMap;
import ch.fhnw.comgr.citysim.model.map.layer.InteractionObject;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler.IAnimationAction;

public class TrafficLightAction implements IAnimationAction {

    private final InteractionObject interactionObject;
    private final IController controller;

    private int[] tmpAuthorization;

    public TrafficLightAction(InteractionObject interactionObject, IController controller) {
        this.interactionObject = interactionObject;
        this.controller = controller;
        setAuthorizationsToFalse();
    }

    @Override
    public void run(double time, double interval) {
        if (time - interactionObject.getActivationTime() > 5) {
            interactionObject.deactivate();
            revertAuthorizations();
            System.out.println("Time is up! Resetting traffic light at " + time);
            controller.kill(this);
        } else if (!interactionObject.isActive()) {
            interactionObject.activate();
        }
    }


    private void setAuthorizationsToFalse() {

        int[] old = interactionObject.getField().getAuthorisations();
        tmpAuthorization = new int[old.length];
        System.arraycopy(old, 0, tmpAuthorization, 0, old.length);

        interactionObject.getField().setAuthorisations(CitySimMap.GRA);
    }

    private void revertAuthorizations() {
        interactionObject.getField().setAuthorisations(tmpAuthorization);
    }
}
