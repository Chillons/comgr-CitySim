package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.util.TaxiLoader;
import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.controller.event.IEventScheduler.IAnimationAction;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by maurice on 11/12/15.
 */
public class Taxi implements IAnimationAction {

	private Mat4 startTransform;
	private Mat4 transform;
	private List<IMesh> taxi;
	private TaxiType taxiType;
	private Field carPositionAsField;
	private Field tempCarPositionAsField;
	private Field target;
	private Field tempTarget;

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
		this.carPositionAsField = controller.getField(Vec3.ZERO);
		this.tempCarPositionAsField = null;
		this.stations = new LinkedList<Field>();
		this.interStation = controller.getField(Vec3.ZERO);

		this.target = controller.getField(Vec3.ZERO);
		this.tempTarget = this.target;

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

	Vec3 carPositionAsVector;
	Vec3 newCarPosition;

	float rotation = 0;

	int[] authorisations;
	int entryPointOfTaxi;
	LinkedList<Field> stations;
	Field interStation;
	boolean run = false;
	boolean turnLeft = false;
	boolean turnRight = false;
	boolean turnBack = false;
	boolean authorisedToTurn = false;
	int stationCounter;

	@Override
	public void run(double time, double interval) {

		carPositionAsVector = startTransform.postMultiply(transform).transform(new Vec3(0, 0, 0));
		carPositionAsField = controller.getField(carPositionAsVector);

		al.execute(carPositionAsField);

		if (!target.equals(tempTarget)) {
			// Es wurde auf einem neuen Field geklickt
			stations = al.getPathFromTo(carPositionAsField, target);
			
			if (stations != null) {
				// es wurde auf eine Kreuzung geklickt
				stationCounter = 1;
				interStation = stations.get(stationCounter);
				run = true;
			} else {
				System.out.println("No intersection clicked");
			}

		}
		tempTarget = target;

		if (run & carPositionAsField.equals(interStation)) {
			// Die InterStation wurde erreicht
			if (stations.size() > stationCounter) {
				// Go to the next Station
				interStation = stations.get(stationCounter);
				stationCounter++;
			} else {
				// Ziel wurde erreicht
				run = false;
			}
		}

		if (!carPositionAsField.equals(interStation) && run) {
			// Die InterStation wurde noch nicht erreicht

			if (carPositionAsField != tempCarPositionAsField) {
				// es wurde ein neues Field beigetreten
				entryPointOfTaxi = carPositionAsField
						.getEntryPointOfTaxiWithinTheField(carPositionAsVector);
				authorisations = carPositionAsField.getAuthorisations();
				authorisedToTurn = true;
			}

			tempCarPositionAsField = carPositionAsField;

			// North East
			if (entryPointOfTaxi == 0 && authorisations[2] == 1 && !turnLeft
					&& authorisedToTurn) {
				// turn left
				if (carPositionAsField.getPosition().x < interStation
						.getPosition().x) {
					System.out.println("NE");
					turnLeft = true;
					turnRight = false;
					turnBack = false;
					rotation += 1;
				}
			}

			// North West
			if (entryPointOfTaxi == 0 && authorisations[3] == 1 && !turnRight
					&& authorisedToTurn) {
				// turn right
				if (carPositionAsField.getPosition().x > interStation
						.getPosition().x) {
					System.out.println("NW");
					turnLeft = false;
					turnRight = true;
					turnBack = false;
					rotation -= 1;
				}
			}

			// North North
			if (entryPointOfTaxi == 0 && authorisations[0] == 1 && !turnBack
					&& authorisedToTurn) {
				// turn back
				if (carPositionAsField.getPosition().y < interStation
						.getPosition().y
						&& (carPositionAsField.getPosition().x == interStation
								.getPosition().x)) {
					System.out.println("NN");
					turnLeft = false;
					turnRight = false;
					turnBack = true;
					rotation += 1;
				}
			}

			// South East
			if (entryPointOfTaxi == 1 && authorisations[2] == 1 && !turnRight
					&& authorisedToTurn) {
				// turn right
				if (carPositionAsField.getPosition().x < interStation
						.getPosition().x) {
					System.out.println("SE");
					turnLeft = false;
					turnRight = true;
					turnBack = false;
					rotation -= 1;
				}
			}

			// South West
			if (entryPointOfTaxi == 1 && authorisations[3] == 1 && !turnLeft
					&& authorisedToTurn) {
				// turn left
				if (carPositionAsField.getPosition().x > interStation
						.getPosition().x) {
					System.out.println("SW");
					turnLeft = true;
					turnRight = false;
					turnBack = false;
					rotation += 1;
				}
			}

			// South South
			if (entryPointOfTaxi == 1 && authorisations[1] == 1 && !turnBack
					&& authorisedToTurn) {
				// turn back
				if (carPositionAsField.getPosition().y > interStation
						.getPosition().y
						&& (carPositionAsField.getPosition().x == interStation
								.getPosition().x)) {
					System.out.println("SS");
					turnLeft = false;
					turnRight = false;
					turnBack = true;
					rotation += 1;
				}
			}

			// East North
			if (entryPointOfTaxi == 2 && authorisations[0] == 1 && !turnRight
					&& authorisedToTurn) {
				// turn right
				if (carPositionAsField.getPosition().y < interStation
						.getPosition().y) {
					System.out.println("EN");
					turnLeft = false;
					turnRight = true;
					turnBack = false;
					rotation -= 1;
				}
			}

			// East South
			if (entryPointOfTaxi == 2 && authorisations[1] == 1 && !turnLeft
					&& authorisedToTurn) {
				// turn left
				if (carPositionAsField.getPosition().y > interStation
						.getPosition().y) {
					System.out.println("ES");
					turnLeft = true;
					turnRight = false;
					turnBack = false;
					rotation += 1;
				}
			}

			// East East
			if (entryPointOfTaxi == 2 && authorisations[2] == 1 && !turnBack
					&& authorisedToTurn) {
				// turn back
				if (carPositionAsField.getPosition().x < interStation
						.getPosition().x
						&& (carPositionAsField.getPosition().y == interStation
								.getPosition().y)) {
					System.out.println("EE");
					turnLeft = false;
					turnRight = false;
					turnBack = true;
					rotation += 1;
				}
			}

			// West North
			if (entryPointOfTaxi == 3 && authorisations[0] == 1 && !turnLeft
					&& authorisedToTurn) {
				// turn left
				if (carPositionAsField.getPosition().y < interStation
						.getPosition().y) {
					System.out.println("WN");
					turnLeft = true;
					turnRight = false;
					turnBack = false;
					rotation += 1;
				}

			}

			// West South
			if (entryPointOfTaxi == 3 && authorisations[1] == 1 && !turnRight
					&& authorisedToTurn) {
				// turn right
				if (carPositionAsField.getPosition().y > interStation
						.getPosition().y) {
					System.out.println("WS");
					turnLeft = false;
					turnRight = true;
					turnBack = false;
					rotation -= 1;
				}
			}

			// West West
			if (entryPointOfTaxi == 3 && authorisations[3] == 1 && !turnBack
					&& authorisedToTurn) {
				// turn back
				if (carPositionAsField.getPosition().x > interStation
						.getPosition().x
						&& (carPositionAsField.getPosition().y == interStation
								.getPosition().y)) {
					System.out.println("WW");
					turnLeft = false;
					turnRight = false;
					turnBack = true;
					rotation += 1;
				}
			}

			if (turnLeft) {
				geradeFahren(9);
			} else if (turnRight) {
				geradeFahren(10);
			} else if (turnBack) {
				geradeFahren(4);
			} else {
				geradeFahren(30);
			}

			if (turnLeft && rotation % 90 != 0 && authorisedToTurn) {
				addTransform(Mat4.rotate(0.5f, new Vec3(0, 1, 0)));
				rotation += 0.5;
			}

			if (turnRight && rotation % 90 != 0 && authorisedToTurn) {
				addTransform(Mat4.rotate(-1f, new Vec3(0, 1, 0)));
				rotation -= 1;
			}

			if (turnBack && rotation % 181 != 0 && authorisedToTurn) {
				addTransform(Mat4.rotate(+1f, new Vec3(0, 1, 0)));
				rotation += 1;
			}

			if (rotation % 90 == 0 && (turnLeft || turnRight)) {
				authorisedToTurn = false;
				turnLeft = false;
				turnRight = false;
				turnBack = false;
				rotation = 0;
			}

			if (rotation % 181 == 0 && turnBack) {
				authorisedToTurn = false;
				turnLeft = false;
				turnRight = false;
				turnBack = false;
				rotation = 0;
			}

		}
	}

	public void geradeFahren(float tempo) {
		addTransform(Mat4.translate(0, 0, tempo));
	}

}
