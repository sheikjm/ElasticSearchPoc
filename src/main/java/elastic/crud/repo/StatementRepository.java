package elastic.crud.repo;

import elastic.crud.model.Statement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StatementRepository extends ElasticsearchRepository<Statement,String> {
}
