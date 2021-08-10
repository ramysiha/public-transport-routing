package com.here.fox.service.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import com.here.fox.model.Graph;
import com.here.fox.model.VectorNode;
import com.here.fox.service.NearbyResult;
import com.here.fox.service.RoutingResult;
import com.here.fox.service.RoutingService;

/**
 * Dijkstra Algorithm for finding shortest path between two vertices
 * Graph is exposed as an adjacency list.
 * @author ramysiha
 *
 */
public final class RoutingServiceImpl implements RoutingService {

	@Override
	public synchronized NearbyResult findNearbyLocation(String source, int radius) {

		Map<String, VectorNode> graph = Graph.getInstance().getVectorMap();
		if (!graph.containsKey(source) || radius < 0)
			throw new IllegalArgumentException(
					String.format("Failed to find source %s or invalid radius %s", source, radius));

		Map<VectorNode, VectorNode> prevMap = new HashMap<>();
		final Map<VectorNode, Integer> pathWeight = graph.values().stream()
				.collect(Collectors.toMap(key -> key, value -> Integer.MAX_VALUE));
		Queue<VectorNode> queue = new PriorityQueue<VectorNode>(new Comparator<VectorNode>() {
			@Override
			public int compare(VectorNode v1, VectorNode v2) {
				return pathWeight.get(v1) - pathWeight.get(v2);
			}
		});

		VectorNode sourceNode = graph.get(source);
		pathWeight.put(sourceNode, 0);
		queue.add(sourceNode);

		while (!queue.isEmpty()) {
			VectorNode node = queue.poll();

			for (VectorNode edgeNode : node.getEdges().keySet()) {
				int weight = pathWeight.get(node) + node.getEdges().get(edgeNode);
				if (pathWeight.get(edgeNode) > weight) {
					pathWeight.put(edgeNode, weight);
					queue.add(edgeNode);
					prevMap.put(edgeNode, node);
				}
			}
		}

		
		return new NearbyResult(pathWeight.entrySet().stream()
				.filter(entry -> entry.getValue() <= radius && !entry.getKey().equals(sourceNode))
				.collect(Collectors.toList()));

	}

	@Override
	public synchronized RoutingResult findRoute(String source, String destination) {
		Map<String, VectorNode> graph = Graph.getInstance().getVectorMap();
		if (!graph.containsKey(source) || !graph.containsKey(destination))
			throw new IllegalArgumentException(
					String.format("Failed to find source %s or destination %s", source, destination));

		Map<VectorNode, VectorNode> prevMap = new HashMap<>();
		final Map<VectorNode, Integer> pathWeight = graph.values().stream()
				.collect(Collectors.toMap(key -> key, value -> Integer.MAX_VALUE));
		Queue<VectorNode> queue = new PriorityQueue<VectorNode>(new Comparator<VectorNode>() {
			@Override
			public int compare(VectorNode v1, VectorNode v2) {
				return pathWeight.get(v1) - pathWeight.get(v2);
			}
		});

		VectorNode sourceNode = graph.get(source);
		VectorNode destNode = graph.get(destination);
		pathWeight.put(sourceNode, 0);
		queue.add(sourceNode);
		boolean destNodeFound = false;
		
		while (!queue.isEmpty()) {
			VectorNode node = queue.poll();
			if (node.equals(destNode)) {
				destNodeFound = true;
				break;
			}

			for (VectorNode edgeNode : node.getEdges().keySet()) {
				int weight = pathWeight.get(node) + node.getEdges().get(edgeNode);
				if (pathWeight.get(edgeNode) > weight) {
					pathWeight.put(edgeNode, weight);
					queue.add(edgeNode);
					prevMap.put(edgeNode, node);
				}
			}
		}
		
		if (!destNodeFound) {
			return new RoutingResult(source, destination);
		}

		LinkedList<VectorNode> pathList = new LinkedList<>();
		VectorNode node = destNode;
		int transitTime = pathWeight.get(destNode);
		while (node != null) {
			pathList.addFirst(node);
			node = prevMap.get(node);
		}
		return new RoutingResult(pathList.stream().map(vn -> vn.getName()).toArray(String[]::new), transitTime);
	}

}
