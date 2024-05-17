package cc.nihilism.app.autolink.service.filemonitor.utils;

import cc.nihilism.app.autolink.basic.exception.ServiceException;
import cc.nihilism.app.autolink.basic.utils.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FormatFileNameUtil {

    public static List<String> rename(String targetPath, String fileName, int year, int season) throws IOException {
        return rename(targetPath, fileName, year, season, "", "");
    }

    public static List<String> rename(String targetPath, String fileName, int year, int season, String prefix, String suffix) throws IOException {
        File originDir = new File(targetPath);
        if (!originDir.exists() || !originDir.isDirectory()) {
            throw new ServiceException("目标文件夹不存在");
        }

        File directory = new File(originDir.getParent() + "/" + fileName + "." + year + ".S" + String.format("%02d", season));
        FileUtils.moveDirectory(originDir, directory);

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new ServiceException("目标文件夹为空");
        }

        Arrays.sort(files, (o1, o2) -> {
            String o1Suffix = FilenameUtils.getExtension(o1.getPath());
            String o2Suffix = FilenameUtils.getExtension(o2.getPath());
            int suffixCompare = o1Suffix.compareTo(o2Suffix);
            if (0 == suffixCompare) {
                return o1.getName().compareTo(o2.getName());
            }

            return suffixCompare;
        });

        List<String> res = new ArrayList<>();
        Map<String, List<File>> map = Arrays.stream(files).filter(File::isFile).collect(Collectors.groupingBy(FormatFileNameUtil::getFileExtension));
        map.forEach((k, v) -> {
            int count = 1;
            for (File file : v) {
                String extName = getFileExtension(file);

                // 转换文件名
                String targetName = format(fileName, year, season, count, prefix, suffix);
                count++;

                // 输出结果
                targetName = "/" + FilenameUtils.getPath(file.getAbsolutePath()) + targetName + "." + extName;
                res.add(targetName);

                // 重命名文件
                try {
                    File target = new File(targetName);
                    if (!target.exists()) {
                        FileUtils.moveFile(file, new File(targetName));
                    }
                } catch (IOException e) {
                    throw new ServiceException("重命名文件失败");
                }
            }
        });

        return res;
    }

    /**
     * 指定文件名的重命名
     */
    private static String format(String fileName, int year, int season, int episode, String prefix, String suffix) {
        String seasonStr = "S" + String.format("%02d", season);
        String episodeStr = "E" + String.format("%02d", episode);

        String format = fileName + "." + year + "." + seasonStr + "." + episodeStr;

        if (StrUtil.isNotEmpty(prefix)) {
            format = prefix + format;
        }

        if (StrUtil.isNotEmpty(suffix)) {
            format = format + suffix;
        }

        return format;
    }

    public static List<Map<String, String>> preview(String targetPath, String fileName, int year, int season, String prefix, String suffix) {
        File directory = new File(targetPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new ServiceException("目标文件夹不存在");
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new ServiceException("目标文件夹为空");
        }

        List<Map<String, String>> res = new ArrayList<>();
        Map<String, String> moveDir = new HashMap<>();
        moveDir.put("target", directory.getParent() + "/" + fileName);
        moveDir.put("origin", targetPath);
        res.add(moveDir);

        Arrays.sort(files, (o1, o2) -> {
            String o1Suffix = FilenameUtils.getExtension(o1.getPath());
            String o2Suffix = FilenameUtils.getExtension(o2.getPath());
            int suffixCompare = o1Suffix.compareTo(o2Suffix);
            if (0 == suffixCompare) {
                return o1.getName().compareTo(o2.getName());
            }

            return suffixCompare;
        });

        Map<String, List<File>> map = Arrays.stream(files).filter(File::isFile).collect(Collectors.groupingBy(FormatFileNameUtil::getFileExtension));
        map.forEach((k, v) -> {
            int count = 1;
            for (File file : v) {
                String extName = getFileExtension(file);

                // 转换文件名
                String targetName = format(fileName, year, season, count, prefix, suffix);
                count++;

                // 输出结果
                targetName = "/" + FilenameUtils.getPath(file.getAbsolutePath()) + targetName + "." + extName;

                Map<String, String> moveFile = new HashMap<>();
                moveFile.put("target", targetName);
                moveFile.put("origin", file.getName());
                res.add(moveFile);
            }
        });

        return res;
    }

    public static String getFileExtension(File file) {
        String extension = FilenameUtils.getExtension(file.getPath());
        if ("ass".equals(extension)) {
            String name = file.getName();
            name = name.substring(0, name.lastIndexOf("."));
            extension = name.substring(name.lastIndexOf(".") + 1) + ".ass";
        }

        return extension;
    }

    public static void main(String[] args) {
        String name = "a.b.c.d.ass";
        File file = new File(name);
        System.out.println(getFileExtension(file));
        file.delete();
    }
}
