package ch.fhnw.comgr.citysim.model.map;

import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;

import java.util.ArrayList;
import java.util.List;

public abstract class LayerObject {

  private List<IMesh> mesh;

  public List<IMesh> getMesh() {
    return mesh;
  }

  public void setMesh(List<IMesh> mesh) {
    // TODO: copy instead of just assign?
    this.mesh = mesh;
  }

  public void setMesh(IMesh mesh) {
    this.mesh = new ArrayList<>();
    this.mesh.add(mesh);
  }

  public void setTransform(Mat4 transform) {
    for (IMesh m : mesh) {
      m.setTransform(transform);
    }
  }
}
