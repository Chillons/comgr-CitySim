package ch.fhnw.comgr.citysim.util;

import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public enum TaxiType {
    LONDON_CAB("londonCab/London_Cab", Mat4.multiply(Mat4.rotate(90, 1,0,0),Mat4.translate(new Vec3(0.76f,0,-0.5f)),Mat4.scale(0.055f))),
    YELLOW_CAB("yellowCab/YellowCab", Mat4.multiply(Mat4.rotate(90, 1,0,0),Mat4.translate(new Vec3(0.76f,0,-0.5f)),Mat4.scale(0.0006f))),
    TUKTUK("tuktuk/3 wheel Tuck Tuck sri lanka", Mat4.multiply(Mat4.rotate(90, 1,0,0),Mat4.translate(new Vec3(0.76f,0,-0.5f)),Mat4.scale(0.055f)));

    private final String name;
    private final Mat4 transform;


    private TaxiType(String name, Mat4 transform) {
        this.name = name;
        this.transform = transform;
    }

    public Mat4 getTransform() {
        return transform;
    }

    @Override
    public String toString() {
        return name;
    }
}
