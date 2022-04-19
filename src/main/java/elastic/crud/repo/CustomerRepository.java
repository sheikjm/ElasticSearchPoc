package elastic.crud.repo;

import elastic.crud.model.Customer;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CustomerRepository extends ElasticsearchRepository<Customer,String> {
    List<Customer> findByFirstname(String name);

    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    List<Customer> findByC(String str);
}
