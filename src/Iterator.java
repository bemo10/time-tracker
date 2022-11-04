

// Forma parte del patron Iterator. Es implementado por la clase TreeIterator para iterar sobre el arbol de tareas e intervalos
public interface Iterator<E> {
  public Iterator<E> first();
  public Iterator<E> next();
  public boolean hasNext();
  public E getElement();
}
