/*
 * Copyright 2018-Present The CloudEvents Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.cloudevents.core.extensions;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.provider.ExtensionProvider;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author fabiojose
 */
public class DistributedTracingExtensionTest {

    @Test
    public void writeExtension() {
        DistributedTracingExtension tracing = new DistributedTracingExtension();
        tracing.setTraceparent("parent");
        tracing.setTracestate("state");

        CloudEvent event = CloudEventBuilder
            .v1()
            .withId("aaa")
            .withSource(URI.create("http://localhost"))
            .withType("example")
            .withExtension(tracing).build();

        assertThat(event.getExtension(DistributedTracingExtension.TRACEPARENT))
            .isEqualTo("parent");
        assertThat(event.getExtension(DistributedTracingExtension.TRACESTATE))
            .isEqualTo("state");
    }

    @Test
    public void parseExtension() {
        CloudEvent event = CloudEventBuilder.v1()
            .withId("aaa")
            .withSource(URI.create("http://localhost"))
            .withType("example")
            .withExtension(DistributedTracingExtension.TRACEPARENT, "parent")
            .withExtension(DistributedTracingExtension.TRACESTATE, "state")
            .build();

        DistributedTracingExtension tracing = ExtensionProvider
            .getInstance()
            .parseExtension(DistributedTracingExtension.class, event);

        assertThat(tracing).isNotNull();
        assertThat(tracing.getTraceparent()).isEqualTo("parent");
        assertThat(tracing.getTracestate()).isEqualTo("state");

    }
}
