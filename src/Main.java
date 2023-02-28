import services.printmanager.PrintManager;
import services.taskmanagers.Managers;
import services.taskmanagers.TaskManager;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {

        PrintManager printManager = Managers.getDefaultPrintManager();
        TaskManager taskManager = Managers.loadFromFile(Paths.get("save.csv"));

        // Получение задачи по id
        System.out.println("Получение задачи по id:");
        printManager.printTask(taskManager.getTaskById(1));
        printManager.printTask(taskManager.getTaskById(3));
        printManager.printTask(taskManager.getTaskById(2));
        printManager.printTask(taskManager.getEpicById(4));
        printManager.printTask(taskManager.getSubtaskById(6));
        printManager.printTask(taskManager.getSubtaskById(5));

        System.out.println("История запроса задач:");
        printManager.printArrayList(taskManager.getHistory());
    }
}
