package ch.fhnw.comgr.citysim.tool;

import java.util.Map;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.action.DriveAnimation;
import ch.fhnw.comgr.citysim.model.Taxi;
import ch.fhnw.ether.controller.event.IPointerEvent;
import ch.fhnw.ether.controller.tool.AbstractTool;
import ch.fhnw.ether.controller.tool.PickUtilities;
import ch.fhnw.ether.controller.tool.PickUtilities.PickMode;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.util.math.Vec3;


public class TaxiMoverTool extends AbstractTool {

		private Taxi currentTaxi;
		private DriveAnimation driveAnimation;

		public TaxiMoverTool(CityController controller) {
			super(controller);
		}

		@Override
		public void pointerPressed(IPointerEvent e) {
			int x = e.getX();
			int y = e.getY();
			Map<Float, I3DObject> pickables = PickUtilities.pickFromScene(PickMode.POINT, x, y, 0, 0, e.getView());
			if (pickables.isEmpty())
				System.out.println("No Mesh Clicked");
			else{
				Vec3 newCarPosition = pickables.values().iterator().next().getPosition();

				driveAnimation.setTarget(CityController.getField(newCarPosition));
			}

		}

		public void setCurrentTaxi(Taxi t, DriveAnimation d){
			currentTaxi = t;
			driveAnimation = d;
		}
	}


