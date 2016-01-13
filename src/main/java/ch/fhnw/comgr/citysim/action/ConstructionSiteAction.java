package ch.fhnw.comgr.citysim.action;

import java.util.Arrays;

import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.model.map.CitySimMap;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Vec3;

public class ConstructionSiteAction implements IEventScheduler.IAnimationAction {

    private final IMesh constructionObject;
    private final IController controller;
    private final double activationTime;
    private final Field field;
    
    private int[] tmpAuthorization;

    public ConstructionSiteAction(IMesh interactionObject, IController controller, Field field) {
        this.constructionObject = interactionObject;
        this.controller = controller;
        this.field = field;
        System.out.println(field.getName());
        addConstruction();
        activationTime = controller.getScheduler().getTime();
        setAuthoToFalse();
    }
    
    public void setAuthoToFalse() {
    	int[] old = field.getAuthorisations();
    	tmpAuthorization = new int[old.length];
    	for (int i = 0; i < old.length; i++) {
    		tmpAuthorization[i] = old[i];
    	}
    	
    	field.setAuthorisations(CitySimMap.GRA);
    }
    
    public void setAuthoBack() {
    	field.setAuthorisations(tmpAuthorization);
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
            CitySimGame.counterConstructions.decrementAndGet();
            setAuthoBack();
            controller.kill(this);
        }
    }
}
