import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main
{
  public static void main(String[] args) {
    Project root = null;

    // Test 1: Sample tree
    //root = testA();


    // Test 2: Counting time
    try {
      root = testB();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }


    // Test 3: Escibir JSON (Ejecutar con el Test 1 o 2 para escribir el arbol que resulta de esos tests)
    //testJsonWrite(root);


    // Test 4: Leer JSON
    /*try {
      testJsonRead();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }*/



  }


  /* Tests */

  public static Project testA()
  {
    // Crear el proyecto raiz
    Project root = new Project();

    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("software design");
    Project p2 = new Project("software testing");
    Project p3 = new Project("databases");
    Project p4 = new Project("problems");
    Project p5 = new Project("time tracker");

    // Crear Tareas
    Task t0 = new Task("transportation");
    Task t1 = new Task("first list");
    Task t2 = new Task("second list");
    Task t3 = new Task("read handout");
    Task t4 = new Task("first milestone");

    // Jerarquia del arbol de Proyectos / Tareas
    root.addChild(p1);
    root.addChild(p2);
    root.addChild(p3);
    root.addChild(t0);

    p1.addChild(p4);
    p1.addChild(p5);

    p4.addChild(t1);
    p4.addChild(t2);

    p5.addChild(t3);
    p5.addChild(t4);

    // Print del arbol a partir del root
    root.printSubTree();

    return root;
  }

  public static Project testB() throws InterruptedException {
    // Crear el proyecto raiz
    Project root = new Project();

    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("software design");
    Project p2 = new Project("software testing");
    Project p3 = new Project("databases");
    Project p4 = new Project("problems");
    Project p5 = new Project("time tracker");

    // Crear Tareas
    Task t0 = new Task("transportation");
    Task t1 = new Task("first list");
    Task t2 = new Task("second list");
    Task t3 = new Task("read handout");
    Task t4 = new Task("first milestone");

    // Jerarquia del arbol de Proyectos / Tareas
    root.addChild(p1);
    root.addChild(p2);
    root.addChild(p3);
    root.addChild(t0);

    p1.addChild(p4);
    p1.addChild(p5);

    p4.addChild(t1);
    p4.addChild(t2);

    p5.addChild(t3);
    p5.addChild(t4);

    // Llamar a la funcion tick del TimeManager que se ejecuta en un nuevo thread cada N segundos (notifica a los Observers)
    int tickDelay = 2000;
    TimeManager timeManager = new TimeManager();
    timeManager.setTickDelay(tickDelay); // Notificar Observers cada segundo
    timeManager.tick();

    // Hacer un print del arbol cada 2 segundos
    Timer printTimer = new Timer();
    TimerTask printTask = new TimerTask() {
      @Override
      public void run() {
        root.printSubTree();
        System.out.println("----------------------------------------------------------------------------------------------------------");
      }
    };
    printTimer.scheduleAtFixedRate(printTask, 100, tickDelay);

    /* Tests */

    // Test: start the clock and wait 1.5 seconds
    System.out.println("\n----- Test: start the clock and wait 1.5 seconds");
    TimeUnit.MILLISECONDS.sleep(1500);

    // Test: start task transportation, wait 6 seconds and then stop it
    System.out.println("\n----- Test: start task transportation, wait 6 seconds and then stop it");
    timeManager.addObserver(t0);
    TimeUnit.MILLISECONDS.sleep(6000);
    timeManager.removeObserver(t0);

    // Test: wait 2 seconds
    System.out.println("\n----- Test: wait 2 seconds");
    TimeUnit.MILLISECONDS.sleep(2000);

    // Test: start task first list, wait 6 seconds
    System.out.println("\n----- Test: start task first list, wait 6 seconds");
    timeManager.addObserver(t1);
    TimeUnit.MILLISECONDS.sleep(6000);

    // Test:  start task second list and wait 4 seconds
    System.out.println("\n----- Test:  start task second list and wait 4 seconds");
    timeManager.addObserver(t2);
    TimeUnit.MILLISECONDS.sleep(4000);

    // Test: stop first list
    System.out.println("\n----- Test: stop first list");
    timeManager.removeObserver(t1);

    // Test: wait 2 seconds and then stop second list
    System.out.println("\n----- Test: wait 2 seconds and then stop second list");
    TimeUnit.MILLISECONDS.sleep(2000);
    timeManager.removeObserver(t2);

    // Test: wait 2 seconds
    System.out.println("\n----- Test: wait 2 seconds");
    TimeUnit.MILLISECONDS.sleep(2000);

    // Test: start transportation, wait 4 seconds and then stop it
    System.out.println("\n----- Test: start transportation, wait 4 seconds and then stop it");
    timeManager.addObserver(t0);
    TimeUnit.MILLISECONDS.sleep(4000);
    timeManager.removeObserver(t0);

    // Detener tick y print schedule
    TimeUnit.MILLISECONDS.sleep(2000);
    timeManager.stopTick();
    printTimer.cancel();
    printTimer.purge();

    return root;
  }

  public static void testJsonWrite(Project root)
  {
    System.out.println("Writing JSON...");
    PersistenceManager persistence = new PersistenceManager(root);
    persistence.treeToJson("workspaceTree.json");
  }

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











  /* My Tests */

  public static void myTests() throws IOException {
    enum Tests
    {
      jerarquiaArbolSimple,         // Test print de un arbol de proyectos / tareas
      jerarquiaArbolComplejo,       // Test añadir proyectos y tareas a un arbol y hacer prints del estado del arbol
      EjecutarTareasSimple,         // Test empezar y terminar tareas
      EjecutarTareasComplejo,       // Test empezar y terminar tareas dentro de un arbol que contiene proyectos
      EscribirJsonSimple,
      LeerJsonSimple,
    }

    Tests test = Tests.LeerJsonSimple;

    switch(test)
    {
      case jerarquiaArbolSimple:
        try {
          test1();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        break;
      case jerarquiaArbolComplejo:
        try {
          test2();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        break;
      case EjecutarTareasSimple:
        try {
          test3();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        break;
      case EjecutarTareasComplejo:
        try {
          test4();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        break;
      case EscribirJsonSimple:
        test5();
        break;
      case LeerJsonSimple:
        testJsonRead();
        break;
    }
  }

  // Test print de un arbol de proyectos / tareas
  public static void test1() throws InterruptedException {
    // Crear el proyecto raiz
    Project root = new Project();

    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("a");
    Project p2 = new Project("b");
    Project p3 = new Project("c");
    Project p4 = new Project("d");
    Project p5 = new Project("e");
    Project p6 = new Project("f");
    Project p7 = new Project("g");
    Project p8 = new Project("h");

    // Crear Tareas
    Task t0 = new Task("a");
    Task t1 = new Task("b");
    Task t2 = new Task("c");
    Task t3 = new Task("d");

    // Jerarquia del arbol de Proyectos / Tareas
    root.addChild(p1);
    root.addChild(p2);

    p1.addChild(p3);
    p1.addChild(t0);
    p1.addChild(p4);
    p1.addChild(t1);

    p2.addChild(p5);

    p5.addChild(t2);
    p5.addChild(p6);
    p5.addChild(p7);

    p6.addChild(p8);

    p8.addChild(t3);

    // Print del arbol a partir del root
    root.printSubTree();
  }

  // Test añadir proyectos y tareas a un arbol y hacer prints del estado del arbol
  public static void test2() throws InterruptedException {
    // Crear el proyecto raiz
    Project root = new Project();

    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("a");
    Project p2 = new Project("b");
    Project p3 = new Project("c");
    Project p4 = new Project("d");
    Project p5 = new Project("e");
    Project p6 = new Project("f");
    Project p7 = new Project("g");
    Project p8 = new Project("h");

    // Crear Tareas
    Task t0 = new Task("a");
    Task t1 = new Task("b");
    Task t2 = new Task("c");
    Task t3 = new Task("d");

    // Jerarquia del arbol de Proyectos / Tareas

    root.addChild(p1);
    root.addChild(p2);

    System.out.println("\nTest: añadir Proyectos a y b");
    root.printSubTree();  // Print del arbol a partir del root

    p1.addChild(p3);
    p1.addChild(t0);
    p1.addChild(p4);
    p1.addChild(t1);

    System.out.println("\nTest: añadir Proyectos c y d / Tareas a y b");
    root.printSubTree();  // Print del arbol a partir del root

    p2.addChild(p5);

    System.out.println("\nTest: añadir Proyecto e");
    root.printSubTree();  // Print del arbol a partir del root

    p5.addChild(t2);
    p5.addChild(p6);
    p5.addChild(p7);

    System.out.println("\nTest: añadir Proyectos f y g / Tarea c");
    root.printSubTree();  // Print del arbol a partir del root

    p6.addChild(p8);

    p8.addChild(t3);

    System.out.println("\nTest: añadir Proyecto h / Tarea d");
    root.printSubTree();  // Print del arbol a partir del root
  }

  // Test empezar y terminar tareas
  public static void test3() throws InterruptedException {
    System.out.println("Test ha empezado. Espera unos segundos... \n");

    // Crear el proyecto raiz
    Project root = new Project();

    // llamar a la funcion tick del TimeManager que se ejecuta en un nuevo thread cada N segundos (notifica a los Observers)
    int tickDelay = 1000;
    TimeManager timeManager = new TimeManager();
    timeManager.setTickDelay(tickDelay); // Notificar Observers cada segundo
    timeManager.tick();

    // Hacer un print del arbol cada segundo
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        root.printSubTree();
      }
    };
    timer.scheduleAtFixedRate(task, 100, tickDelay);


    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Tareas
    Task t0 = new Task("a");
    Task t1 = new Task("b");

    // Jerarquia del arbol de Proyectos / Tareas
    root.addChild(t0);
    root.addChild(t1);


    /* Tests */

    // Test: Empezar tareas a
    TimeUnit.MILLISECONDS.sleep(500);
    System.out.println("\n----- Test: Empezar tarea 'a'");
    timeManager.addObserver(t0);

    // Test: Empezar tareas b
    TimeUnit.MILLISECONDS.sleep(1100);
    System.out.println("\n----- Test: Empezar tarea 'b'");
    timeManager.addObserver(t1);

    // Test: Terminar tareas a
    TimeUnit.MILLISECONDS.sleep(1100);
    System.out.println("\n----- Test: Terminar tarea 'a'");
    timeManager.removeObserver(t0);

    // Test: Empezar tareas a de nuevo
    TimeUnit.MILLISECONDS.sleep(1100);
    System.out.println("\n----- Test: Empezar tarea 'a' de nuevo");
    timeManager.addObserver(t0);

    // Test: Terminar todas las tareas
    TimeUnit.MILLISECONDS.sleep(1100);
    System.out.println("\n----- Test: Terminar todas las tareas");
    timeManager.removeAllObservers();
  }

  // Test empezar y terminar tareas dentro de un arbol que contiene proyectos
  public static void test4() throws InterruptedException {
    System.out.println("Test ha empezado. Espera unos segundos... \n");

    // Crear el proyecto raiz
    Project root = new Project();

    // llamar a la funcion tick del TimeManager que se ejecuta en un nuevo thread cada N segundos (notifica a los Observers)
    int tickDelay = 1000;
    TimeManager timeManager = new TimeManager();
    timeManager.setTickDelay(tickDelay); // Notificar Observers cada segundo
    timeManager.tick();

    // Hacer un print del arbol cada segundo
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        root.printSubTree();
      }
    };
    timer.scheduleAtFixedRate(task, 100, tickDelay);


    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("a");
    Project p2 = new Project("b");
    Project p3 = new Project("c");
    Project p4 = new Project("d");
    Project p5 = new Project("e");
    Project p6 = new Project("f");
    Project p7 = new Project("g");
    Project p8 = new Project("h");

    // Crear Tareas
    Task t0 = new Task("a");
    Task t1 = new Task("b");
    Task t2 = new Task("c");
    Task t3 = new Task("d");
    Task t4 = new Task("e");

    // Jerarquia del arbol de Proyectos / Tareas
    root.addChild(p1);
    root.addChild(p2);
    root.addChild(t3);

    p1.addChild(p3);
    p1.addChild(t0);
    p1.addChild(p4);
    p1.addChild(t1);

    p2.addChild(p5);

    p5.addChild(t2);
    p5.addChild(p6);
    p5.addChild(p7);

    p6.addChild(p8);

    p8.addChild(t4);


    /* Tests */

    // Test: Empezar tareas a y b
    TimeUnit.MILLISECONDS.sleep(500);
    System.out.println("\n----- Test: Empezar tareas 'a' y 'b'");
    timeManager.addObserver(t0);
    timeManager.addObserver(t1);

    // Test: Empezar tareas c y d
    TimeUnit.MILLISECONDS.sleep(1100);
    System.out.println("\n----- Test: Empezar tareas 'c' y 'd'");
    timeManager.addObserver(t2);
    timeManager.addObserver(t3);

    // Test: Terminar tareas a y b
    TimeUnit.MILLISECONDS.sleep(1100);
    System.out.println("\n----- Test: Terminar tareas 'a' y 'b'");
    timeManager.removeObserver(t0);
    timeManager.removeObserver(t1);

    // Test: Empezar tareas a y b de nuevo
    TimeUnit.MILLISECONDS.sleep(1100);
    System.out.println("\n----- Test: Empezar tareas 'a' y 'b' de nuevo");
    timeManager.addObserver(t0);
    timeManager.addObserver(t1);

    // Test: Terminar todas las tareas
    TimeUnit.MILLISECONDS.sleep(1100);
    System.out.println("\n----- Test: Terminar todas las tareas");
    timeManager.removeAllObservers();
  }

  public static void test5()
  {
    // Crear el proyecto raiz
    Project root = new Project();

    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("a");
    Project p2 = new Project("b");
    Project p3 = new Project("c");
    Project p4 = new Project("d");
    Project p5 = new Project("e");
    Project p6 = new Project("f");
    Project p7 = new Project("g");
    Project p8 = new Project("h");

    // Crear Tareas
    Task t0 = new Task("a");
    Task t1 = new Task("b");
    Task t2 = new Task("c");
    Task t3 = new Task("d");
    Task t4 = new Task("e");

    // Crear intervalos
    t0.addInterval(new Interval(t0));
    t0.addInterval(new Interval(t0));

    t1.addInterval(new Interval(t1));
    t1.addInterval(new Interval(t1));
    t1.addInterval(new Interval(t1));

    t2.addInterval(new Interval(t2));

    t3.addInterval(new Interval(t3));

    t4.addInterval(new Interval(t4));

    // Jerarquia del arbol de Proyectos / Tareas
    root.addChild(p1);
    root.addChild(p2);
    root.addChild(t3);

    p1.addChild(p3);
    p1.addChild(t0);
    p1.addChild(p4);
    p1.addChild(t1);

    p2.addChild(p5);

    p5.addChild(t2);
    p5.addChild(p6);
    p5.addChild(p7);

    p6.addChild(p8);

    p8.addChild(t4);

    //
    PersistenceManager persistence = new PersistenceManager(root);
    persistence.treeToJson("workspaceTree.json");
  }


}