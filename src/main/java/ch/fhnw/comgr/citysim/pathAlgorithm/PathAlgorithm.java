package ch.fhnw.comgr.citysim.pathAlgorithm;

import ch.fhnw.comgr.citysim.model.Field;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maurice Gschwind on 14.01.2016.
 */
public interface PathAlgorithm {
    LinkedList<Field> getPathFromTo(Field source, Field target);

    int getDistanceFromTo(Field source, Field target);

    int getTimeFromTo(Field source, Field target);

    Field[][] getFields();

    List<Field> getNodes();
}
