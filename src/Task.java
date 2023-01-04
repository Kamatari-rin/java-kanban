public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

    public Task(String name, String description, int id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }
    @Override
    public String toString() {
        return "\nЗадача: "  + name + "\nОписание: " + description + "\nСтатус: " + status
                + "\n----------------------------------------------------------------------------";
    }
}
