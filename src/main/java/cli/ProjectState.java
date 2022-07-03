package cli;

import models.company.Company;
import models.company.CompanyDaoService;
import models.customer.Customer;
import models.customer.CustomerDaoService;
import models.project.Project;
import models.project.ProjectDaoService;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ProjectState extends CliState{

    private Scanner scanner;
    private CliState state;
    private ConnectionProvider connectionProvider;

    public ProjectState(CliFSM fsm) {
        super(fsm);
        scanner = fsm.getScanner();
        state = fsm.getState();
        connectionProvider = fsm.getConnectionProvider();
    }

    List<String> commands = List.of(
            "1 : Create project",
            "2 : Get project by id",
            "3 : Get all projects",
            "4 : Update project",
            "5 : Delete project by id",
            "6 : Developers total salary of project",
            "7 : List of all projects in special format",
            "8 : Back",
            "9 : Exit"
    );

    @Override
    public void init() throws SQLException {
        projectInputLoop();
    }

    private void projectInputLoop() throws SQLException {
        int inputCommand;
        boolean status = true;
        while (status){
            commands.forEach(System.out::println);

            System.out.println("Choose a command number");
            inputCommand = Integer.parseInt(scanner.nextLine());

            if(inputCommand >= 1 && inputCommand <=9){
                switch (inputCommand){
                    case 9:{
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
                case 8:{
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
                    getTotalSalaryByProjectId();
                    break;
                }
                case 7:{
                    getAllProjectsInFormat();
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
        Project project = new Project();
        setProjectName(project, 1);
        setProjectDescription(project, 1);
        setReleaseDate(project, 1);
        setCompaniesId(project, 1);
        setCustomerId(project, 1);

        try{
            new ProjectDaoService(connectionProvider.createConnection()).createProject(project);
            System.out.println(true);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        projectInputLoop();
    }

    private void getById() throws SQLException {
        int id = setId(new Project(), 2).getProjectId();
        try {
            Project byId = new ProjectDaoService(connectionProvider.createConnection()).getProjectById(id);
            System.out.println(byId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        projectInputLoop();
    }

    private void getAll() throws SQLException {
        try {
            List<Project> all = new ProjectDaoService(connectionProvider.createConnection()).getAllProjects();
            all.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        projectInputLoop();
    }

    private void update() throws SQLException {
        Project project = new Project();
        setProjectName(project, 4);
        setProjectDescription(project, 4);
        setReleaseDate(project, 4);
        setCompaniesId(project, 4);
        setCustomerId(project, 4);

        try{
            new ProjectDaoService(connectionProvider.createConnection()).updateProject(project);
            System.out.println(true);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        projectInputLoop();

    }

    private void deleteById() throws SQLException {
        int id = setId(new Project(), 5).getProjectId();
        try {
            new ProjectDaoService(connectionProvider.createConnection()).deleteProjectById(id);
            System.out.println(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        projectInputLoop();
    }

    private void getTotalSalaryByProjectId() throws SQLException {
        int id = setId(new Project(), 6).getProjectId();
        try {
            int cost = new ProjectDaoService(connectionProvider.createConnection()).getDevelopersSalaryOfProject(id);
            System.out.println("DevelopersSalaryOfProject = " + cost);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        projectInputLoop();
    }

    private void getAllProjectsInFormat() throws SQLException {
        List<ProjectDaoService.project> allProjectsInSpecialFormat = new ProjectDaoService(connectionProvider.createConnection()).
                getProjectsInSpecialFormat();
        allProjectsInSpecialFormat.forEach(System.out::println);

        projectInputLoop();
    }


    private Project setId(Project project, int cmdNumber) {
        while (true) {
            System.out.println("Enter Id");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                project.setProjectId(id);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return project;
    }

    private void setProjectName(Project project, int cmdNumber){
        while (true) {
            System.out.println("Enter Project Name:");
            String projectName =  scanner.nextLine();
            try {
                project.setProjectName(projectName);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setProjectDescription(Project project,int cmdNumber){
        while (true) {
            System.out.println("Enter Project Description:");
            String projectDescription =  scanner.nextLine();
            try {
                project.setProjectName(projectDescription);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private  void setReleaseDate(Project project, int cmdNumber){
        while (true) {
            System.out.println("Enter Release date of project: ");
            try {
                LocalDate releaseDate = LocalDate.parse(scanner.nextLine());
                project.setReleaseDate(releaseDate);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("date format = year-month-day. For example: 2022-07-01");
            }
        }
    }

    private void setCompaniesId(Project project, int cmdNumber){
        while (true) {
            try {
                List<Company> companies = new CompanyDaoService(connectionProvider.createConnection()).getAllCompanies();
                System.out.println(companies);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Choose and enter id of company");
            int companyId = Integer.parseInt(scanner.nextLine());
            try {
                project.setCompaniesId(companyId);
                break;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void setCustomerId(Project project, int cmdNumber){
        while (true) {
            try {
                List<Customer> customers = new CustomerDaoService(connectionProvider.createConnection()).getAllCustomers();
                System.out.println(customers);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Choose and enter id of Customer: ");
            int customerId = Integer.parseInt(scanner.nextLine());
            try {
                project.setCustomerId(customerId);
                break;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}
