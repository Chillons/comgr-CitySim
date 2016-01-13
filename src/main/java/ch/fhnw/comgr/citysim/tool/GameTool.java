package ch.fhnw.comgr.citysim.tool;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.comgr.citysim.action.ConstructionSiteAction;
import ch.fhnw.comgr.citysim.action.TrafficLightAction;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.model.map.CitySimMap;
import ch.fhnw.comgr.citysim.model.map.layer.InteractionObject;
import ch.fhnw.comgr.citysim.model.map.layer.StaticObject;
import ch.fhnw.ether.controller.event.IPointerEvent;
import ch.fhnw.ether.controller.tool.AbstractTool;
import ch.fhnw.ether.controller.tool.PickUtilities;
import ch.fhnw.ether.controller.tool.PickUtilities.PickMode;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;
import ch.fhnw.util.math.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class GameTool extends AbstractTool {
		
		private CityController cityController;
		
		public GameTool(CityController controller) {
			super(controller);
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
				Collection<I3DObject> pickableObjects = pickables.values();
				List<InteractionObject> interactionObjects = CitySimMap.getInstance().getInteractionObjects();

				boolean trafficLightFound = false;
				List<InteractionObject> foundTrafficLights = new ArrayList<>();

				// Check for each interaction Object
				for (InteractionObject interactionObject : interactionObjects) {
					// intersected with each picked Object
					for (I3DObject currentObject : pickableObjects) {
						// If the picked object is in one of the interaction objects
						//noinspection SuspiciousMethodCalls
						if (interactionObject.getMesh().contains(currentObject)) {
							System.out.println("Traffic light found!");
							if (!foundTrafficLights.contains(interactionObject) && !interactionObject.isActive()) {
								foundTrafficLights.add(interactionObject);
								trafficLightFound = true;
								System.out.println("Setting color to red");
								break;
							} else {
								System.out.println("But already red!");
							}
						}
					}
				}

				if (trafficLightFound) {
					if (foundTrafficLights.size() == 1 ) {
						double currentTime = getController().getScheduler().getTime();
						System.out.println("activation Time = " + currentTime);
						InteractionObject trafficLight = foundTrafficLights.get(0);

						trafficLight.activate();
						trafficLight.setActivationTime(currentTime);

						getCityController().animate(new TrafficLightAction(trafficLight, getCityController()));

					} else {
						System.err.println("More than one traffice light found with a click. This should never happen!");
					}
				}

				// No traffic lignt, add a construction site
				if (pickables.values().size() == 1 && !trafficLightFound ) {
					
					Field f = CityController.getField(pickables.values().iterator().next().getPosition());
					int fieldType = f.getContent();
					
					if (fieldType == CitySimMap.N_S ||fieldType == CitySimMap.E_W) {
						int actualCnt = CitySimGame.counterConstructions.get();
						
						if (actualCnt < 3) {
							int neu = actualCnt + 1;
							if (!CitySimGame.counterConstructions.compareAndSet(actualCnt, neu)) {
								return;
							}
							IMesh mesh = MeshUtilities.createCube();
							ConstructionSiteAction csAction = new ConstructionSiteAction(mesh, getCityController(), f);
							getCityController().animate(csAction);
						}
					}
				}
			}
		}
	}


