package back.service;

import back.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单服务类接口
 */
public interface IMenuService extends IService<Menu> {
    /**
     * 根据菜单名称查找菜单
     *
     * @param name 菜单名称
     * @return 菜单列表
     */
    List<Menu> findMenus(String name);
}
