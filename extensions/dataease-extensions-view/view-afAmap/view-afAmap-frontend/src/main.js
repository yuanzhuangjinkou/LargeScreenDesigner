// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'

import 'element-ui/lib/theme-chalk/index.css'
import Cookies from 'js-cookie'
import i18n from './de-base/lang'
import draggable from 'vuedraggable'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import '@/icons' // icon
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import axios from 'axios'


Vue.config.productionTip = false
Vue.use(Antd)
Vue.use(ElementUI);
// Vue.use(VueAxios,axios);

// Vue.use(ElementUI, {
//   size: Cookies.get('size') || 'medium',
//   i18n: (key, value) => i18n.t(key, value)
// })
Vue.component('Treeselect', Treeselect)
Vue.component('draggable', draggable)
Vue.prototype.hasDataPermission = function(pTarget, pSource) {

  if (pSource && pTarget) {
    return pSource.indexOf(pTarget) > -1
  }
  return false
}
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  i18n,
  components: { App },
  template: '<App/>'
})
