import utils.Commands;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String instruction;
        while (!(instruction = input.nextLine()).equals("wvc exit")) {
            if (instruction.startsWith("wvc set cwd ")) {
                Commands.setWorkingDirectory(instruction.substring(12));
                continue;
            }
            if (instruction.startsWith("wvc username ")) {
                Commands.setAuthor(instruction.substring(13));
            }
            if (instruction.equals("wvc init")) {
                Commands.initialize();
                continue;
            }
            if (instruction.startsWith("wvc add ")) {
                Commands.add(instruction.substring(8));
                continue;
            }
            if (instruction.startsWith("wvc commit ")) {
                Commands.commit(instruction.substring(instruction.indexOf("\"") + 1, instruction.lastIndexOf("\"")));
                continue;
            }
            if (instruction.startsWith("wvc branch ")) {
                Commands.branch(instruction.substring(11));
                continue;
            }
            if (instruction.startsWith("wvc switch ")) {
                Commands.switchBranches(instruction.substring(11));
                continue;
            }
            if (instruction.equals("wvc lb")) {
                System.out.println(Commands.listBranches());
                continue;
            }
            if (instruction.equals("wvc lf")) {
                System.out.println(Commands.listFiles());
                continue;
            }
            if (instruction.equals("wvc log")) {
                System.out.println(Commands.getLogs());
                continue;
            }

            if (instruction.equals("wvc reflog")) {
                System.out.println(Commands.getRefLogs());
            }

            if (instruction.startsWith("wvc reset ")) {
                Commands.reset(instruction.substring(10));
                continue;
            }
            System.out.println("Command not found.");
        }
    }
}
