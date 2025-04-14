public class Loom {
  public static void main(String[] args) {
    try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < 10; i++) {
        int taskId = i;
        executor.submit(() -> {
          System.out.println("Running task " + taskId + " on " + Thread.currentThread());
          try {
            Thread.sleep(1000); // Simulate work
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        });
      }
    } // Executor closes automatically
  }
}
