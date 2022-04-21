package elastic.crud;

import elastic.crud.model.Customer;
import elastic.crud.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/saveCustomer")
    public int saveCustomer(@RequestBody List<Customer> customerList){
        customerRepository.saveAll(customerList);
        return customerList.size();
    }

    @GetMapping("/findAll")
    public Iterable<Customer> getAllCustomer(){
        return customerRepository.findAll();
    }
}
