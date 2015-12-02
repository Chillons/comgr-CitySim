package ch.fhnw.comgr.citysim.model.map;

import ch.fhnw.util.math.Mat4;

public class InteractionObject extends LayerObject {

    private boolean active;
    private double activationTime;

    public void deactivate() {
        active = false;
        setTransform(Mat4.scale(1.0f));
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
