import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) {
        // Асинхронне завдання 1: Отримання рядка
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            simulateWork("Task 1", 1000);
            return "Hello";
        });

        // Асинхронне завдання 2: Отримання рядка
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            simulateWork("Task 2", 3000);
            return "World";
        });

        // Використання thenCombine для об'єднання результатів двох завдань
        CompletableFuture<String> combinedResult = task1.thenCombine(task2, (result1, result2) -> {
            return result1 + " " + result2 + "!";
        });

        // Використання allOf для очікування завершення обох завдань
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2);

        // Використання anyOf для отримання результату першого завершеного завдання
        CompletableFuture<Object> firstCompleted = CompletableFuture.anyOf(task1, task2)
                .thenApply(result -> {
                    if (task1.isDone() && task1.join().equals(result)) {
                        return "Task 1 Result: " + result;
                    } else if (task2.isDone() && task2.join().equals(result)) {
                        return "Task 2 Result: " + result;
                    } else {
                        return "Unknown Result: " + result;
                    }
                });

        try {
            // Вивід комбінованого результату
            System.out.println("Combined Result: " + combinedResult.get());

            // Очікування завершення всіх завдань
            allTasks.get();
            System.out.println("Both tasks completed.");

            // Вивід результату першого завершеного завдання
            System.out.println("First Completed Task Result: " + firstCompleted.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Симуляція роботи завдання
    private static void simulateWork(String taskName, int delay) {
        System.out.println(taskName + " started.");
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(taskName + " completed.");
    }
}
