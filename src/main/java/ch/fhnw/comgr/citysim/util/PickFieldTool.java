package ch.fhnw.comgr.citysim.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.Field;
import ch.fhnw.comgr.citysim.Taxi;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler.IAnimationAction;
import ch.fhnw.ether.controller.event.IPointerEvent;
import ch.fhnw.ether.controller.tool.AbstractTool;
import ch.fhnw.ether.controller.tool.PickUtilities;
import ch.fhnw.ether.controller.tool.PickUtilities.PickMode;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Vec3;


public class PickFieldTool extends AbstractTool {
	private Vec3 carPosition;
	private Vec3 newCarPosition;
	private LinkedList<Field> path;
	private CityEventScheduler eventScheduler;
		
		public PickFieldTool(CityController controller) {
			super(controller);
			Thread t = new Thread();
			eventScheduler = new CityEventScheduler(super.getController(), t, 100);
		}

		private CityController getCityController(){
			return (CityController)super.getController();
		}

		@Override
		public void pointerPressed(IPointerEvent e) {
			int x = e.getX();
			int y = e.getY();
			Map<Float, I3DObject> pickables = PickUtilities.pickFromScene(PickMode.POINT, x, y, 0, 0, e.getView());
			if (pickables.isEmpty())
				System.out.println("No Mesh");
			else{
				List<IMesh> actualTaxi = this.getCityController().getTaxis().get(0);
				carPosition = actualTaxi.get(0).getPosition();
				Field fromField = (Field)this.getCityController().getFieldAtPosition(carPosition);
				Field toField = (Field)pickables.values().iterator().next();
				path = fromField.getPathTo(toField);
				
				
				for(int i=1;i<path.size(); i++){
						DriveAction driveAction = new DriveAction(actualTaxi, fromField, path.get(i));
						eventScheduler.run(driveAction);
				}
			}				
		}
	}


