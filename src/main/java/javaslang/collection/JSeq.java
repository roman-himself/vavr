/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.algebra.HigherKinded;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.*;

/**
 * Interface for sequential, traversable data structures.
 * <p>Mutation:</p>
 * <ul>
 * <li>{@link #append(Object)}</li>
 * <li>{@link #appendAll(Iterable)}</li>
 * <li>{@link #insert(int, Object)}</li>
 * <li>{@link #insertAll(int, Iterable)}</li>
 * <li>{@link #prepend(Object)}</li>
 * <li>{@link #prependAll(Iterable)}</li>
 * <li>{@link #set(int, Object)}</li>
 * </ul>
 * <p>Selection:</p>
 * <ul>
 * <li>{@link #get(int)}</li>
 * <li>{@link #indexOf(Object)}</li>
 * <li>{@link #lastIndexOf(Object)}</li>
 * <li>{@link #subsequence(int)}</li>
 * <li>{@link #subsequence(int, int)}</li>
 * </ul>
 * <p>Transformation:</p>
 * <ul>
 * <li>{@link #permutations()}</li>
 * <li>{@link #sort()}</li>
 * <li>{@link #sort(Comparator)}</li>
 * <li>{@link #splitAt(int)}</li>
 * </ul>
 * <p>Traversion:</p>
 * <ul>
 * <li>{@link #iterator(int)}</li>
 * </ul>
 *
 * @param <T> Component type
 * @since 1.1.0
 */
public interface JSeq<T> extends JTraversable<T>, IntFunction<T> {

    /**
     * A {@code JSeq} is a partial function which returns the element at the specified index by calling
     * {@linkplain #get(int)}.
     *
     * @param index an index
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    @Override
    default T apply(int index) {
        return get(index);
    }

    /**
     * Appends an element to this.
     *
     * @param element An element
     * @return A new Seq containing the given element appended to this elements
     */
    JSeq<T> append(T element);

    /**
     * Appends all given elements to this.
     *
     * @param elements An Iterable of elements
     * @return A new Seq containing the given elements appended to this elements
     * @throws NullPointerException if {@code elements} is null
     */
    JSeq<T> appendAll(Iterable<? extends T> elements);

    /**
     * Returns the element at the specified index.
     *
     * @param index an index
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    T get(int index);

    /**
     * Returns the index of the first occurrence of the given element or -1 if this does not contain the given element.
     *
     * @param element an element
     * @return the index of the first occurrence of the given element
     */
    int indexOf(T element);

    /**
     * Inserts the given element at the specified index.
     *
     * @param index   an index
     * @param element an element
     * @return a new Seq, where the given element is inserted into this at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    JSeq<T> insert(int index, T element);

    /**
     * Inserts the given elements at the specified index.
     *
     * @param index    an index
     * @param elements An Iterable of elements
     * @return a new Seq, where the given elements are inserted into this at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    JSeq<T> insertAll(int index, Iterable<? extends T> elements);

    /**
     * Returns an iterator of this elements starting at the given index.
     * The result is equivalent to {@code this.subsequence(index).iterator()}.
     *
     * @param index an index
     * @return a new Iterator, starting with the element at the given index or the empty Iterator, if index = length()
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; length()
     */
    default Iterator<T> iterator(int index) {
        return subsequence(index).iterator();
    }

    /**
     * Returns the index of the last occurrence of the given element or -1 if this does not contain the given element.
     *
     * @param element an element
     * @return the index of the last occurrence of the given element
     */
    int lastIndexOf(T element);

    /**
     * Computes all unique permutations.
     * <p>
     * Example:
     * <pre>
     * <code>
     * [].permutations() = []
     *
     * [1,2,3].permutations() = [
     *   [1,2,3],
     *   [1,3,2],
     *   [2,1,3],
     *   [2,3,1],
     *   [3,1,2],
     *   [3,2,1]
     * ]
     * </code>
     * </pre>
     *
     * @return this unique permutations
     */
    JSeq<? extends JSeq<T>> permutations();

    /**
     * Prepends an element to this.
     *
     * @param element An element
     * @return A new Seq containing the given element prepended to this elements
     */
    JSeq<T> prepend(T element);

    /**
     * Prepends all given elements to this.
     *
     * @param elements An Iterable of elements
     * @return A new Seq containing the given elements prepended to this elements
     */
    JSeq<T> prependAll(Iterable<? extends T> elements);

    /**
     * Sets the given element at the specified index.
     *
     * @param index   an index
     * @param element an element
     * @return a new Seq consisting of this elements and the given element is set at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    JSeq<T> set(int index, T element);

    /**
     * Sorts this elements according to their natural order. If this elements are not
     * {@code Comparable}, a {@code java.lang.ClassCastException} may be thrown.
     *
     * @return A sorted version of this
     * @throws ClassCastException if this elements are not {@code Comparable}
     */
    JSeq<T> sort();

