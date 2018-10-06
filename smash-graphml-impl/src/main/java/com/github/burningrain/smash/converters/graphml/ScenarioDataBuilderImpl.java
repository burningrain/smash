package com.github.burningrain.smash.converters.graphml;

import com.github.burningrain.smash.api.scenario.data.ScenarioDataBuilder;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.io.*;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author burningrain on 04.06.2018.
 */
public class ScenarioDataBuilderImpl implements ScenarioDataBuilder {

    private String title;
    private String startNodeId;
    private Collection<String> endNodes;
    private Collection<String> frontierNodes;
    private HashMap<String, NodeData> nodes = new HashMap<>();
    private SimpleDirectedGraph<NodeData, TransitionWrapper> graph = new SimpleDirectedGraph<>(TransitionWrapper.class);
    private String currentNodeId;


    private static class TransitionWrapper {

        private NodeData source;
        private NodeData dest;
        private TransitionData.Type linkType;

        public NodeData getSource() {
            return source;
        }

        public TransitionWrapper setSource(NodeData source) {
            this.source = source;
            return this;
        }

        public NodeData getDest() {
            return dest;
        }

        public TransitionWrapper setDest(NodeData dest) {
            this.dest = dest;
            return this;
        }

        public TransitionData.Type getLinkType() {
            return linkType;
        }

        public TransitionWrapper setLinkType(TransitionData.Type linkType) {
            this.linkType = linkType;
            return this;
        }
    }

    public void setScenarioTitle(String title) {
        this.title = title;
    }

    public void visit(NodeData nodeData) {
        nodes.put(nodeData.getId(), nodeData);
        graph.addVertex(nodeData);
    }

    public void visit(TransitionData transitionData) {
        final NodeData source = nodes.get(transitionData.getSourceNodeId());
        final NodeData dest = nodes.get(transitionData.getDestNodeId());
        graph.addEdge(source, dest, new TransitionWrapper().setSource(source).setDest(dest).setLinkType(transitionData.getType())); //FIXME
    }

    public void visitStartNode(String nodeDataId) {
        this.startNodeId = nodeDataId;
    }

    public void visitEndNodes(Collection<String> endNodes) {
        this.endNodes = endNodes;
    }

    @Override
    public void visitFrontierNodes(Collection<String> frontierNodes) {
        this.frontierNodes = frontierNodes;
    }

    public void setCurrentNode(String nodeDataId) {
        this.currentNodeId = nodeDataId;
    }

    public String build() {
        GraphMLExporter<NodeData, TransitionWrapper> exporter = new GraphMLExporter<>();

        exporter.setExportEdgeWeights(false);
        exporter.registerAttribute(GraphAttributes.SCENARIO_TITLE, GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING); // FIXME

        // нода
        exporter.registerAttribute(GraphAttributes.ID, GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute(GraphAttributes.ELEMENT_CLASS, GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute(GraphAttributes.TYPE, GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute(GraphAttributes.IS_START_NODE, GraphMLExporter.AttributeCategory.NODE, AttributeType.BOOLEAN);
        exporter.registerAttribute(GraphAttributes.IS_END_NODE, GraphMLExporter.AttributeCategory.NODE, AttributeType.BOOLEAN);
        exporter.registerAttribute(GraphAttributes.IS_FRONTIER_NODE, GraphMLExporter.AttributeCategory.NODE, AttributeType.BOOLEAN);
        exporter.registerAttribute(GraphAttributes.IS_CURRENT_NODE, GraphMLExporter.AttributeCategory.NODE, AttributeType.BOOLEAN);

        // дуга
        exporter.registerAttribute(GraphAttributes.LINK_TYPE, GraphMLExporter.AttributeCategory.EDGE, AttributeType.STRING);

        exporter.setVertexAttributeProvider(new ComponentAttributeProvider<NodeData>() {
            @Override
            public Map<String, Attribute> getComponentAttributes(NodeData nodeData) {
                HashMap<String, Attribute> result = new HashMap<>();
                result.put(GraphAttributes.ID, DefaultAttribute.createAttribute(nodeData.getId()));
                result.put(GraphAttributes.ELEMENT_CLASS, DefaultAttribute.createAttribute(nodeData.getElementClass()));
                result.put(GraphAttributes.TYPE, DefaultAttribute.createAttribute(nodeData.getType().name()));
                result.put(GraphAttributes.IS_START_NODE, DefaultAttribute.createAttribute(startNodeId.equals(nodeData.getId())));
                result.put(GraphAttributes.IS_END_NODE, DefaultAttribute.createAttribute(endNodes.contains(nodeData.getId())));
                result.put(GraphAttributes.IS_FRONTIER_NODE, DefaultAttribute.createAttribute(frontierNodes.contains(nodeData.getId())));
                result.put(GraphAttributes.IS_CURRENT_NODE, DefaultAttribute.createAttribute(currentNodeId.equals(nodeData.getId())));

                result.put(GraphAttributes.SCENARIO_TITLE, DefaultAttribute.createAttribute(ScenarioDataBuilderImpl.this.title));

                return result;
            }
        });
        exporter.setEdgeAttributeProvider(new ComponentAttributeProvider<TransitionWrapper>() {
            @Override
            public Map<String, Attribute> getComponentAttributes(TransitionWrapper transitionWrapper) {
                HashMap<String, Attribute> result = new HashMap<>();
                result.put(GraphAttributes.LINK_TYPE, DefaultAttribute.createAttribute(transitionWrapper.getLinkType().name()));
                return result;
            }
        });


        StringWriter writer = new StringWriter();
        try {
            exporter.exportGraph(graph, writer);
        } catch (ExportException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

}