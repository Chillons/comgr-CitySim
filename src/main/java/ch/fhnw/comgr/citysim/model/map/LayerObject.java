package ch.fhnw.comgr.citysim.model.map;

import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;

import java.util.ArrayList;
import java.util.List;

public abstract class LayerObject {

  private List<IMesh> model;

  private Mat4 transform;

  public List<IMesh> getModel() {
    return model;
  }

  public void setModel(List<IMesh> model) {
    // TODO: copy instead of just assign?
    this.model = model;
  }
}
