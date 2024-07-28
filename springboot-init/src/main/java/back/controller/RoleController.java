package back.controller;

import back.common.Result;
import back.entity.Role;
import back.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private IRoleService roleService;

    /**
     * 保存角色
     *
     * @param role 角色实体对象
     * @return {@code Result} 返回操作结果
     */
    @PostMapping
    public Result save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return Result.ok();
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     * @return {@code Result} 返回操作结果
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色id列表
     * @return {@code Result} 返回操作结果
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return Result.ok();
    }

    /**
     * 查找所有角色
     *
     * @return {@code Result} 返回操作结果及角色列表
     */
    @GetMapping
    public Result findAll() {
        return Result.ok(roleService.list());
    }

    /**
     * 查找单个角色
     *
     * @param id 角色id
     * @return {@code Result} 返回操作结果及角色实体对象
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.ok(roleService.getById(id));
    }

    /**
     * 分页查找角色
     *
     * @param name     角色名称
     * @param pageNum  当前页码
     * @param pageSize 每页大小
     * @return {@code Result} 返回操作结果及分页数据
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);  // 使用like模糊查询角色名称
        queryWrapper.orderByDesc("id");   // 按照id倒序排列
        return Result.ok(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 绑定角色和菜单的关系
     *
     * @param roleId  角色id
     * @param menuIds 菜单id数组
     * @return {@code Result} 返回操作结果
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.ok();
    }

    /**
     * 获取角色菜单
     *
     * @param roleId 角色id
     * @return {@code Result} 返回操作结果及菜单id列表
     */
    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return Result.ok(roleService.getRoleMenu(roleId));
    }
}
