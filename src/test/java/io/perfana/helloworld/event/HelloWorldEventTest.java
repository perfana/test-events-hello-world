package io.perfana.helloworld.event;

/*-
 * #%L
 * test-events-hello-world
 * %%
 * Copyright (C) 2019-2022 Perfana
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

import io.perfana.eventscheduler.EventMessageBusSimple;
import io.perfana.eventscheduler.api.CustomEvent;
import io.perfana.eventscheduler.api.config.TestConfig;
import io.perfana.eventscheduler.api.message.EventMessageBus;
import io.perfana.eventscheduler.log.EventLoggerStdOut;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldEventTest {

    @Test
    public void beforeTest() {
        HelloWorldEventConfig helloWorldEventConfig = new HelloWorldEventConfig();
        helloWorldEventConfig.setEventFactory(HelloWorldEventFactory.class.getSimpleName());
        helloWorldEventConfig.setHelloMessage("hello");
        helloWorldEventConfig.setName("myEvent1");
        helloWorldEventConfig.setEnabled(true);
        helloWorldEventConfig.setTestConfig(TestConfig.builder().build());

        EventMessageBus messageBus = new EventMessageBusSimple();

        HelloWorldEvent event = new HelloWorldEvent(helloWorldEventConfig.toContext(), messageBus, EventLoggerStdOut.INSTANCE);
        event.beforeTest();
        event.keepAlive();
        event.customEvent(CustomEvent.createFromLine("PT3S|fail-over|debug=true;server=test"));
        event.customEvent(CustomEvent.createFromLine("PT1M|scale-down"));
        event.customEvent(CustomEvent.createFromLine("PT1H2M3S|scale-up|"));
        event.afterTest();

        // not much to assert really... just look at System.out and
        // check it does not blow with an Exception...

    }

    @Test
    public void parseSettingsZero() {
        Map<String, String> emptyMap = HelloWorldEvent.parseSettings("");
        assertEquals(0, emptyMap.size());
    }

    @Test
    public void parseSettingsOne() {
        Map<String, String> emptyMap = HelloWorldEvent.parseSettings("foo=bar");
        assertEquals(1, emptyMap.size());
        assertEquals("bar", emptyMap.get("foo"));
    }

    @Test
    public void parseSettingsTwo() {
        Map<String, String> emptyMap = HelloWorldEvent.parseSettings("foo=bar;name=perfana");
        assertEquals(2, emptyMap.size());
        assertEquals("bar", emptyMap.get("foo"));
        assertEquals("perfana", emptyMap.get("name"));
    }

    @Test
    public void parseSettingsNoValue() {
        Map<String, String> emptyMap = HelloWorldEvent.parseSettings("foo=bar;name");
        assertEquals(2,emptyMap.size());
        assertEquals("bar", emptyMap.get("foo"));
        assertEquals("", emptyMap.get("name"));
    }

    @Test
    public void parseSettingsNoEntry() {
        Map<String, String> emptyMap = HelloWorldEvent.parseSettings("foo=bar;");
        assertEquals(1,emptyMap.size());
        assertEquals("bar", emptyMap.get("foo"));
    }

}
