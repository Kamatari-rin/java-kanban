package services.printmanager;

import models.Task;

import java.util.HashMap;
import java.util.List;
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

    public void printMap(HashMap<Integer, Task> taskMap) {
        for (Integer taskID : taskMap.keySet()) {
            System.out.println("    * " + taskMap.get(taskID).getTaskName() + ", " + taskMap.get(taskID).getTaskDescription() + ", "
                    + taskMap.get(taskID).getTaskStatus() + ", [" + taskMap.get(taskID).getTaskID() + "]");
        }
    }
}
