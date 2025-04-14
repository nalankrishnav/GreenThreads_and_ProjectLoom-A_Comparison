import java.util.ArrayList;
import java.util.List;

class TaskManager {
  private final List<Task> tasks = new ArrayList<>();
  private long startTime;
  private static final int MAX_COUNT = 15; // Stop when all tasks reach this count

  public void addTask(Processor processor) {
    tasks.add(new Task(processor));
  }

  public void start() {
    if (tasks.isEmpty()) {
      System.out.println("No tasks to execute!");
      return;
    }

    startTime = System.currentTimeMillis(); // Start time tracking

    // Create and start threads for each task
    List<Thread> threads = new ArrayList<>();
    for (Task task : tasks) {
      Thread thread = new Thread(task);
      threads.add(thread);
      thread.start();
    }

    // Wait for all threads to complete execution
    for (Thread thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // Print results and total execution time
    long totalTime = System.currentTimeMillis() - startTime;
    System.out.println("\nAll tasks completed!");
    printFinalResults();
    System.out.println("Total Execution Time: " + totalTime + "ms");
  }

  private void printFinalResults() {
    System.out.println("\nFinal Results:");
    for (Task task : tasks) {
      System.out.println(task.processor.getName() + " counter: " + task.processor.getCounter());
    }
  }

  // Task class implementing Runnable for threading
  private static class Task implements Runnable {
    private final Processor processor;

    public Task(Processor processor) {
      this.processor = processor;
    }

    @Override
    public void run() {
      while (processor.getCounter() < MAX_COUNT) {
        processor.performTask();
        try {
          Thread.sleep(500); // Simulating time for each execution
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}

// Example Objects
class Processor {
  private final String name;
  private int counter = 0; // Tracks execution count

  public Processor(String name) {
    this.name = name;
  }

  public void performTask() {
    counter++;
    System.out.println(name + " is executing... Count: " + counter);
  }

  public int getCounter() {
    return counter;
  }

  public String getName() {
    return name;
  }
}

// Main Class
public class thread {
  public static void main(String[] args) {
    TaskManager manager = new TaskManager();

    // Creating objects
    Processor obj1 = new Processor("Object1");
    Processor obj2 = new Processor("Object2");
    Processor obj3 = new Processor("Object3");

    // Assigning objects to the task manager
    manager.addTask(obj1);
    manager.addTask(obj2);
    manager.addTask(obj3);

    // Start execution loop
    manager.start();
  }
}
