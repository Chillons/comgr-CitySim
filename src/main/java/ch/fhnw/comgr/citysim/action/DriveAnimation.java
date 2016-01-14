package ch.fhnw.comgr.citysim.action;

import java.util.LinkedList;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.model.Taxi;
import ch.fhnw.comgr.citysim.ui.ScorePanel;
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
	
	private double plusTime = 0;
	private double expectedTime = 0;
	
	
	//* Drive Animation für Taxi *//
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

	//* Set Target löst die Animation aus /**
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
				// Das Taxi ist bereits am fahren
				instruction[0] = "Ich habe bereits ein Fahrziel.";
				instruction[1] = "Bitte hab ein wenig Geduld für die Setzung eines neuen Fahrziels.";
				CityController.getInstructionField().sendInstruction(instruction);
				target = tempTarget; // reset
				
			}else{				
					stations = animatedTaxi.getPathAlgorithm().getPathFromTo(carPositionAsField, target);
					if (stations != null) {
						// es wurde auf eine Kreuzung geklickt
						stationCounter = 1;
						interStation = stations.get(stationCounter);
						run = true;
						
						// Tool switchen
						controller.setCurrentTool(CitySimGame.gameTool);
						
						// Get time
						expectedTime = animatedTaxi.getPathAlgorithm().getTimeFromTo(tempTarget, target);
						// Kommentar an User
						instruction[0] = "Danke. Ich fahre sofort zu meinem neuen Fahrziel, das " + target.getName() + ".";
						instruction[1] = "Diese Fahrt entspricht einer Distanz von " + animatedTaxi.getPathAlgorithm().getDistanceFromTo(tempTarget, target) +
										 "Km. Das sind ungefähr " + expectedTime + " Minuten Fahrt.";				
						CityController.getInstructionField().sendInstruction(instruction);
						
						
					} else {
						
						target = tempTarget; // reset
						// Nicht auf Kreuzung geklickt
						instruction[0] = "Ich kann nur bei Kreuzungen halten";
						instruction[1] = "Bitte wähle eine Kreuzung der Stadt aus.";	
						// Send Message
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
				
				// Vorbereite txt Verzögerung
				instruction[0] = "Das Ziel wurde erreicht, mit einer Verzögerung von " + Math.round(plusTime) + " Minuten gemäss Fahrplan.";
				instruction[1] = "Hast du nun eine neue Wunsch-Destination?";
				
				// Berechnet Score
				CitySimGame.time += (-((int)expectedTime)/2 + Math.round(plusTime));

				// Send Score
				String[] score = new String[2];
				score[0] = "Deine Punktzahl:";
				score[1] = Double.toString(CitySimGame.time);
				controller.getRenderManager().removeMesh(CitySimGame.scorePanel.getMesh());
				CitySimGame.scorePanel = new ScorePanel();
				CitySimGame.scorePanel.sendScore(score);
				controller.getRenderManager().addMesh(CitySimGame.scorePanel.getMesh());
				plusTime = 0;
				// Ermittle Verzögerung
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

			// Get Authorisation fürs Weiterfahren
			if (authorisations[0] == 0 && authorisations[1] == 0 && authorisations[2] == 0 && authorisations[3] == 0) {
				authorisations = carPositionAsField.getAuthorisations();
				plusTime += interval;
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

			
			// Geschwindigkeit bei den Kurven
			if (turnLeft) {
				geradeFahren(18);
			} else if (turnRight) {
				geradeFahren(10);
			} else if (turnBack) {
				geradeFahren(4);
			} else {
				geradeFahren(30);
			}

			// Rotation links
			if (turnLeft && rotation % 91 != 0 && authorisedToTurn) {
				animatedTaxi.addTransform(Mat4.rotate(1f, new Vec3(0, 1, 0)));
				rotation += 1;
			}

			// Rotation rechts
			if (turnRight && rotation % 91 != 0 && authorisedToTurn) {
				animatedTaxi.addTransform(Mat4.rotate(-1f, new Vec3(0, 1, 0)));
				rotation -= 1;
			}

			// Turn Back
			if (turnBack && rotation % 181 != 0 && authorisedToTurn) {
				animatedTaxi.addTransform(Mat4.rotate(+1f, new Vec3(0, 1, 0)));
				rotation += 1;
			}

			
			// Turn left oder right ist fertig
			if (rotation % 91== 0 && (turnLeft || turnRight)) {
				authorisedToTurn = false;
				turnLeft = false;
				turnRight = false;
				turnBack = false;
				rotation = 0;	
			}

			// turn back ist fertig
			if (rotation % 181 == 0 && turnBack) {
				authorisedToTurn = false;
				turnLeft = false;
				turnRight = false;
				turnBack = false;
				rotation = 0;
			}

		}
		
	}

	// Transform fürs Gerade fahren
	public void geradeFahren(float tempo) {
		animatedTaxi.addTransform(Mat4.translate(0, 0, tempo));
	}


}
