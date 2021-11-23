import java.awt.event.WindowStateListener;
import java.util.ArrayList;

public class RegNet
{
    //creates a regional network
    //G: the original graph
    //max: the budget

    static LinkedList list = new LinkedList();
    static int counter = 0;



    public static Graph run(Graph G, int max)
    {

	    //TODO

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


        mst = mst.connGraph();

        int[][] stops = AllPairs(mst);


        //step 3


        //boolean[] visited = new boolean[mst.V()];
        //mst = add(G, mst, max, visited);


        int c = mst.V() * mst.V();
        while (c >= 0) {
            int maxStops = 0;
            int stopsI = 0;
            int stopsJ = 0;
            for (int i = 0; i < mst.V(); i++) {
                for (int j = 0; j < mst.V(); j++) {
                    if (stops[i][j] != 0 && stops[i][j] > maxStops) {
                        maxStops = stops[i][j];
                        stopsI = i;
                        stopsJ = j;
                    } else if (stops[i][j] != 0 && stops[i][j] == maxStops) {
                        if (G.getEdgeWeight(stopsI, stopsJ) > G.getEdgeWeight(i, j)) {
                            maxStops = stops[i][j];
                            stopsI = i;
                            stopsJ = j;
                        }
                    }
                }
            }
            if (G.getEdgeWeight(stopsI, stopsJ) != 0) {
                list = insert(list, stopsI, stopsJ, G.getEdgeWeight(stopsI, stopsJ));
                stops[stopsI][stopsJ] = 0;
                stops[stopsJ][stopsI] = 0;

            }
            c--;
        }



        LinkedList.Node node = list.head;
        while (node.next != null) {
            if (mst.totalWeight() + node.Weight <= max) {
                mst.addEdge(node.Start, node.End, node.Weight);
            }
            node = node.next;
        }

        return mst;
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


    private static int[][] AllPairs(Graph g) {

        int[][] D = new int[g.V()][g.V()];


        for (int i = 0; i < g.V(); i++) {
            for (int j = 0; j < g.V(); j++) {
                if (g.getMatrix()[i][j] != null) {
                    D[i][j] = 1;
                } else if (i == j) {
                    D[i][j] = 0;
                } else {
                    D[i][j] = Integer.MAX_VALUE/2;
                }
            }
        }

        for (int k = 0; k < g.V(); k++) {
            for (int i = 0; i < g.V(); i++) {
                for (int j = 0; j < g.V(); j++) {
                    D[i][j] = Math.min(D[i][j], D[i][k] + D[k][j]);
                }
            }
        }

        for (int i = 0; i < g.V(); i++) {
            for (int j = 0; j < g.V(); j++) {
                if (D[i][j] == 1) {
                    D[i][j] = 0;
                }
            }
        }

        return D;
    } //end all pairs

    public static LinkedList insert(LinkedList list, int i, int j, int weight) {

        LinkedList.Node node = new LinkedList.Node(i, j, weight);
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
        return list;
    } //end insert


//    public static Graph add(Graph og, Graph mst, int max, boolean[] visited) {
//        int c = 0;
//        while (c < mst.V()) {
//            c++;
//        }
//
//        LinkedList.Node node = list.head;
//
//        return mst;
//    }

} //end RegNet