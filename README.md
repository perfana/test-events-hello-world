# test-events-hello-world

This project shows an example implementation of the `Event` interface
and the `EventGenerator` interface.

The `HelloWorldEvent` shows when events are called in `System.out`.
It also prints out some system information at the start.

The `HelloWorldEventGenerator` shows how to create a custom list of events from
a generator class with properties.

This event is an `isReadyForStartParticipant`. Only when this event reports
back a `Go!` message, the event-scheduler is allowed to start.
This plugin will sent a `Go!` message after waiting `helloInitialSleepSeconds` seconds.

## to try it out

If you just want to experiment with the `event-scheduler-maven-plugin` and
sub-plugins `test-events-hello-world` and `perfana-java-client`,
just add the `test-events-hello-world` and `perfana-java-client` plugin to the dependencies
of the `event-scheduler-maven-plugin`.

For instance add the following:

```xml
<plugins>
    <plugin>
        <groupId>io.perfana</groupId>
        <artifactId>event-scheduler-maven-plugin</artifactId>
        <configuration>
            <eventSchedulerConfig>
                <debugEnabled>true</debugEnabled>
                <schedulerEnabled>true</schedulerEnabled>
                <failOnError>true</failOnError>
                <continueOnEventCheckFailure>true</continueOnEventCheckFailure>
                <eventScheduleScript>
                    PT5S|restart|{ server:'myserver' replicas:2 tags: [ 'first', 'second' ] }
                    PT10S|scale-down
                    PT30S|heapdump|server=myserver.example.com;port=1567
                    PT1M|scale-up|{ replicas:2 }
                </eventScheduleScript>
                <testConfig>
                    <systemUnderTest>${systemUnderTest}</systemUnderTest>
                    <version>${version}</version>
                    <workload>${workload}</workload>
                    <testEnvironment>${testEnvironment}</testEnvironment>
                    <testRunId>${testRunId}</testRunId>
                    <buildResultsUrl>${buildResultsUrl}</buildResultsUrl>
                    <rampupTimeInSeconds>${rampupTimeInSeconds}</rampupTimeInSeconds>
                    <constantLoadTimeInSeconds>${constantLoadTimeInSeconds}</constantLoadTimeInSeconds>
                    <annotations>${annotations}</annotations>
                    <tags>${tags}</tags>
                </testConfig>
                <eventConfigs>
                    <eventConfig implementation="io.perfana.helloworld.event.HelloWorldEventConfig">
                        <name>HelloWorldEvent1</name>
                        <helloInitialSleepSeconds>40</helloInitialSleepSeconds>
                        <myRestService>https://my-rest-api</myRestService>
                        <myCredentials>${ENV.SECRET}</myCredentials>
                    </eventConfig>
                    <eventConfig implementation="io.perfana.event.PerfanaEventConfig">
                        <name>PerfanaEvent1</name>
                        <perfanaUrl>http://localhost:8888</perfanaUrl>
                        <assertResultsEnabled>false</assertResultsEnabled>
                        <variables>
                            <var1>my_value</var1>
                        </variables>
                    </eventConfig>
                </eventConfigs>
            </eventSchedulerConfig>
        </configuration>
        <dependencies>
            <dependency>
                <groupId>io.perfana</groupId>
                <artifactId>test-events-hello-world</artifactId>
                <version>${test-events-hello-world.version}</version>
            </dependency>
            <dependency>
                <groupId>io.perfana</groupId>
                <artifactId>perfana-java-client</artifactId>
                <version>${perfana-java-client.version}</version>
            </dependency>
        </dependencies>
    </plugin>
</plugins>
```

See also: 
* https://github.com/perfana/event-scheduler-maven-plugin
* https://github.com/perfana/event-scheduler
* https://github.com/perfana/perfana-java-client

## create your own plugin

Add a dependency to the `event-scheduler` jar, just for compile so you can use the api interfaces
and classes.

Example:

```xml
<dependencies>
   <dependency>
       <groupId>io.perfana</groupId>
       <artifactId>event-scheduler</artifactId>
       <version>3.0.2</version>
       <scope>compile</scope>
   </dependency>
</dependencies>
```

Then you can create your own implementations of the `Event` and the `EventFactory` interface.
The same goes for the `EventGenerator` and the `EventGeneratorFactory` interface.

For convenience, you can use the `EventAdapter` abstract class 
to implementing `Event` interface. Only implement the method you want to override.

Create an `*EventConfig` calls for the configuration with only setters.
Create an immutable `*EventContext` class with only getter.
In the `EventConfig` class override the 2 `toContext` methods.

## actuator client

There is a little bonus inside version 1.2.0+ of this test-events plugin: an
actuator client that calls an actuator/env endpoint and turns properties
into variables in an event-scheduler message. The variables in a message
are picked up by the perfana-java-client and are sent to Perfana for the 
current test run.

* `actuatorPropPrefix` prefix for the properties to send as variables
* `actuatorBaseUrl` the base url for the actuator endpoint, `/env` will be added
* `actuatorEnvProperties` comma seperated list of actuator env properties to turn into variables

Tip: check your http://application/actuator/env to see what is available.

Note: env needs to be enabled in actuator. Be careful though to not expose this endpoint on the internet!

```xml
<eventConfig implementation="io.perfana.helloworld.event.HelloWorldEventConfig">
    <name>ActuatorEvent</name>
    <helloInitialSleepSeconds>0</helloInitialSleepSeconds>
    <actuatorPropPrefix>my-app</actuatorPropPrefix>
    <actuatorBaseUrl>http://my-app:8080/actuator</actuatorBaseUrl>
    <actuatorEnvProperties>java.runtime.version,JDK_JAVA_OPTIONS</actuatorEnvProperties>
</eventConfig>

```

## add services files                               

The magic happens when you add the appropriate file
in `META-INF/services`. See the sample in this project.

You need to specify the fully qualified name of your implementation
(e.g. `io.perfana.helloworld.event.HelloWorldEventFactory`) 
in a file called `io.perfana.eventscheduler.api.EventFactory`. 

And for the event schedule generators use
(e.g. `io.perfana.helloworld.event.HelloWorldEventGeneratorFactory`) 
a file called `io.perfana.eventscheduler.api.EventGeneratorFactory`.

Note that both can contain multiple lines with different implementations.
 
