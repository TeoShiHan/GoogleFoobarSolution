package com.company;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.Collections;

public class Solution {
    public static int solution(int[] banana_list) {
        Graph graph = new Graph();
        graph.convertToMaximalMatching();
        graph.createGraph(banana_list);
        Set<Vertex> unmatchedVertices = graph.vertices.unmatchedSubset;
        boolean possibleToImproveMatching = unmatchedVertices.size() > 1;
        if (possibleToImproveMatching)
        {
            ArrayList<Vertex> unvisitedList = new ArrayList<Vertex>(unmatchedVertices);
            for (Vertex vertex : unvisitedList)
            {
                if (!vertex.isMatched())
                {
                    Search result = graph.exploreAugPath(vertex);
                    graph.improveMatching(result);
                }
            }
        }
        return (unmatchedVertices.size());
    }

    public static void main(String[] args) {
        System.out.println(Solution.solution(new int[]{1, 1, 2, 4, 8, 9, 1, 1, 2, 4, 8, 9, 1, 1, 2, 4, 8, 9, 1, 1, 2, 4, 8, 9, 1, 1, 2, 4, 8, 9}));
    }
}

class Vertex
{
    final int value;
    Set<Vertex> matchedVertex = new HashSet<Vertex>();
    HashMap<Vertex, Edge> edges = new HashMap<Vertex, Edge>();

    public Vertex(int value)
    {
        this.value = value;
    }

    public void setEdge(Edge edge)
    {
        Vertex adjacentVertex = edge.getOppositeVertex(this);
        edges.put(adjacentVertex, edge);
        adjacentVertex.edges.put(this, edge);
    }

    public Edge getEdgeTo(Vertex destinationVertex) {
        return edges.get(destinationVertex);
    }

    public Set<Vertex> getAdjacentVertices()
    {
        return edges.keySet();
    }

    boolean isMatched(){
        return  !matchedVertex.isEmpty();
    }

    boolean isMatchWith(Vertex vertex){
        return matchedVertex.contains(vertex);
    }
}

class Graph
{
    VerticesSet vertices = new VerticesSet();

    public void createGraph(int[] trainersAsset)
    {
        List<Vertex> vertexList = new ArrayList<>();
        for (int bananaQty : trainersAsset)
        {
            Vertex newVertex = new Vertex(bananaQty); // will auto add itself to graph in constructor
            vertexList.add(newVertex);
            for (Vertex vertex : vertexList)
            {
                int[] match = new int[]{vertex.value, newVertex.value};
                boolean causingInfiniteBet = InspectPair.causingInfiniteBet(match);
                if (causingInfiniteBet) {
                    addEdge(newVertex, vertex);
                }
            }
        }
    }

    public void match(Vertex vertexA, Vertex vertexB)
    {
        Edge edgeInMatch = vertexA.getEdgeTo(vertexB);
        edgeInMatch.matched = true;
        vertices.balanceRemoveUnmatchedVertices(vertexA, vertexB);
        vertexA.matchedVertex.add(vertexB);
        vertexB.matchedVertex.add(vertexA);
    }

    public void unmatch(Vertex vertexA, Vertex vertexB){
        Edge edgeInMatch = vertexA.getEdgeTo(vertexB);
        edgeInMatch.matched = false;
        vertexA.matchedVertex.remove(vertexB);
        vertexB.matchedVertex.remove(vertexA);

        if (!vertexA.isMatched()){
            vertices.balanceRemoveMatchedVertices(vertexA);
        }

        if (!vertexB.isMatched()){
            vertices.balanceRemoveMatchedVertices(vertexB);
        }
    }

    public void link(Vertex vertex1, Vertex vertex2)
    {
        Edge newEdge = new Edge(vertex1, vertex2);
        vertex1.setEdge(newEdge);
        vertex2.setEdge(newEdge);
    }

    public void addEdge(Vertex vertexA, Vertex vertexB)
    {
        vertices.addVertices(vertexA, vertexB);
        link(vertexA, vertexB);
    }

