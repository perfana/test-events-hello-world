package nl.stokpop.actuator;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ActuatorClient {

    private final String baseUrl;

    private final Gson gson = new GsonBuilder().create();

    private HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .build();


    public ActuatorClient(String actuatorUrl) {
        this.baseUrl = actuatorUrl;
    }

    public List<Variable> queryActuator(List<String> envKeys) {

        String totalUrl = baseUrl + "/env";
        try {
            URI envUri = new URI(totalUrl);
            HttpRequest httpRequest = HttpRequest.newBuilder(envUri).timeout(Duration.ofSeconds(2)).build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Unexpected status code (not 200): " + response.statusCode() + " for " + httpRequest.uri());
            }

            String body = response.body();

            ActuatorEnvs envs = gson.fromJson(body, ActuatorEnvs.class);

            return envs.propertySources.stream()
                .flatMap(it -> it.properties.entrySet().stream())
                .filter(it -> envKeys.contains(it.getKey()))
                .map(it -> new Variable(it.getKey(), it.getValue().value))
                .collect(Collectors.toList());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("Cannot get " + totalUrl, e);
        }

    }

}
