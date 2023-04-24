package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTests {
    private String directReportsUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        directReportsUrl = "http://localhost:" + port + "/directReport/{id}";
    }

    @Test
    public void testGetReportingStructure(){
        //Test Data
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Lennon");
        testEmployee.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Development Manager");
        List<Employee> testDirectReports = new ArrayList<>(2);
        Employee subEmployee1 = new Employee();
        Employee subEmployee2 = new Employee();
        subEmployee1.setEmployeeId("b7839309-3348-463b-a7e3-5de1c168beb3");
        subEmployee2.setEmployeeId("03aa1462-ffa9-4978-901b-7c001562cf6f");
        testDirectReports.add(subEmployee1);
        testDirectReports.add(subEmployee2);
        testEmployee.setDirectReports(testDirectReports);

        ReportingStructure expectedReports = new ReportingStructure();
        expectedReports.setEmployee(testEmployee);
        expectedReports.setNumberOfReports(4);

        ReportingStructure directReports = restTemplate.getForEntity(directReportsUrl, ReportingStructure.class, testEmployee.getEmployeeId()).getBody();
        assertReportingStructureEquals(expectedReports, directReports);
    }

    private static void assertReportingStructureEquals(ReportingStructure expectedReports, ReportingStructure actualReports) {
        assertEmployeeEquals(expectedReports.getEmployee(), actualReports.getEmployee());
        assertEquals(expectedReports.getNumberOfReports(), actualReports.getNumberOfReports());
    }

    private static void assertEmployeeEquals(Employee expectedEmployee, Employee actualEmployee) {
        assertEquals(expectedEmployee.getEmployeeId(), actualEmployee.getEmployeeId());
        assertEquals(expectedEmployee.getDepartment(), actualEmployee.getDepartment());
        assertEquals(expectedEmployee.getDirectReports().size(), actualEmployee.getDirectReports().size());
        assertEquals(expectedEmployee.getPosition(), actualEmployee.getPosition());
        assertEquals(expectedEmployee.getLastName(), actualEmployee.getLastName());
        assertEquals(expectedEmployee.getFirstName(), actualEmployee.getFirstName());
    }
}
