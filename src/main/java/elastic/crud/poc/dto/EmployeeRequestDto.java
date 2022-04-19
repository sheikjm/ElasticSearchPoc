package elastic.crud.poc.dto;

import elastic.crud.poc.pojo.FamilyInfo;

public class EmployeeRequestDto {

    private String ename;
    private int age;
    private long salary;
    private FamilyRequestDto family;

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public FamilyRequestDto getFamily() {
        return family;
    }

    public void setFamily(FamilyRequestDto family) {
        this.family = family;
    }
}
