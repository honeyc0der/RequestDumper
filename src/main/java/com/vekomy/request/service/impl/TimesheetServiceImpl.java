package com.vekomy.request.service.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.vekomy.request.model.Employee;
import com.vekomy.request.model.Manager;
import com.vekomy.request.model.Task;
import com.vekomy.request.service.TimesheetService;
import com.vekomy.request.service.dao.TaskDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service("timesheetService")
public class TimesheetServiceImpl implements TimesheetService {

    // dependencies
	@Autowired
    private SessionFactory sessionFactory;
	@Autowired
    private TaskDao taskDao;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Task busiestTask() {
        List<Task> tasks = taskDao.list();
        if (tasks.isEmpty()) {
            return null;
        }
        
        Task busiest = tasks.get(0);
        for (Task task : tasks) {
            if (task.getAssignedEmployees().size() > busiest.getAssignedEmployees().size()) {
                busiest = task;
            }
        }
        
        return busiest;
    }

    public List<Task> tasksForEmployee(Employee employee) {
        List<Task> allTasks = taskDao.list();
        List<Task> tasksForEmployee = new ArrayList<Task>();
        
        for (Task task : allTasks) {
            if (task.getAssignedEmployees().contains(employee)) {
                tasksForEmployee.add(task);
            }
        }

        return tasksForEmployee;
    }

    public List<Task> tasksForManager(Manager manager) {
        Query query = currentSession()
                .createQuery("from Task t where t.manager.id = :id");
        query.setParameter("id", manager.getId());
        return query.list();
    }
}
