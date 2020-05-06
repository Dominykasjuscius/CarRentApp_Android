package mycam.com.dominykas.documentreader.myapplication;

public interface CarInterface<E> {
    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return true, if operation is Ok
     */
    boolean add(E e);

    /**
     * Returns the number of elements in this list.
     */
    int size();

    /**
     * @return true if this list contains no elements.
     */
    boolean isEmpty();

    /**
     * Removes all of the elements from this list.
     */
    void clear();

    /**
     * Returns the element at the specified position in this list.
     *
     * @param k index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    E get(int k);

    /**
     * Atitinka iteratoriaus metodą next (List tokio metodo neturi)
     * @return kitą reikšmę.
     */
    E getNext();

    /**
     * Returns an array containing all of the elements in this list in proper sequence (from first to last element).
     * @return an array containing all of the elements
     */
    Object[] toArray();

    /**
     * Iterpia elementa i nurodyta indeksa.
     * @param index
     * @param element
     */
    void set(int index, E element);

    /**
     * Pasalina elementa is saraso pagal indeksa.
     * @param index
     */
    void remove(int index);

    /**
     * Pasalina elementa is saraso, kuris yra toks pat kaip nurodytas.
     * @param element
     */
    void remove(E element);

    /**
     * Suranda elemento indeksa.
     */
    int indexOf(E element);
}
