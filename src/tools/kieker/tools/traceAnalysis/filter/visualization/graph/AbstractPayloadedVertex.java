/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.filter.visualization.graph;

/**
 * Abstract superclass for "payloaded" vertices, i.e. vertices with attached objects.
 * 
 * @author Holger Knoche
 * 
 * @param <V>
 *            The type of the graph's vertices
 * @param <E>
 *            The type of the graph's edges
 * @param <O>
 *            The type of the graph's elements origin
 * @param <P>
 *            The type of the payload
 */
public abstract class AbstractPayloadedVertex<V extends AbstractPayloadedVertex<V, E, O, P>, E extends AbstractEdge<V, E, O>, O, P> extends AbstractVertex<V, E, O> {

	private final P payload;

	/**
	 * Creates a new vertex with the given origin and payload.
	 * 
	 * @param origin
	 *            The vertex' origin
	 * @param payload
	 *            The vertex' payload
	 */
	protected AbstractPayloadedVertex(final O origin, final P payload) {
		super(origin);
		this.payload = payload;
	}

	/**
	 * Returns this vertex' payload.
	 * 
	 * @return See above
	 */
	public P getPayload() {
		return this.payload;
	}
}
