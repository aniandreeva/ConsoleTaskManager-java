import java.util.ArrayList;
import java.util.Scanner;

import Services.AuthenticationService;
import enums.TaskStatus;
import models.Task;
import models.User;
import repositories.TasksRepository;
import repositories.UsersRepository;

public class Program {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.println("1-Login, 2-SignUp");
        int choice = reader.nextInt();

        if (choice == 1) {
            login();
            System.out.println("-----------------------");
        }

        if (choice == 2) {
            register();
            System.out.println("-----------------------");
        }
    }


    public static void login() {
        Scanner reader = new Scanner(System.in);

        System.out.print("Username: ");
        String username = reader.nextLine();

        System.out.print("Password: ");
        String password = reader.nextLine();

        AuthenticationService.Authenticate(username, password);
        if (AuthenticationService.getLoggedUser() != null) {
            System.out.println("Hello, " + AuthenticationService.getLoggedUser().getUsername());

            System.out.println("-----------------------");


            menu();
        } else {
            System.out.println("Try again");
            System.out.println("-----------------------");
            login();
        }
    }

    public static void register() {
        Scanner reader = new Scanner(System.in);
        UsersRepository usersRep = new UsersRepository("Users.txt", User.class);

        User user = new User();
        System.out.print("Username: ");
        user.setUsername(reader.nextLine());
        System.out.print("Password: ");
        user.setPassword(reader.nextLine());

        usersRep.insert(user);
        login();
        System.out.println("-----------------------");
    }

    public static void menu() {
        Scanner reader = new Scanner(System.in);
        System.out.println("1-Show all tasks,2-Add task, 3-Edit task, 4-Delete task, 0-Exit");
        int choice = reader.nextInt();

        if (choice == 1) {
            allTasks();
            System.out.println("-----------------------");
            menu();
        }
        if (choice == 2) {
            addTask();
            System.out.println("-----------------------");
            menu();
        }
        if (choice == 3) {
            editTask();
            System.out.println("-----------------------");
            menu();
        }
        if (choice == 4) {
            allTasks();
            System.out.println("-----------------------");
            deleteTask();
            menu();
        }
        if (choice == 0) {
            System.exit(0);
        }
    }

    public static void allTasks() {
        TasksRepository tasksRepository = new TasksRepository("Tasks.txt", Task.class);
        ArrayList<Task> tasks = tasksRepository.getTasksByUserId(AuthenticationService.getLoggedUser().getId());

        for (Task task : tasks) {
            System.out.println(task.getId() + ", " + task.getTitle() + ", " + task.getContent() + ", " + task.getTaskStatus());
        }
    }

    public static void addTask() {
        Scanner reader = new Scanner(System.in);
        TasksRepository tasksRep = new TasksRepository("Tasks.txt", Task.class);

        System.out.print("Title: ");
        String title = reader.nextLine();
        System.out.print("Content: ");
        String content = reader.nextLine();
        System.out.println("Status (0-undone, 1-in_progress, 2-done): ");
        int taskStatus = reader.nextInt();

        Task task = new Task();

        task.setId(tasksRep.getNextId());
        task.setUserId(AuthenticationService.getLoggedUser().getId());
        task.setTitle(title);
        task.setContent(content);
        task.setTaskStatus(TaskStatus.values()[taskStatus]);

        tasksRep.insert(task);
    }

    public static void editTask() {
        taskDetails();
    }

    public static void deleteTask() {
        Scanner reader = new Scanner(System.in);
        TasksRepository taskRep = new TasksRepository("Tasks.txt", Task.class);

        System.out.print("Choose id: ");
        int id = reader.nextInt();

        Task task = taskRep.getById(id);

        if (task != null) {
            taskRep.delete(id);
        }
    }

    public static void taskDetails() {
        Scanner reader = new Scanner(System.in);
        TasksRepository taskRep = new TasksRepository("Tasks.txt", Task.class);

        allTasks();

        System.out.print("Choose task id: ");
        int id = reader.nextInt();

        Task task = taskRep.getById(id);

        if (task != null) {
            System.out.print("Title: " + task.getTitle());
            System.out.print("Content: " + task.getContent());
            System.out.print("Status: " + task.getTaskStatus());

            updateTask(id);
        } else {
            taskDetails();
        }
    }

    public static void updateTask(int id) {
        Scanner reader = new Scanner(System.in);
        TasksRepository taskRep = new TasksRepository("Tasks.txt", Task.class);
        Task task = taskRep.getById(id);

        if (task != null) {
            System.out.print("Title: ");
            task.setTitle(reader.nextLine());
            System.out.print("Content: ");
            task.setContent(reader.nextLine());
            System.out.println("Status (0-undone, 1-in_progress, 2-done): ");
            int status = reader.nextInt();
            task.setTaskStatus(TaskStatus.values()[status]);

            taskRep.update(task);
        }
    }
}
