package elastic.crud.repo;

import elastic.crud.model.Answer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AnswerRepository extends ElasticsearchRepository<Answer,String> {
}
