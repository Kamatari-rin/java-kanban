public class Subtask extends Task {
    private int epicId; // Epic id
    private int priority;

    public Subtask(String name, String description, int id, Status status, int epicId, int priority) {
        super(name, description, id, status);
        this.epicId = epicId;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return " Подзадача " + priority + ": "  + name + " | Описание: " + description + " | Статус: " + status
                + "\n----------------------------------------------------------------------------\n";
    }

    public int getPriority() {
        return priority;
    }
}
