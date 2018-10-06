package com.github.burningrain.smash.converters.graphml;

/**
 * @author burningrain on 04.06.2018.
 */
public interface GraphAttributes {

    // граф
    String SCENARIO_TITLE = "scenarioTitle";

    // нода
    String ID = "id";
    String ELEMENT_CLASS = "elementClass";
    String TYPE = "type";
    String IS_START_NODE = "isStartNode";
    String IS_END_NODE = "isEndNode";
    String IS_FRONTIER_NODE = "isFrontierNode";
    String IS_CURRENT_NODE = "isCurrentNode";

    // дуга
    String LINK_TYPE = "linkType";

}