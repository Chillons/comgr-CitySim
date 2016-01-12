

package ch.fhnw.comgr.citysim.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

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
import ch.fhnw.util.UpdateRequest;

public class InteractionPanel {
	public static final Font FONT = new Font("SansSerif", Font.BOLD, 24);

	private static final Color CLEAR_COLOR = new Color(0, 0, 0, 0);

	private final UpdateRequest updater = new UpdateRequest();

	private final DefaultMesh mesh;

	private final BufferedImage     image;
	private final Graphics2D        graphics;
	private final ColorMapMaterial  material;

	private int x;
	private int y;
	private int w;
	private int h;
	
	public InteractionPanel(int x, int y, int w, int h) {
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		graphics = image.createGraphics();
		graphics.setFont(FONT);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

		float[] vertices = { x, y, 0, x + w, y, 0, x + w, y + h, 0, x, y, 0, x + w, y + h, 0, x, y + h, 0 };
		IGeometry geometry = DefaultGeometry.createVM(Primitive.TRIANGLES, vertices, MeshUtilities.DEFAULT_QUAD_TEX_COORDS);
		material = new ColorMapMaterial(Texture.TRANSPARENT_1x1);

		mesh = new DefaultMesh(material, geometry, Queue.SCREEN_SPACE_OVERLAY);
	}

	public final Texture getTexture() {
		return material.getTexture();
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final int getWidth() {
		return w;
	}

	public final int getHeight() {
		return h;
	}

	public final void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		updateRequest();
	}

	public void clear() {
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		fillRect(CLEAR_COLOR, x, y, w, h);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		updateRequest();
	}

	public void fillRect(Color color, int x, int y, int w, int h) {
		graphics.setColor(color);
		graphics.fillRect(x, y, w, h);
		updateRequest();
	}

	
	public void sendMessage(String[] message){
		clear();
		fillRect(Color.darkGray, 0, 1480, 1800, 100);
		drawStrings(message, 30, 1530);
		update();
	}
	public void drawString(String string, int x, int y) {
		drawString(Color.WHITE, string, x, y);
		updateRequest();
	}

	public void drawString(Color color, String string, int x, int y) {
		graphics.setColor(color);
		graphics.drawString(string, x, y);
		updateRequest();
	}

	public void drawStrings(String[] strings, int x, int y) {
		drawStrings(Color.WHITE, strings, x, y);
	}

	public void drawStrings(Color color, String[] strings, int x, int y) {
		graphics.setColor(color);
		int dy = 0;
		for (String s : strings) {
			graphics.drawString(s, x, y + dy);
			dy += FONT.getSize();
		}
		updateRequest();
	}

	public IMesh getMesh() {
		return mesh;
	}
	
	public void update() {
		if (updater.testAndClear())
			material.setColorMap(Frame.create(image).getTexture());
	}

	private void updateRequest() {
		updater.request();
	}
}
