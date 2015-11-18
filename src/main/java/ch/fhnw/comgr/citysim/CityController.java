package ch.fhnw.comgr.citysim;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.IMesh.Flag;
import ch.fhnw.ether.scene.mesh.IMesh.Queue;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.Texture;
import ch.fhnw.util.UpdateRequest;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;
import ch.fhnw.util.math.geometry.BoundingBox;

public class CityController extends DefaultController implements I3DObject{
	private final Field[][] fields;
	private final List<Field> nodes;
	private final List<Path> paths;	
	private final List<List<IMesh>> taxis;
	
	private String name = "CityBoard";
	private Queue queue;
	private Set<Flag> flags;
	private IMaterial material;
	private IGeometry geometry;
	private Vec3 position = Vec3.ZERO;
	private Mat4 transform = Mat4.ID;
	private BoundingBox bb;
	  
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
	
	
	/*public void getActualField(float positionX, float positionY){
	for (int i = 0; i < fields.length; i++) {			
		for (int j = 0; j < fields[i].length; j++) {
			if(positionX >= fields[i][j].getStartPositionX() && positionX <= fields[i][j].getStopPositionX()){
				if(positionY >= fields[i][j].getStartPositionY() && positionY <= fields[i][j].getStopPositionY()){
					System.out.println("Actual field is: " + fields[i][j].getName());
				}
			}
		}
	}
}*/
	
	
	
	

	
		
		
		
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


	private final UpdateRequest update = new UpdateRequest(true);
	
	// I3DObject implementation

	@Override
	public BoundingBox getBounds() {
		if (bb == null) {
			bb = new BoundingBox();
			float[] in = new float[3];
			float[] out = new float[3];
			geometry.inspect(0, (attribute, data) -> {
				if (transform != Mat4.ID) {
					for (int i = 0; i < data.length; i += 3) {
						in[0] = data[i + 0];
						in[1] = data[i + 1];
						in[2] = data[i + 2];
						transform.transform(in, out);
						out[0] += position.x;
						out[1] += position.y;
						out[2] += position.z;
						bb.add(out);
					}

				} else {
					for (int i = 0; i < data.length; i += 3) {
						bb.add(data[i + 0] + position.x, data[i + 1] + position.y, data[i + 2] + position.z);
					}
				}
			});
		}
		return bb;
	}

	@Override
	public Vec3 getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vec3 position) {
		this.position = position;
		bb = null;
		updateRequest();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public UpdateRequest getUpdater() {
		return update;
	}

	private void updateRequest() {
		update.request();
	}
	
	


	

}
