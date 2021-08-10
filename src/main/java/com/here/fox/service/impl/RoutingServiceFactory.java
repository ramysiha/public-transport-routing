package com.here.fox.service.impl;

import com.here.fox.service.RoutingService;

public class RoutingServiceFactory {
	public enum RoutingAlg {
		DIJKSTRAS
	}

	public static RoutingService createRoutingServiceFactor(RoutingAlg routingAlg) {
		switch (routingAlg) {
		case DIJKSTRAS:
			return new RoutingServiceImpl();
		default:
			throw new UnsupportedOperationException("Invalid Routing Algorithm");
		}
	}
}
