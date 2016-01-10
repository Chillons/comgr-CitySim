
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
	{CRO,	E_W, 	E_W,	E_W,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	E_W,	E_W, 	E_W, 	E_W ,	CRO, 	E_W, 	E_W , 	E_W,	CRO,	GRA,	GRA, 	GRA, 	GRA ,	CRO}, 
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
			
			Field[][]felder = controller.getFields();
			
			Field target = felder[14][22];
			
			controller.getTaxis().get(0).setTarget(target);
			
			Field[][] fields = controller.getFields();
			
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
			
			ILight light = new DirectionalLight(new Vec3(5,5,5), RGB.WHITE, RGB.WHITE);
			ILight light2 = new DirectionalLight(new Vec3(-10,-10,5), RGB.WHITE, RGB.WHITE);
			scene.add3DObject(light);
			scene.add3DObject(light2);
			
		});
		
			
	}
		
}
