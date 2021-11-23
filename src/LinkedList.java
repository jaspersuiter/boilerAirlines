public class LinkedList {

    Node head;

        static class Node {
            int Start;
            int End;
            int Dist;
            Node next;

            Node(int s, int e, int dist) {
                Start = s;
                End = e;
                Dist = dist;
                next = null;
            }

        }

} //end LL
