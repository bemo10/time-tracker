package tools;

import core.Container;
import core.Project;
import java.util.ArrayList;

/**
 * Este es el interfaz utilizado para el patron strategy.
 * Este patron nos permite tener varias estrategias de busqueda
 * donde se itera sobnre el arbol de contenedores utilizando un iterador
 * que se obtiene apartir de la raiz del arbol
 */
public interface SearchStrategy {
  public ArrayList<Container> search(Project root, Object filter);
}