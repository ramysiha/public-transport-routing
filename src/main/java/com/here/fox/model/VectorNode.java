package com.here.fox.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Graph Vector implementation. it contains a Map of all the edges and the weight of these edges.
 * The Vector also has the "name" field to hold the vector name, the name is the unique identifier for the Vector,
 * hence a duplicate Vectors with the same name will be overriden in the maps.
 * @author ramysiha
 *
 */
public class VectorNode {
	private Map<VectorNode, Integer> edges = new ConcurrentHashMap<>();

	private final String name;

	VectorNode(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Map<VectorNode, Integer> getEdges() {
		return Collections.unmodifiableMap(edges);
	}

	void addToEdges(VectorNode destVector, Integer time) {
		edges.put(destVector, time);	
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VectorNode))
			return false;
		return name.equals(((VectorNode) obj).getName());
	}

	@Override
	public String toString() {
		return name;
	}
}
