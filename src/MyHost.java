import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/* Implement this class. */

public class MyHost extends Host {
	// Camp folosit pentru a verifica daca sunt primite task-uri de la dispatcher.
	boolean isInterrupted = false;
	
	// Comparator folosit pentru adaugarea task-urilor in coada in ordinea descrescatoare
	// a prioritatii.
	Comparator<Task> comparator = new Comparator<Task>() {
		public int compare(Task t1, Task t2) {
			if (t1.getPriority() == t2.getPriority()) {
				return t1.getId() - t2.getId();
			}
			
			return t2.getPriority() - t1.getPriority();
		}
	};
	
	// Multime care retine task-urile aflate in executie.
	private Set<Task> running = Collections.synchronizedSet(new LinkedHashSet<>());
	
	// Coada folosita pentru stocarea task-urilor primite de la dispatcher.
	private BlockingQueue<Task> q = new PriorityBlockingQueue<Task>(100, comparator);
	
    @Override
    public synchronized void run() {
    	// Blocam host-ul curent un anumit moment de timp pentru a ne asigura ca
    	// au intrat elemente de procesat in coada.
    	try {
    		Thread.sleep(50);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    	
    	// Executam task-urile pana cand host-ul este intrerupt.
    	while (!isInterrupted) {
			if (!q.isEmpty()) {
				try {
					Task task = null;
					Task t = null;
					
					// Task-ul curent.
					task = q.take();
					
					// Task-ul curent este in executie.
					running.add(task);
					
					// Daca task-ul curent a inlocuit un task ce nu este preemptibil si nu si-a terminat
					// executia atunci il adaugam inapoi in coada si il scoatem pe acela ce trebuie executat.
					// Daca task-ul curent este preemptibil si prioritatea urmatorului task din coada(daca
					// acesta exista) este mai mare, atunci il scoatem pe acela in executie si pe cel curent
					// il adaugam momentan inapoi.
					if (q.peek() != null && !q.peek().isPreemptible()) {
    					if (q.peek().getPriority() < task.getPriority() &&
    							q.peek().getLeft() > 0 &&
    							q.peek().getLeft() < q.peek().getDuration()) {
    						t = q.take();
							q.put(task);
		    				task = t;
    					}
	    			} else if (task.isPreemptible()) {
		    			if (q.peek() != null && q.peek().getPriority() > task.getPriority() && task.getLeft() > 0) {
		    				t = q.take();
							q.put(task);
		    				task = t;
		    			}
					}
					
					long step = 1000;
					
					// Executia task-urilor(fac din secunda in secunda).
					synchronized (this) {
						if (task.getLeft() > 0) {
		    				Thread.sleep(step);
		    				task.setLeft(task.getLeft() - step);
						}
					}
					
					// Daca task-ul nu si-a terminat treaba dupa secunda curenta
					// atunci il bag inapoi in coada. ALtfel, ii dau "finish()"
					// si il scot din executie.
					if (task.getLeft() > 0 && task.getLeft() < task.getDuration()) {
						q.put(task);
					} else {
						task.finish();
						running.remove(task);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
    	}
    }

    @Override
    public void addTask(Task task) {
    	try {
			q.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    @Override
    public int getQueueSize() {
        return q.size() + running.size();
    }

    @Override
    public long getWorkLeft() {
    	long workLeft = 0;
    	
    	List<Task> l = new ArrayList<>(running);
    	l.addAll(q);
    	
        for (Task t : l) {
        	workLeft += t.getLeft();
        }
        
        return workLeft;
    }

    @Override
    public void shutdown() {
    	this.interrupt();
    	isInterrupted = true;
    }
}
