package services.printmanager;

import models.Task;

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
}
