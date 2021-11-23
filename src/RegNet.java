import java.awt.event.WindowStateListener;
import java.util.ArrayList;

public class RegNet
{
    //creates a regional network
    //G: the original graph
    //max: the budget

    static LinkedList list = new LinkedList();
    static int counter = 0;
    static Graph original;



    public static Graph run(Graph G, int max)
    {

	    //TODO
        original = G;
        //step 1
        Graph mst = kruskal(G);

        //step 2
        int last = mst.sortedEdges().size();

        for (int i = last - 1; i > 0; i--) {
            if (mst.totalWeight() > max) {
                String u = mst.sortedEdges().get(i).u;
                String v = mst.sortedEdges().get(i).v;

                if (mst.deg(u) == 1) {
                    mst.removeEdge(mst.getEdge(mst.sortedEdges().get(i).ui(), mst.sortedEdges().get(i).vi()));
                } else if (mst.deg(v) == 1) {
                    mst.removeEdge(mst.getEdge(mst.sortedEdges().get(i).ui(), mst.sortedEdges().get(i).vi()));
                }
            }
        }


        //int[][] stops = AllPairs(mst);


        //step 3




        for (int i = 0; i < mst.V(); i++) {
            boolean[] visited = new boolean[mst.V()];
            AllPairs(mst, i, visited, mst.adj(i), 0);
        }


//        int c = mst.V() * mst.V();
//        while (c >= 0) {
//            int maxStops = 0;
//            int stopsI = 0;
//            int stopsJ = 0;
//            for (int i = 0; i < mst.V(); i++) {
//                for (int j = 0; j < mst.V(); j++) {
//                    if (stops[i][j] != 0 && stops[i][j] > maxStops) {
//                        maxStops = stops[i][j];
//                        stopsI = i;
//                        stopsJ = j;
//                    } else if (stops[i][j] != 0 && stops[i][j] == maxStops) {
//                        if (G.getEdgeWeight(stopsI, stopsJ) > G.getEdgeWeight(i, j)) {
//                            maxStops = stops[i][j];
//                            stopsI = i;
//                            stopsJ = j;
//                        }
//                    }
//                }
//            }
//            if (G.getEdgeWeight(stopsI, stopsJ) != 0) {
//                list = insert(list, stopsI, stopsJ, G.getEdgeWeight(stopsI, stopsJ));
//                stops[stopsI][stopsJ] = 0;
//                stops[stopsJ][stopsI] = 0;
//
//            }
//            c--;
//        }


        LinkedList.Node node = list.head;
        while (node.next != null) {
            if (mst.totalWeight() + G.getEdgeWeight(node.Start, node.End) <= max) {
                insert(mst, list, node.Start, node.End, node.Dist);
            }
            node = node.next;
        }

        return mst.connGraph();
    }

    /*
            Kruskal's
     */

    private static Graph kruskal(Graph g){

        Graph T = new Graph(g.V());
        Graph og = g.subgraph(g.getCodes());
        T.setCodes(g.getCodes());

        UnionFind unionFind = new UnionFind(g.V());

        while (T.E() < og.V() - 1) {
            int u = og.sortedEdges().get(0).ui();
            int v = og.sortedEdges().get(0).vi();
            if (unionFind.find(u) != unionFind.find(v)) {
                T.addEdge(og.sortedEdges().get(0).u, og.sortedEdges().get(0).v , og.sortedEdges().get(0).w);
                unionFind.union(u,v);
            }
            og.removeEdge(og.sortedEdges().get(0));
        } //end while
        return T;
    } //end kruskal


//    private static int[][] AllPairs(Graph g) {
//
//        int[][] D = new int[g.V()][g.V()];
//
//
//        for (int i = 0; i < g.V(); i++) {
//            for (int j = 0; j < g.V(); j++) {
//                if (g.getMatrix()[i][j] != null) {
//                    D[i][j] = 1;
//                } else if (i == j) {
//                    D[i][j] = 0;
//                } else {
//                    D[i][j] = Integer.MAX_VALUE/2;
//                }
//            }
//        }
//
//        for (int k = 0; k < g.V(); k++) {
//            for (int i = 0; i < g.V(); i++) {
//                for (int j = 0; j < g.V(); j++) {
//                    D[i][j] = Math.min(D[i][j], D[i][k] + D[k][j]);
//                }
//            }
//        }
//
//        for (int i = 0; i < g.V(); i++) {
//            for (int j = 0; j < g.V(); j++) {
//                if (D[i][j] == 1) {
//                    D[i][j] = 0;
//                }
//            }
//        }
//
//        return D;
//    } //end all pairs

    public static LinkedList insert(Graph mst, LinkedList list, int i, int j, int dist) {

        if (dist == 0)
            return list;

        LinkedList.Node node = new LinkedList.Node(i, j, dist);
        node.next = null;

        if (list.head == null) {
            list.head = node;
        } else{
            LinkedList.Node last = list.head;

            while (last.next != null) {
                last = last.next;
            }

            last.next = node;
        }

        counter++;

        LinkedList.Node current = list.head;
        LinkedList.Node index = null;

        while (current != null) {
            index = current.next;

            while (index != null) {
                if (current.Dist < index.Dist) {
                    LinkedList.Node temp = null;
                    temp = current;
                    current = index;
                    index = temp;
                }
                index = index.next;
            }
            current = current.next;
        }


        return list;
    } //end insert


    public static void AllPairs(Graph mst, int v, boolean[] visited, ArrayList<Integer> adj, int dist) {
        LinkedList.Node node = list.head;
        visited[v] = true;
        for (int i = 0; i < adj.size(); i++) {
            if (!visited[i]) {
                boolean dupe = false;
                while (node != null) {
                    if (node.Start == v && node.End == i || node.Start == i && node.End == v) {
                        dupe = true;
                        break;
                    }
                    node = node.next;
                }
                if (!dupe) {
                    insert(mst, list, v, i, dist);
                }
                visited[i] = true;
                AllPairs(mst, v, visited, mst.adj(i), dist++);
            }
        }
    }

} //end RegNet