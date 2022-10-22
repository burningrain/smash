# smash

Smash is a simple business process engine. 
It supports the next types of elements: 
* a state 
* a predicate
* a subprocess (but it will be merged with the main process at the FSM creation stage)
```
|__ smash-api             the engine and api for extending by implementing the logic
|__ smash-graphml-impl    the example of the logic implementation
```

### An Example Of Business Process Description

![the process description](https://github.com/burningrain/smash/blob/master/smash-graphml-impl/src/main/resources/example.png?raw=true)

```java
        // states
        StateData a = new StateData("a", AState.class);
        StateData b = new StateData("b", BState.class);
        StateData c = new StateData("c", CState.class);
        StateData d = new StateData("d", DState.class);
        StateData e = new StateData("e", EState.class);
        StateData before_d = new StateData("before_d", DState.class);

        // predicates
        PredicateData predicate1 = new PredicateData("predicate1", Predicate1.class);
        PredicateData predicate2 = new PredicateData("predicate2", Predicate2.class);
        PredicateData predicate3 = new PredicateData("predicate3", Predicate3.class);

        // process
        ScenarioData scenario =
        begin(SCENARIO_TITLE)
            .state(a)
            .predicate(predicate1)
            .no(
                begin(" -the predicate 1- NO")
                    .state(b)
                    .predicate(predicate2)
                        .no(begin(" -the predicate 2- NO")
                                .state(c)
                                .state(b)
                            .end()
                        )
                        .yes(begin(" -the predicate 2- YES")
                                .predicate(predicate3)
                                    .no(begin(" - the predicate 3- NO")
                                            .state(d)
                                        .end()
                                    )
                                    .yes(begin(" -the predicate 3- YES")
                                            .state(e)
                                            .exit()
                                        .end()
                                    )
                            .end()
                        )
                .end()
            )
            .yes(
                begin(" -the predicate 1- YES")
                    .state(before_d)
                    .state(d)
                    .exit()
                .end()
            )
        .end();
```
You could see how it works at the [test](https://github.com/burningrain/smash/blob/master/smash-api/src/test/java/com/github/burningrain/smash/api/scenario/builder/SmashBuilderTest.java)

### How Use

* add the library to your project:
```
    <dependency>
        <groupId>com.github.burningrain</groupId>
        <artifactId>smash</artifactId>
        <version>${version}</version>
    </dependency>
```

* implement next interfaces:
    - **com.github.burningrain.smash.api.ProcessContext** this is a business process context
    - **com.github.burningrain.smash.api.SmashElementContext** this is a factory for retrieving process elements
    - **com.github.burningrain.smash.api.scenario.data.ScenarioDataBuilder, 
       com.github.burningrain.smash.api.scenario.data.StringScenarioConverter** this is converters from/to a string view 
    - **com.github.burningrain.smash.api.entity.ProcessDao** this is a data layer for saving/loading of process data
    - **com.github.burningrain.smash.api.entity.ScenarioEntity** this is the process data

* create a business process description. It is called **ScenarioData** (you can see an example above)
* create FSM set  **Smash**:
```java
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
```

* Use Smash (Smash is the main class of this library):
```java
ProcessContextMock processContextMock = new ProcessContextMock(1L);
ViewMock viewMock = smash.processInput(processContextMock);// AState#getView
```

What is the execution result (ViewMock)? What does it need for?
Initially, this project was developed to handle inputs from views and describe transitions between them.