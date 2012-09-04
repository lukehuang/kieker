/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
 ***************************************************************************/

package kieker.test.tools.junit.rbridge;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.tools.util.RBridgeControl;

/**
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
public class RBridgeTest {
	private static final Log LOG = LogFactory.getLog(RBridgeTest.class);

	@Before
	public void setUp() throws Exception {}

	@Test
	public void test() {

		final RBridgeControl r = RBridgeControl.getInstance(new File("."));

		r.e("measures <<- c(NA,NA,NA,NA,NA,NA,NA,NA,NA,NA,31.0,41.0,95.0,77.0,29.0,62.0,49.0,NA)");
		r.e("forecasts <<- c(NA,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2.8181818181818183,6.0,12.846153846153847,17.428571428571427,18.2,20.9375,22.58823529411765)");
		r.e("anomalies <<- c(NA,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,0.8333333333333333,0.7446808510638298,0.761768901569187,0.6308623298033282,0.2288135593220339,0.49510173323285606,0.3689400164338537,NA)");
		r.e("combined <<- cbind(measures, forecasts, anomalies)");
		final Object result = r
				.e("plotAnomaly(combined, '/Users/till/Documents/repositories/uni/thesis-repos/Opad4lsssExperiments/opad4lsss_r/plots/junit_testplot.pdf', 1323437034798, 2000)");

		LOG.info(result);

		Assert.assertTrue(result != null);
		Assert.assertTrue(result instanceof org.rosuda.REngine.REXPInteger);
	}

}