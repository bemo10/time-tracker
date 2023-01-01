package core;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase es un Observable. Notificamos a los observers cada periodo de tiempo
 */
public class TimeManager extends Observable {
  private static TimeManager instance = null;
  // Time in milliseconds to notify observers
  private static int tickDelay = 2000;
  private Timer timer = new Timer();
  public static final Logger logger = LoggerFactory.getLogger(Main.class);

  private boolean invariant() {
    return (tickDelay >= 0 && timer != null);
  }

  /**
   * Devolver el singleton de esta clase.
   */
  public static TimeManager getInstance() {
    if (instance == null) {
      instance = new TimeManager();
    }

    return instance;
  }

  /**
   *  Esta funcion crea un nuevo thread donde notificamos a los observers
   *  cada periodo de tiempo "tickDelay".
   */
  public void tick() {
    assert invariant();

    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        logger.info(" ### Time Manager: Tick executed! ###");
        setChanged();
        // Notificar a los observers y pasarles como argumento la variable "tickDelay"
        notifyObservers(tickDelay);
      }
    };

    this.timer.scheduleAtFixedRate(task, 0, tickDelay);
  }

  /**
   *  Metodo que devuelve el tickDelay siempre que sea myor que 0 .
   */
  public static void setTickDelay(int delay) {
    // Precondition
    if (delay < 0) {
      throw new IllegalArgumentException("delay cannot be less than zero");
    }
    tickDelay = delay;
  }

  // Empezar la ejecucion de tick
  public static void startTick() {
    getInstance().tick();
  }

  /**
   *  Detener al thread creado en la funcion tick().
   */
  public static void stopTick() {
    if (instance != null) {
      instance.timer.cancel();
      instance.timer.purge();
    }
  }
}