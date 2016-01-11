package ch.fhnw.comgr.citysim.model.taxi;

import ch.fhnw.ether.formats.obj.ObjReader;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;

import javax.imageio.spi.IIORegistry;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TaxiLoader {

  private TaxiLoader() {

  }

  public static List<IMesh> getCab() {
    IIORegistry registry = IIORegistry.getDefaultInstance();
    registry.registerServiceProvider(new com.realityinteractive.imageio.tga.TGAImageReaderSpi());

//    final URL obj = TaxiLoader.class.getClassLoader().getResource("assets/taxi/yellowCab/premier.obj");
    final URL obj = TaxiLoader.class.getClassLoader().getResource("assets/taxi/london/London_Cab.obj");

    final List<IMesh> meshes = new ArrayList<>();
    try {
      new ObjReader(obj).getMeshes().forEach(mesh -> meshes.add(mesh));
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("number of meshes before merging: " + meshes.size());
    final List<IMesh> merged = MeshUtilities.mergeMeshes(meshes);
    System.out.println("number of meshes after merging: " + merged.size());


    return merged;
  }

}
