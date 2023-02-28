package services.printmanager;

import models.Task;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PrintManager {

    public void printTask(Task task) {
        System.out.println("    * " + task.getTaskName() + ", " + task.getTaskDescription() + ", "
                + task.getTaskStatus() + ", [" + task.getTaskID() + "]");
    }

    public void printArrayList(List<Task> list) {

        for (Task task : list) {
            System.out.println("    * " + task.getTaskName() + ", " + task.getTaskDescription() + ", "
                    + task.getTaskStatus() + ", [" + task.getTaskID() + "]");
        }
    }

    public void printMap(Map taskMap) {
        for (Object taskID : taskMap.keySet()) {
            Task task = (Task) taskMap.get(taskID);
            System.out.println("    * " + task.getTaskName() + ", " + task.getTaskDescription() + ", "
                    + task.getTaskStatus() + ", [" + task.getTaskID() + "]");
        }
    }
}
