package ch.fhnw.comgr.citysim;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PathAlgorithm {

  private final Field[][] fields;
  private final List<Field> nodes;
  private final List<Path> paths;	
  private LinkedList<Field> pathsProNode;

  private Set<Field> settledNodes;
  private Set<Field> unSettledNodes;
  private Map<Field, Field> predecessors;
  private Map<Field, Integer> distance;
  

  public PathAlgorithm(Field[][] fields) {
    // create a copy of the array so that we can operate on this array
	this.fields = fields;
    this.nodes = new ArrayList<Field>();
    this.paths = new ArrayList<Path>();
  }
  
  
  public List<Path> getPaths(){
	  searchForPaths();
	  for(int i=0; i<nodes.size(); i++) {
		  pathsProNode = new LinkedList<Field>();
		  this.execute(nodes.get(i));
		  for(int j=0; j<nodes.size();j++){  
			  pathsProNode = this.getPath(nodes.get(j));
			  if(pathsProNode !=null){
				  nodes.get(i).addToPaths(pathsProNode);
			  }
		  }
		  
	  }
	  return paths;
  }
  
 
  public List<Field> getNodes(){
	  return nodes;	  
  }
  
  
  public void execute(Field source) {
    settledNodes = new HashSet<Field>();
    unSettledNodes = new HashSet<Field>();
    distance = new HashMap<Field, Integer>();
    predecessors = new HashMap<Field, Field>();
    distance.put(source, 0);
    unSettledNodes.add(source);
    while (unSettledNodes.size() > 0) {
      Field node = getMinimum(unSettledNodes);
      settledNodes.add(node);
      unSettledNodes.remove(node);
      findMinimalDistances(node);
    }
  }

  private void findMinimalDistances(Field node) {
    List<Field> adjacentNodes = getNeighbors(node);
    for (Field target : adjacentNodes) {
      if (getShortestDistance(target) > getShortestDistance(node)
          + getDistance(node, target)) {
        distance.put(target, getShortestDistance(node)
            + getDistance(node, target));
        predecessors.put(target, node);
        unSettledNodes.add(target);
      }
    }

  }

  private int getDistance(Field node, Field target) {
    for (Path p : paths) {
      if (p.getSource().equals(node)
          && p.getDestination().equals(target)) {
        return p.getWeight();
      }
    }
    throw new RuntimeException("Should not happen");
  }

  private List<Field> getNeighbors(Field node) {
    List<Field> neighbors = new ArrayList<Field>();
    for (Path p : paths) {
      if (p.getSource().equals(node)
          && !isSettled(p.getDestination())) {
        neighbors.add(p.getDestination());
      }
    }
    return neighbors;
  }

  private Field getMinimum(Set<Field> vertexes) {
    Field minimum = null;
    for (Field vertex : vertexes) {
      if (minimum == null) {
        minimum = vertex;
      } else {
        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
          minimum = vertex;
        }
      }
    }
    return minimum;
  }

  private boolean isSettled(Field vertex) {
    return settledNodes.contains(vertex);
  }

  private int getShortestDistance(Field destination) {
    Integer d = distance.get(destination);
    if (d == null) {
      return Integer.MAX_VALUE;
    } else {
      return d;
    }
  }

  /*
   * This method returns the path from the source to the selected target and
   * NULL if no path exists
   */
  public LinkedList<Field> getPath(Field target) {
    LinkedList<Field> path = new LinkedList<Field>();
    Field step = target;
    // check if a path exists
    if (predecessors.get(step) == null) {
      return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
      step = predecessors.get(step);
      path.add(step);
    }
    // Put it into the correct order
    Collections.reverse(path);
    return path;
  }
  

  
  
  
	public void searchForPaths(){
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
					switch (fields[i][j].getContent()) {
					case 0:
						break;
					case 1:
						break;
					case 2:
						break;
						
					case 3:
						// Crossing
						nodes.add(fields[i][j]);
						searchNorth(i,j);
						searchSouth(i,j);
						searchEast(i,j);
						searchWest(i,j);
						break;
					case 4:
						nodes.add(fields[i][j]);
						searchNorth(i,j);
						searchEast(i,j);
						break;
					case 5:
						nodes.add(fields[i][j]);
						searchNorth(i,j);
						searchWest(i,j);
						break;
					case 6:
						nodes.add(fields[i][j]);
						searchSouth(i,j);
						searchEast(i,j);
						break;
					case 7:
						nodes.add(fields[i][j]);
						searchSouth(i,j);
						searchWest(i,j);
						break;
					}
			}
		
	}
	}

	private void searchNorth(int i, int j){
		Field startField = fields[i][j];
		Field stopField = fields[i][j];
		Path path = null;
		int length = 0;
		while(i>0){
			if(fields[i-1][j].getContent() == 2){
				// Strasse fährt weiter oben
				stopField = fields[i-1][j];
				length++;
				i--;
				//System.out.println(stopField.getName());
			}else if(fields[i-1][j].getContent() == 6 || fields[i-1][j].getContent() == 7 || fields[i-1][j].getContent() == 3){
				// Ankunft zur nächste aktzeptierenden Kreuzung
				stopField = fields[i-1][j];
				path = new Path(startField, stopField, length);
				paths.add(path);
				break;
			}else{
				break;
			}
		}
	}
	
	private void searchSouth(int i, int j){
		Field startField = fields[i][j];
		Field stopField = fields[i][j];
		Path path = null;
		int length = 0;
		while(i<fields.length-1){
			if(fields[i+1][j].getContent() == 2){
				// Strasse fährt weiter runter
				stopField = fields[i+1][j];
				length++;
				i++;
			}else if(fields[i+1][j].getContent() == 4 || fields[i+1][j].getContent() == 5 || fields[i+1][j].getContent() == 3){
				// Ankunft zur nächste aktzeptierenden Kreuzung
				stopField = fields[i+1][j];
				path = new Path(startField, stopField, length);
				paths.add(path);
				break;
			}else{
				break;
			}
		}
	}
		

	
	private void searchEast(int i, int j){
		Field startField = fields[i][j];
		Field stopField = fields[i][j];
		Path path = null;
		int length = 0;
		while(j<fields[i].length-1){
			if(fields[i][j+1].getContent() == 1){
				// Strasse fährt weiter rechts
				stopField = fields[i][j+1];
				length++;
				j++;
			}else if(fields[i][j+1].getContent() == 5 || fields[i][j+1].getContent() == 7 || fields[i][j+1].getContent() == 3){
				// Ankunft zur nächste aktzeptierenden Kreuzung
				stopField = fields[i][j+1];
				path = new Path(startField, stopField, length);
				paths.add(path);
				break;
			}else{
				break;
			}
		}
		
	}
	
	
	private void searchWest(int i, int j){
		Field startField = fields[i][j];
		Field stopField = fields[i][j];
		Path path = null;
		int length = 0;
		while(j>0){
			
			if(fields[i][j-1].getContent() == 1){
				// Strasse fährt weiter oben
				
				stopField = fields[i][j-1];
				length++;
				j--;
			}else if(fields[i][j-1].getContent() == 4 || fields[i][j-1].getContent() == 6 || fields[i][j-1].getContent() == 3){
				// Ankunft zur nächste aktzeptierenden Kreuzung
				stopField = fields[i][j-1];
				path = new Path(startField, stopField, length);
				paths.add(path);
				break;
			}else{
				break;
			}
		}
		
	}
	

} 
