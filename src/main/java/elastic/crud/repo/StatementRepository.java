package elastic.crud.repo;

import elastic.crud.model.Statement;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface StatementRepository extends ElasticsearchRepository<Statement,String> {

    // @Query("{"query":{"bool":{"must":[{"match":{"userId":"?0"}},{"nested":{"path":"questions","query":{"bool":{"must":[{"match":{"questions.id":"?1"}}]}}}}]}}}")"

    @Query("{\"match\": {\"gender\": \"?0\"}}")
    public List<Statement> findAllStatement(String gender);

    @Query("{\"parent_id\": {\"type\": \"?0\",\"id\": \"?1\"}}")
    public List<Statement> byParentId(String child,String id);
}
