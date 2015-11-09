package ch.fhnw.comgr.citysim;

import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.util.math.geometry.GeometryUtilities;

public class Util {

	public static float[] vertices = { 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1,
			1, 0, 0, 1, 0 };

	public static float[] colors = new float[] { .5f, .5f, .5f, 1, .5f, .5f,
			.5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f,
			.5f, .5f, 1 };

	public static float[] texCoords = { 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1 };

	public static IGeometry getDefaultField() {
		return DefaultGeometry.createVCM(Primitive.TRIANGLES, vertices, colors, texCoords);
	}
	
	
	
	
	
	//@formatter:off
	public static final float[] UNIT_CUBE_TRIANGLES = {
		// bottom
		-0.5f, -0.5f, -0.5f, -0.5f, +0.5f, -0.5f, +0.5f, +0.5f, -0.5f,
		-0.5f, -0.5f, -0.5f, +0.5f, +0.5f, -0.5f, +0.5f, -0.5f, -0.5f,

		// top
		+0.5f, -0.5f, +0.5f, +0.5f, +0.5f, +0.5f, -0.5f, +0.5f, +0.5f, 
		+0.5f, -0.5f, +0.5f, -0.5f, +0.5f, +0.5f, -0.5f, -0.5f, +0.5f,

		// front
		-0.5f, -0.5f, -0.5f, +0.5f, -0.5f, -0.5f, +0.5f, -0.5f, +0.5f, 
		-0.5f, -0.5f, -0.5f, +0.5f, -0.5f, +0.5f, -0.5f, -0.5f, +0.5f,

		// back
		+0.5f, +0.5f, -0.5f, -0.5f, +0.5f, -0.5f, -0.5f, +0.5f, +0.5f, 
		+0.5f, +0.5f, -0.5f, -0.5f, +0.5f, +0.5f, +0.5f, +0.5f, +0.5f,

		// left
		-0.5f, +0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, +0.5f, 
		-0.5f, +0.5f, -0.5f, -0.5f, -0.5f, +0.5f, -0.5f, +0.5f, +0.5f,

		// right
		+0.5f, -0.5f, -0.5f, +0.5f, +0.5f, -0.5f, +0.5f, +0.5f, +0.5f, 
		+0.5f, -0.5f, -0.5f, +0.5f, +0.5f, +0.5f, +0.5f, -0.5f, +0.5f 
	};

	public static final float[] UNIT_CUBE_NORMALS = GeometryUtilities.calculateNormals(UNIT_CUBE_TRIANGLES);
	
	
	public static IGeometry getDefaultCar() {
		return DefaultGeometry.createVN(Primitive.TRIANGLES, UNIT_CUBE_TRIANGLES, UNIT_CUBE_NORMALS);
	}
	
	
}
