package com.liajay.flightbooking.inventory.dal.dataobject;

/**
 * 航班状态枚举
 */
public enum FlightStatus {
    SCHEDULED("已安排"),
    DELAYED("延误"),
    CANCELLED("取消"),
    DEPARTED("已起飞"),
    ARRIVED("已到达");

    private final String description;

    FlightStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
