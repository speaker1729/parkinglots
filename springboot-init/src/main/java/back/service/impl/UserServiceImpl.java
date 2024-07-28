package back.service.impl;

import back.common.Constants;
import back.common.RoleEnum;
import back.entity.Menu;
import back.entity.User;
import back.entity.dto.UserDTO;
import back.entity.dto.UserPasswordDTO;
import back.mapper.RoleMapper;
import back.mapper.RoleMenuMapper;
import back.mapper.UserMapper;
import back.service.IMenuService;
import back.service.IUserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import back.exception.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import back.utils.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Log LOG = Log.get();
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "high";

    /**
     * 登录
     *
     * @param userDTO 用户dto
     * @return {@code UserDTO}
     */
    @Override
    public UserDTO login(UserDTO userDTO) {
        // 获取用户信息
        User one = getUserInfo(userDTO);
        if (one != null) {
            // 将用户信息拷贝到传入的DTO对象中
            BeanUtil.copyProperties(one, userDTO, true);
            // 设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);
            // 获取用户角色
            String role = one.getRole();
            // 设置用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    /**
     * 注册
     *
     * @param userDTO 用户dto
     * @return {@code User}
     */
    @Override
    public User register(UserDTO userDTO) {
        // 查询是否已经有该用户
        User one = getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, userDTO.getUsername()));
        if (one == null) {
            // 如果用户不存在，将DTO转换成Entity对象，设置默认角色，进行保存
            one = new User();
            //密码加密
            String sqlPwd = getSafePwd(userDTO.getPassword());
            userDTO.setPassword(sqlPwd);
            BeanUtil.copyProperties(userDTO, one, true);
            // 默认一个普通用户的角色
            one.setRole(RoleEnum.ROLE_USER.toString());
            save(one);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    /**
     * 更新密码
     *
     * @param userPasswordDTO 用户密码dto
     */
    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        // 密码加密
        userPasswordDTO.setPassword(getSafePwd(userPasswordDTO.getPassword()));
        userPasswordDTO.setNewPassword(getSafePwd(userPasswordDTO.getNewPassword()));
        // 更新密码
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    /**
     * 得到安全pwd
     *
     * @param pwd 明文密码
     * @return {@code String}
     */
    @Override
    public String getSafePwd(String pwd) {
        // 对密码进行加盐加密
        return DigestUtils.md5DigestAsHex((SALT + pwd).getBytes());
    }

    /**
     * 获取用户信息
     *
     * @param userDTO 用户dto
     * @return {@code User}
     */
    private User getUserInfo(UserDTO userDTO) {
        // 构造查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        String sqlPwd = getSafePwd(userDTO.getPassword());
        queryWrapper.eq("password", sqlPwd);
        User one;
        try {
            // 查询用户信息
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     *
     * @param roleFlag 角色标志
     * @return {@code List<Menu>}
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        // 获取角色ID
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 获取当前角色的所有菜单ID集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf() 移除 children 里面不在 menuIds 集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
