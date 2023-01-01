package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONObject;
import java.time.format.DateTimeFormatter;

/**
 * De esta clase heredan las clases de tareas y proyectos.
 */
public abstract class Container {
  private static int idGenerator = 0;
  protected int id;
  protected String name = "";
  protected Container parent = null;
  protected Duration totalTime = Duration.ofSeconds(0);
  protected LocalDateTime initialDate = null;
  protected LocalDateTime finalDate = null;
  protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  protected ArrayList<String> tags = new ArrayList<>();

  public Container() {
    this.name = "";
    this.parent = null;

    assert invariant();
  }

  public Container(String name) {
    generateNewId();
    this.name = name;

    assert invariant();
  }

  public Container(int id, String name, Duration totalTime, LocalDateTime initialDate,
            LocalDateTime finalDate) {
    // Precondition
    if (totalTime == null) {
      throw new IllegalArgumentException("totalTime is null");
    }

    this.id = id;
    this.name = name;
    this.totalTime = totalTime;
    this.initialDate = initialDate;
    this.finalDate = finalDate;

    assert invariant();
  }

  private boolean invariant() {
    return (id >= 0 && totalTime != null);
  }


  // Esta funcion sirve para generar un id unico por cada contenedor (tarea / proyecto)
  private void generateNewId() {
    this.id = this.idGenerator++;

    assert invariant();
  }

  public int getId() {
    assert invariant();
    return id;
  }

  public String getName() {
    return name;
  }

  public Container getParent() {
    return parent;
  }

  public abstract Duration getTotalTime();

  public abstract LocalDateTime getInitialDate();

  public abstract LocalDateTime getFinalDate();

  /**
   * Metodo que modifica la duracion de una clase especial.
   */
  public void setTotalTime(Duration duration) {
    // Precondition
    if (totalTime == null) {
      throw new IllegalArgumentException("totalTime is null");
    }
    totalTime = duration;
  }

  public void setInitialDate(LocalDateTime date) {
    initialDate = date;
  }

  public void setFinalDate(LocalDateTime date) {
    finalDate = date;
  }

  public void setParent(Container parent) {
    this.parent = parent;
  }

  /**
   * Metodo getter devuelve los valores de la lista .
   */
  public ArrayList<String> getTags() {
    return tags;
  }

  /**
   * Metodo que añade un valor de la lista .
   */
  public void addTag(String tag) {
    // Precondition
    if (tag == null) {
      throw new IllegalArgumentException("tag is null");
    }
    this.tags.add(tag.toLowerCase());
  }

  public abstract JSONObject toJson(int depth);
  protected void toJson(JSONObject json) {
    json.put("id", id);
    json.put("name", name);
    json.put("initialDate", initialDate==null
        ? JSONObject.NULL : formatter.format(initialDate));
    json.put("finalDate", finalDate==null
        ? JSONObject.NULL : formatter.format(finalDate));
    json.put("duration", totalTime.toSeconds());
  }


  // Esta funcion hace un print de este contenedor y todos sus padres
  public abstract void printChanged();

  public abstract String toString();
}
