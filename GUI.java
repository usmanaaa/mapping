
/* Usman Amin
 * uamin2@u.rochester.edu
 * Lab Section: MW 1815-1930
 * I did not collaborate with anyone on this assignment.
 */

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI {
	final static int width = 600;
	final static int height = 800;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis(); // Starts timer to find runtime of program.
		Graph graph = new Graph();
		HashMap<String, Node> adjList = graph.adjList;
		ArrayList<Edge> edges = graph.edges;
		String[] instructions = new String[2]; // Stores instructions for directions

		/*
		 * Reads input file and stores all intersections and roads as Nodes and Edges,
		 * respectively.
		 */
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(args[0]));
			String line;
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\\s+");
				if (info[0].equals("i")) { // If line starts with i its an intersection.
					Node node = new Node(info[1], Double.parseDouble(info[2]), Double.parseDouble(info[3]));
					adjList.put(info[1], node);
				}
				if (info[0].equals("r")) { // If line starts with r its a road.
					Edge road = new Edge(info[1], info[2], info[3]);
					edges.add(road);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add road connections for all intersections.
		for (int i = 0; i < edges.size(); i++) {
			Edge road = edges.get(i);
			graph.addConnection(road);
		}

		// Creates JPanel for drawing the map.
		JFrame frame = new JFrame();
		JPanel panel = graph.new Panel();
		frame.setTitle("Street Map");
		frame.setPreferredSize(new Dimension(width + 100, height + 100));
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

		// Program decides what to do based on arguments given.
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--show")) {
				frame.setVisible(true);
			}

			if (args[i].equals("--directions")) {
				instructions[0] = args[i + 1]; // Start Node
				instructions[1] = args[i + 2]; // End Node
				graph.shortestPaths(instructions[0]);
				graph.directions(instructions[1]);
				if (graph.getTotalDistance(instructions[1]) == Integer.MAX_VALUE) { 
					System.out.println("\n" + "\n" + "Destination is unreachable.");
				} else {
					System.out.println("\n" + "\n" + "Total distance traveled: "
							+ graph.getTotalDistance(instructions[1]) + " miles");
				}

			}
		}

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		long runTime = elapsedTime / 1000; // Program runtime in seconds.
		System.out.println("\n" + "\n" + "Program runtime: " + runTime + " seconds");

	}
}
