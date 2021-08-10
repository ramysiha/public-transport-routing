package com.here.fox.service;

import java.util.Arrays;

/**
 * A POJO class that will return the result of executing the "route" command on the Graph
 * The return result will contain a list of the vectors that will be traversed as well as the total
 * travel time.
 * @author ramysiha
 *
 */
public final class RoutingResult {
	private String[] routePoints;
	private int time;
	private String destination;
	private String source;

	public RoutingResult(String[] routePoints, int time) {
		if (time < 0)
			throw new IllegalArgumentException("Time cannot be in -ve");
		this.time = time;
		this.routePoints = Arrays.copyOf(routePoints, routePoints.length);
	}

	public RoutingResult(String source, String destination) {
		if (source == null || destination == null)
			throw new NullPointerException();
		if (source.isEmpty() || destination.isEmpty())
			throw new IllegalArgumentException("source or destination cannot be empty");
		
		this.source = source;
		this.destination = destination;
	}

	@Override
	public String toString() {
		if (routePoints == null || routePoints.length == 0)
			return String.format("Error: No route from %s to %s", source, destination);

		String routeStr = String.join(" -> ", routePoints);
		routeStr += ": " + time;
		return routeStr;
	}
}
