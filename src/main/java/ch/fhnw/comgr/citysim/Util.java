package ch.fhnw.comgr.citysim;

import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;

public class Util {

	public static float[] vertices = { 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1,
			1, 0, 0, 1, 0 };

	public static float[] colors = new float[] { .5f, .5f, .5f, 1, .5f, .5f,
			.5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f,
			.5f, .5f, 1 };

	public static float[] texCoords = { 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1 };

	public static IGeometry getDefaultGeometry() {
		return DefaultGeometry.createVCM(Primitive.TRIANGLES, vertices, colors, texCoords);
	}
}
