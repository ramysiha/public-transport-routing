package com.here.fox.service;

public interface RoutingService {
	
	/**
	 * 
	 * @param source source vector to start the search from.
	 * @param radius max radius to be traversed ( inclusive )
	 * @return
	 */
	public NearbyResult findNearbyLocation(String source, int radius);

	/**
	 * 
	 * @param source source vector to start the path routing.
	 * @param destination destination vector to reach the routing path.
	 * @return
	 */
	public RoutingResult findRoute(String source, String destination);
}
