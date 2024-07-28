<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                      v-model="username"></el-input>
            <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5"
                      v-model="email"></el-input>
            <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5"
                      v-model="address"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-popconfirm
                    class="ml-5"
                    confirm-button-text='确定'
                    cancel-button-text='取消'
                    icon="el-icon-info"
                    icon-color="red"
                    title="您确定批量删除这些数据吗？"
                    @confirm="delBatch"
            >
                <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
            </el-popconfirm>
        </div>
        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="username" label="用户名" width="140"></el-table-column>
            <el-table-column label="角色">
                <template v-slot="scope">
                    <span v-if="scope.row.role">{{
                        roles.find(v => v.flag === scope.row.role) ? roles.find(v => v.flag === scope.row.role).name : ''
                        }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
            <el-table-column prop="phone" label="电话"></el-table-column>
            <el-table-column prop="address" label="地址"></el-table-column>
            <el-table-column label="操作" width="200" align="center">
                <template v-slot:default="scope">
                    <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i>
                    </el-button>
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='取消'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定删除吗？"
                            @confirm="del(scope.row.id)"
                    >
                        <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i>
                        </el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-sizes="[2, 5, 10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
        <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" size="small">
                <el-form-item label="用户名">
                    <el-input v-model="form.username" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="角色">
                    <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
                        <el-option v-for="item in roles" :key="item.name" :label="item.name"
                                   :value="item.flag"></el-option>
                    </el-select>
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
                    <el-input v-model="form.address" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "User", // 组件名称
    data() { // 数据
        return {
            tableData: [], // 表格数据
            total: 0, // 数据总数
            pageNum: 1, // 当前页码
            pageSize: 10, // 每页数据数
            username: "", // 用户名
            email: "", // 邮箱
            address: "", // 地址
            form: {}, // 表单数据
            dialogFormVisible: false, // 是否显示表单对话框
            multipleSelection: [], // 多选数据
            roles: [] // 角色数据
        }
    },
    created() { // 组件创建时调用的方法
        this.load() // 加载数据
    },
    methods: { // 组件方法
        load() { // 加载数据方法
            this.request.get("/user/page", { // 请求用户数据
                params: {
                    pageNum: this.pageNum, // 当前页码
                    pageSize: this.pageSize, // 每页数据数
                    username: this.username, // 用户名
                    email: this.email, // 邮箱
                    address: this.address, // 地址
                }
            }).then(res => { // 请求成功回调函数
                this.tableData = res.data.records // 设置表格数据
                this.total = res.data.total // 设置数据总数
            })
            this.request.get("/role").then(res => { // 请求角色数据
                this.roles = res.data // 设置角色数据
            })
        },
        save() { // 保存数据方法
            this.request.post("/user", this.form).then(res => { // 发送保存请求
                if (res.flag) { // 保存成功
                    this.$message.success("保存成功") // 显示提示消息
                    this.dialogFormVisible = false // 关闭表单对话框
                    this.load() // 重新加载数据
                } else { // 保存失败
                    this.$message.error("保存失败") // 显示提示消息
                }
            })
        },
        handleAdd() { // 处理添加事件
            this.dialogFormVisible = true // 显示表单对话框
            this.form = {} // 清空表单数据
        },
        handleEdit(row) { // 处理编辑事件
            this.form = JSON.parse(JSON.stringify(row)) // 复制行数据到表单数据中
            this.dialogFormVisible = true // 显示表单对话框
        },
        del(id) { // 删除数据方法
            this.request.delete("/user/" + id).then(res => { // 发送删除请求
                if (res.flag) { // 删除成功
                    this.$message.success("删除成功") // 显示提示消息
                    this.load() // 重新加载数据
                } else { // 删除失败
                    this.$message.error("删除失败") // 显示提示消息
                }
            })
        },
        handleSelectionChange(val) { // 处理多选事件
            console.log(val)
            this.multipleSelection = val // 设置多选数据
        },
        delBatch() { // 批量删除数据方法
            let ids = this.multipleSelection.map(v => v.id) // 获取多选数据的id数组
            this.request.post("/user/del/batch", ids).then(res => { // 发送批量删除请求
                if (res.flag) { // 删除成功
                    this.$message.success("批量删除成功") // 显示提示消息
                    this.load() // 重新加载数据
                } else { // 删除失败
                    this.$message.error("批量删除失败") // 显示提示消息
                }
            })
        },
        reset() { // 重置查询条件方法
            this.username = "" // 重置用户名
            this.email = "" // 重置邮箱
            this.address = "" // 重置地址
            this.load() // 重新加载数据
        },
        handleSizeChange(pageSize) { // 处理每页数据数改变事件
            console.log(pageSize)
            this.pageSize = pageSize // 设置每页数据数
            this.load() // 重新加载数据
        },
        handleCurrentChange(pageNum) { // 处理页码改变事件
            console.log(pageNum)
            this.pageNum = pageNum // 设置当前页码
            this.load() // 重新加载数据
        }
    }
}
</script>

<style>
.headerBg {
    background: #eee !important;
}
</style>
