package ch.fhnw.comgr.citysim.action;

import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Vec3;

public class ConstructionSiteAction implements IEventScheduler.IAnimationAction {

    private final IMesh constructionObject;
    private final IController controller;
    private final double activationTime;
    private final Field field;

    public ConstructionSiteAction(IMesh interactionObject, IController controller, Field field) {
        this.constructionObject = interactionObject;
        this.controller = controller;
        this.field = field;
        addConstruction();
        activationTime = controller.getScheduler().getTime();
    }
    
    public void addConstruction() {
    	Vec3 pos = field.getPosition();
    	constructionObject.setPosition(constructionObject.getPosition().add(new Vec3(pos.x + 0.5, pos.y + 0.5, 0)));
    	controller.getScene().add3DObject(constructionObject);
    }

    @Override
    public void run(double time, double interval) {
        if (time - activationTime > 5) {
            System.out.println("Time is up! Remove Construction Site");
            controller.getScene().remove3DObject(constructionObject);
            controller.kill(this);
            CitySimGame.counterConstructions.decrementAndGet();
        }
    }
}
