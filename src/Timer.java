/* Do not modify (will be rewritten by the checker). */

/**
 * Class for measuring the time since a specified initialization moment.
 */
public class Timer {
    static long startTime = 0;

    /**
     * Sets the starting time.
     */
    static void init() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Gets the time since initialization.
     *
     * @return time since initialization (in seconds)
     */
    static double getTimeDouble() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }
}