    public Search exploreAugPath(Vertex initialVertex)
    {
        Search search = new Search(initialVertex);
        Set<Vertex> children;
        Set<Vertex> parents = search.getSearchHistory(0);

        boolean augPathNotFound = true;
        boolean stillHaveChildren = true;

        BlossomVertex blossom;

        while (augPathNotFound && stillHaveChildren){
            search.newLevel();
            for (Vertex parent : parents) if (augPathNotFound)
            {
                children =  search.getVisitableAdjacent(parent);
                for (Vertex child : children) if (augPathNotFound)
                {
                    augPathNotFound = !search.augPathFound(child, parent);
                    if (!augPathNotFound){
                        search.setLastSearchVertex(child);
                    }
                    if ( search.isAltEdge(parent, child))
                    {
                        search.track(child, parent);
                        search.setBlossomIdentifier(child);
                        search.setVertexAsUnvisitable(child);
                        blossom =  search.detectAndExtractBlossom(child,  search.getSearchHistory());

                        if (blossom != null)
                        {
                            Vertex outsideEntrance =  search.getPrecedentVertex(blossom.rootVertex);
                            search.track(blossom, outsideEntrance);
                            search.setBlossomIdentifier(blossom);
                            search.getSearchHistory().add(blossom);
                        }
                    }
                }
            }
            parents =  search.getSearchHistory();
            stillHaveChildren = !parents.isEmpty();
        }
        return search;
    }



    public void improveMatching(Search augSearch)
    {
        Vertex currentVertex = augSearch.getLastSearchVertex();
        List<Vertex> sequence = new ArrayList<>();
        int counter = 0;

        while (currentVertex != null)
        {
            Vertex prevVertex = augSearch.getPrecedentVertex(currentVertex);
            boolean currentVertexIsBlossom = currentVertex.value == -1;

            if (currentVertexIsBlossom){
                BlossomVertex blossom = (BlossomVertex) currentVertex;
                Vertex blossomExit = blossom.getBlossomExit(sequence.get(counter-1));
                List<Vertex> blossomCycle = blossom.rotateVertices(blossomExit);


                for (Vertex blossomV : blossomCycle){
                    remach(blossomV, sequence.get(counter-1));
                    sequence.add(blossomV);

                    ++counter;
                }

            } else {
                sequence.add(currentVertex);

                if (counter > 0){
                    remach(currentVertex, sequence.get(counter-1));
                }
                ++counter;
            }
            currentVertex = prevVertex;
        }
    }

    public void remach(Vertex vertexA, Vertex vertexB)
    {
        if (vertexA.isMatchWith(vertexB)){
            unmatch(vertexA, vertexB);

        }else {
            match(vertexA, vertexB);
        }
    }

    public void convertToMaximalMatching()
    {
        List<Vertex> unmatchedVertexList = new ArrayList<Vertex>(vertices.unmatchedSubset);
        for (Vertex vertex : unmatchedVertexList){
            Set<Vertex> adj = vertex.getAdjacentVertices();

            for (Vertex a : adj) if (!vertex.isMatched())
            {
                if (!a.isMatched())
                {
                    match(a, vertex);
                    break;
                }
            }
        }
    }
}

class Search
{
    private Map<Vertex, Boolean> blossomIdentifier;
    private Map<Vertex, Vertex> backwardTrack;
    private Map<Integer, HashSet<Vertex>> searchHistory;
    private int searchLevel;
    private Vertex lastSearchVertex;



    public Search(Vertex startingVertex)
    {
        this.blossomIdentifier = new HashMap<Vertex, Boolean>();
        this.backwardTrack = new HashMap<Vertex, Vertex>();
        this.searchLevel = 0;
        initSearchHistory(startingVertex);
        setBlossomIdentifier(startingVertex, true);
    }

    public void initSearchHistory(Vertex startingVertex)
    {
        this.searchHistory = new HashMap<Integer, HashSet<Vertex>>()
        {{
            put(-1, new HashSet<Vertex>()); // visited
            put(0, new HashSet<Vertex>());
        }};
        this.searchHistory.get(-1).add(startingVertex);
        this.searchHistory.get(0).add(startingVertex);
    }

    public void newLevel()
    {
        this.searchLevel++;
        searchHistory.put(searchLevel, new HashSet<>());
    }



    public void track(Vertex child, Vertex parent){
        backwardTrack.put(child, parent);
    }

    public HashSet<Vertex> getSearchHistory(){
        return this.searchHistory.get(this.searchLevel);
    }

    public HashSet<Vertex> getSearchHistory(int level){
        return this.searchHistory.get(level);
    }

    public Vertex getPrecedentVertex(Vertex vertex){
        return backwardTrack.get(vertex);
    }

