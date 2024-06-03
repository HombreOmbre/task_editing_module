public class Student
    implements Comparable<Student> {
    private String studentName;

    public Student(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName() {
        return this.studentName;
    }

    @Override
    public int compareTo(Student student) {
        return this.studentName.hashCode() - student.getStudentName().hashCode();
    }

    @Override
    public String toString() {
        return this.studentName;
    }
}
