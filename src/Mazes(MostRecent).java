//Part 1 of Maze Game
//Wang, Joseph
//Fried, Eylam


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import tester.*;

import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*; 

/** ---------------------- Deques Taken From Past HW -----------------------**/

//to represent a boolean statement
interface IPred<T> {
    boolean apply(T t);
}

//a predicate which always returns true - helps test the actual function
//find and makes sure it returns correctly
class AnyString implements IPred<String> {
    public boolean apply(String s) {
        return true;
    }
}

//a predicate that returns true for the string 2 ("2")
class String2 implements IPred<String> {
    public boolean apply(String s) {
        return s.equals("2");
    }
}

//a predicate that returns true for the string 2 ("2")
class FindHello implements IPred<String> {
    public boolean apply(String s) {
        return s.equals("Hello");
    }
}


//to represent the class Deque, which is an array list that can traverse in
//both directions (forward and backward)
class Deque<T> {
    Sentinel<T> header;
 
    // constructor with zero arguments - initializes the Sentinel's next and
    // previous as the Sentinel itself
    Deque() {
        this.header = new Sentinel<T>();
    }
 
    // convenience constructor
    Deque(Sentinel<T> header) {
        this.header = header;
    }
 
    // counts this deque's size - includes nodes but not the header
    int size() {
        return this.header.size();
    }
 
    // EFFECT: adds a given data point to the head of the list
    void addAtHead(T data) {
        if (this.header.endList()) {
            this.header.addToEmpty(data);
        }
        else {
            this.header.next.prev = new Node<T>(data, this.header.next, 
                    this.header);
        }
    }
 
    // EFFECT: adds a data point at the end of the list
    void addAtTail(T data) {
        if (this.header.endList()) {  // checks for an empty list
            this.header.addToEmpty(data);
        }
        else { // got to add the data point to the end of the whole list
            this.header.prev.next = new Node<T>(data, this.header, 
                    this.header.prev);
        }
    }
     
    // returns the data of the removed node 
    // (next time you should really put this in the instructions)
    // EFFECT: removes the first node from the list
    T removeFromHead() {
        if (this.header.endList()) { // checks for an empty list
            throw new RuntimeException("Can't remove from an empty list");
        }
        else { // we cast it to Node<T> since we already did a check for endList
            Node<T> removed = (Node<T>)this.header.next;
            this.header.next = this.header.next.next;
            this.header.next.prev = this.header;
            return removed.data;
        }
    }
     
    // returns the data of the removed node 
    // (next time you should really put this in the instructions)
    // EFFECT: removes the last node from the list
    T removeFromTail() {
        if (this.header.endList()) { // checks for an empty list
            throw new RuntimeException("Can't remove from an empty list");
        }
        else { // we cast it to Node<T> since we already did a check for endList
            Node<T> removed = (Node<T>)this.header.prev;
            this.header.prev = this.header.prev.prev;
            this.header.prev.next = this.header;
            return removed.data;
        }
    }
     
    // searches the deque list for the first node that the given predicate
    // returns true for. If none exist, then the header is given
    ANode<T> find(IPred<T> pred) {
        if (this.header.next instanceof Sentinel<?>) { // quick check for empty
            return this.header;         // if true would bypass the other steps
        }
        else { // since not empty, assigns the current as the first of list
            ANode<T> current = this.header.next;
            return findHelp(pred, current, this);
        }
    }
     
    // helper function to the find method - actively goes through the list
    ANode<T> findHelp(IPred<T> pred, ANode<T> current, Deque<T> deq) {
        if (current instanceof Sentinel<?>) { // Safeguard against errors
            return deq.header;
        }
        // casts the current as a node since we did the Sentinel check
        else if (pred.apply( ((Node<T>)current).data )) { // checks if true
            return current;
        }
        else { // recurs on the next of the list, keeping pred and deq the same
            return findHelp(pred, current.next, deq);
        }
    }
    
    // EFFECT: removes a given node from this list - fixes up links
    void removeNode(ANode<T> anode) {
        if (anode.endList()) { /* does nothing */ }
        else { //casts anode as a Node<T> since we did the Sentinel check above
            this.removeNodeHelp((Node<T>)anode, (Node<T>)this.header.next);
        }
    }
     
    // EFFECT: removes a given node from this list - fixes up links
    void removeNodeHelp(Node<T> anode, Node<T> current) {
        if (anode.data.equals(current.data)) { // checks if same nodes
            ANode<T> tempPrev = current.prev;
            ANode<T> tempNext = current.next;
             
            tempPrev.next = tempNext;
            tempNext.prev = tempPrev;
        }
        else if (anode.endList()) { /* if the last, does nothing */ }
        else { // recurs on the next of current 
              // (casts to node because we already did the endList check)
            this.removeNodeHelp(anode, (Node<T>)current.next);
        }
    }     
}
    
//represents the abstract class ANode which is the different elements in a 
//Deque
abstract class ANode<T> {
    ANode<T> next;
    ANode<T> prev;
         
    ANode(ANode<T> next, ANode<T> prev) {
        this.next = next;
        this.prev = prev;
    }
    // helps count the size of the node's deque
    abstract int size();
     
    // Abstracted helper methods for duplicate code
     
    // checks if the list is an empty list by checking if it has a next
    // that is not the sentinel
    boolean endList() {
        return this.next instanceof Sentinel<?> ;
    }    
}
    
//to represent the start of the Deque, this shows the start and end
class Sentinel<T> extends ANode<T> {
    // Sentinel is has no other info besides what is in ANode<T>, used to
    // show the start and end of the list
     
    // base constructor
    Sentinel(ANode<T> next, ANode<T> prev) {
        super(next, prev);
    }
     
    // zero argument constructor
    Sentinel() {
        this(null, null);
        this.next = this;
        this.prev = this;
    }
     
    // counts the size of the list
    public int size() {
        if (this.next == null || this.next instanceof Sentinel<?>) {
            return 0;
        }
        else {
            return this.next.size();
        }
    }
     
    // adds a single data point to the list
    public void addToEmpty(T data) {
        this.next = new Node<T>(data, this, this);
    }     
}
    
//to represent a node in a Deque that holds data
class Node<T> extends ANode<T> {
    T data;
         
    // only T data constructor - next and previous are initialized as null
    Node(T data) {
        super(null, null); // does this work instead of this(data, null, null)?
        this.data = data;
    }
     
