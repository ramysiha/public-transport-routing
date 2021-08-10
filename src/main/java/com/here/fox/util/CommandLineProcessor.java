package com.here.fox.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.here.fox.model.Graph;
import com.here.fox.service.RoutingService;

/**
 * Command line utility for reading and processing user input.
 * The class implements Runnable and it is supposed to run in a separate thread due to
 * the blocking effect of reading from the input stream.
 * @author ramysiha
 *
 */
public class CommandLineProcessor implements Runnable {

	private Pattern routeCommandPattern = Pattern.compile("route\\s+(\\w+)\\s*->\\s*(\\w)");

	private Pattern nearbyCommandPattern = Pattern.compile("nearby\\s+(\\w+),\\s+(\\d+)");

	private Pattern transportPattern = Pattern.compile("(\\w+)\\s+->\\s+(\\w+):\\s+(\\d+)");

	private enum CommandEnum {
		NEARBY("nearby"), ROUTE("route");

		private String commandName;

		private CommandEnum(String commandName) {
			this.commandName = commandName;
		}

		public String getCommandName() {
			return commandName;
		}

		@Override
		public String toString() {
			return commandName;
		}
	}

	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final RoutingService routingService;
	private final boolean printLogo;

	public CommandLineProcessor(RoutingService routingService) {
		printLogo = true;
		inputStream = System.in;
		outputStream = System.out;
		this.routingService = routingService;
	}

	public CommandLineProcessor(RoutingService routingService, InputStream inputStream, OutputStream outputStream) {
		printLogo = false;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.routingService = routingService;
	}

	@Override
	public void run() {

		try (PrintWriter writer = new PrintWriter(outputStream, true)) {

			try (Scanner scanner = new Scanner(inputStream)) {

				printLogo(writer);

				if (!scanner.hasNextInt()) {
					throw new IllegalArgumentException("Invalid initial input, edge count was expected");
				}

				int edgeCount = scanner.nextInt();
				if (scanner.hasNextLine())
					scanner.nextLine();
				Graph.GraphBuilder graphBuilder = new Graph.GraphBuilder();
				graphBuilder.withEdgeCount(edgeCount);
				while (edgeCount > 0 && scanner.hasNextLine()) {
					if (addEdge(graphBuilder, scanner.nextLine())) {
						edgeCount--;
					}
				}

				graphBuilder.build();

				while (scanner.hasNextLine()) {
					try {
						writer.println(processCommand(scanner.nextLine()));
					} catch (IllegalArgumentException e) {
						System.err.println(e.getMessage());
						writer.println("Invalid Query!");
					}
				}
			} catch (IllegalStateException e) {
				System.err.println(e.getMessage());

			}
		}
	}

	private void printLogo(PrintWriter writer) {
		if (printLogo)
			writer.println("*** Public Transport System Operational. Please provide your input ***");
	}

	public String processCommand(String command) {
		if (command.startsWith(CommandEnum.NEARBY.getCommandName())) {
			Matcher matcher = nearbyCommandPattern.matcher(command);
			if (!matcher.matches()) {
				System.err.println("Failed to parse nearby command " + command);
			} else {
				return routingService.findNearbyLocation(matcher.group(1), Integer.parseInt(matcher.group(2)))
						.toString();
			}
		} else if (command.startsWith(CommandEnum.ROUTE.getCommandName())) {
			Matcher matcher = routeCommandPattern.matcher(command);
			if (!matcher.matches()) {
				System.err.println("Failed to parse route command " + command);
			} else {
				return routingService.findRoute(matcher.group(1), matcher.group(2)).toString();
			}
		} else {
			System.err.println("Failed to parse command " + command);
		}
		return "";
	}

	public boolean addEdge(Graph.GraphBuilder graphBuilder, String edgeStr) {
		Matcher matcher = transportPattern.matcher(edgeStr);
		if (!matcher.matches()) {
			System.err.println("Invalid Edge line " + edgeStr + " please re-insert it.");
			return false;
		}

		graphBuilder.withVector(matcher.group(1), matcher.group(2), Integer.parseInt(matcher.group(3)));
		return true;
	}

}
