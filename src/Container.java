import java.time.LocalDateTime;
import java.time.Duration;


abstract public class Container {
  private static int idGenerator = 0;
  protected int id;
  protected String name = "";
  protected Container parent = null;
  protected Duration totalTime = Duration.ofSeconds(0);
  protected LocalDateTime initialDate = null;
  protected LocalDateTime finalDate = null;

  Container()
  {
    generateNewId();
    this.name = "";
    this.parent = null;
  }

  Container(Container parent)
  {
    generateNewId();
    this.parent = parent;
  }

  Container(String name)
  {
    generateNewId();
    this.name = name;
  }

  Container(String name, Container parent)
  {
    generateNewId();
    this.name = name;
    this.parent = parent;
  }

  Container(int id, String name, Duration totalTime, LocalDateTime initialDate, LocalDateTime finalDate)
  {
    this.id = id;
    this.name = name;
    this.totalTime = totalTime;
    this.initialDate = initialDate;
    this.finalDate = finalDate;
  }

  private void generateNewId()
  {
    this.id = this.idGenerator++;
  }

  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public Container getParent()
  {
    return parent;
  }

  abstract public Duration getTotalTime();
  abstract public LocalDateTime getInitialDate();
  abstract public LocalDateTime getFinalDate();

  public void setTotalTime(Duration duration)
  {
    totalTime = duration;
  }

  public void setInitialDate(LocalDateTime date)
  {
    initialDate = date;
  }

  public void setFinalDate(LocalDateTime date)
  {
    finalDate = date;
  }

  public void setParent(Container parent)
  {
    this.parent = parent;
  }
}