    // convenience constructor
    Node(T data, ANode<T> next, ANode<T> prev) {
        super(next, prev);
        this.data = data;
         
        /* had this in before, but the if's should work - take out if all good
        this.next.prev = this;
        this.prev.next = this;
        */
         
        // makes the given Nodes link back to this node
        if (next == null) {
            throw new IllegalArgumentException("The next is null");
        }
        else {
            this.next.prev = this; 
        }
         
        if (prev == null) {
            throw new IllegalArgumentException("The prev is null");
        }
        else {
            this.prev.next = this;
        }
         
    }
     
     
    // to help count the size of this node's deque
    public int size() {
        if (this.next instanceof Sentinel<?>) {
            return 1;
        }
        else {
            return 1 + this.next.size();
        }
    }
}

/** ------------- Stacks and Queues Using Deques (from Lab 10) -------------**/

// represents a stack class (last in first out) for depth first search
class Stack<T> {
    Deque<T> contents;
    
    Stack(Deque<T> contents) {
        this.contents = contents;
    }
    
    // adds an item to the head of a list
    // EFFECT: adds an item to the head
    void push(T items) {
        this.contents.addAtHead(items);
    }
    
    // tells if a stack is empty or not
    boolean isEmpty() {
        return this.contents.size() == 0;
    }
    
    // removes and returns the head of a list
    // EFFECT: removes an item from the head
    T pop() {
        return this.contents.removeFromHead();
    }
    
}


// represents a queue class (first in first out) for breadth first search
class Queue<T> {
    Deque<T> contents;
    
    Queue(Deque<T> contents) {
        this.contents = contents;
    }
    
    // adds an item to the end of the list
    // EFFECT: adds an item to the tail
    void enqueue(T item) {
        this.contents.addAtTail(item);
    }
    
    // tells if the queue is empty or not
    boolean isEmpty() {
        return this.contents.size() == 0;
    }
    
    // removes and returns the head of a list
    // EFFECTS: removes an item from the head
    T dequeue() {
        return this.contents.removeFromHead();
    }
}


/** ---------------------- IList's and Iterators ---------------------------**/

//to represent a list
interface IList<T> extends Iterable<T> {

    //is this list the same as that list
    boolean sameList(IList<T> list);

    //is the cons list the same as that one
    boolean sameConsList(ConsList<T> other);

    //is the empty list the same as that one
    boolean sameMtList(MtList<T> other);

    //does this list contain the given object
    boolean contains(T t);

    //is this list a cons
    boolean isCons();

    //return the given list as a cons
    ConsList<T> asCons();

    //return the iterator 
    Iterator<T> iterator();

    //add the given item to be the first of this list
    IList<T> add(T t);

    //return the length of this list
    int length();

    //remove the given item from the list
    IList<T> remove(T t);
}

//to represent an empty list 
class MtList<T> implements IList<T> {

    //is this empty the same as that list
    public boolean sameList(IList<T> list) {
        return list.sameMtList(this);
    } 

    //is this empty list the same as that cons
    public boolean sameConsList(ConsList<T> other) {
        return false;
    }

    //is this empty list the same as that one
    public boolean sameMtList(MtList<T> other) {
        return true;
    }

    //does this list contain the given object
    public boolean contains(T t) {
        return false;
    }

    //return the iterator
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }

    //is this list a cons
    public boolean isCons() {
        return false;
    }

    //return the given list as a cons
    public ConsList<T> asCons() {
        throw new ClassCastException("MtList<T> cannot be cast ot ConsList<T>");
    }

    //add the given item to be the first of this list
    public IList<T> add(T t) {
        return new ConsList<T>(t, this);
    }

    //return the length of this IList
    public int length() {
        return 0;
    }

    //remove the given item from this list
    public IList<T> remove(T t) {
        return new MtList<T>();
    }
}


//to represent a cons list
class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;

    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    //is this list the same as that list
    public boolean sameList(IList<T> list) {
        return list.sameConsList(this);
    }

    //is this cons list the same as that cons list
    public boolean sameConsList(ConsList<T> other) {
        return this.first.equals(other.first) && other.rest.sameList(this.rest);
    }

    //is this cons list the same as that empty one
    public boolean sameMtList(MtList<T> other) {
        return false;
    }

    //does this list contain the given object
    public boolean contains(T t) {
        return (t.equals(this.first)) || this.rest.contains(t);
    }

    //return the iterator
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }

    //is this a cons
    public boolean isCons() {
        return true;
    }

    //return this as a cons list
    public ConsList<T> asCons() {
        return this;
    }

    //add the given item to this cons
    public IList<T> add(T t) {
        return new ConsList<T>(t, this);
    }

    //return the length of this cons list
    public int length() {
        return 1 + this.rest.length();
    }

    //remove the given item 
    public IList<T> remove(T t) {
        if (t == this.first) {
            return this.rest;
        }
        else {
            return new ConsList<T>(this.first, this.rest.remove(t));
        }
    }

}


//to represent an iterator
class IListIterator<T> implements Iterator<T> {
    IList<T> items;

    IListIterator(IList<T> items) {
        this.items = items;
    }

    //does this iterator have a next
    public boolean hasNext() {  
        return this.items.isCons();
    }

    //return the next item
    public T next() {
        if (!this.hasNext()) {
            throw new RuntimeException("No Next Item");
        }
        else {
            ConsList<T> cons1 = this.items.asCons();
            T result = cons1.first;
            this.items = cons1.rest;
            return result;
        }
    }

    //remove this
    public void remove() {
        throw new 
        UnsupportedOperationException("Why did it have to come to this??");
    }
}

/** ----------------------------------------------------------------------- **/

/** ------------------------ GRAPH REPRESENTATION --------------------------**/

//to represent the position of a node
class NodePosn extends Posn {
    @Override
    //override the method hashCode
    public int hashCode() {
        return this.x * 1000 + this.y * 10;
    }

    public NodePosn(int x, int y) {
        super(x, y);
    } 
    //is this posn the same as that one
    boolean samePosn(NodePosn p) {
        return (this.x == p.x && this.y == p.y);
    }
    @Override
    //override the method equals
    public boolean equals(Object other) {
        if (!(other instanceof NodePosn)) {
            return false;
        }
        NodePosn that = (NodePosn)other;
        return this.samePosn(that);
    }
}

