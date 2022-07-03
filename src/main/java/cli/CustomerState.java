package cli;

import models.customer.Customer;
import models.customer.CustomerDaoService;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CustomerState extends CliState{
    private Scanner scanner;
    private CliState state;
    private ConnectionProvider connectionProvider;

    public CustomerState(CliFSM fsm) {
        super(fsm);
        scanner = fsm.getScanner();
        state = fsm.getState();
        connectionProvider = fsm.getConnectionProvider();
    }

    List<String> commands = List.of(
            "1 : Create customer",
            "2 : Get customer by id",
            "3 : Get all customers",
            "4 : Update customer",
            "5 : Delete customer by id",
            "6 : Back",
            "7 : Exit"
    );

    @Override
    public void init() throws SQLException {
        customerInputLoop();
    }

    private void customerInputLoop() throws SQLException {
        int inputCommand;
        boolean status = true;
        while (status){
            commands.forEach(System.out::println);

            System.out.println("Choose a command number");
            inputCommand = Integer.parseInt(scanner.nextLine());

            if(inputCommand >= 1 && inputCommand <=7){
                switch (inputCommand){
                    case 7:{
                        System.exit(0);
                        status = false;
                        break;
                    }
                    default:{
                        status = false;
                        break;
                    }
                }
            } else {
                unknownCommand(inputCommand);
            }

            switch (inputCommand){
                case 6:{
                    idleState();
                    break;
                }
                case 1:{
                    create();
                    break;
                }
                case 2:{
                    getById();
                    break;
                }
                case 3:{
                    getAll();
                    break;
                }
                case 4:{
                    update();
                    break;
                }
                case 5:{
                    deleteById();
                    break;
                }
            }
        }
    }

    public void unknownCommand(int cmdNumber){
        state.unknownCommand(cmdNumber);
    }

    @Override
    public void idleState() throws SQLException {
        new CliFSM(connectionProvider);
    }

    private void create() throws SQLException {
        Customer customer = new Customer();
        setCustomersName(customer, 1);
        setCustomersDescription(customer, 1);

        try {
            new CustomerDaoService(connectionProvider.createConnection()).createCustomer(customer);
            System.out.println(true);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        customerInputLoop();
    }

    private void getById() throws SQLException {
        int id = setId(new Customer(), 2).getCustomerId();

        try {
            Customer byId = new CustomerDaoService(connectionProvider.createConnection()).getCustomerById(id);
            System.out.println(byId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        customerInputLoop();
    }

    private void getAll() throws SQLException {
        try {
            List<Customer> all = new CustomerDaoService(connectionProvider.createConnection()).getAllCustomers();
            all.forEach(System.out::println);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        customerInputLoop();
    }

    private void update() throws SQLException {
        Customer customer = new Customer();
        setCustomersName(customer, 4);
        setCustomersDescription(customer, 4);

        try {
            new CustomerDaoService(connectionProvider.createConnection()).updateCustomer(customer);
            System.out.println(true);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        customerInputLoop();
    }

    private void deleteById() throws SQLException {
        int id = setId(new Customer(), 2).getCustomerId();

        try {
            new CustomerDaoService(connectionProvider.createConnection()).deleteCustomerById(id);
            System.out.println(true);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        customerInputLoop();
    }

    private Customer setId(Customer customer, int cmdNumber){
        while (true) {
            System.out.println("Enter Id");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                customer.setCustomerId(id);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return customer;
    }

    private void setCustomersName(Customer customer, int cmdNumber){
        while (true) {
            System.out.println("Enter Customer Name:");
            String customerName =  scanner.nextLine();
            try {
                customer.setCustomersName(customerName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setCustomersDescription(Customer customer, int cmdNumber){
        while (true) {
            System.out.println("Enter Customer Description:");
            String customerDescription =  scanner.nextLine();
            try {
                customer.setCustomersDescriptions(customerDescription);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
