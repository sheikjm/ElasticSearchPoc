package elastic.crud.poc;

import com.alibaba.fastjson.JSON;
import elastic.crud.model.Person;
import elastic.crud.model.Statement;
import elastic.crud.poc.dto.EmployeeRequestDto;
import elastic.crud.poc.pojo.Employee;
import elastic.crud.poc.pojo.FamilyInfo;
import elastic.crud.poc.repository.EmployeeRepository;
import elastic.crud.poc.repository.FamilyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.join.query.HasChildQueryBuilder;
import org.elasticsearch.join.query.HasParentQueryBuilder;
import org.elasticsearch.join.query.JoinQueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.join.JoinField;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

@RestController()
@RequestMapping(value = "/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    ElasticsearchOperations elasticsearchOperations;



    @PostMapping(value = "/save")

    public String saveEmployee(@RequestBody EmployeeRequestDto requestDto){

        log.info(requestDto.getEname());

        Employee employee=new Employee();
        employee.setEname(requestDto.getEname());
        employee.setAge(requestDto.getAge());
        employee.setSalary(requestDto.getSalary());
        Employee savedEmployee=employeeRepository.save(employee);

        FamilyInfo familyInfo=new FamilyInfo();
        familyInfo.setFatherName(requestDto.getFamily().getFatherName());
        familyInfo.setMotherName(requestDto.getFamily().getMotherName());
        familyInfo.setRelation(new JoinField<>("family", savedEmployee.getId()));
        familyRepository.save(familyInfo);

        return savedEmployee.getId();
    }

    @GetMapping("/getEmployeeByChild")
    public Employee getEmployeeByChild(@RequestParam String fatherName){
        HasChildQueryBuilder query = JoinQueryBuilders.hasChildQuery(
                "family",QueryBuilders.matchQuery("fatherName",fatherName) ,ScoreMode.None
        );

        NativeSearchQuery build1 = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();

       SearchHits<Employee> employeeSearchHits= elasticsearchOperations.search(build1, Employee.class, IndexCoordinates.of("employee_index"));
       return employeeSearchHits.getSearchHit(0).getContent();

    }

    @GetMapping("/getFamilyByParentId")
    public FamilyInfo getFamilyByParentId(@RequestParam String id){


        HasParentQueryBuilder query=JoinQueryBuilders.hasParentQuery(
                "employee",QueryBuilders.matchQuery("_id",id),true
        );

        NativeSearchQuery build1 = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();

        SearchHits<FamilyInfo> employeeSearchHits= elasticsearchOperations.search(build1, FamilyInfo.class, IndexCoordinates.of("employee_index"));
        return employeeSearchHits.getSearchHit(0).getContent();

    }

    @GetMapping("/getEmployeeByAsc")
    public List<Employee> getEmployeeAsc(){

        QueryBuilder query=QueryBuilders.matchAllQuery();

        NativeSearchQuery queryBuilder = new NativeSearchQueryBuilder().withSorts(SortBuilders.fieldSort("createdAt").order(SortOrder.DESC))
                .withQuery(query)
                .build();

        SearchHits<Employee> employeeSearchHits= elasticsearchOperations.search(queryBuilder, Employee.class, IndexCoordinates.of("employee_index"));


        return employeeSearchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
    @GetMapping("/getEmployeeByAgeRange")
    public List<Employee> getEmployeeByAgeRange(){
        QueryBuilder queryBuilder= QueryBuilders.boolQuery()
                .must(rangeQuery("age").gt(21))
                .must(rangeQuery("age").lte(30));

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();
        SearchHits<Employee> employeeSearchHits= elasticsearchOperations.search(nativeSearchQuery, Employee.class, IndexCoordinates.of("employee_index"));


        return employeeSearchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

}
