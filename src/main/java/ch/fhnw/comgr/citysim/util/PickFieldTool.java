package ch.fhnw.comgr.citysim.util;

import java.util.List;
import java.util.Map;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.Taxi;
import ch.fhnw.ether.controller.event.IPointerEvent;
import ch.fhnw.ether.controller.tool.AbstractTool;
import ch.fhnw.ether.controller.tool.PickUtilities;
import ch.fhnw.ether.controller.tool.PickUtilities.PickMode;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.util.math.Vec3;


public class PickFieldTool extends AbstractTool {
		
		private CityController cityController;
		
		public PickFieldTool(CityController controller) {
			super(controller);
			this.cityController = controller;
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
				System.out.println("No Mesh Clicked");
			else{		
				Vec3 newCarPosition = pickables.values().iterator().next().getPosition();
				Taxi actualTaxi = cityController.getTaxis().get(0);
				actualTaxi.setTarget(cityController.getField(newCarPosition));
				System.out.println("Click: X " + newCarPosition.x + " Y " + newCarPosition.y);
			}	
			
		}
	}


