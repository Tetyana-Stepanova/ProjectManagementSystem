package cli;

import models.skill.Skill;
import models.skill.SkillDaoService;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SkillState extends CliState{
    private Scanner scanner;
    private CliState state;
    private ConnectionProvider connectionProvider;

    public SkillState(CliFSM fsm) {
        super(fsm);
        scanner = fsm.getScanner();
        state = fsm.getState();
        connectionProvider = fsm.getConnectionProvider();
    }

    List<String> commands = List.of(
            "1 : Create skill",
            "2 : Get skill by id",
            "3 : Get all skills",
            "4 : Update skill",
            "5 : Delete skill by id",
            "6 : Back",
            "7 : Exit"
    );

    @Override
    public void init() throws SQLException {
        skillInputLoop();
    }

    private void skillInputLoop() throws SQLException {
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
        Skill skill = new Skill();
        setDevLanguage(skill, 1);
        setSkillLevel(skill, 1);

        try {
            new SkillDaoService(connectionProvider.createConnection()).createSkill(skill);
            System.out.println(true);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        skillInputLoop();
    }

    private void getById() throws SQLException {
        int id = setId(new Skill(), 2).getSkillsId();

        try {
            Skill byId = new SkillDaoService(connectionProvider.createConnection()).getSkillById(id);
            System.out.println(byId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        skillInputLoop();
    }

    private void getAll() throws SQLException {
        try {
            List<Skill> all = new SkillDaoService(connectionProvider.createConnection()).getAllSkills();
            all.forEach(System.out::println);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        skillInputLoop();
    }

    private void update() throws SQLException {
        Skill skill = new Skill();
        setDevLanguage(skill, 4);
        setSkillLevel(skill, 4);

        try {
            new SkillDaoService(connectionProvider.createConnection()).updateSkill(skill);
            System.out.println(true);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        skillInputLoop();
    }

    private void deleteById() throws SQLException {
        int id = setId(new Skill(), 2).getSkillsId();

        try {
            new SkillDaoService(connectionProvider.createConnection()).deleteSkill(id);
            System.out.println(true);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        skillInputLoop();

    }

    private Skill setId(Skill skill, int cmdNumber){
        while (true) {
            System.out.println("Enter Id");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                skill.setSkillsId(id);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return skill;
    }

    private void setDevLanguage(Skill skill, int cmdNumber){
       while (true){
           System.out.println("Enter developer's Language:");
           String devLanguage =  scanner.nextLine();
           try {
               skill.setDevLanguage(devLanguage);
               break;
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
       }
    }

    private void setSkillLevel(Skill skill, int cmdNumber){
        while (true){
            System.out.println("Enter skill's level:");
            String skillsLevel =  scanner.nextLine();
            try {
                skill.setSkillLevel(skillsLevel);
                break;
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
