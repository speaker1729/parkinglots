package back.controller;

import back.common.Constants;
import back.common.Result;
import back.config.interceptor.AuthAccess;
import back.entity.User;
import back.entity.dto.UserDTO;
import back.entity.dto.UserPasswordDTO;
import back.service.IUserService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import back.exception.ServiceException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {
    // 通过@Resource注解自动装配userService实例
    @Resource
    private IUserService userService;

    // 登录接口
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.fail(Constants.CODE_400, "参数错误");
        }
        // 调用userService的login方法进行登录
        UserDTO dto = userService.login(userDTO);
        return Result.ok(dto);
    }

    // 注册接口
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.fail(Constants.CODE_400, "参数错误");
        }
        // 设置用户的昵称为用户名
        userDTO.setNickname(userDTO.getUsername());
        // 调用userService的register方法进行注册
        return Result.ok(userService.register(userDTO));
    }

    // 保存或更新用户信息接口
    @PostMapping
    public Result save(@RequestBody User user) {
        String username = user.getUsername();
        if (StrUtil.isBlank(username)) {
            return Result.fail(Constants.CODE_400, "参数错误");
        }
        // 如果没有设置昵称，默认设置为用户名
        if (StrUtil.isBlank(user.getNickname())) {
            user.setNickname(username);
        }
        // 如果用户已存在，则不修改密码
        if (user.getId() != null) {
            user.setPassword(null);
        } else {
            // 如果是新用户，则默认密码为"123"
            if (user.getPassword() == null) {
                user.setPassword("123");
            }
        }
        // 调用userService的saveOrUpdate方法进行保存或更新
        return Result.ok(userService.saveOrUpdate(user));
    }

    // 修改密码接口
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        // 调用userService的updatePassword方法进行密码修改
        userService.updatePassword(userPasswordDTO);
        return Result.ok();
    }

    // 重置密码接口
    @AuthAccess
    @PutMapping("/reset")
    public Result reset(@RequestBody UserPasswordDTO userPasswordDTO) {
        if (StrUtil.isBlank(userPasswordDTO.getUsername()) || StrUtil.isBlank(userPasswordDTO.getPhone())) {
            throw new ServiceException("-1", "参数异常");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userPasswordDTO.getUsername());
        queryWrapper.eq("phone", userPasswordDTO.getPhone());
        List<User> list = userService.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            User user = list.get(0);
            // 将密码重置为默认密码"123"
            user.setPassword(userService.getSafePwd("123"));
            userService.updateById(user);
        }
        return Result.ok();
    }

    // 删除用户接口
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        // 调用userService的removeById方法进行删除
        return Result.ok(userService.removeById(id));
    }

    // 批量删除用户接口
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // 调用userService的removeByIds方法批量删除用户
        return Result.ok(userService.removeByIds(ids));
    }

    // 查询所有用户接口
    @GetMapping
    public Result findAll() {
        // 调用userService的list方法查询所有用户
        return Result.ok(userService.list());
    }

    // 根据用户ID查询用户接口
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        // 调用userService的getById方法根据ID查询用户
        return Result.ok(userService.getById(id));
    }

    // 根据用户名查询用户接口
    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        // 调用userService的getOne方法根据条件查询单个用户
        return Result.ok(userService.getOne(queryWrapper));
    }

    // 分页查询用户接口
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }
        // 调用userService的page方法进行分页查询
        return Result.ok(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
