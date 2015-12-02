package ch.fhnw.comgr.citysim.model.map;

import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.ShadedMaterial;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.math.Mat4;

public class InteractionObject extends LayerObject {

    private boolean active;
    private double activationTime;

    public static final IMaterial redBlock = new ShadedMaterial(RGB.RED, RGB.RED, RGB.RED, RGB.WHITE, 10, 1, 1f);
    public static final IMaterial greenBlock = new ShadedMaterial(RGB.GREEN, RGB.GREEN, RGB.GREEN, RGB.WHITE, 10, 1, 1f);

    public InteractionObject(IMesh mesh) {
        super(mesh);
    }

    public void deactivate() {
        active = false;
        setTransform(null);
    }

    public void activate() {
        active = true;
        setTransform(Mat4.scale(0.5f));
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
