package cc.nihilism.app.autolink.filemonitor.utils;

import cc.nihilism.app.autolink.basic.utils.TrieNode;
import cc.nihilism.app.autolink.basic.utils.TrieTree;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileMonitor {
    private static final long INTERVAL = 1000;
    private static final FileAlterationMonitor MONITOR = new FileAlterationMonitor(INTERVAL);
    private static final TrieTree<FileAlterationObserver> OBSERVER_MAP = new TrieTree<>();


    /**
     * 给文件添加监听
     *
     * @param path 文件路径
     */
    public static void add(String path) {
        if (OBSERVER_MAP.isSubPath(path)) {
            return;
        }

        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        MONITOR.addObserver(observer);
        observer.addListener(new FileListener());

        OBSERVER_MAP.addPath(path, observer);
    }

    public static void remove(String path) {
        TrieNode<FileAlterationObserver> observer = OBSERVER_MAP.getNode(path);
        if (observer != null) {
            MONITOR.removeObserver(observer.getData());
        }
    }

    public static void stop() throws Exception {
        MONITOR.stop();
    }

    public static void start() throws Exception {
        MONITOR.start();
    }

    public static String getPath(String path) {
        return OBSERVER_MAP.getPath(path);
    }
}
