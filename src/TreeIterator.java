import java.util.ArrayList;

public class TreeIterator implements Iterator {
  private Project root;
  private Container currentElement;

  TreeIterator(Project root)
  {
    this.root = root;
    this.currentElement = root;
  }

  TreeIterator(Project root, Container current)
  {
    this.root = root;
    this.currentElement = current;
  }

  public TreeIterator first()
  {
    ArrayList<Container> children = root.getChildren();
    Container firstChild = root;

    if (children.size() > 0)
    {
        firstChild = children.get(0);
    }

    return new TreeIterator(this.root, firstChild);
  }

  public TreeIterator next()
  {
    Container nextContainer = getNextContainer();

    if (nextContainer != null)
    {
      return new TreeIterator(this.root, nextContainer);
    }
    return null;
  }

  private Container getNextContainer()
  {
    if (this.getElement() instanceof Project)
    {
      if (((Project)this.getElement()).createIterator().first().getElement() != this.getElement())
      {
        return ((Project)this.getElement()).createIterator().first().getElement();
      }
    }

    Container child = this.getElement();
    while(true)
    {
      Project parent = (Project)child.getParent();
      if (parent == null)
      {
        break;
      }

      if (parent.getChildren().indexOf(child)+1 < parent.getChildren().size())
      {
        return parent.getChildren().get(parent.getChildren().indexOf(child)+1);
      }

      child = parent;
    }


    return null;
  }

  /*
  public TreeIterator first()
  {
    ArrayList<Container> children = root.getChildren();
    Project firstChild = root;

    boolean foundFirstElement = false;
    while(!foundFirstElement)
    {
      // Find a child project in children list
      boolean foundChildProject = false;

      for (int i = 0; i < children.size(); i++)
      {
        if (children.get(i) instanceof Project)
        {
          firstChild = (Project)children.get(i);
          children = firstChild.getChildren();
          foundChildProject = true;
          break;
        }
      }

      // If no child project is found take current child as first element
      if (!foundChildProject)
      {
        foundFirstElement = true;
      }
    }

    return new TreeIterator(this.root, firstChild);
  }

  public TreeIterator next()
  {
    Project nextProject = getNextProject(this.currentElement);

    if (nextProject != null)
    {
      return new TreeIterator(this.root, nextProject);
    }
    return null;
  }

  private Project getNextProject(Project project)
  {
    Project projectParent = (Project) project.getParent();
    if (projectParent == null)
    {
      return null;
    }
    int projectIndex = projectParent.getChildren().indexOf(project);

    if (projectIndex < projectParent.getChildren().size()-1)
    {
      for (int i = projectIndex+1; i < projectParent.getChildren().size(); i++)
      {
        if (projectParent.getChildren().get(i) instanceof Project)
        {
          return (Project)((Project) projectParent.getChildren().get(i)).createIterator().first().getElement();
        }
      }
    }

    if (projectParent != this.root)
    {
      return projectParent;
    }

    return null;
  }
   */

  public boolean hasNext()
  {
    if (this.next() != null)
    {
      return true;
    }
    return false;
  }
  public Container getElement()
  {
    return currentElement;
  }
}
