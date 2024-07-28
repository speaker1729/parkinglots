package back.controller;

import back.common.Result;
import back.entity.ParkingLots;
import back.entity.User;
import back.entity.dto.SearchDTO;
import back.mapper.UserMapper;
import back.service.IParkingLotsService;
import back.utils.TokenUtils;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 停车场控制器类
 */
@RestController
@RequestMapping("/parkingLots")
public class ParkingLotsController {
    @Resource
    private IParkingLotsService parkingLotsService;
    @Resource
    private UserMapper userMapper;
    // 获取当前时间
    private final String now = DateUtil.now();

    // 新增或者更新停车场信息
    @PostMapping
    public Result save(@RequestBody ParkingLots parkingLots) {
        Integer ownerId = parkingLots.getOwnerId();
        // 根据 ownerId 获取用户信息
        User user = userMapper.selectById(ownerId);
        parkingLots.setOwnerName(user.getNickname());
        parkingLots.setOwnerPhone(user.getPhone());
        // 保存或者更新停车场信息
        parkingLotsService.saveOrUpdate(parkingLots);
        return Result.ok();
    }

    // 删除指定 id 的停车场信息
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        parkingLotsService.removeById(id);
        return Result.ok();
    }

    // 批量删除停车场信息
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        parkingLotsService.removeByIds(ids);
        return Result.ok();
    }

    // 获取所有停车场信息
    @GetMapping
    public Result findAll() {
        return Result.ok(parkingLotsService.list());
    }

    // 获取指定 id 的停车场信息
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.ok(parkingLotsService.getById(id));
    }

    // 分页查询停车场信息
    @PostMapping("/page")
    public Result findPage(@RequestBody SearchDTO dto) {
        // 创建查询条件
        QueryWrapper<ParkingLots> qw = new QueryWrapper<>();
        qw.orderByDesc("parking_lot_id");
        // 地区筛选
        if (StringUtils.isNotBlank(dto.getName())) {
            qw.like("location", dto.getName());
        }
        // 状态筛选
        if (dto.getStatus() != null) {
            qw.eq("status", dto.getStatus());
        }
        // 价格筛选
        if (dto.getBeginPrice() != null && dto.getEndPrice() != null) {
            qw.apply("price >= {0} AND price <= {1}", dto.getBeginPrice(), dto.getEndPrice());
        }
        // 日期筛选
        if (dto.getBeginTime() != null && dto.getEndTime() != null) {
            qw.between("available_from", dto.getBeginTime(), dto.getEndTime())
                    .and(wrapper -> wrapper.between("available_to", dto.getBeginTime(), dto.getEndTime()));
        }
        // 分页查询停车场信息
        Page<ParkingLots> page = parkingLotsService.page(new Page<>(dto.getPageNum(), dto.getPageSize()), qw);
        List<ParkingLots> records = page.getRecords();
        // 遍历查询结果，获取用户信息
        records.forEach(item -> {
            User user = userMapper.selectById(item.getOwnerId());
            item.setOwnerName(user.getNickname());
            item.setOwnerPhone(user.getPhone());
        });
        // 根据用户手机号过滤查询结果
        if (StringUtils.isNotBlank(dto.getUserPhone())) {
            records = records.stream().filter(item -> item.getOwnerPhone().contains(dto.getUserPhone())).collect(Collectors.toList());
        }
        page.setRecords(records);
        return Result.ok(page);
    }

    // 获取当前用户信息
    private User getUser() {
        return TokenUtils.getCurrentUser();
    }
}
