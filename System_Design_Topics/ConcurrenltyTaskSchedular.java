import java.util.concurrent.*;
import java.util.*;

class Task implements Callable<Void> {
    private final String taskId;
    private final int timeToComplete;

    public Task(String taskId, int timeToComplete) {
        this.taskId = taskId;
        this.timeToComplete = timeToComplete;
    }

    @Override
    public Void call() throws Exception {
        System.out.println("Starting task: " + taskId);
        Thread.sleep(timeToComplete);
        System.out.println("Completed task: " + taskId);
        return null;
    }
}

public class TaskScheduler {
    private final ExecutorService executorService;
    private final BlockingQueue<Task> taskQueue;
    private final List<Future<Void>> runningTasks;

    public TaskScheduler(int maxConcurrentTasks) {
        this.executorService = Executors.newFixedThreadPool(maxConcurrentTasks);
        this.taskQueue = new LinkedBlockingQueue<>();
        this.runningTasks = new ArrayList<>(maxConcurrentTasks);
    }

    public void addTask(Task task) {
        taskQueue.offer(task);
        scheduleTasks();
    }

    private void scheduleTasks() {
        while (runningTasks.size() < 2 && !taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            if (task != null) {
                Future<Void> future = executorService.submit(task);
                runningTasks.add(future);
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public static void main(String[] args) {
        TaskScheduler scheduler = new TaskScheduler(2);

        scheduler.addTask(new Task("task1", 2000));
        scheduler.addTask(new Task("task2", 500));
        scheduler.addTask(new Task("task3", 3000));

        // Simulate task completion and scheduling of new tasks
        while (!scheduler.taskQueue.isEmpty() || !scheduler.runningTasks.isEmpty()) {
            Iterator<Future<Void>> iterator = scheduler.runningTasks.iterator();
            while (iterator.hasNext()) {
                Future<Void> future = iterator.next();
                if (future.isDone()) {
                    iterator.remove();
                    scheduler.scheduleTasks();
                }
            }
        }

        scheduler.shutdown();
    }
}