package com.here.fox.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton Graph implementation. That class is designed to hold a map of Name -> Vector
 * The Graph class can only be constructed once using GraphBuilder class. The builder class
 * expects to know the edge count and then each edge can be added.
 * Each new edge will create any vectors if they were missing.
 * @author ramysiha
 *
 */
public final class Graph {
	private Map<String, VectorNode> vectorMap;

	public static class GraphBuilder {
		private int edgeCount;
		private int vectorInputCount;
		private Map<String, VectorNode> builderGraph = new ConcurrentHashMap<>();

		/**
		 * Set the number of edges to be constructed.
		 * @param edgeCount number ofedges that will be created. 
		 * @return
		 */
		public Graph.GraphBuilder withEdgeCount(int edgeCount) {
			if (edgeCount < 1)
				throw new IllegalArgumentException("EdgeCount must be greater than 0");
			this.edgeCount = edgeCount;
			return this;
		}

		/**
		 * Insert a new edge connection two vectors with the given time.
		 * @param source source vector
		 * @param destination destination vector
		 * @param time travel time from source to destination or the weight of the edge.
		 * @return
		 */
		public Graph.GraphBuilder withVector(String source, String destination, Integer time) {
			if (source == null || destination == null || time == null)
				throw new NullPointerException();

			if (source.equals(destination))
				throw new IllegalArgumentException(
						String.format("Source %s and Destination %s cannot be the same", source, destination));
			if (time < 1)
				throw new IllegalArgumentException(
						String.format("Source to Destination time %d must be greater than 0", time));

			vectorInputCount++;
			VectorNode sourceVector = builderGraph.computeIfAbsent(source, src -> new VectorNode(src));
			VectorNode destVector = builderGraph.computeIfAbsent(destination, dst -> new VectorNode(dst));

			sourceVector.addToEdges(destVector, time);

			return this;
		}

		/**
		 * Construct a new instance of the Graph and set it as the main singelton.
		 * @throws IllegalStateException in case the Graph Instance has already been pre-initialized.
		 * @return
		 */
		public Graph build() {
			if (INSTANCE != null) {
				throw new IllegalStateException("Path Router is a singelton and should be constructed only once");
			}
			if (edgeCount != vectorInputCount)
				throw new IllegalStateException(String.format("Number of edges %d does not vector input count %d",
						edgeCount, vectorInputCount));

			INSTANCE = new Graph();

			INSTANCE.vectorMap = new ConcurrentHashMap<>(builderGraph);
			builderGraph.clear();

			return INSTANCE;
		}
	}

	private Graph() {

	}

	private static Graph INSTANCE;

	/**
	 * Returns a single instance of an initialized graph
	 * @throws IllegalStateException if the Graph was not initialized using the GraphBuilder.
	 * @return
	 */
	public static Graph getInstance() {
		if (INSTANCE == null)
			throw new IllegalStateException("PathRouter has not been initialized, please use the Builder.");
		return INSTANCE;
	}

	/**
	 * Returns an unmodifiable thread safe of vector map.
	 * @return
	 */
	public Map<String, VectorNode> getVectorMap() {
		return Collections.unmodifiableMap(vectorMap);
	}

}