    /**
     * Sorts this elements according to the provided {@code Comparator}. If this elements are not
     * {@code Comparable}, a {@code java.lang.ClassCastException} may be thrown.
     *
     * @param comparator A comparator
     * @return a sorted version of this
     */
    JSeq<T> sort(Comparator<? super T> comparator);

    /**
     * Splits a Seq at the specified index. The result of {@code splitAt(n)} is equivalent to
     * {@code Tuple.of(take(n), drop(n))}.
     *
     * @param n An index.
     * @return A Tuple containing the first n and the remaining elements.
     */
    Tuple2<? extends JSeq<T>, ? extends JSeq<T>> splitAt(int n);

    /**
     * <p>Returns a Seq that is a subsequence of this. The subsequence begins with the element at the specified index
     * and extends to the end of this Seq.</p>
     * Examples:
     * <pre>
     * <code>
     * List.of(1, 2).substring(0) = List.of(1, 2)
     * List.of(1, 2).substring(1) = List.of(2)
     * List.of(1, 2).substring(2) = List.nil()
     * </code>
     * </pre>
     *
     * @param beginIndex the beginning index, inclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if {@code beginIndex} is negative or larger than the length of this
     *                                   {@code String} object.
     */
    JSeq<T> subsequence(int beginIndex);

    /**
     * <p>Returns a Seq that is a subsequence of this. The subsequence begins with the element at the specified index
     * and extends to the element at index {@code endIndex - 1}.</p>
     * Examples:
     * <pre>
     * <code>
     * List.of(1, 2, 3, 4).substring(1, 3) = List.of(2, 3)
     * List.of(1, 2, 3, 4).substring(0, 4) = List.of(1, 2, 3, 4)
     * List.of(1, 2, 3, 4).substring(2, 2) = List.nil()
     * </code>
     * </pre>
     *
     * @param beginIndex the beginning index, inclusive
     * @param endIndex   the end index, exclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if the
     *                                   {@code beginIndex} is negative, or
     *                                   {@code endIndex} is larger than the length of
     *                                   this {@code String} object, or
     *                                   {@code beginIndex} is larger than
     *                                   {@code endIndex}.
     */
    JSeq<T> subsequence(int beginIndex, int endIndex);

    // -- Adjusted return types of Traversable methods

    @Override
    JSeq<T> clear();

    @Override
    JSeq<? extends JSeq<T>> combinations();

    @Override
    JSeq<? extends JSeq<T>> combinations(int k);

    @Override
    JSeq<T> distinct();

    @Override
    <U> JSeq<T> distinct(Function<? super T, ? extends U> keyExtractor);

    @Override
    JSeq<T> drop(int n);

    @Override
    JSeq<T> dropRight(int n);

    @Override
    JSeq<T> dropWhile(Predicate<? super T> predicate);

    @Override
    JSeq<T> filter(Predicate<? super T> predicate);

    @Override
    JSeq<T> findAll(Predicate<? super T> predicate);

    @Override
    <U, TRAVERSABLE extends HigherKinded<U, JTraversable<?>>> JSeq<U> flatMap(Function<? super T, ? extends TRAVERSABLE> mapper);

    @Override
    <U, TRAVERSABLE extends HigherKinded<U, JTraversable<?>>> JSeq<U> flatten(Function<? super T, ? extends TRAVERSABLE> f);

    @Override
    JSeq<? extends JSeq<T>> grouped(int size);

    @Override
    JSeq<T> init();

    @Override
    Option<? extends JSeq<T>> initOption();

    @Override
    JSeq<T> intersperse(T element);

    @Override
    <U> JSeq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends JSeq<T>, ? extends JSeq<T>> partition(Predicate<? super T> predicate);

    @Override
    JSeq<T> peek(Consumer<? super T> action);

    @Override
    JSeq<T> remove(T element);

    @Override
    JSeq<T> removeAll(T element);

    @Override
    JSeq<T> removeAll(Iterable<? extends T> elements);

    @Override
    JSeq<T> replace(T currentElement, T newElement);

    @Override
    JSeq<T> replaceAll(T currentElement, T newElement);

    @Override
    JSeq<T> replaceAll(UnaryOperator<T> operator);

    @Override
    JSeq<T> retainAll(Iterable<? extends T> elements);

    @Override
    JSeq<T> reverse();

    @Override
    JSeq<? extends JSeq<T>> sliding(int size);

    @Override
    JSeq<? extends JSeq<T>> sliding(int size, int step);

    @Override
    Tuple2<? extends JSeq<T>, ? extends JSeq<T>> span(Predicate<? super T> predicate);

    @Override
    JSeq<T> tail();

    @Override
    Option<? extends JSeq<T>> tailOption();

    @Override
    JSeq<T> take(int n);

    @Override
    JSeq<T> takeRight(int n);

    @Override
    JSeq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<? extends JSeq<T1>, ? extends JSeq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> JSeq<Tuple2<T, U>> zip(Iterable<U> that);

    @Override
    <U> JSeq<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    @Override
    JSeq<Tuple2<T, Integer>> zipWithIndex();
}