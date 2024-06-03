import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map;

public class ConsoleManipulator {

    private static int lineCounter = 1;

    protected static void clearLineCounter() {
        lineCounter = 1;
    }

    protected void addSpacesToConsole(){
        for (int i = 0; i < 2; i++)
            System.out.println();
    };

    protected int showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        boolean isTrue;

        do {
            System.out.println("Choose one option");
            System.out.println("   1) Add new task");
            System.out.println("   2) Edit task");
            System.out.println("   3) Tasks manager");
            System.out.println("   0) Exit");

            try {
                userInput = scanner.nextInt();

                if (userInput < 0 || userInput > 3) {
                    isTrue = true;
                    System.out.println("Please choose number from 0 to 3");
                } else {
                    isTrue = false;
                }
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input. Please enter number from 0 to 3");
                isTrue = true;
                scanner.nextLine();
            }
        } while (isTrue);

        return userInput;
    }

    protected String getNewTaskName(Map<String, Task> tasksList) {
        Scanner scanner = new Scanner(System.in);
        boolean isTrue;
        String taskName;

        addSpacesToConsole();
        do {
            System.out.print("Enter name for task: ");
            taskName = scanner.nextLine();

            if (tasksList.containsKey(taskName)) {
                isTrue = true;
                System.out.println("Already exists task with this name. Try another one.");
            } else {
                isTrue = false;
            }
        } while (isTrue);

        return taskName;
    }

    protected String getTaskDescription() {
        Scanner scanner = new Scanner(System.in);

        System.out.print((lineCounter++) + ") ");

        return scanner.nextLine().trim();
    }

    protected int getEditTaskMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean isTrue;
        int userInput = 0;

        do {
            try {
                System.out.println("Choose one option");
                System.out.println("   1) Change task name");
                System.out.println("   2) Change description");
                System.out.println("   3) Save task");
                System.out.println("   0) Back to main menu");
                userInput = scanner.nextInt();

                if (userInput < 0 || userInput > 3) {
                    System.out.println("Please enter number from 0 to 3");
                    isTrue = true;
                } else {
                    isTrue = false;
                }

            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input. Please enter number from 0 to 3");
                isTrue = true;
                scanner.nextLine();
            }
        } while (isTrue);

        return userInput;
    }

    protected int showOptionsToSaveTask() {
        Scanner scanner = new Scanner(System.in);
        boolean isTrue;
        int userInput = 2;

        addSpacesToConsole();
        do {
            System.out.println("Do you want to save this task to text file?");
            System.out.println("   1) Yes");
            System.out.println("   2) No");

            try {
                userInput = scanner.nextInt();

                if (userInput < 1 || userInput > 2) {
                    isTrue = true;
                    System.out.println("Please choose number from 1 to 2");
                } else {
                    isTrue = false;
                }
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input. Please enter number from 1 to 2");
                isTrue = true;
                scanner.nextLine();
            }
        } while (isTrue);

        return userInput;
    }

    protected String getExistingTaskName(Map<String, Task> tasksList) {
        Scanner scanner = new Scanner(System.in);
        boolean isTrue;
        String userInput;

        addSpacesToConsole();
        do {
                System.out.println("   Enter \"ESC\" to go back to main menu.");
                System.out.print("Enter task name to edit: ");

                userInput = scanner.nextLine();

                if ((!tasksList.containsKey(userInput.trim())) && (!userInput.equals("ESC"))) {
                    System.out.println("There is no such task, try another one.");
                    isTrue = true;
                } else {
                    isTrue = false;
                }

        } while(isTrue);

        return userInput;
    }

    protected int showTaskManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean isTrue;
        int userInput = 0;

        do {
            try {
                System.out.println("Choose one option:");
                System.out.println("   1) Get number of all responses to the task");
                System.out.println("   2) Get number of total responses in one student");
                System.out.println("   3) Show all tasks descriptions");
                System.out.println("   4) Show all responses from student");
                System.out.println("   0) Back to main menu");
                userInput = scanner.nextInt();

                if (userInput < 0 || userInput > 4) {
                    System.out.println("Please enter number from 0 to 4");
                    isTrue = true;
                } else {
                    isTrue = false;
                }

            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input. Please enter number from 0 to 4");
                isTrue = true;
                scanner.nextLine();
            }
        } while (isTrue);

        return userInput;
    }

    protected String getStudentName(MyArray<Student> students) {
        Scanner scanner = new Scanner(System.in);
        boolean isTrue = true;
        String studentName;

        addSpacesToConsole();
        System.out.println("Enter student name for searching or enter \"ESC\" for quit.");
        do {
            Iterator<Student> iterator = students.iterator();

            studentName = scanner.nextLine().trim();

            if (studentName.equals("ESC")) {
                return "ESC";
            }

            while (iterator.hasNext() && isTrue) {
                isTrue = !(iterator.next().getStudentName().equals(studentName));
            }

            if (isTrue) {
                System.out.println("There is no such student try another one");
            }
        } while (isTrue);

        return studentName;
    }

    protected String getUserNextOrEndInput() {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        boolean isTrue;

        do {
            userInput = scanner.next().trim();

            if (userInput.equals("ESC") || userInput.equals(">>")) {
                isTrue = false;
            } else {
                isTrue = true;
            }

        } while (isTrue);

        return userInput;
    }

    protected String getUserNavigationInput() {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        boolean isTrue;

        do {
            userInput = scanner.nextLine();

            if (userInput.equals(">>") ||
                userInput.equals("<<") ||
                userInput.equals("ESC")
            ) {
                isTrue = false;
            } else {
                System.out.println("Wrong input. Try enter \"<<\" , \">>\" or \"ESC\"");
                isTrue = true;
            }
        } while (isTrue);

        return userInput;
    }
}
