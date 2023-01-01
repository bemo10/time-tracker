package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.SearchStrategy;
import org.json.JSONObject;
import org.json.JSONArray;
import java.time.format.DateTimeFormatter;

/**
 * Un Project es uno de los contenedores que un usuario puede crear
 * dentro del arbol de contenedores.
 * Los proyectos pueden contener hijos que pueden ser proyectos y/o tareas.
 */
public class Project extends Container {
  private ArrayList<Container> children = new ArrayList<>();
  private SearchStrategy searchObject;
  public static final Logger logger = LoggerFactory.getLogger(Main.class);

  public Project() {
    super();

    assert invariant();
  }

  public Project(String name) {
    super(name);

    assert invariant();
  }

  public Project(int id, String name, Duration totalTime, LocalDateTime initialDate,
          LocalDateTime finalDate) {
    super(id, name, totalTime, initialDate, finalDate);

    assert invariant();
  }

  protected boolean invariant() {
    return (children != null && totalTime != null);
  }

  /**
   * Metodo que añade un nuevo intervalo .
   */
  public void addChild(Container child) {
    // Precondition
    if (child == null) {
      throw new IllegalArgumentException("child is null");
    }
    if (child == this) {
      throw new IllegalArgumentException("child cannot be this same container");
    }
    children.add(child);
    child.setParent(this);

    assert invariant();
  }

  /**
   * Metodo que devuelve el tiempo total .
   */
  public Duration getTotalTime() {
    updateTimeData();

    assert invariant();
    return totalTime;
  }

  /**
   * Metodo que devuelve el tiempo inicial .
   */
  public LocalDateTime getInitialDate() {
    updateTimeData();
    if (initialDate != null) {
      initialDate = initialDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return initialDate;
  }

  /**
   * Metodo que devuelve el tiempo final .
   */
  public LocalDateTime getFinalDate() {
    updateTimeData();
    if (finalDate != null) {
      finalDate = finalDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return finalDate;
  }

  // Calcular fecha inicial, fecha final y tiempo total a partir de hijos
  private void updateTimeData() {
    assert invariant();

    this.totalTime = Duration.ofSeconds(0);

    this.children.forEach(container -> {
      // Comprobar y asignar fecha inicial
      if (container.getInitialDate() != null) {
        if ((this.initialDate == null) || (container.getInitialDate().isBefore(this.initialDate))) {
          this.initialDate = container.getInitialDate();
        }
      }

      // Comprobar y asignar fecha final
      if (container.getFinalDate() != null) {
        if ((this.finalDate == null) || (container.getFinalDate().isAfter(this.finalDate))) {
          this.finalDate = container.getFinalDate();
        }
      }

      // Asignar tiempo total
      this.totalTime = this.totalTime.plus(container.getTotalTime());
    });

    assert invariant();
  }


  /**
   * Metodo que obtiene el hijo de un proyecto.
   */
  public ArrayList<Container> getChildren() {
    assert invariant();

    return children;
  }

  /**
   *  Hacer un print del sub-arbol que tiene este proyecto como raiz .
   */
  public void printSubTree() {
    assert invariant();

    System.out.println("Root " + this.toString() + " --->");

    for (TreeIterator it = this.createIterator().first(); it != null; it = it.next()) {
      // Decoracion de texto
      String indentation = "";
      Project parent = (Project) it.getElement().getParent();
      while (parent != null) {
        indentation += "    ";
        parent = (Project) parent.getParent();
      }
      indentation += "|---";

      // Print de proyectos y tareas
      System.out.println(indentation + " " + it.getElement().toString());

      // Print de intervalos
      if (it.getElement() instanceof Task) {
        String finalIndentation = indentation;
        ((Task) it.getElement()).getIntervals().forEach(interval -> {
          System.out.println("    " + finalIndentation + " " + interval.toString());
        });
      }
    }
  }

  /**
   * Hace print de este proyecto y todos sus padres. Es util para hacer un print
   * cuando ocurre un cambio en las fechas o tiempo total de este proyecto
   */
  public void printChanged() {
    logger.info(this.toString());

    if (parent != null) {
      parent.printChanged();
    }
  }

  // Crear y devolver un iterador que tiene como raiz este proyecto
  public TreeIterator createIterator() {
    return new TreeIterator(this);
  }

  /**
   * Metodo que llama a la funcion search dentro de un objeto filter.
   */
  public ArrayList<Container> search(Object filter) {
    // Precondition
    if (filter == null) {
      throw new IllegalArgumentException("filter is null");
    }
    return searchObject.search(this, filter);
  }

  /**
   * Metodo que modifica el strategy de una clase SearchStrategy .
   */
  public void setSearchStrategy(SearchStrategy strategy) {
    // Precondition
    if (strategy == null) {
      throw new IllegalArgumentException("strategy is null");
    }
    searchObject = strategy;
  }

  public JSONObject toJson(int depth) {
    JSONObject json = new JSONObject();
    json.put("class", "project");
    super.toJson(json);
    if (depth>0) {
      JSONArray jsonActivities = new JSONArray();
      for (Container child : children) {
        jsonActivities.put(child.toJson(depth - 1));
        // important: decrement depth
      }
      json.put("activities", jsonActivities);
    }
    return json;
  }

  public Container findContainerById(int id) {
    if (this.id == id) {
      return this;
    }

    for (TreeIterator it = this.createIterator().first(); it != null; it = it.next()) {
      if (it.getElement().getId() == id) {
        return it.getElement();
      }
    }

    return null;
  }

  /**
   * Metodo que devuelve un string para hacer el logging con la info .
   */
  public String toString() {
    logger.trace("Starting Project {}", this.getName());

    String me = "Project ";
    if (this.getName() != "") {
      me += '"' + this.getName() + '"' + " ";
    }
    me += " with id " + this.getId() + "  ->  START: "
        + (this.getFinalDate() != null ? this.getInitialDate()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)  + "  -  END: "
        +  (this.getFinalDate() != null ? this.getFinalDate()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
        +  "  -  DURATION: " + this.getTotalTime().getSeconds() + "s";

    return me;
  }

}
