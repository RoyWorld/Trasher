package com.trasher.algorithm;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by RoyChan on 2018/1/8.
 */
public class AOVNetwork {
    private int V;//no and tag of vertices
    private LinkedList<Integer> adjacency[];//adjacency list


    public AOVNetwork(int v) {
        V = v;
        adjacency = new LinkedList[v];

        for (int i = 0; i < v; i++) {
            adjacency[i] = new LinkedList<>();
        }
    }

    /**
     * add an edge from v to w
     * @param v
     * @param w
     */
    public void addEdge(int v, int w){
        adjacency[v].add(w);
    }

    /**
     * add edge from v to w1, w2, w3, ....
     * @param v
     * @param w
     */
    public void addEdges(int v, int... w){
        for (int ww : w) {
            adjacency[v].add(ww);
        }
    }

    public List<Integer> topologicalSorting(){
        LinkedList<Integer> sortedNodes = new LinkedList<>();//L, nodes that have bean sorted
        Set<Integer> zeroIndegreeNodes = new TreeSet<>();//S, nodes with no incoming edge

        LinkedList<Integer> transposeOfAdjacency[] = new LinkedList[V];//transpose of adjacency list

        //transpose of adjacency init
        IntStream.range(0, transposeOfAdjacency.length)
                .forEach(n -> {
                    transposeOfAdjacency[n] = new LinkedList<>();
                });
        //tranform adjacency to transpose of adjacency
        IntStream.range(0, transposeOfAdjacency.length)
                .forEach(n -> {
                    adjacency[n].stream().forEach(m ->{
                        transposeOfAdjacency[m].add(n);
                    });
                });

        //zeroIndegreeNodes init
        IntStream.range(0, transposeOfAdjacency.length)
                .filter(i -> transposeOfAdjacency[i].size() == 0)
                .forEach(i -> {
                    zeroIndegreeNodes.add(i);
                });
        //judge cycles
        if (zeroIndegreeNodes.isEmpty()){
            System.out.println("AOVNetwork has cycles");
            return null;
        }
        //Kahn's algorithm begin
        Iterator<Integer> iterator = zeroIndegreeNodes.iterator();
        while (iterator.hasNext()){
            Integer n = iterator.next();
            iterator.remove();//remove a node n from S

            sortedNodes.addLast(n);//add n to tail of L

            Iterator<Integer> mIterator = adjacency[n].iterator();
            while (mIterator.hasNext()){
                Integer m = mIterator.next();
                mIterator.remove();//remove edge e from the graph
                transposeOfAdjacency[m].remove(n);//remove edge e from the graph

                //judge does m has no other incoming edges
                if (transposeOfAdjacency[m].size() == 0){
                    zeroIndegreeNodes.add(m);//insert m into S
                }
            }

            //reassign to iterator
            iterator = zeroIndegreeNodes.iterator();
        }

        //judge does graph has edges
        for (int i = 0; i < adjacency.length; i++) {
            if (adjacency[i].size() != 0){
                System.out.println("AOVNetwork has cycles");
                return null;
            }
        }

        return sortedNodes;
    }


    @Override
    public String toString() {
        return "AOVNetwork{" +
                "V=" + V +
                ", adjacency=" + Arrays.toString(adjacency) +
                '}';
    }
}
