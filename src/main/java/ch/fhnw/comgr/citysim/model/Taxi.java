package ch.fhnw.comgr.citysim.model;

import ch.fhnw.comgr.citysim.pathAlgorithm.PathAlgorithm;
import ch.fhnw.comgr.citysim.util.AssetsLoader;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

import java.util.List;

public class Taxi{

	private Mat4 startTransform;
	private Mat4 transform;
	private List<IMesh> taxi;
	private TaxiType taxiType;
	private PathAlgorithm pathAlgorithm;



	public Taxi(TaxiType taxiType, PathAlgorithm pathAlgorithm) {
		this.taxiType = taxiType;
		this.pathAlgorithm = pathAlgorithm;
		this.transform = Mat4.ID;
		// Get Meshes from File
		this.taxi = AssetsLoader.getObject(taxiType.toString());

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


	public PathAlgorithm getPathAlgorithm() {
		return pathAlgorithm;
	}

	public void setPathAlgorithm(PathAlgorithm pathAlgorithm) {
		this.pathAlgorithm = pathAlgorithm;
	}
}
