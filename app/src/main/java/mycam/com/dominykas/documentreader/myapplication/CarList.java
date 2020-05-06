package mycam.com.dominykas.documentreader.myapplication;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class CarList <E extends Comparable<E>>
        implements CarInterface<E>, Iterable<E>, Cloneable {



    private Node<E> first;
    private Node<E> last;
    private Node<E> current;
    private int size;


    @Override
    public boolean add(E e) {
        Node<E> la = last;
        Node<E> nodeNew = new Node<>(la, e, null);
        last = nodeNew;
        if(la == null)
            first = nodeNew;
        else
            la.next = nodeNew;
        size++;
        return true;
    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Išvalo sąrašą
     */
    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
        current = null;
    }

    /**
     * Grąžina elementą pagal jo indeksą (pradinis indeksas 0)
     *
     * @param k indeksas
     * @return k-ojo elemento reikšmę; jei k yra blogas, gąžina null
     */
    @Override
    public E get(int k) {
        if (k < 0 || k >= size) {
            return null;
        }
        current = first.findNode(k);
        return current.element;
    }

    /**
     * Pereina prie kitos reikšmės ir ją grąžina
     *
     * @return kita reikšmė
     */
    @Override
    public E getNext() {
        if (current == null) {
            return null;
        }
        current = current.next;
        if (current == null) {
            return null;
        }
        return current.element;
    }

    /**
     * Sukuria sąrašo kopiją.
     * @return sąrašo kopiją
     */
    @Override
    public CarList<E> clone() {
        CarList<E> cl = null;

        try {
            cl = (CarList<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            System.exit(1);
        }
        if (first == null) {
            return cl;
        }
        cl.first = new Node<>(null, this.first.element, null);
        Node<E> e2 = cl.first;
        for (Node<E> e1 = first.next; e1 != null; e1 = e1.next) {
            e2.next = new Node<>(e1.prev, e1.element, null);
            e2 = e2.next;
        }
        cl.last = e2;
        cl.size = this.size;
        return cl;
    }


    public void set(int index, E element){
        if (index > 0 || index <= size) {
            Node<E> z;
            if (index < (size >> 1)) {
                Node<E> x = first;
                for (int i = 0; i < index; i++)
                    x = x.next;
                z = x;
            } else {
                Node<E> x = last;
                for (int i = size - 1; i > index; i--)
                    x = x.prev;
                z = x;
            }
            E val = z.element;
            z.element = element;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public int indexOf(E element) {
        int index = 0;
        if (element == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.element == null)
                    return index;
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (element.equals(x.element))
                    return index;
                index++;
            }
        }
        return -1;
    }

    /**
     * Panaikina nurodyta elementa is saraso.
     */
    public void remove(E o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.element == null) {
                    unlink(x);
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.element)) {
                    unlink(x);
                }
            }
        }

    }

    /**
     * Pasalina elementa is saraso pagal indeksa.
     */
    public void remove(int index) {
        if(size >= index){
            int i = 0;
            for (Node<E> x = first; x != null; x = x.next) {
                System.out.printf(i+"");

                if (i == index){
                    unlink(x);
                    break;
                }
                i++;
            }
        }
    }

    /**
     * Panaikina elementa is saraso.
     * @param x
     * @return
     */
    E unlink(Node<E> x) {
        // assert x != null;
        final E element = x.element;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.element = null;
        size--;
        return element;
    }

    /**
     * Formuojamas Object masyvas (E tipo masyvo negalima sukurti),
     *   kur surašomi sąrašo elementai
     *
     * @return sąrašo elementų masyvas
     */
    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        int i = 0;
        for (Node<E> e1 = first; e1 != null; e1 = e1.next) {
            a[i++] = e1.element;
        }
        return a;
    }

    /**
     * Masyvo rikiavimas Arrays klasės metodu sort
     */
    public void sortJava() {
        Object[] a = this.toArray();
        Arrays.sort(a);
        int i = 0;
        for (Node<E> e1 = first; e1 != null; e1 = e1.next) {
            e1.element = (E) a[i++];
        }
    }

    /**
     * Rikiavimas Arrays klasės metodu sort pagal komparatorių
     *
     * @param c komparatorius
     */
    public void sortJava(Comparator<E> c) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);
        int i = 0;
        for (Node<E> e1 = first; e1 != null; e1 = e1.next) {
            e1.element = (E) a[i++];
        }
    }

    /**
     * Sąrašo rikiavimas burbuliuko metodu
     */
    public void sortBuble() {
        if (first == null) {
            return;
        }
        for (;;) {
            boolean jauGerai = true;
            Node<E> e1 = first;
            for (Node<E> e2 = first.next; e2 != null; e2 = e2.next) {
                if (e1.element.compareTo(e2.element) > 0) {
                    E t = e1.element;
                    e1.element = e2.element;
                    e2.element = t;
                    jauGerai = false;
                }
                e1 = e2;
            }
            if (jauGerai) {
                return;
            }
        }
    }

    /**
     * Sąrašo rikiavimas burbuliuko metodu pagal komparatorių
     *
     * @param c komparatorius
     */
    public void sortBuble(Comparator<E> c) {
        if (first == null) {
            return;
        }
        for (;;) {
            boolean jauGerai = true;
            Node<E> e1 = first;
            for (Node<E> e2 = first.next; e2 != null; e2 = e2.next) {
                if (c.compare(e1.element, e2.element) > 0) {
                    E t = e1.element;
                    e1.element = e2.element;
                    e2.element = t;
                    jauGerai = false;
                }
                e1 = e2;
            }
            if (jauGerai) {
                return;
            }
        }
    }

    /**
     * MinMax rikiavimo metodas.
     */
    public void MinMax(Comparator<E> c){
        for(Node<E> e1 = first; e1 != null; e1 = e1.next){
            Node<E> max = e1;
            for(Node<E> e2 = e1; e2 != null; e2 = e2.next){
                if(c.compare(max.element, e2.element) > 0){
                    max = e2;
                }
                E k = e1.element;
                e1.element = max.element;
                max.element = k;
            }
        }
    }

    /**
     * Sukuria iteratoriaus objektą sąrašo elementų peržiūrai
     *
     * @return iteratoriaus objektą
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIteratorKTU();
    }

    /**
     * Iteratoriaus metodų klasė
     */
    private class ListIteratorKTU implements Iterator<E> {

        private CarList.Node<E> iterPosition;

        ListIteratorKTU() {
            iterPosition = first;
        }

        @Override
        public boolean hasNext() {
            return iterPosition != null;
        }

        @Override
        public E next() {
            E d = iterPosition.element;
            iterPosition = iterPosition.next;
            return d;
        }
    }

    /**
     * Vidinė mazgo klasė
     *
     * @param <E> mazgo duomenų tipas
     */

    private static class Node<E> {

        E element;    // ji nematoma už klasės ListKTU ribų
        Node<E> next; // next - kaip įprasta - nuoroda į kitą mazgą
        Node<E> prev;

        Node(Node<E> prev, E e, Node<E> next) { //mazgo konstruktorius
            this.prev = prev;
            this.element = e;
            this.next = next;
        }

        /**
         * Suranda sąraše k-ąjį mazgą
         *
         * @param k ieškomo mazgo indeksas (prasideda nuo 0)
         * @return surastas mazgas
         */
        public Node<E> findNode(int k) {
            Node<E> e = this;
            for (int i = 0; i < k; i++) {
                e = e.next;
            }
            return e;
        }
    }
}

