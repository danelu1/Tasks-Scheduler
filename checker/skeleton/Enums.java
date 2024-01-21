/* Do not modify (will be rewritten by the checker). */

/**
 * Enum for the scheduling algorithm implemented by the dispatcher.
 */
enum SchedulingAlgorithm {
    ROUND_ROBIN,
    SHORTEST_QUEUE,
    SIZE_INTERVAL_TASK_ASSIGNMENT,
    LEAST_WORK_LEFT
}

/**
 * Enum for the type of tasks (only used by the SITA scheduling algorithm).
 */
enum TaskType {
    SHORT,
    MEDIUM,
    LONG
}