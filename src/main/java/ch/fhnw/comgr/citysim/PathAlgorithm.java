package ch.fhnw.comgr.citysim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PathAlgorithm {

  private static Field[][] fields;
  private static List<Field> nodes;
  private static List<Path> paths;	
  private static LinkedList<Field> pathsProNode;

  private static Set<Field> settledNodes;
  private static Set<Field> unSettledNodes;
  private static Map<Field, Field> predecessors;
  private static Map<Field, Integer> distance;
  

  public PathAlgorithm(Field[][] fields) {
    // create a copy of the array so that we can operate on this array
	PathAlgorithm.fields = fields;
    PathAlgorithm.nodes = new ArrayList<Field>();
    PathAlgorithm.paths = new ArrayList<Path>();
    PathAlgorithm.getPaths();
  }
  

  
  public static LinkedList<Field> getPathFromTo(Field source, Field target){  
	  LinkedList <Field> path = new LinkedList<Field>();
	  searchForPaths();
	  for(int i=0; i<nodes.size(); i++) { 
		  if(source.equals(nodes.get(i))){ 
			  PathAlgorithm.execute(nodes.get(i));
			  return PathAlgorithm.getPath(target);
		  }
	  }
	  // Keine station gefunden
	  path.add(source);
	  return path;
  }
  
  
  public static int getDistanceFromTo(Field source, Field target){  
	  LinkedList <Field> path = new LinkedList<Field>();
	  searchForPaths();
	  for(int i=0; i<nodes.size(); i++) { 
		  if(source.equals(nodes.get(i))){ 
			  PathAlgorithm.execute(nodes.get(i));
			  int distTotal = 0;
			  LinkedList<Field> stations = PathAlgorithm.getPath(target);
			  for(int j=0; j< stations.size()-1; j++){
				  distTotal += getDistance(stations.get(j), stations.get(j+1));
			  }
			  return distTotal;
		  }
	  }
	  return -1;
  }
  
  public static int getTimeFromTo(Field source, Field target){  
	  LinkedList <Field> path = new LinkedList<Field>();
	  searchForPaths();
	  for(int i=0; i<nodes.size(); i++) { 
		  if(source.equals(nodes.get(i))){ 
			  PathAlgorithm.execute(nodes.get(i));
			  int distance = PathAlgorithm.getPath(target).size();
			  int time = (60 * distance) / 50; //für eine Geschwindigkeit von 50km/h
			  return time;
		  }
	  }

	  return -1;
  }


  /*
   * This method returns the path from the source to the selected target and
   * NULL if no path exists
   */
  private static LinkedList<Field> getPath(Field target) {
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
  

  
  private static List<Path> getPaths(){
	  PathAlgorithm.searchForPaths();
	  for(int i=0; i<nodes.size(); i++) {
		  pathsProNode = new LinkedList<Field>();
		  PathAlgorithm.execute(nodes.get(i));
		  for(int j=0; j<nodes.size();j++){  
			  pathsProNode = PathAlgorithm.getPath(nodes.get(j));
		  }
	  }
	  return paths;
  }
  
  public static Field[][] getFields(){
	  return fields;	  
  }
  
  public static List<Field> getNodes(){
	  return nodes;	  
  }
  
  private static void execute(Field source) {
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

  private static void findMinimalDistances(Field node) {
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

  private static int getDistance(Field node, Field target) {
    for (Path p : paths) {
      if (p.getSource().equals(node)
          && p.getDestination().equals(target)) {
        return p.getWeight();
      }
    }
    throw new RuntimeException("Should not happen");
  }

  private static List<Field> getNeighbors(Field node) {
    List<Field> neighbors = new ArrayList<Field>();
    for (Path p : paths) {
      if (p.getSource().equals(node)
          && !isSettled(p.getDestination())) {
        neighbors.add(p.getDestination());
      }
    }
    return neighbors;
  }

  private static Field getMinimum(Set<Field> vertexes) {
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

  private static boolean isSettled(Field vertex) {
    return settledNodes.contains(vertex);
  }

  private static int getShortestDistance(Field destination) {
    Integer d = distance.get(destination);
    if (d == null) {
      return Integer.MAX_VALUE;
    } else {
      return d;
    }
  }
  
  
	private static void searchForPaths(){
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

	private static void searchNorth(int i, int j){
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
	
	private static void searchSouth(int i, int j){
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
		

	
	private static void searchEast(int i, int j){
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
	
	
	private static void searchWest(int i, int j){
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
