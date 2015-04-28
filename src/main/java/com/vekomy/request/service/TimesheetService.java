package com.vekomy.request.service;

import com.vekomy.request.model.Employee;
import com.vekomy.request.model.Manager;
import com.vekomy.request.model.Task;

import java.util.List;

/**
 * Business that defines operations on timesheets
 */
public interface TimesheetService {
	
	/**
	 * @return Finds the busiest task (with the most of employees).
     * Returns {@code null} when tasks are empty.
	 */
	Task busiestTask();
	
	/**
	 * Finds all the tasks for the employee.
	 * @param e Employee
	 * @return Tasks
	 */
	List<Task> tasksForEmployee(Employee e);
	
	/**
	 * Finds all the tasks for the manager.
	 * @param m Manager
	 * @return Tasks
	 */
	List<Task> tasksForManager(Manager m);
	
}
