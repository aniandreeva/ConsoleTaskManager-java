package repositories;

import java.io.*;

import models.User;

public class UsersRepository extends BaseRepository<User> {
    private String path = "Users.txt";

    public UsersRepository(String path, Class<User> user) {
        super(path, user);
        this.path = path;
        classT = user;
    }

    protected void readItem(BufferedReader reader, User user) throws IOException {
        user.setUsername(reader.readLine());
        user.setPassword(reader.readLine());
    }

    protected void writeItem(PrintWriter writer, User user) {
        writer.println(user.getId());
        writer.println(user.getUsername());
        writer.println(user.getPassword());
    }

    public User getByUsernameAndPassword(String username, String password) {
        return this.getAll().stream().filter((User) -> User.getUsername().equals(username) && User.getPassword().equals(password)).findFirst().orElse(null);
    }
}
