package ch.fhnw.comgr.citysim.model;

import ch.fhnw.comgr.citysim.util.TaxiLoader;
import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.controller.event.IEventScheduler.IAnimationAction;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

import java.util.LinkedList;
import java.util.List;

public class Taxi{

	private Mat4 startTransform;
	private Mat4 transform;
	private List<IMesh> taxi;
	private TaxiType taxiType;



	public Taxi(TaxiType taxiType) {
		this.taxiType = taxiType;
		this.transform = Mat4.ID;
		// Get Meshes from File
		this.taxi = TaxiLoader.getTaxi(taxiType);

		// Get Starttransformation
		this.startTransform = taxiType.getTransform();
		transform = (Mat4.translate(new Vec3(-650, 0, 0)));

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
			mesh.setTransform(startTransform.postMultiply(transform));
		}
	}

	/**
	 * Set Transformation Matrix. Initial Transformation will automaticly be
	 * added.
	 *
	 * @param mat4s
	 */
	public void setTransform(Mat4... mat4s) {
		for (Mat4 mat4 : mat4s) {
			transform = transform.postMultiply(mat4);
		}
		update();
	}

	/**
	 * Add Transformation
	 */
	public void addTransform(Mat4... mat4s) {
		for (Mat4 mat4 : mat4s) {
			transform = transform.postMultiply(mat4);
		}
		update();
	}

	public Mat4 getTransform() {
		return transform;
	}

	public Mat4 getStartTransform(){
		return startTransform;
	}


}
