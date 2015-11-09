package ch.fhnw.comgr.citysim;

import ch.fhnw.ether.scene.mesh.IMesh.Queue;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.material.IMaterial;

public class Path{
	  private final Field source;
	  private final Field destination;
	  private final int weight; 
	  

	  public Path(Field source, Field destination, int weight) {
	    this.source = source;
	    this.destination = destination;
	    this.weight = weight;
	  }
	  
	  public Field getDestination() {
	    return destination;
	  }

	  public Field getSource() {
	    return source;
	  }
	  public int getWeight() {
	    return weight;
	  }
	  
	  @Override
	  public String toString() {
	    return source + " " + destination;
	  }
	  
	  
	} 
