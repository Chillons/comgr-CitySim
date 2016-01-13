package ch.fhnw.comgr.citysim.model.map.layer;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.mesh.IMesh;

import java.util.ArrayList;
import java.util.List;

public class InteractionObject extends LayerObject {

    private boolean active;
    private double activationTime;



    private final List<IMesh> activatedMeshes;
    private final List<IMesh> deactivatedMeshes;
    private final Field field;

    public InteractionObject(IMesh staticMesh, IMesh deactivatedMesh, IMesh activatedMesh, Field field) {
        super(staticMesh);
        this.field = field;

        this.activatedMeshes = new ArrayList<>();
        this.activatedMeshes.add(activatedMesh);
        this.deactivatedMeshes = new ArrayList<>();
        this.deactivatedMeshes.add(deactivatedMesh);

        setBaseTransformations();

        CitySimGame.scene.add3DObjects(deactivatedMeshes);
    }

    public InteractionObject(List<IMesh> staticMeshes, List<IMesh> deactivatedMeshes, List<IMesh> activatedMeshes, Field field) {
        super(staticMeshes);
        this.activatedMeshes = activatedMeshes;
        this.deactivatedMeshes = deactivatedMeshes;
        this.field = field;

        setBaseTransformations();

        CitySimGame.scene.add3DObjects(staticMeshes);
        CitySimGame.scene.add3DObjects(deactivatedMeshes);
    }

    @Override
    public List<IMesh> getMesh() {
        List<IMesh> returns = new ArrayList<>();
        returns.addAll(super.getMesh());
        returns.addAll(activatedMeshes);
        returns.addAll(deactivatedMeshes);
        return returns;
    }

    public void deactivate() {
        active = false;
        CitySimGame.scene.add3DObjects(deactivatedMeshes);
        CitySimGame.scene.remove3DObjects(activatedMeshes);
    }

    public void activate() {
        active = true;
        CitySimGame.scene.add3DObjects(activatedMeshes);
        CitySimGame.scene.remove3DObjects(deactivatedMeshes);
    }

    private void setBaseTransformations() {
        for (IMesh mesh: activatedMeshes) {
            mesh.setTransform(baseTransformations.get(0));
        }
        for (IMesh mesh: deactivatedMeshes) {
            mesh.setTransform(baseTransformations.get(0));
        }
    }


    public double getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(double activationTime) {
        this.activationTime = activationTime;
    }

    public boolean isActive() {
        return active;
    }

    public Field getField() {
        return field;
    }
}
