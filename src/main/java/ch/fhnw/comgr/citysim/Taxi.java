package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.util.TaxiLoader;
import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.controller.event.IEventScheduler.IAnimationAction;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;
import ch.fhnw.util.math.geometry.BoundingBox;

import java.util.List;

/**
 * Created by maurice on 11/12/15.
 */
public class Taxi {

	private Mat4 baseTransformation;
	private Mat4 transform;
	private List<IMesh> taxi;
	private TaxiType taxiType;
	private Field position;
	public Field getPosition() {
		return position;
	}

	public void setPosition(Field position) {
		this.position = position;
	}

	private Field target;

	private CityController controller;

	private PathAlgorithm al;
	
	private BoundingBox bb;

	public Taxi(TaxiType taxiType, CityController controller) {
		this.taxiType = taxiType;
		this.controller = controller;
		this.transform = Mat4.ID;
		// Get Meshes from File
		this.taxi = TaxiLoader.getTaxi(taxiType);

		// Get Starttransformation
		this.baseTransformation = taxiType.getTransform();
		transform = (Mat4.translate(new Vec3(-650, 0, 0)));

		// Update transformationmatrix to meshes
		update();

		// Initial Position is 0 0
		this.position = controller.getField(Vec3.ZERO);
		this.target = controller.getField(Vec3.ZERO);

		al = new PathAlgorithm(controller.getFields());
	}
	
	public void updateBoundingBox() {
		// TODO: Laeuft das so?????
		bb = new BoundingBox();
		taxi.forEach(m -> bb.add(m.getBounds()));
	}

	public List<IMesh> getMesh() {
		return taxi;
	}

	/**
	 * Transform all Meshes with the actual Transformmatrix
	 */
	public void update() {
		for (IMesh mesh : taxi) {
			mesh.setTransform(baseTransformation.postMultiply(transform));
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

	public Field getTarget() {
		return target;
	}

	public void setTarget(Field target) {
		this.target = target;
	}
	
	public Vec3 getTaxiPosition() {
		return baseTransformation.postMultiply(transform).transform(new Vec3(0, 0, 0));
	}

	Vec3 carPosition;
	Vec3 newCarPosition;

	float angleCar = 0;

	boolean turnLeft = false;
	boolean turnRight = false;

	
	public void geradeFahren(float tempo) {
		addTransform(Mat4.translate(0, 0, tempo));
	}

	public BoundingBox getBoundingBox() {
		updateBoundingBox();
		return bb;
	}
}
