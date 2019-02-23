package com.cliff.grammarmatch.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * static utility methods for working with Collections of String(s) that are used by the Grammar Matchers
 */
public class GrammarUtils {


    /**
     * Partitions the passed in utterance into groups of words. Each group is formed by taking a
     * sliding window over all the words in the utterance. The window size starts at "max" and
     * decreases down to size "min". For example, an utterance of "A B C D" with max=3 and min=2 will
     * return a list of the following Strings:
     *   ["A B C"], ["B C D"], ["A B"], ["B C"], ["C D"]
     *
     * @param utterance - a space separated String of "words"
     * @param max - the maximum size of the group of words to return
     * @param min - the minimum size of the group of words to return
     * @return a List of Strings where each String is a group of words captured by the sliding window
     */
    public static List<String> buildSlidingGroups(String utterance, int max, int min) {
        if (min > max) throw new IllegalArgumentException(String.format("min size:%d greater than max:%d size",min,max));

        final List<String> tokens = Arrays.asList( utterance.split(" ") );
        List<String> wordGroups = new ArrayList<>();
        for (int i = max; i >= min; i--) {
            final List<String> slice = sliding(tokens, i)
                    .map( ls -> String.join(" ", ls) )
                    .collect( Collectors.toList() );
            wordGroups.addAll(slice);
        }
        return wordGroups;
    }

    /**
     * Forms a "sliding window" over the elements of the list. Each "window" is a subset of the list containing
     * 'size' elements. The window then slides to the right by one element and the next subset of elements is added,
     * until the end of the list is reached.
     *
     * @param list - the elements to slide over
     * @param size - the size of the window
     * @param <T> - type of List elements
     * @return a Stream of List<T> containing sublists of size: 'size'
     */
    public static <T> Stream<List<T>> sliding(List<T> list, int size) {
        if(size > list.size())
            return Stream.empty();
        return IntStream.range(0, list.size() - size+1)
                .mapToObj(start -> list.subList(start, start+size));
    }
}
