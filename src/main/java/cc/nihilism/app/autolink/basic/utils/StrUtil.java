package cc.nihilism.app.autolink.basic.utils;

import org.dromara.hutool.core.convert.Convert;

public class StrUtil extends org.dromara.hutool.core.text.StrUtil {
    public static String toDBC(String str) {
        return Convert.toDBC(str);
    }
}
