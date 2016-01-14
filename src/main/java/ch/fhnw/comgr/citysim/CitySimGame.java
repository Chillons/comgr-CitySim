
package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.action.DriveAnimation;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.model.Taxi;
import ch.fhnw.comgr.citysim.model.map.CitySimMap;
import ch.fhnw.comgr.citysim.model.map.layer.InteractionObject;
import ch.fhnw.comgr.citysim.tool.GameTool;
import ch.fhnw.comgr.citysim.tool.TaxiMoverTool;
import ch.fhnw.comgr.citysim.ui.InstructionField;
import ch.fhnw.comgr.citysim.ui.InstructionPanel;
import ch.fhnw.comgr.citysim.ui.ScorePanel;
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
import java.util.concurrent.atomic.AtomicInteger;

public final class CitySimGame {

	public static final AtomicInteger counterConstructions = new AtomicInteger(0);
	public static TaxiMoverTool taxiMoverTool;
	public static GameTool gameTool;
	public static ScorePanel scorePanel;
	public static int time;
	public static IScene scene;



	public static void main(String[] args) {
		new CitySimGame();
	}

	// Setup the whole thing
	public CitySimGame() {
		// Create controller
		
		CityController controller = new CityController(CitySimMap.MAP);

		taxiMoverTool = new TaxiMoverTool(controller);
		gameTool = new GameTool(controller);

		controller.run(time -> {
			// Create view
			// Neues Config machen
			IView view = new DefaultView(controller, 100, 100, 800, 600, new IView.Config(ViewType.INTERACTIVE_VIEW, 0, new IView.ViewFlag[0]), "City Sim");
			ICamera camera = new Camera(new Vec3(0, 5, 5), Vec3.ZERO);
			FrameCameraControl fcc = new FrameCameraControl(camera, CityController.getMap().getDijkstraPath().getNodes());
			fcc.frame();

			// Create scene and add triangle
			scene = new DefaultScene(controller);
			controller.setScene(scene);

			scene.add3DObject(camera);
			controller.setCamera(view, camera);
			
			controller.setCurrentTool(taxiMoverTool);				

			/////// CITY ////////

			for (int i = 0; i < CityController.getFields().length; i++) {
				for (int j = 0; j < CityController.getFields()[0].length; j++) {
					scene.add3DObject(CityController.getFields()[i][j]);
				}
			}

			CitySimMap map = CitySimMap.getInstance();

			/////// Taxi //////////


			Taxi taxi = new Taxi(TaxiType.YELLOW_CAB, map.getDijkstraPath());
			taxi.setTransform(Mat4.translate(1, 0, 0));

			DriveAnimation drive = new DriveAnimation(taxi, controller);
			controller.animate(drive);
			scene.add3DObjects(taxi.getMesh());
			taxiMoverTool.setCurrentTaxi(taxi, drive);

			Field[][] fields = CityController.getFields();

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


			//GUI-Panels
			InstructionPanel instructionPanel = new InstructionPanel();
			InstructionField instructionField = new InstructionField();
			scorePanel = new ScorePanel();
			controller.getRenderManager().addMesh(instructionPanel.getMesh());
			controller.getRenderManager().addMesh(instructionField.getMesh());
			controller.getRenderManager().addMesh(scorePanel.getMesh());

			CityController.setInstructionField(instructionField);

			String[] instruction = new String[2];
			instruction[0] = "Hallo, mein Name ist John. Ich bin der Taxifahrer von CitySim.";
			instruction[1] = "Klicke auf eine Kreuzung um mir ein neues Fahrziel zu geben.";
			instructionField.sendInstruction(instruction);

			time = 0;
			String[] score = new String[2];
			score[0] = "Deine Punktzahl:";
			score[1] = "0";
			scorePanel.sendScore(score);


			ILight light = new DirectionalLight(new Vec3(5,5,5), RGB.WHITE, RGB.WHITE);
			ILight light2 = new DirectionalLight(new Vec3(-10,-10,5), RGB.WHITE, RGB.WHITE);
			scene.add3DObject(light);
			scene.add3DObject(light2);




			map.createRandomTrafficLights(4);


		});
	}
}
