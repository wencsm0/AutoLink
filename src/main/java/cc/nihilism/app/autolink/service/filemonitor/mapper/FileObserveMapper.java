package cc.nihilism.app.autolink.service.filemonitor.mapper;

import cc.nihilism.app.autolink.service.filemonitor.domain.FileObserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileObserveMapper extends JpaRepository<FileObserve, Long> {
    FileObserve findByFilePath(String filePath);
}
