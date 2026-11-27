package ru.sortviz.app;

import ru.sortviz.algorithm.Algorithms;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class Visualizer {
    private final JComboBox<Algorithms> algorithms = new JComboBox<>(Algorithms.values());
    private final JSlider delay = new JSlider(10, 350, 80);
    private final JLabel status = new JLabel();
    private final Bars bars = new Bars();
    private final Timer timer = new Timer(80, event -> advance());
    private int[] input;
    private List<int[]> frames = List.of();
    private int next;

    public void show() {
        JFrame window = new JFrame("Визуализатор алгоритмов сортировки");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        var controls = new JPanel();
        var shuffle = new JButton("Новый массив");
        var start = new JButton("Старт");
        var pause = new JButton("Пауза / продолжить");
        controls.add(algorithms);
        controls.add(shuffle);
        controls.add(start);
        controls.add(pause);
        controls.add(new JLabel("Задержка"));
        controls.add(delay);
        shuffle.addActionListener(event -> shuffle());
        start.addActionListener(event -> start());
        pause.addActionListener(event -> {
            if (timer.isRunning()) {
                timer.stop();
                status.setText("Пауза");
            } else if (next < frames.size()) {
                timer.start();
            }
        });
        delay.addChangeListener(event -> timer.setDelay(delay.getValue()));
        window.add(controls, BorderLayout.NORTH);
        window.add(bars, BorderLayout.CENTER);
        window.add(status, BorderLayout.SOUTH);
        shuffle();
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void shuffle() {
        timer.stop();
        frames = List.of();
        next = 0;
        input = ThreadLocalRandom.current().ints(32, 5, 101).toArray();
        bars.display(input);
        status.setText("Новый массив");
    }

    private void start() {
        timer.stop();
        bars.display(input);
        var snapshots = new ArrayList<int[]>();
        var selected = (Algorithms) algorithms.getSelectedItem();
        selected.strategy().sort(input.clone(), snapshots::add);
        frames = snapshots;
        next = 0;
        advance();
        if (next < frames.size()) timer.start();
    }

    private void advance() {
        if (next < frames.size()) bars.display(frames.get(next++));
        if (next == frames.size()) {
            timer.stop();
            status.setText("Сортировка завершена");
        } else {
            status.setText("Шаг " + next + " из " + frames.size());
        }
    }

    private static final class Bars extends JPanel {
        private int[] values = {};
        Bars() { setPreferredSize(new Dimension(820, 370)); }
        void display(int[] snapshot) { values = snapshot; repaint(); }

        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (values.length == 0) return;
            graphics.setColor(new Color(28, 92, 166));
            int rowWidth = Math.max(1, (getWidth() - 20) / values.length);
            int baseline = getHeight() - 15;
            for (int i = 0; i < values.length; i++) {
                int height = values[i] * Math.max(0, baseline - 10) / 100;
                graphics.fillRect(10 + i * rowWidth, baseline - height, Math.max(1, rowWidth - 3), height);
            }
        }
    }
}
