import java.util.Timer;
import java.util.TimerTask;


// Forma parte del patron observer. Esta clase es un Observable. Sirve para notificar a los observers cada periodo de tiempo
public class TimeManager extends Observable {
  // Time in milliseconds to notify observers
  private int tickDelay = 2000;
  private Timer timer = new Timer();

  // Esta funcion crea un nuevo thread donde notificamos a los observers cada periodo de tiempo "tickDelay"
  public void tick()
  {
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        System.out.println(" ### Time Manager: Tick executed! ###");
        setChanged();
        // Notificar a los observers y pasarles como argumento la variable "tickDelay"
        notifyObservers(tickDelay);
      }
    };

    this.timer.scheduleAtFixedRate(task, 0, tickDelay);
  }

  public void setTickDelay(int tickDelay)
  {
    this.tickDelay = tickDelay;
  }

  // Detener al thread creado en la funcion tick()
  public void stopTick()
  {
    timer.cancel();
    timer.purge();
  }
}
