/* Usman Amin
 * uamin2@u.rochester.edu
 * Lab Section: MW 1815-1930
 * I did not collaborate with anyone on this assignment.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

// Node class that represents intersections on the map.
public class Node implements Comparable<Node> {
	String id;
	double latitude;
	double longitude;
	boolean visited;
	double dist; // Represents distance from a node to the starting node.
	Node parent;

	LinkedList<Node> adjacence; // Stores adjacent nodes
	HashMap<Node, Edge> edgeConnections; // Stores node and the edge that connects it.

	// Constructor
	public Node(String id, double latitude, double longitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		visited = false;
		parent = null;

		adjacence = new LinkedList<Node>();
		edgeConnections = new HashMap<Node, Edge>();
	}

	// Register adjacent nodes and stores the connection of their edge.
	public void addEdge(Node n, Edge e) {
		adjacence.add(n);
		n.adjacence.add(this);

		edgeConnections.put(n, e);
		n.edgeConnections.put(this, e);

	}

	// Enables nodes to be comparable by distance.
	@Override
	public int compareTo(Node n) {
		if (this.dist == n.dist) {
			return 0;
		} else if (this.dist < n.dist) {
			return -1;
		} else {
			return 1;

		}
	}

}
