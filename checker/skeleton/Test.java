/* Do not modify (will be rewritten by the checker). */

/**
 * Class for a test, defined by the number of hosts, the scheduling algorithm, the wait time until the
 * threads are joined, the folder with the input files, and the amount of points obtained from this test.
 */
public final class Test {
    int noHosts;
    SchedulingAlgorithm algorithm;
    int waitTime;
    String inFolder;
    int value;

    /**
     * Creates a test entry.
     *
     * @param noHosts   number of hosts in the tested scenario
     * @param algorithm scheduling algorithm employed in the tested scenario
     * @param waitTime  time until the threads involved in the test are joined
     * @param inFolder  name of the folder containing the test input files
     * @param value     amount of points obtained by passing this test
     */
    public Test(int noHosts, SchedulingAlgorithm algorithm, int waitTime, String inFolder, int value) {
        this.noHosts = noHosts;
        this.algorithm = algorithm;
        this.waitTime = waitTime;
        this.inFolder = inFolder;
        this.value = value;
    }
}
