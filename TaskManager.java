import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class TeamMember {
    private String name;
    private String teamId;
    private String memberId;

    public TeamMember(String name, String teamId, String memberId) {
        this.name = name;
        this.teamId = teamId;
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}

class Task {
    private static int taskCount = 0;
    private int taskId;
    private String name;
    private String description;
    private String teamId;

    public Task(String name, String description) {
        this.taskId = ++taskCount;
        this.name = name;
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setTaskId(int newTaskId) {
        this.taskId = newTaskId;
    }
}

public class TaskManager {
    private List<Task> tasks;
    private List<TeamMember> teamMembers;

    public TaskManager() {
        tasks = new ArrayList<>();
        teamMembers = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Task added!");
    }

    public void removeTask(int taskIdToRemove) {
        displayTasks();

        int indexToRemove = -1;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskId() == taskIdToRemove) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            tasks.remove(indexToRemove);
            System.out.println("Task removed!");
            adjustTaskIds();
        } else {
            System.out.println("Task not found!");
        }

        displayTasks();
    }

    private void adjustTaskIds() {
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setTaskId(i + 1);
        }
    }

    public void displayTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Task not found");
        } else {
            System.out.println("\n+-------------------+-------------------+-------------------+-------------------+");
            System.out.printf("| %-17s | %-17s | %-17s | %-17s |%n", "Task ID", "Task Name", "Description", "Assigned Team");
            System.out.println("+-------------------+-------------------+-------------------+-------------------+");

            for (Task task : tasks) {
                System.out.printf("| %-17d | %-17s | %-17s | %-17s |%n", task.getTaskId(), task.getName(), task.getDescription(), task.getTeamId());
                System.out.println("+-------------------+-------------------+-------------------+-------------------+");
            }
        }
    }

    public void addTeam(String teamNameOrId, int numMembers) {
        // Check if the team ID already exists
        if (isTeamIdExist(teamNameOrId)) {
            System.out.println("Team with the same ID already exists. Please enter a different team ID.");
            return;
        }

        char teamIdentifier = teamNameOrId.toLowerCase().charAt(0);
        for (int i = 1; i <= numMembers; i++) {
            System.out.print("Enter team member name " + ": ");
            Scanner scanner = new Scanner(System.in);
            String teamMemberName = scanner.nextLine();
            teamMembers.add(new TeamMember(teamMemberName, teamNameOrId, teamIdentifier + String.format("%03d", i)));
        }
        System.out.println("Team added successfully!");
    }

    private boolean isTeamIdExist(String teamId) {
        for (TeamMember member : teamMembers) {
            if (member.getTeamId().equalsIgnoreCase(teamId)) {
                return true;
            }
        }
        return false;
    }

    public void removeTeam(String teamIdToRemove) {
        // Display existing teams before removal
        System.out.println("\nExisting Teams:");
        displayTeamMembers();

        List<TeamMember> removedMembers = new ArrayList<>();

        // Find and remove all members of the team
        teamMembers.removeIf(member -> {
            if (member.getTeamId().equalsIgnoreCase(teamIdToRemove)) {
                removedMembers.add(member);
                return true;
            }
            return false;
        });

        if (!removedMembers.isEmpty()) {
            System.out.println("Team removed!");
        } else {
            System.out.println("Team not found!");
        }

        // Display updated team information after removal
        System.out.println("\nUpdated Teams:");
        displayTeamMembers();
    }

    public void displayTeamMembers() {
        if (teamMembers.isEmpty()) {
            System.out.println("No team members found.");
        } else {
            System.out.println("\n+-------------------+-------------------+-------------------+");
            System.out.printf("| %-17s | %-17s | %-17s |%n", "Team Member Name", "Team ID", "Member ID");
            System.out.println("+-------------------+-------------------+-------------------+");
            for (TeamMember member : teamMembers) {
                System.out.printf("| %-17s | %-17s | %-17s |%n", member.getName(), member.getTeamId(), member.getMemberId());
                System.out.println("+-------------------+-------------------+-------------------+");
            }
        }
    }

    public void assignTask() {
        displayTasks();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter task ID to assign: ");
        int taskId = scanner.nextInt();

        if (taskId >= 1 && taskId <= tasks.size()) {
            Task task = tasks.get(taskId - 1);

            if (task != null) {
                displayTeamMembersWithIds();

                System.out.print("Enter team ID to assign the task: ");
                scanner.nextLine();
                String teamId = scanner.nextLine();

                if (isTeamIdValid(teamId)) {
                    assignTaskToTeam(task, teamId);
                } else {
                    System.out.println("Invalid Team ID.");
                }
            } else {
                System.out.println("Can not find the task.");
            }
        } else {
            System.out.println("Invalid Task ID.");
        }
    }

    private void assignTaskToTeam(Task task, String teamId) {
        int memberIdCount = 1;

        for (TeamMember member : teamMembers) {
            if (member.getTeamId().equalsIgnoreCase(teamId)) {
                member.setMemberId(teamId.charAt(0) + String.format("%03d", memberIdCount++));
            }
        }

        task.setTeamId(teamId);
        System.out.println("Task '" + task.getName() + "' assigned to Team: " + teamId);
    }

    private void displayTeamMembersWithIds() {
        System.out.println("\nAll Team :");
        System.out.println("+-------------------+-------------------+-------------------+-------------------+");
        System.out.printf("| %-17s | %-17s | %-17s | %-17s |%n", "Team Member Name", "Team ID", "Member ID", "Assigned Task");
        System.out.println("+-------------------+-------------------+-------------------+-------------------+");

        for (TeamMember member : teamMembers) {
            Task assignedTask = getTaskByTeamId(member.getTeamId());
            String assignedTaskName = (assignedTask != null) ? assignedTask.getName() : "Not Assigned";
            System.out.printf("| %-17s | %-17s | %-17s | %-17s |%n", member.getName(), member.getTeamId(), member.getMemberId(), assignedTaskName);
            System.out.println("+-------------------+-------------------+-------------------+-------------------+");
        }
    }

    private Task getTaskByTeamId(String teamId) {
        for (Task task : tasks) {
            if (task.getTeamId() != null && task.getTeamId().equalsIgnoreCase(teamId)) {
                return task;
            }
        }
        return null;
    }

    public void displayAllData() {
        System.out.println("\nAll Data:");
        displayTasks();
        displayTeamMembers();
    }

    private boolean isTeamIdValid(String teamId) {
        for (TeamMember member : teamMembers) {
            if (member.getTeamId().equalsIgnoreCase(teamId)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nTask allocation");
            System.out.println("1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. Show Tasks");
            System.out.println("4. Add Team");
            System.out.println("5. Remove Team");
            System.out.println("6. Display Team Members");
            System.out.println("7. Assign Task to Team");
            System.out.println("8. Show All Data");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter task name: ");
                    scanner.nextLine();
                    String taskName = scanner.nextLine();
                    System.out.print("Enter task description: ");
                    String taskDescription = scanner.nextLine();
                    taskManager.addTask(new Task(taskName, taskDescription));
                    break;
                case 2:
                    taskManager.displayTasks();
                    System.out.print("Enter task ID to remove: ");
                    int taskIdToRemove = scanner.nextInt();
                    taskManager.removeTask(taskIdToRemove);
                    break;
                case 3:
                    taskManager.displayTasks();
                    break;
                case 4:
                    System.out.print("Enter team name or ID: ");
                    scanner.nextLine();
                    String teamNameOrId = scanner.nextLine();

                    int numMembers = 0;
                    boolean validInput = false;

                    while (!validInput) {
                        System.out.print("How many members in the team: ");

                        try {
                            numMembers = Integer.parseInt(scanner.nextLine());
                            validInput = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid integer.");
                        }
                    }

                    taskManager.addTeam(teamNameOrId, numMembers);
                    break;
                case 5:
                    // Display existing teams before removal
                    System.out.println("\nExisting Teams:");
                    taskManager.displayTeamMembers();

                    // Ask for team ID to remove
                    System.out.print("Enter team ID to remove: ");
                    scanner.nextLine();
                    String teamIdToRemove = scanner.nextLine();
                    taskManager.removeTeam(teamIdToRemove);
                    break;
                case 6:
                    taskManager.displayTeamMembers();
                    break;
                case 7:
                    taskManager.assignTask();
                    break;
                case 8:
                    taskManager.displayAllData();
                    break;
                case 9:
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
}