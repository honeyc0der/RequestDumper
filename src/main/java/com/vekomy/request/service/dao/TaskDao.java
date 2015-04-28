package com.vekomy.request.service.dao;

import com.vekomy.request.model.Task;
import com.vekomy.request.service.GenericDao;

/**
 * DAO of Task.
 */
public interface TaskDao extends GenericDao<Task, Long> {

    /**
     * Tries to remove task from the system.
     * @param task Task to remove
     * @return {@code true} if there is no timesheet created on task.
     * Else {@code false}.
     */
    boolean removeTask(Task task);

}
