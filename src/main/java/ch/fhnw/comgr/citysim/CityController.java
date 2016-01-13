package ch.fhnw.comgr.citysim;

import ch.fhnw.comgr.citysim.model.Field;
import ch.fhnw.comgr.citysim.ui.InteractionPanel;
import ch.fhnw.comgr.citysim.ui.MessagePanel;
import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.image.Frame;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Vec3;

import java.io.IOException;

public class CityController extends DefaultController {
    private static Field[][] fields;
    private static MessagePanel messagePanel;

    public CityController(int[][] strasse) {

        fields = new Field[strasse.length][strasse[0].length];

        float startX = 0;
        float startY = 0;

        for (int i = 0; i < strasse.length; i++) {
            for (int j = 0; j < strasse[i].length; j++) {

                Field field = createField(strasse[i][j]);

                field.setContent(strasse[i][j]);
                field.setName("Feld " + i + " " + j);

                field.setAuthorisations(strasse[i][j]);

                field.setPosition(new Vec3(startX + j, startY - i, 0f));

                fields[i][j] = field;
            }
        }

        PathAlgorithm pathAlgorithm = new PathAlgorithm(CityController.fields);

    }

    public static MessagePanel getMessagePanel() {
        return messagePanel;
    }

    public static void setMessagePanel(MessagePanel mp) {
        CityController.messagePanel = mp;

    }

    public static Field getField(Vec3 position) {
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (position.x >= fields[i][j].getBounds().getMinX() && position.x < fields[i][j].getBounds().getMaxX()) {
                    if (position.y >= fields[i][j].getBounds().getMinY() && position.y <= fields[i][j].getBounds().getMaxY()) {
                        return fields[i][j];
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

}
