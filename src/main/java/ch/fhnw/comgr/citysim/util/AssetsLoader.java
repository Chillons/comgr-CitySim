package ch.fhnw.comgr.citysim.util;

import ch.fhnw.ether.formats.obj.ObjReader;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AssetsLoader {

    private AssetsLoader() {

    }

    public static List<IMesh> getObject(final String name) {
        final URL obj = AssetsLoader.class.getClassLoader().getResource("assets/" + name + ".obj");

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

    /**
     * Gets all three meshes
     *
     * @param name the base name.
     * @return an array with base model, deactivated state, activated state
     */
    public static List[] getDynamicObject(final String name) {

        List[] ans = new List[3];

        ans[0] = getObject(name);
        ans[1] = getObject(name + "Inactive");
        ans[2] = getObject(name + "Active");


        return ans;
    }

}
