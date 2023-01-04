public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        EpicManager epicManager = new EpicManager();
        SubtaskManager subtaskManager = new SubtaskManager();

        epicManager.createEpic("Сходить в магазин", "купить продукты");
        subtaskManager.createSubTask("Купить молоко", "жирность 3%", 1, 1);
        subtaskManager.createSubTask("Купить хлеба", "черный резанный%", 1, 2);
        subtaskManager.createSubTask("Купить чипсы", "Lays сметана и лук", 1, 3);

        taskManager.createTask("Убраться на столе", "Вытереть пыль и убрать лишнее.");
        taskManager.createTask("Почистить компьютер", "Помыть фильтры и протереть пыль.");


//        System.out.println(taskManager.getTaskById(5));

//         epicManager.deleteAllTasks();
//         epicManager.deleteEpicById(1);
//         epicManager.deleteAllSubtaskInEpic(1)
//         subtaskManager.deleteSubtaskById(1, 3);
//         subtaskManager.updateSubtaskStatus(1, 2, Task.Status.DONE);
//         subtaskManager.updateSubtaskStatus(1, 3, Task.Status.DONE);
//         subtaskManager.updateSubtaskStatus(1, 4, Task.Status.DONE);

//         System.out.println(epicManager.getEpicById(1));
         System.out.println(epicManager.getAllEpics());
//         System.out.println(subtaskManager.getSubtaskById(1, 3));
    }
}
