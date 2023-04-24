package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure getReportingStructure(String id) {
        LOG.debug("Calculating reporting structure for employeeID: [{}]", id);
        ReportingStructure resultingReportingStructure = new ReportingStructure();

        Employee theHeadEmployee = employeeRepository.findByEmployeeId(id);
        resultingReportingStructure.setEmployee(theHeadEmployee);
        resultingReportingStructure.setNumberOfReports(CalculateNumberOfReports(theHeadEmployee));
        return resultingReportingStructure;
    }

    private int CalculateNumberOfReports(Employee theEmployee) {
        if(theEmployee.getFirstName() == null) {
            LOG.debug("Getting employee information for employee: [{}]", theEmployee.getEmployeeId());
            theEmployee = employeeRepository.findByEmployeeId(theEmployee.getEmployeeId());
        }
        if (theEmployee.getDirectReports() == null || theEmployee.getDirectReports().size() == 0) {
            LOG.debug("No direct reports for employee: [{} {}].", theEmployee.getFirstName(), theEmployee.getLastName());
            return 0;
        }
        int numberOfDirectReports = theEmployee.getDirectReports().size();
        for (Employee directReportEmployee : theEmployee.getDirectReports()) {
            numberOfDirectReports = numberOfDirectReports + CalculateNumberOfReports(directReportEmployee);
        }
        return numberOfDirectReports;
    }
}
