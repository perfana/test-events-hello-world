package nl.stokpop.helloworld.event;

/*-
 * #%L
 * test-events-hello-world
 * %%
 * Copyright (C) 2019 - 2021 Stokpop
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

import net.jcip.annotations.Immutable;
import nl.stokpop.eventscheduler.api.config.EventContext;

import java.time.Duration;
import java.util.List;

@Immutable
public class StokpopHelloEventContext extends EventContext {

    private final String myRestService;
    private final Duration helloInitialSleep;
    private final String helloMessage;
    private final String myCredentials;
    private final String myEventTags;
    private final String actuatorBaseUrl;
    private final List<String> actuatorEnvProps;

    protected StokpopHelloEventContext(EventContext context, String myRestService, Duration helloInitialSleep, String helloMessage, String myCredentials, String myEventTags, String actuatorBaseUrl, List<String> actuatorEnvProps) {
        super(context, StokpopHelloEventFactory.class.getName(), true);
        this.myRestService = myRestService;
        this.helloInitialSleep = helloInitialSleep;
        this.helloMessage = helloMessage;
        this.myCredentials = myCredentials;
        this.myEventTags = myEventTags;
        this.actuatorBaseUrl = actuatorBaseUrl;
        this.actuatorEnvProps = actuatorEnvProps;
    }

    public String getMyRestService() {
        return myRestService;
    }

    public Duration getHelloInitialSleep() {
        return helloInitialSleep;
    }

    public String getHelloMessage() {
        return helloMessage;
    }

    public String getMyCredentials() {
        return myCredentials;
    }

    public String getMyEventTags() {
        return myEventTags;
    }

    @Override
    public String toString() {
        return "StokpopHelloEventConfig{" +
            "myRestService='" + myRestService + '\'' +
            ", helloInitialSleep=" + helloInitialSleep +
            ", helloMessage='" + helloMessage + '\'' +
            ", myCredentials='" + myCredentials + '\'' +
            ", myEventTags='" + myEventTags + '\'' +
            ", actuatorBaseUrl='" + actuatorBaseUrl + '\'' +
            ", actuatorEnvProperties='" + actuatorEnvProps + '\'' +
            "} " + super.toString();
    }

    public String getActuatorBaseUrl() {
        return actuatorBaseUrl;
    }

    public List<String> getActuatorEnvProperties() {
        return actuatorEnvProps;
    }
}
