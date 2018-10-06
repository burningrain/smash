package com.github.burningrain.smash.api.scenario.builder;

import com.github.burningrain.smash.api.Smash;
import com.github.burningrain.smash.api.SmashData;
import com.github.burningrain.smash.api.elements.SmashPredicate;
import com.github.burningrain.smash.api.elements.SmashState;
import com.github.burningrain.smash.api.mocks.*;
import com.github.burningrain.smash.api.scenario.data.ScenarioData;
import com.github.burningrain.smash.api.scenario.data.nodes.PredicateData;
import com.github.burningrain.smash.api.scenario.data.nodes.StateData;
import org.junit.Before;
import org.junit.Test;

import static com.github.burningrain.smash.api.scenario.builder.SmashBuilder.begin;

/**
 * @autor burningrain on 05.09.2018.
 */
public class SmashBuilderTest {

    private static final String SCENARIO_TITLE = "СЦЕНАРИЙ";
    private Smash<ProcessContextMock, ViewMock> smash;
    private ProcessDaoMock processDaoMock = new ProcessDaoMock();

    private static class AState extends SmashState<ProcessContextMock,ViewMock> {

        @Override
        public void processInput(ProcessContextMock input) {
            System.out.println(this.getClass() + " processInput");
        }

        @Override
        public ViewMock getView(ProcessContextMock processContext) {
            System.out.println(this.getClass() + " getView");
            return null;
        }
    }

    private static class BState extends SmashState<ProcessContextMock,ViewMock> {

        @Override
        public void processInput(ProcessContextMock input) {
            System.out.println(this.getClass() + " processInput");
        }

        @Override
        public ViewMock getView(ProcessContextMock processContext) {
            System.out.println(this.getClass() + " getView");
            return null;
        }
    }

    private static class CState extends SmashState<ProcessContextMock,ViewMock> {

        @Override
        public void processInput(ProcessContextMock input) {
            System.out.println(this.getClass() + " processInput");
        }

        @Override
        public ViewMock getView(ProcessContextMock processContext) {
            System.out.println(this.getClass() + " getView");
            return null;
        }
    }

    private static class DState extends SmashState<ProcessContextMock,ViewMock> {

        @Override
        public void processInput(ProcessContextMock input) {
            System.out.println(this.getClass() + " processInput");
        }

        @Override
        public ViewMock getView(ProcessContextMock processContext) {
            System.out.println(this.getClass() + " getView");
            return null;
        }
    }

    private static class EState extends SmashState<ProcessContextMock,ViewMock> {

        @Override
        public void processInput(ProcessContextMock input) {
            System.out.println(this.getClass() + " processInput");
        }

        @Override
        public ViewMock getView(ProcessContextMock processContext) {
            System.out.println(this.getClass() + " getView");
            return null;
        }
    }

    private static class Predicate1 extends SmashPredicate<ProcessContextMock> {

        @Override
        public Boolean process(ProcessContextMock input) {
            System.out.println(this.getClass() + " process");
            Boolean isPredicate1 = input.getByKey("isPredicate1");
            return isPredicate1 != null && isPredicate1;
        }
    }

    private static class Predicate2 extends SmashPredicate<ProcessContextMock> {

        @Override
        public Boolean process(ProcessContextMock input) {
            System.out.println(this.getClass() + " process");
            Boolean isPredicate2 = input.getByKey("isPredicate2");
            return isPredicate2 != null && isPredicate2;
        }
    }

    private static class Predicate3 extends SmashPredicate<ProcessContextMock> {

        @Override
        public Boolean process(ProcessContextMock input) {
            System.out.println(this.getClass() + " process");
            Boolean isPredicate2 = input.getByKey("isPredicate3");
            return isPredicate2 != null && isPredicate2;
        }
    }

