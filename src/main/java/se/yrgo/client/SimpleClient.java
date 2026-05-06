package se.yrgo.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import se.yrgo.dataaccess.CustomerDao;
import se.yrgo.domain.Action;
import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;
import se.yrgo.services.calls.CallHandlingService;
import se.yrgo.services.customers.CustomerManagementService;
import se.yrgo.services.customers.CustomerNotFoundException;
import se.yrgo.services.diary.DiaryManagementService;

public class SimpleClient {

    public static void main(String[] args) {

        try (ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml")) {

            CustomerManagementService customerService = container.getBean(CustomerManagementService.class);
            CallHandlingService callService = container.getBean(CallHandlingService.class);
            DiaryManagementService diaryService = container.getBean(DiaryManagementService.class);

            customerService.newCustomer(new Customer("CS03939", "Acme", "Good Customer"));

            Call newCall = new Call("Larry Wall called from Acme Corp");
            Action action1 = new Action("Call back Larry to ask how things are going", new GregorianCalendar(2016, 0, 0), "rac");
            Action action2 = new Action("Check our sales dept to make sure Larry is being tracked", new GregorianCalendar(2016, 0, 0), "rac");

            List<Action> actions = new ArrayList<Action>();
            actions.add(action1);
            actions.add(action2);

            try {
                callService.recordCall("CS03939", newCall, actions);
            } catch (CustomerNotFoundException e) {
                System.out.println("That customer doesn't exist");
            }

            System.out.println("Here are the outstanding actions:");
            Collection<Action> incompleteActions = diaryService.getAllIncompleteActions("rac");
            for (Action next : incompleteActions) {
                System.out.println(next);
            }

            // Test getting all customers
            System.out.println("\nTEST GETTING ALL CUSTOMERS... ");
            List<Customer> customerList = customerService.getAllCustomers();
            for (Customer customer : customerList) {
                System.out.println(customer);
            }

            // Test creating new customers
            customerService.newCustomer(new Customer("ABC123", "Rolling", "Nice customer"));
            customerService.newCustomer(new Customer("DEF456", "Splash", "Super customer"));
            customerService.newCustomer(new Customer("GHI789", "DRUMS", "Want to get it touch with Jerry."));

            // Test getting new customers
            System.out.println("\nTEST IF ADDING NEW CUSTOMERS WORKED... ");
            List<Customer> getAllCustomersAfterAdding = customerService.getAllCustomers();
            for (Customer customer : getAllCustomersAfterAdding) {
                System.out.println(customer);
            }

            // Test updating customer info
            try {
                Customer customer1 = customerService.findCustomerById("ABC123");
                System.out.println("\nUPDATING CUSTOMER COMPANY NAME... ");
                customer1.setCompanyName("Big Company");

                customerService.updateCustomer(customer1);
                Customer customer1Updated = customerService.findCustomerById("ABC123");
                System.out.println("UPDATED CUSTOMER: " + customer1Updated.getCustomerId() + " and updated company name " + customer1Updated.getCompanyName());

            } catch (CustomerNotFoundException e) {
                System.err.println("Customer not found.");
            }

            // Test getting all customers
            System.out.println("\nTEST GETTING ALL CUSTOMERS AGAIN... ");
            List<Customer> anotherCustomerList = customerService.getAllCustomers();
            for (Customer customer : anotherCustomerList) {
                System.out.println(customer);
            }

            // Test finding customer by name
            try {
                System.out.println("\nTEST GETTING CUSTOMERS BY NAME... ");
                List<Customer> customerByNameList = customerService.findCustomersByName("Big Company");
                for (Customer customer : customerByNameList) {
                    System.out.println(customer);
                }
            } catch (CustomerNotFoundException e) {
                System.err.println("Customer not found.");
            }

            // Test getting full customer detail
            try {
                Customer customerDetails = customerService.getFullCustomerDetail("CS03939");
                System.out.println("\nCUSTOMER DETAILS FOR " + customerDetails);
                List<Call> customerDetailsCalls = customerDetails.getCalls();
                for (Call call : customerDetailsCalls) {
                    System.out.println(call);
                }
            } catch (CustomerNotFoundException e) {
                System.err.println("Customer not found.");
            }

            // Test deleting a customer
            try {
                Customer customerToDelete = customerService.findCustomerById("ABC123");
                System.out.println("\nTEST DELETING CUSTOMER: " + customerToDelete);
                customerService.deleteCustomer(customerToDelete);

                List<Customer> listAfterDeletedCustomer = customerService.getAllCustomers();
                for (Customer customer : listAfterDeletedCustomer) {
                    System.out.println(customer);
                }
            } catch (CustomerNotFoundException e) {
                System.err.println("Customer you want to delete could not be found.");
            }

            // Test exceptions kedja
            System.out.println("\nTEST EXCEPTIONS... ");
            try {
                customerService.findCustomerById("jdhfj");
            } catch (CustomerNotFoundException e) {
                System.err.println("Customer not found.");
            }

        }
    }
}