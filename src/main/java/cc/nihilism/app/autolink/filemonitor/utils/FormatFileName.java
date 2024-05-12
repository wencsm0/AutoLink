package cc.nihilism.app.autolink.filemonitor.utils;

import cc.nihilism.app.autolink.basic.utils.StrUtil;

public class FormatFileName {
    /**
     * 重命名形如[xxx] name xxx [01][xxx]格式的文件名
     *
     * @param fileName 文件名
     * @param year     剧集年份
     * @param season   季数
     * @param episode  集数
     * @return [xxx]name.xxx.year.season.episode[xxx]
     */
    public static String format1(String fileName, int year, int season, int episode) {
        final String seasonStr = "S" + String.format("%02d", season);
        final String episodeStr = "E" + String.format("%02d", episode);

        // 替换[01]格式的剧集命名为' <year> E<season>S<index> '
        fileName = fileName.replaceAll("\\[\\d+]", " " + year + " " + seasonStr + episodeStr + " ");

        // 全角转半角
        fileName = StrUtil.toDBC(fileName);

        // 去除多余空格
        fileName = fileName.replaceAll(" +", " ");
        fileName = fileName.replaceAll("(?<![a-zA-Z0-9]) | (?![a-zA-Z0-9])", "");
        fileName = StrUtil.trim(fileName);

        // 空格转'.'
        fileName = fileName.replaceAll("(?<!]) (?!\\[)", ".");
        return fileName;
    }

    public static void main(String[] args) {
        String targetName = "[UHA-WINGS&VCB-Studio] Kaguya-sama wa Kokurasetai꞉ Tensai-tachi no Renai Zunousen [01][Ma10p_1080p][x265_flac]";

        // 输出结果
        // [UHA-WINGS&VCB-Studio]Kaguya-sama.wa.Kokurasetai꞉Tensai-tachi.no.Renai.Zunousen.2019.E01S01[Ma10p_1080p][x265_flac]
        System.out.println(format1(targetName, 2019, 1, 1));
    }
}
