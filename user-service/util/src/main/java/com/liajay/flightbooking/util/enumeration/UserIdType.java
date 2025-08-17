package com.liajay.flightbooking.util.enumeration;

/**
 * 用户ID生成类型枚举
 * 
 * @author liajay
 * @since 2025-08-16
 */
public enum UserIdType {
    
    /**
     * UUID类型（32位无连字符）
     */
    UUID("uuid", "UUID类型（32位无连字符）"),
    
    /**
     * UUID类型（36位含连字符）
     */
    UUID_WITH_HYPHENS("uuid_with_hyphens", "UUID类型（36位含连字符）"),
    
    /**
     * 雪花算法类型（19位数字）
     */
    SNOWFLAKE("snowflake", "雪花算法类型（19位数字）"),
    
    /**
     * 时间戳类型（20位：14位时间戳+6位随机数）
     */
    TIMESTAMP("timestamp", "时间戳类型（20位：14位时间戳+6位随机数）"),
    
    /**
     * 短ID类型（可指定长度的字母数字组合）
     */
    SHORT("short", "短ID类型（可指定长度的字母数字组合）"),
    
    /**
     * 数字类型（基于雪花算法的long类型）
     */
    NUMERIC("numeric", "数字类型（基于雪花算法的long类型）");

    private final String code;
    private final String description;

    UserIdType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举值
     * 
     * @param code 代码
     * @return 枚举值
     * @throws IllegalArgumentException 如果代码不存在
     */
    public static UserIdType fromCode(String code) {
        for (UserIdType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown user id type code: " + code);
    }

    /**
     * 检查代码是否有效
     * 
     * @param code 代码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        try {
            fromCode(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
