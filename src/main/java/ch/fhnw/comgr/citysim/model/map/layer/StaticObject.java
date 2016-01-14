package ch.fhnw.comgr.citysim.model.map.layer;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;

import java.util.List;

public class StaticObject extends LayerObject {
    public StaticObject(IMesh mesh) {
        super(mesh);
    }

    public StaticObject(List<IMesh> mesh, int x, int y) {
        super(mesh);
        final Field field = CityController.getFields()[x][y];
        setTransform(Mat4.translate(field.getPosition().x, field.getPosition().y, 0));
    }
}
