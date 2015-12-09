package ch.fhnw.comgr.citysim.model.map;

import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.ShadedMaterial;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.math.Mat4;

import java.util.ArrayList;
import java.util.List;

public class InteractionObject extends LayerObject {

    private boolean active;
    private double activationTime;

    private final List<IMesh> activatedMeshes;
    private final List<IMesh> deactivatedMeshes;

    public InteractionObject(IMesh staticMesh, IMesh activatedMesh, IMesh deactivatedMesh) {
        super(staticMesh);
        this.activatedMeshes = new ArrayList<>();
        this.activatedMeshes.add(activatedMesh);
        this.deactivatedMeshes = new ArrayList<>();
        this.deactivatedMeshes.add(deactivatedMesh);
    }

    public InteractionObject(List<IMesh> staticMeshes, List<IMesh> activatedMeshes, List<IMesh> deactivatedMeshes) {
        super(staticMeshes);
        this.activatedMeshes = activatedMeshes;
        this.deactivatedMeshes = deactivatedMeshes;
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
        for (IMesh mesh : activatedMeshes) {
            mesh.setTransform(Mat4.scale(0.01f));
        }
        for (IMesh mesh : deactivatedMeshes) {
            mesh.setTransform(Mat4.scale(1f));
        }

    }

    public void activate() {
        active = true;
        for (IMesh mesh : activatedMeshes) {
            mesh.setTransform(Mat4.scale(1f));
        }
        for (IMesh mesh : deactivatedMeshes) {
            mesh.setTransform(Mat4.scale(0.01f));
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
}
