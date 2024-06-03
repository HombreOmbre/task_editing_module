import java.util.Scanner;
import java.util.Iterator;

public class Task
    extends StringValidator {

    private String taskName;
    private MyArray<DescMethods> taskDesc;

    public Task(String taskName) {
        this.taskName = taskName;
        this.taskDesc = new MyArray<>(8);
    }

    public void setTaskName(String newTaskName) {
        this.taskName = newTaskName;
    }

    public boolean addTaskDescription(String descriptionLine) {
            descriptionLine = descriptionLine.trim();

            if (descriptionLine.equals("--")) {
                return false;
            }

            if (checkIfLatex(descriptionLine)) {
                this.taskDesc.add(new LatexDesc(descriptionLine));
            }   else {
                if (checkIfImg(descriptionLine)) {
                    this.taskDesc.add(new ImgDesc(descriptionLine));
                } else {
                    this.taskDesc.add(new StringDesc(descriptionLine));
                }
            }

        return true;
    }

    public void changeTaskDesc() {
        Scanner scanner = new Scanner(System.in);
        MyArray<DescMethods> tmpArr = new MyArray<>(this.taskDesc);
        boolean isTrue = true;
        int tmpArrSize = tmpArr.getSize();
        int lineNum = 1;
        this.taskDesc = new MyArray<>(8);
        int index = 0;

        System.out.println("Press enter if you don't want make any changes in line above \n Press \"\\n\" for adding new empty line or \"ESC\" for end changing rest of the description \n Press \"--\" for ending description at this point");
        while (isTrue) {
            String desc;
            if (index < tmpArrSize) {
                desc = tmpArr.get(index++).getDescValue();
            } else {
                desc = "";
            }

            System.out.println((lineNum++) + ") " + desc);
            String newDescLine = scanner.nextLine();
            newDescLine = newDescLine.trim();

            if (newDescLine.equals("--")) {
                isTrue = false;
                return;
            }

            if (newDescLine.equals("ESC")) {
                isTrue = false;
                if (index < tmpArrSize) {
                    for (int k = index; k < tmpArrSize; k++) {
                        String tmpDesc = tmpArr.get(k).getDescValue();

                        if (checkIfLatex(tmpDesc)) {
                            this.taskDesc.add(new LatexDesc(tmpDesc));
                        } else {
                            if (checkIfImg(tmpDesc)) {
                                this.taskDesc.add(new ImgDesc(tmpDesc));
                            } else {
                                this.taskDesc.add(new StringDesc(tmpDesc));
                            }
                        }
                    }
                }
                return;
            }

            if (newDescLine.isEmpty()) {
                newDescLine = desc;
            }

            if (newDescLine.equals("\\n")) {
                newDescLine = "";
                index -= 1;
            }

            if (checkIfLatex(newDescLine)) {
                this.taskDesc.add(new LatexDesc(newDescLine));
            } else {
                if (checkIfImg(newDescLine)) {
                    this.taskDesc.add(new ImgDesc(newDescLine));
                } else {
                    this.taskDesc.add(new StringDesc(newDescLine));
                }
            }
        }
    }

    public String getTaskDescriptionStr() {
        StringBuilder taskDescription = new StringBuilder();
        Iterator<DescMethods> iterator = this.taskDesc.iterator();

        while (iterator.hasNext())
            taskDescription.append(iterator.next().getDescValue() + "\n");

        return taskDescription.toString();
    }
}
