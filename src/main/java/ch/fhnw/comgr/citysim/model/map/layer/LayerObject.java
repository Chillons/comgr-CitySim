package ch.fhnw.comgr.citysim.model.map.layer;

import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;

import java.util.ArrayList;
import java.util.List;

public abstract class LayerObject {

  private final List<IMesh> mesh;
  private final List<Mat4> baseTransformations;
  private int fieldX;
  private int fieldY;

  public LayerObject(IMesh mesh) {
    this.mesh = new ArrayList<>();
    this.mesh.add(mesh);

    baseTransformations = new ArrayList<>();
    baseTransformations.add(mesh.getTransform());

  }

  public LayerObject(List<IMesh> mesh) {
    this.mesh = new ArrayList<>();
    this.mesh.addAll(mesh);
    baseTransformations = new ArrayList<>();
    for (int i = 0; i < mesh.size(); i++) {
      baseTransformations.add(i, mesh.get(i).getTransform());
      System.out.println("added base transformation");
    }
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

  public void setPosition(int fieldX, int fieldY) {

    this.fieldX = fieldX;
    this.fieldY = fieldY;
  }
}
