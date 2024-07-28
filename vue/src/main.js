import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/css/gloabl.css'
import request from "@/utils/request";
import BaiduMap from 'vue-baidu-map'
Vue.use(BaiduMap, {
  ak: 'Qi87yd0e4ufjauY9cMdAdaElHWArMfUU'  //这个地方是官方提供的ak密钥
})
Vue.config.productionTip = false

Vue.use(ElementUI, { size: "mini" });

Vue.prototype.request=request

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
