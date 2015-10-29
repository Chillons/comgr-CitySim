package ch.fhnw.comgr.citysim;

import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshLibrary;
import ch.fhnw.ether.ui.Button;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.math.Vec3;

import java.awt.event.KeyEvent;

public class CitySim {

    private IScene scene;

    public static void main(String[] args) {
        new CitySim();
    }

    public CitySim() {
        // Create controller
        IController controller = new DefaultController();

        controller.run((time) -> {
            // Create view
            IView view = new DefaultView(controller, 100, 100, 500, 500, IView.INTERACTIVE_VIEW, "Simple Cube");

            // Create scene
            scene = new DefaultScene(controller);
            controller.setScene(scene);

            IMesh ground = MeshLibrary.createGroundPlane();

            scene.add3DObject(ground);

            // Create and add camera
            ICamera camera = new Camera(new Vec3(0, -5, 5), Vec3.ZERO);
            scene.add3DObject(camera);
            controller.setCamera(view, camera);

            // Add cube
            scene.add3DObject(MeshLibrary.createCube());

            // Add an exit button
            controller.getUI().addWidget(new Button(0, 0, "Quit", "Quit", KeyEvent.VK_ESCAPE, (button, v) -> System.exit(0)));
        });
    }

}
