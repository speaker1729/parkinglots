package back.service;

import back.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色服务类接口
 */
public interface IRoleService extends IService<Role> {
    /**
     * 设置角色菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    /**
     * 获取角色菜单
     *
     * @param roleId 角色id
     * @return 菜单id列表
     */
    List<Integer> getRoleMenu(Integer roleId);
}
