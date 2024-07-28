<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <el-card style="margin: 20px 0">
            <el-form :inline="true" :model="searchFrom" class="demo-form-inline">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="住宅小区">
                            <el-input v-model="searchFrom.name" placeholder="请输入住宅小区"></el-input>
                        </el-form-item>
                        <el-form-item label="价格区间">
                            <div style="display:flex;">
                                <el-input v-model="searchFrom.beginPrice" placeholder="请输入起始价格"></el-input>
                                <span style="margin: 0 20px">-</span>
                                <el-input v-model="searchFrom.endPrice" placeholder="请输入结束价格"></el-input>
                            </div>
                        </el-form-item>
                        <el-form-item label="时间段">
                            <el-date-picker
                                    v-model="searchFrom.zDates"
                                    type="datetimerange"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    :default-time="['00:00:00', '23:59:59']">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系人">
                            <el-input v-model="searchFrom.userPhone" placeholder="请输入联系人"></el-input>
                        </el-form-item>
                        <el-form-item label="预定状态">
                            <el-select v-model="searchFrom.status" placeholder="请选择预定状态">
                                <el-option label="待预定" :value="1"></el-option>
                                <el-option label="已预定" :value="2"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-form-item>
                        <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
                        <el-button type="warning" @click="reset">重置</el-button>
                        <el-button type="success" @click="ydcsJX">按预定次数降序</el-button>
                    </el-form-item>
                </el-row>
            </el-form>
        </el-card>

        <div style="margin: 10px 0">
            <el-button type="success" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                  @selection-change="handleSelectionChange">
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
            <el-table-column prop="ownerId" label="预定信息">
                <template v-slot="scope">
          <span v-if="scope.row.status===2">
            <el-button type="success" @click="shwoUser(scope.row)">
              查看
            </el-button>
          </span>
                    <span v-else>无</span>
                </template>
            </el-table-column>
            <el-table-column prop="availableFrom" label="可用起始时间" width="180"></el-table-column>
            <el-table-column prop="availableTo" label="可用结束时间" width="180"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="sum" label="预定次数"></el-table-column>
            <el-table-column prop="image" label="缩略图" width="130">
                <template v-slot:default="scope">
                    <el-image
                            style="width: 100px; height: 100px"
                            :src="scope.row.image">
                    </el-image>
                </template>
            </el-table-column>
            <el-table-column prop="description" label="停车地位置" width="100">
                <template v-slot:default="scope">
                    <el-button type="success" @click="dituClick(scope.row.location)">
                        位置
                    </el-button>
                </template>

            </el-table-column>
            <el-table-column label="操作" width="300" align="center">
                <template v-slot:default="scope">
                    <el-button v-if="scope.row.status===1 && checkDate(scope.row.availableTo)" type="primary" @click="zuShow(scope.row.parkingLotId)">车位预定</el-button>
                    <el-button v-if="scope.row.status===2 && user.id===scope.row.userId && checkDate(scope.row.availableTo)" type="primary"
                               @click="quXiao(scope.row.parkingLotId)">车位取消
                    </el-button>
<!--                    <el-button type="warning" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>-->
                </template>
            </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="searchFrom.pageNum"
                    :page-sizes="[2, 5, 10, 20]"
                    :page-size="searchFrom.pageSize"
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

        <el-dialog title="预定" :visible.sync="showZuyong" width="40%"
                   :close-on-click-modal="false">
            <el-form :rules="rules" ref="ruleForm" :model="zFrom" label-width="120px" size="small"
                     style="width: 80%; margin: 0 auto">
                <el-form-item label="车牌号" prop="licensePlate">
                    <el-input v-model="zFrom.licensePlate" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="预定时间" prop="zDates">
                    <el-date-picker
                            v-model="zFrom.zDates"
                            type="datetimerange"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :default-time="['00:00:00', '23:59:59']">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showZuyong = false">取 消</el-button>
                <el-button type="primary" @click="zuyong">预 定</el-button>
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
        <el-dialog title="位置" :visible.sync="dituShow" width="40%" :close-on-click-modal="false">
            <baidu-map
                    class="bm-view"
                    :zoom="15"
                    :center="center"
                    inertial-dragging
                    @ready="mapReady"
                    :scroll-wheel-zoom="true"
            >
                <!-- 定位控件   anchor="BMAP_ANCHOR_BOTTOM_RIGHT"代表放在右下角 -->
                <bm-geolocation
                        anchor="BMAP_ANCHOR_BOTTOM_RIGHT"
                        :showAddressBar="true"
                        :autoLocation="true"
                ></bm-geolocation>
                <!-- 缩放控件   anchor代表控件停靠位置   anchor="BMAP_ANCHOR_TOP_RIGHT"代表放在右上角-->
                <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>
            </baidu-map>
        </el-dialog>
    </div>
</template>

<script>
import {
    BaiduMap,
    BmControl,
    BmView,
    BmAutoComplete,
    BmLocalSearch,
    BmMarker,
    BmGeolocation,
} from "vue-baidu-map";

