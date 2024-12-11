import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        // task 1
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
            long start = System.currentTimeMillis();
            Random random = new Random();
            char[] initialArray = new char[20];
            for (int i = 0; i < initialArray.length; i++) {
                initialArray[i] = (char) (random.nextInt(94) + 32);
            }

            System.out.println("Initial Array: " + Arrays.toString(initialArray));

            CompletableFuture<char[]> letters = CompletableFuture.supplyAsync(() -> {
                return IntStream.range(0, initialArray.length)
                        .mapToObj(i -> initialArray[i])
                        .filter(Character::isLetter)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString().toCharArray();
            });

            CompletableFuture<char[]> spaces = CompletableFuture.supplyAsync(() -> {
                return IntStream.range(0, initialArray.length)
                        .mapToObj(i -> initialArray[i])
                        .filter(ch -> ch == ' ' || ch == '\t')
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString().toCharArray();
            });

            CompletableFuture<char[]> others = CompletableFuture.supplyAsync(() -> {
                return IntStream.range(0, initialArray.length)
                        .mapToObj(i -> initialArray[i])
                        .filter(ch -> !Character.isLetter(ch) && ch != ' ' && ch != '\t')
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString().toCharArray();
            });

            letters.thenAcceptAsync(arr -> System.out.println("Letters: " + Arrays.toString(arr)));
            spaces.thenAcceptAsync(arr -> System.out.println("Spaces and Tabs: " + Arrays.toString(arr)));
            others.thenAcceptAsync(arr -> System.out.println("Others: " + Arrays.toString(arr)));

            CompletableFuture.allOf(letters, spaces, others).join();

            long end = System.currentTimeMillis();
            System.out.println("Task 1 completed in: " + (end - start) + " ms");
        });

        task1.join();

        // task 2
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
            long start = System.currentTimeMillis();
            Random random = new Random();
            double[] numbers = new double[20];
            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = random.nextDouble() * 100;
            }

            System.out.println("\nInitial Numbers: " + Arrays.toString(numbers));

            CompletableFuture<Double> sumFuture = CompletableFuture.supplyAsync(() -> {
                return Arrays.stream(numbers).sum();
            });

            CompletableFuture<Double> avgFuture = sumFuture.thenApplyAsync(sum -> sum / numbers.length);

            avgFuture.thenAcceptAsync(avg -> {
                System.out.println("Average: " + avg);
            });

            avgFuture.join();

            long end = System.currentTimeMillis();
            System.out.println("Task 2 completed in: " + (end - start) + " ms");
        });

        task2.join();

        System.out.println("All tasks completed successfully.");
    }
}