// represents a vertex in a graph
class Vertex {
    NodePosn pos;
    Vertex top;
    Vertex bottom;
    Vertex left;
    Vertex right;
    boolean visited;
    boolean path;
    boolean playerHere;
    boolean topPath;
    boolean bottomPath;
    boolean rightPath;
    boolean leftPath; 
    @Override
    public int hashCode() {
        return (this.pos.x * 1000) + (this.pos.y * 10);
    }
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vertex)) {
            return false;
        }
        Vertex that = (Vertex)other;
        return this.sameVertex(that);
    }

    Vertex(int x, int y) {
        this.pos = new NodePosn(x, y);
        this.top = null;
        this.bottom = null;
        this.left = null;
        this.right = null;
        this.visited = false;
        this.path = false;
        this.playerHere = false;
        this.topPath = false;
        this.bottomPath = false;
        this.rightPath = false;
        this.leftPath = false;
    }
    //is this vertex the same as that one
    public boolean sameVertex(Vertex that) {
        return this.pos.x == that.pos.x &&
                this.pos.y == that.pos.y;
    }

    //update this cell on top
    //EFFECT: change this.top to the given one
    void newTop(Vertex top) {
        this.top = top;
    }

    //update this cell on the bottom
    //EFFECT: change this.left to the given one
    void newBottom(Vertex bottom) {
        this.bottom = bottom;
    }

    //update this cell on the left
    //EFFECT: change this.left to the given one
    void newLeft(Vertex left) {
        this.left = left;
    }

    //update this cell on the right
    //EFFECT: change this.right to the given one
    void newRight(Vertex right) {
        this.right = right;
    }

    //update travel from this node to the node above
    //EFFECT: change the top path boolean
    void updateTopPath(boolean b) {
        this.topPath = b;
    }

    //update travel from this node to the one below
    //EFFECT: change the bottom path boolean 
    void updateBottomPath(boolean b) {
        this.bottomPath = b;
    }

    //update travel from this node to the node on the right
    //EFFECT: change the right path boolean
    void updateRightPath(boolean b) {
        this.rightPath = b;
    }

    //update travel from this node to the node on the left
    //EFFECT: change the left path boolean
    void updateLeftPath(boolean b) {
        this.leftPath = b;
    }

    //update to check that someone has traveled on it
    //EFFECT: change whether the user has seen this node or not
    void updateBeenHere() {
        this.visited = true;
    }

    //update this node as the player's node (to be represented in blue)
    //EFFECT: change whether this is the player's current node
    void updatePlayersHere(boolean b) {
        this.playerHere = b;
    }

    //update the ability to travel from this node to the given node
    //EFFECT: call one of the update path methods to true
    void updateEdges(Vertex to) {
        if (this.top == to) {
            this.updateTopPath(true);
        }
        if (this.bottom == to) {
            this.updateBottomPath(true);
        }
        if (this.left == to) {
            this.updateLeftPath(true);
        }
        if (this.right == to) {
            this.updateRightPath(true);
        }
    }

    // gives a string representation for the vertex
    public String representative() {
        return (this.pos.x + "") + "," + (this.pos.y + "");
    }
}

// represents the edges of a graph
class Edge {
    Vertex from;
    Vertex to;
    int weight;
    @Override
    public int hashCode() {
        return this.from.hashCode() * this.weight;
    }
    @Override
    //override equals 
    public boolean equals(Object other) {
        if (!(other instanceof Edge)) {
            return false;
        }
        Edge that = (Edge)other;
        return this.sameEdge(that);
    }

    Edge(Vertex from, Vertex to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    //is this edge the same as that one
    public boolean sameEdge(Edge that) {
        return this.from.sameVertex(that.from) &&
                this.to.sameVertex(that.to) &&
                this.weight == that.weight;
    }
}


//Comparator for Edges
class EdgeComparator implements Comparator<Edge> {
    //compare the two edge weights
    public int compare(Edge edge1, Edge edge2) {
        return edge1.weight - edge2.weight;
    }  
}

//to represent a function that will have a given hashmap
class UnionFind {
    HashMap<String, String> r;

    UnionFind(HashMap<String, String> r) {
        this.r = r;
    }

    //union the two values of the hashmap
    //CHANGE: update r value at node one
    void union(String s1, String s2) {
        String parent1 = this.find(s1);
        String parent2 = this.find(s2);
        this.r.put(this.find(parent1), this.find(parent2));
    }

    //return the representative of this nodePosn key
    String find(String that) {
        if (this.r.get(that).equals(that)) {
            return that;
        }
        else {
            return find(r.get(that)); 
        }
    } 
}


/** ---------------------------------------------------------------------- **/

// the player, which holds the current vertex
class Player {
    int x;
    int y;
    Vertex current;

    Player(Vertex current) {
        this.current = current;
        this.x = this.current.pos.x;
        this.y = this.current.pos.y;
        this.current.updatePlayersHere(true);
    }
    
    //update the current cell of the player
    //EFFECT: update this.current, as well as the X and Y posn of the player's
    //current vertex field in previous and current
    void newCell(Vertex v) {
        this.current.updatePlayersHere(false);
        this.current = v;
        this.x = this.current.pos.x;
        this.y = this.current.pos.y;
        this.current.updatePlayersHere(true);
    }
    
    //is the player able to move in the desired direction
    public boolean ableToMove(String dir) {
        boolean able = false;
        if (dir.equals("down") && this.current.bottomPath) {
            able = true;
        }
        if (dir.equals("up") && this.current.topPath) {
            able = true;
        }
        if (dir.equals("right") && this.current.rightPath) {
            able = true;
        }
        if (dir.equals("left") && this.current.leftPath) {
            able = true;
        }
        //return the updates ability to move boolean
        return able;
    }
}

/** ---------------------------------------------------------------------- **/

class MazeWorld extends World {
    // constraints for the board and canvas, keeps the proportions constant
    static final int CANVAS_WIDTH = 600;
    static final int CANVAS_HEIGHT = (CANVAS_WIDTH);
    static final int AMOUNT_COLUMNS = 10;  // 100 has to be divisible by this
    static final int AMOUNT_ROWS = CANVAS_HEIGHT / 
            (CANVAS_WIDTH / AMOUNT_COLUMNS);
    static final int CELL_SIZE = CANVAS_WIDTH / AMOUNT_COLUMNS;

