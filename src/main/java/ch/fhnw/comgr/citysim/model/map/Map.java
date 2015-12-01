package ch.fhnw.comgr.citysim.model.map;

import java.util.ArrayList;
import java.util.List;

public class Map {

  /*
  Content is organized in layers:

    Layer 0:
    map:
    streets, gras, empty ground -> static map

    Layer :
    Static Content:
    buildings (, signs)

    Layer 2:
    Interactable content
    traffic lights, construction sites

    Layer 3:
    dynamic content
    other cars, pedestrians, birds

    Layer 4:
    taxis
    needs special movement

   */

  public static final int LAYER_STREET = 0;
  public static final int LAYER_STATIC = 1;
  public static final int LAYER_INTERACT = 2;
  public static final int LAYER_DYNAMIC = 3;

  private List<LayerObject> staticObjects;
  private List<LayerObject> interactionObjects;
  private List<LayerObject> dynamicObjects;

  public Map() {
    staticObjects = new ArrayList<>();
    interactionObjects = new ArrayList<>();
    dynamicObjects = new ArrayList<>();
  }

  /**
   * Adds an object to a layer at a specific position
   *
   * @param object
   * @param layerId
   * @return true if it could be added, false otherwise
   */
  public boolean addObjectToLayer(LayerObject object, int layerId) {

    switch (layerId) {
      case LAYER_STATIC:
        staticObjects.add(object);
        break;
      case LAYER_INTERACT:
        interactionObjects.add(object);
        break;
      case LAYER_DYNAMIC:
        dynamicObjects.add(object);
        break;
    }
    return false;
  }

}
