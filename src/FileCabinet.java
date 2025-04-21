import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileCabinet implements Cabinet {
    private List<Folder> folders;

    @Override
    public Optional<Folder> findFolderByName(String name) {
        List<Folder> folders = flattenedFolders();
        return folders.stream().filter(f -> name.equals(f.getName())).findFirst();
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {
        List<Folder> folders = flattenedFolders();
        return folders.stream().filter(f -> size.equals(f.getSize())).collect(Collectors.toList());
    }

    @Override
    public int count() {
        List<Folder> folders = flattenedFolders();
        return folders.size();
    }

    private List<Folder> flattenedFolders() {
        List<Folder> result = new ArrayList<>();
        ArrayDeque<Folder> queue = new ArrayDeque<>(folders);
        while (!queue.isEmpty()) {
            Folder folder = queue.getFirst();
            if (folder instanceof MultiFolder) {
                List<Folder> folders = ((MultiFolder) folder).getFolders();
                queue.addAll(folders);
            } else if (folder instanceof Folder) {
                result.add(folder);
            } else {
                throw new RuntimeException("Unknown folder type.");
            }
        }
        return result;
    }

}
