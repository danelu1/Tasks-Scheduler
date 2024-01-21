/* Do not modify (will be rewritten by the checker). */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger score = new AtomicInteger(0);
    private static final long GRACE_PERIOD = 350L;

    public static void main(String[] args) {
        Test[] tests = {
                new Test(2, SchedulingAlgorithm.ROUND_ROBIN, 14, "RR_Test1", 5),
                new Test(4, SchedulingAlgorithm.ROUND_ROBIN, 15, "RR_Test2", 5),
                new Test(2, SchedulingAlgorithm.ROUND_ROBIN, 15, "RR_Test3", 10),
                new Test(2, SchedulingAlgorithm.ROUND_ROBIN, 14, "RR_Test4", 5),
                new Test(2, SchedulingAlgorithm.ROUND_ROBIN, 14, "RR_Test5", 5),
                new Test(2, SchedulingAlgorithm.SHORTEST_QUEUE, 17, "SQ_Test1", 5),
                new Test(3, SchedulingAlgorithm.SHORTEST_QUEUE, 15, "SQ_Test2", 5),
                new Test(2, SchedulingAlgorithm.SHORTEST_QUEUE, 16, "SQ_Test3", 10),
                new Test(2, SchedulingAlgorithm.SHORTEST_QUEUE, 16, "SQ_Test4", 5),
                new Test(2, SchedulingAlgorithm.SHORTEST_QUEUE, 16, "SQ_Test5", 5),
                new Test(3, SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT, 15, "SITA_Test1", 10),
                new Test(3, SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT, 15, "SITA_Test2", 10),
                new Test(3, SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT, 15, "SITA_Test3", 10),
                new Test(2, SchedulingAlgorithm.LEAST_WORK_LEFT, 16, "LWL_Test1", 5),
                new Test(4, SchedulingAlgorithm.LEAST_WORK_LEFT, 15, "LWL_Test2", 5),
                new Test(2, SchedulingAlgorithm.LEAST_WORK_LEFT, 15, "LWL_Test3", 10),
                new Test(2, SchedulingAlgorithm.LEAST_WORK_LEFT, 15, "LWL_Test4", 10),
        };

        // set a grace period to wait for the results
        Exitter exitter = new Exitter(GRACE_PERIOD);
        exitter.start();

        // run the tests
        for (Test test : tests) {
            int result = runTest(test);
            score.addAndGet(result);
        }

        // interrupt exitter thread
        exitter.interrupt();

        System.out.println("Total: " + score.get() + "/120");
    }

    /**
     * Runs a test.
     *
     * @param test test to be run
     * @return points obtained for this test
     */
    private static int runTest(Test test) {
        System.out.println("Running " + test.inFolder + " (" + test.value + " points)...");

        // get number of hosts, scheduling algorithm, program wait time
        int noHosts = test.noHosts;
        SchedulingAlgorithm algorithm = test.algorithm;
        int waitTime = test.waitTime;

        List<Host> hosts = new ArrayList<>(noHosts);
        for (int i = 0; i < noHosts; i++) {
            hosts.add(new MyHost());
        }

        // initialize the task dispatcher
        Dispatcher dispatcher = new MyDispatcher(algorithm, hosts);

        // initialize the task generators
        List<TaskGenerator> generators = new ArrayList<>();
        File[] dir = new File("in" + File.separator + test.inFolder).listFiles();
        if (dir != null) {
            for (File input : dir) {
                if (input.getName().startsWith("in")) {
                    generators.add(new TaskGenerator(dispatcher, input));
                }
            }
        }

        // start the program timer
        Timer.init();

        // start the hosts and the task generators
        for (Host host : hosts) {
            host.start();
        }

        for (TaskGenerator generator : generators) {
            generator.start();
        }

        // wait for the specified amount of time before joining the threads
        try {
            Thread.sleep(1000L * waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // wait for all the threads
        for (TaskGenerator generator : generators) {
            try {
                generator.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Host host : hosts) {
            try {
                host.shutdown();
                host.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // get all the tasks, sort them by finish time, then save the output
        List<Task> finishedTasks = new ArrayList<>();
        for (TaskGenerator generator : generators) {
            finishedTasks.addAll(generator.getTasks());
        }
        Collections.sort(finishedTasks);
        StringBuilder result = new StringBuilder();
        for (Task task : finishedTasks) {
            result.append(task.printFinish()).append(System.lineSeparator());
        }

        // verify the result of this test
        try {
            String output = new Scanner(new File("in" + File.separator + test.inFolder + File.separator + "out")).useDelimiter("\\Z").next();
            if (!result.toString().startsWith(output)) {
                System.out.println("Test failed... Output is:");
                System.out.println(result);
                System.out.println("It should have been:");
                System.out.println(output);
                return 0;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("OK");

        return test.value;
    }
}
