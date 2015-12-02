package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.util.TaxiLoader;
import ch.fhnw.util.math.geometry.BoundingBox;
import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.scene.attribute.IAttribute;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.IMesh.Flag;
import ch.fhnw.ether.scene.mesh.IMesh.Queue;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.IGeometryAttribute;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.util.UpdateRequest;
import ch.fhnw.util.math.Mat3;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by maurice on 11/12/15.
 */
public class Taxi{

    private Mat4 transform;

    private List<IMesh> taxi;

    private TaxiType taxiType;
    
    private Field target;
    
    private CityController controller;
   
    public Taxi(TaxiType taxiType, CityController controller) {
    	this.taxiType = taxiType;
    	this.controller = controller;

    	// Get Meshes from File
        this.taxi = TaxiLoader.getTaxi(taxiType);
        
        // Get Starttransformation
        this.transform = taxiType.getTransform();
        
        // Update transformationmatrix to meshes
        update();
    }

    public List<IMesh> getMesh() {
        return taxi;
    }
    
    /**
     * Transform all Meshes with the actual Transformmatrix
     */
    public void update() {
		for (IMesh mesh : taxi) {
			mesh.setTransform(transform);
		}
    }

    /**
     * Set Transformation Matrix. Initial Transformation will automaticly be added.
     * @param mat4s
     */
    public void setTransform(Mat4...mat4s) {
        Mat4 transform = taxiType.getTransform();
        for (Mat4 mat4 : mat4s) {
            transform = transform.postMultiply(mat4);
        }
        this.transform = transform;

        update();
    }
    

	public Mat4 getTransform() {
		return transform;
	}

	public Field getTarget() {
		return target;
	}

	public void setTarget(Field target) {
		this.target = target;
	}


}
