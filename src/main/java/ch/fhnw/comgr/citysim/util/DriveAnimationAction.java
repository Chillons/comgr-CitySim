package ch.fhnw.comgr.citysim.util;

import java.util.List;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.Field;
import ch.fhnw.comgr.citysim.Taxi;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public class DriveAnimationAction implements IEventScheduler.IAnimationAction{
	private Taxi taxi;
	private CityController controller;
	
	public DriveAnimationAction(Taxi taxi, CityController controller, List<Field> path){
		this.taxi = taxi;
		this.controller = controller;
	}
	
	Vec3 carPosition;
	Vec3 newCarPosition;

	float angleCar = 0;

	boolean turnLeft = false;
	boolean turnRight = false;
		
	public void run(double time, double interval){
		Vec3 carPosition = taxi.getTaxiPosition();
		System.out.println("Position Car: "
				+ carPosition);
		
		newCarPosition = taxi.getTarget().getPosition();
		Vec3 roundedCardPosition = new Vec3((int) carPosition.x,
				(int) (carPosition.y), (int) carPosition.z);
		System.out.println("Feld von Taxi"
				+ controller.getField(roundedCardPosition));

		taxi.setPosition(controller.getField(roundedCardPosition));


		boolean run = true;
		
		if (!roundedCardPosition.equals(newCarPosition) && run) {
			System.out.println("Carposition:  " + roundedCardPosition);
			System.out.println("NeuePosition: " + newCarPosition);

			if (turnLeft) {
				taxi.geradeFahren(9);
			} else {
				taxi.geradeFahren(10);				
			}
			
			if (turnLeft && angleCar % 90 != 0) {
				taxi.addTransform(Mat4.rotate(0.5f, new Vec3(0, 1, 0)));
				angleCar += 0.5;
			}
			
			if (turnRight && angleCar % 90 != 0) {
				taxi.addTransform(Mat4.rotate(-1f, new Vec3(0, 1, 0)));
				angleCar -= 1;
			}
			
			if (taxi.getPosition().getPosition().x == 0 && taxi.getPosition().getPosition().y == -2
					&& !turnLeft) {
				turnLeft = true;
				angleCar += 1;
			}
			if (taxi.getPosition().getPosition().x == 1 && taxi.getPosition().getPosition().y == -2
					&& !turnRight) {
				turnLeft = false;
				turnRight = true;
				angleCar -= 1;
			}
			
			if (taxi.getPosition().getPosition().x == 1 && taxi.getPosition().getPosition().y == -4) {
				run = false;
			}

		}

	}
}
