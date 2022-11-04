import java.lang.reflect.Array;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

// Un Task es uno de los contenedores que un usuario puede crear dentro del arbol de contenedores.
// Las tareas tambien son parte del patron de Observer. Dentro del patron las tareas son Observers mientras que la clase TimeManager es el Observable
// Las tareas pueden contener intervalos. Cada vez que una tarea se añade a TimeManager como Observer, esta crea un nuevo intervalo
public class Task extends Container implements Observer {
  private ArrayList<Interval> intervals = new ArrayList<>();
  private boolean isRunning = false;

  Task() {}

  Task(String name)
  {
    super(name);
  }

  Task(int id, String name, Duration totalTime, LocalDateTime initialDate, LocalDateTime finalDate)
  {
    super(id, name, totalTime, initialDate, finalDate);
  }

  public Duration getTotalTime()
  {
    this.updateTimeData();
    return totalTime;
  }

  public LocalDateTime getInitialDate()
  {
    this.updateTimeData();
    if (initialDate != null)
    {
      initialDate = initialDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return initialDate;
  }

  public LocalDateTime getFinalDate()
  {
    this.updateTimeData();
    if (finalDate != null)
    {
      finalDate = finalDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return finalDate;
  }

  // Calcular fecha inicial, fecha final y tiempo total a partir de los intervalos
  private void updateTimeData()
  {
    if (!intervals.isEmpty())
    {
      // La fecha inicial de esta tarea es la fecha inicial del primer intervalo mientras que la fecha final es la fecha final del ultimo intervalo
      this.initialDate = intervals.get(0).getInitialDate();
      this.finalDate = intervals.get(intervals.size()-1).getFinalDate();

      // Calcular tiempo total de esta tarea a partir del tiempo total de todos los intervalos
      this.totalTime = Duration.ofSeconds(0);
      intervals.forEach(interval -> {
        this.totalTime = this.totalTime.plus(interval.getTotalTime());
      });
    }
  }

  // Esta funcion es una implementacion de la funcion update() del interfaz Observer. Sive para actualizar la fecha inicial y final y el tiempo total del ultimo intervalo
  public void update(Object arg)
  {
    // El argumento que recibimos del Observable es el tiempo que a parasado desde el ultimo tick
    int timePassed = (int)arg;

    // Actualizar el ultimo intervalo
    Interval lastInterval = this.intervals.get(intervals.size()-1);
    lastInterval.setFinalDate(LocalDateTime.now());
    lastInterval.setTotalTime(lastInterval.getTotalTime().plusMillis(timePassed));
    lastInterval.setInitialDate(lastInterval.getFinalDate().minus(lastInterval.getTotalTime()));
  }

  // Implementacion de la funcion start() del interfaz Observer. El Observable llama a esta funcion cuando esta tarea es añadida a la lista de Observers.
  // Sirve para crear un nuevo intervalo y actualizar la variable "isRunning"
  public void start()
  {
    isRunning = true;
    // Añadir un nuevo intervalo
    this.addInterval(new Interval());
  }

  // implementacion de la funcion stop() del interfaz Observer. El Observable llama a esta funcion cuando esta tarea es eliminada de la lista de Observers.
  // Sirve para actualizar la variable "isRunning"
  public void stop()
  {
    isRunning = false;
  }

  // Añadir un nuevo intervalo
  public void addInterval(Interval interval)
  {
    this.intervals.add(interval);
    interval.setParent(this);
  }

  public ArrayList<Interval> getIntervals()
  {
    return intervals;
  }

  public String toString()
  {
     String text = "Task " + '"' + this.getName() + '"' + " with id " + this.getId() +
        "  ->  START: " + ( this.getFinalDate() != null? this.getInitialDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null )  +
        "  -  END: " + ( this.getFinalDate() != null? this.getFinalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null ) +
        "  -  DURATION: " + this.getTotalTime().getSeconds() + "s";
     if (isRunning)
     {
       text += "   [ ▶ TASK IS RUNNING]";
     }

    return text;
  }

}
