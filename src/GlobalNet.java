import java.util.ArrayList;
import java.util.Comparator;

public class GlobalNet
{

    public static int[] dist;
    //creates a global network
    //O : the original graph
    //regions: the regional graphs
    public static Graph run(Graph O, Graph[] regions) 
    {
	    //TODO

        return null;
    }

    private static int[] Dijkstra(Graph g, int s) {

        dist = new int[g.V()];
        int[] prev = new int[g.V()];
        ArrayList<Edge> q = new ArrayList<>();

        dist[s] = 0;
        for (int u = 0; u < g.V(); u++) {
            if (u != s) {
                dist[u] = Integer.MAX_VALUE/2;
            }
            prev[u] = -1;

            q.add(new Edge(g.getEdge(u, s).v, g.getEdge(u, s).u, dist[u]));
        }


        q.sort(new EdgeSort());

        while (q.size() != 0) {
            int u = q.get(0).ui();
            q.remove(0);
            for (int i = 0; i < g.adj(u).size() - 1; i++) {
                int w = g.adj(u).get(i);
                int d = dist[u] + g.getEdgeWeight(u, w);
                if (d < dist[w]) {
                    dist[w] = d;
                    prev[w] = u;
                    q.set(d, g.getEdge(u,w));
                }
            }
        }

       return prev;
} //end Dijkstra


}

    
    