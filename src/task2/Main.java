package task2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) {
        try {
            // Асинхронно отримуємо дані для варіантів програмного забезпечення
            CompletableFuture<Double> priceFuture = fetchPrice("Програмне забезпечення A");
            CompletableFuture<Double> functionalityFuture = fetchFunctionalityScore("Програмне забезпечення A");
            CompletableFuture<Double> supportFuture = fetchSupportScore("Програмне забезпечення A");

            // Поєднуємо результати для обчислення загального рейтингу
            CompletableFuture<Double> combinedScoreFuture =
                    priceFuture.thenCombine(functionalityFuture, (price, functionality) -> functionality - price)
                            .thenCombine(supportFuture, (partialScore, support) -> partialScore + support);

            // Ланцюжок задач: знайти найкраще програмне забезпечення
            CompletableFuture<String> bestSoftwareFuture = combinedScoreFuture.thenCompose(score -> {
                System.out.println("Загальний рейтинг для Програмного забезпечення A: " + score);
                return findBestSoftware();
            });

            // Очікуємо завершення всіх задач та виводимо результат
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(priceFuture, functionalityFuture, supportFuture);
            allFutures.join();

            // Виводимо найкраще програмне забезпечення
            System.out.println("Найкраще програмне забезпечення: " + bestSoftwareFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static CompletableFuture<Double> fetchPrice(String softwareName) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay();
            System.out.println("Отримуємо ціну для: " + softwareName);
            return 100.0; // Мок цін
        });
    }

    private static CompletableFuture<Double> fetchFunctionalityScore(String softwareName) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay();
            System.out.println("Отримуємо рейтинг функціональності для: " + softwareName);
            return 85.0; // Мок рейтинг функціональності
        });
    }

    private static CompletableFuture<Double> fetchSupportScore(String softwareName) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay();
            System.out.println("Отримуємо рейтинг підтримки для: " + softwareName);
            return 90.0; // Мок рейтинг підтримки
        });
    }

    private static CompletableFuture<String> findBestSoftware() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay();
            System.out.println("Оцінюємо найкраще програмне забезпечення...");
            return "Програмне забезпечення A"; // Мок найкращого програмного забезпечення
        });
    }

    private static void simulateDelay() {
        try {
            Thread.sleep(1000); // Імітація затримки мережі або обробки
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
