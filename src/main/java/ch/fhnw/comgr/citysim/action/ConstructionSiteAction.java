package ch.fhnw.comgr.citysim.action;

import java.util.List;

import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.model.map.CitySimMap;
import ch.fhnw.comgr.citysim.util.AssetsLoader;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public class ConstructionSiteAction implements IEventScheduler.IAnimationAction {

    private final List<IMesh> obj;
    private final IController controller;
    private final double activationTime;
    private final Field field;
    
    private int[] tmpAuthorization;

    public ConstructionSiteAction(IMesh interactionObject, IController controller, Field field) {
        
        this.obj = AssetsLoader.getObject("construction/cone");
        
        this.controller = controller;
        this.field = field;
        System.out.println(field.getName());
        addConstruction();
        activationTime = controller.getScheduler().getTime();
        setAuthorizationsToFalse();
    }
    
    private void setAuthorizationsToFalse() {
    	int[] old = field.getAuthorisations();
    	tmpAuthorization = new int[old.length];
        System.arraycopy(old, 0, tmpAuthorization, 0, old.length);
    	
    	field.setAuthorisations(CitySimMap.GRA);
    }
    
    private void revertAuthorizations() {
    	field.setAuthorisations(tmpAuthorization);
    }
    
    public void addConstruction() {
    	Vec3 pos = field.getPosition();
    	
    	Mat4 trans = Mat4.multiply(Mat4.rotate(90, 1,0,0),Mat4.rotate(90, 0,1,0) ,Mat4.translate(new Vec3(0.5f, 0.01f, 0.5f)),Mat4.scale(0.00375f));
        this.obj.forEach(o -> o.setTransform(trans.preMultiply(Mat4.translate(pos.x, pos.y, 0))));
    	
    	controller.getScene().add3DObjects(obj);
    }

    @Override
    public void run(double time, double interval) {
        if (time - activationTime > 5) {
            System.out.println("Time is up! Remove Construction Site");
            controller.getScene().remove3DObjects(obj);
            CitySimGame.counterConstructions.decrementAndGet();
            revertAuthorizations();
            controller.kill(this);
        }
    }
}
