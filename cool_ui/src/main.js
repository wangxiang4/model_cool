import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import Cookies from 'js-cookie'
//a modern alternative to CSS resets
import 'normalize.css/normalize.css'
import Element from 'element-ui'
import './assets/styles/element-variables.scss'
//global css
import '@/assets/styles/index.scss'
//模酷酷独立css加载
import '@/assets/styles/cool.scss'
//icon
import './assets/icons'
//permission control
import './permission'

//按钮权限校验器 注册指标
import permission from './directive/permission'
//字典注册
import { getDicts } from "@/api/system/dictData"
//模酷酷独立js注册
import { parseTime, newPath, resetForm, addDateRange, selectDictLabel, download, handleTree } from "@/utils/cool"
//分页组件注册
import Pagination from "@/components/Pagination"

//全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.parseTime = parseTime
Vue.prototype.newPath = newPath
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

//element-message
Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgWarning = function (msg) {
  this.$message({ showClose: true, message: msg, type: "warning" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载
Vue.component('Pagination', Pagination)

Vue.use(permission)

Vue.use(Element, {
    size: Cookies.get('size') || 'medium' // set element-ui default size
})

//关闭开发模式有生产提示
Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
