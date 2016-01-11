
package ch.fhnw.comgr.citysim;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;

import ch.fhnw.comgr.citysim.util.HouseLoader;
import ch.fhnw.comgr.citysim.util.PickFieldTool;
import ch.fhnw.comgr.citysim.util.TaxiType;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.FrameCameraControl;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.light.DirectionalLight;
import ch.fhnw.ether.scene.light.ILight;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.ui.GraphicsPlane;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.IView.ViewType;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public final class StreetExample {
	
	public static final int GRA = 0;
	public static final int E_W = 1;
	public static final int N_S = 2;
	public static final int CRO = 3;
	public static final int N_E = 4;
	public static final int N_W = 5;
	public static final int S_E = 6;
	public static final int S_W = 7;
	// Ausrichtung nach unten. 
	public static final int H0S = 10;
	// Ausrichtung nach oben. 
	public static final int H0N = 12;
	//Ausrichtung nach unten. Braucht rechts noch einmal gras
	public static final int H1S = 11;
	//Ausrichtung nach oben. Braucht links noch einmal gras
	public static final int H1N = 13;
		
	private List<IMesh> car;
	
	float startX = -(strasse[0].length / 2.0f);
	float startY = (strasse.length / 2.0f);

	private Vec3 carPosition = new Vec3(0f, 0f, 0f);
	Vec3 newCarPosition= carPosition;
	
	
	public static final int[][] strasse = { 
																										  // 13
	{ S_E,	E_W,	E_W,	E_W,	CRO, 	E_W, 	E_W ,	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO, 	E_W, 	E_W ,	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	S_W}, 
	{ N_S,	H1S,	GRA,	H0S,	N_S, 	H0S, 	GRA ,	H0S,	N_S,	GRA,	H0S, 	GRA, 	H0S ,	N_S, 	H0S, 	H1S, 	GRA,	N_S,	GRA,	H1S, 	GRA, 	GRA ,	N_S}, 
	{ CRO,	E_W, 	E_W,	E_W,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO}, 
	{ H0S,	GRA,	H0S,	GRA, 	N_S, 	GRA, 	H0S, 	H0S,	N_S,	GRA,	H0S, 	H0S, 	GRA ,	N_S, 	GRA, 	H0S, 	GRA,	N_S,	H1S,	GRA, 	H1S, 	GRA ,	N_S}, 
	{ CRO,	E_W, 	E_W,	E_W,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO},
	{ N_S,	GRA,	GRA,	GRA, 	N_S,	GRA, 	GRA, 	GRA,	N_S,	GRA,	H1S, 	GRA, 	GRA ,	N_S, 	GRA, 	GRA, 	GRA,	N_S,	GRA,	GRA, 	GRA, 	GRA ,	N_S},
	{ CRO,	E_W, 	E_W,	E_W,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO}, 
	{ N_S,	GRA,	GRA,	GRA, 	GRA, 	GRA, 	GRA, 	GRA,	N_S,	GRA,	GRA, 	GRA, 	GRA ,	N_S, 	GRA, 	GRA, 	GRA,	N_S,	GRA,	GRA, 	GRA, 	GRA ,	N_S},
	{ N_S,	H0S,	H0S,	GRA, 	GRA, 	GRA, 	GRA, 	GRA,	N_S,	GRA,	H1S, 	GRA, 	H0S ,	N_S, 	GRA, 	H1S, 	GRA,	N_S,	GRA,	H0S, 	GRA, 	GRA ,	N_S},
	{ CRO,	E_W, 	E_W,	E_W,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO}, 
	{ N_S,	GRA,	GRA,	GRA, 	N_S,	GRA, 	GRA, 	GRA,	GRA,	GRA,	GRA, 	GRA, 	GRA ,	N_S, 	GRA, 	GRA, 	GRA,	N_S,	GRA,	GRA, 	GRA, 	GRA ,	N_S}, 
	{ N_S,	GRA,	H1S,	GRA, 	N_S,	H1S, 	GRA, 	GRA,	H0S,	H0S,	H1S, 	GRA, 	H0S ,	N_S, 	H1S, 	GRA, 	GRA,	N_S,	GRA,	H0S, 	H1S, 	GRA ,	N_S}, 
	{ CRO,	E_W, 	E_W,	E_W,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO},
	{ N_S,	GRA,	GRA,	GRA, 	N_S,	GRA, 	GRA, 	GRA,	N_S,	GRA,	GRA, 	GRA, 	GRA ,	N_S, 	GRA, 	H0S, 	H0S,	N_S,	GRA,	GRA, 	GRA, 	GRA ,	N_S},
	{CRO,	E_W, 	E_W,	E_W,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO, 	E_W, 	E_W , 	E_W,	N_S,	GRA,	GRA, 	GRA, 	GRA ,	CRO}, 
	{ N_S,	GRA,	GRA,	GRA, 	N_S, 	GRA, 	GRA, 	GRA,	N_S,	GRA,	GRA, 	GRA, 	GRA ,	N_S, 	GRA, 	GRA, 	GRA,	N_S,	GRA,	GRA, 	GRA, 	GRA ,	N_S},
	{ N_S,	GRA,	GRA,	GRA, 	N_S, 	GRA, 	GRA, 	GRA,	N_S,	H0S,	H1S, 	GRA, 	GRA ,	N_S, 	H0S, 	H1S, 	GRA,	N_S,	H0S,	H1S, 	GRA, 	GRA ,	N_S}, 
	{ N_E,	E_W,	E_W,	E_W,	CRO, 	E_W, 	E_W ,	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO, 	E_W, 	E_W ,	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	N_W}  // 17
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
			IView view = new DefaultView(controller, 100, 100, 800, 600, new IView.Config(ViewType.INTERACTIVE_VIEW, 0, new IView.ViewFlag[0]), "City Sim");		
			ICamera camera = new Camera(new Vec3(0, 5, 5), Vec3.ZERO);
			FrameCameraControl fcc = new FrameCameraControl(camera, PathAlgorithm.getNodes());
			fcc.frame();
			


			// Create scene and add triangle
			IScene scene = new DefaultScene(controller);			
			controller.setScene(scene);
			
			scene.add3DObject(camera);
			controller.setCamera(view, camera);
			
			PickFieldTool pickFieldTool = new PickFieldTool(controller);
			controller.setCurrentTool(pickFieldTool);
			

			/////// CITY ////////

			for (int i = 0; i < PathAlgorithm.getFields().length; i++) {
				for (int j = 0; j < PathAlgorithm.getFields()[0].length; j++) {
					scene.add3DObject(PathAlgorithm.getFields()[i][j]);	
				}
			}	

			/////// Taxi //////////

			Taxi taxi = new Taxi(TaxiType.YELLOW_CAB);
			taxi.setTransform(Mat4.translate(1, 0, 0));
			
			DriveAnimation drive = new DriveAnimation(taxi);			
			controller.animate(drive);
			scene.add3DObjects(taxi.getMesh());
			pickFieldTool.setCurrentTaxi(taxi, drive);
			
			
			Field[][] fields = PathAlgorithm.getFields();
			
			//Field target = fields[14][22];			
			//drive.setTarget(target);
			

			
			for (int i = 0; i < fields.length; i++) {
				for (int j = 0; j < fields[i].length; j++) {
					if (strasse[i][j] == H0S) {
						Vec3 pos = fields[i][j].getPosition();
						List<IMesh> house = HouseLoader.getHouse("Bambo_House");
						Mat4 trans = Mat4.multiply(Mat4.rotate(90, 1,0,0),Mat4.translate(new Vec3(-0.5f, 0.01, 0.33f)),Mat4.scale(0.06f)); // 0.048
						house.forEach(h -> h.setTransform(trans.preMultiply(Mat4.translate(pos.x + 0.7f, pos.y + 0.5f, 0))));
						scene.add3DObjects(house);
					}
					if (strasse[i][j] == H1S) {
						Vec3 pos = fields[i][j].getPosition();
						List<IMesh> house = HouseLoader.getHouse("hOUSE");
						// y, höhe, x
						Mat4 trans = Mat4.multiply(Mat4.rotate(90, 1,0,0),Mat4.rotate(90, 0,1,0) ,Mat4.translate(new Vec3(0.0078f, 0.537, 0.4f)),Mat4.scale(0.0015f));
						house.forEach(h -> h.setTransform(trans.preMultiply(Mat4.translate(pos.x + 0.7f, pos.y + 0.5f, 0))));
						scene.add3DObjects(house);
					}
				}
			}
			
			
			//InteractionPanel
			InteractionPanel plane = new InteractionPanel(0, 0, 1800, 1600);
			controller.getRenderManager().addMesh(plane.getMesh());
			CityController.setInteractionPanel(plane);
			
			String[] message = new String[2];
			message[0] = "Hallo mein Name ist John. Ich bin der Taxifahrer von CitySim.";
			message[1] = "Klicke auf eine Kreuzung um mir einen neuen Fahrziel zu setzen.";
			plane.sendMessage(message);

		
			ILight light = new DirectionalLight(new Vec3(5,5,5), RGB.WHITE, RGB.WHITE);
			ILight light2 = new DirectionalLight(new Vec3(-10,-10,5), RGB.WHITE, RGB.WHITE);
			scene.add3DObject(light);
			scene.add3DObject(light2);
			
		});
		
			
	}
	
		
}
