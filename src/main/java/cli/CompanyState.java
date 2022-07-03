package cli;

import models.company.Company;
import models.company.CompanyDaoService;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CompanyState extends CliState{
    private Scanner scanner;
    private CliState state;
    private ConnectionProvider connectionProvider;

    public CompanyState(CliFSM fsm) {
        super(fsm);
        scanner = fsm.getScanner();
        state = fsm.getState();
        connectionProvider = fsm.getConnectionProvider();
    }

    List<String> commands = List.of(
            "1 : Create company",
            "2 : Get company by id",
            "3 : Get all companies",
            "4 : Update company",
            "5 : Delete company by id",
            "6 : Back",
            "7 : Exit"
    );

    @Override
    public void init() throws SQLException {
        companyInputLoop();
    }

    private void companyInputLoop() throws SQLException {
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
        Company company = new Company();
        setCompaniesName(company, 1);
        setCompaniesDescription(company, 1);

        try{
            new CompanyDaoService(connectionProvider.createConnection()).createCompany(company);
            System.out.println(true);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        companyInputLoop();
    }

    private void getById() throws SQLException {
        int id = setId(new Company(), 2).getCompaniesId();
        try {
            Company byId = new CompanyDaoService(connectionProvider.createConnection()).getCompanyById(id);
            System.out.println(byId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        companyInputLoop();
    }

    private void getAll() throws SQLException {
        try {
            List<Company> all = new CompanyDaoService(connectionProvider.createConnection()).getAllCompanies();
            all.forEach(System.out::println);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        companyInputLoop();
    }

    private void update() throws SQLException {
        Company company = new Company();
        setCompaniesName(company, 4);
        setCompaniesDescription(company, 4);

        try {
            new CompanyDaoService(connectionProvider.createConnection()).updateCompany(company);
            System.out.println(true);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        companyInputLoop();
    }

    private void deleteById() throws SQLException {
        int id = setId(new Company(), 5).getCompaniesId();

        try {
            new CompanyDaoService(connectionProvider.createConnection()).deleteCompanyById(id);
            System.out.println(true);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        companyInputLoop();
    }

    private Company setId(Company company, int cmdNumber){
        while (true) {
            System.out.println("Enter Id");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                company.setCompaniesId(id);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return company;
    }

    private void setCompaniesName(Company company, int cmdNumber){
        while (true) {
            System.out.println("Enter Company Name:");
            String companyName =  scanner.nextLine();
            try {
                company.setCompaniesName(companyName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setCompaniesDescription(Company company, int cmdNumber){
        while (true) {
            System.out.println("Enter Company Description:");
            String companyDescription =  scanner.nextLine();
            try {
                company.setCompaniesDescription(companyDescription);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
