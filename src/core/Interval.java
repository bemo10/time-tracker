package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import java.time.format.DateTimeFormatter;

/**
 * Un intervalo representa un periodo de tiempo en el que una tarea ha estado ejecutando.
 */
public class Interval {
  private static int idGenerator = 0;
  private int id;
  private Task parent = null;
  private Duration totalTime = Duration.ofSeconds(0);
  private LocalDateTime initialDate;
  private LocalDateTime finalDate;
  protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private boolean active;
  public static final Logger logger = LoggerFactory.getLogger(Main.class);

  public Interval() {
    generateNewId();
    this.initialDate = LocalDateTime.now();
    this.finalDate = LocalDateTime.now();

    assert invariant();
  }

  public Interval(Task parent) {
    generateNewId();
    this.parent = parent;
    this.initialDate = LocalDateTime.now();
    this.finalDate = LocalDateTime.now();

    assert invariant();
  }

  public Interval(int id, Duration totalTime, LocalDateTime initialDate, LocalDateTime finalDate) {
    this.id = id;
    this.totalTime = totalTime;
    this.initialDate = initialDate;
    this.finalDate = finalDate;

    assert invariant();
  }

  private boolean invariant() {
    return (id >= 0 && totalTime != null);
  }

  // Generar un Id unico para cada intervalo
  private void generateNewId() {
    this.id = this.idGenerator++;
    assert invariant();
  }

  public int getId() {
    assert invariant();
    return id;
  }

  public Task getParent() {
    return this.parent;
  }

  /**
   * Metodo que modifica al padre de una tarea .
   */
  public void setParent(Task parent) {
    // Precondition
    if (parent == null) {
      throw new IllegalArgumentException("parent is null");
    }
    this.parent = parent;
  }

  public Duration getTotalTime() {
    assert invariant();
    return totalTime;
  }

  /**
   * Metodo que devuelve el tiempo inicial .
   */
  public LocalDateTime getInitialDate() {
    if (initialDate != null) {
      initialDate = initialDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return initialDate;
  }

  /**
   * Metodo que devuelve el tiempo final .
   */
  public LocalDateTime getFinalDate()  {
    if (finalDate != null) {
      finalDate = finalDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return finalDate;
  }

  /**
   * Metodo que modifica el tiempo de una clase Duration .
   */
  public void setTotalTime(Duration duration) {
    // Precondition
    if (duration == null) {
      throw new IllegalArgumentException("duration is null");
    }
    this.totalTime = duration;
    assert invariant();
  }

  public void setInitialDate(LocalDateTime date)  {
    this.initialDate = date;
  }

  public void setFinalDate(LocalDateTime date)  {
    this.finalDate = date;
  }

  public boolean getActive() { return this.active; }
  public void setActive(boolean state) { this.active = state; }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("class", "interval");
    json.put("id", id);
    json.put("initialDate", initialDate==null
        ? JSONObject.NULL : formatter.format(initialDate));
    json.put("finalDate", finalDate==null
        ? JSONObject.NULL : formatter.format(finalDate));
    json.put("duration", totalTime.toSeconds());
    json.put("active", active);
    return json;
  }

  /**
   * Metodo que printa la clase intervalo en tiempo .
   */
  public String toString()  {
    return "Interval with id " + this.getId() + "  ->  START: " + (this.getFinalDate() != null
        ? this.getInitialDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        : null) + "  -  END: " + (this.getFinalDate() != null
        ? this.getFinalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
        + "  -  DURATION: " + this.getTotalTime().getSeconds() + "s";
  }
}
