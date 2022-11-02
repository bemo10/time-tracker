import java.time.Duration;


public interface Observer {
  public void update(Object arg);
  public void start();
  public void stop();
}
