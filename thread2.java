import java.util.*;

class TaskManager {
  private final List<Task> tasks = new ArrayList<>();
  private int currentTaskIndex = 0;
  private long switchTime;
  private static final int SWITCH_INTERVAL = 1000; // Switch every 1 second
  private long startTime; // Track total execution time
  private static final int MAX_COUNT = 15; // Stop when all tasks reach this count

  public void addTask(Processor obj) {
    tasks.add(new Task(obj));
  }

  public void start() {
    if (tasks.isEmpty()) {
      System.out.println("No tasks to execute!");
      return;
    }

    startTime = System.currentTimeMillis(); // Start time tracking
    switchTime = startTime + SWITCH_INTERVAL;

    while (true) {
      long currentTime = System.currentTimeMillis();

      // Run the current task
      Task currentTask = tasks.get(currentTaskIndex);
      currentTask.processor.performTask();

      // Check if all tasks have reached MAX_COUNT
      if (allTasksCompleted()) {
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("\nAll tasks completed!");
        printFinalResults();
        System.out.println("Total Execution Time: " + totalTime + "ms");
        return; // Exit program
      }

      // Switch task after interval
      if (currentTime >= switchTime) {
        currentTaskIndex = (currentTaskIndex + 1) % tasks.size(); // Move to next task
        switchTime = currentTime + SWITCH_INTERVAL;
        System.out.println("Switching to next task...\n");
      }

      sleep(500); // Simulate processing time
    }
  }

  private boolean allTasksCompleted() {
    for (Task task : tasks) {
      if (task.processor.getCounter() < MAX_COUNT) {
        return false;
      }
    }
    return true;
  }

  private void printFinalResults() {
    System.out.println("\nFinal Results:");
    for (Task task : tasks) {
      System.out.println(task.processor.getName() + " counter: " + task.processor.getCounter());
    }
  }

  private void sleep(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  // ✅ **Fix: Added Task Inner Class**
  private static class Task {
    Processor processor;

    public Task(Processor processor) {
      this.processor = processor;
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
    if (counter < 15) {
      counter++;
      System.out.println(name + " is executing... Count: " + counter);
    }
  }

  public int getCounter() {
    return counter;
  }

  public String getName() {
    return name;
  }
}

// Main Class
public class thread2 {
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
