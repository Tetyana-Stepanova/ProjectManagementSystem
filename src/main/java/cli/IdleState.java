package cli;

import java.sql.SQLException;

public class IdleState extends CliState {

    public IdleState(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(int inputCommand){
        System.out.println("Unknown command = " + inputCommand);
        System.out.println("Correct number of command is from 2 to 6, number 1 to exit");
    }

    @Override
    public void company() throws SQLException {
        fsm.setState(new CompanyState(fsm));
    }

    @Override
    public void customer() throws SQLException {
        fsm.setState(new CustomerState(fsm));
    }

    @Override
    public void project() throws SQLException {
        fsm.setState(new ProjectState(fsm));
    }

    @Override
    public void developer() throws SQLException {
        fsm.setState(new DeveloperState(fsm));
    }

    @Override
    public void skill() throws SQLException {
        fsm.setState(new SkillState(fsm));
    }
}
