package com.github.burningrain.smash.api;

import com.github.burningrain.smash.api.elements.SmashElement;
import com.github.burningrain.smash.api.elements.SmashState;
import com.github.burningrain.smash.api.entity.ProcessDao;
import com.github.burningrain.smash.api.entity.ScenarioEntity;
import com.github.burningrain.smash.api.scenario.Scenario;
import com.github.burningrain.smash.api.scenario.ScenarioConverter;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.nodes.Node;
import com.github.burningrain.smash.api.scenario.nodes.StateNode;

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
        ScenarioEntity processEntity = processDao.getScenarioById(processContext.getProcessId());
        Scenario scenario = scenarioManager.getScenario(processEntity.getScenarioTitle());

        // обрабатываем текущее состояние
        StateNode node = (StateNode) scenario.getCurrentNode(processEntity.getCurrentStateId());

        // если стартовая нода, просто выставляем флаг, что процесс начат и ничего не делаем
        if (node.isStart() && ProcessStatus.NOT_STARTED == processEntity.getStatus()) {
            processEntity.setStatus(ProcessStatus.IN_PROGRESS);
            processDao.createOrUpdateScenario(processEntity);
            return;
        }

        // если нода конечная, и пришел запрос на обработку - кидаем исключение
        if (node.isEnd()) {
            if(ProcessStatus.FINISHED == processEntity.getStatus()) {
                throw new IllegalStateException("process with id=[" + processEntity.getProcessId() + "], scenarioTitle=[" + processEntity.getScenarioTitle() +"] was finished!");
            }

            // прерываем обработку процесса, чтобы сразу попасть в getView в последней ноде
            return;
        }

        final SmashElement smashElement = getElementFromContext(node.getElementClass());
        smashElement.process(processContext);

        // переходим к следующему состоянию, пробираясь через if-ы
        SmashElement scenarioElement = smashElement;
        Node nextNode;
        do {
            nextNode = scenario.next(processEntity.getCurrentStateId(), scenarioElement, processContext);
            processEntity.setCurrentStateId(nextNode.getId());
            scenarioElement = getElementFromContext(nextNode.getElementClass());
        } while(!NodeData.Type.STATE.equals(nextNode.getType()));

        // сохраняем данные по графу и состояние
        processEntity.setScenarioTitle(scenario.getScenarioData().getTitle());
        processEntity.setCurrentStateId(nextNode.getId());
        if(Smash.isDebugMode()) {
            processEntity.setScenario(scenarioConverter.toString(scenario.getScenarioData(), processEntity.getCurrentStateId()));
        }
        processDao.createOrUpdateScenario(processEntity);
    }

    public VIEW getView(ProcessContext processContext) {
        final ScenarioEntity processEntity = processDao.getScenarioById(processContext.getProcessId());
        Scenario scenario = scenarioManager.getScenario(processEntity.getScenarioTitle());
        Node currentNode = scenario.getCurrentNode(processEntity.getCurrentStateId());

        final SmashState scenarioNode =
                (SmashState) getElementFromContext(currentNode.getElementClass());
        VIEW view = (VIEW) scenarioNode.getView(processContext);

        // если нода конечная, то помечаем, что процесс окончен и дальше не идем
        if(currentNode.isEnd()) {
            processEntity.setStatus(ProcessStatus.FINISHED);
            processDao.createOrUpdateScenario(processEntity);
        }

        return view;
    }

    private <T extends SmashElement> T getElementFromContext(Class<T> clazz) {
        T scenarioElement = smashElementContext.getScenarioElement(clazz);
        if(scenarioElement == null) throw new IllegalStateException("Object of class [" + clazz.getName() + "] not found in " + SmashElementContext.class.getName());
        return scenarioElement;
    }

}