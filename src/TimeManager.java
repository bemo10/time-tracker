import java.util.Timer;
import java.util.TimerTask;

public class TimeManager extends Observable {
  // Time in milliseconds to notify observers
  private int tickDelay = 2000;
  private Timer timer = new Timer();

  public void tick()
  {
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        System.out.println(" ### Time Manager: Tick executed! ###");
        setChanged();
        notifyObservers(tickDelay);
      }
    };

    this.timer.scheduleAtFixedRate(task, 0, tickDelay);
  }

  public void setTickDelay(int tickDelay)
  {
    this.tickDelay = tickDelay;
  }

  public void stopTick()
  {
    timer.cancel();
    timer.purge();
  }
}
