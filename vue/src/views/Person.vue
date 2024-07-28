<template>
    <el-card style="width: 500px;">
        <el-form label-width="80px" size="small">
            <el-upload
                    class="avatar-uploader"
                    action="http://localhost:9090/file/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
            >
                <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <el-form-item label="用户名">
                <el-input v-model="form.username" disabled autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
                <el-input v-model="form.nickname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
                <el-input v-model="form.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电话">
                <el-input v-model="form.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="地址">
                <el-input type="textarea" v-model="form.address" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="save">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script>
export default {
    name: "Person", // 组件名称
    data() {
        return {
            form: {}, // 表单数据对象
            user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {} // 从localStorage中获取用户信息
        }
    },
    created() {
        this.getUser().then(res => { // 在组件创建之后调用getUser方法，将获取到的用户信息赋值给form
            console.log(res) // 输出获取到的用户信息
            this.form = res // 将获取到的用户信息赋值给form
        })
    },
    methods: {
        async getUser() { // 使用async/await关键字定义异步方法getUser
            return (await this.request.get("/user/username/" + this.user.username)).data // 发送请求，获取当前用户的信息
        },
        save() { // 保存表单数据
            this.request.post("/user", this.form).then(res => { // 发送请求，将表单数据保存到后端
                if (res.flag) { // 如果保存成功
                    this.$message.success("保存成功") // 使用element-ui的message组件提示保存成功
                    // 触发父级更新User的方法
                    this.$emit("refreshUser") // 触发父组件的refreshUser方法，更新用户信息
                    // 更新浏览器存储的用户信息
                    this.getUser().then(res => { // 获取最新的用户信息
                        res.token = JSON.parse(localStorage.getItem("user")).token // 将localStorage中的token赋值给获取到的用户信息
                        localStorage.setItem("user", JSON.stringify(res)) // 将最新的用户信息存储到localStorage中
                    })
                } else { // 如果保存失败
                    this.$message.error("保存失败") // 使用element-ui的message组件提示保存失败
                }
            })
        },
        handleAvatarSuccess(res) { // 上传头像成功回调
            this.form.avatarUrl = res // 将上传成功后的头像地址赋值给表单数据中的avatarUrl字段
        }
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
