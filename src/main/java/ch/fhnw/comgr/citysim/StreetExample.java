/*
 * Copyright (c) 2013 - 2015 Stefan Muller Arisona, Simon Schubiger, Samuel von Stachelski
 * Copyright (c) 2013 - 2015 FHNW & ETH Zurich
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *  Neither the name of FHNW / ETH Zurich nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.fhnw.comgr.citysim;

import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.Texture;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.IView.ViewType;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public final class StreetExample {
	
	public static final int GRAS = 0;
	public static final int STREET_H = 1;
	public static final int STREET_V = 2;
	public static final int AMPEL = 3;
	
	
	public static final int[][] strasse = { 
			{ GRAS, GRAS, STREET_V, GRAS }, 
			{ STREET_H, STREET_H, AMPEL, STREET_H}, 
			{ GRAS, GRAS,STREET_V,GRAS} };

	public static float[] vertices = { 
			0, 0, 0, 
			1, 0, 0, 
			1, 1, 0, 
			
			0, 0, 0, 
			1, 1, 0, 
			0, 1, 0 };
	
	public static void main(String[] args) {
		new StreetExample();
	}

	private static IMesh makeColoredTriangle(float off) {
		float[] vertices = { off + 0, 0, 
				off + 0, 0, 
				off + 0, 0.5f, 
				off + 0.5f, 0, 0.5f };
		float[] colors = { 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1 };

		DefaultGeometry g = DefaultGeometry.createVC(Primitive.TRIANGLES, vertices, colors);
		return new DefaultMesh(new ColorMaterial(RGBA.WHITE, true), g, IMesh.Queue.DEPTH);
	}

	// Setup the whole thing
	public StreetExample() {
		// Create controller
		IController controller = new DefaultController();

		// Create view
		// Neues Config machen
		IView view = new DefaultView(controller, 100, 100, 500, 500, new IView.Config(ViewType.INTERACTIVE_VIEW, 0, new IView.ViewFlag[0]), "Simple Sphere");
		
		ICamera camera = new Camera(new Vec3(0, -5, 5), Vec3.ZERO);

		// Create scene and add triangle
		IScene scene = new DefaultScene(controller);
		controller.setScene(scene);

		IMesh colDreick = makeColoredTriangle(2);
		scene.add3DObject(colDreick);
		
		
		scene.add3DObject(camera);
		controller.setCamera(view, camera);
	
		
		float startX = -(strasse[0].length / 2.0f);
		float startY = -(strasse.length / 2.0f);
		
		for (int i = 0; i < strasse.length; i++) {
			for (int j = 0; j < strasse[i].length; j++) {
				IMesh tmp = makeField(strasse[i][j]);
				tmp.setName("Feld " + i + " " + j);
				tmp.setTransform(Mat4.translate(startX+j, startY+i, 0));
				scene.add3DObject(tmp);
			}
		}
		
	}
	
	private static IMesh makeField(int type) {
		switch (type) {
		case GRAS:
			return makeGrass();
		case STREET_H:
			return makeStreetHorizontal();
		case STREET_V:
			return makeStreetVertical();
		case AMPEL:
			return makeAmpel();
		}
		return null;
	}
	
	
	private static IMesh makeStreetHorizontal() {
		float[] colors = new float[] {.5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1};
		float[] texCoords = { 0, 0, 1, 0, 1, 1,  0, 0, 1, 1, 0, 1 };
		IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(StreetExample.class.getResource("/assets/road.jpg")), true);
		IGeometry g = DefaultGeometry.createVCM(Primitive.TRIANGLES, vertices, colors, texCoords);
		return new DefaultMesh(m, g);
		
	}
	
	private static IMesh makeStreetVertical() {
		float[] colors = new float[] {.5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1};
		float[] texCoords = { 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1 };
		IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(StreetExample.class.getResource("/assets/road.jpg")), true);
		IGeometry g = DefaultGeometry.createVCM(Primitive.TRIANGLES, vertices, colors, texCoords);
		return new DefaultMesh(m, g);
		
	}
	
	private static IMesh makeGrass() {
		float[] colors = new float[] {.5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1};
		float[] texCoords = { 0, 0, 1, 0, 1, 1,  0, 0, 1, 1, 0, 1 };
		IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(StreetExample.class.getResource("/assets/grass.jpg")), true);
		IGeometry g = DefaultGeometry.createVCM(Primitive.TRIANGLES, vertices, colors, texCoords);
		return new DefaultMesh(m, g);
	}
	
	private static IMesh makeAmpel() {
		float[] colors = new float[] {.5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1, .5f, .5f, .5f, 1};
		float[] texCoords = { 0, 0, 1, 0, 1, 1,  0, 0, 1, 1, 0, 1 };
		IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(StreetExample.class.getResource("/assets/fhnw_logo.jpg")), true);
		IGeometry g = DefaultGeometry.createVCM(Primitive.TRIANGLES, vertices, colors, texCoords);
		return new DefaultMesh(m, g);
	}
}
