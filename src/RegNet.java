import java.util.ArrayList;

public class RegNet
{
    //creates a regional network
    //G: the original graph
    //max: the budget



    public static Graph run(Graph G, int max) 
    {
	    //TODO

        //step 1
        Graph mst = kruskal(G);

        //step 2
        int i = 0;
        while (mst.totalWeight() > max) {
            int last = mst.sortedEdges().size();
            Edge e = mst.sortedEdges().get(last - 1 - i);

            if (isConn(mst, e)) {
                mst.removeEdge(e);
            }

            i++;
        } //end while

        //step 3

        //initialize stops to 0
        int[][] stops = new int[mst.V()][mst.V()];

        for (i = 0; i < G.V(); i++) {
            for (int j = 0; j < G.V(); j++) {
               stops[i][j] = 0;
            }

        }

        int[][] Distance = AllPairs(mst, stops);

        LinkedList list = stopOrganizer(G.V(), Distance, stops);
        LinkedList.Node node = list.head;

        while (node.next != null) {
            if (mst.totalWeight() + G.getEdgeWeight(node.Start, node.End) < max) {
                mst.addEdge(G.getEdge(node.Start, node.End));
            }
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

    private static boolean isConn(Graph g, Edge e) {
        Graph dupe = g.subgraph(g.getCodes());
        dupe.removeEdge(e);
        dupe = dupe.connGraph();

        return g.V() == dupe.V();
    } //end isConn

    private static int[][] AllPairs(Graph g, int[][] stops) {

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
                    int a = D[i][k];
                    int b = D[i][k] + D[k][j];
                    D[i][j] = Math.min(D[i][j], D[i][k] + D[k][j]);
                    if(a > b) {
                        stops[i][j] = stops[i][j] + 1;
                    }
                }
            }
        }

        return D;
    } //end all pairs

    private static LinkedList stopOrganizer(int v, int[][] distance,int[][] stops) {
        int c = 0;
        LinkedList list = new LinkedList();
        LinkedList.Node node = new LinkedList.Node(0, 0, 0);
        node.next = null;
        list.head = node;
        LinkedList.Node last = node;

        for (int i = 0; i < v; i++) {
            for (int j = 0; j < v; j++) {
                if (stops[i][j] != 0) {
                    c++;
                }
            } //end inner for
        }

        //loops through the array for the amount of times the stops is > 1;
        //get the path with the largest amount of stops and puts it into a LL

        for (int k = c; k > 0; k--) {
            LinkedList.Node next = new LinkedList.Node(0, 0, 0);
            int max = 0;
            int s = 0;
            int d = 0;
            int value = 0;
            for(int i = 0; i < v; i++){
                for(int j = 0; j < v; j++) {
                    int a = stops[i][j];
                    if (a > max) {
                        s = i;
                        d = j;
                        value = stops[i][j];
                    }
                   max = Math.max(stops[i][j], max);
                }
            }
            node.Start = s;
            node.End = d;
            node.Stops = value;
            stops[s][d] = 0;
            node.next = next;
            last = node;
        }

        last = null; //deletes extra node

        //sorts nodes by weight of 1) stops 2) distance
        while (node.next != null) {
            if (node.Stops == node.next.Stops) {
                if (distance[node.Start][node.End] > distance[node.next.Start][node.next.End]) {
                    LinkedList.Node temp = node;
                    node = node.next;
                    node.next = temp;
                }
            }
            node = node.next;
        }

        return list;
    } //end organizer


} //end RegNet