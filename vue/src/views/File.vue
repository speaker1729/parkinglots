<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                      v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
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
            <el-table-column prop="name" label="文件名称"></el-table-column>
            <el-table-column prop="type" label="文件类型"></el-table-column>
            <el-table-column prop="size" label="文件大小(kb)"></el-table-column>
            <el-table-column label="预览">
                <template v-slot:default="scope">
                    <el-image
                            style="width: 100px; height: 100px"
                            :src="scope.row.url"
                            :preview-src-list="srcList">
                    </el-image>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200" align="center">
                <template v-slot:default="scope">
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
    </div>
</template>

<script>
export default {
    name: "File", // 组件名
    data() {
        return {
            tableData: [], // 表格数据
            srcList: [], // 存储文件url的数组
            name: '', // 文件名
            multipleSelection: [], // 多选框选中的数据
            pageNum: 1, // 当前页码
            pageSize: 10, // 每页数据量
            total: 0 // 数据总数
        }
    },
    created() {
        this.load() // 组件创建时调用load方法加载数据
    },
    methods: {
        load() { // 加载数据的方法
            this.request.get("/file/page", { // 调用接口/file/page
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                }
            }).then(res => { // 接口调用成功后的回调函数
                this.tableData = res.data.records // 将返回的数据存储在tableData中
                this.tableData.forEach((item) => { // 遍历tableData中每个文件
                    this.srcList.push(item.url); // 将文件的url存储在srcList中
                })
                this.total = res.data.total // 将数据总数存储在total中
            })
        },
        del(id) { // 删除单个文件的方法
            this.request.delete("/file/" + id).then(res => { // 调用接口/file/:id，其中:id是文件的id
                if (res.flag) { // 接口调用成功，flag为true
                    this.$message.success("删除成功") // 弹出删除成功的提示消息
                    this.load() // 重新加载数据
                } else { // 接口调用失败，flag为false
                    this.$message.error("删除失败") // 弹出删除失败的提示消息
                }
            })
        },
        handleSelectionChange(val) { // 多选框选中状态改变时调用的方法
            this.multipleSelection = val // 将选中的数据存储在multipleSelection中
        },
        delBatch() { // 批量删除文件的方法
            let ids = this.multipleSelection.map(v => v.id)  // 获取待删除文件的id数组
            this.request.post("/file/del/batch", ids).then(res => { // 调用接口/file/del/batch，其中参数为待删除文件的id数组
                if (res.flag) { // 接口调用成功，flag为true
                    this.$message.success("批量删除成功") // 弹出批量删除成功的提示消息
                    this.load() // 重新加载数据
                } else { // 接口调用失败，flag为false
                    this.$message.error("批量删除失败") // 弹出批量删除失败的提示消息
                }
            })
        },
        reset() { // 重置查询条件的方法
            this.name = "" // 将name设为空
            this.load() // 重新加载数据
        },
        handleSizeChange(pageSize) { // 每页数据量改变时调用的方法
            console.log(pageSize)
            this.pageSize = pageSize // 将pageSize设为新值
            this.load() // 重新加载数据
        },
        handleCurrentChange(pageNum) { // 页码改变时调用的方法
            console.log(pageNum)
            this.pageNum = pageNum // 将pageNum设为新值
            this.load() // 重新加载数据
        },
    }
}
</script>

<style scoped>

</style>