    @Before
    public void beforeTest() {
        StateData a = new StateData("a", AState.class);
        StateData b = new StateData("b", BState.class);
        StateData c = new StateData("c", CState.class);
        StateData d = new StateData("d", DState.class);
        StateData e = new StateData("e", EState.class);
        StateData before_d = new StateData("before_d", DState.class);

        PredicateData predicate1 = new PredicateData("predicate1", Predicate1.class);
        PredicateData predicate2 = new PredicateData("predicate2", Predicate2.class);
        PredicateData predicate3 = new PredicateData("predicate3", Predicate3.class);
//        NodeData predicate3 = NodeData.of(NodeData.Type.PREDICATE, "predicate1", Predicate1.class);

        ScenarioData scenario =
        begin(SCENARIO_TITLE)
            .state(a)
            .predicate(predicate1)
            .no(
                begin(" -предикат 1- нет")
                    .state(b)
                    .predicate(predicate2)
                        .no(begin(" -предикат 2- нет")
                                .state(c)
                                .state(b)
                            .end()
                        )
                        .yes(begin(" -предикат 2- да")
                                .predicate(predicate3)
                                    .no(begin(" -предикат 3- нет")
                                            .state(d)
                                        .end()
                                    )
                                    .yes(begin(" -предикат 3- да")
                                            .state(e)
                                            .exit()
                                        .end()
                                    )
                            .end()
                        )
                .end()
            )
            .yes(
                begin(" -предикат 1- да")
                    .state(before_d)
                    .state(d)
                    .exit()
                .end()
            )
        .end();

        Smash<ProcessContextMock, ViewMock> smash = Smash.builder()
                .setDebugMode(false)
                .setSmashData(
                        SmashData.builder()
                        .addScenario(scenario)
                        .setDefaultScenario(SCENARIO_TITLE)
                        .build()
                )
                .setSmashElementContext(SmashElementContextMock.builder()
                        .add(new AState())
                        .add(new BState())
                        .add(new CState())
                        .add(new DState())
                        .add(new EState())
                        .add(new Predicate1())
                        .add(new Predicate2())
                        .add(new Predicate3())
                        .build()
                )
                .setProcessDao(processDaoMock)
                .setScenarioEntity(ScenarioEntityMock.class)
                .setScenarioToStringConverterClass(ScenarioDataBuilderMock.class)
                .setStringToScenarioConverter(new StringScenarioConverterMock())
                .build();

        this.smash = smash;
    }

    @Test
    public void testJumpToTheFuture() {
        ProcessContextMock processContextMock = new ProcessContextMock(1L);
        smash.processInput(processContextMock); // AState#getView

        processContextMock.put("isPredicate1", true);
        smash.processInput(processContextMock); // AState processInput Predicate1#process DState#getView
        smash.processInput(processContextMock);
    }


    @Test
    public void testScenarioWithLoopsInBranch_BCB() {
        ProcessContextMock processContextMock = new ProcessContextMock(1L);
        smash.processInput(processContextMock); // AState#getView
        smash.processInput(processContextMock); // AState#processInput Predicate1 process BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        smash.processInput(processContextMock); // CState#processInput BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        smash.processInput(processContextMock); // return to b - CState#processInput BState#getView

        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        smash.processInput(processContextMock); // CState#processInput BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        smash.processInput(processContextMock); // return to b - CState#processInput BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView

        processContextMock.put("isPredicate2", true); // BState#processInput
        smash.processInput(processContextMock);       // CState#processInput BState#getView
        smash.processInput(processContextMock);       // BState#processInput Predicate2#process DState#getView
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowExceptionWhenProcessEndScenario() {
        ProcessContextMock processContextMock = new ProcessContextMock(1L);
        smash.processInput(processContextMock); // AState#getView
        smash.processInput(processContextMock); // AState#processInput Predicate1 process BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        smash.processInput(processContextMock); // CState#processInput BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        processContextMock.put("isPredicate2", true); // BState#processInput
        smash.processInput(processContextMock);       // CState#processInput BState#getView
        smash.processInput(processContextMock);       // BState#processInput Predicate2#process DState#getView
        smash.processInput(processContextMock);       // IllegalStateException
    }

    @Test
    public void testEndBranch() {
        ProcessContextMock processContextMock = new ProcessContextMock(1L);
        smash.processInput(processContextMock); // AState#getView
        smash.processInput(processContextMock); // AState#processInput Predicate1 process BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        smash.processInput(processContextMock); // CState#processInput BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        smash.processInput(processContextMock); // return to b - CState#processInput BState#getView

        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        smash.processInput(processContextMock); // CState#processInput BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView
        smash.processInput(processContextMock); // return to b - CState#processInput BState#getView
        smash.processInput(processContextMock); // BState#processInput Predicate2#process CState#getView

        processContextMock.put("isPredicate2", true); // BState#processInput
        processContextMock.put("isPredicate3", true); // BState#processInput
        smash.processInput(processContextMock);       // CState#processInput BState#getView
        smash.processInput(processContextMock);       // BState#processInput Predicate2#process DState#getView
    }


}