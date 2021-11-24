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

        //step 3


        int[][] stops = AllPairs(mst);

        for (int i = 0; i < mst.V(); i++) {
            for (int j = 0; j < mst.V(); j++) {
                if (stops[i][j] != 0 && stops[i][j] != Integer.MAX_VALUE/2) {
                    insert(list, i, j, stops[i][j]);
                    stops[i][j] = 0;
                    stops[j][i] = 0;
                }
            }
        }

        sortList(list);



        LinkedList.Node node = list.head;
        while (node.next != null) {
            if (mst.totalWeight() + G.getEdgeWeight(node.Start, node.End) <= max) {
                mst.addEdge(G.getEdge(G.index(G.getCode(node.Start)), G.index(G.getCode(node.End))));
            }
            node = node.next;
        }

        return mst.connGraph();
    }

    public static void sortList(LinkedList list) {

        LinkedList.Node current = list.head, index = null;

        int tempDist, tempStart, tempEnd;

        if (list.head == null) {
            return;
        }
        else {
            while (current != null) {
                index = current.next;

                while (index != null) {
                    if (current.Dist < index.Dist) {
                        tempDist = current.Dist;
                        tempEnd = current.End;
                        tempStart = current.Start;

                        current.Dist = index.Dist;
                        current.Start = index.Start;
                        current.End = index.End;

                        index.Dist = tempDist;
                        index.Start = tempStart;
                        index.End = tempEnd;
                    }

                    if (current.Dist == index.Dist) {
                        if (original.getEdgeWeight(original.index(original.getCode(current.Start)), original.index(original.getCode(current.End))) >=
                                original.getEdgeWeight(original.index(original.getCode(index.Start)), original.index(original.getCode(index.End)))) {

                            tempDist = current.Dist;
                            tempEnd = current.End;
                            tempStart = current.Start;

                            current.Dist = index.Dist;
                            current.Start = index.Start;
                            current.End = index.End;

                            index.Dist = tempDist;
                            index.Start = tempStart;
                            index.End = tempEnd;
                        }
                    }

                    index = index.next;
                }
                current = current.next;
            }
        }
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

    public static LinkedList insert(LinkedList list, int i, int j, int dist) {

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

        return list;
    } //end insert


} //end RegNet