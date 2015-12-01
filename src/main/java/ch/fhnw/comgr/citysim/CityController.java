package ch.fhnw.comgr.citysim;


import java.util.ArrayList;
import java.util.List;

import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.Texture;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Vec3;

public class CityController extends DefaultController{
	private final Field[][] fields;
	private final List<Field> nodes;
	private final List<Path> paths;	
	private final List<List<IMesh>> taxis;
		  
	public CityController(int[][] strasse){

		fields = new Field[strasse.length][strasse[0].length];

		
		float startX = -(strasse[0].length / 2.0f);
		float startY = (strasse.length / 2.0f);
		
		for (int i = 0; i < strasse.length; i++) {
			for (int j = 0; j < strasse[i].length; j++) {
				
				Field field = createField(strasse[i][j]);
				
				field.setContent(strasse[i][j]);
				field.setName("Feld " + i + " " + j);
				
				field.setPosition(new Vec3(startX+j, startY-i, 0f));
				//field.setTransform(Mat4.translate(startX+j, startY-i, 0f));
				
				fields[i][j] = field;				
			}
		}
		
		PathAlgorithm pathAlgorithm = new PathAlgorithm(this.fields);	
		paths = pathAlgorithm.getPaths();	
		nodes = pathAlgorithm.getNodes();
		taxis = new ArrayList<List<IMesh>>();
	}
	
	
	public Field[][] getFields(){
		return fields;
	}
	

	public List<Field> getNodes(){
		return nodes;
	}
	
	
	public List<List<IMesh>> getTaxis(){
		return taxis;
	}
	
	
	public void addTaxi(List<IMesh> t){
		taxis.add(t);
	}
	
	
		private static Field createField(int type){
			switch (type) {
			case 0:
				return createField("/assets/grass.jpg");
			case 1:
				return createField("/assets/roadEW.jpg");
			case 2:
				return createField("/assets/roadNS.jpg");
			case 3:
				return createField("/assets/roadNEWS.jpg");
			case 4:
				return createField("/assets/roadNE.jpg");
			case 5:
				return createField("/assets/roadNW.jpg");
			case 6:
				return createField("/assets/roadSE.jpg");
			case 7:
				return createField("/assets/roadSW.jpg");
			}
			return null;
		}
		
				
		private static Field createField(String asset) {
			IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(StreetExample.class.getResource(asset)), true);
			IGeometry g = Util.getDefaultField();
			return new Field(m, g);
		}

	
}
