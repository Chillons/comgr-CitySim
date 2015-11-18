package ch.fhnw.comgr.citysim.util;


import java.util.LinkedList;
import java.util.List;

import ch.fhnw.comgr.citysim.Field;
import ch.fhnw.comgr.citysim.Taxi;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.controller.event.DefaultEventScheduler;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Vec3;

public class DriveAction implements IEventScheduler.IAction{
	
	private List<IMesh> taxi;
	private Vec3 carPosition;
	private Vec3 newCarPosition;
	private LinkedList<Field> path;
	
	
	public DriveAction(List<IMesh> taxi, Field fF, Field tF){
		this.taxi = taxi;
		this.carPosition = fF.getPosition();
		this.newCarPosition = tF.getPosition();
	}
	
	
	public void setCarPosition(Vec3 p){
		this.newCarPosition = p;
	}
	
	
	public void run(double time){
			for (IMesh mesh : taxi) {
	            mesh.setPosition(newCarPosition);
	        }	
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
	}
		
	
	
	
	/*if(newCarPosition.x > carPosition.x && newCarPosition.x - carPosition.x > 0.1){
			moveEast(newCarPosition.x - carPosition.x);
		}else if(newCarPosition.x < carPosition.x && newCarPosition.x - carPosition.x < 0.1){
			moveWest(carPosition.x - newCarPosition.x);
		}

		if(newCarPosition.y > carPosition.y && newCarPosition.y - carPosition.y > 0.1){
			moveNorth(newCarPosition.y - carPosition.y);
		}else if(newCarPosition.y < carPosition.y && newCarPosition.y - carPosition.y < 0.1){
			moveSouth(carPosition.y - newCarPosition.y);
		}/*

		
	}
	
	
	/*public void moveEast(float distance){
		carPosition = carPosition.add(new Vec3(0.1f,0,0));        
		for (IMesh mesh : taxi) {
            mesh.setPosition(carPosition);
        }

	}
	
	
	public void moveWest(float distance){
		carPosition = carPosition.subtract(new Vec3(0.1f,0,0));
		for (IMesh mesh : taxi) {
            mesh.setPosition(carPosition);
        }
	}
	
	
	public void moveNorth(float distance){
		carPosition = carPosition.add(new Vec3(0,0.1f,0));
		for (IMesh mesh : taxi) {
            mesh.setPosition(carPosition);
        }
	}
	
	
	public void moveSouth(float distance){
		carPosition = carPosition.subtract(new Vec3(0,0.1f,0));
		for (IMesh mesh : taxi) {
            mesh.setPosition(carPosition);
        }
	}*/
}
