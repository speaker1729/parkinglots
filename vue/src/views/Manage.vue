<template>
    <el-container style="min-height: 100vh">

        <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
            <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" style="padding-bottom: 20px"/>
        </el-aside>

        <el-container>
            <el-header style="border-bottom: 1px solid #ccc;">
                <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :user="user"/>
            </el-header>
            <el-main style="background-color: navajowhite;">
                <!-- 表示当前页面的子路由会在 <router-view /> 里面展示-->
                <router-view @refreshUser="getUser"/>
            </el-main>
        </el-container>

    </el-container>
</template>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
    name: 'Home',  // 组件的名称为"Home"
    data() {
        return {
            collapseBtnClass: 'el-icon-s-fold',  // 收缩按钮的类名，初始值为"el-icon-s-fold"
            isCollapse: false,  // 控制侧边栏是否收缩的布尔值，初始值为false
            sideWidth: 200,  // 侧边栏的宽度，初始值为200
            logoTextShow: true,  // 控制是否显示侧边栏Logo的布尔值，初始值为true
            user: {}  // 从后台获取到的User数据，初始值为空对象
        }
    },
    components: {
        Aside,
        Header
    },
    created() {  // 组件的生命周期函数，在组件创建时会调用，调用getUser()方法获取最新的User数据
        this.getUser()
    },
    methods: {
        collapse() {  // 收缩按钮的点击事件，控制侧边栏的展开和收缩，以及相关样式的变化
            this.isCollapse = !this.isCollapse  // 将侧边栏展开和收缩的布尔值取反
            if (this.isCollapse) {  // 如果侧边栏收缩
                this.sideWidth = 64  // 侧边栏宽度变为64px
                this.collapseBtnClass = 'el-icon-s-unfold'  // 收缩按钮的类名变为"el-icon-s-unfold"
                this.logoTextShow = false  // 不显示侧边栏Logo
            } else {  // 如果侧边栏展开
                this.sideWidth = 200  // 侧边栏宽度变为200px
                this.collapseBtnClass = 'el-icon-s-fold'  // 收缩按钮的类名变为"el-icon-s-fold"
                this.logoTextShow = true  // 显示侧边栏Logo
            }
        },
        getUser() {  // 获取最新的User数据，从本地存储中获取用户名，然后通过请求后台获取最新数据，并更新组件的user属性
            let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""  // 从本地存储中获取用户名
            if (username) {
                // 从后台获取User数据
                this.request.get("/user/username/" + username).then(res => {
                    // 重新赋值后台的最新User数据
                    this.user = res.data
                })
            }
        }
    }
}
</script>

