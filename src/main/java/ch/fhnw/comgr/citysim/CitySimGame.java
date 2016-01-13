
package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.action.DriveAnimation;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.model.Taxi;
import ch.fhnw.comgr.citysim.model.map.CitySimMap;
import ch.fhnw.comgr.citysim.tool.TaxiMoverTool;
import ch.fhnw.comgr.citysim.ui.InteractionPanel;
import ch.fhnw.comgr.citysim.ui.MessagePanel;
import ch.fhnw.comgr.citysim.util.AssetsLoader;
import ch.fhnw.comgr.citysim.model.TaxiType;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.FrameCameraControl;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.light.DirectionalLight;
import ch.fhnw.ether.scene.light.ILight;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.IView.ViewType;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

import java.util.List;

public final class CitySimGame {

	public static void main(String[] args) {
		new CitySimGame();
	}

	// Setup the whole thing
	public CitySimGame() {
		// Create controller
		
		CityController controller = new CityController(CitySimMap.MAP);

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

			TaxiMoverTool taxiMoverTool = new TaxiMoverTool(controller);
			controller.setCurrentTool(taxiMoverTool);

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
			taxiMoverTool.setCurrentTaxi(taxi, drive);

			Field[][] fields = PathAlgorithm.getFields();

			//Field target = fields[14][22];
			//drive.setTarget(target);

			for (int i = 0; i < fields.length; i++) {
				for (int j = 0; j < fields[i].length; j++) {
					if (CitySimMap.MAP[i][j] == CitySimMap.H0S) {
						Vec3 pos = fields[i][j].getPosition();
						List<IMesh> house = AssetsLoader.getObject("houses/Bambo_House");
						Mat4 trans = Mat4.multiply(Mat4.rotate(90, 1,0,0),Mat4.translate(new Vec3(-0.5f, 0.01, 0.33f)),Mat4.scale(0.06f)); // 0.048
						house.forEach(h -> h.setTransform(trans.preMultiply(Mat4.translate(pos.x + 0.7f, pos.y + 0.5f, 0))));
						scene.add3DObjects(house);
					}
					if (CitySimMap.MAP[i][j] == CitySimMap.H1S) {
						Vec3 pos = fields[i][j].getPosition();
						List<IMesh> house = AssetsLoader.getObject("houses/hOUSE");
						// y, höhe, x
						Mat4 trans = Mat4.multiply(Mat4.rotate(90, 1,0,0),Mat4.rotate(90, 0,1,0) ,Mat4.translate(new Vec3(0.0078f, 0.537, 0.4f)),Mat4.scale(0.0015f));
						house.forEach(h -> h.setTransform(trans.preMultiply(Mat4.translate(pos.x + 0.7f, pos.y + 0.5f, 0))));
						scene.add3DObjects(house);
					}
				}
			}


			//InteractionPanel
			InteractionPanel interactionPanel = new InteractionPanel();
			MessagePanel messagePanel = new MessagePanel();
			controller.getRenderManager().addMesh(interactionPanel.getMesh());
			controller.getRenderManager().addMesh(messagePanel.getMesh());

			
			CityController.setMessagePanel(messagePanel);

			String[] message = new String[2];
			message[0] = "Hallo, mein Name ist John. Ich bin der Taxifahrer von CitySim.";
			message[1] = "Klicke auf eine Kreuzung um mir einen neuen Fahrziel zu setzen.";
			messagePanel.sendMessage(message);


			ILight light = new DirectionalLight(new Vec3(5,5,5), RGB.WHITE, RGB.WHITE);
			ILight light2 = new DirectionalLight(new Vec3(-10,-10,5), RGB.WHITE, RGB.WHITE);
			scene.add3DObject(light);
			scene.add3DObject(light2);

		});
	}
//
//	public static List<IMesh> getTrafficLight() {
//
//		final URL obj = StreetExample.class.getClassLoader().getResource("assets/trafficLight/trafficLight1.obj");
//
//		final List<IMesh> meshes = new ArrayList<>();
//		try {
//			new ObjReader(obj).getMeshes().forEach(mesh -> meshes.add(mesh));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("number of meshes before merging: " + meshes.size());
//		final List<IMesh> merged = MeshUtilities.mergeMeshes(meshes);
//		System.out.println("number of meshes after merging: " + merged.size());
//
//
//		return merged;
//	}


}



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

//			InteractionObject interactionObject = new InteractionObject(MeshUtilities.createCube(InteractionObject.greenBlock));

//				CitySimMap map = CitySimMap.getInstance();
//
//				InteractionObject interactionObject2 = new InteractionObject(TrafficLightLoader.getStatic(getClass()),
//								TrafficLightLoader.getEnabled(getClass()), TrafficLightLoader.getDisabled(getClass()));
//
//				map.addObjectToLayer(interactionObject2);
//
//				for (InteractionObject intObj : map.getInteractionObjects()) {
//					scene.add3DObjects(intObj.getMesh());
//				}
//
//				// Traffic Light
//				List<IMesh> trafficLight = getTrafficLight();
//
//				scene.add3DObjects(trafficLight);
