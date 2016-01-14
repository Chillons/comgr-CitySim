package ch.fhnw.comgr.citysim.model.map.layer;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public abstract class LayerObject {

  protected final List<IMesh> mesh;
  protected final List<Mat4> baseTransformations;

  public LayerObject(IMesh mesh) {

    this.mesh = new ArrayList<>();
    this.mesh.add(mesh);

    baseTransformations = new ArrayList<>();
    baseTransformations.add(mesh.getTransform());
    CitySimGame.scene.add3DObjects(mesh);
  }

  public void setBaseTransformations(Mat4 transformation) {
    this.baseTransformations.clear();
    this.baseTransformations.add(transformation);
  }

  public LayerObject(List<IMesh> mesh) {
    this.mesh = new ArrayList<>();
    this.mesh.addAll(mesh);
    baseTransformations = new ArrayList<>();
    for (int i = 0; i < mesh.size(); i++) {
      baseTransformations.add(i, mesh.get(i).getTransform());
      System.out.println("added base transformation");
    }
    CitySimGame.scene.add3DObjects(mesh);
  }

  public List<IMesh> getMesh() {
    return mesh;
  }

  public void setTransform(Mat4 transform) {

    for (int i = 0; i < mesh.size(); i++) {
      IMesh m = mesh.get(i);
      if (transform != null) {
        m.setTransform(baseTransformations.get(i).postMultiply(transform));
      } else {
        m.setTransform(baseTransformations.get(i));
      }
    }
  }
}
