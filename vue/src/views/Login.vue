<template>
    <div class="wrapper">
        <div style="height: 60px; line-height: 60px; font-size: 20px; padding-left: 50px; color: white;background-color: rgba(0,0,0,0.2)">
            {{ $store.state.commonTitle }}
        </div>
        <div style="margin: 150px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
            <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
            <el-form :model="user" :rules="rules" ref="userForm">
                <el-form-item prop="username">
                    <el-input size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input size="medium" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
                </el-form-item>
                <el-form-item style="margin: 10px 0; text-align: right">
                    <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">
                        前往注册
                    </el-button>
                    <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
                </el-form-item>
                <el-form-item style="margin: 10px 0; text-align: right">
                    <el-button type="text" size="mid" autocomplete="off" @click="handlePass">找回密码</el-button>
                </el-form-item>
            </el-form>
        </div>
        <el-dialog title="找回密码" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="100px" size="small">
                <el-form-item label="用户名">
                    <el-input v-model="pass.username" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input v-model="pass.phone" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="passwordBack">重置密码</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {resetRouter, setRoutes} from "@/router";

export default {
    name: "Login", // 组件名称
    data() {
        return {
            user: {}, // 用户名
            pass: {}, // 密码
            dialogFormVisible: false, // 是否显示重置密码的对话框
            rules: { // 表单校验规则
                username: [
                    {required: true, message: '请输入用户名', trigger: 'blur'}, // 用户名不能为空
                    {min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur'} // 用户名长度在3到10个字符之间
                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'}, // 密码不能为空
                    {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'} // 密码长度在1到20个字符之间
                ],
            }
        }
    },
    mounted() {
        // 重置路由
        resetRouter()
    },
    methods: {
        login() { // 登录方法
            this.$refs['userForm'].validate((valid) => { // 校验表单
                if (valid) {  // 表单校验合法
                    this.request.post("/user/login", this.user).then(res => { // 发送登录请求
                        if (res.flag) { // 登录成功
                            localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
                            localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器
                            // 动态设置当前用户的路由
                            setRoutes()
                            // 跳转到首页
                            this.$router.push("/")
                            // 弹出登录成功的提示信息
                            this.$message.success("登录成功")
                        } else { // 登录失败
                            // 弹出登录失败的提示信息
                            this.$message.error(res.message)
                        }
                    })
                }
            });
        },
        handlePass() { // 处理重置密码的方法
            this.dialogFormVisible = true // 显示重置密码的对话框
            this.pass = {} // 清空密码
        },
        passwordBack() { // 重置密码的方法
            this.request.put("/user/reset", this.pass).then(res => { // 发送重置密码的请求
                if (res.flag) { // 重置成功
                    // 弹出重置成功的提示信息，并关闭对话框
                    this.$message.success("重置密码成功，新密码为：123，请尽快修改密码")
                    this.dialogFormVisible = false
                } else { // 重置失败
                    // 弹出重置失败的提示信息
                    this.$message.error(res.message)
                }
            })
        }
    }
}
</script>

<style>
.wrapper {
    height: 100vh;
    background: url("../assets/img/bg.jpg") center center;
    background-size: 100% 100%;
    overflow: hidden;
}
</style>
