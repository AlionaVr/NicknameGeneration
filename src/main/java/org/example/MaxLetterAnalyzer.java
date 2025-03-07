package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MaxLetterAnalyzer {
    final int QUEUE_CAPACITY = 10;
    final int LENGTH = 10;
    final int TEXT_NUMBER = 10_000;
    AtomicBoolean isGenerationFinished = new AtomicBoolean(false);

    public void analize() throws InterruptedException {
        BlockingQueue<String> queueForA = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        BlockingQueue<String> queueForB = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        BlockingQueue<String> queueForC = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        Thread generatorTextThread = new Thread(() -> {
            for (int i = 0; i < TEXT_NUMBER; i++) {
                String text = generateText("abc", LENGTH);
                try {
                    queueForA.put(text);
                    queueForB.put(text);
                    queueForC.put(text);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(e.getMessage());
                }
            }
            isGenerationFinished.set(true);
            System.out.println("generation finished");
        });

        Thread aAnalyzerThread = new Thread(() -> {
            try {
                int maxCount = -1;
                String maxText = "";

                while (!isGenerationFinished.get()) {
                    String text = queueForA.take();
                    int count = countLetter(text, 'a');

                    if (count > maxCount) {
                        maxCount = count;
                        maxText = text;
                    }
                }
                System.out.println("aAnalyzerThread finished");
                System.out.println("MaxText A: " + maxText + " MaxCount A " + maxCount);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("aAnalyzerThread was interrupted");
            }
        });

        Thread bAnalyzerThread = new Thread(() -> {
            try {
                int maxCount = -1;
                String maxText = "";
                while (!isGenerationFinished.get()) {
                    String text = queueForB.take();
                    int count = countLetter(text, 'b');

                    if (count > maxCount) {
                        maxCount = count;
                        maxText = text;
                    }
                }
                System.out.println("bAnalyzerThread finished");
                System.out.println("MaxText B:  " + maxText + " MaxCount B " + maxCount);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("bAnalyzerThread was interrupted");
            }
        });

        Thread cAnalyzerThread = new Thread(() -> {
            try {
                int maxCount = -1;
                String maxText = "";
                while (!isGenerationFinished.get()) {
                    String text = queueForC.take();
                    int count = countLetter(text, 'c');

                    if (count > maxCount) {
                        maxCount = count;
                        maxText = text;
                    }
                }
                System.out.println("cAnalyzerThread finished");
                System.out.println("MaxText C: " + maxText + " MaxCount C " + maxCount);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("cAnalyzerThread was interrupted");
            }
        });

        generatorTextThread.start();
        aAnalyzerThread.start();
        bAnalyzerThread.start();
        cAnalyzerThread.start();

        generatorTextThread.join();
        aAnalyzerThread.join();
        bAnalyzerThread.join();
        cAnalyzerThread.join();
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


