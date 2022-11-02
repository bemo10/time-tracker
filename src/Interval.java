import java.time.LocalDateTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;


public class Interval {
  private static int idGenerator = 0;
  private int id;
  private Task parent = null;
  private Duration totalTime;
  private LocalDateTime initialDate;
  private LocalDateTime finalDate;

  Interval()
  {
    this.id = generateNewId();
    this.totalTime = Duration.ofSeconds(0);
    this.initialDate = LocalDateTime.now();
    this.finalDate = LocalDateTime.now();
  }

  Interval(Task parent)
  {
    this.id = generateNewId();
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

  private int generateNewId()
  {
    return idGenerator++;
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

  public void calculateTotalTime()
  {
    this.totalTime = Duration.between(this.getInitialDate(), this.getFinalDate());
  }

  public String toString()
  {
    return "Interval with id " + this.getId() +
        "  ->  START: " + this.getInitialDate()  +
        "  -  END: " + this.getFinalDate() +
        "  -  DURATION: " + this.getTotalTime();
  }
}
