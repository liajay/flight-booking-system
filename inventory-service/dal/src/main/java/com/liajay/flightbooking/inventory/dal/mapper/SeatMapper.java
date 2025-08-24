package com.liajay.flightbooking.inventory.dal.mapper;

import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.dal.dataobject.SeatClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 座位MyBatis Mapper接口
 * 
 * @author liajay
 */
@Mapper
public interface SeatMapper {

    /**
     * 根据航班ID和座位号查询座位
     */
    Seat findByFlightNumberAndSeatNumber(@Param("flightNumber") String flightNumber, 
                                         @Param("seatNumber") String seatNumber);

    /**
     * 多条件动态查询
     */
    List<Seat> findByConditions(@Param("flightNumber") String flightNumber,
                                @Param("seatClass") SeatClass seatClass,
                                @Param("isAvailable") Boolean isAvailable,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                @Param("seatNumberStart") String seatNumberStart,
                                @Param("seatNumberEnd") String seatNumberEnd,
                                @Param("offset") int offset,
                                @Param("pageSize") int pageSize);

    /**
     * 根据条件统计座位总数
     */
    long countByConditions(@Param("flightNumber") String flightNumber,
                          @Param("seatClass") SeatClass seatClass,
                          @Param("isAvailable") Boolean isAvailable,
                          @Param("minPrice") BigDecimal minPrice,
                          @Param("maxPrice") BigDecimal maxPrice,
                          @Param("seatNumberStart") String seatNumberStart,
                          @Param("seatNumberEnd") String seatNumberEnd);

    /**
     * 查找指定航班的第一个可用座位
     */
    Seat findFirstAvailableSeat(@Param("flightNumber") String flightNumber);

    /**
     * 更新座位可用状态
     */
    int updateSeatAvailability(@Param("id") Long id, @Param("isAvailable") Boolean isAvailable);


}
