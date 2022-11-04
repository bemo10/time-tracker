import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


// Un Project es uno de los contenedores que un usuario puede crear dentro del arbol de contenedores. Los proyectos pueden contener hijos que pueden ser proyectos y/o tareas.
public class Project extends Container {
  ArrayList<Container> children = new ArrayList<>();

  Project() {}

  Project(String name)
  {
    super(name);
  }

  Project(int id, String name, Duration totalTime, LocalDateTime initialDate, LocalDateTime finalDate)
  {
    super(id, name, totalTime, initialDate, finalDate);
  }

  // Añadir un nuevo hijo
  public void addChild(Container child)
  {
    children.add(child);
    child.setParent(this);
  }

  public Duration getTotalTime()
  {
    updateTimeData();
    return totalTime;
  }

  public LocalDateTime getInitialDate()
  {
    updateTimeData();
    if (initialDate != null)
    {
      initialDate = initialDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return initialDate;
  }

  public LocalDateTime getFinalDate()
  {
    updateTimeData();
    if (finalDate != null)
    {
      finalDate = finalDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return finalDate;
  }

  // Calcular fecha inicial, fecha final y tiempo total a partir de hijos
  private void updateTimeData()
  {
    this.totalTime = Duration.ofSeconds(0);

    this.children.forEach(container -> {
      // Comprobar y asignar fecha inicial
      if (container.getInitialDate() != null)
      {
        if ( (this.initialDate == null) || (container.getInitialDate().isBefore(this.initialDate)) )
        {
          this.initialDate = container.getInitialDate();
        }
      }

      // Comprobar y asignar fecha final
      if (container.getFinalDate() != null)
      {
        if ( (this.finalDate == null) || (container.getFinalDate().isAfter(this.finalDate)) )
        {
          this.finalDate = container.getFinalDate();
        }
      }

      // Asignar tiempo total
      this.totalTime = this.totalTime.plus(container.getTotalTime());
    });
  }

  public ArrayList<Container> getChildren()
  {
    return children;
  }

  // Hacer un print del sub-arbol que tiene este proyecto como raiz
  public void printSubTree()
  {
    System.out.println("Root " + this.toString() + " --->");


    for(TreeIterator it = this.createIterator().first(); it != null; it = it.next())
    {
      // Decoracion de texto
      String indentation = "";
      Project parent = (Project)it.getElement().getParent();
      while(parent != null)
      {
        indentation += "    ";
        parent = (Project)parent.getParent();
      }
      indentation += "|---";

      // Print de proyectos y tareas
      System.out.println(indentation + " " + it.getElement().toString());

      // Print de intervalos
      if (it.getElement() instanceof Task)
      {
        String finalIndentation = indentation;
        ((Task) it.getElement()).getIntervals().forEach(interval -> {
          System.out.println("    " + finalIndentation + " " + interval.toString());
        });
      }
    }
  }

  // Crear y devolver un iterador que tiene como raiz este proyecto
  public TreeIterator createIterator()
  {
    return new TreeIterator(this);
  }

  public String toString()
  {

    String me = "Project ";
    if (this.getName() != "")
    {
      me += '"' + this.getName() + '"' + " ";
    }
    me += "with id " + this.getId() +
        "  ->  START: " + ( this.getFinalDate() != null? this.getInitialDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null )  +
        "  -  END: " + ( this.getFinalDate() != null? this.getFinalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null ) +
        "  -  DURATION: " + this.getTotalTime().getSeconds() + "s";

    return me;
  }

}
