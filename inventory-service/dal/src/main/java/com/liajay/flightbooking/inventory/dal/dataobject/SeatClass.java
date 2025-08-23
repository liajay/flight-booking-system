package com.liajay.flightbooking.inventory.dal.dataobject;

/**
 * 座位舱位等级枚举
 */
public enum SeatClass {
    ECONOMY("经济舱"),
    BUSINESS("商务舱"),
    FIRST("头等舱");

    private final String description;

    SeatClass(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
