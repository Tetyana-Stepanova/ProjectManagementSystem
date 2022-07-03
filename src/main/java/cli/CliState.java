package cli;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class CliState {
    protected final CliFSM fsm;

    public void init() throws SQLException {}
    public void company() throws SQLException {}
    public void customer() throws SQLException {}
    public void project() throws SQLException {}
    public void developer() throws SQLException {}
    public void skill() throws SQLException {}
    public void unknownCommand(int cmd) {}
    public void idleState() throws SQLException {}
}
