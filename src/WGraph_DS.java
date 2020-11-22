package ex1.src;

import java.io.Serializable;
import java.util.*;
/**
 * A weighted graph data structure based on weighted_graph interface and contains two inner classes
 * vertex and edge.
 * Contains 3 Maps, each represents another piece of data:
 * vertices - A map that stores all the vertices (key and node_info object) based on the node_info interface
 * that was implemented in the vertex inner class.
 * adjacent - A map that save a key and a set of node_info which represents its connections to other vertices.
 * edges - A map that contains an edge with its weight.
 *
 * Hashmaps, Hashmaps and Hashmaps why? -
 * Many of its methods runs in O(1) (get,put,remove,containsKey) which perfectly
 * fits the interface's methods requirements.
 */
public class WGraph_DS implements weighted_graph, Serializable {
    private Map<Integer, node_info> vertices;
    private Map<Integer, Set<node_info>> adjacent;
    private Map<Edge, Double> edges;
    private int MC;

    //Default constructor
    public WGraph_DS() {
        this.adjacent = new HashMap<>();
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        this.MC = 0;
    }

    /**
     * return the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        return this.vertices.get(key);
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return boolean
     * Explanation:
     * Check in edges map if there is an edge between the two nodes from both sides
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        return this.edges.containsKey(new Edge(node1, node2)) && this.edges.containsKey(new Edge(node2, node1));
    }

    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return double
     * Explanation:
     * Firstly we want to see if one of the nodes are null or both don't have
     * an edge between them if one of the conditions above are true then it will
     * return -1, otherwise return the weight of the edge.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (getNode(node1) == null || getNode(node2) == null || !hasEdge(node1, node2))
            return -1;

        return this.edges.get(new Edge(node1, node2));
    }

    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     * @param key
     * Explanation:
     * Firstly we check if the node exists (return nothing),
     * then we add the node to the vertices and to the adjacent maps by creating a new node
     * that contains the given key.
     */
    @Override
    public void addNode(int key) {
        if (getNode(key) != null)
            return;
        Vertex v = new Vertex(key);
        this.vertices.put(key, v);
        this.adjacent.put(v.getKey(), new LinkedHashSet<>());
        this.MC++;
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     * Explanation:
     * First of all we check whether w is a negative number. If it does - throw an Exception.
     * Then we add both nodes to each other's collection afterward we put it in the
     * edges map and increase the modification.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (node1 == node2) {
            return;
        }

        getV(node1).add(getNode(node2));
        getV(node2).add(getNode(node1));

        this.edges.put(new Edge(node1, node2), w);
        this.edges.put(new Edge(node2, node1), w);
        this.MC++;
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return vertices.values();
    }

    /**
     * Returns a Collection that contains all the nodes that are connected to node_id.
     * Explanation:
     * First of all I want to find if the node_id exist or not set if the
     * node_id exists return the Collection if not return null.
     * @param node_id
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        node_info node = getNode(node_id);
        if (node == null) {
            throw new IllegalArgumentException(String.format("Node %d does not exist", node_id));
        }
        return this.adjacent.get(node_id);
    }

    /**
     * Removes a node/vertex from all the lists.
     * Explanation:
     * Firstly it get the key node - let's call it "node". Then it checks
     * if that node is null return null, if not - we have to detach it from
     * all other nodes so: we make an ArrayList (let's call it a list)
     * make that list store all the neighbors of "key" then run over that list
     * to remove the neighbors from the "edges" map.
     * Afterward remove the key from both vertices and adjacent list.
     * And then - we get the deleted key
     * @param key
     * @return node_info\null
     */
    @Override
    public node_info removeNode(int key) {
        node_info node = getNode(key);
        if (node != null) {
            List<node_info> list = new ArrayList<>();
            for (node_info adj : this.getV(key)) {
                list.add(adj);
            }
            for (node_info adjacent : list) {
                this.removeEdge(key, adjacent.getKey());
            }
            this.adjacent.remove(key);
            this.vertices.remove(key);
            MC++;
        }
        return node;
    }

