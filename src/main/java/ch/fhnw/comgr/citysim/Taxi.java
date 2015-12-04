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
	private Field tempPosition;
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
		this.tempPosition = null;
		
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

	int[] authorisations;
	int entryPointOfTaxi;
	boolean turnLeft = false;
	boolean turnRight = false;
	boolean authorisedToTurn = false;
	

	@Override
	public void run(double time, double interval) {

		carPosition = startTransform.postMultiply(transform).transform(
				new Vec3(0, 0, 0));

		newCarPosition = target.getPosition();
		
		position = controller.getField(carPosition);
		boolean run = true;
		al.execute(position);
		
		Vec3 roundedCarPosition = new Vec3((int) carPosition.x,
				(int) (carPosition.y), (int) carPosition.z);
		
		
		if (!position.equals(target) && run) {
			
			if(position != tempPosition){
				// es wurde ein neues Field beigetreten
				entryPointOfTaxi = position.getEntryPointOfTaxiWithinTheField(carPosition);
				authorisations = position.getAuthorisations();
				authorisedToTurn = true;
			}
		
			tempPosition = position;
					
			// North East
			if(entryPointOfTaxi == 0 && authorisations[2] == 1 && !turnLeft && authorisedToTurn){
				//turn left	
				if (position.getPosition().x < newCarPosition.x){
					System.out.println("NE");
					turnLeft = true;
					turnRight = false;
					angleCar += 1;
				}
			}
						
			// North West
			if(entryPointOfTaxi == 0 && authorisations[3] == 1 && !turnRight && authorisedToTurn){
				//turn right
				if (position.getPosition().x > newCarPosition.x){
					System.out.println("NW");
					turnLeft = false;
					turnRight = true;
					angleCar -= 1;
				}
			}
						
			// South East
			if(entryPointOfTaxi == 1 && authorisations[2] == 1 && !turnRight && authorisedToTurn){
				//turn right
				if (position.getPosition().x < newCarPosition.x){
					System.out.println("SE");
					turnLeft = false;
					turnRight = true;
					angleCar -= 1;
				}
			}
			
			// South West
			if(entryPointOfTaxi == 1 && authorisations[3] == 1 && !turnLeft && authorisedToTurn){
				//turn left
				if (position.getPosition().x > newCarPosition.x){
					System.out.println("SW");
					turnLeft = true;
					turnRight = false;
					angleCar += 1;
				}
			}
			
			// East North
			if(entryPointOfTaxi == 2 && authorisations[0] == 1 && !turnRight && authorisedToTurn){
				//turn right
				if (position.getPosition().y < newCarPosition.y){
					System.out.println("EN");
					turnLeft = false;
					turnRight = true;
					angleCar -= 1;
				}
			}
					
			// East South
			if(entryPointOfTaxi == 2 && authorisations[1] == 1 && !turnLeft && authorisedToTurn){
				//turn left
				if (position.getPosition().y > newCarPosition.y){
					System.out.println("ES");
					turnLeft = true;
					turnRight = false;
					angleCar += 1;
				}
			}
						
			// West North
			if(entryPointOfTaxi == 3 && authorisations[0] == 1 && !turnLeft && authorisedToTurn){
				//turn left
				if (position.getPosition().y < newCarPosition.y){
					System.out.println("WN");
					turnLeft = true;
					turnRight = false;
					angleCar += 1;
				}
				
			}
			
			// West South
			if(entryPointOfTaxi == 3 && authorisations[1] == 1 && !turnRight && authorisedToTurn){
				//turn right
				if (position.getPosition().y > newCarPosition.y){
					System.out.println("WS");
					turnLeft = false;
					turnRight = true;
					angleCar -= 1;
				}
			}
			
						
			/*if (turnLeft) {
				geradeFahren(9);
			} else {
				geradeFahren(10);				
			}*/
			
			geradeFahren(10);
							
			if (turnLeft && angleCar % 90 != 0 && authorisedToTurn) {
				addTransform(Mat4.rotate(0.5f, new Vec3(0, 1, 0)));
				angleCar += 0.5;
			}
			
			if (turnRight && angleCar % 90 != 0 && authorisedToTurn) {
				addTransform(Mat4.rotate(-1f, new Vec3(0, 1, 0)));
				angleCar -= 1;
			}
			
			if(angleCar % 90 == 0){
				authorisedToTurn = false;
				turnLeft = false;
				turnRight = false;
			}
	}
					
	}

	public void geradeFahren(float tempo) {
		addTransform(Mat4.translate(0, 0, tempo));
	}

}
