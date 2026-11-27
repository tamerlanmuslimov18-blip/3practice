package ru.sortviz;

import ru.sortviz.algorithm.Algorithms;
import ru.sortviz.app.Visualizer;
import javax.swing.SwingUtilities;

public final class Main {
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("--self-test")) {
            Algorithms.verify();
            return;
        }
        SwingUtilities.invokeLater(() -> new Visualizer().show());
    }
}
