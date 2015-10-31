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
	public static final int STREET_EAST_WEST = 1;
	public static final int STREET_NORTH_SOUTH = 2;
	public static final int CROSSING = 3;
	public static final int STREET_NORTH_EAST = 4;
	public static final int STREET_NORTH_WEST = 5;
	public static final int STREET_SOUTH_EAST = 6;
	public static final int STREET_SOUTH_WEST = 7;
	
	
	
	public static final int[][] strasse = { 
			{ STREET_SOUTH_EAST, 	STREET_EAST_WEST, 	STREET_EAST_WEST, 	STREET_EAST_WEST, 	STREET_SOUTH_WEST }, 
			{ STREET_NORTH_SOUTH, 	GRAS, 				GRAS,	 			GRAS, 				STREET_NORTH_SOUTH }, 
			{ CROSSING,  			STREET_EAST_WEST, 	STREET_EAST_WEST, 	STREET_EAST_WEST, 	CROSSING },
			{ STREET_NORTH_SOUTH, 	GRAS, 				GRAS,	 			GRAS, 				STREET_NORTH_SOUTH },
			{ STREET_NORTH_EAST, 	STREET_EAST_WEST,	STREET_EAST_WEST,	STREET_EAST_WEST,	STREET_NORTH_WEST} };

	
	public static void main(String[] args) {
		new StreetExample();
	}

	// Setup the whole thing
	public StreetExample() {
		// Create controller
		IController controller = new DefaultController();
		controller.run(time -> {
			// Create view
			// Neues Config machen
			IView view = new DefaultView(controller, 100, 100, 500, 500, new IView.Config(ViewType.INTERACTIVE_VIEW, 0, new IView.ViewFlag[0]), "Simple Sphere");
			
			ICamera camera = new Camera(new Vec3(0, -5, 5), Vec3.ZERO);

			// Create scene and add triangle
			IScene scene = new DefaultScene(controller);
			
			controller.setScene(scene);
			
			scene.add3DObject(camera);
			controller.setCamera(view, camera);
		
			
			float startX = -(strasse[0].length / 2.0f);
			float startY = (strasse.length / 2.0f);
			
			for (int i = 0; i < strasse.length; i++) {
				for (int j = 0; j < strasse[i].length; j++) {
					IMesh tmp = makeField(strasse[i][j]);
					tmp.setName("Feld " + i + " " + j);
					tmp.setTransform(Mat4.translate(startX+j, startY-i, 0));
					scene.add3DObject(tmp);
				}
			}
		});
	}
	
	private static IMesh makeField(int type) {
		switch (type) {
		case GRAS:
			return makeField("/assets/grass.jpg");
		case STREET_EAST_WEST:
			return makeField("/assets/roadEW.jpg");
		case STREET_NORTH_SOUTH:
			return makeField("/assets/roadNS.jpg");
		case CROSSING:
			return makeField("/assets/roadNEWS.jpg");
		case STREET_NORTH_EAST:
			return makeField("/assets/roadNE.jpg");
		case STREET_NORTH_WEST:
			return makeField("/assets/roadNW.jpg");
		case STREET_SOUTH_EAST:
			return makeField("/assets/roadSE.jpg");
		case STREET_SOUTH_WEST:
			return makeField("/assets/roadSW.jpg");
		}
		return null;
	}
	
	private static IMesh makeField(String asset) {
		IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(StreetExample.class.getResource(asset)), true);
		IGeometry g = Util.getDefaultGeometry();
		return new DefaultMesh(m, g);
	}
	
}
