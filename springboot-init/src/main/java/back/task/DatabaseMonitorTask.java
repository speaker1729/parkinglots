package back.task;

import back.entity.ParkingLots;
import back.entity.Reservations;
import back.mapper.ParkingLotsMapper;
import back.mapper.ReservationsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 数据库监控任务类
 */
@Component
public class DatabaseMonitorTask {
    @Resource
    private ParkingLotsMapper parkingLotsMapper;
    @Resource
    private ReservationsMapper reservationsMapper;

    /**
     * 校验车位信息是否已结束
     */
    @Scheduled(fixedRate = 1000) // 每隔1秒执行一次
    public void checkAndChangeStatus() {
        // 查询所有车位信息
        List<ParkingLots> list = parkingLotsMapper.selectList(null);
        // 遍历车位信息
        list.forEach(item -> {
            Date availableTo = item.getAvailableTo(); // 获取可用时间
            Date now = new Date(); // 获取当前时间
            // 如果可用时间不为空且当前时间在可用时间之后
            if (availableTo != null && now.after(availableTo)) {
                if (item.getStatus() == 1) { // 如果车位状态为1（已预定）
                    // 时间已过期，更改状态字段为3（已结束）
                    item.setStatus(3);
                    // 执行更新操作
                    parkingLotsMapper.updateById(item);
                } else if (item.getStatus() == 2) { // 如果车位状态为2（已租用）
                    // 查询预定记录
                    Reservations reservations = reservationsMapper.selectOne(new LambdaQueryWrapper<Reservations>()
                            .eq(Reservations::getParkingLotId, item.getParkingLotId())
                            .eq(Reservations::getUserId, item.getUserId()));
                    // 如果预定记录不为空
                    if (reservations != null) {
                        reservationsMapper.deleteById(reservations.getReservationId()); // 删除预定记录
                        item.setOwnerName(null);
                        item.setOwnerPhone(null);
                        item.setOwnerId(null);
                        // 时间已过期，更改状态字段为3（已结束）
                        item.setStatus(3);
                        // 执行更新操作
                        parkingLotsMapper.updateById(item);
                    }
                }
            }
        });
    }

    /**
     * 校验预定信息是否已结束
     */
    @Scheduled(fixedRate = 1000) // 每隔1秒执行一次
    public void checkYD() {
        // 查询所有预定信息
        List<Reservations> list = reservationsMapper.selectList(null);
        // 遍历预定信息
        list.forEach(item -> {
            Date availableTo = item.getEndTime(); // 获取结束时间
            Date now = new Date(); // 获取当前时间
            // 如果结束时间不为空且当前时间在结束时间之后
            if (availableTo != null && now.after(availableTo)) {
                ParkingLots parkingLots = parkingLotsMapper.selectById(item.getParkingLotId()); // 查询车位信息
                parkingLots.setOwnerName(null);
                parkingLots.setOwnerPhone(null);
                parkingLots.setOwnerId(null);
                parkingLots.setStatus(1); // 更改车位状态为1（空闲）
                parkingLotsMapper.updateById(parkingLots); // 执行更新操作
                // 时间已过期，更改状态字段为3（已结束）
                item.setStatus(3);
                // 执行更新操作
                reservationsMapper.updateById(item);
            }
        });
    }
}
