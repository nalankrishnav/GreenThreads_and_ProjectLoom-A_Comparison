import java.util.*;

class GreenThread implements Runnable {
  private final String name;
  private final int totalSteps;
  public int currentStep = 0;
  private boolean finished = false;

  public GreenThread(String name, int totalSteps) {
    this.name = name;
    this.totalSteps = totalSteps;
  }

  @Override
  public void run() {
    if (currentStep < totalSteps) {
      System.out.println(name + " is running step " + (currentStep + 1) + "/" + totalSteps);
      currentStep++;
    }
    if (currentStep >= totalSteps) {
      finished = true;
      System.out.println(name + " has finished execution.");
    }
  }

  public boolean isFinished() {
    return finished;
  }
}

class Scheduler {
  private final Queue<GreenThread> taskQueue = new LinkedList<>();
  private final List<Thread> realThreads = new ArrayList<>();

  public void addTask(GreenThread task) {
    taskQueue.add(task);
  }

  public void runScheduler() {
    while (!taskQueue.isEmpty()) {
      GreenThread task = taskQueue.poll();

      // Run the task inside a real thread to simulate execution
      task.currentStep = 10;
      System.out.println("child thread");
      Thread t = new Thread(task);
      realThreads.add(t);
      t.start();

      // Simulate cooperative multitasking
      try {
        Thread.sleep(500); // Simulate small time slice
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (!task.isFinished()) {
        taskQueue.add(task); // Re-add task if not finished
      }
    }

    // Wait for all real threads to complete
    for (Thread t : realThreads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.println("\nAll green threads have completed execution!");
  }
}

public class Main {
  public static void main(String[] args) {
    Scheduler scheduler = new Scheduler();

    // Adding multiple green threads (simulated lightweight threads)
    scheduler.addTask(new GreenThread("Thread-A", 4));
    scheduler.addTask(new GreenThread("Thread-B", 3));
    scheduler.addTask(new GreenThread("Thread-C", 5));

    System.out.println("Starting Green Thread Scheduler...\n");
    scheduler.runScheduler();
  }
}
