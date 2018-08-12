package com.github.burningrain.smash.converters.graphml;

import com.github.burningrain.smash.api.scenario.data.ScenarioData;
import com.github.burningrain.smash.api.scenario.data.StringScenarioConverter;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.io.*;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * @author burningrain on 04.06.2018.
 */
public class StringScenarioConverterImpl implements StringScenarioConverter {

    private static class WrapperDto {
        private String scenarioTitle;

        private String startId;
        private String endId;
    }

    public ScenarioData toScenarioData(String scenario) {
        final WrapperDto wrapperDto = new WrapperDto();
        SimpleDirectedGraph<NodeData, TransitionData> graph = new SimpleDirectedGraph<>(TransitionData.class);

        GraphMLImporter<NodeData, TransitionData> importer = new GraphMLImporter<>(
            new VertexProvider<NodeData>() {
                @Override
                public NodeData buildVertex(String id, Map<String, Attribute> attributes) {
                    if(wrapperDto.scenarioTitle == null){
                        wrapperDto.scenarioTitle = attributes.get(GraphAttributes.SCENARIO_TITLE).getValue();
                    }

                    final String identifier = attributes.get(GraphAttributes.ID).getValue();
                    final boolean isStart = Boolean.valueOf(attributes.get(GraphAttributes.IS_START_NODE).getValue());
                    final boolean isEnd = Boolean.valueOf(attributes.get(GraphAttributes.IS_END_NODE).getValue());
                    if(isStart){
                        wrapperDto.startId = identifier;
                    }
                    if(isEnd) {
                        wrapperDto.endId = identifier;
                    }

                    return NodeData.of(
                            Boolean.valueOf(attributes.get(GraphAttributes.IS_PASSED).getValue()),
                            NodeData.Type.valueOf(attributes.get(GraphAttributes.TYPE).getValue()),
                            identifier,
                            attributes.get(GraphAttributes.ELEMENT_CLASS).getValue()
                    );
                }
            },
            new EdgeProvider<NodeData, TransitionData>() {

                @Override
                public TransitionData buildEdge(NodeData from, NodeData to, String label, Map<String, Attribute> attributes) {
                    final TransitionData.Type type = TransitionData.Type.valueOf(attributes.get(GraphAttributes.LINK_TYPE).getValue());
                    return TransitionData.of(type, from.getId(), to.getId(), false);
                }
            }
        );
        try {
            importer.importGraph(graph, new ByteArrayInputStream(scenario.getBytes()));
        } catch (ImportException e) {
            throw new RuntimeException(e);
        }

        ScenarioData.Builder builder = new ScenarioData.Builder();
        for (NodeData nodeData : graph.vertexSet()) {
            builder.addNodeData(nodeData);
        }
        for (TransitionData transitionData : graph.edgeSet()) {
            builder.addTransitionData(transitionData);
        }
        builder.setTitle(wrapperDto.scenarioTitle);
        builder.setStartNode(wrapperDto.startId);
        builder.setEndNode(wrapperDto.endId);
        return builder.build();
    }

}