import java.util.concurrent.CopyOnWriteArrayList;


// Forma parte del patron Observer. Es implementado por la clase TimeManager
abstract class Observable {
  protected boolean isChanged;
  // En vez de ArrayList usamos CopyOnWriteArrayList porque es thread safe
  // (en caso de quitar elementos de esta lista mientras que estamos iterando sobre esta misma lista en un thread diferente)
  CopyOnWriteArrayList<Observer> observers;

  Observable()
  {
    isChanged = false;
    observers = new CopyOnWriteArrayList<>();
  }

  // Añadir un nuevo observer y llamar a su funcion de start()
  public void addObserver(Observer observer)
  {
    observer.start();
    observers.add(observer);
  }

  // Quitar un observer y llamar a su funcion de stop()
  public void removeObserver(Observer observer)
  {
    observer.stop();
    observers.remove(observer);
  }

  // Quitar todos los observers
  public void removeAllObservers()
  {
    observers.forEach(observer -> {
      this.removeObserver(observer);
    });
  }

  // Indicar que algo a cambiado para notificar a los observers
  public void setChanged()
  {
    this.isChanged = true;
  }

  // Hacer un clear a la variable "isChanged" para que no se notifique a los observers
  public void clearChanged()
  {
    this.isChanged = false;
  }

  // Notificar a los observers si hubo un cambio en este Observable
  public void notifyObservers(Object arg)
  {
    if (this.isChanged)
    {
      // Hacer un clear a la variable "isChanged"
      clearChanged();

      // Iterar sobre todos los observers y llamar a la funcion update()
      observers.forEach(observer -> {
        observer.update(arg);
      });
    }
  }
}
