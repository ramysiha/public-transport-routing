package com.here.fox.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.here.fox.model.VectorNode;

/**
 * A POJO class that will return the result of executing the "nearby" command on the Graph
 * The return result will contain a list of all the nearby vectors and their travel time.
 * @author ramysiha
 *
 */
public final class NearbyResult {
	private final List<Entry<VectorNode, Integer>> nearbyPoints;

	public NearbyResult(List<Entry<VectorNode, Integer>> list) {
		this.nearbyPoints = new ArrayList<>(list);
	}

	@Override
	public String toString() {
		if (nearbyPoints.isEmpty())
			return "Not Found!";

		return nearbyPoints.stream().map(entry -> entry.getKey() + ": " + entry.getValue())
				.collect(Collectors.joining(", "));
	}
}
