package cc.nihilism.app.autolink.filemonitor.mapper;

import cc.nihilism.app.autolink.filemonitor.domain.FileObserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileObserveMapper extends JpaRepository<FileObserve, Long> {
    FileObserve findByFilePath(String filePath);
}
