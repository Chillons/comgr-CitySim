
package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.model.map.InteractionObject;
import ch.fhnw.comgr.citysim.model.map.CitySimMap;
import ch.fhnw.comgr.citysim.model.taxi.Taxi;
import ch.fhnw.comgr.citysim.util.PickFieldTool;
import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.IView.ViewType;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.math.Vec3;

import javax.imageio.spi.IIORegistry;
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
	
	float startX = -(strasse[0].length / 2.0f);
	float startY = (strasse.length / 2.0f);

	private Vec3 carPosition = new Vec3(0f, 0f, 0f);
	Vec3 newCarPosition= carPosition;
	
	
	public static final int[][] strasse = { 
			{ GRAS, 							GRAS, 						STREET_SOUTH_EAST,		STREET_EAST_WEST,		STREET_SOUTH_WEST, 	GRAS },
			{ STREET_SOUTH_WEST, 	GRAS, 						STREET_NORTH_SOUTH,		GRAS,								STREET_NORTH_EAST, 	STREET_SOUTH_WEST},
			{ STREET_NORTH_SOUTH, GRAS, 						STREET_NORTH_SOUTH , 	GRAS, 							GRAS,								STREET_NORTH_SOUTH},
			{ CROSSING,  					STREET_EAST_WEST, CROSSING, 				 	 	STREET_EAST_WEST,		STREET_EAST_WEST,		CROSSING },
			{ STREET_NORTH_SOUTH, GRAS, 		 				STREET_NORTH_SOUTH,  	GRAS,						 		GRAS,								STREET_NORTH_SOUTH  },
			{ STREET_NORTH_WEST, 	GRAS,							STREET_NORTH_EAST,   	STREET_EAST_WEST, 	CROSSING,						STREET_NORTH_WEST},
			{ GRAS,								GRAS, 						GRAS,									GRAS,								STREET_NORTH_SOUTH, GRAS},
			{ STREET_EAST_WEST,		STREET_EAST_WEST,	STREET_EAST_WEST,			STREET_EAST_WEST,		STREET_NORTH_WEST,	GRAS}
	};

		
	public static void main(String[] args) {

			IIORegistry registry = IIORegistry.getDefaultInstance();
			registry.registerServiceProvider(new com.realityinteractive.imageio.tga.TGAImageReaderSpi());
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
			ICamera camera = new Camera(new Vec3(0, -5, 5), Vec3.ZERO);

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

//			Taxi taxi = new Taxi(TaxiType.YELLOW_CAB);
//
//			car = taxi.getMesh();
//			for (IMesh mesh : car) {
//				mesh.setTransform(taxi.getTransform());
//			}
//
//			controller.addTaxi(car);
//			scene.add3DObjects(car);


			/////// Traffic Light dummy ///////

			InteractionObject interactionObject = new InteractionObject();
			interactionObject.setMesh(MeshUtilities.createCube());

			CitySimMap map = CitySimMap.getInstance();
			map.addObjectToLayer(interactionObject);

			for (InteractionObject intObj : map.getInteractionObjects()) {
				scene.add3DObjects(intObj.getMesh());
			}

		});
		
			
	}
		
}
