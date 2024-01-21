/* Implement this class. */

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class MyDispatcher extends Dispatcher {
	private AtomicInteger index = new AtomicInteger(0);

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }
    
    public synchronized Host findMinimumQueue(List<Host> hosts) {
    	return hosts
    			.stream()
    			.reduce((host1, host2) -> host1.getQueueSize() > host2.getQueueSize() ? host2 : host1)
    			.get();
    }
    
    public synchronized Host getHostType(Task task) {
    	if (task.getType().equals(TaskType.SHORT)) {
			return hosts.get(0);
		} else if (task.getType().equals(TaskType.MEDIUM)) {
			return hosts.get(1);
		} else if (task.getType().equals(TaskType.LONG)) {
			return hosts.get(2);
		}
    	
    	return null;
    }
    
    public synchronized Host findLeastWorkLeftHost(List<Host> hosts) {
    	return hosts
    			.stream()
    			.reduce((host1, host2) -> host1.getWorkLeft() > host2.getWorkLeft() ? host2 : host1)
    			.get();
    }

    @Override
    public void addTask(Task task) {
    	if (this.algorithm == SchedulingAlgorithm.ROUND_ROBIN) {
    		hosts.get((index.getAndIncrement()) % hosts.size()).addTask(task);
    	} else if (this.algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {
    		findMinimumQueue(hosts).addTask(task);
    	} else if (this.algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {
    		getHostType(task).addTask(task);
    	} else if (this.algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {
    		findLeastWorkLeftHost(hosts).addTask(task);
    	}
    }
}
