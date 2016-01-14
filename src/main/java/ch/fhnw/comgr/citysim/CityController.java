package ch.fhnw.comgr.citysim;

import java.io.IOException;

import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.model.map.CitySimMap;
import ch.fhnw.comgr.citysim.pathAlgorithm.DijkstraAlgorithm;
import ch.fhnw.comgr.citysim.ui.InstructionField;
import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.image.Frame;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Vec3;

public class CityController extends DefaultController {

    private static Field[][] fields;
    private static InstructionField instructionField;
    private static CitySimMap map;

    public CityController(int[][] map) {

        setFields(new Field[map.length][map[0].length]);

        float startX = 0;
        float startY = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {

                Field field = createField(map[i][j]);

                field.setContent(map[i][j]);
                field.setName("Feld " + i + " " + j);

                field.setAuthorisations(map[i][j]);

                field.setPosition(new Vec3(startX + j, startY - i, 0f));

                getFields()[i][j] = field;
            }
        }

        this.map = CitySimMap.getInstance();
        CitySimMap.getInstance().setDijkstra(new DijkstraAlgorithm(getFields()));

    }

    public static InstructionField getInstructionField() {
        return instructionField;
    }

    public static void setInstructionField(InstructionField instructionField) {
        CityController.instructionField = instructionField;

    }

    public static Field getField(Vec3 position) {
        for (Field[] field : getFields()) {
            for (Field aField : field) {
                if (position.x >= aField.getBounds().getMinX() && position.x < aField.getBounds().getMaxX()) {
                    if (position.y >= aField.getBounds().getMinY() && position.y <= aField.getBounds().getMaxY()) {
                        return aField;
                    }
                }
            }
        }
        return null;
    }

    private static Field createField(int type) {
        switch (type) {
            case 11:
            case 10:
            case 12:
            case 13:
            case 0:
                return createField("/assets/road/grassfinal.jpg");
            case 1:
                return createField("/assets/road/roadEW.jpg");
            case 2:
                return createField("/assets/road/roadNS.jpg");
            case 3:
                return createField("/assets/road/roadNEWS.jpg");
            case 4:
                return createField("/assets/road/roadNE.jpg");
            case 5:
                return createField("/assets/road/roadNW.jpg");
            case 6:
                return createField("/assets/road/roadSE.jpg");
            case 7:
                return createField("/assets/road/roadSW.jpg");
            case 21:
                return createField("/assets/road/roadTN.jpg");
            case 22:
                return createField("/assets/road/roadTE.jpg");
            case 23:
                return createField("/assets/road/roadTS.jpg");
            case 24:
                return createField("/assets/road/roadTW.jpg");
        }

        return null;
    }

    private static Field createField(String asset) {
        IMaterial m = null;
        try {
            m = new ColorMapMaterial(RGBA.WHITE, Frame.create(CitySimGame.class.getResource(asset)).getTexture(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IGeometry g = Util.getDefaultField();
        return new Field(m, g);
    }

    public static Field[][] getFields() {
        return fields;
    }

    public static void setFields(Field[][] fields) {
        CityController.fields = fields;
    }

    public static CitySimMap getMap() {
        return map;
    }
}
