import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TaskEditorApp
    extends ConsoleManipulator {

    // Add new path in Windows
    private static final String path = "~/ProjectOne/";
    private Map<String, Task> tasksList;

    public TaskEditorApp() {
        this.tasksList = new HashMap<>();
    }

    public void startApp() {
        makeDirectoryForTasks();
        addExistingTaskToMap();

        boolean isTrue = true;

        while (isTrue) {
            int userInput = showMainMenu();

            switch (OptionsOrder.getOption(userInput)) {
                case FIRST -> addNewTask();
                case SECOND -> editTask();
                case THIRD -> tasksManager();
                case ESC -> isTrue = false;
            }
        }
    }

    private void addNewTask() {
        String taskName = getNewTaskName(this.tasksList);

        tasksList.put(taskName, new Task(taskName));
        Task task = tasksList.get(taskName);
        boolean isTrue;

        addSpacesToConsole();
        System.out.println(" Enter \"--\" to stop adding description \n Press enter for empty line.");
        do {
            isTrue = task.addTaskDescription(getTaskDescription());
        } while (isTrue);
        clearLineCounter();

        System.out.println("Task added successfully");
        int saveToFile = showOptionsToSaveTask();

        switch (OptionsOrder.getOption(saveToFile)) {
            case FIRST -> saveTaskToFile(taskName);
            case SECOND -> addSpacesToConsole();
        }
    }

    private void editTask() {
        String taskName = getExistingTaskName(this.tasksList);
        if (taskName.equals("ESC")) {
            return;
        }

        boolean isTrue = true;
        do {
            int userInput = getEditTaskMenu();

            switch (OptionsOrder.getOption(userInput)) {
                case FIRST -> taskName = changeTaskName(taskName);
                case SECOND -> {
                    tasksList.get(taskName).changeTaskDesc();
                    changeTaskDescriptionFile(taskName);
                }
                case THIRD -> saveTaskToFile(taskName);
                case ESC -> isTrue = false;
            }
        } while (isTrue);
    }

    private void tasksManager() {
        boolean isTrue = true;

        while (isTrue) {
        int userInput = showTaskManagerMenu();

            switch (OptionsOrder.getOption(userInput)) {
                case FIRST -> numberOfAllResponsesForTask();
                case SECOND -> numberOfTotalResponsesForStudent();
                case THIRD -> showAllTasksDescription();
                case FOURTH -> showSequentialAllStudentResponses();
                case ESC -> isTrue = false;
            }
        }
    }

    private void numberOfAllResponsesForTask() {
        String taskName = getExistingTaskName(this.tasksList);
        if (taskName.equals("ESC")) {
            return;
        }

        MyArray<Student> studentsNamesArr = getAllStudentsNames();
        Iterator<Student> iterator = studentsNamesArr.iterator();
        int counter = 0;
        String studentsPath = path + "Students/";

        while (iterator.hasNext()) {
            Path pathToTask = Paths.get(studentsPath, iterator.next().getStudentName(), taskName, "Main.java");

            if (Files.exists(pathToTask)) {
                    counter++;
            }
        }

        System.out.println("There is  " + counter + " responses for " + taskName);
        addSpacesToConsole();
    }

    private void numberOfTotalResponsesForStudent() {
        MyArray<Student> studentsNamesArr = getAllStudentsNames();
        String studentName = getStudentName(studentsNamesArr);
        String pathToStudentFolder = path + "Students/" + studentName + "/";
        int counter = 0;

        if (Files.exists(Paths.get(pathToStudentFolder))) {
            File studentDirectory = new File(pathToStudentFolder);
            MyArray<String> arrWithTasksDirectories = new MyArray<>(8);
            File[] listFiles = studentDirectory.listFiles();

            if (listFiles != null) {
                Arrays.stream(listFiles)
                        .map(path -> path.getName())
                        .sorted((path1, path2) -> path1.compareTo(path2))
                        .forEach(arrWithTasksDirectories::add);

                Iterator<String> iterator = arrWithTasksDirectories.iterator();

                while (iterator.hasNext()) {
                    String taskName = iterator.next();
                    Path pathToTaskFolder = Paths.get(pathToStudentFolder, taskName, "/", "Main.java");
                    if (Files.exists(Paths.get(pathToTaskFolder.toString()))) {
                        counter++;
                    }
                }
            }
        }

        System.out.println("Student " + studentName + " sent " + counter + " responses");
        addSpacesToConsole();
    }

    private void showAllTasksDescription() {
        Iterator<String> iterator = tasksList.keySet().iterator();
        boolean isTrue = true;

        while (iterator.hasNext() && isTrue) {
            String taskName = iterator.next();
            System.out.println("Task: " + taskName);
            System.out.println(tasksList.get(taskName).getTaskDescriptionStr());

            System.out.println("\nType \">>\" to move on to the next job description or press \"ESC\" to return to the manager menu");
            String userInput = getUserNextOrEndInput();

            if (userInput.equals("ESC")) {
                isTrue = false;
            }
        }

        if (!iterator.hasNext()) {
            System.out.println("There are no more tasks to show");
        }
        addSpacesToConsole();
    }

    private void showSequentialAllStudentResponses() {
        MyArray<Student> studentsNamesArr = getAllStudentsNames();
        String studentName = getStudentName(studentsNamesArr);

        List<String> directoriesNames = getDirectoriesForStudent(studentName);
        Map<String, String> studentSolutions = getDescriptionsFromStudentsSolutions(studentName, directoriesNames);
        List<String> solutionsArr = studentSolutions.keySet().stream().sorted((name1, name2) -> name1.compareTo(name2)).toList();
        int solutionsArrSize = solutionsArr.size();
        boolean isTrue = true;
        int index = 0;

        if (solutionsArrSize > 0) {
            addSpacesToConsole();
            while (isTrue) {
                String solutionName = solutionsArr.get(index);
                System.out.println("Task: " + solutionName);
                System.out.println(studentSolutions.get(solutionName));

                System.out.println("\nType \">>\" to the next solution, type \"<<\" to the previous solution or type \"ESC\" to return to the manager menu");
                String userInput = getUserNavigationInput();

                if (userInput.equals("ESC")) {
                    isTrue = false;
                }

                if (userInput.equals("<<")) {
                    if (index == 0) {
                        index = solutionsArrSize - 1;
                    } else {
                        index -= 1;
                    }
                }

                if (userInput.equals(">>")) {
                    if (index == (solutionsArrSize - 1)) {
                        index = 0;
                    } else {
                        index += 1;
                    }
                }
            }
        } else {
            System.out.println("This student has not sent any solution");
        }
        addSpacesToConsole();
    }

    private void saveTaskToFile(String taskName) {
        if (!Files.exists(Paths.get(path, "Tasks", taskName))) {
            try {
                // Change slash in Windows
                FileWriter newTask = new FileWriter(path + "Tasks/" + taskName + ".txt");
                newTask.write(tasksList.get(taskName).getTaskDescriptionStr());
                newTask.close();

                addTaskFolder(taskName);
                System.out.println("File with task made successfully");
            } catch (IOException e) {
                System.out.println("Something gone wrong");
            }
        } else {
            System.out.println("Such a file already exists");
        }
    }

    public String changeTaskName(String existingTaskName) {
        String newTaskName = getNewTaskName(this.tasksList);

        Task tmpTask = tasksList.get(existingTaskName);
        tasksList.put(newTaskName, tmpTask);
        tmpTask.setTaskName(existingTaskName);
        tasksList.remove(existingTaskName);

        renameTaskFolder(existingTaskName, newTaskName);
        renameTaskFile(existingTaskName, newTaskName);

        return newTaskName;
    }

    private MyArray<Student> getAllStudentsNames() {
        String directoryPath = path + "Students";
        MyArray<Student> studentsNamesArr = new MyArray<>(8);

        if (Files.exists(Paths.get(directoryPath))) {
            File studentsDirectory = new File(directoryPath);

            File[] filesName = studentsDirectory.listFiles();

            if (filesName != null) {
                Arrays.stream(filesName)
                        .map(path -> new Student(path.getName()))
                        .sorted(
                                (stud1, stud2) -> stud1.compareTo(stud2)
                        )
                        .forEach(studentsNamesArr::add);
            } else {
                System.out.println("Students directory is empty");
            }
        }

        return studentsNamesArr;
    }

    private void addTaskFolder(String taskName) {
        MyArray<Student> studentsNamesArr = getAllStudentsNames();
        Iterator<Student> iterator = studentsNamesArr.iterator();
        String studentsFolder = path + "Students";

        while (iterator.hasNext()) {
            String studentName = iterator.next().getStudentName();
            Path taskFolderPath = Paths.get(studentsFolder, studentName, taskName);

            if (!Files.exists(taskFolderPath)) {
                try {
                    Files.createDirectories(taskFolderPath);
                } catch (IOException e) {
                    System.out.println("Something gone wrong: " + e);
                }
            }
        }
    }

    private List<String> getDirectoriesForStudent(String studentName) {
        List<String> directoriesNames = new ArrayList<>();
        String pathToStudentDirecotry = path + "Students/" + studentName;

        if (Files.exists(Paths.get(pathToStudentDirecotry))) {
            File studentDirectory = new File(pathToStudentDirecotry);
            Arrays.stream(studentDirectory.listFiles())
                    .map(
                            path -> path.getName()
                    )
                    .sorted((path1, path2) -> path1.compareTo(path2))
                    .forEach(directoriesNames::add);
        }

        return directoriesNames;
    }

    private Map<String, String> getDescriptionsFromStudentsSolutions(String studentName, List<String> directoriesNames) {
        Iterator<String> iterator = directoriesNames.iterator();
        Map<String, String> studentSolutions = new HashMap<>();

        while (iterator.hasNext()) {
            String directoryName = iterator.next();
            String pathToTask = path + "Students/" + studentName + "/" + directoryName + "/" + "Main.java";
            Path pathToSolution = Paths.get(pathToTask);

            if (Files.exists(pathToSolution)) {
                String solution = new MySolutionsReader() {
                    @Override
                    public String getSolutionsTxt(Path path) {
                        List<String> listWithSolution = new ArrayList<>();
                        try {
                            List<String> lines = Files.readAllLines(path);
                            lines.stream()
                                    .map(line -> line + "\n")
                                    .forEach(listWithSolution::add);
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        String lastLine = listWithSolution.getLast().replace("\\n", "");
                        listWithSolution.removeLast();
                        listWithSolution.addLast(lastLine);

                        StringBuilder sb = new StringBuilder();
                        listWithSolution.forEach(sb::append);

                        return sb.toString();
                    }
                }.getSolutionsTxt(Paths.get(pathToTask));

                studentSolutions.put(directoryName, solution);
            }
        }

        return studentSolutions;
    }

    private void renameTaskFolder(String oldTaskName, String newTaskName) {
        MyArray<Student> studentsNamesArr = getAllStudentsNames();
        Iterator<Student> iterator = studentsNamesArr.iterator();
        String studentsFolder = path + "Students/";


        while (iterator.hasNext()) {
            String studentName = iterator.next().getStudentName();
            String taskFolder = studentsFolder + studentName;

            Path pathToOldTaskFolder = Paths.get(taskFolder, oldTaskName);

            if (Files.exists(pathToOldTaskFolder)) {
                try {
                    Files.move(pathToOldTaskFolder, Paths.get(taskFolder, newTaskName));
                } catch (IOException e) {
                    System.out.println("Something gone wrong with renaming folders in Students directory.");
                }
            }
        }
    }

    private void renameTaskFile(String oldTaskName, String newTaskName) {
        String tasksFolder = path + "Tasks";
        Path pathToOldTaskFile = Paths.get(tasksFolder, (oldTaskName + ".txt"));

        if (Files.exists(pathToOldTaskFile)) {
            try {
                Files.move(pathToOldTaskFile, Paths.get(tasksFolder, (newTaskName + ".txt")));
            }catch (IOException e) {
                System.out.println("Something gone wrong with renaming files.");
            }
        }
    }

    private void changeTaskDescriptionFile(String taskName) {
        if (Files.exists(Paths.get(path, "Tasks", (taskName + ".txt")))) {
            try {
                FileWriter file = new FileWriter(path + "Tasks/" + taskName + ".txt");
                file.write(this.tasksList.get(taskName).getTaskDescriptionStr());
                file.close();

                System.out.println("Overwriting successfully completed");
            } catch (IOException e) {
                System.out.println("Something wrong with overwriting description.");
            }
        } else {
            int userOption = showOptionsToSaveTask();

            switch(OptionsOrder.getOption(userOption)) {
                case FIRST -> saveTaskToFile(taskName);
                case SECOND -> addSpacesToConsole();
            }
        }
    }

    private void makeDirectoryForTasks() {
        Path tasksDirectory = Paths.get(path, "Tasks");

        if (!Files.exists(tasksDirectory)) {
            try {
                Files.createDirectory(tasksDirectory);
            } catch (IOException e) {
                System.out.println("Something gone wrong during creating \"Tasks\" directory");
            }
        }
    }

    public void addExistingTaskToMap() {
        if (Files.exists(Paths.get(path, "Tasks"))) {
            File tasksDir = new File(path + "Tasks");
            if (tasksDir != null) {
                List<String> existingTasksNames = new ArrayList<>();
                Arrays.stream(tasksDir.listFiles())
                        .map(path -> path.getName().split("[.]")[0])
                        .sorted((task1, task2) -> task1.compareTo(task2))
                        .forEach(existingTasksNames::add);


                Iterator<String> iterator = existingTasksNames.iterator();

                while (iterator.hasNext()) {
                    String taskName = iterator.next();
                    List<String> taskDesc = new MyTasksReader() {
                        @Override
                        public List<String> getTaskDescToList(Path path) {
                            List<String> listWithDesc = new ArrayList<>();
                            try {
                                List<String> lines = Files.readAllLines(path);
                                listWithDesc.addAll(lines);
                            } catch (IOException e) {
                                System.out.println(e);
                            }

                            return listWithDesc;
                        }
                    }.getTaskDescToList(Paths.get("~/ProjectOne/Tasks/", (taskName + ".txt")));

                    tasksList.put(taskName, new Task(taskName));

                    Task task = tasksList.get(taskName);
                    for (String line : taskDesc) {
                        task.addTaskDescription(line);
                    }
                }
            }
        }
    }
}