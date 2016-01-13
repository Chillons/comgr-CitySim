

package ch.fhnw.comgr.citysim.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.ether.image.Frame;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.IMesh.Queue;
import ch.fhnw.ether.scene.mesh.MeshUtilities;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.Texture;
import ch.fhnw.util.color.RGBA;

public class InstructionPanel {

	private DefaultMesh mesh;

	private final BufferedImage     image;
	private final Graphics2D        graphics;
	private ColorMapMaterial  material;

	private int x;
	private int y;
	private int w;
	private int h;
	
	public InstructionPanel() {		
		this.x = 0;
		this.y = 0;
		this.w = 1600;
		this.h = 393;
		
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		graphics = image.createGraphics();
					
		float[] vertices = { x, y, 0, x + w, y, 0, x + w, y + h, 0, x, y, 0, x + w, y + h, 0, x, y + h, 0 };
		IGeometry geometry = DefaultGeometry.createVM(Primitive.TRIANGLES, vertices, MeshUtilities.DEFAULT_QUAD_TEX_COORDS);
		
		material = new ColorMapMaterial(Texture.TRANSPARENT_1x1);
				
        try {
        	material = new ColorMapMaterial(RGBA.WHITE, Frame.create(CitySimGame.class.getResource("/assets/taxi_driver.png")).getTexture());
        } catch (IOException e) {
            e.printStackTrace();
        }
                
		mesh = new DefaultMesh(material, geometry, Queue.SCREEN_SPACE_OVERLAY);	
	}
	
	public IMesh getMesh() {
		return mesh;
	}
	
}
