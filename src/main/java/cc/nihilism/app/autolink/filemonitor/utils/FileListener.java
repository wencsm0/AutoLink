package cc.nihilism.app.autolink.filemonitor.utils;

import cc.nihilism.app.autolink.filemonitor.domain.FileObserve;
import cc.nihilism.app.autolink.filemonitor.service.FileObserveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileListener extends FileAlterationListenerAdaptor {
    private final FileObserveService fileObserveService = FileObserveService.getFileObserveService();

    private String getTargetPath(File file) {
        final String parentPath = FileMonitor.getPath(file.getAbsolutePath());
        FileObserve observe = fileObserveService.getByFilePath(parentPath);
        if (observe == null || StrUtil.isEmpty(observe.getFilePath()) || StrUtil.isEmpty(observe.getLinkPath())) {
            return null;
        }

        final String filePath = observe.getFilePath();
        final String linkPath = observe.getLinkPath();
        return linkPath + file.getAbsolutePath().substring(filePath.length());
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        log.info("onDirectoryCreate: {}", directory.getAbsolutePath());
        final String targetPath = getTargetPath(directory);
        if (StrUtil.isEmpty(targetPath)) {
            return;
        }

        try {
            Files.createDirectories(Path.of(targetPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDirectoryChange(File directory) {
        log.info("onDirectoryChange: {}", directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        log.info("onDirectoryDelete: {}", directory.getAbsolutePath());
        final String targetPath = getTargetPath(directory);
        if (StrUtil.isEmpty(targetPath)) {
            return;
        }

        try {
            FileUtils.deleteDirectory(new File(targetPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFileCreate(File file) {
        if (!file.canRead()) {
            return;
        }

        final String targetPath = getTargetPath(file);
        if (StrUtil.isEmpty(targetPath)) {
            return;
        }

        final String sourcePath = file.getAbsolutePath();
        try {
            Files.createLink(Path.of(targetPath), Path.of(sourcePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFileChange(File file) {
        String compressedPath = file.getAbsolutePath();
        log.info("修改：{}", compressedPath);
    }

    @Override
    public void onFileDelete(File file) {
        log.info("删除：{}", file.getAbsolutePath());
        final String targetPath = getTargetPath(file);
        if (StrUtil.isEmpty(targetPath)) {
            return;
        }

        try {
            Files.deleteIfExists(Path.of(targetPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }
}