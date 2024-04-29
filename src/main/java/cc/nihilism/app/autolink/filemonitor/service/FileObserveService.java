package cc.nihilism.app.autolink.filemonitor.service;

import cc.nihilism.app.autolink.filemonitor.domain.FileObserve;
import cc.nihilism.app.autolink.filemonitor.mapper.FileObserveMapper;
import cc.nihilism.app.autolink.filemonitor.utils.FileMonitor;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FileObserveService {
    @Getter
    private static FileObserveService fileObserveService;
    private final FileObserveMapper fileObserveMapper;

    public FileObserveService(FileObserveMapper fileObserveMapper) {
        this.fileObserveMapper = fileObserveMapper;
    }

    @PostConstruct
    public void init() {
        try {
            fileObserveService = this;

            List<FileObserve> list = list();
            list.forEach(e -> FileMonitor.add(e.getFilePath()));
            FileMonitor.start();
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    public List<FileObserve> list() {
        return fileObserveMapper.findAll();
    }

    public void save(FileObserve fileObserve) {
        fileObserve.setUpdateTime(new Date());
        fileObserve.setCreateTime(new Date());
        fileObserve.setStatus("1");

        fileObserveMapper.save(fileObserve);
        FileMonitor.add(fileObserve.getFilePath());
    }

    public void delete(FileObserve fileObserve) {
        fileObserveMapper.delete(fileObserve);
        FileMonitor.remove(fileObserve.getFilePath());
    }

    public void update(FileObserve fileObserve) {
        fileObserve.setUpdateTime(new Date());

        delete(fileObserve);
        save(fileObserve);
    }

    public FileObserve getByFilePath(String filePath) {
        return fileObserveMapper.findByFilePath(filePath);
    }
}
