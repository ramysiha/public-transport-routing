package com.here.fox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.here.fox.model.Graph;
import com.here.fox.service.RoutingService;
import com.here.fox.service.impl.RoutingServiceFactory;
import com.here.fox.service.impl.RoutingServiceFactory.RoutingAlg;

public class RoutingServiceTest extends AbstractTest {

	private static final int MAX_DISTANCE = 1000;
	private RoutingService routingService = RoutingServiceFactory.createRoutingServiceFactor(RoutingAlg.DIJKSTRAS);

	@Test
	@Timeout(value = 30, unit = TimeUnit.MILLISECONDS)
	void testGraphPerformance() throws InterruptedException, ExecutionException {
		Graph.GraphBuilder builder = new Graph.GraphBuilder();
		builder.withEdgeCount(182);
		ThreadLocalRandom random = ThreadLocalRandom.current();
		List<String> vectors = Arrays.asList("A", "B", "C", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O");
		for (String v1 : vectors) {
			for (String v2 : vectors) {
				if (v1.equals(v2))
					continue;
				builder.withVector(v1, v2, 1 + random.nextInt(1000));
			}
		}
		builder.build();

		for (String vector : vectors) {
			routingService.findNearbyLocation(vector, MAX_DISTANCE);
		}

	}
	
	@Test
	void testNoRoute() {
		Graph.GraphBuilder builder = new Graph.GraphBuilder();
		builder.withEdgeCount(2);
		builder.withVector("A", "B", 1);
		builder.withVector("A", "C", 1);
		builder.build();
		
		assertEquals("A -> B: 1", routingService.findRoute("A", "B").toString());
		
		assertEquals("Error: No route from B to C", routingService.findRoute("B", "C").toString());
	}

	@Test
	void testFaultyGraph() {
		Graph.GraphBuilder builder = new Graph.GraphBuilder();
		builder.withEdgeCount(1);
		builder.withVector("A", "B", 1);
		builder.withVector("B", "C", 1);
		
		assertThrows(IllegalArgumentException.class, () -> builder.withEdgeCount(-1));
		
		assertThrows(NullPointerException.class, () -> builder.withVector(null, "B", 1));
		
		assertThrows(IllegalArgumentException.class, () -> builder.withVector("A", "B", -1));
		
		assertThrows(IllegalStateException.class, () -> builder.build());
		
		assertThrows(IllegalStateException.class, () -> Graph.getInstance());
	}
}
