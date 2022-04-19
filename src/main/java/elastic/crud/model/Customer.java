package elastic.crud.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "customer")
public class Customer {

    @Id
    private String id;
    private String firstname;
    private String lastname;

    @Field(type = FieldType.Auto,name = "age")
    private int age;

    @Field(type = FieldType.Object,name = "address")
    private Address address;
    @Field(type = FieldType.Nested,name = "course")
    private List<Course> course;

    public Customer(String id, String firstname, String lastname, int age, Address address, List<Course> course) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.address = address;
        this.course = course;
    }

    public Address getAddress() {
        return address;
    }

    public List<Course> getCourse() {
        return course;
    }

    public void setCourse(List<Course> course) {
        this.course = course;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