    /**
     * Removes an Edge from the edge map and from each node's list.
     * Explanation:
     * Firstly it checks whether the nodes exist or both nodes are connected. If both true, it returns nothing.
     * Then it removes from each nodes list the other node and as well from the edges map and increase the modification
     * on the graph.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (!hasEdge(node1, node2))
            MC++;
        if ((getNode(node1) == null || getNode(node2) == null)) {
            return;
        }
        if (!this.getV(node1).contains(getNode(node2)) || !this.getV(node2).contains(getNode(node1))) {
            return;
        }
        this.edges.get(new Edge(node1, node2));
        this.edges.get(new Edge(node2, node1));

        this.getV(node1).remove(getNode(node2));
        this.getV(node2).remove(getNode(node1));

        this.edges.remove(new Edge(node1, node2));
        this.edges.remove(new Edge(node2, node1));
    }

    /** return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int nodeSize() {
        return this.vertices.size();
    }

    /**
     * return the number of edges (undirectional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int edgeSize() {
        // Assuming the number of edges
        return this.edges.size() / 2;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return
     */
    @Override
    public int getMC() {
        return this.MC;
    }

    /**
     * Check whether the addresses of the pointers this & o1 are equal
     * @param o1
     * @return boolean
     */
    @Override
    public boolean equals(Object o1) {
        if (o1 == null) {
            return false;
        }
        if (o1 == this) {
            return true;
        }
        if (!(o1 instanceof WGraph_DS)) {
            return false;
        }
        WGraph_DS other = (WGraph_DS) o1;
        return vertices.equals(other.vertices) && 
               adjacent.equals(other.adjacent) && 
                edges.equals(other.edges) && 
                this.MC == other.MC;

    }

    /**
     * Prevent collision within the hashmap
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.vertices.hashCode();
        hash = 31 * hash + this.adjacent.hashCode();
        hash = 31 * hash + this.edges.hashCode();
        hash = 31 * hash + MC;
        return hash;
    }

    // -----------------------------------INNER CLASSES--------------------------------
    /** A Vertex inner class on a graph that implements the given interface node_info  */
    public static class Vertex implements node_info, Serializable {

        private double tag;
        private String info;
        private int key;

        //A Default constructor
        public Vertex() {
            this.tag = 0;
            this.setInfo("");
        }

        //A copy constructor
        public Vertex(int key) {
            this();
            this.key = key;
        }

        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         * @return int
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * return the remark (meta data) associated with this node.
         * @return String
         */
        @Override
        public String getInfo() {
            return "info: " + this.info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         * @return double
         */
        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        public String toString() {
            return String.format("%d", this.getKey());
        }

        /**
         * Check whether the addresses of the pointers this & o1 are equal
         * @param o
         * @return boolean
         */
        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this)
                return true;
            if (!(o instanceof Vertex))
                return false;
            Vertex other = (Vertex) o;
            return this.key == other.key && this.info.equals(other.info) && this.tag == other.tag;
        }

        /**
         * Prevents a collision on the hashmap
         * @return int
         */
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + this.key;
            hash = 31 * hash + this.info.hashCode();
            hash = 31 * hash + Double.hashCode(this.tag);
            return hash;
        }
    }

    /** an edge on a graph between two vertices. */
    public class Edge implements Serializable {
        private int src;
        private int dest;

        //Copy constructor
        public Edge(int source, int destination) {
            this.src = source;
            this.dest = destination;
        }

        /**
         * Get the source node of the edge
         * @return int
         */
        public int getSrc() {
            return this.src;
        }

        /**
         * Get the destination node of the edge
         * @return int
         */
        public int getDest() {
            return this.dest;
        }

        @Override
        public String toString() {
            return String.format("%d  -> %d", getSrc(), getDest());
        }

        /**
         * Check whether a address of an edge and an object is equal
         * @param o
         * @return boolean
         */
        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof Edge))
                return false;
            Edge other = (Edge) o;
            return this.src == other.src && this.dest == other.dest;
        }

        /**
         * Prevents a collision in the hashmap
         * @return int
         */
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + this.src;
            hash = 31 * hash + this.dest;
            return hash;
        }
    }
}
