

public interface Iterator<E> {
  public Iterator<E> first();
  public Iterator<E> next();
  public boolean hasNext();
  public E getElement();
}
