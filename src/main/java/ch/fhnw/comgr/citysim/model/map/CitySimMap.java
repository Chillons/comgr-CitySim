package ch.fhnw.comgr.citysim.model.map;

import ch.fhnw.comgr.citysim.CitySimGame;
import ch.fhnw.comgr.citysim.model.map.layer.DynamicObject;
import ch.fhnw.comgr.citysim.model.map.layer.InteractionObject;
import ch.fhnw.comgr.citysim.model.map.layer.LayerObject;
import ch.fhnw.comgr.citysim.model.map.layer.StaticObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Content is organized in layers:
 * <p>
 * Layer 0:
 * map:
 * streets, gras, empty ground, most buildings -> static map
 * <p>
 * Layer :
 * Static Content:
 * decoration like trees, signs
 * <p>
 * Layer 2:
 * Interactable content
 * traffic lights, construction sites
 * <p>
 * Layer 3:
 * dynamic content
 * other cars, pedestrians, birds
 * <p>
 * Layer 4:
 * taxis
 * needs special movement
 */
public class CitySimMap {


    public static final int GRA = 0;
    public static final int E_W = 1;
    public static final int N_S = 2;
    public static final int CRO = 3;
    public static final int N_E = 4;
    public static final int N_W = 5;
    public static final int S_E = 6;
    public static final int S_W = 7;

    // Ausrichtung nach unten.
    public static final int H0S = 10;
    //Ausrichtung nach unten. Braucht rechts noch einmal gras
    public static final int H1S = 11;
    // Ausrichtung nach oben.
    public static final int H0N = 12;
    //Ausrichtung nach oben. Braucht links noch einmal gras
    public static final int H1N = 13;

    public static final int T_N = 21;
    public static final int T_E = 22;
    public static final int T_S = 23;
    public static final int T_W = 24;

    //@formatter:off
    public static final int[][] MAP = {
            //                                            10                                                               23
            {S_E, E_W, E_W, E_W, T_S, E_W, E_W, E_W, T_S, E_W, E_W, E_W, E_W, T_S, E_W, E_W, E_W, T_S, E_W, E_W, E_W, E_W, S_W},
            {N_S, H1S, GRA, H0S, N_S, H1S, GRA, H0S, N_S, H0S, H1S, GRA, H0S, N_S, H0S, H1S, GRA, N_S, H0S, H1S, GRA, H0S, N_S},
            {N_E, E_W, E_W, E_W, CRO, E_W, E_W, E_W, CRO, E_W, E_W, E_W, E_W, CRO, E_W, E_W, E_W, CRO, E_W, E_W, E_W, E_W, T_W},
            {H0S, H0S, H1S, GRA, N_S, H0S, H1S, GRA, N_S, H0S, H0S, H1S, GRA, N_S, H0S, H1S, GRA, N_S, H1S, GRA, H1S, GRA, N_S},
            {S_E, E_W, E_W, E_W, CRO, E_W, E_W, E_W, CRO, E_W, E_W, E_W, E_W, CRO, E_W, E_W, E_W, CRO, E_W, E_W, E_W, E_W, T_W},
            {N_S, H1S, GRA, H0S, N_S, H1S, GRA, H0S, N_S, H0S, H1S, GRA, H0S, N_S, H1S, GRA, H0S, N_S, H1S, GRA, H0S, H0S, N_S},
            {T_E, E_W, E_W, E_W, T_N, E_W, E_W, E_W, CRO, E_W, E_W, E_W, E_W, CRO, E_W, E_W, E_W, CRO, E_W, E_W, E_W, E_W, T_W},
            {N_S, H1S, GRA, H1S, GRA, H0S, H1S, GRA, N_S, H1S, GRA, H0S, H0S, N_S, H1S, GRA, H0S, N_S, H0S, H0S, H1S, GRA, N_S},
            {N_S, H0S, H0S, H0S, H1S, GRA, H0S, H0S, N_S, H0S, H1S, GRA, H0S, N_S, H0S, H1S, GRA, N_S, H0S, H0S, H1S, GRA, N_S},
            {T_E, E_W, E_W, E_W, T_S, E_W, E_W, E_W, T_N, E_W, E_W, E_W, E_W, CRO, E_W, E_W, E_W, CRO, E_W, E_W, E_W, E_W, T_W},
            {N_S, H1S, GRA, H0S, N_S, H0S, H1S, GRA, H1S, GRA, H0S, H0S, H0S, N_S, H1S, GRA, H0S, N_S, H1S, GRA, H0S, H0S, N_S},
            {N_S, H0S, H1S, GRA, N_S, H1S, GRA, H0S, H0S, H0S, H1S, GRA, H0S, N_S, H1S, GRA, H0S, N_S, H0S, H0S, H1S, GRA, N_S},
            {T_E, E_W, E_W, E_W, CRO, E_W, E_W, E_W, T_S, E_W, E_W, E_W, E_W, CRO, E_W, E_W, E_W, CRO, E_W, E_W, E_W, E_W, T_W},
            {N_S, H1S, GRA, H0S, N_S, H0S, H1S, GRA, N_S, H1S, GRA, H1S, GRA, N_S, H0S, H0S, H0S, N_S, H0S, H0S, H1S, GRA, N_S},
            {T_E, E_W, E_W, E_W, CRO, E_W, E_W, E_W, CRO, E_W, E_W, E_W, E_W, CRO, E_W, E_W, E_W, T_W, H0S, H1S, GRA, H0S, N_S},
            {N_S, H1S, GRA, H0S, N_S, H0S, H1S, GRA, N_S, H0S, H0S, H1S, GRA, N_S, H0S, H0S, H0S, N_S, H1S, GRA, H0S, H0S, N_S},
            {N_S, H0S, H1S, GRA, N_S, H0S, H1S, GRA, N_S, H0S, H1S, GRA, H0S, N_S, H0S, H1S, GRA, N_S, H0S, H1S, GRA, H0S, N_S},
            {N_E, E_W, E_W, E_W, T_N, E_W, E_W, E_W, T_N, E_W, E_W, E_W, E_W, T_N, E_W, E_W, E_W, T_N, E_W, E_W, E_W, E_W, N_W}  // 17
    };
    //@formatter:on

    private static final CitySimMap mapInstance = new CitySimMap();

    private final List<StaticObject> staticObjects;
    private final List<InteractionObject> interactionObjects;
    private final List<DynamicObject> dynamicObjects;

    private CitySimMap() {
        staticObjects = new ArrayList<>();
        interactionObjects = new ArrayList<>();
        dynamicObjects = new ArrayList<>();
    }

    public static CitySimMap getInstance() {
        return mapInstance;
    }

    public static void main(String[] args) {

//			IIORegistry registry = IIORegistry.getDefaultInstance();
//			registry.registerServiceProvider(new com.realityinteractive.imageio.tga.TGAImageReaderSpi());
        new CitySimGame();
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
