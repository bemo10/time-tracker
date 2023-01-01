package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.json.JSONArray;
import java.time.format.DateTimeFormatter;

/**
 * Un Task es uno de los contenedores que un usuario puede crear dentro del arbol de contenedores.
 * Las tareas tambien son parte del patron de Observer. Dentro del patron las tareas
 * son Observers mientras que la clase TimeManager es el Observable
 * Las tareas pueden contener intervalos. Cada vez que una tarea se añade a TimeManager
 * como Observer, esta crea un nuevo intervalo
 */
public class Task extends Container implements Observer {
  private ArrayList<Interval> intervals = new ArrayList<>();
  private boolean active = false;
  public static final Logger logger = LoggerFactory.getLogger(Main.class);

  public Task(String name) {
    super(name);

    assert invariant();
  }

  public Task(int id, String name, Duration totalTime, LocalDateTime initialDate,
       LocalDateTime finalDate) {
    super(id, name, totalTime, initialDate, finalDate);

    assert invariant();
  }

  protected boolean invariant() {
    return (intervals != null && totalTime != null);
  }

  /**
   *  Metodo que devuelve la duración total del tiempo de una clase Duration.
   */
  public Duration getTotalTime() {
    this.updateTimeData();

    assert invariant();
    return totalTime;
  }

  /**
   * Metodo que devuelve el tiempo inicial.
   */
  public LocalDateTime getInitialDate() {
    this.updateTimeData();
    if (initialDate != null) {
      initialDate = initialDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return initialDate;
  }

  /**
   * Metodo que devuelve el tiempo final.
   */
  public LocalDateTime getFinalDate() {
    this.updateTimeData();
    if (finalDate != null) {
      finalDate = finalDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return finalDate;
  }

  // Calcular fecha inicial, fecha final y tiempo total a partir de los intervalos
  private void updateTimeData() {
    assert invariant();

    if (!intervals.isEmpty())  {
      // La fecha inicial de esta tarea es la fecha inicial del primer intervalo mientras
      // que la fecha final es la fecha final del ultimo intervalo
      this.initialDate = intervals.get(0).getInitialDate();
      this.finalDate = intervals.get(intervals.size() - 1).getFinalDate();

      // Calcular tiempo total de esta tarea a partir del tiempo total de todos los intervalos
      this.totalTime = Duration.ofSeconds(0);
      intervals.forEach(interval -> {
        this.totalTime = this.totalTime.plus(interval.getTotalTime());
      });
    }

    assert invariant();
  }

  /**
   * Esta funcion es una implementacion de la funcion update() del interfaz Observer.
   * Sive para actualizar la fecha inicial y final y el tiempo total del ultimo intervalo
   */
  public void update(Observable obs, Object arg) {
    assert invariant();
    // Precondition
    if (arg == null) {
      throw new IllegalArgumentException("arg is null");
    }
    // El argumento que recibimos del Observable es el tiempo que a parasado desde el ultimo tick
    int timePassed = (int) arg;

    // Actualizar el ultimo intervalo
    Interval lastInterval = this.intervals.get(intervals.size() - 1);
    lastInterval.setFinalDate(LocalDateTime.now());
    lastInterval.setTotalTime(lastInterval.getTotalTime().plusMillis(timePassed));
    lastInterval.setInitialDate(lastInterval.getFinalDate().minus(lastInterval.getTotalTime()));

    // Print de los cambios que han ocurrido
    printChanged();

    assert invariant();
  }

  /**
   * Hace print de esta tarea, su ultimo intervalo y todos sus padres. Es util para hacer
   * un print cuando ocurre un cambio en las fechas o tiempo total de esta tarea
   */
  public void printChanged() {
    assert invariant();

    logger.info(intervals.get(intervals.size() - 1).toString());
    logger.info(this.toString());

    if (parent != null) {
      parent.printChanged();
    }
  }

  /**
   * Implementacion de la funcion start() del interfaz Observer. El Observable llama
   * a esta funcion cuando esta tarea es añadida a la lista de Observers.
   * Sirve para crear un nuevo intervalo y actualizar la variable "isRunning"
   */
  public void start() {
    assert invariant();

    // Si el ultimo intervalo aun sigue activo hay que desactivarlo
    if (this.intervals.size() > 0) {
      if (this.intervals.get(this.intervals.size() - 1).getActive() == true) {
        this.stop();
      }
    }

    // Activar esta tarea
    active = true;

    // Añadir un nuevo intervalo
    this.addInterval(new Interval());

    // Activar intervalo
    this.intervals.get(intervals.size() - 1).setActive(true);

    // Añadir esta tarea al Observable
    TimeManager.getInstance().addObserver(this);
  }

  /**
   * implementacion de la funcion stop() del interfaz Observer. El Observable llama a esta
   * funcion cuando esta tarea es eliminada de la lista de Observers.
   * Sirve para actualizar la variable "isRunning"
   */
  public void stop() {
    active = false;

    // Desactivar intervalo
    this.intervals.get(intervals.size() - 1).setActive(false);

    // Quitar esta tarea al Observable
    TimeManager.getInstance().deleteObserver(this);
  }

  /**
   *  Metodo que añade un nuevo intervalo .
   */
  public void addInterval(Interval interval) {
    assert invariant();
    // Precondition
    if (interval == null) {
      throw new IllegalArgumentException("interval is null");
    }
    this.intervals.add(interval);
    interval.setParent(this);

    assert invariant();
  }

  public ArrayList<Interval> getIntervals() {
    assert invariant();
    return intervals;
  }

  public JSONObject toJson(int depth) {
    // depth not used here
    JSONObject json = new JSONObject();
    json.put("class", "task");
    super.toJson(json);
    json.put("active", active);
    if (depth>0) {
      JSONArray jsonIntervals = new JSONArray();
      for (Interval interval : intervals) {
        jsonIntervals.put(interval.toJson());
      }
      json.put("intervals", jsonIntervals);
    } else {
      json.put("intervals", new JSONArray());
    }
    return json;
  }

  /**
   * Metodo que printa la clase task en tiempo .
   */
  public String toString() {
    logger.trace("Starting task {}", this.getName());
    String text = "Task " + '"' + this.getName() + '"' + " with id " + this.getId()
        + "  ->  START: " + (this.getFinalDate() != null ? this.getInitialDate()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
        + "  -  END: " + (this.getFinalDate() != null ? this.getFinalDate()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
        + "  -  DURATION: " + this.getTotalTime().getSeconds() + "s";
    /*
    if (active) {
         text += "   [ ▶ TASK IS RUNNING]";
       }
    */

    return text;
  }

}
