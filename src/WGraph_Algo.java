package ex1.src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.AbstractMap.SimpleEntry;

/**
 * A weighted graph algorithm class that implemented on a given weighted graph algorithm interface (weighted_graph_algorithms).
 * This class and the algorithm is based on a MinHeap data-structure and contain it as an inner class for easier understanding.
 * This class contains all the requested methods:
 *              - init,
 *              - getGraph
 *              - copy
 *              - isConnected
 *              - shortestPathDist,
 *              - shortestPath
 *              - other two private functions and an inner class ( each has an explaination)
 *
 * The Dijkstra algorithm I used:
 * (taken from https://ssaurel.medium.com/calculate-shortest-paths-in-java-by-implementing-dijkstras-algorithm-5c1db06b6541)
 *
 * function Dijkstra(Graph,source):
 *
 *   	create vertex set Q
 *
 *   	for each vertex v in Graph:		//Initialization
 *   		dist[v] <- INFINITY		//Unknown distance from source to V
 *   		prev[v] <- UNDEFINED		//Previous node in optimal path from source
 *   		add v to Q			//All nodes initially in Q (unvisited nodes)
 *
 *      dist[source] <- 0			//Distance from source to source
 *
 *   	while Q is not empty:
 *   		u <- vertex in Q with min dist[u] //Source node will be selected first
 *   		remove u from Q
 *
 *
 *   		for each neighbor v of u:        // where v is still in Q
 *   			alt <- dist[u] + length(u,v)
 *   			if alt < dist[v]:	 // A shorter path to v has been found
 *   			dist[v] <- alt
 *   			prev[v] <- u
 *
 *   	return dist[], prev[]
 */
