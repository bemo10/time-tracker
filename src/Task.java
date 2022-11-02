import java.lang.reflect.Array;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;

public class Task extends Container implements Observer {
  private ArrayList<Interval> intervals = new ArrayList<Interval>();
  private boolean isRunning = false;


  Task()
  {

  }

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

  private void updateTimeData()
  {
    if (!intervals.isEmpty())
    {
      this.initialDate = intervals.get(0).getInitialDate();
      this.finalDate = intervals.get(intervals.size()-1).getFinalDate();

      // Calcular tiempo total de esta tarea a partir del tiempo total de todos los intervalos
      this.totalTime = Duration.ofSeconds(0);
      intervals.forEach(interval -> {
        this.totalTime = this.totalTime.plus(interval.getTotalTime());
      });
    }
  }

  public void update(Object arg)
  {
    int timePassed = (int)arg;

    // Actualizar el ultimo intervalo
    Interval lastInterval = this.intervals.get(intervals.size()-1);
    //this.updateInterval(lastInterval);
    lastInterval.setFinalDate(LocalDateTime.now());
    lastInterval.setTotalTime(lastInterval.getTotalTime().plusMillis(timePassed));
    lastInterval.setInitialDate(lastInterval.getFinalDate().minus(lastInterval.getTotalTime()));
  }

  public void start()
  {
    isRunning = true;
    // Añadir un nuevo intervalo
    this.addInterval(new Interval());
  }

  public void stop()
  {
    isRunning = false;
    // Update last interval
    Interval lastInterval = this.intervals.get(intervals.size()-1);
    //this.updateInterval(lastInterval);
  }

  public void addInterval(Interval interval)
  {
    this.intervals.add(interval);
    interval.setParent(this);
  }

  public ArrayList<Interval> getIntervals()
  {
    return intervals;
  }

  /*public void updateInterval(Interval interval)
  {
    interval.setFinalDate(LocalDateTime.now());
    interval.calculateTotalTime();
  }*/

  public String toString()
  {
     String text = "Task " + '"' + this.getName() + '"' + " with id " + this.getId() +
        "  ->  START: " + this.getInitialDate()  +
        "  -  END: " + this.getFinalDate() +
        "  -  DURATION: " + this.getTotalTime();
     if (isRunning)
     {
       text += "   [ ▶ TASK IS RUNNING]";
     }

    return text;
  }

}