    public Set<Vertex> getVisitableAdjacent(Vertex vertex)
    {
        Set<Vertex> adjacent = new HashSet<Vertex>(vertex.getAdjacentVertices());
        Set<Vertex> unvisitable = new HashSet<Vertex>(getSearchHistory(-1));
        adjacent.removeAll(unvisitable);
        return adjacent;
    }

    public boolean getBlossomIdentifier(Vertex vertex){
        return blossomIdentifier.get(vertex);
    }

    public void setVertexAsUnvisitable(Vertex vertex)
    {
        searchHistory.get(searchLevel).add(vertex);
        searchHistory.get(-1).add(vertex);
    }

    public void setBlossomIdentifier(Vertex vertex, boolean blossomIdentifier){
        this.blossomIdentifier.put(vertex,blossomIdentifier);
    }

    public void setBlossomIdentifier(Vertex vertex)
    {

        if (vertex.value == -1) {
            BlossomVertex vertex1 = (BlossomVertex) vertex;
            if (vertex1.beforeRoot == null){
                setBlossomIdentifier(vertex1, true);
            }else {
                Vertex precedentVertex = getPrecedentVertex(vertex);
                boolean altFlag = !getBlossomIdentifier(precedentVertex);
                blossomIdentifier.put(vertex, altFlag);
            }
        } else {
            Vertex precedentVertex = getPrecedentVertex(vertex);
            boolean altFlag = !getBlossomIdentifier(precedentVertex);
            blossomIdentifier.put(vertex, altFlag);
        }
    }

    public BlossomVertex extractBlossom(Vertex blossomEnd1, Vertex blossomEnd2)
    {
        Vertex root = null;
        Vertex prevA = blossomEnd1;
        Vertex prevB = blossomEnd2;



        boolean rootNotDetected = true;

        List<Vertex>leftWing = new ArrayList<Vertex>();
        Set<Vertex>rightWing = new LinkedHashSet<Vertex>();

        while (rootNotDetected)
        {
            rightWing.add(prevA);
            leftWing.add(prevB);

            prevA = getPrecedentVertex(prevA);
            prevB = getPrecedentVertex(prevB);

            rootNotDetected = prevA != prevB;

            root = prevA;
        }

        Vertex beforeRoot = getPrecedentVertex(root);

        Set<Vertex> blossom = new LinkedHashSet<>();
        Collections.reverse(leftWing);

        blossom.add(root);
        blossom.addAll(leftWing);
        blossom.addAll(rightWing);

        if (blossomNotDetected(blossom)) {
            return null;
        }

        return new BlossomVertex(blossom, beforeRoot);
    }

    public BlossomVertex detectAndExtractBlossom(Vertex blossomEnding, HashSet<Vertex> levelVertices)
    {
        Set<Vertex> blossomEndingAdj = new HashSet<Vertex>(blossomEnding.getAdjacentVertices());
        Set<Vertex> level = new HashSet<Vertex>(levelVertices);

        blossomEndingAdj.retainAll(level);
        BlossomVertex blossomVertex = null;

        for (Vertex vertex : blossomEndingAdj)
        {
            boolean identifier1 =  getBlossomIdentifier(vertex);
            boolean identifier2 = getBlossomIdentifier(blossomEnding);
            boolean twoIdenticalBlossomLabelDetected = identifier1 == identifier2;

            if (twoIdenticalBlossomLabelDetected) {
//                System.out.println(BLOSSOM DETECTED");
                blossomVertex = extractBlossom(vertex, blossomEnding);
            }
        }
        return blossomVertex;
    }

    public boolean augPathFound(Vertex child, Vertex parent)
    {
        boolean moreThan2Edge = searchLevel >= 3;
        boolean vtxNotMatched = !child.isMatched();
        boolean maintainAltEdge = isAltEdge(parent, child);
        return moreThan2Edge && vtxNotMatched && maintainAltEdge;
    }

    public boolean blossomNotDetected(Set<Vertex> blossom) {
        return blossom.size() < 3;
    }

    public boolean isAltEdge(Vertex source, Vertex dest)
    {
        Vertex ancestor = getPrecedentVertex(source);
        boolean haveAncestor = ancestor != null;
        boolean isAlternating;

        if (haveAncestor)
        {
            Edge parentToChild = source.getEdgeTo(dest);
            Edge parentToAncestor = source.getEdgeTo(ancestor);
            isAlternating = parentToChild.matched != parentToAncestor.matched;
        }
        else {
            isAlternating = true;
        }
        return isAlternating;
    }

