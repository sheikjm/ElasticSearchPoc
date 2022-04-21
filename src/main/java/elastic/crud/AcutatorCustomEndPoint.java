package elastic.crud;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Endpoint(id="customApi")
@Component
public class AcutatorCustomEndPoint {

    @ReadOperation
    @Bean
    public String greet() {
        return "Hello from custom endpoint";
    }
}
