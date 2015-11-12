package ch.fhnw.comgr.citysim.util;

import ch.fhnw.ether.formats.obj.ObjReader;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TaxiLoader {

    private TaxiLoader() {

    }

    public static List<IMesh> getTaxi(TaxiType type) {
        final URL obj = TaxiLoader.class.getClassLoader().getResource("assets/taxi/" + type.toString() + ".obj");

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
