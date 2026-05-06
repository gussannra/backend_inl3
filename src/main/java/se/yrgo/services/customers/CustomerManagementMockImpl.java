package se.yrgo.services.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

public class CustomerManagementMockImpl implements CustomerManagementService {
    private final HashMap<String, Customer> customerMap;

    public CustomerManagementMockImpl() {
        customerMap = new HashMap<String, Customer>();
        customerMap.put("OB74", new Customer("OB74", "Fargo Ltd", "some notes"));
        customerMap.put("NV10", new Customer("NV10", "North Ltd", "some other notes"));
        customerMap.put("RM210", new Customer("RM210", "River Ltd", "some more notes"));
    }


    @Override
    public void newCustomer(Customer newCustomer) {
        customerMap.put(newCustomer.getCustomerId(), newCustomer);
    }

    @Override
    public void updateCustomer(Customer changedCustomer) {
        changedCustomer.setCompanyName("ExampleCompanyName");
        changedCustomer.setCustomerId("ExampleCustomerId");
        changedCustomer.setEmail("example.company@example.com");
        changedCustomer.setTelephone("012345Example");
        changedCustomer.setNotes("some example notes");
    }

    @Override
    public void deleteCustomer(Customer oldCustomer) {
        customerMap.remove(oldCustomer.getCustomerId(), oldCustomer);
    }

    @Override
    public Customer findCustomerById(String customerId) {
        return customerMap.get(customerId);
    }

    @Override
    public List<Customer> findCustomersByName(String name) {
        List<Customer> customerList = new ArrayList<>();
        for (Map.Entry<String, Customer> entry : customerMap.entrySet()) {
            if (entry.getValue().getCompanyName().equals(name)) {
                customerList.add(entry.getValue());
            }
        }

        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();

        for (Map.Entry<String, Customer> customerEntry : customerMap.entrySet()) {
            customerList.add(customerEntry.getValue());
        }

        return customerList;
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) {
        Customer customer;

        for (Map.Entry<String, Customer> customerEntry : customerMap.entrySet()) {
            if (customerEntry.getKey().equals(customerId)) {
                customer = customerEntry.getValue();
                return customer;
            }
        }

        return null;
    }

    @Override
    public void recordCall(String customerId, Call callDetails) {
        //First find the customer
        Customer customer = customerMap.get(customerId);

        //Call the addCall on the customer
        customer.addCall(callDetails);
    }
}
