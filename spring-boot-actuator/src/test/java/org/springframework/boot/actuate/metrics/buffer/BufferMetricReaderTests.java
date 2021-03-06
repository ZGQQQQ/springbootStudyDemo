/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.metrics.buffer;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BufferMetricReader}.
 *
 * @author Dave Syer
 */
public class BufferMetricReaderTests {

	private CounterBuffers counters = new CounterBuffers();

	private GaugeBuffers gauges = new GaugeBuffers();

	private BufferMetricReader reader = new BufferMetricReader(this.counters, this.gauges);

	@Test
	public void countReflectsNumberOfMetrics() {
		this.gauges.set("foo", 1);
		this.counters.increment("bar", 2);
		assertThat(this.reader.count()).isEqualTo(2);
	}

	@Test
	public void findGauge() {
		this.gauges.set("foo", 1);
		assertThat(this.reader.findOne("foo")).isNotNull();
		assertThat(this.reader.count()).isEqualTo(1);
	}

	@Test
	public void findCounter() {
		this.counters.increment("foo", 1);
		assertThat(this.reader.findOne("foo")).isNotNull();
		assertThat(this.reader.count()).isEqualTo(1);
	}

}
