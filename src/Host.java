/* Do not modify (will be rewritten by the checker). */

/**
 * Abstract class for a host.
 */
public abstract class Host extends Thread {
    /**
     * Adds a task at this host (called by the dispatcher).
     *
     * @param task task to be added at this host
     */
    public abstract void addTask(Task task);

    /**
     * Gets the queue size of this host.
     *
     * @return the queue size of this host
     */
    public abstract int getQueueSize();

    /**
     * Gets the work left at this host.
     *
     * @return the work left at this host
     */
    public abstract long getWorkLeft();

    /**
     * Shuts this host down.
     */
    public abstract void shutdown();
}
