package com.vekomy.request.service.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import com.vekomy.request.model.Timesheet;
import com.vekomy.request.service.dao.TimesheetDao;

import java.util.List;

@Repository("timesheetDao")
public class TimesheetDaoImpl extends HibernateDao<Timesheet, Long> implements TimesheetDao {

    @Override
    public List<Timesheet> list() {
        return currentSession().createCriteria(Timesheet.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
    }
}