public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph graph;

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        graph = g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return weighted_graph
     */
    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * Explanation:
     * We "walk" with a node all over the graph vertices and store them in a graph (graphCopy) then
     * we do the same with all its edges - we walk all over each node and get its neighbors/edges
     * by reconnect them all over again in the graph we want to return (graphCopy)
     * @return weighted_graph
     */
    @Override
    public weighted_graph copy() {
        weighted_graph graphCopy = new WGraph_DS();
        for (node_info currNode : this.graph.getV()) {
            graphCopy.addNode(currNode.getKey());
        }
        for (node_info currNode : graph.getV()) {
            for (node_info adjacenetNode : this.graph.getV(currNode.getKey())) {
                this.graph.connect(currNode.getKey(), adjacenetNode.getKey(),
                        graph.getEdge(currNode.getKey(), adjacenetNode.getKey()));
            }
        }
        return graphCopy;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume undirectional graph.
     * @return boolean
     * Explanation:
     * First we check whether the graph is empty or if there's one vertex. If happen -> returns true.
     * Then we get all the distances with dijkstra algorithm and we walk with a node all over the vertices.
     * If one of the distances that found in dijkstra is MAX_VALUE (or infinity) the it will return false
     * because it's obvious that the graph isn't connected if one of the nodes isn't reachable.
     * If the above doesn't happen the graph is connected.
     */
    @Override
    public boolean isConnected() {
        Collection<node_info> vertices = graph.getV();
        if (vertices.isEmpty() || vertices.size() == 1) {
            return true;
        }
        node_info src = vertices.iterator().next();
        Map<node_info, Double> allDistances = dijkstra(src.getKey()).getValue();
        for (node_info node : graph.getV()) {
            if (allDistances.get(node) == Double.MAX_VALUE) {
                return false;
            }
        }
        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return double
     * Explanation:
     * We use Map.Entry which is like Pairs to store all the shortests paths using dijkstra algorithm
     * Then if there is no such path we return -1  or return the distance we got.
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        Map.Entry<Map<node_info, node_info>, Map<node_info, Double>> allShortestsPaths = dijkstra(src);
        Map<node_info, Double> distances = allShortestsPaths.getValue();
        if (distances.get(graph.getNode(dest)) == Double.MAX_VALUE) {
            return -1;
        }
        else {
            return distances.get(graph.getNode(dest));
        }
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return List<node_info>
     * Explanation:
     * We store all the source's paths using Map.Entry (which is like Pairs) then use a private method to get
     * the the path by entering the destination node and its parents.
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        Map.Entry<Map<node_info, node_info>, Map<node_info, Double>> allShortestsPaths = dijkstra(src);
        Map<node_info, node_info> parents = allShortestsPaths.getKey();
        return computePath(graph.getNode(dest), parents);
    }

    /**
     * A private function that returns a list with the shortest path.
     * First of all we make a list of the shortest path we find by checking if the destination node isn't null, while he
     * isn't null then the list will get the current node which was pointed at the destination node and the
     * current node will turn into destination's node parent. Since we went from the end we will have to reverse
     * the list we intend to return.
     * @param destNode
     * @param parents
     * @return List<node_info>
     */
    private List<node_info> computePath(node_info destNode, Map<node_info, node_info> parents) {
        List<node_info> shortestPath = new ArrayList<>();
        node_info currNode = destNode;
        while (currNode != null) {
            shortestPath.add(currNode);
            currNode = parents.get(currNode);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }

    /**
     * An inner class that represents a MinHeap data structure,
     * the class implemented by a TreeMap in order to sort the priorities.
     */
    class MinHeap {
        Map<node_info, Double> elementToPriority;
        TreeMap<Double, Set<node_info>> prioritiesToElements; // We intentionally use TreeSet for sorting.

        //Default constructor
        MinHeap() {
            elementToPriority = new HashMap<>();
            prioritiesToElements = new TreeMap<>();
        }

        /**
         * Add a node to the Heap only if the priority already doesn't exist.
         * Runs in O(1) if the priorities are different then O(nlogn).
         * @param node
         * @param priority
         */
        public void add(node_info node, double priority) {
            elementToPriority.put(node, priority);
            if (!prioritiesToElements.containsKey(priority)) {
                prioritiesToElements.put(priority, new LinkedHashSet<>());
            }
            prioritiesToElements.get(priority).add(node);
        }

        /**
         * Decreases value of a key.
         * Runs in O(1) if the priorities are different then O(nlogn).
         * @param node
         * @param priority
         * @return node_info
         */
        public node_info decreaseKey(node_info node, double priority) {
            this.remove(node);
            this.add(node, priority);
            return node;
        }

        /**
         * Removes a node from the Priority queue.
         * Explanation:
         * We store the priority and the previous priority in a Set, Then we remove the node
         * if the list is empty there is no point of keeping it so we better remove it.
         * Then we return a Pair of a node and its previous priority.
         * @param node
         * @return Map.Entry<node_info, Double>
         */
        public Map.Entry<node_info, Double> remove(node_info node) {
            double previousPriority = elementToPriority.get(node);
            Set<node_info> elements = this.prioritiesToElements.get(previousPriority);
            elements.remove(node);
            if (elements.isEmpty()) {
                prioritiesToElements.remove(previousPriority);
            }
            elementToPriority.remove(node);
            return new SimpleEntry<>(node, previousPriority);
        }

        /**
         * Check whether the priority queue is empty.
         * (It check in both TreeMap & HashMap)
         * @return boolean
         */
        public boolean isEmpty() {
            return this.prioritiesToElements.isEmpty() && elementToPriority.isEmpty();
        }

        /**
         * Remove Min / Extract Min from the Heap.
         * Explanation:
         * If the list is not empty we get the lowest priority value in the priority queue then with an
         * iterator we get the first node (maximum) in the priority and remove it. Else we will get a null
         * because the list is empty.
         * @return Map.Entry<node_info, Double>
         */
        public Map.Entry<node_info, Double> removeMin() {
            if (!isEmpty()) {
                Map.Entry<Double, Set<node_info>> lowestPriority = this.prioritiesToElements.firstEntry();
                node_info firstNodeInPriority = lowestPriority.getValue().iterator().next();
                return remove(firstNodeInPriority);
            }
            return null;
        }
    }

    /**
     * Dijkstra algorithm based on a MinHeap as a priority queue.
     * If there are n different priorities then the time complexity of the sort is O(nlogn) on the other hand
     * in case that can be atleast V different priorities (in case each node has different priority) then the
     * sort is O(Vlog(V)).
     *
     * Explanation:
     * Like I mentioned in the header I used an algorithm.
     * First we make a map of parents and their distance and make a new heap
     * then we store in that heap,parents and distances all the edges while all the distances of all the nodes
     * in the graph is Infinity (Double.MAX_VALUE) then we update the source node we start with.
     * if the heap isn't empty get the distance of current and the path and put them in the
     * distances map.
     * Then we go all over the neighbors of the current and check for the weight of the edge searching
     * for the smallest one and decreasing each key and we keep going like that.
     * Then - finally! we get the shortest path in a map entry
     * @param src
     * @return Map.Entry<Map<node_info, node_info>, Map<node_info, Double>>
     */
    private Map.Entry<Map<node_info, node_info>, Map<node_info, Double>> dijkstra(int src) {
        Map<node_info, node_info> parents = new HashMap<>();
        Map<node_info, Double> distances = new HashMap<>();
        MinHeap heap = new MinHeap();
        for (node_info node : this.graph.getV()) {
            parents.put(node, null);
            heap.add(node, Double.MAX_VALUE);
            distances.put(node, Double.MAX_VALUE);
        }
        heap.decreaseKey(graph.getNode(src), 0);

        while (!heap.isEmpty()) {
            Map.Entry<node_info, Double> entry = heap.removeMin();
            node_info current = entry.getKey();
            double pathDistance = entry.getValue();
            distances.put(current, pathDistance);

            for (node_info adjacent : graph.getV(current.getKey())) {
                double edgeWeight = graph.getEdge(current.getKey(), adjacent.getKey());
                double currentDistance = pathDistance + edgeWeight;
                if (currentDistance < distances.get(adjacent)) {
                    parents.put(adjacent, current);
                    heap.decreaseKey(adjacent, currentDistance);
                }
            }
        }
        return new SimpleEntry<>(parents, distances);
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            String filename = !file.contains(".txt") ? file + ".txt" : file;
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject((WGraph_DS) this.graph);
            out.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            String filename = !file.contains(".txt") ? file + ".txt" : file;
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            this.graph = (WGraph_DS) in.readObject();            
            in.close();
            return true;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
