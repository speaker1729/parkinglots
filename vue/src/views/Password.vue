<template>
    <el-card style="width: 500px;">
        <el-form label-width="120px" size="small" :model="form" :rules="rules" ref="pass">
            <el-form-item label="原密码" prop="password">
                <el-input v-model="form.password" autocomplete="off" show-password></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
            </el-form-item>
            <el-form-item label="确认新密码" prop="confirmPassword">
                <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="save">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script>
export default {
    // 组件名称
    name: "Password",
    // 组件数据
    data() {
        return {
            // 表单数据
            form: {},
            // 用户信息
            user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
            // 表单验证规则
            rules: {
                password: [
                    {required: true, message: '请输入原密码', trigger: 'blur'},
                    {min: 3, message: '长度不少于3位', trigger: 'blur'}
                ],
                newPassword: [
                    {required: true, message: '请输入新密码', trigger: 'blur'},
                    {min: 3, message: '长度不少于3位', trigger: 'blur'}
                ],
                confirmPassword: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                    {min: 3, message: '长度不少于3位', trigger: 'blur'}
                ],
            }
        }
    },
    // 在组件被创建时执行
    created() {
        // 将当前用户的用户名设置为表单数据中的用户名
        this.form.username = this.user.username
    },
    // 组件方法
    methods: {
        // 保存修改后的密码
        save() {
            // 校验整个表单的输入是否合法
            this.$refs.pass.validate((valid) => {
                if (valid) {
                    // 判断两次输入的新密码是否相同
                    if (this.form.newPassword !== this.form.confirmPassword) {
                        this.$message.error("2次输入的新密码不相同")
                        return false
                    }
                    // 发送修改密码的请求
                    this.request.post("/user/password", this.form).then(res => {
                        if (res.flag) {
                            // 修改成功，提示用户并登出
                            this.$message.success("修改成功")
                            this.$store.commit("logout")
                        } else {
                            // 修改失败，提示用户失败原因
                            this.$message.error(res.message)
                        }
                    })
                }
            })
        },
    }
}
</script>

<style>
.avatar-uploader {
    text-align: center;
    padding-bottom: 10px;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 138px;
    height: 138px;
    line-height: 138px;
    text-align: center;
}

.avatar {
    width: 138px;
    height: 138px;
    display: block;
}
</style>
