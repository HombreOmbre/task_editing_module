import java.nio.file.Path;
import java.util.List;

public interface MyTasksReader {
    public List<String> getTaskDescToList(Path path);
}
