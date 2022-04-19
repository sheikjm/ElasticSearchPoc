package elastic.crud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.join.JoinField;

@Document(indexName = "statements")
@Routing("routing")
public class Statement {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String text;

    @Field(type = FieldType.Keyword)
    private String routing;

    @Field(type = FieldType.Text)
    private String gender;

    @JoinTypeRelations(relations = {
            @JoinTypeRelation(parent = "statement",children = {"answer"})
    })
    private JoinField<String> relaltion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRouting() {
        return routing;
    }

    public void setRouting(String routing) {
        this.routing = routing;
    }

    public JoinField<String> getRelaltion() {
        return relaltion;
    }

    public void setRelaltion(JoinField<String> relaltion) {
        this.relaltion = relaltion;
    }

    @Override
    public String toString() {
        return "Statement{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", routing='" + routing + '\'' +
                ", relaltion=" + relaltion +
                '}';
    }
}