export default {
    name: "ParkingLots",
    //需要引入的组件
    components: {
        BaiduMap,
        BmControl,
        BmView,
        BmAutoComplete,
        BmLocalSearch,
        BmMarker,
        BmGeolocation,
    },
    data() {
        return {
            center: "",
            rules: {
                licensePlate: [
                    {required: true, message: '请输入车牌号', trigger: 'blur'},
                    {pattern: /^[\u4e00-\u9fa5]{1,2}[A-Z]{1}[A-Z0-9]{5}$/, message: '请输入正确的车牌号', trigger: 'blur'}
                ],
                zDates: [
                    {required: true, message: '请选择预定时间', trigger: 'blur'}
                ]
            },
            formRules: {
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
            tableData: [],
            fileList: [],
            total: 0,
            name: "",
            form: {},
            zFrom: {},
            userFrom: {},
            searchFrom: {
                pageNum: 1,
                pageSize: 10
            },
            zDates: [],
            dates: [],
            dialogFormVisible: false,
            showZuyong: false,
            dituShow: false,
            showUser: false,
            multipleSelection: [],
            showId: null,
            user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
        }
    },
    created() {
        this.load()
    },
    methods: {
        checkDate(date){
            const availableTo = new Date(date);
            const currentDate = new Date();
            if (availableTo > currentDate) {
                // availableTo 早于当前日期和时间
                return true
            }
            return  false
        },
        ydcsJX(){
            this.tableData.sort((a, b) => b.sum - a.sum);
        },
        dituClick(data) {
            this.center = ""
            this.center = data
            this.dituShow = true
        },
        //地图加载后的回调
        mapReady({BMap, map}) {
            //保存this指向，因为在百度的回调中this不指向vue
            const _this = this;
            // 获取自动定位方法
            var geolocation = new BMap.Geolocation();
            // 获取自动定位获取的坐标信息
            geolocation.getCurrentPosition(
                function (r) {
                    //可以conso.log看一下这个r，他里面包含了检索到的位置信息。下面就把两个维度信息赋值给center来定位
                    _this.center = {
                        lng: r.point.lng,
                        lat: r.point.lat,
                    };
                },
                //启用高精度
                {enableHighAccuracy: true}
            );
        },
        //车位预定弹出框显示
        zuShow(id) {
            this.zFrom = {}
            this.zFrom.parkingLotId = id
            this.showZuyong = true
        },
        //车位预定
        zuyong() {
            this.$refs['ruleForm'].validate((valid) => {
                console.log(valid)
                if (valid) {
                    this.zFrom.startTime = this.formatDateTime2(this.zFrom.zDates[0])
                    this.zFrom.endTime = this.formatDateTime2(this.zFrom.zDates[1])
                    this.zFrom.userId = this.user.id
                    this.request.post("/reservations/zuyong", this.zFrom).then(res => {
                        if (res.flag) {
                            this.$message.success("预定成功")
                            this.showZuyong = false
                            this.load()
                        } else {
                            this.$message.error("预定失败")
                        }
                    })
                }
            })
        },
        //车位取消
        quXiao(id) {
            this.request.get("/reservations/quxiao/" + id).then(res => {
                if (res.flag) {
                    this.$message.success("取消成功")
                    this.load()
                } else {
                    this.$message.error("取消失败")
                }
            })
        },
        //查看预定详情信息
        shwoUser(data) {
            this.request.get("/reservations/findByLogId/" + data.parkingLotId).then(res => {
                if (res.flag) {
                    console.log(res.flag)
                    let dd = res.data
                    this.userFrom.licensePlate = dd.licensePlate
                    this.userFrom.startTime = dd.startTime
                    this.userFrom.endTime = dd.endTime
                    this.userFrom.username = dd.user.nickname
                    this.userFrom.phone = dd.user.phone
                    this.showUser = true
                }
            })
        },
        load() {
            if (this.searchFrom.zDates) {
                this.searchFrom.beginTime = this.searchFrom.zDates[0]
                this.searchFrom.endTime = this.searchFrom.zDates[1]
            }
            this.request.post("/parkingLots/page", this.searchFrom).then(res => {
                this.tableData = res.data.records
                this.total = res.data.total
            })
        },
        // //批量删除选择框
        // handleSelectionChange(val) {
        //     this.multipleSelection = val
        // },
        //保存数据
        save() {
            this.$refs['formRef'].validate((valid) => {
                if (valid) {
                    this.form.availableFrom = this.formatDateTime2(this.dates[0])
                    this.form.availableTo = this.formatDateTime2(this.dates[1])
                    this.form.ownerId = this.user.id
                    this.form.status=1
                    this.request.post("/parkingLots", this.form).then(res => {
                        if (res.flag) {
                            this.$message.success("保存成功")
                            this.dialogFormVisible = false
                            this.load()
                        } else {
                            this.$message.error("保存失败")
                        }
                    })
                }
            })
        },
        //显示新增弹出框
        handleAdd() {
            this.dialogFormVisible = true
            this.form = {}
            this.$nextTick(() => {
                if (this.$refs.image) {
                    this.$refs.image.clearFiles();
                }
            })
        },
        //显示修改弹出框
        // handleEdit(row) {
        //     this.dates = []
        //     this.form = JSON.parse(JSON.stringify(row))
        //     console.log(this.form)
        //     this.dates.push(this.form.availableFrom)
        //     this.dates.push(this.form.availableTo)
        //     this.dialogFormVisible = true
        //     this.$nextTick(() => {
        //         if (this.$refs.image) {
        //             this.$refs.image.clearFiles();
        //         }
        //     })
        // },
        handleImportSuccess(res) {
            this.form.image = res
        },
        reset() {
            this.searchFrom = {
                pageNum: 1,
                pageSize: 10
            }
            this.load()
        },
        handleSizeChange(pageSize) {
            this.searchFrom.pageSize = pageSize
            this.load()
        },
        handleCurrentChange(pageNum) {
            this.searchFrom.pageNum = pageNum
            this.load()
        },
        //日期处理工具
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
    }
}
</script>


<style>
.headerBg {
    background: #eee !important;
}

.fontl {
    margin-right: 10px;
}

/* 给个宽高 */
.bm-view {
    height: 400px;
}
</style>
