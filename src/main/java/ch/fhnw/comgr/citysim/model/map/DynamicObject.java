package ch.fhnw.comgr.citysim.model.map;

import ch.fhnw.ether.scene.mesh.MeshUtilities;

public class DynamicObject extends LayerObject {

    private DynamicObject() {
        // FIXME dummy implementation
        super(MeshUtilities.createCube());
    }



}
