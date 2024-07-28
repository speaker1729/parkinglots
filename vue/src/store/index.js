import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";

Vue.use(Vuex)
const store = new Vuex.Store({
    state: {
        currentPathName: '',  // 当前路由名称
        commonTitle: "停车位共享平台"  // 公共标题
    },
    mutations: {
        setPath(state) {  // 设置当前路由名称
            state.currentPathName = localStorage.getItem("currentPathName")
        },
        logout() {  // 退出登录
            // 清空缓存
            localStorage.removeItem("user")
            localStorage.removeItem("menus")
            router.push("/login").then(r => {
            })
            // 重置路由
            resetRouter()
        }
    }
})
export default store
