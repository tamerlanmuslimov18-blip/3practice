package ru.sortviz.algorithm;

import java.util.function.Consumer;

@FunctionalInterface
public interface SortAlgorithm {
    void sort(int[] values, Consumer<int[]> onStep);
}
