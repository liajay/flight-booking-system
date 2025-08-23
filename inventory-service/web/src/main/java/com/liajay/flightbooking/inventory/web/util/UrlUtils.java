package com.liajay.flightbooking.inventory.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * URL编码解码工具类
 */
public class UrlUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(UrlUtils.class);
    
    /**
     * URL解码
     * @param encoded 已编码的字符串
     * @return 解码后的字符串，如果解码失败返回原字符串
     */
    public static String decode(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return encoded;
        }
        
        try {
            String decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_8.name());
            logger.debug("URL解码: {} -> {}", encoded, decoded);
            return decoded;
        } catch (UnsupportedEncodingException e) {
            logger.warn("URL解码失败, 返回原字符串: {}", encoded, e);
            return encoded;
        }
    }
    
    /**
     * URL编码
     * @param raw 原始字符串
     * @return 编码后的字符串，如果编码失败返回原字符串
     */
    public static String encode(String raw) {
        if (raw == null || raw.isEmpty()) {
            return raw;
        }
        
        try {
            String encoded = URLEncoder.encode(raw, StandardCharsets.UTF_8.name());
            logger.debug("URL编码: {} -> {}", raw, encoded);
            return encoded;
        } catch (UnsupportedEncodingException e) {
            logger.warn("URL编码失败, 返回原字符串: {}", raw, e);
            return raw;
        }
    }
    
    /**
     * 检查字符串是否需要URL解码
     * 简单检查是否包含%符号
     */
    public static boolean needsDecode(String str) {
        return str != null && str.contains("%");
    }
    
    /**
     * 安全解码 - 只有当字符串看起来像URL编码时才解码
     */
    public static String safeDecodeIfNeeded(String str) {
        if (needsDecode(str)) {
            return decode(str);
        }
        return str;
    }
}
