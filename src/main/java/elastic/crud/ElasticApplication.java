package elastic.crud;

import elastic.crud.model.Answer;
import elastic.crud.model.Statement;
import elastic.crud.repo.AnswerRepository;
import elastic.crud.repo.StatementRepository;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.join.JoinField;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;


import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.join.query.JoinQueryBuilders.hasChildQuery;

@SpringBootApplication
public class ElasticApplication implements CommandLineRunner {

	//https://github.com/spring-projects/spring-data-elasticsearch/blob/416daef2f92734d0d1b5681265d719b9f1c347d5/src/test/java/org/springframework/data/elasticsearch/repositories/custom/SampleCustomMethodRepository.java

	//https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.query-methods

	//"https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-query-builders.html"

	@Autowired
	StatementRepository statementRepository;
	@Autowired
	AnswerRepository answerRepository;

	@Autowired
	ElasticsearchOperations elasticsearchOperations;

	@Autowired
	ElasticSearchJavaCli baseClassForEs;



	public static void main(String[] args) {
		SpringApplication.run(ElasticApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		baseClassForEs.reIndexDocs();

		Statement statement=new Statement();
		statement.setId("3");
		statement.setGender("male");
		statement.setText("parent text 3rd record");
		statement.setRelaltion(new JoinField<>("statement"));

		Statement saveStatement=statementRepository.save(statement);

		statement=new Statement();
		statement.setId("2");
		statement.setGender("female");
		statement.setText("parent text");
		statement.setRelaltion(new JoinField<>("statement"));

		saveStatement=statementRepository.save(statement);

		Answer answer=new Answer();
		answer.setId("3445");
		answer.setText("child text");
		answer.setRelaltion(new JoinField<>("answer", saveStatement.getId()));

		answerRepository.save(answer);

		hasVotes().stream().forEach(e->{
			System.out.println(e.toString());
		});


		findByName().stream().forEach(e->{
			System.out.println(e.toString());
		});
		fuzziness().stream().forEach(e->{
			System.out.println(e.toString());
		});
		multiMatchQuery();

		System.out.println("before enterd custom query");
		statementRepository.findAllStatement("female");

		statementRepository.byParentId("answer","2");

		baseClassForEs.indexDocument();
		baseClassForEs.searchRequestExample();


	}


	SearchHits<Statement> hasVotes() {
		NativeSearchQuery query = new NativeSearchQueryBuilder()
				.withQuery(hasChildQuery("answer", matchAllQuery(), ScoreMode.None))
				.build();

		return elasticsearchOperations.search(query, Statement.class);
	}


	SearchHits<Statement> findByName(){
		System.out.println("entered");
		NativeSearchQuery nativeSearchQuery=new NativeSearchQueryBuilder()
				.withQuery(matchQuery("text","parent"))

				.build();

		//return elasticsearchOperations.search(nativeSearchQuery,Statement.class);
		return elasticsearchOperations.search(nativeSearchQuery,Statement.class, IndexCoordinates.of("statements"));
	}
	SearchHits<Statement> fuzziness(){
		System.out.println("entered to fuzzy query");
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchQuery("text", "par")
						.operator(Operator.AND)
						.fuzziness(Fuzziness.ONE)
						.prefixLength(3))
				.build();
		return elasticsearchOperations.search(searchQuery,Statement.class, IndexCoordinates.of("statements"));
	}
	void multiMatchQuery(){

		QueryBuilder q=QueryBuilders.queryStringQuery("male").field("gender");

		QueryBuilder queryBuilder=QueryBuilders.boolQuery()
				.should(QueryBuilders.queryStringQuery("parent")
						.lenient(true)
						.field("text")
				).mustNot(QueryBuilders.queryStringQuery("female")
						.lenient(true)
						.field("gender")
				);
		NativeSearchQuery build = new NativeSearchQueryBuilder()
				.withQuery(queryBuilder)
				.build();

		SearchHits<Statement> searchHits=elasticsearchOperations.search(build,Statement.class, IndexCoordinates.of("statements"));
		searchHits.stream().forEach(e->{
			System.out.println(e.toString());
		});

		NativeSearchQuery build1 = new NativeSearchQueryBuilder()
				.withQuery(q)
				.build();
		elasticsearchOperations.search(build1,Statement.class, IndexCoordinates.of("statements"));
		searchHits.stream().forEach(e->{
			System.out.println("********************");
			System.out.println(e.toString());
		});
	}


}
