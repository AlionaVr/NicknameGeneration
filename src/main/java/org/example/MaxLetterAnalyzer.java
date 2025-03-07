package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MaxLetterAnalyzer {
    final int QUEUE_CAPACITY = 100;
    final int LENGTH = 100_000;
    final int TEXT_NUMBER = 10_000;
    final String END_MARKER = "END";

    public void analize() throws InterruptedException {
        BlockingQueue<String> queueForA = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        BlockingQueue<String> queueForB = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        BlockingQueue<String> queueForC = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        Thread generatorTextThread = new Thread(() -> {
            try {
                for (int i = 0; i < TEXT_NUMBER; i++) {
                    String text = generateText("abc", LENGTH);
                    queueForA.put(text);
                    queueForB.put(text);
                    queueForC.put(text);
                }
                queueForA.put(END_MARKER);
                queueForB.put(END_MARKER);
                queueForC.put(END_MARKER);
                System.out.println("generation finished");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Generator was interrupted");
            }
        });

        Thread aAnalyzerThread = new Thread(createAnalyzerTask(queueForA, 'a'));
        Thread bAnalyzerThread = new Thread(createAnalyzerTask(queueForB, 'b'));
        Thread cAnalyzerThread = new Thread(createAnalyzerTask(queueForC, 'c'));

        generatorTextThread.start();
        aAnalyzerThread.start();
        bAnalyzerThread.start();
        cAnalyzerThread.start();

        generatorTextThread.join();
        aAnalyzerThread.join();
        bAnalyzerThread.join();
        cAnalyzerThread.join();
    }

    private Runnable createAnalyzerTask(BlockingQueue<String> queue, char letter) {
        return () -> {
            try {
                int maxCount = -1;
                String maxText = "";

                while (true) {
                    String text = queue.take();
                    if (text.equals(END_MARKER)) break;

                    int count = countLetter(text, letter);
                    if (count > maxCount) {
                        maxCount = count;
                        maxText = text;
                    }
                }
                System.out.printf("AnalyzerThread finished for letter %c .%n MaxText: %s %nMaxCount %d %n", letter, maxText, maxCount);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("aAnalyzerThread was interrupted");
            }
        };
    }

    private int countLetter(String text, char letter) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == letter) {
                count++;
            }
        }
        return count;
    }

    public String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}