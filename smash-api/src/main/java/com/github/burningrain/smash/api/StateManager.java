package com.github.burningrain.smash.api;

import com.github.burningrain.smash.api.entity.ProcessDao;
import com.github.burningrain.smash.api.scenario.Scenario;
import com.github.burningrain.smash.api.scenario.ScenarioConverter;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.nodes.StateNode;
import com.github.burningrain.smash.api.entity.ScenarioEntity;
import com.github.burningrain.smash.api.elements.SmashElement;
import com.github.burningrain.smash.api.elements.SmashState;
import com.github.burningrain.smash.api.scenario.nodes.Node;

/**
 * @author burningrain on 10.10.2017.
 */
class StateManager<PC extends ProcessContext, VIEW> extends SmashState<PC, VIEW> {

    private InternalContext smashContext;

    private ScenarioManager scenarioManager;
    private ProcessDao processDao;
    private SmashElementContext smashElementContext;
    private ScenarioConverter scenarioConverter;

    public StateManager(InternalContext smashContext) {
        this.smashContext = smashContext;

        scenarioManager = smashContext.getSingleton(ScenarioManager.class);
        processDao = smashContext.getSingleton(ProcessDao.class);
        smashElementContext = smashContext.getSingleton(SmashElementContext.class);
        scenarioConverter = smashContext.getSingleton(ScenarioConverter.class);
    }

    public void processInput(ProcessContext processContext) {
        Scenario scenario;
        ScenarioEntity processEntity = processDao.getScenarioById(processContext.getProcessId());
        // если нет сценария, то выставляем сценарий по умолчанию
        if(processEntity == null) {
            processEntity = smashContext.getPrototype(ScenarioEntity.class);
            processEntity.setProcessId(processContext.getProcessId());
            scenario = scenarioConverter.toScenario(scenarioManager.generateDefaultScenario(), processEntity.getCurrentStateId());
        } else {
            scenario = getScenario(processEntity);
        }

        // обрабатываем текущее состояние
        StateNode node = (StateNode) scenario.getCurrentNode();
        final SmashElement smashElement = smashElementContext.getScenarioElement(node.getElementClass());
        smashElement.process(processContext);

        // если нода конечная, дальше не идем
        if(scenario.getScenarioData().getEndNode().equals(node.getId())) {
            return;
        }

        // переходим к следующему состоянию, пробираясь через if-ы
        SmashElement scenarioElement = smashElement;
        Node nextNode;
        do {
            nextNode = scenario.next(scenarioElement, processContext);
            scenarioElement = smashElementContext.getScenarioElement(nextNode.getElementClass());
        } while(!NodeData.Type.STATE.equals(nextNode.getType()));

        // сохраняем данные по графу и состояние
        processEntity.setScenarioTitle(scenario.getScenarioData().getTitle());
        processEntity.setCurrentStateId(nextNode.getId());
        if(Smash.isDebugMode()) {
            processEntity.setScenario(scenarioConverter.toString(scenario.getScenarioData(), scenario.getCurrentNode().getId()));
        }
        processDao.createOrUpdateScenario(processEntity);
    }

    public VIEW getView(ProcessContext processContext) {
        final ScenarioEntity processEntity = processDao.getScenarioById(processContext.getProcessId());
        Scenario scenario = scenarioConverter.toScenario(
                scenarioManager.getScenario(processEntity.getScenarioTitle()), processEntity.getCurrentStateId());
        final SmashState scenarioNode =
                (SmashState) smashElementContext.getScenarioElement(scenario.getCurrentNode().getElementClass());
        return (VIEW) scenarioNode.getView(processContext);
    }

    private Scenario getScenario(ScenarioEntity processEntity) {
        return scenarioConverter.toScenario(
                scenarioManager.getScenario(processEntity.getScenarioTitle()), processEntity.getCurrentStateId());
    }

}