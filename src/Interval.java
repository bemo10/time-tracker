import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// Un intervalo representa un perio de tiempo en el que una tarea ha estado ejecutando
public class Interval {
  private static int idGenerator = 0;
  private int id;
  private Task parent = null;
  private Duration totalTime;
  private LocalDateTime initialDate;
  private LocalDateTime finalDate;

  Interval()
  {
    generateNewId();
    this.totalTime = Duration.ofSeconds(0);
    this.initialDate = LocalDateTime.now();
    this.finalDate = LocalDateTime.now();
  }

  Interval(Task parent)
  {
    generateNewId();
    this.parent = parent;
    this.totalTime = Duration.ofSeconds(0);
    this.initialDate = LocalDateTime.now();
    this.finalDate = LocalDateTime.now();
  }

  Interval(int id, Duration totalTime, LocalDateTime initialDate, LocalDateTime finalDate)
  {
    this.id = id;
    this.totalTime = totalTime;
    this.initialDate = initialDate;
    this.finalDate = finalDate;
  }

  // Generar un Id unico para cada intervalo
  private void generateNewId()
  {
    this.id = this.idGenerator++;
  }

  public int getId()
  {
    return id;
  }

  public Task getParent()
  {
    return this.parent;
  }

  public void setParent(Task parent)
  {
    this.parent = parent;
  }

  public Duration getTotalTime()
  {
    return totalTime;
  }

  public LocalDateTime getInitialDate()
  {
    if (initialDate != null)
    {
      initialDate = initialDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return initialDate;
  }

  public LocalDateTime getFinalDate()
  {
    if (finalDate != null)
    {
      finalDate = finalDate.truncatedTo(ChronoUnit.SECONDS);
    }
    return finalDate;
  }

  public void setTotalTime(Duration duration)
  {
    this.totalTime = duration;
  }

  public void setInitialDate(LocalDateTime date)
  {
    this.initialDate = date;
  }

  public void setFinalDate(LocalDateTime date)
  {
    this.finalDate = date;
  }

  public String toString()
  {
    return "Interval with id " + this.getId() +
        "  ->  START: " + ( this.getFinalDate() != null? this.getInitialDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null ) +
        "  -  END: " + ( this.getFinalDate() != null? this.getFinalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null ) +
        "  -  DURATION: " + this.getTotalTime().getSeconds() + "s";
  }
}
