package ch.fhnw.comgr.citysim.action;

import java.util.Arrays;
import java.util.LinkedList;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.comgr.citysim.PathAlgorithm;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.model.Taxi;
import ch.fhnw.ether.controller.event.IEventScheduler.IAnimationAction;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public class DriveAnimation implements IAnimationAction {

	
	private Field carPositionAsField;
	private Field tempCarPositionAsField;
	private Field target;
	private Field tempTarget;
	Vec3 carPositionAsVector;
	Vec3 newCarPosition;

	float rotation = 0;

	volatile int[] authorisations;
	int entryPointOfTaxi;
	LinkedList<Field> stations;
	Field interStation;
	boolean run = false;
	boolean turnLeft = false;
	boolean turnRight = false;
	boolean turnBack = false;
	boolean authorisedToTurn = false;
	int stationCounter;
	Taxi animatedTaxi;
	String[] instruction = new String[2];
	
	private final CityController controller;
	
	public DriveAnimation(Taxi t, CityController controller){
		this.animatedTaxi = t;
		// Initial Position is 0 0
		this.carPositionAsField = CityController.getField(Vec3.ZERO);
		this.tempCarPositionAsField = null;
		this.stations = new LinkedList<Field>();
		this.interStation = CityController.getField(Vec3.ZERO);

		this.target = CityController.getField(Vec3.ZERO);
		this.tempTarget = this.target;
		
		this.controller = controller;
	}
	
	
	public Field getTarget() {
		return target;
	}

	public void setTarget(Field target) {
		this.target = target;
	}
	




	@Override
	public void run(double time, double interval) {
		
		carPositionAsVector = animatedTaxi.getStartTransform().postMultiply(animatedTaxi.getTransform()).transform(new Vec3(0, 0, 0));
		carPositionAsField = CityController.getField(carPositionAsVector);


		if (!target.equals(tempTarget)) {
			// Es wurde auf einem neuen Field geklickt
			if(!carPositionAsField.equals(tempTarget)){
				instruction[0] = "Ich habe bereits einen Fahrziel.";
				instruction[1] = "Bitte hab ein wenig Geduld für die Setzung eines neuen Fahrziel.";
				CityController.getInstructionField().sendInstruction(instruction);
				target = tempTarget; // reset
				
			}else{				
					stations = PathAlgorithm.getPathFromTo(carPositionAsField, target);
					if (stations != null) {
						// es wurde auf eine Kreuzung geklickt
						stationCounter = 1;
						interStation = stations.get(stationCounter);
						run = true;
						
						controller.setCurrentTool(CitySimGame.gameTool);
						
						instruction[0] = "Danke. Ich fahre sofort zu meinem neuen Fahrziel, das " + target.getName() + ".";
						instruction[1] = "Dieser Fahrt entspricht eine Distanz von " + PathAlgorithm.getDistanceFromTo(tempTarget, target) +
										 "Km. Das sind ungefähr " + PathAlgorithm.getTimeFromTo(tempTarget, target) + " Minuten Fahrt.";				
						CityController.getInstructionField().sendInstruction(instruction);
						
						
					} else {
						target = tempTarget; // reset
						
						instruction[0] = "Ich kann nur bei Kreuzungen halten";
						instruction[1] = "Bitte wähle eine Kreuzung der Stadt aus.";				
						CityController.getInstructionField().sendInstruction(instruction);
					}
					tempTarget = target;
			}

		}
		

		if (run & carPositionAsField.equals(interStation)) {
			// Die InterStation wurde erreicht
			if (stations.size() > stationCounter) {
				// Go to the next Station
				interStation = stations.get(stationCounter);
				stationCounter++;
			} else {
				// Ziel wurde erreicht
				run = false;
				controller.setCurrentTool(CitySimGame.taxiMoverTool);
				
				instruction[0] = "Das Ziel wurde erreicht, mit einer Verzögerung von 0 Minuten gemäss Fahrplan.";
				instruction[1] = "Hast du nun eine neue Wunsch-Destination?";				
				CityController.getInstructionField().sendInstruction(instruction);
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

			if (authorisations[0] == 0 && authorisations[1] == 0 && authorisations[2] == 0 && authorisations[3] == 0) {
				authorisations = carPositionAsField.getAuthorisations();
				return;
			}
			
			
			// North East
			if (entryPointOfTaxi == 0 && authorisations[2] == 1 && !turnLeft
					&& authorisedToTurn) {
				// turn left
				if (carPositionAsField.getPosition().x < interStation
						.getPosition().x) {
					//System.out.println("NE");
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
					//System.out.println("NW");
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
					//System.out.println("NN");
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
					//System.out.println("SE");
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
					//System.out.println("SW");
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
					//System.out.println("SS");
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
					//System.out.println("EN");
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
					//System.out.println("ES");
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
					//System.out.println("EE");
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
					//System.out.println("WN");
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
					//System.out.println("WS");
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
					//System.out.println("WW");
					turnLeft = false;
					turnRight = false;
					turnBack = true;
					rotation += 1;
				}
			}

			if (turnLeft) {
				geradeFahren(18);
			} else if (turnRight) {
				geradeFahren(10);
			} else if (turnBack) {
				geradeFahren(4);
			} else {
				geradeFahren(30);
			}

			if (turnLeft && rotation % 91 != 0 && authorisedToTurn) {
				animatedTaxi.addTransform(Mat4.rotate(1f, new Vec3(0, 1, 0)));
				rotation += 1;
			}

			if (turnRight && rotation % 91 != 0 && authorisedToTurn) {
				animatedTaxi.addTransform(Mat4.rotate(-1f, new Vec3(0, 1, 0)));
				rotation -= 1;
			}

			if (turnBack && rotation % 181 != 0 && authorisedToTurn) {
				animatedTaxi.addTransform(Mat4.rotate(+1f, new Vec3(0, 1, 0)));
				rotation += 1;
			}

			
			if (rotation % 91== 0 && (turnLeft || turnRight)) {
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
		animatedTaxi.addTransform(Mat4.translate(0, 0, tempo));
	}


}
