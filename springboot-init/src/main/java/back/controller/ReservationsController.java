package back.controller;

import back.common.Result;
import back.entity.ParkingLots;
import back.entity.Reservations;
import back.entity.User;
import back.entity.dto.ReservationsDTO;
import back.mapper.ParkingLotsMapper;
import back.mapper.ReservationsMapper;
import back.mapper.UserMapper;
import back.service.IReservationsService;
import back.utils.TokenUtils;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 前端控制器
 */
@RestController
@RequestMapping("/reservations")
public class ReservationsController {
    @Resource
    private IReservationsService reservationsService; // 注入预订服务类
    @Resource
    private ParkingLotsMapper parkingLotsMapper; // 注入停车场映射器
    @Resource
    private ReservationsMapper reservationsMapper; // 注入预订映射器
    @Resource
    private UserMapper userMapper; // 注入用户映射器
    private final String now = DateUtil.now(); // 当前时间

    // 新增或者更新预订信息
    @PostMapping
    public Result save(@RequestBody Reservations reservations) {
        reservationsService.saveOrUpdate(reservations); // 调用预订服务类的保存或更新方法
        return Result.ok(); // 返回保存成功信息
    }

    // 购买停车位
    @PostMapping("zuyong")
    public Result zuyong(@RequestBody Reservations reservations) {
        reservations.setStatus(2); // 将预订状态设置为已预定
        reservationsService.save(reservations); // 调用预订服务类的保存方法
        ParkingLots data = parkingLotsMapper.selectById(reservations.getParkingLotId()); // 查询停车场信息
        ParkingLots parkingLots = new ParkingLots(); // 创建一个新的停车场实例
        parkingLots.setStatus(2); // 将停车场状态设置为已购买
        parkingLots.setSum(data.getSum() + 1); // 将停车场总数加1
        parkingLots.setUserId(reservations.getUserId()); // 设置停车场所属用户ID
        parkingLots.setParkingLotId(reservations.getParkingLotId()); // 设置停车场ID
        parkingLotsMapper.updateById(parkingLots); // 更新停车场信息
        return Result.ok(); // 返回购买成功信息
    }

    /**
     * 取消预订
     */
    @GetMapping("quxiao/{id}")
    public Result quxiao(@PathVariable Integer id) {
        User user = getUser(); // 获取当前用户
        int rows = reservationsMapper.delete(new LambdaQueryWrapper<Reservations>() // 根据条件删除预订信息
                .eq(Reservations::getUserId, user.getId())
                .eq(Reservations::getParkingLotId, id));
        if (rows > 0) { // 如果删除成功
            ParkingLots parkingLots = parkingLotsMapper.selectById(id); // 查询停车场信息
            parkingLots.setStatus(1); // 将停车场状态设置为未购买
            if (parkingLots.getSum() > 0) {
                parkingLots.setSum(parkingLots.getSum() - 1); // 如果停车场总数大于0，则将总数减1
            }
            parkingLotsMapper.updateById(parkingLots); // 更新停车场信息
        }
        return Result.ok(); // 返回取消成功信息
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        reservationsService.removeById(id); // 根据ID删除预订信息
        return Result.ok(); // 返回删除成功信息
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        reservationsService.removeByIds(ids); // 根据ID列表批量删除预订信息
        return Result.ok(); // 返回删除成功信息
    }

    @GetMapping
    public Result findAll() {
        return Result.ok(reservationsService.list()); // 返回所有预订信息
    }

    // 根据停车场ID查询预订信息
    @GetMapping("findByLogId/{id}")
    public Result findByLogId(@PathVariable Integer id) {
        Reservations reservations = reservationsMapper.selectOne(new LambdaQueryWrapper<Reservations>()
                .eq(Reservations::getParkingLotId, id)
                .eq(Reservations::getStatus, 2)); // 根据停车场ID和状态查询预订信息
        User user = userMapper.selectById(reservations.getUserId()); // 查询预订所属用户信息
        ReservationsDTO dto = new ReservationsDTO(); // 创建一个新的DTO实例
        BeanUtils.copyProperties(reservations, dto); // 复制预订信息到DTO实例中
        dto.setUser(user); // 将用户信息设置到DTO实例中
        return Result.ok(dto); // 返回查询结果
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.ok(reservationsService.getById(id)); // 根据ID查询预订信息并返回
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Reservations> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("reservation_id"); // 按照预订ID倒序排序
        return Result.ok(reservationsService.page(new Page<>(pageNum, pageSize), queryWrapper)); // 分页查询预订信息并返回
    }

    // 获取当前用户
    private User getUser() {
        return TokenUtils.getCurrentUser();
    }
}
