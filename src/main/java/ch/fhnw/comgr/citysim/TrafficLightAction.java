package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.model.map.InteractionObject;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;
import ch.fhnw.util.math.Mat4;

public class TrafficLightAction implements IEventScheduler.IAnimationAction {

    private final InteractionObject interactionObject;
    private final IController controller;

    public TrafficLightAction(InteractionObject interactionObject, IController controller) {
        this.interactionObject = interactionObject;
        this.controller = controller;
    }

    @Override
    public void run(double time, double interval) {
        if (time - interactionObject.getActivationTime() > 5) {
            interactionObject.deactivate();
            controller.kill(this);
        } else {
            interactionObject.activate();
        }
    }
}
