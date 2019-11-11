package nl.stokpop.helloworld.event;

/*-
 * test-events-hello-world
 *
 * Copyright (C) 2019 Stokpop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

import nl.stokpop.eventscheduler.api.CustomEvent;
import nl.stokpop.eventscheduler.api.EventGenerator;
import nl.stokpop.eventscheduler.api.EventGeneratorProperties;
import nl.stokpop.eventscheduler.api.EventLogger;
import nl.stokpop.eventscheduler.api.TestContext;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

public class StokpopEventGenerator implements EventGenerator {

    private static final String INPUT_FILE_TAG = "input-file";
    private final EventLogger logger;
    private final EventGeneratorProperties properties;
    private final TestContext testContext;

    StokpopEventGenerator(TestContext testContext, EventGeneratorProperties properties, EventLogger logger) {
        this.testContext = testContext;
        this.properties = properties;
        this.logger = logger;
    }

    @Override
    public List<CustomEvent> generate() {
        String slowbackendFilePath = properties.getProperty(INPUT_FILE_TAG);

        if (slowbackendFilePath == null) {
            throw new RuntimeException("unable to find setting for: " + INPUT_FILE_TAG);
        }

        logger.info("StokpopEventGenerator: using input-file path: " + slowbackendFilePath);

        List<CustomEvent> events = new ArrayList<>();
        events.add(new CustomEvent(
                Duration.of(1, SECONDS),
                "hello-world",
                "phase 1",
                "{}"));
        return events;
    }
}
