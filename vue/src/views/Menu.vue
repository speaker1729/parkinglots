<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                      v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd(null)">新增 <i class="el-icon-circle-plus-outline"></i>
            </el-button>
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
                  row-key="id" default-expand-all @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column prop="path" label="路径"></el-table-column>
            <el-table-column prop="pagePath" label="页面路径"></el-table-column>
            <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
                <template v-slot:default="scope">
                    <span :class="scope.row.icon"/>
                </template>
            </el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="sortNum" label="顺序"></el-table-column>
            <el-table-column label="操作" width="300" align="center">
                <template v-slot:default="scope">
                    <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">
                        新增子菜单 <i class="el-icon-plus"></i></el-button>
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
        <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" size="small">
                <el-form-item label="名称">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="路径">
                    <el-input v-model="form.path" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="页面路径">
                    <el-input v-model="form.pagePath" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="图标">
                    <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
                        <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
                            <i :class="item.value"/> {{ item.name }}
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="顺序">
                    <el-input v-model="form.sortNum" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input v-model="form.description" autocomplete="off"></el-input>
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
    name: "Menu",  // 组件名称
    data() {  // 组件数据
        return {
            tableData: [],  // 表格数据
            total: 0,  // 数据总数
            pageNum: 1,  // 当前页码
            pageSize: 10,  // 每页显示的数据条数
            name: "",  // 查询的名称
            form: {},  // 表单数据
            dialogFormVisible: false,  // 是否显示对话框
            multipleSelection: [],  // 多选框选中的数据
            options: []  // 下拉框选项
        }
    },
    created() {  // 组件创建时调用的方法
        this.load()  // 加载数据
    },
    methods: {  // 组件方法
        load() {  // 加载数据方法
            this.request.get("/menu", {  // 发送GET请求
                params: {  // 请求参数
                    name: this.name,  // 查询名称
                }
            }).then(res => {  // 成功回调函数
                this.tableData = res.data  // 将返回的数据赋值给表格数据
            })
            // 请求图标的数据
            this.request.get("/menu/icons").then(res => {  // 发送GET请求
                this.options = res.data  // 将返回的数据赋值给下拉框选项
            })
        },
        save() {  // 保存数据方法
            this.request.post("/menu", this.form).then(res => {  // 发送POST请求
                if (res.flag) {  // 如果返回结果为成功
                    this.$message.success("保存成功")  // 提示保存成功
                    this.dialogFormVisible = false  // 隐藏对话框
                    this.load()  // 重新加载数据
                } else {  // 如果返回结果为失败
                    this.$message.error("保存失败")  // 提示保存失败
                }
            })
        },
        handleAdd(pid) {  // 添加数据方法
            this.dialogFormVisible = true  // 显示对话框
            this.form = {}  // 清空表单数据
            if (pid) {  // 如果pid有值
                this.form.pid = pid  // 将pid赋值给表单数据
            }
        },
        handleEdit(row) {  // 编辑数据方法
            this.form = JSON.parse(JSON.stringify(row))  // 深拷贝表格行数据到表单数据
            this.dialogFormVisible = true  // 显示对话框
        },
        del(id) {  // 删除数据方法
            this.request.delete("/menu/" + id).then(res => {  // 发送DELETE请求
                if (res.flag) {  // 如果返回结果为成功
                    this.$message.success("删除成功")  // 提示删除成功
                    this.load()  // 重新加载数据
                } else {  // 如果返回结果为失败
                    this.$message.error("删除失败")  // 提示删除失败
                }
            })
        },
        handleSelectionChange(val) {  // 多选框选中数据变化方法
            console.log(val)  // 打印选中的数据
            this.multipleSelection = val  // 将选中的数据赋值给多选框选中数据
        },
        delBatch() {  // 批量删除数据方法
            let ids = this.multipleSelection.map(v => v.id)  // 将选中数据的id组成的数组赋值给ids
            this.request.post("/menu/del/batch", ids).then(res => {  // 发送POST请求
                if (res.flag) {  // 如果返回结果为成功
                    this.$message.success("批量删除成功")  // 提示批量删除成功
                    this.load()  // 重新加载数据
                } else {  // 如果返回结果为失败
                    this.$message.error("批量删除失败")  // 提示批量删除失败
                }
            })
        },
        reset() {  // 重置查询条件方法
            this.name = ""  // 将查询名称置为空
            this.load()  // 重新加载数据
        },
        handleSizeChange(pageSize) {  // 每页显示数据条数变化方法
            console.log(pageSize)  // 打印每页显示数据条数
            this.pageSize = pageSize  // 将每页显示数据条数赋值给组件数据中的pageSize
            this.load()  // 重新加载数据
        },
        handleCurrentChange(pageNum) {  // 当前页码变化方法
            console.log(pageNum)  // 打印当前页码
            this.pageNum = pageNum  // 将当前页码赋值给组件数据中的pageNum
            this.load()  // 重新加载数据
        }
    }
}
</script>

<style>
.headerBg {
    background: #eee !important;
}

.fontSize18 {
    font-size: 18px;
}

.fontSize12 {
    font-size: 12px;
}
</style>
