package io.perfana.actuator;

/*-
 * #%L
 * test-events-hello-world
 * %%
 * Copyright (C) 2019 - 2022 Perfana
 * %%
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
 * #L%
 */

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActuatorClientTest {

    @Test
    @Disabled("only run with actual actuator running on http://localhost:8080/actuator")
    public void testQuery() {
        ActuatorClient actuatorClient = new ActuatorClient("http://localhost:8080/actuator");

        List<Variable> variables = actuatorClient.queryActuator(List.of("java.runtime.version", "JDK_JAVA_OPTIONS","doesNotExist"));

        assertEquals(2, variables.size());
        assertEquals("systemProperties:java.runtime.version", variables.get(0).getName());
        assertEquals("17.0.1+12-LTS", variables.get(0).getValue());
        assertEquals("systemEnvironment:JDK_JAVA_OPTIONS", variables.get(1).getName());
        assertEquals("-javaagent:/pyroscope.jar", variables.get(1).getValue());

    }

}
