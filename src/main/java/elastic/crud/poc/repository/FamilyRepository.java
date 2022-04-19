package elastic.crud.poc.repository;

import elastic.crud.poc.pojo.FamilyInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FamilyRepository extends ElasticsearchRepository<FamilyInfo,String> {
}
