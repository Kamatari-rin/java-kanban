public class Subtask extends Task {
    private int epicID;
    private int taskPriority;

    public Subtask(String taskName, String taskDescription, TaskType taskType, Status taskStatus, int epicID, int taskPriority) {
        super(taskName, taskDescription, taskType, taskStatus);
        this.epicID = epicID;
        this.taskPriority = taskPriority;
    }

    @Override
    public String toString() {
        return " Подзадача " + taskPriority + ": "  + taskName + " | Описание: " + taskDescription + " | Статус: " + taskStatus
                + "\n----------------------------------------------------------------------------\n";
    }

    public int getPriority() {
        return taskPriority;
    }
}
