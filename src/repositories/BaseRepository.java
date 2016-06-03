package repositories;
//
//import com.sun.java.util.jar.pack.Package;
//import com.sun.javafx.tk.Toolkit;
//import enums.TaskStatus;
//import models.Task;

import models.BaseModel;

import java.io.*;
import java.util.ArrayList;

public abstract class BaseRepository<T extends BaseModel> {
    private String path;
    protected Class<T> classT;

    public BaseRepository(String path, Class<T> classT) {
        this.path = path;
        this.classT = classT;
    }

    protected abstract void readItem(BufferedReader reader, T item) throws IOException;

    protected abstract void writeItem(PrintWriter writer, T item);

    public ArrayList<T> getAll() {

        ArrayList<T> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                T item = classT.newInstance();

                item.setId(Integer.parseInt(line));
                readItem(reader, item);

                items.add(item);
            }
        } catch (FileNotFoundException fe) {
            System.out.println("File not found!");
        } catch (IOException io) {
            System.out.println("Something went wrong!");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return items;
    }

    public int getNextId() {
        int itemId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                T item = classT.newInstance();

                itemId = Integer.parseInt(line);
                readItem(reader, item);
            }
        } catch (FileNotFoundException fe) {
            System.out.println("File not found!");
        } catch (IOException io) {
            System.out.println("Something went wrong!");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return itemId + 1;
    }

    public T getById(int id) {
        return getAll().stream().filter(t -> t.getId() == id).findFirst().get();
    }

    public void insert(T item) {
        File file = new File(path);

        try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {
            if (!file.exists()) {
                file.createNewFile();
            }

            item.setId(getNextId());
            writeItem(writer, item);

        } catch (IOException io) {
            System.out.println("Something went wrong!");
        }
    }

    public void update(T item) {
        String tempPath = "temp-" + path;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(tempPath, true))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    T oldItem = classT.newInstance();

                    oldItem.setId(Integer.parseInt(line));
                    readItem(reader, oldItem);


                    if (oldItem.getId() == item.getId()) {
                        writeItem(writer, item);
                    } else {
                        writeItem(writer, oldItem);
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException fe) {
            System.out.println("File not found!");
        } catch (IOException io) {
            System.out.println("Something went wrong!");
        }

        File oldFile = new File(path);
        oldFile.delete();

        File newFile = new File(tempPath);
        newFile.renameTo(oldFile);
    }

    public void delete(int id) {
        String tempPath = "temp-" + path;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(tempPath))) {
                String line;


                while ((line = reader.readLine()) != null) {
                    T item = classT.newInstance();

                    item.setId(Integer.parseInt(line));
                    readItem(reader, item);

                    if (item.getId() != id) {
                        writeItem(writer, item);
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException fe) {
            System.out.println("File not found!");
        } catch (IOException io) {
            System.out.println("Something went wrong!");
        }

        File oldFile = new File(path);
        oldFile.delete();

        File newFile = new File(tempPath);
        newFile.renameTo(oldFile);
    }
}