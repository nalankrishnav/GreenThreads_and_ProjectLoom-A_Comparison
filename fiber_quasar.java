import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;

public class fiber_quasar {
  public static void main(String[] args) throws Exception {
    Fiber<Void> fiber = new Fiber<Void>(() -> {
      for (int i = 0; i < 5; i++) {
        System.out.println("Fiber iteration: " + i);
        Fiber.sleep(500);
      }
    }).start();

    fiber.join();
  }
}
