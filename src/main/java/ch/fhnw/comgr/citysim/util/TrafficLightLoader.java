package ch.fhnw.comgr.citysim.util;

import ch.fhnw.ether.formats.obj.ObjReader;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TrafficLightLoader {

  public static List<IMesh> getStatic(Class cl) {
    final URL obj = cl.getClassLoader().getResource("assets/ampel/ampelStatic.obj");

    return getMeshes(obj);
  }

  public static List<IMesh> getEnabled(Class cl) {
    final URL obj = cl.getClassLoader().getResource("assets/ampel/ampelRed.obj");

    return getMeshes(obj);
  }

  public static List<IMesh> getDisabled(Class cl) {
    final URL obj = cl.getClassLoader().getResource("assets/ampel/ampelGreen.obj");

    return getMeshes(obj);
  }

  private static List<IMesh> getMeshes(URL obj) {
    final List<IMesh> meshes = new ArrayList<>();
    try {
      new ObjReader(obj).getMeshes().forEach(meshes::add);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("number of meshes before merging: " + meshes.size());
    final List<IMesh> merged = MeshUtilities.mergeMeshes(meshes);
    System.out.println("number of meshes after merging: " + merged.size());
    return merged;
  }
}
