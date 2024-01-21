/* Do not modify (will be rewritten by the checker). */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class that simulates a task generator. Reads a list of tasks and their start times from a file,
 * and sends those tasks to the dispatcher based on their starting point.
 */
public class TaskGenerator extends Thread {
    private final Dispatcher dispatcher;
    private final List<Task> tasks = new ArrayList<>();

    /**
     * @param dispatcher reference to the task dispatcher
     * @param file       file to read tasks from
     */
    public TaskGenerator(Dispatcher dispatcher, File file) {
        this.dispatcher = dispatcher;
        readInputFile(file);
    }

    @Override
    public void run() {
        int lastStartValue = 0;
        for (Task task : tasks) {
            int diff = task.getStart() - lastStartValue;
            if (diff > 0) {
                lastStartValue = task.getStart();
                try {
                    Thread.sleep(1000L * diff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            dispatcher.addTask(task);
        }
    }

    /**
     * Gets the list of tasks generated here.
     *
     * @return list of tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Gets task generator info from a file.
     *
     * @param file file to be read
     */
    private void readInputFile(File file) {
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.startsWith("#")) {
                    continue;
                }

                tasks.add(getTaskFromString(data));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a task object based on an input string.
     *
     * @param line string to be parsed
     * @return new task object
     */
    private static Task getTaskFromString(String line) {
        String[] values = line.split(",");
        return new Task(Integer.parseInt(values[0]), Integer.parseInt(values[1]), 1000L * Integer.parseInt(values[2]),
                TaskType.values()[Integer.parseInt(values[3])], Integer.parseInt(values[4]), Boolean.parseBoolean(values[5]));
    }
}
