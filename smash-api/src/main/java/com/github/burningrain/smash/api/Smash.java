package com.github.burningrain.smash.api;

import com.github.burningrain.smash.api.entity.ProcessDao;
import com.github.burningrain.smash.api.entity.ScenarioEntity;
import com.github.burningrain.smash.api.scenario.Scenario;
import com.github.burningrain.smash.api.scenario.ScenarioConverter;
import com.github.burningrain.smash.api.scenario.data.ScenarioData;
import com.github.burningrain.smash.api.scenario.data.ScenarioDataBuilder;
import com.github.burningrain.smash.api.scenario.data.StringScenarioConverter;

/**
 * @author burningrain on 18.05.2018.
 */
public class Smash<PC extends ProcessContext, VIEW> {

    private static boolean debugMode;

    private InternalContext smashContext = new InternalContext();

    private StateManager<PC, VIEW> stateManager;
    private ProcessDao processDao;
    private ScenarioConverter scenarioConverter;
    private ScenarioManager scenarioManager;

    private Smash(Builder builder) {
        debugMode = builder.debugMode;

        // данные
        smashContext.addPrototype(ScenarioEntity.class, builder.scenarioEntity);
        smashContext.addSingleton(ProcessDao.class, builder.processDao);

        // конвертер
        smashContext.addSingleton(StringScenarioConverter.class, builder.stringScenarioConverter);
        smashContext.addPrototype(ScenarioDataBuilder.class, builder.visitorBuilderClass);
        smashContext.addSingleton(ScenarioConverter.class, new ScenarioConverter(smashContext));

        // объекты работы с графом состояний
        smashContext.addSingleton(ScenarioManager.class, new ScenarioManager(smashContext, builder.smashData));
        smashContext.addSingleton(SmashElementContext.class, builder.smashElementContext);
        smashContext.addSingleton(StateManager.class, new StateManager<PC, VIEW>(smashContext));

        stateManager = smashContext.getSingleton(StateManager.class);
        processDao = smashContext.getSingleton(ProcessDao.class);
        scenarioConverter = smashContext.getSingleton(ScenarioConverter.class);
        scenarioManager = smashContext.getSingleton(ScenarioManager.class);
    }

    public static Builder builder() {
        return new Builder();
    }

    public VIEW processInput(PC processContext) {
        ScenarioEntity processEntity = processDao.getScenarioById(processContext.getProcessId());
        // если нет сценария, то выставляем сценарий по умолчанию
        if(processEntity == null) {
            processEntity = createProcessWithDefaultScenario(processContext, scenarioManager.getDefaultScenario());
            processEntity.setProcessId(processContext.getProcessId());
            processDao.createOrUpdateScenario(processEntity);
        }

        stateManager.processInput(processContext);
        return stateManager.getView(processContext);
    }

    public void setScenarioForProcess(ProcessContext context, String scenarioTitle) {
        processDao.createOrUpdateScenario(createProcessWithDefaultScenario(context, scenarioTitle));
    }

    private ScenarioEntity createProcessWithDefaultScenario(ProcessContext context, String scenarioTitle) {
        Scenario scenario = scenarioManager.getScenario(scenarioTitle);
        ScenarioData scenarioData = scenario.getScenarioData();

        final ScenarioEntity processEntity = smashContext.getPrototype(ScenarioEntity.class);
        processEntity.setProcessId(context.getProcessId());
        if(debugMode) {
            processEntity.setScenario(scenarioConverter.toString(scenarioData, scenarioData.getStartNodeId()));
        }
        processEntity.setCurrentStateId(scenarioData.getStartNodeId());
        processEntity.setScenarioTitle(scenarioTitle);
        processEntity.setStatus(ProcessStatus.NOT_STARTED);
        return processEntity;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static class Builder {

        private boolean debugMode;

        private ProcessDao processDao;
        private SmashData smashData;
        private SmashElementContext smashElementContext;
        private StringScenarioConverter stringScenarioConverter;
        private Class<? extends ScenarioDataBuilder> visitorBuilderClass;
        private Class<? extends ScenarioEntity> scenarioEntity;

        public Builder setProcessDao(ProcessDao processDao) {
            this.processDao = processDao;
            return this;
        }

        public Builder setStringToScenarioConverter(StringScenarioConverter stringScenarioConverter) {
            this.stringScenarioConverter = stringScenarioConverter;
            return this;
        }

        public Builder setScenarioToStringConverterClass(Class<? extends ScenarioDataBuilder> visitorBuilderClass) {
            this.visitorBuilderClass = visitorBuilderClass;
            return this;
        }

        public Builder setSmashData(SmashData smashData) {
            this.smashData = smashData;
            return this;
    }

        public Builder setSmashElementContext(SmashElementContext smashElementContext) {
            this.smashElementContext = smashElementContext;
            return this;
        }

        public Builder setScenarioEntity(Class<? extends ScenarioEntity> scenarioEntity) {
            this.scenarioEntity = scenarioEntity;
            return this;
        }

        public Builder setDebugMode(boolean debugMode) {
            this.debugMode = debugMode;
            return this;
        }

        public <PC extends ProcessContext, VIEW> Smash<PC, VIEW> build() {
            return new Smash<PC, VIEW>(this);
        }

    }


}