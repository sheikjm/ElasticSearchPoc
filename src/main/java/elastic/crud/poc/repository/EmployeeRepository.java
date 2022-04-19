package elastic.crud.poc.repository;

import elastic.crud.poc.pojo.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EmployeeRepository extends ElasticsearchRepository<Employee,String> {
}
