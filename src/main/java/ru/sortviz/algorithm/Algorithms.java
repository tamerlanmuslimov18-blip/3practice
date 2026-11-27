package ru.sortviz.algorithm;

import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import java.util.function.Consumer;

public enum Algorithms {
    BUBBLE("Пузырьковая", Algorithms::bubble),
    QUICK("Быстрая", (a, step) -> quick(a, 0, a.length - 1, step)),
    MERGE("Слиянием", (a, step) -> merge(a, new int[a.length], 0, a.length, step));

    private final String label;
    private final SortAlgorithm strategy;

    Algorithms(String label, SortAlgorithm strategy) {
        this.label = label;
        this.strategy = strategy;
    }

    public SortAlgorithm strategy() { return strategy; }
    @Override public String toString() { return label; }

    private static void swap(int[] a, int i, int j, Consumer<int[]> step) {
        if (i == j) return;
        int old = a[i];
        a[i] = a[j];
        a[j] = old;
        step.accept(ArrayUtils.clone(a));
    }

    private static void bubble(int[] a, Consumer<int[]> step) {
        for (int end = a.length - 1; end > 0; end--) {
            boolean changed = false;
            for (int i = 0; i < end; i++) {
                if (a[i] > a[i + 1]) {
                    swap(a, i, i + 1, step);
                    changed = true;
                }
            }
            if (!changed) return;
        }
    }

    private static void quick(int[] a, int left, int right, Consumer<int[]> step) {
        if (left >= right) return;
        int i = left;
        int j = right;
        int pivot = a[left + (right - left) / 2];
        while (i <= j) {
            while (a[i] < pivot) i++;
            while (a[j] > pivot) j--;
            if (i <= j) swap(a, i++, j--, step);
        }
        quick(a, left, j, step);
        quick(a, i, right, step);
    }

    private static void merge(int[] a, int[] buffer, int left, int right, Consumer<int[]> step) {
        if (right - left < 2) return;
        int middle = left + (right - left) / 2;
        merge(a, buffer, left, middle, step);
        merge(a, buffer, middle, right, step);
        System.arraycopy(a, left, buffer, left, right - left);
        int first = left;
        int second = middle;
        for (int i = left; i < right; i++) {
            a[i] = second == right || (first < middle && buffer[first] <= buffer[second])
                    ? buffer[first++] : buffer[second++];
            step.accept(ArrayUtils.clone(a));
        }
    }

    public static void verify() {
        int[][] inputs = {{}, {4}, {3, 1, 3, 2}, {-1, -9, 0}, {1, 2, 3}, {9, 8, 7}};
        for (Algorithms algorithm : values()) {
            for (int[] input : inputs) {
                int[] expected = input.clone();
                Arrays.sort(expected);
                int[] actual = input.clone();
                algorithm.strategy().sort(actual, frame -> {
                    if (frame == actual || frame.length != actual.length) {
                        throw new AssertionError("Invalid snapshot");
                    }
                });
                if (!Arrays.equals(actual, expected)) throw new AssertionError(algorithm);
            }
        }
        System.out.println("All 18 sorting checks passed.");
    }
}
