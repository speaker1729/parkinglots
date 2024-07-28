<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                      v-model="name"></el-input>
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
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column prop="flag" label="唯一标识"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column label="操作" width="280" align="center">
                <template slot-scope="scope">
                    <el-button type="info" @click="selectMenu(scope.row)">分配菜单 <i class="el-icon-menu"></i>
                    </el-button>
                    <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i>
                    </el-button>
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
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
        <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" size="small">
                <el-form-item label="名称">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="唯一标识">
                    <el-input v-model="form.flag" autocomplete="off"></el-input>
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
        <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
            <el-tree
                    :props="props"
                    :data="menuData"
                    show-checkbox
                    node-key="id"
                    ref="tree"
                    :default-expanded-keys="expends"
                    :default-checked-keys="checks">
                <span class="custom-tree-node" slot-scope="{ node, data }">
                    <span><i :class="data.icon"></i> {{ data.name }}</span>
                </span>
            </el-tree>
            <div slot="footer" class="dialog-footer">
                <el-button @click="menuDialogVis = false">取 消</el-button>
                <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "Role", // 组件名字，即 Vue 组件的名称
    data() { // 定义数据
        return {
            tableData: [], // 表格数据
            total: 0, // 数据总数
            pageNum: 1, // 当前页码
            pageSize: 10, // 每页显示条数
            name: "", // 查询条件
            form: {}, // 表单数据
            dialogFormVisible: false, // 是否显示表单对话框
            menuDialogVis: false, // 是否显示菜单对话框
            multipleSelection: [], // 表格多选功能选中的数据
            menuData: [], // 菜单数据
            props: { // 树形控件的属性
                label: 'name', // 显示节点文本的属性名
            },
            expends: [], // 默认展开的节点的 key 数组
            checks: [], // 默认勾选的节点的 key 数组
            roleId: 0, // 角色 ID
            roleFlag: '', // 角色标识
            ids: [] // 所有菜单 ID 数组
        }
    },
    created() { // Vue 生命周期钩子，在组件被创建时调用
        this.load()
    },
    methods: {
        load() { // 加载数据
            this.request.get("/role/page", { // 发送 GET 请求到后端接口
                params: { // 请求参数
                    pageNum: this.pageNum, // 当前页码
                    pageSize: this.pageSize, // 每页显示条数
                    name: this.name, // 查询条件
                }
            }).then(res => { // 请求成功后的回调函数
                this.tableData = res.data.records // 将返回的数据设置到表格数据中
                this.total = res.data.total // 将返回的数据总数设置到 total 变量中
            })
            this.request.get("/menu/ids").then(r => { // 获取菜单 ID 数组
                this.ids = r.data
            })
        },
        save() { // 保存数据
            this.request.post("/role", this.form).then(res => { // 发送 POST 请求到后端接口
                if (res.flag) { // 如果保存成功
                    this.$message.success("保存成功") // 使用 element-ui 组件显示成功提示信息
                    this.dialogFormVisible = false // 隐藏表单对话框
                    this.load() // 重新加载数据
                } else { // 如果保存失败
                    this.$message.error("保存失败") // 使用 element-ui 组件显示失败提示信息
                }
            })
        },
        saveRoleMenu() { // 保存角色菜单信息
            this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => { // 发送 POST 请求到后端接口，传入角色 ID 和勾选的菜单 ID 数组
                if (res.flag) { // 如果保存成功
                    this.$message.success("绑定成功") // 使用 element-ui 组件显示成功提示信息
                    this.menuDialogVis = false // 隐藏菜单对话框
                    // 操作管理员角色后需要重新登录
                    if (this.roleFlag === 'ROLE_ADMIN') { // 如果当前角色是管理员
                        this.$store.commit("logout") // 调用 Vuex store 中的 logout 方法，退出登录
                    }
                } else { // 如果保存失败
                    this.$message.error(res.message) // 使用 element-ui 组件显示失败提示信息
                }
            })
        },
        handleAdd() { // 处理添加操作
            this.dialogFormVisible = true // 显示表单对话框
            this.form = {} // 清空表单数据
        },
        handleEdit(row) { // 处理编辑操作
            this.form = JSON.parse(JSON.stringify(row)) // 将当前行的数据拷贝到表单数据中
            this.dialogFormVisible = true // 显示表单对话框
        },
        del(id) { // 删除数据
            this.request.delete("/role/" + id).then(res => { // 发送 DELETE 请求到后端接口
                if (res.flag) { // 如果删除成功
                    this.$message.success("删除成功") // 使用 element-ui 组件显示成功提示信息
                    this.load() // 重新加载数据
                } else { // 如果删除失败
                    this.$message.error("删除失败") // 使用 element-ui 组件显示失败提示信息
                }
            })
        },
        handleSelectionChange(val) { // 处理表格多选功能选中状态变化事件
            console.log(val) // 输出选中的数据
            this.multipleSelection = val // 将选中的数据设置到 multipleSelection 变量中
        },
        reset() { // 重置查询条件
            this.name = "" // 清空查询条件
            this.load() // 重新加载数据
        },
        handleSizeChange(pageSize) { // 处理每页显示条数变化事件
            console.log(pageSize) // 输出每页显示条数
            this.pageSize = pageSize // 将每页显示条数设置到 pageSize 变量中
            this.load() // 重新加载数据
        },
        handleCurrentChange(pageNum) { // 处理当前页码变化事件
            console.log(pageNum) // 输出当前页码
            this.pageNum = pageNum // 将当前页码设置到 pageNum 变量中
            this.load() // 重新加载数据
        },
        async selectMenu(role) { // 处理选择菜单事件
            this.roleId = role.id // 将当前角色的 ID 设置到 roleId 变量中
            this.roleFlag = role.flag // 将当前角色的标识设置到 roleFlag 变量中
            // 请求菜单数据
            this.request.get("/menu").then(res => { // 发送 GET 请求到后端接口
                this.menuData = res.data // 将返回的菜单数据设置到 menuData 变量中
                // 把后台返回的菜单数据处理成 id数组
                this.expends = this.menuData.map(v => v.id) // 将菜单数据处理成展开的节点 key 数组
            })
            this.menuDialogVis = true // 显示菜单对话框
            this.request.get("/role/roleMenu/" + this.roleId).then(res => { // 发送 GET 请求到后端接口，获取当前角色的菜单信息
                this.checks = res.data // 将返回的菜单 ID 数组设置到 checks 变量中
                this.$refs.tree.setCheckedKeys(res.data) // 将返回的菜单 ID 数组设置为树形控件的勾选状态
                this.ids.forEach(id => { // 遍历所有菜单 ID
                    if (!this.checks.includes(id)) { // 如果当前菜单 ID 不在返回的菜单 ID 数组中
                        this.$refs.tree.setChecked(id, false) // 取消该菜单的勾选状态
                    }
                })
            })
        },
    }
}
</script>

<style>
.headerBg {
    background: #eee !important;
}
</style>
