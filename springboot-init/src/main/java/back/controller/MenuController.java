package back.controller;

import back.common.Constants;
import back.common.Result;
import back.entity.Dict;
import back.entity.Menu;
import back.mapper.DictMapper;
import back.service.IMenuService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Resource
    private IMenuService menuService;
    @Resource
    private DictMapper dictMapper;

    /**
     * 保存
     *
     * @param menu 菜单
     * @return {@code Result}
     */
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        menuService.saveOrUpdate(menu);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param id id
     * @return {@code Result}
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        menuService.removeById(id);
        return Result.ok();
    }

    /**
     * 删除批处理
     *
     * @param ids id
     * @return {@code Result}
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        menuService.removeByIds(ids);
        return Result.ok();
    }

    /**
     * 找到所有id
     *
     * @return {@code Result}
     */
    @GetMapping("/ids")
    public Result findAllIds() {
        return Result.ok(menuService.list().stream().map(Menu::getId));
    }

    /**
     * 找到所有
     *
     * @param name 名字
     * @return {@code Result}
     */
    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        return Result.ok(menuService.findMenus(name));
    }

    /**
     * 找到一个
     *
     * @param id id
     * @return {@code Result}
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.ok(menuService.getById(id));
    }

    /**
     * 找到页面
     *
     * @param name     名字
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@code Result}
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.ok(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 获取图标
     *
     * @return {@code Result}
     */
    @GetMapping("/icons")
    public Result getIcons() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.ok(dictMapper.selectList(queryWrapper));
    }
}
