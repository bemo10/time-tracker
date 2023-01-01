package core;

import java.util.ArrayList;

/**
 * Iterador que sirve para iterar sobre un arbol de contenedores (proyectos y tareas).
 * Parte del patron Iterator e implementa el interfaz Iterator
 */
public class TreeIterator implements Iterator {
  private Project root;
  private Container currentElement;

  TreeIterator(Project root) {
    this.root = root;
    this.currentElement = root;

    assert invariant();
  }

  TreeIterator(Project root, Container current) {
    // Precondition
    if (root == null) {
      throw new IllegalArgumentException("root is null");
    }
    if (current == null) {
      throw new IllegalArgumentException("current is null");
    }
    this.root = root;
    this.currentElement = current;

    assert invariant();
  }

  private boolean invariant() {
    return (root != null && currentElement != null);
  }

  /**
   * Devuelve el iterador que correspode al primer elemento del arbol.
   * El primer elemento es el primer hijo de la raiz (primero desde la izquierda).
   */
  public TreeIterator first() {
    assert invariant();

    ArrayList<Container> children = root.getChildren();
    Container firstChild = root;

    if (children.size() > 0) {
      firstChild = children.get(0);
    }

    return new TreeIterator(this.root, firstChild);
  }

  /**
   * Devuelve el iterador que correspode al siguiente elemento del arbol.
   */
  public TreeIterator next() {
    assert invariant();

    Container nextContainer = getNextContainer();

    if (nextContainer != null) {
      return new TreeIterator(this.root, nextContainer);
    }
    // Devolvemos null si el elemento actual es el ultimo
    return null;
  }

  // Devuelve el siguiente Container apartir el container actual de este iterador
  private Container getNextContainer()  {
    assert invariant();

    // Si el contenedor actual es un proyecto y este proyecto tiene hijos entonces
    // obtenemos el siguiente elemento con la funcion first()
    if (this.getElement() instanceof Project) {
      if (((Project) this.getElement()).createIterator().first().getElement()
          != this.getElement()) {
        return ((Project) this.getElement()).createIterator().first().getElement();
      }
    }

    // Si el contenedor actual no tiene hijos, entonces comprobamos
    // si su padre tiene mas hijos y devolvemos el siguiente hijo.
    // Si su padre no tiene mas hijos entonces subimos un nivel en el arbol de contenedores y
    // buscamos si hay mas hijos hasta llegar el siguiente elemento
    Container child = this.getElement();
    while (true) {
      Project parent = (Project) child.getParent();
      if (parent == null)  {
        break;
      }

      if (parent.getChildren().indexOf(child) + 1 < parent.getChildren().size())  {
        return parent.getChildren().get(parent.getChildren().indexOf(child) + 1);
      }

      child = parent;
    }

    // En caso de que sea el ultimo elemento devolvemos null
    return null;
  }

  /**
   * Comprobamos si existe el siguiente elemento.
   */
  public boolean hasNext()  {
    if (this.next() != null) {
      return true;
    }
    return false;
  }

  // Devolvemos el Contenedor actual de este iterador
  public Container getElement() {
    assert invariant();
    return this.currentElement;
  }
}