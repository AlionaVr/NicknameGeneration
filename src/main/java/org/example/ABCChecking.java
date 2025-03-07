package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ABCChecking {
    protected AtomicInteger counter3 = new AtomicInteger(0);
    protected AtomicInteger counter4 = new AtomicInteger(0);
    protected AtomicInteger counter5 = new AtomicInteger(0);

    MaxLetterAnalyzer analyzer = new MaxLetterAnalyzer();

    public void abcChecker() throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = analyzer.generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindromeThread = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text)) {
                    increaseCounter(text);
                }
            }
        });

        Thread sameLettersThread = new Thread(() -> {
            for (String text : texts) {
                if (isSameLetters(text)) {
                    increaseCounter(text);
                }
            }
        });

        Thread ascendingOrderThread = new Thread(() -> {
            for (String text : texts) {
                if (isLettersInAscendingOrder(text)) {
                    increaseCounter(text);
                }
            }
        });

        palindromeThread.start();
        sameLettersThread.start();
        ascendingOrderThread.start();

        palindromeThread.join();
        sameLettersThread.join();
        ascendingOrderThread.join();

        System.out.println("Beautiful words of length 3: " + counter3);
        System.out.println("Beautiful words of length 4: " + counter4);
        System.out.println("Beautiful words of length 5: " + counter5);
    }

    private void increaseCounter(String text) {
        if (text.length() == 3) {
            counter3.getAndIncrement();
        } else if (text.length() == 4) {
            counter4.getAndIncrement();
        } else if (text.length() == 5) {
            counter5.getAndIncrement();
        }
    }


    private boolean isPalindrome(String text) {
        for (int i = 0; i < text.length() / 2; i++) { //text.length()/2 т.к. для проверки полиндрома, нам достаточно сравнить половину слова со второй половиной
            if (text.charAt(i) != text.charAt(text.length() - i - 1)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSameLetters(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) != text.charAt(0)) {
                return false;
            }
        }
        return true;
    }

    private boolean isLettersInAscendingOrder(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) > text.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
