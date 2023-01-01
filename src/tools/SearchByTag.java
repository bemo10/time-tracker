package tools;

import core.Container;
import core.Main;
import core.Project;
import core.TreeIterator;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta es una de las estrategias del patron strategy que usamos para hacer la busqueda.
 * */
public class SearchByTag implements SearchStrategy {
  private static SearchByTag instance = null;
  public static final Logger logger = LoggerFactory.getLogger(Main.class);

  /**
   * Metodo que devuelve una instancia.
   * */
  public static SearchByTag getInstance() {
    if (instance == null) {
      instance = new SearchByTag();
    }
    logger.info("Trobat");
    return instance;
  }

  /**
   * Iteramos sobre el sub-arbol de root y buscamos los proyectos/tareas
   * que tienen el tag especificado por parametro filter .
   * */
  public ArrayList<Container> search(Project root, Object filter) {
    // Precondition
    if (root == null) {
      logger.warn("root is null");
      throw new IllegalArgumentException("root is null");
    }
    if (!(filter instanceof String)) {
      logger.warn("filter is not a string");
      throw new IllegalArgumentException("filter is not a string");
    }

    ArrayList<Container> results = new ArrayList<>();
    String caseInsensitiveFilter = ((String) filter).toLowerCase();

    for (TreeIterator it = root.createIterator().first(); it != null; it = it.next()) {
      if (it.getElement().getTags().contains(caseInsensitiveFilter)) {
        results.add(it.getElement());
      }
    }

    return results;
  }
}