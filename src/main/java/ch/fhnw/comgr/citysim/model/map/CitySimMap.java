package ch.fhnw.comgr.citysim.model.map;

import java.util.ArrayList;
import java.util.List;

public class CitySimMap {

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

  private static final CitySimMap mapInstance = new CitySimMap();

  public static final int LAYER_STREET = 0;
  public static final int LAYER_STATIC = 1;
  public static final int LAYER_INTERACT = 2;
  public static final int LAYER_DYNAMIC = 3;

  private List<StaticObject> staticObjects;
  private List<InteractionObject> interactionObjects;
  private List<DynamicObject> dynamicObjects;

  private CitySimMap() {
    staticObjects = new ArrayList<>();
    interactionObjects = new ArrayList<>();
    dynamicObjects = new ArrayList<>();
  }

  public static CitySimMap getInstance() {
    return mapInstance;
  }



  /**
   * Adds an object to a layer at a specific position
   *
   * @param object
   * @return true if it could be added, false otherwise
   */
  public boolean addObjectToLayer(LayerObject object) {
        if (object instanceof InteractionObject) {
          interactionObjects.add((InteractionObject) object);
          return true;
        } else if (object instanceof DynamicObject) {
          dynamicObjects.add((DynamicObject) object);
          return true;
        } else if (object instanceof StaticObject) {
          staticObjects.add((StaticObject) object);
          return true;
        } else {
          return false;
        }
  }

  public List<DynamicObject> getDynamicObjects() {
    return dynamicObjects;
  }

  public List<InteractionObject> getInteractionObjects() {
    return interactionObjects;
  }

  public List<StaticObject> getStaticObjects() {
    return staticObjects;
  }

}
