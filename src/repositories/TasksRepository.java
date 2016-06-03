package repositories;

import Services.AuthenticationService;
import enums.TaskStatus;
import models.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TasksRepository extends BaseRepository<Task> {
    private String path = "Tasks.txt";

    public TasksRepository(String path, Class<Task> task) {
        super(path, task);
        this.path = path;
        classT = task;
    }

    protected void readItem(BufferedReader reader, Task task) throws IOException {
        task.setUserId(Integer.parseInt(reader.readLine()));
        task.setTitle(reader.readLine());
        task.setContent(reader.readLine());
        task.setTaskStatus(TaskStatus.valueOf(reader.readLine()));
    }

    protected void writeItem(PrintWriter writer, Task task) {
        writer.println(task.getId());
        writer.println(task.getUserId());
        writer.println(task.getTitle());
        writer.println(task.getContent());
        writer.println(task.getTaskStatus().toString());
    }

    public ArrayList<Task> getTasksByUserId(int id) {
        ArrayList<Task> tasks = getAll().stream().filter(t -> t.getUserId() == id).collect(Collectors.toCollection(ArrayList<Task>::new));
        return tasks;
    }

    public Task getById(int id) {
        return getAll().stream().filter(t -> t.getId() == id).findFirst().get();
    }

    public Task getByTitleAndContent(String title, String content) {
        return getAll().stream().filter((Task) -> Task.getTitle().equals(title) && Task.getContent().equals(content)).findFirst().orElse(null);
    }
}
