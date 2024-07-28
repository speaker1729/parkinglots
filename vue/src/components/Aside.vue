<template>
  <!-- 侧边栏菜单 -->
    <el-menu :default-openeds="opens" style="min-height: 100%; overflow-x: hidden"
             background-color="rgb(48, 65, 86)"
             text-color="#fff"
             active-text-color="#ffd04b"
             :collapse-transition="false"
             :collapse="isCollapse"
             router
    >
        <!--        &lt;!&ndash; 系统logo &ndash;&gt;-->
        <!--        <div style="height: 60px; line-height: 60px; text-align: center">-->
        <!--            <img src="src/assets/img/logo.png" alt="" style="width: 20px; position: relative; top: 5px;">-->
        <!--            &lt;!&ndash; 系统标题 &ndash;&gt;-->
        <!--            <b style="color: white; margin-left: 5px" v-show="logoTextShow">{{ $store.state.commonTitle }}</b>-->
        <!--        </div>-->
        <!-- 循环菜单 -->
        <div v-for="item in menus" :key="item.id">
            <div v-if="item.path">
                <!-- 菜单项 -->
                <el-menu-item :index="item.path">
                    <i :class="item.icon"></i>
                    <span slot="title">{{ item.name }}</span>
                </el-menu-item>
            </div>
            <div v-else>
                <!-- 子菜单 -->
                <el-submenu :index="item.id + ''">
                    <template slot="title">
                        <i :class="item.icon"></i>
                        <span slot="title">{{ item.name }}</span>
                    </template>
                    <!-- 循环子菜单项 -->
                    <div v-for="subItem in item.children" :key="subItem.id">
                        <el-menu-item :index="subItem.path">
                            <i :class="subItem.icon"></i>
                            <span slot="title">{{ subItem.name }}</span>
                        </el-menu-item>
                    </div>
                </el-submenu>
            </div>
        </div>
    </el-menu>
</template>
<script>
export default {
    // 组件名为Aside
    name: "Aside",
    // 接收父组件传递的isCollapse和logoTextShow两个属性
    props: {
        isCollapse: Boolean,
        logoTextShow: Boolean
    },
    data() {
        return {
            // 从localStorage中获取菜单数据
            menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
            // 配置默认展开的子菜单项
            opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
        }
    },
}
</script>
<style scoped>
/* 菜单项激活时的样式 */
.el-menu-item.is-active {
    background-color: rgb(38, 52, 69) !important;
}

/* 鼠标悬停在菜单项上的样式 */
.el-menu-item:hover {
    background-color: rgb(38, 52, 69) !important;
}

/* 鼠标悬停在子菜单上的样式 */
.el-submenu__title:hover {
    background-color: rgb(38, 52, 69) !important;
}

/* 解决收缩菜单文字不消失的问题 */
.el-menu--collapse span {
    visibility: hidden;
}
</style>
