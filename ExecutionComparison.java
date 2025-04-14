import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

class TaskManager {
  private final List<Task> tasks = new ArrayList<>();
  private int currentTaskIndex = 0;
  private long switchTime;
  private static final int SWITCH_INTERVAL = 1000;
  private long startTime;
  private static final int TASKS_PER_PROCESSOR = 10;

  public void addTask(Processor obj) {
    tasks.add(new Task(obj));
  }

  public long[] start() {
    if (tasks.isEmpty())
      return new long[] { 0, 0, 0 };
    startTime = System.currentTimeMillis();
    switchTime = startTime + SWITCH_INTERVAL;

    long[] times = new long[3];

    while (true) {
      long currentTime = System.currentTimeMillis();
      Task currentTask = tasks.get(currentTaskIndex);
      times = currentTask.processor.performComputation(times);

      if (allTasksCompleted())
        return times;

      if (currentTime >= switchTime) {
        currentTaskIndex = (currentTaskIndex + 1) % tasks.size();
        switchTime = currentTime + SWITCH_INTERVAL;
      }
      sleep(500);
    }
  }

  private boolean allTasksCompleted() {
    return tasks.stream().allMatch(task -> task.processor.getTaskCount() >= TASKS_PER_PROCESSOR);
  }

  private void sleep(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ignored) {
    }
  }

  private static class Task {
    Processor processor;

    public Task(Processor processor) {
      this.processor = processor;
    }
  }
}

class Processor {
  private final String name;
  private int taskCount = 0;

  public Processor(String name) {
    this.name = name;
  }

  public long[] performComputation(long[] times) {
    long start, time1, time2, time3;

    start = System.currentTimeMillis();
    multiplyMatrices(50);
    time1 = System.currentTimeMillis() - start;

    start = System.currentTimeMillis();
    int[] arr = generateRandomArray(5000);
    mergeSort(arr, 0, arr.length - 1);
    time2 = System.currentTimeMillis() - start;

    start = System.currentTimeMillis();
    computePrimes(100000);
    time3 = System.currentTimeMillis() - start;

    taskCount++;
    return new long[] { times[0] + time1, times[1] + time2, times[2] + time3 };
  }

  public int getTaskCount() {
    return taskCount;
  }

  private void multiplyMatrices(int size) {
    int[][] A = new int[size][size], B = new int[size][size], C = new int[size][size];
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        A[i][j] = B[i][j] = (int) (Math.random() * 10);
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        for (int k = 0; k < size; k++)
          C[i][j] += A[i][k] * B[k][j];
  }

  private void mergeSort(int[] arr, int left, int right) {
    if (left < right) {
      int mid = left + (right - left) / 2;
      mergeSort(arr, left, mid);
      mergeSort(arr, mid + 1, right);
      merge(arr, left, mid, right);
    }
  }

  private void merge(int[] arr, int left, int mid, int right) {
    int[] L = Arrays.copyOfRange(arr, left, mid + 1), R = Arrays.copyOfRange(arr, mid + 1, right + 1);
    int i = 0, j = 0, k = left;
    while (i < L.length && j < R.length)
      arr[k++] = (L[i] <= R[j]) ? L[i++] : R[j++];
    while (i < L.length)
      arr[k++] = L[i++];
    while (j < R.length)
      arr[k++] = R[j++];
  }

  private void computePrimes(int limit) {
    boolean[] primes = new boolean[limit + 1];
    Arrays.fill(primes, true);
    primes[0] = primes[1] = false;
    for (int p = 2; p * p <= limit; p++)
      if (primes[p])
        for (int i = p * p; i <= limit; i += p)
          primes[i] = false;
  }

  private int[] generateRandomArray(int size) {
    Random rand = new Random();
    return rand.ints(size, 0, 10000).toArray();
  }
}

public class ExecutionComparison {
  public static void main(String[] args) {
    long[] roundRobinTimes, threadTimes, loomTimes;

    // Round-Robin Execution
    TaskManager roundRobin = new TaskManager();
    roundRobin.addTask(new Processor("Object1"));
    roundRobin.addTask(new Processor("Object2"));
    roundRobin.addTask(new Processor("Object3"));
    roundRobinTimes = roundRobin.start();

    // Multi-threaded Execution
    threadTimes = runWithThreads();

    // Virtual Threads Execution (Loom)
    loomTimes = runWithVirtualThreads();

    writeResultsToFile(roundRobinTimes, threadTimes, loomTimes);
  }

  private static long[] runWithThreads() {
    ExecutorService executor = Executors.newFixedThreadPool(3);
    List<Future<long[]>> futures = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      futures.add(executor.submit(() -> new Processor("ThreadedProcessor").performComputation(new long[] { 0, 0, 0 })));
    }

    long[] totalTimes = { 0, 0, 0 };
    for (Future<long[]> future : futures) {
      try {
        long[] times = future.get();
        totalTimes[0] += times[0];
        totalTimes[1] += times[1];
        totalTimes[2] += times[2];
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    executor.shutdown();
    return totalTimes;
  }

  private static long[] runWithVirtualThreads() {
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    List<Future<long[]>> futures = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      futures.add(
          executor.submit(() -> new Processor("VirtualThreadProcessor").performComputation(new long[] { 0, 0, 0 })));
    }

    long[] totalTimes = { 0, 0, 0 };
    for (Future<long[]> future : futures) {
      try {
        long[] times = future.get();
        totalTimes[0] += times[0];
        totalTimes[1] += times[1];
        totalTimes[2] += times[2];
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    executor.shutdown();
    return totalTimes;
  }

  private static void writeResultsToFile(long[] roundRobinTimes, long[] threadTimes, long[] loomTimes) {
    try (FileWriter writer = new FileWriter("execution_times.txt")) {
      writer.write("Execution Time (ms) for Each Method:\n");
      writer.write("Round-Robin - Matrix: " + roundRobinTimes[0] + " Merge: " + roundRobinTimes[1] + " Primes: "
          + roundRobinTimes[2] + "\n");
      writer.write(
          "Threads - Matrix: " + threadTimes[0] + " Merge: " + threadTimes[1] + " Primes: " + threadTimes[2] + "\n");
      writer.write(
          "Virtual Threads - Matrix: " + loomTimes[0] + " Merge: " + loomTimes[1] + " Primes: " + loomTimes[2] + "\n");
      System.out.println("Results written to execution_times.txt");
    } catch (IOException e) {
      System.err.println("Error writing to file: " + e.getMessage());
    }
  }
}
