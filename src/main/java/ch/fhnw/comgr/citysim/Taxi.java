package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.util.TaxiLoader;
import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.controller.event.IEventScheduler.IAnimationAction;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

import java.util.List;

/**
 * Created by maurice on 11/12/15.
 */
public class Taxi implements IAnimationAction {

	private static final float EPSILON = 0.0001f;
	private Mat4 startTransform;
	private Mat4 transform;
	private List<IMesh> taxi;
	private TaxiType taxiType;
	private Field position;
	private Field target;

	private CityController controller;

	PathAlgorithm al;

	public Taxi(TaxiType taxiType, CityController controller) {
		this.taxiType = taxiType;
		this.controller = controller;
		this.transform = Mat4.ID;
		// Get Meshes from File
		this.taxi = TaxiLoader.getTaxi(taxiType);

		// Get Starttransformation
		this.startTransform = taxiType.getTransform();
		transform = (Mat4.translate(new Vec3(-650, 0, 0)));

		// Update transformationmatrix to meshes
		update();

		// Initial Position is 0 0
		this.position = controller.getField(Vec3.ZERO);
		this.target = controller.getField(Vec3.ZERO);

		al = new PathAlgorithm(controller.getFields());
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

	public Field getTarget() {
		return target;
	}

	public void setTarget(Field target) {
		this.target = target;
	}

	Vec3 carPosition;
	Vec3 newCarPosition;

	float angleCar = 0;

	boolean turnLeft = false;
	boolean turnRight = false;

	@Override
	public void run(double time, double interval) {
		System.out.println("Position Car: "
				+ startTransform.postMultiply(transform).transform(
						new Vec3(0, 0, 0)));
		carPosition = startTransform.postMultiply(transform).transform(
				new Vec3(0, 0, 0));
		newCarPosition = target.getPosition();
		Vec3 roundedCardPosition = new Vec3((int) carPosition.x,
				(int) (carPosition.y), (int) carPosition.z);
		System.out.println("Feld von Taxi"
				+ controller.getField(roundedCardPosition));

		position = controller.getField(roundedCardPosition);

		al.execute(position);

		boolean run = true;
		
		if (!roundedCardPosition.equals(newCarPosition) && run) {
			System.out.println("Carposition:  " + roundedCardPosition);
			System.out.println("NeuePosition: " + newCarPosition);

			if (turnLeft) {
				geradeFahren(9);
			} else {
				geradeFahren(10);				
			}
			
			if (turnLeft && angleCar % 90 != 0) {
				addTransform(Mat4.rotate(0.5f, new Vec3(0, 1, 0)));
				angleCar += 0.5;
			}
			
			if (turnRight && angleCar % 90 != 0) {
				addTransform(Mat4.rotate(-1f, new Vec3(0, 1, 0)));
				angleCar -= 1;
			}
			
			if (position.getPosition().x == 0 && position.getPosition().y == -2
					&& !turnLeft) {
				turnLeft = true;
				angleCar += 1;
			}
			if (position.getPosition().x == 1 && position.getPosition().y == -2
					&& !turnRight) {
				turnLeft = false;
				turnRight = true;
				angleCar -= 1;
			}
			
			if (position.getPosition().x == 1 && position.getPosition().y == -4) {
				run = false;
			}

		}
	}

	public void moveEast(float distance) {
		addTransform(Mat4.translate(10f, 0, 10));
	}

	public void moveWest(float distance) {
		addTransform(Mat4.translate(10f, 0, 10));
	}

	public void moveNorth(float distance) {
		addTransform(Mat4.translate(10f, 0, 10));
	}

	public void moveSouth(float distance) {
		addTransform(Mat4.translate(10f, 0, 10));
	}

	public void geradeFahren(float tempo) {
		addTransform(Mat4.translate(0, 0, tempo));
	}

}