    public void setLastSearchVertex(Vertex lastSearchVertex){
        this.lastSearchVertex = lastSearchVertex;
    }

    public Vertex getLastSearchVertex() {
        return lastSearchVertex;
    }
}

class InspectPair {

    public static boolean causingInfiniteBet(int[] bets)
    {
        int x = bets[0];
        int y = bets[1];
        int z = (x + y) / gcd(x, y);
        return ((z - 1) & z) != 0;
    }

    public static int gcd(int a, int b){
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
    }

}

class BlossomVertex extends Vertex {
    Set<Vertex> vertices;
    Vertex rootVertex;
    Vertex beforeRoot;

    public BlossomVertex(Set<Vertex> vertices, Vertex beforeRoot)
    {
        super(-1);
        this.vertices = vertices;
        this.rootVertex = vertices.iterator().next(); // first == root
        this.beforeRoot = beforeRoot;
        formingBlossomEdge(beforeRoot);
    }

    public void formingBlossomEdge(Vertex beforeRoot)
    {
        for (Vertex vertex : vertices)
        {
            Set<Vertex> adjacent = new HashSet<Vertex>(vertex.getAdjacentVertices());
            adjacent.removeAll(vertices);

            for (Vertex adj : adjacent)
            {
                Edge blossomEdge = new Edge(this, adj);
                blossomEdge.matched = vertex.getEdgeTo(adj).matched; // blossom rules
                this.setEdge(blossomEdge);
            }
        }
        if (beforeRoot != null){
            Edge sourceEdge = beforeRoot.getEdgeTo(rootVertex);
            Edge blossomEdge = new Edge(this, beforeRoot);
            blossomEdge.matched = sourceEdge.matched;
            this.setEdge(blossomEdge);
        }
    }

    public List<Vertex> rotateVertices(Vertex newStartingVertex)
    {
        // default starting vertex = root
        List<Vertex> defaultCycle = new ArrayList<Vertex>(vertices);
        HashMap<Vertex,Vertex> rotator = new HashMap<Vertex,Vertex>();
        List<Vertex> result = new ArrayList<Vertex>();

        for (int i = 0 ; i < defaultCycle.size() ; i++){
            if (i > 0){
                rotator.put(defaultCycle.get(i), defaultCycle.get(i-1));
            }
        }

        result.add(newStartingVertex);
        while (rotator.get(newStartingVertex) != null){
            result.add(rotator.get(newStartingVertex));
            newStartingVertex = rotator.get(newStartingVertex);
        }

        return result;
    }

    public Vertex getBlossomExit(Vertex firstVertexEncounterAfterExitBlossom)
    {
        Set<Vertex> verticesA = new HashSet<>(firstVertexEncounterAfterExitBlossom.getAdjacentVertices());
        Set<Vertex> verticesB = new HashSet<>(this.vertices);
        verticesA.retainAll(verticesB);
        return  verticesA.iterator().next();
    }

    @Override
    boolean isMatched(){ // blossom vertex always true
        if (beforeRoot != null){
            return true;
        }
        return false;
    }
}

class VerticesSet
{
    HashSet<Vertex> all = new HashSet<Vertex>();
    HashSet<Vertex>matchedSubset = new HashSet<>();
    HashSet<Vertex>unmatchedSubset = new HashSet<>();

    // balance means (subsetA + subsetB == all elements)
    public void balanceRemoveUnmatchedVertices(Vertex ... vertices)
    {
        for (Vertex vertex : vertices)
        {
            unmatchedSubset.remove(vertex);
            matchedSubset.add(vertex);
        }
    }

    public void balanceRemoveMatchedVertices(Vertex ... vertices)
    {
        for (Vertex vertex : vertices)
        {
            matchedSubset.remove(vertex);
            unmatchedSubset.add(vertex);
        }
    }

    public void addVertices(Vertex... vertices)
    {
        for (Vertex vertex : vertices)
        {
            all.add(vertex);
            unmatchedSubset.add(vertex);
        }
    }
}


class Edge
{
    boolean matched;
    HashMap<Vertex, Vertex> relativeVertex = new HashMap<Vertex, Vertex>();

    public Edge(Vertex vertexA, Vertex vertexB)
    {
        this.matched = false;
        relativeVertex.put(vertexA, vertexB); // like bi-map
        relativeVertex.put(vertexB, vertexA);
    }

    public Vertex getOppositeVertex(Vertex vertex) {
        return relativeVertex.get(vertex);
    }
}