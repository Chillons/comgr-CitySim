package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.util.TaxiLoader;
import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

import java.util.List;

/**
 * Created by maurice on 11/12/15.
 */
public class Taxi {

    private Mat4 transform;

    private List<IMesh> taxi;

    private TaxiType taxiType;

    public Taxi(TaxiType taxiType) {
        taxi = TaxiLoader.getTaxi(taxiType);
        this.taxiType = taxiType;
        this.transform = taxiType.getTransform();
    }

    public List<IMesh> getMesh() {
        return taxi;
    }

    public Mat4 getTransform() {
        return transform;
    }

    public void setTransform(Mat4...mat4s) {
        Mat4 transform = taxiType.getTransform();
        for (Mat4 mat4 : mat4s) {
            transform = transform.postMultiply(mat4);
        }
        this.transform = transform;

        for (IMesh mesh : taxi) {
            mesh.setTransform(transform);
        }
    }
}
