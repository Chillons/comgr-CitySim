
package ch.fhnw.comgr.citysim;

import java.awt.event.KeyEvent;

import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshLibrary;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;
import ch.fhnw.ether.scene.mesh.material.CustomMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.Texture;
import ch.fhnw.ether.ui.Button;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.IView.ViewType;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;
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
	private IMesh car;
	float startX = -(strasse[0].length / 2.0f);
	float startY = (strasse.length / 2.0f);
	float positionX = startX;
	float positionY = startY;
	float directionX = 0;
	float directionY = 0;
	private static Field[][] fields;
	
	
	public static final int[][] strasse = { 
			{ STREET_SOUTH_EAST, 	STREET_EAST_WEST, 	STREET_EAST_WEST, 	STREET_EAST_WEST, 	STREET_SOUTH_WEST }, 
			{ STREET_NORTH_SOUTH, 	GRAS, 				GRAS,	 			GRAS, 				STREET_NORTH_SOUTH }, 
			{ CROSSING,  			STREET_EAST_WEST, 	STREET_EAST_WEST, 	STREET_EAST_WEST, 	CROSSING },
			{ STREET_NORTH_SOUTH, 	GRAS, 				GRAS,	 			GRAS, 				STREET_NORTH_SOUTH },
			{ STREET_NORTH_EAST, 	STREET_EAST_WEST,	STREET_EAST_WEST,	STREET_EAST_WEST,	STREET_NORTH_WEST} };

		
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

			//////////////////////
			/////// CITY ////////
			//////////////////////
			fields = new Field[strasse.length][strasse[0].length];
			for (int i = 0; i < strasse.length; i++) {
				for (int j = 0; j < strasse[i].length; j++) {
					Field field = createField(strasse[i][j]);
					field.setGridPositionX(i);
					field.setGridPositionY(j);
					field.setName("Feld " + i + " " + j);
					field.setTransform(Mat4.translate(startX+j, startY-i, 0f));
					field.savePosition();
					fields[i][j] = field;
					scene.add3DObject(field);
				}
			}
			
			
			//////////////////////
			/////// CAR //////////
			//////////////////////
			car = MeshLibrary.createCube();
			scene.add3DObject(car);
			controller.getUI().addWidget(new Button(0, 1, "left", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				directionX = -0.01f;
				directionY =0;
			}));
			controller.getUI().addWidget(new Button(2, 1, "right", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				directionX = 0.01f;
				directionY =0;
			}));
			controller.getUI().addWidget(new Button(1, 2, "up", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				directionX = 0;
				directionY = 0.01f;
			}));
			controller.getUI().addWidget(new Button(1, 0, "down", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				directionX = 0;
				directionY = -0.01f;
			}));
			controller.getUI().addWidget(new Button(1, 1, "stop", "", KeyEvent.VK_ESCAPE, (button, v) -> {
				directionX = 0;
				directionY = 0;
			}));
		});
		
			
		controller.animate(new IEventScheduler.IAnimationAction() {		
			@Override
			public void run(double time, double interval) {

				// Limit Position X
				if(positionX >= fields[0][0].getStartPositionX()){
					positionX += directionX;
				}else{
					positionX = fields[0][0].getStartPositionX();
				}
				
				if(positionX <= fields[0][4].getStopPositionX()){
					positionX += directionX;
				}else{
					positionX = fields[0][4].getStopPositionX();
				}
				
				
				
				if(positionY <= fields[0][0].getStopPositionY()){
					positionY += directionY;
				}else{
					positionY = fields[0][0].getStopPositionY();
				}
				
				if(positionY >= fields[4][0].getStartPositionY()){
					positionY += directionY;
				}else{
					positionY = fields[4][0].getStartPositionY();
				}			
				

				
	            //Mat4 transform = Mat4.multiply(Mat4.scale(0.5f), Mat4.translate(positionX, positionY, 0.5f));         
				Mat4 transform = Mat4.multiply(Mat4.translate(positionX, positionY, 0f));         
	            car.setTransform(transform);
				getLocalisation(positionX, positionY);

			}
		});
	}
		
	
	private static void getLocalisation(float positionX, float positionY){
		for (int i = 0; i < fields.length; i++) {			
			for (int j = 0; j < fields[i].length; j++) {
				if(positionX >= fields[i][j].getStartPositionX() && positionX <= fields[i][j].getStopPositionX()){
					if(positionY >= fields[i][j].getStartPositionY() && positionY <= fields[i][j].getStopPositionY()){
						LOGGER.info("Localisation ist: " + fields[i][j].getName());
					}
				}
			}
		}		
	}
	
	
	private static Field createField(int type){
		switch (type) {
		case GRAS:
			return createField("/assets/grass.jpg");
		case STREET_EAST_WEST:
			return createField("/assets/roadEW.jpg");
		case STREET_NORTH_SOUTH:
			return createField("/assets/roadNS.jpg");
		case CROSSING:
			return createField("/assets/roadNEWS.jpg");
		case STREET_NORTH_EAST:
			return createField("/assets/roadNE.jpg");
		case STREET_NORTH_WEST:
			return createField("/assets/roadNW.jpg");
		case STREET_SOUTH_EAST:
			return createField("/assets/roadSE.jpg");
		case STREET_SOUTH_WEST:
			return createField("/assets/roadSW.jpg");
		}
		return null;
	}
	
			
	private static Field createField(String asset) {
		IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(StreetExample.class.getResource(asset)), true);
		IGeometry g = Util.getDefaultGeometry();
		return new Field(m, g);
	}
		
}
