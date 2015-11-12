
package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.ui.Button;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.IView.ViewType;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.math.Vec3;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Logger;

public final class StreetExample {
	
	public static final int GRAS = 0;
	public static final int STREET_EAST_WEST = 1;
	public static final int STREET_NORTH_SOUTH = 2;
	public static final int CROSSING = 3;
	public static final int STREET_NORTH_EAST = 4;
	public static final int STREET_NORTH_WEST = 5;
	public static final int STREET_SOUTH_EAST = 6;
	public static final int STREET_SOUTH_WEST = 7;
	
	private final static Logger LOGGER = Logger.getLogger(StreetExample.class.getName());
	private List<IMesh> car;
	private CityBoard cityboard;
	
	float startX = -(strasse[0].length / 2.0f);
	float startY = (strasse.length / 2.0f);

	private Vec3 carPosition = new Vec3(0f, 0f, 0f);
	Vec3 newCarPosition= carPosition;
	
	
	public static final int[][] strasse = { 
			{ GRAS, 				GRAS, 				STREET_SOUTH_EAST }, 
			{ STREET_SOUTH_WEST, 	GRAS, 				STREET_NORTH_SOUTH}, 
			{ STREET_NORTH_SOUTH, 	GRAS, 				STREET_NORTH_SOUTH }, 
			{ CROSSING,  			STREET_EAST_WEST, 	CROSSING },
			{ STREET_NORTH_SOUTH, 	GRAS, 		 		STREET_NORTH_SOUTH },
			{ STREET_NORTH_WEST, 	GRAS,				STREET_NORTH_EAST} };

		
	public static void main(String[] args) {
		new StreetExample();
	}

	
	// Setup the whole thing
	public StreetExample() {
		// Create controller
		
		IController controller = new DefaultController();
		controller.run(time -> {
			// Create view
			// Neues Config machen
			IView view = new DefaultView(controller, 100, 100, 500, 500, new IView.Config(ViewType.INTERACTIVE_VIEW, 0, new IView.ViewFlag[0]), "City Sim");		
			ICamera camera = new Camera(new Vec3(0, -5, 5), Vec3.ZERO);

			// Create scene and add triangle
			IScene scene = new DefaultScene(controller);			
			controller.setScene(scene);
			
			scene.add3DObject(camera);
			controller.setCamera(view, camera);

			/////// CITY ////////
			cityboard = new CityBoard(strasse);	
			for (int i = 0; i < cityboard.getFields().length; i++) {
				for (int j = 0; j < cityboard.getFields()[0].length; j++) {
					scene.add3DObject(cityboard.getFields()[i][j]);	
				}
			}			
			/////// CAR //////////
			Taxi taxi = new Taxi(TaxiType.YELLOW_CAB);
			car = taxi.getMesh();
			for (IMesh mesh : car) {
				mesh.setTransform(taxi.getTransform());
			}
			scene.add3DObjects(car);
			
						
			controller.getUI().addWidget(new Button(2,5, "Feld 0 2", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				newCarPosition = cityboard.getNodes().get(0).getPosition(); 
			}));
			controller.getUI().addWidget(new Button(0,4, "Feld 1 0", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				newCarPosition = cityboard.getNodes().get(1).getPosition(); 
			}));
			controller.getUI().addWidget(new Button(0,2, "Feld 3 0", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				newCarPosition = cityboard.getNodes().get(2).getPosition(); 
			}));
			controller.getUI().addWidget(new Button(2,2, "Feld 3 2", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				newCarPosition = cityboard.getNodes().get(3).getPosition(); 
			}));
			controller.getUI().addWidget(new Button(0,0, "Feld 5 0", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				newCarPosition = cityboard.getNodes().get(4).getPosition(); 
			}));
			controller.getUI().addWidget(new Button(2, 0, "Feld 5 2", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				newCarPosition = cityboard.getNodes().get(5).getPosition();
			}));
		});
		
			
		controller.animate(new IEventScheduler.IAnimationAction() {		
			@Override
			public void run(double time, double interval) {
					
				if(newCarPosition.x > carPosition.x && newCarPosition.x - carPosition.x > 0.1){
					moveEast(carPosition.x - newCarPosition.x);
				}else if(newCarPosition.x < carPosition.x && newCarPosition.x - carPosition.x < 0.1){
					moveWest(carPosition.x - newCarPosition.x);
				}

				if(newCarPosition.y > carPosition.y && newCarPosition.y - carPosition.y > 0.1){
					moveNorth(carPosition.y - newCarPosition.y);
				}else if(newCarPosition.y < carPosition.y && newCarPosition.y - carPosition.y < 0.1){
					moveSouth(carPosition.y - newCarPosition.y);
				}

			}
		});
		
	}
	
	public void moveEast(float distance){
		carPosition = carPosition.add(new Vec3(0.1f,0,0));
		for (IMesh mesh : car) {
			mesh.setPosition(carPosition);
		}
	}
	
	
	public void moveWest(float distance){
		carPosition = carPosition.subtract(new Vec3(0.1f,0,0));
		for (IMesh mesh : car) {
			mesh.setPosition(carPosition);
		}
	}
	
	
	public void moveNorth(float distance){
		carPosition = carPosition.add(new Vec3(0,0.1f,0));
		for (IMesh mesh : car) {
			mesh.setPosition(carPosition);
		}
	}
	
	
	public void moveSouth(float distance){
		carPosition = carPosition.subtract(new Vec3(0,0.1f,0));
		for (IMesh mesh : car) {
			mesh.setPosition(carPosition);
		}
	}
		
		
}
