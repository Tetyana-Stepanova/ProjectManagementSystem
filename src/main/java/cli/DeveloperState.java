package cli;

import models.company.Company;
import models.company.CompanyDaoService;
import models.developer.Developer;
import models.developer.DeveloperDaoService;
import models.project.Project;
import models.project.ProjectDaoService;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DeveloperState extends CliState{
    private Scanner scanner;
    private CliState state;
    private ConnectionProvider connectionProvider;

    public DeveloperState(CliFSM fsm) {
        super(fsm);
        scanner = fsm.getScanner();
        state = fsm.getState();
        connectionProvider = fsm.getConnectionProvider();
    }

    List<String> commands = List.of(
            "1 : Create developer",
            "2 : Get developer by id",
            "3 : Get all developers",
            "4 : Update developer",
            "5 : Delete developer by id",
            "6 : List of developers by project id",
            "7 : List of all Java developers",
            "8 : List of all Middle developers",
            "9 : Back",
            "10 : Exit"
    );

    @Override
    public void init() throws SQLException {
        developerInputLoop();
    }

    private void developerInputLoop() throws SQLException {
        int inputCommand;
        boolean status = true;
        while (status){
            commands.forEach(System.out::println);

            System.out.println("Choose a command number");
            inputCommand = Integer.parseInt(scanner.nextLine());

            if(inputCommand >= 1 && inputCommand <=10){
                switch (inputCommand){
                    case 10:{
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
                case 9:{
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
                case 6:{
                    getByProjectId();
                    break;
                }
                case 7:{
                    getAllJavaDev();
                    break;
                }
                case 8:{
                    getAllMiddleDev();
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
        //fsm.setState(new IdleState(fsm));
    }

    private void create() throws SQLException {
        Developer developer = new Developer();
        setFirstName(developer, 1);
        setLastName(developer, 1);
        setAge(developer, 1);
        setGender(developer, 1);
        setCity(developer, 1);
        setSalary(developer, 1);
        setCompaniesId(developer, 1);

        try{
            new DeveloperDaoService(connectionProvider.createConnection()).createDeveloper(developer);
            System.out.println(true);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        developerInputLoop();
    }

    private void getById() throws SQLException {
        int id = setId(new Developer(), 2).getDevelopersId();
        try {
            Developer byId = new DeveloperDaoService(connectionProvider.createConnection()).getDeveloperById(id);
            System.out.println(byId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        developerInputLoop();
    }

    private void getAll() throws SQLException {
        try {
            List<Developer> all = new DeveloperDaoService(connectionProvider.createConnection()).getAllDevelopers();
            all.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        developerInputLoop();
    }

    private void update() throws SQLException {
        Developer developer = new Developer();
        setFirstName(developer, 4);
        setLastName(developer, 4);
        setAge(developer, 4);
        setGender(developer, 4);
        setCity(developer, 4);
        setSalary(developer, 4);
        setCompaniesId(developer, 4);

        try {
            new DeveloperDaoService(connectionProvider.createConnection()).update(developer);
            System.out.println(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        developerInputLoop();

    }

    private void deleteById() throws SQLException {
        int id = setId(new Developer(), 5).getDevelopersId();
        try {
            new DeveloperDaoService(connectionProvider.createConnection()).deleteById(id);
            System.out.println(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        developerInputLoop();
    }

    private void getByProjectId() throws SQLException {
        while (true) {
            try{
                List<Project> projects = new ProjectDaoService(connectionProvider.createConnection()).getAllProjects();
                projects.forEach(System.out::println);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("Enter project_id:");
            try {
                int projectId = Integer.parseInt(scanner.nextLine());
                List<Developer> developersByProjectId = new DeveloperDaoService(connectionProvider.createConnection()).
                        getDevelopersByProject(projectId);
                developersByProjectId.forEach(System.out::println);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        developerInputLoop();
    }

    private void getAllJavaDev() throws SQLException {
        while (true){
            try {
                List<Developer> developersJava = new DeveloperDaoService(connectionProvider.createConnection()).
                        getAllJavaDevelopers();
                developersJava.forEach(System.out::println);
                break;
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        developerInputLoop();
    }

    private void getAllMiddleDev() throws SQLException {
        while (true){
            try {
                List<Developer> developersMiddle = new DeveloperDaoService(connectionProvider.createConnection()).
                        getAllMiddleDevelopers();
                developersMiddle.forEach(System.out::println);
                break;
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        developerInputLoop();
    }

    private Developer setId(Developer developer, int cmdNumber) {
        while (true) {
            System.out.println("Enter Id");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                developer.setDevelopersId(id);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return developer;
    }

    private void setFirstName(Developer developer, int cmdNumber) {
        while (true) {
            System.out.println("Enter First Name:");
            String firstName =  scanner.nextLine();
            try {
                developer.setFirstName(firstName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setLastName(Developer developer, int cmdNumber){
        while (true) {
            System.out.println("Enter Last Name");
            String lastName =  scanner.nextLine();
            try {
                developer.setLastName(lastName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setAge(Developer developer, int cmdNumber) {
        while (true) {
            System.out.println("Enter Age:");
            try {
                int age = Integer.parseInt(scanner.nextLine());
                developer.setAge(age);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setGender(Developer developer, int cmdNumber){
        while (true){
            System.out.println("Enter gender:");
            String gender = scanner.nextLine();
            try {
                developer.setGender(gender);
                break;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void setCity(Developer developer, int cmdNumber){
        while (true){
            System.out.println("Enter City:");
            String city = scanner.nextLine();
            try {
                developer.setCity(city);
                break;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void setSalary(Developer developer, int cmdNumber){
        while (true){
            System.out.println("Enter Salary:");
            int salary = Integer.parseInt(scanner.nextLine());
            try {
                developer.setSalary(salary);
                break;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void setCompaniesId(Developer developer, int cmdNumber){
        while (true) {
            try {
                List<Company> companies = new CompanyDaoService(connectionProvider.createConnection()).getAllCompanies();
                companies.forEach(System.out::println);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Choose and enter id of company");
            int companyId = Integer.parseInt(scanner.nextLine());
            try {
                developer.setCompaniesId(companyId);
                break;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
