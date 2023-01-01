package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.SearchByTag;

/**
 * Creacion de la clase principal, en la que se crean los test y el proyecto.
 */
public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  /**
   * Metodo donde llamamos a los diferentes test .
   */

  public static void main(String[] args) {
    Project root = null;
    // Test 1: Sample tree
    //root = testA();

    // Test 2: Counting time
    try {
      root = testB_logger();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }


    // Test 3: Escibir JSON (Ejecutar con Test 1/2 escribir el arbol resultado del test)
    //testJsonWrite(root);


    // Test 4: Leer JSON
    /*try {
      testJsonRead();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }*/


    testSearchByTag();

  }


  /* Tests */
  /**
   * Creacion dentro de una clase del test A, este crea proyectos y tareas añadiendo hijos.
   */
  public static Project testA() {
    // Crear el proyecto raiz
    Project root = new Project();

    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("software design");
    Project p2 = new Project("software testing");
    Project p3 = new Project("databases");

    // Crear Tareas
    Task t0 = new Task("transportation");

    // Jerarquia del arbol de Proyectos / Tareas
    root.addChild(p1);
    root.addChild(p2);
    root.addChild(p3);
    root.addChild(t0);

    Project p4 = new Project("problems");
    Project p5 = new Project("time tracker");

    p1.addChild(p4);
    p1.addChild(p5);

    Task t1 = new Task("first list");
    Task t2 = new Task("second list");

    p4.addChild(t1);
    p4.addChild(t2);

    Task t3 = new Task("read handout");
    Task t4 = new Task("first milestone");

    p5.addChild(t3);
    p5.addChild(t4);

    // Print del arbol a partir del root
    root.printSubTree();

    return root;
  }

  /**
   * Creacion dentro de una clase del test B, este crea proyectos, crea tareas
   * y hace la sequencia necesaria.
   */
  public static Project testB_logger() throws InterruptedException {
    // Crear el proyecto raiz
    Project root = new Project("root");

    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("software design");
    Project p2 = new Project("software testing");
    Project p3 = new Project("databases");

    // Crear Tareas
    Task t0 = new Task("transportation");

    // Jerarquia del arbol de Proyectos / Tareas
    root.addChild(p1);
    root.addChild(p2);
    root.addChild(p3);
    root.addChild(t0);

    Project p4 = new Project("problems");
    Project p5 = new Project("time tracker");

    p1.addChild(p4);
    p1.addChild(p5);

    Task t1 = new Task("first list");
    Task t2 = new Task("second list");

    p4.addChild(t1);
    p4.addChild(t2);

    Task t3 = new Task("read handout");
    Task t4 = new Task("first milestone");

    p5.addChild(t3);
    p5.addChild(t4);

    // Llamar a la funcion tick del TimeManager que se ejecuta
    // en un nuevo thread cada N segundos (notifica a los Observers)
    int tickDelay = 2000;
    TimeManager.setTickDelay(tickDelay); // Notificar Observers cada segundo

    /* Tests */
    // Test: start the clock and wait 1.5 seconds
    logger.info("\n----- Test: start the clock and wait 1.5 seconds");
    TimeManager.startTick();
    TimeUnit.MILLISECONDS.sleep(1500);

    // Test: start task transportation, wait 6 seconds and then stop it
    logger.info("\n----- Test: start task transportation, wait 6 seconds and then stop it");
    t0.start();
    TimeUnit.MILLISECONDS.sleep(6000);
    t0.stop();

    // Test: wait 2 seconds
    logger.info("\n----- Test: wait 2 seconds");
    TimeUnit.MILLISECONDS.sleep(2000);

    // Test: start task first list, wait 6 seconds
    logger.info("\n----- Test: start task first list, wait 6 seconds");
    t1.start();
    TimeUnit.MILLISECONDS.sleep(6000);

    // Test:  start task second list and wait 4 seconds
    logger.info("\n----- Test:  start task second list and wait 4 seconds");
    t2.start();
    TimeUnit.MILLISECONDS.sleep(4000);

    // Test: stop first list
    logger.info("\n----- Test: stop first list");
    t1.stop();

    // Test: wait 2 seconds and then stop second list
    logger.info("\n----- Test: wait 2 seconds and then stop second list");
    TimeUnit.MILLISECONDS.sleep(2000);
    t2.stop();

    // Test: wait 2 seconds
    logger.info("\n----- Test: wait 2 seconds");
    TimeUnit.MILLISECONDS.sleep(2000);

    // Test: start transportation, wait 4 seconds and then stop it
    logger.info("\n----- Test: start transportation, wait 4 seconds and then stop it");
    t0.start();
    TimeUnit.MILLISECONDS.sleep(4000);
    t0.stop();

    // Detener tick del TimeManager y print schedule
    TimeUnit.MILLISECONDS.sleep(2000);
    TimeManager.stopTick();

    return root;
  }

  /**
   * Crear el test de Objetos Json escritos.
   */
  public static void testJsonWrite(Project root) {
    System.out.println("Writing JSON...");
    PersistenceManager persistence = new PersistenceManager(root);
    persistence.treeToJson("workspaceTree.json");
  }

  /**
   * Crear el test de Objetos Json leidos.
   */
  public static void testJsonRead() throws IOException {
    System.out.println("Reading JSON...");

    // Crear el proyecto raiz
    Project root = new Project();

    //
    PersistenceManager persistence = new PersistenceManager(root);
    persistence.jsonToTree("workspaceTree.json");

    // Print del arbol
    persistence.getRoot().printSubTree();
  }

  /**
   * Crear el proyecto raiz.
   */
  public static Project testSearchByTag() {

    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("software design");
    p1.addTag("java");
    p1.addTag("flutter");
    Project p2 = new Project("software testing");
    p2.addTag("c++");
    p2.addTag("Java");
    p2.addTag("python");
    Project p3 = new Project("databases");
    p3.addTag("SQL");
    p3.addTag("python");
    p3.addTag("C++");

    // Crear Tareas
    Task t1 = new Task("first list");
    t1.addTag("java");
    Task t2 = new Task("second list");
    t2.addTag("Dart");
    Task t4 = new Task("first milestone");
    t4.addTag("Java");
    t4.addTag("IntelliJ");

    Project root = new Project();

    // Jerarquia del arbol de Proyectos / Tareas
    Task t0 = new Task("transportation");

    root.addChild(p1);
    root.addChild(p2);
    root.addChild(p3);
    root.addChild(t0);

    Project p4 = new Project("problems");
    Project p5 = new Project("time tracker");

    p1.addChild(p4);
    p1.addChild(p5);

    p4.addChild(t1);
    p4.addChild(t2);

    Task t3 = new Task("read handout");

    p5.addChild(t3);
    p5.addChild(t4);

    // Print del arbol a partir del root
    //root.printSubTree();

    /* Search by tag test */
    System.out.println("\n--- Search by tag test: ---");
    ArrayList<Container> results;
    root.setSearchStrategy(SearchByTag.getInstance());

    // Test tag: java
    System.out.print("\nTag 'java'   ->   ");
    results = root.search("java");
    results.forEach(container -> {
      System.out.print(container.getName() + ", ");
    });

    // Test tag: JAVA
    System.out.print("\nTag 'JAVA'   ->   ");
    results = root.search("JAVA");
    results.forEach(container -> {
      System.out.print(container.getName() + ", ");
    });

    // Test tag: intellij
    System.out.print("\nTag 'intellij'   ->   ");
    results = root.search("intellij");
    results.forEach(container -> {
      System.out.print(container.getName() + ", ");
    });

    // Test tag: c++
    System.out.print("\nTag 'c++'   ->   ");
    results = root.search("c++");
    results.forEach(container -> {
      System.out.print(container.getName() + ", ");
    });

    // Test tag: python
    System.out.print("\nTag 'python'   ->   ");
    results = root.search("python");
    results.forEach(container -> {
      System.out.print(container.getName() + ", ");
    });


    return root;
  }

}