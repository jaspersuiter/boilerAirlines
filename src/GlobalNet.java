import java.util.ArrayList;

public class GlobalNet
{

    //creates a global network
    //O : the original graph
    //regions: the regional graphs
    private static ArrayList<Edge> edgeList;
    private static Graph original;
    private static Graph[] ogRegions;
    public static Graph run(Graph O, Graph[] regions) 
    {
        original = O;
        ogRegions = regions;
	    //TODO
        connect();
        original = original.connGraph();
        Graph mst = new Graph(O.V());
        original = original.connGraph();

        mst.setCodes(original.getCodes());
        mst.setCodes(O.getCodes());
        mst = mst.connGraph();
        for (int i = 0; i < edgeList.size(); i++) {
            mst.addEdge(edgeList.get(i));
        }
        return mst.connGraph();
    }


    public static void connect() {
        edgeList = new ArrayList<>();
        for (int i = 0; i < ogRegions.length; i++) {
            edgeList.addAll(ogRegions[i].edges());
            for (int j = 0; j < ogRegions.length; j++) {
                if (ogRegions[i] != ogRegions[j]){
                    regionConnect(ogRegions[i], ogRegions[j]);
                }
            }
        }

    } //end connect

    public static void  regionConnect(Graph one, Graph two) {
        Graph sub = original.subgraph(original.getCodes());


        int idx = sub.index(one.getCode(0));
        int[] dist = Dijkstra(sub, idx, one).get(0);
        int[] prev = Dijkstra(sub, idx, one).get(1);

        int min = Integer.MAX_VALUE/2;
        int vertex = 0;


        for (int i = 0; i < two.V(); i++) {
           idx = original.index(two.getCode(i));

            if (dist[idx] < min) {
                min = dist[idx];
                vertex = idx;
            }
        }

        boolean forever = true;
        while (forever) {
            Edge newEdge = new Edge(original.getCode(vertex), original.getCode(prev[vertex]), original.getEdgeWeight(vertex, prev[vertex]));
            insert(newEdge);
            if (dist[prev[vertex]] == 0) {
                break;
            } else {
                vertex = prev[vertex];
            }
        }


    } //end Region connect

    public static void insert(Edge e) {
        for (Edge edge : edgeList) {
            if (edge.equals(e))
                return;
        }
        edgeList.add(e);
    }

    private static ArrayList<int[]> Dijkstra(Graph g, int s, Graph o) {

        int[] dist = new int[g.V()];
        int[] prev = new int[g.V()];
        DistQueue q = new DistQueue(g.V());
        dist[s] = 0;

        for (int u = 0; u < g.V(); u++) {
            boolean bool = false;
            if (o.index(g.getCode(u)) != -1) {
                bool = true;
            }
            if (u != s && !bool) {
                dist[u] = Integer.MAX_VALUE / 2;
            }
            prev[u] = -1;
            q.insert(u, dist[u]);
        }

        while (!q.isEmpty()) {
            int u = q.delMin();
            for (int w : g.adj(u)) {

                if (!q.inQueue(w)) {
                    continue;
                }

                int d = dist[u] + g.getEdgeWeight(u, w);
                if (d < dist[w]) {
                    dist[w] = d;
                    prev[w] = u;
                    q.set(w, d);
                }
            }
        }

        ArrayList<int[]> returnVal = new ArrayList<>();
        returnVal.add(dist);
        returnVal.add(prev);

        return returnVal;
    } //end Dijkstra


}

    
    