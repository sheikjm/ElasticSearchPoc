package elastic.crud.poc.pojo;

import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.join.JoinField;

@Document(indexName = "employee_index")
@Routing("family_routing")
public class FamilyInfo {

    private String id;
    private String motherName;
    private String fatherName;
    @Field(type = FieldType.Keyword)
    private String family_routing;
    @JoinTypeRelations(relations = {
            @JoinTypeRelation(parent = "employee",children = {"family"})
    })
    private JoinField<String> relation;

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getRouting() {
        return family_routing;
    }

    public void setRouting(String routing) {
        this.family_routing = routing;
    }

    public JoinField<String> getRelation() {
        return relation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRelation(JoinField<String> relation) {
        this.relation = relation;
    }
}
