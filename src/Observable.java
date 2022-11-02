import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

abstract class Observable {
  protected boolean isChanged;
  // Instead of ArrayList we use CopyOnWriteArrayList because it´s thread safe
  // (in case of removing elements while iterating in a different thread for example)
  CopyOnWriteArrayList<Observer> observers;

  Observable()
  {
    isChanged = false;
    observers = new CopyOnWriteArrayList<>();
  }

  public void addObserver(Observer observer)
  {
    observer.start();
    observers.add(observer);
  }

  public void removeObserver(Observer observer)
  {
    observer.stop();
    observers.remove(observer);
  }

  public void removeAllObservers()
  {
    observers.forEach(observer -> {
      this.removeObserver(observer);
    });
  }

  public void setChanged()
  {
    this.isChanged = true;
  }

  public void clearChanged()
  {
    this.isChanged = false;
  }

  public void notifyObservers(Object arg)
  {
    if (this.isChanged)
    {
      clearChanged();

      observers.forEach(observer -> {
        observer.update(arg);
      });
    }
  }
}
