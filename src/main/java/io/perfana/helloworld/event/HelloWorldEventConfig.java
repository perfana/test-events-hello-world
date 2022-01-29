/*
 * Copyright (C) 2020-2022 Peter Paul Bakker - Perfana
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.perfana.helloworld.event;

import io.perfana.eventscheduler.api.config.EventConfig;
import io.perfana.eventscheduler.api.config.TestContext;
import net.jcip.annotations.NotThreadSafe;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NotThreadSafe
public class HelloWorldEventConfig extends EventConfig {

    private String myRestService;
    private int helloInitialSleepSeconds = 4;
    private String helloMessage = "Default Hello Message";
    private String myCredentials;
    private String myEventTags;
    private String actuatorPropPrefix = "actuator";
    private String actuatorBaseUrl;
    private String actuatorEnvProperties;

    public void setMyRestService(String myRestService) {
        this.myRestService = myRestService;
    }

    public void setHelloInitialSleepSeconds(int helloInitialSleepSeconds) {
        this.helloInitialSleepSeconds = helloInitialSleepSeconds;
    }

    public void setHelloMessage(String helloMessage) {
        this.helloMessage = helloMessage;
    }

    public void setMyCredentials(String myCredentials) {
        this.myCredentials = myCredentials;
    }

    public void setMyEventTags(String myEventTags) {
        this.myEventTags = myEventTags;
    }

    private List<String> createEnvProps() {
        return actuatorEnvProperties == null ? Collections.emptyList() : Arrays.asList(actuatorEnvProperties.split(","));
    }

    @Override
    public HelloWorldEventContext toContext() {
        List<String> envProps = createEnvProps();
        return new HelloWorldEventContext(super.toContext(), myRestService, Duration.ofSeconds(helloInitialSleepSeconds), helloMessage, myCredentials, myEventTags, actuatorPropPrefix, actuatorBaseUrl, envProps);
    }

    @Override
    public HelloWorldEventContext toContext(TestContext override) {
        List<String> envProps = createEnvProps();
        return new HelloWorldEventContext(super.toContext(override), myRestService, Duration.ofSeconds(helloInitialSleepSeconds), helloMessage, myCredentials, myEventTags, actuatorPropPrefix, actuatorBaseUrl, envProps);
    }

    @Override
    public String toString() {
        return "HelloWorldEventConfig{" +
            "myRestService='" + myRestService + '\'' +
            ", helloInitialSleepSeconds=" + helloInitialSleepSeconds +
            ", helloMessage='" + helloMessage + '\'' +
            ", myCredentials='" + myCredentials + '\'' +
            ", myEventTags='" + myEventTags + '\'' +
            ", actuatorPropPrefix='" + actuatorPropPrefix + '\'' +
            ", actuatorBaseUrl='" + actuatorBaseUrl + '\'' +
            ", actuatorEnvProperties='" + actuatorEnvProperties + '\'' +
            "} " + super.toString();
    }

    public String getActuatorBaseUrl() {
        return actuatorBaseUrl;
    }

    public void setActuatorBaseUrl(String actuatorBaseUrl) {
        this.actuatorBaseUrl = actuatorBaseUrl;
    }

    public String getActuatorEnvProperties() {
        return actuatorEnvProperties;
    }

    public void setActuatorEnvProperties(String actuatorEnvProperties) {
        this.actuatorEnvProperties = actuatorEnvProperties;
    }

    public String getActuatorPropPrefix() {
        return actuatorPropPrefix;
    }

    public void setActuatorPropPrefix(String actuatorPropPrefix) {
        this.actuatorPropPrefix = actuatorPropPrefix;
    }
}
