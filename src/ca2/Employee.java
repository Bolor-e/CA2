/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca2;

/**
 *
 * @author hsboo
 */
public class Employee {
    private String name;
    private JobPosition jobTitle;
    private Department department;

    public Employee(String name, JobPosition jobTitle, Department department) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public JobPosition getJobTitleType() {
        return jobTitle;
    }

    public Department getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return name + " | " + jobTitle + " | " + department;
    }
}