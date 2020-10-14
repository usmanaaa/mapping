/* Usman Amin
 * uamin2@u.rochester.edu
 * Lab Section: MW 1815-1930
 * I did not collaborate with anyone on this assignment.
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.swing.JPanel;

public class Graph {

	static HashMap<String, Node> adjList; // The graph itself
	static ArrayList<Edge> edges;
	static ArrayList<Node> path; // Stores the nodes visited in the shortest path from A to B.

	// Constructor
	public Graph() {
		path = new ArrayList<Node>();
		adjList = new HashMap<String, Node>();
		edges = new ArrayList<Edge>();

	}

	// Sets all nodes to unvisited.
	public static void setAllToUnvisited() {
		for (String s : adjList.keySet()) {
			Node curr = adjList.get(s);
			curr.visited = false;

		}
	}

	/* Connects two nodes with an edge by adding them to each other's adjacency
	 * list and storing their connecting edge.
	 */
	public static void addConnection(Edge e) {
		Node head = adjList.get(e.head);
		Node tail = adjList.get(e.tail);
		e.distance = calculateDistance(e);

		head.addEdge(tail, e);
	}

	/* Finds the distance in miles between two points using its longitude and latitude through the
	 * Haversine formula.
	 */
	public static double calculateDistance(Edge e) {
		double R = 3961; // Converts to miles
		Node intersection1 = adjList.get(e.head);
		Node intersection2 = adjList.get(e.tail);

		double lat1 = intersection1.latitude;
		double lat2 = intersection2.latitude;
		double lon1 = intersection1.longitude;
		double lon2 = intersection2.longitude;

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		// Haversine formula 
		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		
		return R * c;
	}

	// Gets the maximum latitude of all nodes in a graph.
	public static double getMaxLat() {
		ArrayList<Double> lats = new ArrayList<Double>();
		for (String id : adjList.keySet()) {
			double latitude = adjList.get(id).latitude;
			lats.add(latitude);
		}
		double max = lats.get(0);
		for (int i = 1; i < lats.size(); i++) {
			if (lats.get(i) > max) {
				max = lats.get(i);
			}
		}
		return max;
	}
	
	// Gets the maximum longitude of all nodes in a graph.
	public static double getMaxLon() {
		ArrayList<Double> lons = new ArrayList<Double>();
		for (String id : adjList.keySet()) {
			double longitude = adjList.get(id).longitude;
			lons.add(longitude);
		}
		double max = lons.get(0);
		for (int i = 1; i < lons.size(); i++) {
			if (lons.get(i) > max) {
				max = lons.get(i);
			}
		}
		return max;
	}

	// Gets the minimum latitude of all nodes in a graph.
	public static double getMinLat() {
		ArrayList<Double> lats = new ArrayList<Double>();
		for (String id : adjList.keySet()) {
			double latitude = adjList.get(id).latitude;
			lats.add(latitude);
		}
		double min = lats.get(0);
		for (int i = 1; i < lats.size(); i++) {
			if (lats.get(i) < min) {
				min = lats.get(i);
			}
		}
		return min;
	}
	
	// Gets the minimum longitude of all nodes in a graph.
	public static double getMinLon() {
		ArrayList<Double> lons = new ArrayList<Double>();
		for (String id : adjList.keySet()) {
			double longitude = adjList.get(id).longitude;
			lons.add(longitude);
		}
		double min = lons.get(0);
		for (int i = 1; i < lons.size(); i++) {
			if (lons.get(i) < min) {
				min = lons.get(i);
			}
		}
		return min;
	}

	/* Uses Djikstara's algorithm to find the shortest path from the start node to every other node 
	 * in graph with a PriorityQueue
	 */
	public static void shortestPaths(String a) {
		Node start = adjList.get(a);
		PriorityQueue<Node> pq = new PriorityQueue<Node>();
		int count = 0;

		// Makes distance from start Node infinity except for itself, which will be 0.
		for (String i : adjList.keySet()) {
			Node n = adjList.get(i);
			n.dist = Integer.MAX_VALUE;
			if (n.id == start.id) {
				n.dist = 0;
			}
			count++;
			pq.add(n);
		}

		/* After it is added to the PQ the Node with the minimum distance will be visited. If a Node
		 * it is connected to has not been visited, it will visit and create a cost to reach it from
		 * the given start Node. It also keeps track of what order the nodes were visited.
		 */
		while (count > 0) {
			Node curr = adjList.get(pq.poll().id);
			curr.visited = true;
			count--;
			LinkedList<Node> connections = curr.adjacence;
			
			for (Node n : connections) {
				if (n.visited == false) {
					double cost = curr.edgeConnections.get(n).distance;
					if (n.dist > curr.dist + cost) {
						n.parent = curr; // Keeps track of order of nodes visited.
						n.dist = curr.dist + cost;
						pq.remove(n);
						pq.add(n);
					}
				}
			}
		}
	}

	/* Takes a given Node, traces it backwards to the starting node and prints them out recursively.
	 * It also stores the path taken in order to draw its path.
	 */
	public void directions(String b) {
		Node curr = adjList.get(b);

		if (curr.parent != null) {
			directions(curr.parent.id);
			System.out.print(" to ");

		}

		path.add(curr); // Stores path
		System.out.print(curr.id);

	}

	// Gets total distance traveled in miles in a path.
	public double getTotalDistance(String b) {
		Node curr = adjList.get(b);
		return curr.dist;

	}
	
	// toString
		public void tostring() {
			for (String id : adjList.keySet()) {
				HashMap<Node, Edge> adj = adjList.get(id).edgeConnections;
				System.out.println("Node in adjList id: " + id);
				for (Node n : adj.keySet()) {
					System.out.println("   Connects to: " + n.id);
				}
			}
		}

	public class Panel extends JPanel {
		
		/* Draws inital map. Takes the range of longitude and latitude and uses it to scale 
		 * x-axis and y-axis appropriately. It then essentially draws the edge connecting two
		 * nodes.
		 */ 
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			double minLat = getMinLat();
			double minLon = getMinLon();
			double maxLat = getMaxLat();
			double maxLon = getMaxLon();
			double xRange = (maxLon - minLon);
			double yRange = (maxLat - minLat);
			double xScale = xRange / getWidth();
			double yScale = yRange / getHeight();

			for (Edge e : edges) {
				Node head = adjList.get(e.head);
				Node tail = adjList.get(e.tail);

				double lat1 = head.latitude;
				double lat2 = tail.latitude;
				double lon1 = head.longitude;
				double lon2 = tail.longitude;

				int y1 = (int) ((lat1 - minLat) / yScale);
				int y2 = (int) ((lat2 - minLat) / yScale);
				int x1 = (int) ((lon1 - minLon) / xScale);
				int x2 = (int) ((lon2 - minLon) / xScale);
				g.drawLine(x1, getHeight() - y1, x2, getHeight() - y2); 

			}

			// Draws map of path by looking at the path list and drawing their edges. 
			g.setColor(Color.MAGENTA);
			for (int i = 0; i < path.size() - 1; i++) {
				Node head = path.get(i);
				Node tail = path.get(i + 1);

				double lat1 = head.latitude;
				double lat2 = tail.latitude;
				double lon1 = head.longitude;
				double lon2 = tail.longitude;

				int y1 = (int) ((lat1 - minLat) / yScale);
				int y2 = (int) ((lat2 - minLat) / yScale);
				int x1 = (int) ((lon1 - minLon) / xScale);
				int x2 = (int) ((lon2 - minLon) / xScale);
				g.drawLine(x1, getHeight() - y1, x2, getHeight() - y2);

			}

		}
	}

}
