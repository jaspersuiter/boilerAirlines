public class LinkedList {

    Node head;

        static class Node {
            int Start;
            int End;
            int Stops;
            Node next;

            Node(int s, int e, int stops) {
                Start = s;
                End = e;
                Stops = stops;
                next = null;
            }

        }

} //end LL
