package ch.fhnw.comgr.citysim.pathAlgorithm;

import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.model.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DijkstraAlgorithm implements PathAlgorithm {

  private Field[][] fields;
  private List<Field> nodes;
  private List<Path> paths;
  private LinkedList<Field> pathsProNode;

  private Set<Field> settledNodes;
  private Set<Field> unSettledNodes;
  private Map<Field, Field> predecessors;
  private Map<Field, Integer> distance;

  

  public DijkstraAlgorithm(Field[][] fields) {
    // create a copy of the array so that we can operate on this array
	this.fields = fields;
    nodes = new ArrayList<Field>();
    paths = new ArrayList<Path>();
    getPaths();
  }
  

  
  @Override
  public LinkedList<Field> getPathFromTo(Field source, Field target){
	  LinkedList <Field> path = new LinkedList<Field>();
	  for (Field node : nodes) {
		  if (source.equals(node)) {
			  execute(node);
			  return getPath(target);
		  }
	  }
	  // Keine station gefunden
	  path.add(source);
	  return path;
  }
  
  
  @Override
  public int getDistanceFromTo(Field source, Field target){
	  LinkedList <Field> path = new LinkedList<Field>();
	  for (Field node : nodes) {
		  if (source.equals(node)) {
			  execute(node);
			  int distTotal = 0;
			  LinkedList<Field> stations = getPath(target);
			  for (int j = 0; j < stations.size() - 1; j++) {
				  distTotal += getDistance(stations.get(j), stations.get(j + 1));
			  }
			  return distTotal;
		  }
	  }
	  return -1;
  }
  
  @Override
  public int getTimeFromTo(Field source, Field target){
	  LinkedList <Field> path = new LinkedList<Field>();
	  for (Field node : nodes) {
		  if (source.equals(node)) {
			  execute(node);
			  int distTotal = 0;
			  LinkedList<Field> stations = getPath(target);
			  for (int j = 0; j < stations.size() - 1; j++) {
				  distTotal += getDistance(stations.get(j), stations.get(j + 1));
			  }
			  return (60 * distTotal) / 50; //für eine Geschwindigkeit von 50km/h
		  	}  
		  }

	  return -1;
  }


  /*
   * This method returns the path from the source to the selected target and
   * NULL if no path exists
   */
  private LinkedList<Field> getPath(Field target) {
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
  

  
  private List<Path> getPaths(){
	  searchForPaths();
	  for(int i=0; i<nodes.size(); i++) {
		  pathsProNode = new LinkedList<Field>();
		  execute(nodes.get(i));
		  for (Field node : nodes) {
			  pathsProNode = getPath(node);
		  }
	  }
	  return paths;
  }
  
  @Override
  public Field[][] getFields(){
	  return fields;	  
  }
  
  @Override
  public List<Field> getNodes(){
	  return nodes;	  
  }
  
  private void execute(Field source) {
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
  
  
	private void searchForPaths(){
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
					case 21:
						nodes.add(fields[i][j]);
						searchNorth(i,j);
						searchEast(i,j);
						searchWest(i,j);
						break;
					case 22:
						nodes.add(fields[i][j]);
						searchNorth(i,j);
						searchSouth(i,j);
						searchEast(i,j);
						break;
					case 23:
						nodes.add(fields[i][j]);
						searchSouth(i,j);
						searchEast(i,j);
						searchWest(i,j);
						break;
					case 24:
						nodes.add(fields[i][j]);
						searchNorth(i,j);
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
			}else if(fields[i-1][j].getContent() == 6 || fields[i-1][j].getContent() == 7 || fields[i-1][j].getContent() == 3
					|| fields[i-1][j].getContent() == 22 || fields[i-1][j].getContent() == 23 || fields[i-1][j].getContent() == 24){
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
			}else if(fields[i+1][j].getContent() == 4 || fields[i+1][j].getContent() == 5 || fields[i+1][j].getContent() == 3
					|| fields[i+1][j].getContent() == 21 || fields[i+1][j].getContent() == 22 || fields[i+1][j].getContent() == 24){
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
			}else if(fields[i][j+1].getContent() == 5 || fields[i][j+1].getContent() == 7 || fields[i][j+1].getContent() == 3
					|| fields[i][j+1].getContent() == 21 || fields[i][j+1].getContent() == 23 || fields[i][j+1].getContent() == 24){
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
			}else if(fields[i][j-1].getContent() == 4 || fields[i][j-1].getContent() == 6 || fields[i][j-1].getContent() == 3
					|| fields[i][j-1].getContent() == 21 || fields[i][j-1].getContent() == 22 || fields[i][j-1].getContent() == 23){
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
