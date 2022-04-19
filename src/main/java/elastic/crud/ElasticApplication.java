package elastic.crud;

import elastic.crud.model.Answer;
import elastic.crud.model.Statement;
import elastic.crud.repo.AnswerRepository;
import elastic.crud.repo.StatementRepository;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.join.JoinField;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.join.query.JoinQueryBuilders.hasChildQuery;

@SpringBootApplication
public class ElasticApplication implements CommandLineRunner {

	@Autowired
	StatementRepository statementRepository;
	@Autowired
	AnswerRepository answerRepository;

	@Autowired
	ElasticsearchOperations elasticsearchOperations;

	public static void main(String[] args) {
		SpringApplication.run(ElasticApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Statement statement=new Statement();
		statement.setId("123");
		statement.setText("parent text");
		statement.setRelaltion(new JoinField<>("statement"));

		Statement saveStatement=statementRepository.save(statement);

		Answer answer=new Answer();
		answer.setId("3445");
		answer.setText("child text");
		answer.setRelaltion(new JoinField<>("answer", saveStatement.getId()));

		answerRepository.save(answer);

		hasVotes().stream().forEach(e->{
			System.out.println(e.toString());
		});

	}
	SearchHits<Statement> hasVotes() {
		NativeSearchQuery query = new NativeSearchQueryBuilder()
				.withQuery(hasChildQuery("answer", matchAllQuery(), ScoreMode.None))
				.build();

		return elasticsearchOperations.search(query, Statement.class);
	}


}
