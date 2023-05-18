package kberestbrewery.services;

import kberestbrewery.web.model.CustomerDTO;

import java.util.UUID;

public interface CustomerService {
    CustomerDTO getCustomerById(UUID customerID);
    CustomerDTO saveNewCustomer(CustomerDTO customerDTO);
    void updateCustomer(UUID customerId, CustomerDTO customerDTO);
    void deleteCustomerById(UUID customerId);
}
