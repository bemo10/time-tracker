//import org.json;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.time.LocalDateTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class PersistenceManager {
  private Project root = null;

  PersistenceManager()
  {

  }

  PersistenceManager(Project root)
  {
    this.root = root;
  }

  public Project getRoot()
  {
    return root;
  }

  public void setRoot(Project root)
  {
    this.root = root;
  }

  public void treeToJson(String directory)
  {
    String jsonContent = "";

    if (this.root == null)
    {
      return;
    }

    JSONObject treeJsonObj = new JSONObject();
    JSONArray containersJsonArray = new JSONArray();
    JSONArray intervalsJsonArray = new JSONArray();

    for (TreeIterator it = this.root.createIterator().first(); it != null; it = it.next())
    {
      Container container = it.getElement();

      String containerType = null;
      if (container instanceof Project)
      {
        containerType = "Project";
      }
      if (container instanceof Task)
      {
        containerType = "Task";

        // añadir los intervalos de esta tarea al jsonArray de intervalos
        ((Task)container).getIntervals().forEach(interval -> {
          JSONObject intervalJsonObj;
          intervalJsonObj = jsonObjFromData(null, Integer.toString(interval.getId()), null, interval.getParent(), interval.getTotalTime(), interval.getInitialDate(), interval.getFinalDate());
          intervalsJsonArray.put(intervalJsonObj);
        });
      }

      // Añadir el Proyecto / Tarea al JsonArray
      JSONObject containerJsonObj;
      containerJsonObj = jsonObjFromData(containerType, Integer.toString(container.getId()), container.getName(), container.getParent(), container.getTotalTime(), container.getInitialDate(), container.getFinalDate());

      containersJsonArray.put(containerJsonObj);
    }

    // Añadir los JsonArrays de Contenedores e Intervalos al JsonObject final
    treeJsonObj.put("Containers", containersJsonArray);
    treeJsonObj.put("Intervals", intervalsJsonArray);

    jsonContent = treeJsonObj.toString();

    // Crear nuevo fichero de texto y escribir el contenido del JsonObject
    try {
      // Crear nuevo fichero JSON
      File myObj = new File(directory);
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());

      } else {
        System.out.println("File already exists.");
      }
      // Escribir JSON
      try {
        FileWriter myWriter = new FileWriter("workspaceTree.json");
        myWriter.write(jsonContent);
        myWriter.close();
        System.out.println("Successfully wrote to the file.");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  private JSONObject jsonObjFromData(String type, String id, String name, Container parent, Duration totalTime, LocalDateTime initialDate, LocalDateTime finalDate)
  {
    JSONObject containerJsonObj = new JSONObject();

    if (type != null)
    {
      if (type.equals("Project"))
      {
        containerJsonObj.put("ContainerType", "Project");
      }
      if (type.equals("Task"))
      {
        containerJsonObj.put("ContainerType", "Task");
      }
    }

    containerJsonObj.put("Id", id);

    if (name != null)
    {
      containerJsonObj.put("Name", name);
    }

    if (parent != null)
    {
      containerJsonObj.put("Parent", Integer.toString(parent.getId()));
    }
    else
    {
      containerJsonObj.put("Parent", JSONObject.NULL);
    }

    if (totalTime != null)
    {
      containerJsonObj.put("TotalTime", totalTime.toString());
    }
    else
    {
      containerJsonObj.put("TotalTime", JSONObject.NULL);
    }

    if (initialDate != null)
    {
      containerJsonObj.put("InitialDate", initialDate.toString());
    }
    else
    {
      containerJsonObj.put("InitialDate", JSONObject.NULL);
    }

    if (finalDate != null)
    {
      containerJsonObj.put("FinalDate", finalDate.toString());
    }
    else
    {
      containerJsonObj.put("FinalDate", JSONObject.NULL);
    }


    return containerJsonObj;
  }

  public void jsonToTree(String directory) throws IOException
  {
    try (FileInputStream is = new FileInputStream(directory)) {
      JSONTokener tokener = new JSONTokener(is);
      JSONObject object = new JSONObject(tokener);

      LinkedHashMap<String, Container> containersId = new LinkedHashMap<>();
      LinkedHashMap<Container, String> containersParents = new LinkedHashMap<>();

      JSONArray jsonContainers = object.getJSONArray("Containers");
      JSONArray jsonIntervals = object.getJSONArray("Intervals");

      // Containers
      for (int i = 0; i < jsonContainers.length(); i++) {
        String containerType = jsonContainers.getJSONObject(i).getString("ContainerType");
        int id = jsonContainers.getJSONObject(i).getInt("Id");
        String name = jsonContainers.getJSONObject(i).getString("Name");
        String parent = jsonContainers.getJSONObject(i).getString("Parent");

        String totalTimeString = jsonContainers.getJSONObject(i).getString("TotalTime");
        Duration totalTime = Duration.parse(totalTimeString);

        String initialDateString;
        LocalDateTime initialDate = null;
        if (!jsonContainers.getJSONObject(i).isNull("InitialDate"))
        {
          initialDateString = jsonContainers.getJSONObject(i).getString("InitialDate");
          initialDate = LocalDateTime.parse(initialDateString);
        }

        String finalDateString;
        LocalDateTime finalDate = null;
        if (!jsonContainers.getJSONObject(i).isNull("FinalDate"))
        {
          finalDateString = jsonContainers.getJSONObject(i).getString("FinalDate");
          finalDate = LocalDateTime.parse(finalDateString);
        }

        if (containerType.equals("Project"))
        {
          Project project = new Project(id, name, totalTime, initialDate, finalDate);
          containersId.put(Integer.toString(id), project);
          containersParents.put(project, parent);
        }
        if (containerType.equals("Task"))
        {
          Task task = new Task(id, name, totalTime, initialDate, finalDate);
          containersId.put(Integer.toString(id), task);
          containersParents.put(task, parent);
        }
      }

      for (Container container : containersParents.keySet()) {
        String parent = containersParents.get(container);
        if (containersId.get(parent) != null)
        {
          ((Project)containersId.get(parent)).addChild(container);
        }
        else
        {
          this.root.addChild(container);
        }
      }


      // Intervals
      for (int i = 0; i < jsonIntervals.length(); i++) {
        int id = jsonIntervals.getJSONObject(i).getInt("Id");
        String parent = jsonIntervals.getJSONObject(i).getString("Parent");

        String totalTimeString = jsonIntervals.getJSONObject(i).getString("TotalTime");
        Duration totalTime = Duration.parse(totalTimeString);

        String initialDateString;
        LocalDateTime initialDate = null;
        if (!jsonIntervals.getJSONObject(i).isNull("InitialDate"))
        {
          initialDateString = jsonIntervals.getJSONObject(i).getString("InitialDate");
          initialDate = LocalDateTime.parse(initialDateString);
        }

        String finalDateString;
        LocalDateTime finalDate = null;
        if (!jsonIntervals.getJSONObject(i).isNull("FinalDate"))
        {
          finalDateString = jsonIntervals.getJSONObject(i).getString("FinalDate");
          finalDate = LocalDateTime.parse(finalDateString);
        }

        Interval interval = new Interval(id, totalTime, initialDate, finalDate);
        ((Task)containersId.get(parent)).addInterval(interval);
      }


    }
  }
}
