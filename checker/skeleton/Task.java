/* Do not modify (will be rewritten by the checker). */

/**
 * Class for a task to be scheduled.
 */
public class Task implements Comparable<Task> {
    private final int id;
    private final int start;
    private final long duration;
    private final TaskType type;
    private final int priority;
    private final boolean isPreemptible;
    private long left;
    private double finish = 0;

    /**
     * Creates a new Task object.
     *
     * @param id            task ID
     * @param start         task start time (in seconds since the beginning of the program)
     * @param duration      task duration (in milliseconds)
     * @param type          task type (short, medium, long)
     * @param priority      task priority
     * @param isPreemptible specifies whether the task can be pre-empted
     */
    public Task(int id, int start, long duration, TaskType type, int priority, boolean isPreemptible) {
        this.id = id;
        this.start = start;
        this.duration = duration;
        this.type = type;
        this.priority = priority;
        this.isPreemptible = isPreemptible;
        this.left = duration;
    }

    /**
     * Gets the task ID.
     *
     * @return ID of the task
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the task start time.
     *
     * @return start time of the task (in seconds since the beginning of the program)
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the task duration.
     *
     * @return duration of the task (in milliseconds)
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Gets the task type.
     *
     * @return type of the task (short, medium, long)
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Gets the task priority.
     *
     * @return priority of the task
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Checks if the task is pre-emptible
     *
     * @return {@code true} if the task is pre-emptible, {@code false} otherwise
     */
    public boolean isPreemptible() {
        return isPreemptible;
    }

    /**
     * Gets the remaining run time of the task.
     *
     * @return remaining run time of the task (in milliseconds)
     */
    public Long getLeft() {
        return left;
    }

    /**
     * Sets the remaining run time of the task.
     *
     * @param left remaining run time of the task to be set (in milliseconds)
     */
    public void setLeft(long left) {
        this.left = left;
    }

    /**
     * Marks the task as finished.
     */
    public void finish() {
        finish = Timer.getTimeDouble();
    }

    /**
     * Gets the finish time of the task.
     *
     * @return finish time of the task (in seconds)
     */
    public double getFinish() {
        return finish;
    }

    /**
     * Shows the finish time of the task.
     *
     * @return finish time of the task (in seconds, as a String)
     */
    public String printFinish() {
        return id + "," + Math.round(finish);
    }

    @Override
    public String toString() {
        return "Task" + id + "{" +
                "start=" + start +
                ", duration=" + duration +
                ", type=" + type +
                ", priority=" + priority +
                ", isPreemptible=" + isPreemptible +
                ", left=" + left +
                ", finish=" + finish +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        if (Math.round(finish) == Math.round(o.finish)) {
            return id - o.id;
        }

        return finish > o.finish ? 1 : -1;
    }
}