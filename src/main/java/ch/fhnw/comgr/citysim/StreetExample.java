
package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.util.HouseLoader;
import ch.fhnw.comgr.citysim.util.PickFieldTool;
import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.controller.tool.PickTool;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.light.DirectionalLight;
import ch.fhnw.ether.scene.light.ILight;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.IMesh.Flag;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.Texture;
import ch.fhnw.ether.ui.Button;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.IView.ViewType;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;
import ch.fhnw.util.math.geometry.GeodesicSphere;

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
	
	private List<IMesh> car;
	
	float startX = -(strasse[0].length / 2.0f);
	float startY = (strasse.length / 2.0f);

	private Vec3 carPosition = new Vec3(0f, 0f, 0f);
	Vec3 newCarPosition= carPosition;
	
	
	public static final int[][] strasse = { 
	{ STREET_SOUTH_WEST, 	GRAS,		STREET_SOUTH_EAST,	STREET_EAST_WEST, 	STREET_SOUTH_WEST, GRAS , GRAS}, 
	{ STREET_NORTH_SOUTH,	GRAS,		STREET_NORTH_SOUTH, 	GRAS, 			STREET_NORTH_SOUTH, GRAS, GRAS}, 
	{ STREET_NORTH_SOUTH,	GRAS,		STREET_NORTH_SOUTH, 	GRAS, 			STREET_NORTH_SOUTH, GRAS, GRAS}, 
	{ CROSSING, 			STREET_EAST_WEST,	CROSSING,  		STREET_EAST_WEST, 	CROSSING, STREET_EAST_WEST, STREET_SOUTH_WEST},
	{ STREET_NORTH_SOUTH,	GRAS,		STREET_NORTH_SOUTH, 	GRAS, 		 		STREET_NORTH_SOUTH, GRAS, STREET_NORTH_SOUTH},
	{ STREET_NORTH_EAST,	STREET_EAST_WEST,		STREET_NORTH_WEST, 	GRAS, 				CROSSING, STREET_EAST_WEST, STREET_NORTH_WEST}, 
	{ STREET_SOUTH_EAST,	STREET_EAST_WEST,		STREET_EAST_WEST, 	STREET_EAST_WEST, 	CROSSING, STREET_EAST_WEST, STREET_SOUTH_WEST},
	{ STREET_NORTH_EAST,	STREET_EAST_WEST,		STREET_EAST_WEST, 	STREET_EAST_WEST, 	CROSSING, STREET_EAST_WEST, STREET_NORTH_WEST},
	{ STREET_SOUTH_WEST, 	GRAS,		STREET_SOUTH_EAST,	STREET_EAST_WEST, 	CROSSING, GRAS , GRAS}, 
	{ STREET_NORTH_SOUTH,	GRAS,		STREET_NORTH_SOUTH, 	GRAS, 				STREET_NORTH_SOUTH, GRAS, GRAS}, 
	{ STREET_NORTH_SOUTH,	GRAS,		STREET_NORTH_SOUTH, 	GRAS, 				STREET_NORTH_SOUTH, GRAS, GRAS}, 
	{ CROSSING, 			STREET_EAST_WEST,	CROSSING,  			STREET_EAST_WEST, 	CROSSING, STREET_EAST_WEST, STREET_SOUTH_WEST},
	{ STREET_NORTH_SOUTH,	GRAS,		STREET_NORTH_SOUTH, 	GRAS, 		 		STREET_NORTH_SOUTH, GRAS, STREET_NORTH_SOUTH},
	{ STREET_NORTH_EAST,	STREET_EAST_WEST,		STREET_NORTH_WEST, 	GRAS, 				CROSSING, STREET_EAST_WEST, STREET_NORTH_WEST}, 
{ STREET_SOUTH_EAST,	STREET_EAST_WEST,		STREET_EAST_WEST, 	STREET_EAST_WEST, 	CROSSING, STREET_EAST_WEST, STREET_SOUTH_WEST},
{ STREET_NORTH_EAST,	STREET_EAST_WEST,		STREET_EAST_WEST, 	STREET_EAST_WEST, 	CROSSING, STREET_EAST_WEST, STREET_NORTH_WEST}
	};

		
	public static void main(String[] args) {
		new StreetExample();
	}

	
	// Setup the whole thing
	public StreetExample() {
		// Create controller
		
		CityController controller = new CityController(strasse);
		
		controller.run(time -> {
			// Create view
			// Neues Config machen
			IView view = new DefaultView(controller, 100, 100, 500, 500, new IView.Config(ViewType.INTERACTIVE_VIEW, 0, new IView.ViewFlag[0]), "City Sim");		
			ICamera camera = new Camera(new Vec3(0, 5, 5), Vec3.ZERO);

			// Create scene and add triangle
			IScene scene = new DefaultScene(controller);			
			controller.setScene(scene);
			
			scene.add3DObject(camera);
			controller.setCamera(view, camera);
			
			PickFieldTool pickFieldTool = new PickFieldTool(controller);
			controller.setCurrentTool(pickFieldTool);
			

			/////// CITY ////////

			for (int i = 0; i < controller.getFields().length; i++) {
				for (int j = 0; j < controller.getFields()[0].length; j++) {
					scene.add3DObject(controller.getFields()[i][j]);	
				}
			}			
			/////// CAR //////////

			Taxi taxi = new Taxi(TaxiType.YELLOW_CAB, controller);
	
			taxi.setTransform(Mat4.translate(1, 0, 0));
			
			controller.addTaxi(taxi);
			scene.add3DObjects(taxi.getMesh());
			
			
			
			controller.startAnimationTaxis(); 

			Field[][] fields = controller.getFields();
			
			for (int i = 0; i < fields.length; i++) {
				for (int j = 0; j < fields[i].length; j++) {
					if (strasse[i][j] == GRAS) {
						Vec3 pos = fields[i][j].getPosition();
						List<IMesh> house = HouseLoader.getHouse("bilding");
						Mat4 trans = Mat4.multiply(Mat4.rotate(90, 1,0,0),Mat4.translate(new Vec3(0f, 0.01,0f)),Mat4.scale(0.0048f));
						house.forEach(h -> h.setTransform(trans.preMultiply(Mat4.translate(pos.x + 0.7f, pos.y + 0.5f, 0))));
						scene.add3DObjects(house);
					}
				}
			}
			
			scene.add3DObject(new DirectionalLight(Vec3.Z, RGB.WHITE, RGB.WHITE));
			
		});
		
			
	}
		
}
