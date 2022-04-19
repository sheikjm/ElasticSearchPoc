package elastic.crud.poc.pojo;

import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.join.JoinField;

import java.util.Date;

@Document(indexName = "employee_index")
@Routing("employee_routing")
public class Employee {

    private String id;
    private String ename;
    private int age;
    private long salary;
    private Date createdAt=new Date();

    @Field(type = FieldType.Keyword)
    private String employee_routing;

    @JoinTypeRelations(relations = {
            @JoinTypeRelation(parent = "employee",children = {"family"})
    })
    private JoinField<String> relation=new JoinField<>("employee");

    @Field(type = FieldType.Keyword)
    private String routing;

    public String getId() {
        return id;
    }

    public String getEmployee_routing() {
        return employee_routing;
    }

    public void setEmployee_routing(String employee_routing) {
        this.employee_routing = employee_routing;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public JoinField<String> getRelaltion() {
        return relation;
    }

    public void setRelaltion(JoinField<String> relaltion) {
        this.relation = relaltion;
    }

    public String getRouting() {
        return routing;
    }

    public void setRouting(String routing) {
        this.routing = routing;
    }
}
