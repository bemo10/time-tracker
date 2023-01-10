package webserver;

import core.Container;
//import core.Clock;
import core.Project;
import core.Task;
import core.TimeManager;

public class MainWebServer {
  public static void main(String[] args) {
    webServer();
  }

  public static void webServer() {
    final Container root = makeTreeCourses();
    // implement this method that returns the tree of
    // appendix A in the practicum handout

    // Llamar a la funcion tick del TimeManager que se ejecuta
    // en un nuevo thread cada N segundos (notifica a los Observers)
    int tickDelay = 2000;
    TimeManager.setTickDelay(tickDelay); // Notificar Observers cada segundo
    TimeManager.startTick();

    new WebServer(root);
  }

  public static Project makeTreeCourses() {
    // Crear el proyecto raiz
    Project root = new Project("root");

    /* SIMULAR INTERACCIONES DEL USUARIO */

    // Crear Proyectos
    Project p1 = new Project("software design");
    Project p2 = new Project("software testing");
    Project p3 = new Project("databases");

    // TEST
    /*
    Project p101 = new Project("Project");
    Project p102 = new Project("Project");
    Project p103 = new Project("Project");
    Project p104 = new Project("Project");
    Project p105 = new Project("Project");
    Project p106 = new Project("Project");
    Project p107 = new Project("Project");
    Project p108 = new Project("Project");
    Task t101 = new Task("Task");
    Task t102 = new Task("Task");
    Task t103 = new Task("Task");
    Task t104 = new Task("Task");
    Task t105 = new Task("Task");
    Task t106 = new Task("Task");
    Task t107 = new Task("Task");
    Task t108 = new Task("Task");
    root.addChild(p101);
    root.addChild(p102);
    root.addChild(p103);
    root.addChild(p104);
    root.addChild(p105);
    root.addChild(p106);
    root.addChild(p107);
    root.addChild(p108);
    root.addChild(t101);
    root.addChild(t102);
    root.addChild(t103);
    root.addChild(t104);
    root.addChild(t105);
    root.addChild(t106);
    root.addChild(t107);
    root.addChild(t108);
  */

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

    return root;
  }
}