    //Vertex currentVert;
    Posn current;
    NodePosn end;
    ArrayList<Edge> edges;
    ArrayList<Edge> worklist;
    ArrayList<ArrayList<Vertex>> board;
    Random rand;
    UnionFind findUnion; 
    Vertex endVertex;
    boolean depthSearch;
    boolean breadthSearch;
    boolean depthComplete;
    boolean breadthComplete;
    Player p;
    boolean gameOver;
    NodePosn start;
    Deque<Vertex> searchDeque;
    Sentinel<Vertex> searchSent;
    Node<Vertex> searchNode;
    Queue<Vertex> breadthQueue;
    Stack<Vertex> depthStack;
    HashMap<String, Vertex> cameFrom;
    ArrayList<Vertex> path;

    MazeWorld() {
        this.reset();
    }

    // resets the initial conditions (helps with onKey commands)
    public void reset() {
        int gameHeight = CANVAS_HEIGHT;
        int gameWidth = CANVAS_WIDTH;
        depthSearch = false;
        breadthSearch = false;
        this.board = this.createVertices();
        this.current = new Posn(0, 0);
        this.end = new NodePosn(AMOUNT_COLUMNS - 1, AMOUNT_ROWS - 1);
        this.start = new NodePosn(0, 0);
        //this.board = this.createNodes(gameWidth, gameHeight);
        this.findUnion = new UnionFind(this.mapN(this.board));
        this.rand = new Random();
        this.worklist = this.makeLinks(this.board);
        this.edges = this.makeTree(this.worklist, this.findUnion);
        //this.board.get(0).get(0).playerHere = true;
        this.updateNodes(edges); 
        this.p = new Player(this.board.get(0).get(0));
        this.gameOver = false;
        this.depthComplete = false;
        this.breadthComplete = false;
        this.searchDeque = new Deque<Vertex>(this.searchSent);
        this.searchSent = new Sentinel<Vertex>();
        this.searchNode = new Node<Vertex>(p.current, this.searchSent, 
                this.searchSent); 
        this.initSearch(edges);
        this.breadthQueue = new Queue<Vertex>(this.searchDeque);
        this.depthStack = new Stack<Vertex>(this.searchDeque);
        this.cameFrom = this.searchMap(this.board);
        this.path = new ArrayList<Vertex>();
    }


    /** ---------------------- MAKING THE BOARD ---------------------------**/


    //turn the ArrayList<ArrayList<Cell>> into an IList
    IList<Vertex> makeBoardList(ArrayList<ArrayList<Vertex>> vertices) {
        IList<Vertex> result = new MtList<Vertex>();
        for (int index = 0; index < vertices.size(); index += 1) {
            for (int index2 = 0; index2 < vertices.get(index).size(); 
                    index2 += 1) {
                result = result.add(vertices.get(index).get(index2));
            }   
        }
        return result;
    }


    //ArrayList<ArrayList<Double>> --> ArrayList<ArrayList<Cell>>
    ArrayList<ArrayList<Vertex>> createVertices() {
        ArrayList<ArrayList<Vertex>> result = new ArrayList<ArrayList<Vertex>>();
        for (int index = 0; index < AMOUNT_ROWS; index += 1) {
            ArrayList<Vertex> row = new ArrayList<Vertex>();
            result.add(row);        
            for (int index2 = 0; index2 < AMOUNT_COLUMNS; index2 += 1) {
                result.get(index).add(new Vertex(index2, index));
            }                   
        }
        this.connectVertices(result);
        return result;
    }
    //create the game by creating all the nodes and link them together
    ArrayList<ArrayList<Vertex>> createNodes(int width, int height) {
        ArrayList<ArrayList<Vertex>> grid = new ArrayList<ArrayList<Vertex>>();
        
        for (int index = 0; index < height; index += 1) {
            ArrayList<Vertex> row = new ArrayList<Vertex>();
            grid.add(row);
            for (int index2 = 0; index2 < width; index2 += 1) {
                grid.get(index).add(new Vertex(index2, index));   
            }
        }     
        this.connectVertices(grid);
        return grid;
    }
    

    // connect all the cells in an ArrayList<ArrayList<Cells>> cells
    // EFFECT: change left, right, top and bottom cells of each cell in cells
    public void connectVertices(ArrayList<ArrayList<Vertex>> vertices) {
        for (int index = 0; index < AMOUNT_ROWS; index += 1) {           
            for (int index2 = 0; index2 < AMOUNT_COLUMNS; index2 += 1) {                
                Vertex current = vertices.get(index).get(index2);

                if (index2 > 0) {
                    current.newLeft(vertices.get(index).get(index2 - 1));
                }
                else {
                    current.newLeft(current);
                }
                if (index2 < vertices.size() - 1) {
                    current.newRight(vertices.get(index).get(index2 + 1));
                }
                else {
                    current.newRight(current);
                }
                if (index > 0) {
                    current.newTop(vertices.get(index - 1).get(index2));
                }
                else {
                    current.newTop(current);
                }
                if (index < vertices.size() - 1) {
                    current.newBottom(vertices.get(index + 1).get(index2));
                }
                else {
                    current.newBottom(current);
                }
            }           
        }
    }


    /** ------------------------ SETTING UP THE GRAPH ---------------------**/

