package cli;

import lombok.Getter;
import storage.ConnectionProvider;

import java.sql.SQLException;
import java.util.*;

import java.util.Scanner;

public class CliFSM {
    @Getter
    private CliState state;
    @Getter
    private Scanner scanner;

    @Getter
    private ConnectionProvider connectionProvider;

    List<String> commands = List.of(
            "1 : Exit",
            "2 : Developers",
            "3 : Projects",
            "4 : Skills",
            "5 : Companies",
            "6 : Customers"
    );

    public CliFSM(ConnectionProvider connectionProvider) throws SQLException {
        this.connectionProvider = connectionProvider;

        state = new IdleState(this);

        scanner = new Scanner(System.in);

        startInputLoop();
    }

    public void startInputLoop() throws SQLException {
        int inputCommand;

        while (true) {
            commands.forEach(System.out::println);

            System.out.println("Choose a command number");
            inputCommand = Integer.parseInt(scanner.nextLine());

            if(inputCommand >= 1 && inputCommand <=6){
                if (inputCommand == 1) {
                    System.exit(0);
                } else {
                    break;
                }
            } else {
                unknownCommand(inputCommand);
            }
        }


        switch (inputCommand){
            case 2: {
                developer();
                break;
            }
            case 3: {
                project();
                break;
            }
            case 4: {
                skill();
                break;
            }
            case 5: {
                company();
                break;
            }
            case 6: {
                customer();
                break;
            }
        }
    }

    public void company() throws SQLException {
        state.company();
    }

    public void customer() throws SQLException {
        state.customer();
    }

    public void project() throws SQLException {
        state.project();
    }

    public void developer() throws SQLException {
        state.developer();
    }

    public void skill() throws SQLException {
        state.skill();
    }

    public void unknownCommand(int cmd) {

        state.unknownCommand(cmd);
    }

    public void setState(CliState state) throws SQLException {
        this.state = state;
        state.init();
    }

}
