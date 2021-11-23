import java.util.ArrayList;

public class GlobalNet
{

    //creates a global network
    //O : the original graph
    //regions: the regional graphs
    private static ArrayList<Edge> edgeList = new ArrayList<>();
    private static Graph original;
    private static Graph[] ogRegions;
    private static int[] dist;
    private static int[] prev;
    public static Graph run(Graph O, Graph[] regions) 
    {
        original = O;
        ogRegions = regions;
	    //TODO
        connect();
        O.connGraph();
        Graph mst = new Graph(O.V());
        mst.setCodes(O.getCodes());
        for (int i = 0; i < edgeList.size(); i++) {
            mst.addEdge(edgeList.get(i));
        }
        return mst.connGraph();
    }


    public static void connect() {
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
        sub.setCodes(original.getCodes()); //might be able to delete

        int idx = sub.index(one.getCode(0));
        dist = new int[original.V()];
        prev = new int[original.V()];
        Dijkstra(sub, idx, one);

        int vertex = 0;


        for (int i = 0; i < two.V(); i++) {
            idx = original.index(two.getCode(i));

            if (dist[idx] < Integer.MAX_VALUE/2) {
                int min = dist[idx];
                vertex = idx;
            }
        }

        boolean forever = true;
        while (forever) {
            Edge newEdge = new Edge(original.getCode(vertex), original.getCode(prev[vertex]), original.getEdgeWeight(vertex, prev[vertex]));
            if (dist[prev[vertex]] == 0) {
                insert(newEdge);
                break;
            } else {
                insert(newEdge);
                vertex = prev[vertex];
            }
        }

    } //end Region connect

    public static void insert(Edge e) {
        for (int i = edgeList.size() - 1; i >= 0; i--) {
            if (edgeList.get(i).equals(e))
                return;
        }
        edgeList.add(e);
    }

    private static void Dijkstra(Graph g, int s, Graph o) {

        dist = new int[g.V()];
        int[] prev = new int[g.V()];
        DistQueue q = new DistQueue(g.V());
        dist[s] = 0;

        for (int u = 0; u < g.V(); u++) {
            boolean bool = false;
            if (o.index(o.getCode(u)) != -1) {
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
            for (int i = 0; i < g.adj(u).size() - 1; i++) {
                int w = g.adj(u).get(i);
                int d = dist[u] + g.getEdgeWeight(u, w);
                if (d < dist[w]) {
                    dist[w] = d;
                    prev[w] = u;
                    q.set(w, d);
                }
            }
        }

    } //end Dijkstra


}

    
    