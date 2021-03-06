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

package org.springframework.boot.actuate.metrics.rich;

import org.junit.Test;

import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.export.RichGaugeExporter;
import org.springframework.boot.actuate.metrics.repository.InMemoryMultiMetricRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MultiMetricRichGaugeReader}.
 *
 * @author Dave Syer
 */
public class MultiMetricRichGaugeReaderTests {

	private InMemoryMultiMetricRepository repository = new InMemoryMultiMetricRepository();

	private MultiMetricRichGaugeReader reader = new MultiMetricRichGaugeReader(this.repository);

	private InMemoryRichGaugeRepository data = new InMemoryRichGaugeRepository();

	private RichGaugeExporter exporter = new RichGaugeExporter(this.data, this.repository);

	@Test
	public void countOne() {
		this.data.set(new Metric<Integer>("foo", 1));
		this.data.set(new Metric<Integer>("foo", 1));
		this.exporter.export();
		// Check the exporter worked
		assertThat(this.repository.countGroups()).isEqualTo(1);
		assertThat(this.reader.count()).isEqualTo(1);
		RichGauge one = this.reader.findOne("foo");
		assertThat(one).isNotNull();
		assertThat(one.getCount()).isEqualTo(2);
	}

	@Test
	public void countTwo() {
		this.data.set(new Metric<Integer>("foo", 1));
		this.data.set(new Metric<Integer>("bar", 1));
		this.exporter.export();
		assertThat(this.reader.count()).isEqualTo(2);
		RichGauge one = this.reader.findOne("foo");
		assertThat(one).isNotNull();
		assertThat(one.getCount()).isEqualTo(1);
	}

}
