<template>
    <div style="line-height: 60px; display: flex">
        <div style="flex: 1;">
            <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>
            <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
                <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
                <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>  <!-- 当前页面名称 -->
            </el-breadcrumb>
        </div>
        <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
            <div style="display: inline-block">
                <img :src="user.avatarUrl" alt=""
                     style="width: 40px;height: 40px; border-radius: 50%; position: relative; top: 10px; right: 5px">
                <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                    <router-link to="/password">修改密码</router-link>  <!-- 修改密码路由 -->
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                    <router-link to="/person">个人信息</router-link>  <!-- 个人信息路由 -->
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                    <div style="text-decoration: none" @click="logout">退出</div>  <!-- 退出登录 -->
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>
<script>
export default {
    name: "Header",
    props: {
        collapseBtnClass: String,  // 折叠按钮的样式类
        user: Object  // 用户信息对象
    },
    computed: {
        currentPathName() {
            return this.$store.state.currentPathName;　　//需要监听的数据，当前页面名称
        }
    },
    data() {
        return {}
    },
    methods: {
        collapse() {
            // this.$parent.$parent.$parent.$parent.collapse()  // 通过4个 $parent 找到父组件，从而调用其折叠方法
            this.$emit("asideCollapse")  // 触发父组件的自定义事件，实现折叠
        },
        logout() {
            this.$store.commit("logout")  // 调用 store 中的 logout 方法，清除登录状态
            this.$message.success("退出成功")  // 弹出退出成功的提示框
        }
    }
}
</script>
<style scoped>
</style>
