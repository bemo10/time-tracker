package webserver;

import core.Container;
//import core.Clock;
import core.Project;
import core.Task;

public class MainWebServer {
  public static void main(String[] args) {
    webServer();
  }

  public static void webServer() {
    final Container root = makeTreeCourses();
    // implement this method that returns the tree of
    // appendix A in the practicum handout

    // start your clock

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