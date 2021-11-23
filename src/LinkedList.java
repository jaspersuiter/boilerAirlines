public class LinkedList {

    Node head;

        static class Node {
            int Start;
            int End;
            int Weight;
            Node next;

            Node(int s, int e, int weight) {
                Start = s;
                End = e;
                Weight= weight;
                next = null;
            }

        }

} //end LL
