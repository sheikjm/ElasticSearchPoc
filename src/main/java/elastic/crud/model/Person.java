package elastic.crud.model;

import java.util.Date;

public class Person {

    private int id;
    private String fullName;
    private Date dob;
    private int age;

    public Person(int id,String fullName, Date dob, int age) {
        this.fullName = fullName;
        this.dob = dob;
        this.age = age;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", dob=" + dob +
                ", age=" + age +
                '}';
    }
}
