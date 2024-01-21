/* Do not modify (will be rewritten by the checker). */

import java.util.List;

/**
 * Abstract class for a task dispatcher.
 */
public abstract class Dispatcher {
    protected final SchedulingAlgorithm algorithm;
    protected final List<Host> hosts;

    /**
     * Instantiated a dispatcher object.
     *
     * @param algorithm scheduling algorithm to be used
     * @param hosts     lists of hosts the dispatcher assigns tasks to
     */
    public Dispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        this.algorithm = algorithm;
        this.hosts = hosts;
    }

    /**
     * Adds a tasks to this dispatcher (i.e., a new task enters the system).
     *
     * @param task task to be scheduled
     */
    public abstract void addTask(Task task);
}
