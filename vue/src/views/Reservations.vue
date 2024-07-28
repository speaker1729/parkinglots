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
            <el-table-column prop="ownerName" label="所有者姓名"></el-table-column>
            <el-table-column prop="ownerPhone" label="所有者手机号" width="100"></el-table-column>
            <el-table-column prop="location" label="位置"></el-table-column>
            <el-table-column prop="price" label="价格">
                <template v-slot="scope">
                    {{ scope.row.price }}/每小时
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
                <template v-slot="scope">
                    <el-tag type="success" v-if="scope.row.status===1">待预定</el-tag>
                    <el-tag type="danger" v-if="scope.row.status===2">已预定</el-tag>
                    <el-tag type="danger" v-if="scope.row.status===3">已结束</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="ownerId" label="租用信息">
                <template v-slot="scope">
                    <span v-if="scope.row.status===2">
                        <el-button type="warning" @click="shwoUser(scope.row)">
                            查看
                        </el-button>
                    </span>
                    <span v-else>无</span>
                </template>
            </el-table-column>
            <el-table-column prop="availableFrom" label="可用起始时间"></el-table-column>
            <el-table-column prop="availableTo" label="可用结束时间"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="image" label="缩略图">
                <template v-slot:default="scope">
                    <el-image
                            style="width: 100px; height: 100px"
                            :src="scope.row.image">
                    </el-image>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="300" align="center">
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
                            @confirm="del(scope.row.parkingLotId)"
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
        <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
            <el-form :rules="formRules" ref="formRef" :model="form" label-width="120px" size="small"
                     style="width: 80%; margin: 0 auto">
                <el-form-item label="位置" prop="location">
                    <el-input v-model="form.location" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="价格" prop="price">
                    <el-input v-model="form.price" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="可用起始时间">
                    <el-date-picker
                            v-model="dates"
                            type="datetimerange"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="form.description" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="图片">
                    <el-upload
                            class="avatar-uploader"
                            action="http://localhost:9090/file/upload"
                            :on-success="handleImportSuccess"
                            :limit="1"
                            :show-file-list="false"
                            :file-list="fileList"
                            list-type="picture-card"
                    >
                        <img v-if="form.image" style="width:148px;height:148px" :src='form.image'/>
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="信息" :visible.sync="showZuyong" width="40%" :close-on-click-modal="false">
            <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
                <el-form-item label="车牌号">
                    <el-input v-model="zFrom.licensePlate" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="租用时间">
                    <el-date-picker
                            v-model="zDates"
                            type="datetimerange"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showZuyong = false">取 消</el-button>
                <el-button type="primary" @click="zuyong">租 用</el-button>
            </div>
        </el-dialog>
        <el-dialog title="信息" :visible.sync="showUser" width="40%" :close-on-click-modal="false">
            <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
                <el-form-item label="用户">
                    <span>{{ userFrom.username }}</span>
                </el-form-item>
                <el-form-item label="手机号">
                    <span>{{ userFrom.phone }}</span>
                </el-form-item>
                <el-form-item label="车牌号">
                    <span>{{ userFrom.licensePlate }}</span>
                </el-form-item>
                <el-form-item label="使用时间段">
                    <span>{{ userFrom.startTime }} <span style="margin: 0 20px">-</span> {{ userFrom.endTime }}</span>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "ParkingLots",
    data() {
        return {
            formRules: {    // 表单验证规则
                location: [
                    {required: true, message: '请输入数据', trigger: 'blur'}
                ],
                price: [
                    {required: true, message: '请输入数据', trigger: 'blur'}
                ],
                description: [
                    {required: true, message: '请输入数据', trigger: 'blur'}
                ]
            },
            tableData: [],      // 表格数据
            fileList: [],       // 文件列表数据
            total: 0,           // 总记录数
            pageNum: 1,         // 当前页数
            pageSize: 10,       // 每页显示记录数
            name: "",           // 名字
            form: {},           // 表单数据
            zFrom: {},          // 租用表单数据
            userFrom: {},       // 用户表单数据
            zDates: [],         // 租用时间
            dates: [],          // 时间
            dialogFormVisible: false,   // 对话框是否可见
            showZuyong: false,  // 显示租用表单
            showUser: false,    // 显示用户表单
            multipleSelection: [],  // 多选记录
            showId: null,       // 显示记录的ID
            user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}  // 当前用户信息
        }
    },
    created() {
        this.load()
    },
    methods: {
        /**
         * 格式化日期时间字符串
         * @param {string} dateTimeString - 日期时间字符串
         * @returns {string} 格式化后的日期时间字符串，格式为yyyy-MM-dd HH:mm:ss
         */
        formatDateTime2(dateTimeString) {
            const dateTime = new Date(dateTimeString);
            const year = dateTime.getFullYear();
            const month = ("0" + (dateTime.getMonth() + 1)).slice(-2);
            const day = ("0" + dateTime.getDate()).slice(-2);
            const hours = ("0" + dateTime.getHours()).slice(-2);
            const minutes = ("0" + dateTime.getMinutes()).slice(-2);
            const seconds = ("0" + dateTime.getSeconds()).slice(-2);
            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
        },
        /**
         * 显示租用弹窗
         * @param {number} id - 停车场ID
         */
        zuShow(id) {
            this.zFrom.parkingLotId = id
            this.showZuyong = true
        },
        // 租用操作
        zuyong() {
            this.zFrom.startTime = this.formatDateTime2(this.zDates[0])
            this.zFrom.endTime = this.formatDateTime2(this.zDates[1])
            this.zFrom.userId = this.user.id
            // 发送请求
            this.request.post("/parkingLots", {
                parkingLotId: this.zFrom.parkingLotId,
                status: 2,
                ownerId: this.user.id
            }).then(res => {
                if (res.flag) {
                    this.request.post("/reservations", this.zFrom).then(res => {
                        if (res.flag) {
                            this.$message.success("租用成功")
                            this.showZuyong = false
                            this.load()
                        } else {
                            this.$message.error("租用失败")
                        }
                    })
                } else {
                    this.$message.error("租用失败")
                }
            })
        },
        /**
         * 显示用户信息弹窗
         * @param {object} data - 数据对象，包含停车场ID等信息
         */
        shwoUser(data) {
            // 发送请求获取预约信息
            this.request.get("/reservations/findByLogId/" + data.parkingLotId).then(res => {
                if (res.flag) {
                    console.log(res.flag)
                    let dd = res.data
                    // 填充数据
                    this.userFrom.licensePlate = dd.licensePlate
                    this.userFrom.startTime = dd.startTime
                    this.userFrom.endTime = dd.endTime
                    this.userFrom.username = dd.user.nickname
                    this.userFrom.phone = dd.user.phone
                    this.showUser = true
                }
            })
            // 发送请求获取用户信息
            this.request.get("/user/" + data.parkingLotId).then(res => {
                if (res.flag) {
                    let d = res.data
                    // TODO: 填充数据
                }
            })
        },
        // 加载停车场数据
        load() {
            // 如果当前用户角色是管理员
            if (this.user.role === "ROLE_ADMIN") {
                // 发送POST请求，获取停车场列表数据
                this.request.post("/parkingLots/page", {
                    name: this.name, pageNum: this.pageNum,
                    pageSize: this.pageSize
                }).then(res => {
                    // 将获取到的数据赋值给tableData和total
                    this.tableData = res.data.records
                    this.total = res.data.total
                })
            }
            // 否则，当前用户不是管理员
            else {
                // 发送POST请求，获取该用户所拥有的停车场列表数据
                this.request.post("/parkingLots/page", {
                    name: this.name, pageNum: this.pageNum,
                    pageSize: this.pageSize
                }).then(res => {
                    // 将获取到的数据过滤，只保留ownerId与当前用户id相等的停车场数据
                    this.tableData = res.data.records.filter(item => item.ownerId === this.user.id)
                    // 将获取到的数据总数赋值给total
                    this.total = res.data.total
                })
            }
        },
        // 当选择table中的数据项时触发
        handleSelectionChange(val) {
            console.log(val)
            this.multipleSelection = val
        },
        // 保存停车场数据
        save() {
            // 首先校验表单数据的合法性
            this.$refs['formRef'].validate((valid) => {
                if (valid) {
                    // 格式化时间数据，并将其他需要提交的数据赋值给form对象
                    this.form.availableFrom = this.formatDateTime2(this.dates[0])
                    this.form.availableTo = this.formatDateTime2(this.dates[1])
                    this.form.ownerId = this.user.id
                    this.form.status = 1
                    // 发送POST请求，保存停车场数据
                    this.request.post("/parkingLots", this.form).then(res => {
                        if (res.flag) {
                            this.$message.success("保存成功")
                            // 关闭对话框，并重新加载停车场数据
                            this.dialogFormVisible = false
                            this.load()
                        } else {
                            this.$message.error("保存失败")
                        }
                    })
                }
            })
        },
        // 点击添加按钮时触发
        handleAdd() {
            // 显示添加停车场对话框，并清空表单数据
            this.dialogFormVisible = true
            this.form = {}
            // 清空图片和文件上传组件中的已选文件
            this.$nextTick(() => {
                if (this.$refs.img) {
                    this.$refs.img.clearFiles();
                }
                if (this.$refs.file) {
                    this.$refs.file.clearFiles();
                }
            })
        },
        //显示修改弹出框
        handleEdit(row) {
            this.dates = []
            this.form = JSON.parse(JSON.stringify(row))
            console.log(this.form)
            this.dates.push(this.form.availableFrom)
            this.dates.push(this.form.availableTo)
            this.dialogFormVisible = true
            this.$nextTick(() => {
                if (this.$refs.image) {
                    this.$refs.image.clearFiles();
                }
            })
        },
        del(id) {
            this.request.delete("/parkingLots/" + id).then(res => {
                if (res.flag) {
                    this.$message.success("删除成功")
                    this.load()
                } else {
                    this.$message.error("删除失败")
                }
            })
        },
        handleImportSuccess(res) {
            this.form.image = res   // 导入成功后将图片路径赋值给表单的image字段
        },
        delBatch() {
            if (!this.multipleSelection.length) {    // 如果没有选择需要删除的数据，则提示错误信息
                this.$message.error("请选择需要删除的数据")
                return
            }
            let ids = this.multipleSelection.map(v => v.parkingLotId)  // 将多选的记录的ID提取出来
            this.request.post("/parkingLots/del/batch", ids).then(res => {   // 发送批量删除请求，传入记录的ID数组
                if (res.flag) {     // 如果删除成功，则提示成功信息，并重新加载数据
                    this.$message.success("批量删除成功")
                    this.load()
                } else {    // 如果删除失败，则提示失败信息
                    this.$message.error("批量删除失败")
                }
            })
        },
        reset() {
            this.name = ""  // 将搜索框中的内容清空
            this.load()      // 重新加载数据
        },
        handleSizeChange(pageSize) {
            this.pageSize = pageSize   // 改变每页显示记录数时，将页面记录数更新
            this.load()      // 重新加载数据
        },
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum     // 改变当前页数时，将页面显示的页数更新
            this.load()      // 重新加载数据
        },
    }
}
</script>

<style>
.headerBg {
    background: #eee !important;
}
</style>
