package ch.fhnw.comgr.citysim.model;

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