    //create the hashmap and map nodes to itself
    HashMap<String, String> mapN(ArrayList<ArrayList<Vertex>> vertices) {   
        HashMap<String, String> r = new HashMap<String, String>();

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.get(i).size(); j += 1) {  
                r.put(vertices.get(i).get(j).representative(), 
                        vertices.get(i).get(j).representative());
            }
        }
        return r;
    }

    //create a list of edges to be walls and sort them too
    ArrayList<Edge> makeLinks(ArrayList<ArrayList<Vertex>> v) {
        ArrayList<Edge> result = new ArrayList<Edge>();
        Vertex curN;
        for (int i = 0; i < v.size(); i += 1) {
            for (int j = 0; j < v.get(i).size(); j += 1) {
                curN = v.get(i).get(j);
                //T --> B
                if (i < v.size()) {
                    result.add(new Edge(curN, curN.bottom, 
                            this.rand.nextInt(1000) + 1));   
                }
                else { /* Nothing happens */ }
                //L --> R
                if (j < v.get(i).size()) {
                    result.add(new Edge(curN, curN.right, 
                            this.rand.nextInt(1000) + 1));   
                }
                else { /* Nothing happens */ }
            }
        }  
        Collections.sort(result, new EdgeComparator());
        return result;
    }


    //create the tree of the game choosing the cheapest edges (Kruskal's)
    ArrayList<Edge> makeTree(ArrayList<Edge> source, UnionFind unionfind) {  
        ArrayList<Edge> result = new ArrayList<Edge>();
        int index = 0;
        int allNodes = AMOUNT_COLUMNS * AMOUNT_ROWS;
        Edge curEdge;
        while (result.size() < allNodes && index < source.size()) {
            curEdge = source.get(index);
            String n1 = this.findUnion.find(curEdge.from.representative());
            String n2 = this.findUnion.find(curEdge.to.representative());
            if (unionfind.find(n1).equals(unionfind.find(n2))) {
                // nothing since they are in the same group already
            }
            else {
                result.add(curEdge);
                unionfind.union(n1, n2); 
            }
            index += 1;
        }

        return result;
    }

    //update the node
    void updateNodes(ArrayList<Edge> edges) {
        for (int index = 0; index < edges.size(); index = index + 1) {
            edges.get(index).from.updateEdges(edges.get(index).to);
            edges.get(index).to.updateEdges(edges.get(index).from);
        }
    }
    
    
    /** --------------------- INITILIZE SEARCH -----------------------------**/
    
    // finds the starting node of the worklist given the player's current pos
    // Note: works if the player is at the start or on the correct path,
    //       but can't find the end if the player already goes down a wrong
    //       path since it is already visited
    public Vertex findStart() {
        ArrayList<Edge> work = this.edges;
        for (int i = 0; i < work.size(); i += 1) {
            Vertex v = work.get(i).from;
            Vertex z = work.get(i).to;
            if (v.pos.samePosn(this.p.current.pos)) {
                return v;
            }
            if (z.pos.samePosn(this.p.current.pos)) {
                return z;
            }
        }
        throw new RuntimeException("Can't find starting vertex");
    }
    
    // creates a Queue to initialize the breadth-first search
    // EFFECT: initializes the node, sentinel, and deque
    public void initSearch(ArrayList<Edge> e) {
        this.searchNode = new Node<Vertex>(this.findStart(), this.searchSent,
                this.searchSent);
        this.searchDeque = new Deque<Vertex>(this.searchSent);
        this.cameFrom = this.searchMap(this.board);
    }
    
    //create the hashmap and map nodes to itself
    HashMap<String, Vertex> searchMap(ArrayList<ArrayList<Vertex>> vertices) {   
        HashMap<String, Vertex> r = new HashMap<String, Vertex>();
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.get(i).size(); j += 1) {  
                r.put(vertices.get(i).get(j).representative(), 
                        vertices.get(i).get(j));
            }
        }
        return r;
    }
    
    /** ---------------------- BREADTH SEARCH -----------------------------**/
    
    // conducts a breadth first search on the maze
    public void doBreadth() {
        Queue<Vertex> work = this.breadthQueue;
        if (!work.isEmpty()) {
            Vertex v = work.dequeue();
            if (v.visited) {
                // nothing, already discarded from dequeue
            }
            else if (v.pos.samePosn(this.end)) {
                // returns the reconstructed path
                this.breadthComplete = true;
                this.breadthSearch = false;
                //this.reconstruct(this.cameFrom, v);
            }
            else {
                if (v.topPath) {
                    this.breadthQueue.enqueue(v.top);
                    this.cameFrom.put(v.top.representative(), 
                            v);
                }
                if (v.rightPath) {
                    this.breadthQueue.enqueue(v.right);
                    this.cameFrom.put(v.right.representative(), 
                            v);
                }
                if (v.bottomPath) {
                    this.breadthQueue.enqueue(v.bottom);
                    this.cameFrom.put(v.bottom.representative(), 
                            v);
                }
                if (v.leftPath) {
                    this.breadthQueue.enqueue(v.left);
                    this.cameFrom.put(v.left.representative(), 
                            v);
                }
                v.updateBeenHere();
            }
        }
    }

    
    /** ---------------------- DEPTH SEARCH -----------------------------**/
    
    // conducts a depth first search on the maze
    public void doDepth() {
        Stack<Vertex> work = this.depthStack;
        if (!work.isEmpty()) {
            Vertex v = work.pop();
            if (v.visited) {
                // nothing, already discarded from dequeue
            }
            else if (v.pos.samePosn(this.end)) {
                // returns the reconstructed path
                this.depthComplete = true;
                this.depthSearch = false;
                //this.reconstruct(this.cameFrom, v);
            }
            else {
                if (v.topPath) {
                    this.depthStack.push(v.top);
                    this.cameFrom.put(v.top.representative(), 
                            v);
                }
                if (v.rightPath) {
                    this.depthStack.push(v.right);
                    this.cameFrom.put(v.right.representative(), 
                            v);
                }
                if (v.bottomPath) {
                    this.depthStack.push(v.bottom);
                    this.cameFrom.put(v.bottom.representative(), 
                            v);
                }
                if (v.leftPath) {
                    this.depthStack.push(v.left);
                    this.cameFrom.put(v.left.representative(), 
                            v);
                }
                v.updateBeenHere();
            }
        }
    }

    public void reconstruct(HashMap<String, Vertex> hash, Vertex v) {
        Vertex current = v;
        String key = current.representative();
        while (!(hash.get(key).sameVertex(current))) {
            // sets path to true (changes color of cell)
            current.path = true;
            
            // changes to get the next cell
            Vertex temp = hash.get(key);
            current = temp;
            key = current.representative();
        }
    }
    
    /** ------------------------ RENDERING --------------------------------**/

    // gives the adjusted integer to place on the scene from the cell number
    public int toPixel(int i) {
        return (i * CELL_SIZE) + (CELL_SIZE / 2);
    }

    // creates a cell's background based on if it is visited or not
    public WorldImage makeCellBG(Vertex v) {
        if (v.path) {
            return new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
                    new Color(235, 171, 248, 255));
        }
        else if (v.playerHere) {
            return new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
                    new Color(92, 235, 123, 255));
        }
        else if (v.pos.samePosn(this.end)) {
            return new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
                    new Color(247, 85, 85, 255));
        }
        else if (v.visited) {
            return new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
                    new Color(190, 244, 253, 255));
        }
        else {
            return new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
                    new Color(212, 212, 212, 255));
        }
    }

    // draws the individual cell
    public WorldImage drawCell(Vertex v) {
        WorldImage cellBG = this.makeCellBG(v);
        WorldImage cell = cellBG;
        if (!(v.topPath)) {
            cell = new OverlayOffsetImage(new LineImage(new Posn(CELL_SIZE, 0), 
                    Color.BLACK), 0.0, (double)(CELL_SIZE / 2), cellBG);
            cellBG = cell;
        }
        if (!(v.bottomPath)) {
            cell = new OverlayOffsetImage(new LineImage(new Posn(CELL_SIZE, 0), 
                    Color.BLACK), 0.0, -(double)(CELL_SIZE / 2), cellBG);
            cellBG = cell;
        }
        if (!(v.leftPath)) {
            cell = new OverlayOffsetImage(new LineImage(new Posn(0, CELL_SIZE), 
                    Color.BLACK), (double)(CELL_SIZE / 2), 0.0, cellBG);
            cellBG = cell;
        }
        if (!(v.rightPath)) {
            cell = new OverlayOffsetImage(new LineImage(new Posn(0, CELL_SIZE), 
                    Color.BLACK), -(double)(CELL_SIZE / 2), 0.0, cellBG);
        }
        return cell;
    }
    
    // displays the win screen on the given WorldScene
    public WorldScene winScreen(WorldScene bg) {
        // places a transparency over the game to show they won
        bg.placeImageXY(new RectangleImage(CANVAS_WIDTH, CANVAS_HEIGHT, 
                OutlineMode.SOLID, new Color(254, 215, 248, 100)), 
                CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
        
        bg.placeImageXY(new TextImage("MAZE COMPLETED", CANVAS_WIDTH / 10, 
                Color.WHITE), CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
        bg.placeImageXY(new TextImage("Press R to Restart a New Maze", 
                CANVAS_WIDTH / 20, Color.WHITE), CANVAS_WIDTH / 2, 
                CANVAS_HEIGHT / 2 + CANVAS_WIDTH / 10);
        return bg;
    }

    // creates the scene of the game
    public WorldScene makeScene() {
        // sets the base scene
        WorldScene bg = new WorldScene(CANVAS_WIDTH, CANVAS_HEIGHT);

        // draws the edges
        for (int i = 0; i < this.edges.size(); i += 1) {
            Vertex v = this.edges.get(i).from;
            Vertex z = this.edges.get(i).to;
            bg.placeImageXY(this.drawCell(v), 
                    (v.pos.x * CELL_SIZE) + (CELL_SIZE / 2) , 
                    (v.pos.y * CELL_SIZE) + (CELL_SIZE / 2));
            bg.placeImageXY(this.drawCell(z), 
                    (z.pos.x * CELL_SIZE) + (CELL_SIZE / 2) , 
                    (z.pos.y * CELL_SIZE) + (CELL_SIZE / 2));
        }
        
        // Instructions
        bg.placeImageXY(new TextImage("R: Reset    B: Breadth Search    "
                + "D: Depth Search    Arrows: Move", CANVAS_WIDTH / 35
                , Color.WHITE), CANVAS_WIDTH / 2 , CANVAS_HEIGHT * 29 / 30);
        
        
        // draws the breadth first search SOLUTION
        if (this.breadthComplete) {
            // places a transparency over the game to show they won
            bg.placeImageXY(new RectangleImage(CANVAS_WIDTH, CANVAS_HEIGHT, 
                    OutlineMode.SOLID, new Color(254, 215, 248, 100)), 
                    CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
            
            bg.placeImageXY(new TextImage("BREADTH COMPLETED", 
                    CANVAS_WIDTH / 11, Color.WHITE), CANVAS_WIDTH / 2, 
                    CANVAS_HEIGHT / 2);
            bg.placeImageXY(new TextImage("Press R to Restart a New Maze", 
                    CANVAS_WIDTH / 20, Color.WHITE), CANVAS_WIDTH / 2, 
                    CANVAS_HEIGHT / 2 + CANVAS_WIDTH / 10);
            return bg;
        }
        
        // draws the depth first search SOLUTION
        if (this.depthComplete) {
            // places a transparency over the game to show they won
            bg.placeImageXY(new RectangleImage(CANVAS_WIDTH, CANVAS_HEIGHT, 
                    OutlineMode.SOLID, new Color(254, 215, 248, 100)), 
                    CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
            
            bg.placeImageXY(new TextImage("DEPTH COMPLETED", CANVAS_WIDTH / 11, 
                    Color.WHITE), CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
            bg.placeImageXY(new TextImage("Press R to Restart a New Maze", 
                    CANVAS_WIDTH / 20, Color.WHITE), CANVAS_WIDTH / 2, 
                    CANVAS_HEIGHT / 2 + CANVAS_WIDTH / 10);
            return bg;
        }
        
        if (this.gameOver) {
            this.winScreen(bg);
        }
        return bg;
    }

    /** --------------------- ON KEY MECHANICS ----------------------------**/

    // returns true if any of the game ending scenarios are present
    public boolean endScenarios() {
        return this.breadthComplete || this.depthComplete || this.gameOver;
    }
    
    public void onKeyEvent(String ke) {

        // DOESNT WORK YET (THROWS AN EXCEPTION???)
        //Vertex currentVert = this.board.get(this.current.x).get(this.current.y);

        // creates a random maze
        if (ke.equals("r")) {
            this.reset();
        }
        //breadth first
        if (ke.equals("b") && !this.depthSearch && !this.endScenarios()) {
            this.initSearch(this.edges);
            this.breadthSearch = true;
        }
        //depth first
        if (ke.equals("d") && !this.breadthSearch && !this.endScenarios()) {
            this.initSearch(this.edges);
            this.depthSearch = true;
        }
        //move left
        if (ke.equals("left") && p.ableToMove("left") && 
                !(p.current.left.equals(p.current)) && !this.endScenarios()) {
            this.p.current.updateBeenHere();
            this.p.newCell(p.current.left);
        }
        //move right
        if (ke.equals("right") && p.ableToMove("right") && 
                !(p.current.right.equals(p.current)) && !this.endScenarios()) {
            this.p.current.updateBeenHere();
            this.p.newCell(p.current.right);
            
        } 
        if (ke.equals("up") && p.ableToMove("up") && 
                !(p.current.top.equals(p.current)) && !this.endScenarios()) {
            this.p.current.updateBeenHere();
            this.p.newCell(p.current.top);
        }
        if (ke.equals("down") && p.ableToMove("down") && 
                !(p.current.bottom.equals(p.current)) && !this.endScenarios()) {
            this.p.current.updateBeenHere();
            this.p.newCell(p.current.bottom);
        }
        else {
            return;
        }
    }
    
    
    //on tick
    public void onTick() {
        if (this.p.current.pos.samePosn(this.end)) {
            this.gameOver = true;
        }
        if (this.breadthSearch && !this.breadthComplete) {
            this.doBreadth();
        }
        if (this.depthSearch && !this.depthComplete) {
            this.doDepth();
        }
    }

}

class ExamplesMazeGameWorld {
    MazeWorld w0;
    MazeWorld w1;
    ArrayList<Edge> arr;
    ArrayList<ArrayList<Vertex>> arr1;
    ArrayList<ArrayList<Vertex>> arr2;

    NodePosn posn1 = new NodePosn(12, 13);
    NodePosn posn2 = new NodePosn(12, 13);
    NodePosn posn3 = new NodePosn(13, 13);

    Vertex v1;
    Vertex v2;
    Vertex v3;
    Vertex v4;
    Vertex v5;

    Vertex v6;
    Vertex v7;
    Vertex v8;
    Vertex v9;

    Edge e1;
    Edge e2;
    Edge e3;
    Edge e4;

    HashMap<String, String> hashM;
    EdgeComparator eComp = new EdgeComparator();
    UnionFind uf;
    
    Player p;


    // initializes the maze as a new MazeWorld() (basically resets the maze)
    void initMaze() {
        w0 = new MazeWorld();
        w1 = new MazeWorld();
        arr1 = w0.createVertices();

        v1 = new Vertex(12, 13);
        v2 = new Vertex(12, 12);
        v3 = new Vertex(12, 11);
        v4 = new Vertex(13, 12);
        v5 = new Vertex(11, 12);

        v6 = new Vertex(0, 0);
        v7 = new Vertex(1, 0);
        v8 = new Vertex(0, 1);
        v9 = new Vertex(1, 1);

        v6.newBottom(v8);
        v6.newTop(v6);
        v6.newRight(v7);
        v6.newLeft(v6);

        v7.newBottom(v9);
        v7.newTop(v7);
        v7.newRight(v7);
        v7.newLeft(v6);

        v8.newBottom(v8);
        v8.newTop(v6);
        v8.newRight(v9);
        v8.newLeft(v8);

        v9.newBottom(v9);
        v9.newTop(v7);
        v9.newRight(v9);
        v9.newLeft(v8);

        e1 = new Edge(v2, v2, 2);
        e2 = new Edge(v2, v4, 7);
        e3 = new Edge(v2, v3, 4);
        e4 = new Edge(v2, v5, 1);


        hashM = new HashMap<String, String>();
        uf = new UnionFind(hashM);



    }
    /*
    //test the method samePosn
    void testSamePosn(Tester t) {
        t.checkExpect(this.posn1.samePosn(this.posn2), true);
        t.checkExpect(this.posn2.samePosn(this.posn3), false);
    }
    // used to see if there are n-1 edges with n vertexes
    void countEdges(Tester t) {
        this.initMaze();
        t.checkExpect(this.w0.makeLinks(this.w0.board), 
                this.w0.AMOUNT_COLUMNS * this.w0.AMOUNT_ROWS);
    }
    //test the method newTop
    void testNewTop(Tester t) {
        this.initMaze();
        t.checkExpect(this.v2.top, null);
        this.v2.newTop(this.v5);
        t.checkExpect(this.v2.top, v5);
    }
    //test the method newBottom
    void testNewBottom(Tester t) {
        this.initMaze();
        t.checkExpect(this.v3.bottom,  null);
        this.v3.newBottom(this.v4);
        t.checkExpect(this.v3.bottom, this.v4);
    }
    //test the method newRight
    void testNewRight(Tester t) {
        this.initMaze();
        t.checkExpect(this.v2.right, null);
        this.v2.newRight(this.v5);
        t.checkExpect(this.v2.right, this.v5);
    }
    //test the method newLeft
    void testNewLeft(Tester t) {
        this.initMaze();
        t.checkExpect(this.v2.left, null);
        this.v2.newLeft(this.v4);
        t.checkExpect(this.v2.left, this.v4);
    }
    //test the method updateEdges
    void testUpdateEdges(Tester t) {
        this.initMaze();
        t.checkExpect(!v2.bottomPath, true);
        t.checkExpect(!v2.topPath, true);
        t.checkExpect(!v2.leftPath, true);
        t.checkExpect(!v2.rightPath, true);
        this.v2.newBottom(v1);
        this.v2.newTop(v5);
        this.v2.newRight(v4);
        this.v2.newLeft(v3);
        this.v2.updateEdges(v1);
        t.checkExpect(this.v2.bottomPath, true);
        this.v2.updateEdges(this.v5);
        t.checkExpect(this.v2.topPath, true);
        this.v2.updateEdges(this.v4);
        t.checkExpect(this.v2.rightPath, true);
        this.v2.updateEdges(this.v3);
        t.checkExpect(this.v2.leftPath, true);

    }
    //test the EdgeComparator
    void testEdgeComparator(Tester t) {
        this.initMaze();
        e1 = new Edge(v2, v1, 2);
        e2 = new Edge(v2, v4, 7);
        e3 = new Edge(v2, v3, 4);
        e4 = new Edge(v2, v5, 1);
        t.checkExpect(this.eComp.compare(e1, e2), -5);
    }
    //test the makeTree method
    void testMakeTree(Tester t) {
        this.initMaze();
        this.w0.board = arr1;
        t.checkExpect(this.w0.equals(w0), true);
    }
    //test the method toPixel 
    void testToPixel(Tester t) {
        this.initMaze();
        t.checkExpect(w0.toPixel(1), 15);
        t.checkExpect(w0.toPixel(10), 105);
    }
    //test the method mapN
    void testMapN(Tester t) {
        this.initMaze();
        hashM = w0.mapN(arr1);
        t.checkExpect(hashM.get(arr1.get(1).get(1).representative()), 
                v9.representative());
    }
    //test the method updateLeftPath
    void testUpdateLeftPath(Tester t) {
        this.initMaze();
        t.checkExpect(!v2.leftPath, true);
        this.v2.updateLeftPath(true);
        t.checkExpect(this.v2.leftPath, true);
    }
    //test the method updateRightPath
    void testUpdateRightPath(Tester t) {
        this.initMaze();
        t.checkExpect(!this.v2.rightPath, true);
        this.v2.updateRightPath(true);
        t.checkExpect(this.v2.rightPath, true);
    }
    //test the method updateBottomPath
    void testUpdateBottomPath(Tester t) {
        this.initMaze();
        t.checkExpect(!this.v2.bottomPath, true);
        this.v2.updateBottomPath(true);
        t.checkExpect(this.v2.bottomPath, true);
    }
    //test the method updateTopPath
    void testUpdateTopPath(Tester t) {
        this.initMaze();
        t.checkExpect(!this.v2.topPath, true);
        this.v2.updateTopPath(true);
        t.checkExpect(this.v2.topPath, true);
    }
    //test the method sameVertex
    void testSameVertex(Tester t) {
        this.initMaze();
        t.checkExpect(this.v1.sameVertex(this.v2), false);
        t.checkExpect(this.v1.sameVertex(this.v1), true);
    }
    //test the method sameEdge
    void testSameEdge(Tester t) {
        this.initMaze();
        t.checkExpect(this.e1.sameEdge(this.e2), false);
        t.checkExpect(this.e1.sameEdge(this.e1), true);
    }
    //test the method equals
    void testEquals(Tester t) {
        this.initMaze();
        t.checkExpect(this.v3.equals(this.v4), false);
        t.checkExpect(this.v6.equals(this.v6), true);
        t.checkExpect(this.v4.equals(this.v5), false);
        t.checkExpect(this.v8.equals(this.v8), true);
    }
    //test the method hashCode
    void testHashCode(Tester t) {
        this.initMaze();
        t.checkExpect(this.v6.hashCode(), 0);
        t.checkExpect(this.v7.hashCode(), 1000);

        t.checkExpect(this.e1.hashCode(), 24240);
        t.checkExpect(this.e4.hashCode(), 12120);
    }
    //test drawCell method
    void testMakeCellBG(Tester t) {
        this.initMaze();
        t.checkExpect(w0.makeCellBG(v1), new RectangleImage(w0.CELL_SIZE, 
                w0.CELL_SIZE, OutlineMode.SOLID, new Color(212, 212, 212, 255)));
        this.v2.visited = true;
        t.checkExpect(w0.makeCellBG(this.v2), new RectangleImage(w0.CELL_SIZE, 
                w0.CELL_SIZE, OutlineMode.SOLID, new Color(190, 244, 253, 255)));
        this.v3.playerHere = true;
        t.checkExpect(w0.makeCellBG(this.v3), new RectangleImage(w0.CELL_SIZE, 
                w0.CELL_SIZE, OutlineMode.SOLID, new Color(92, 235, 123, 255)));
    }
    //test the method abelToMove
    void testAbleToMove(Tester t) {
        this.initMaze();
        p = new Player(v1);
        p.newCell(v2);
        t.checkExpect(p.ableToMove("up"), false);
        t.checkExpect(p.ableToMove("down"), false);
        t.checkExpect(p.ableToMove("left"), false);
        t.checkExpect(p.ableToMove("right"), false);
        v2.updateRightPath(true);
        v2.updateLeftPath(true);
        v2.updateTopPath(true);
        v2.updateBottomPath(true);
        t.checkExpect(p.ableToMove("up"), true);
        t.checkExpect(p.ableToMove("down"), true);
        t.checkExpect(p.ableToMove("right"), true);
        t.checkExpect(p.ableToMove("left"), true);
    }
    //test the method newCell
    void testNewCell(Tester t) {
        this.initMaze();
        p = new Player(v1);
        t.checkExpect(p.current, v1);
        p.newCell(v5);
        t.checkExpect(p.current, v5);
    }
    /*
    //test onTick
    void testOnTick(Tester t) {
        this.initMaze();
        w0.onTick();
    }
    */
    /*
    //test onKeyEvents
    void testOnKey(Tester t) {
        this.initMaze();
        w0.board = arr2;
        w0.p.newCell(v6);
        this.w0.onKeyEvent("b");
        t.checkExpect(w0.breadthSearch, true);
        w0.breadthSearch = false;
        this.w0.onKeyEvent("d");
        t.checkExpect(w0.depthSearch, true);
        this.w0.onKeyEvent("r");
        t.checkExpect(w0.p.current.pos.samePosn(new NodePosn(0, 0)), true);
        this.w0.onKeyEvent("left");
        t.checkExpect(w0.p.current.pos.samePosn(new NodePosn(0, 0)), true);
        this.w0.onKeyEvent("up");
        t.checkExpect(w0.p.current.pos.samePosn(new NodePosn(0, 0)), true);
    }
    */
    
    /*
    //test the method updateNodes
    void testUpdateNodes(Tester t) {
        this.initMaze();
        arr = w0.makeLinks(arr2);
        w0.board = arr2;
        w0.updateNodes(arr);
        t.checkExpect(w0.board.get(0).get(0).bottomPath, true);
    }
     */
    /*
    //test the method makeLinks
    void testMakeLinks(Tester t) {
        this.initMaze();
        Vertex v = new Vertex(0, 0);
        v.newBottom(v);
        v.newTop(v);
        v.newRight(v);
        v.newLeft(v);
        t.checkExpect(w0.makeLinks(arr2).get(0).from, v);
        t.checkExpect(w0.makeLinks(arr2).get(0).to, v);
        t.checkRange(w0.makeLinks(arr2).get(0).weight, 0, 1000);
    }
     */
    

    // Runs the game
    void testBigBang(Tester t) {
        this.initMaze();
        this.w0.bigBang(this.w0.CANVAS_WIDTH, this.w0.CANVAS_HEIGHT, .01);
    }


}







