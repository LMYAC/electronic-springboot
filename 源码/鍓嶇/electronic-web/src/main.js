// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
//导入ElementUI的css样式
import 'element-ui/lib/theme-chalk/index.css';

//导入axios
import axios from "axios";

import { Loading, MessageBox  } from 'element-ui';

let loadingCount = 0;
let loading;

const startLoading = (tip) => {
  if (loadingCount === 0) {
    loading = Loading.service({
      lock: true,
      text: tip,
      background: 'rgba(0, 0, 0, 0.7)',
      spinner: 'el-icon-loading'
    });
  }
  loadingCount += 1;
};

export const closeLoading = () => {
  if (loadingCount <= 0) {
    return;
  }
  loadingCount -= 1;
  if (loadingCount === 0) {
    loading.close();
  }
};

//Vue不能够直接发送ajax请求，除非自己封装。当然我们也可以引入第三方库来实现ajax请求的发送，这个第三方库就是axios，
//因此需要先安装axios，然后再导入axios，最后将axios在Vue中使用

//这里需要一个拦截器，来实现请求前缀的添加，这样就避免了在每一个请求地址上添加请求前缀
//我们发送的请求都是通过axios来进行发送的，如果能够在axios中添加一个请求拦截器，就能够实现
//上面的前缀添加功能
axios.interceptors.request.use(config =>{
  // if(config.url === '/user/login'){
  //   startLoading('正在登录...')
  // } else {
  //   startLoading('查询中...');
  // }
  config.url = "/electronic" + config.url
  return config;
})

axios.interceptors.response.use(resp=>{
  // closeLoading();
  if(resp.data.status === 4005){
    Vue.prototype.showAlert("登录超时，请重新登录", _ =>{
      router.push({path: '/'});
    });
    return
  } else if(resp.data.status === 4001){
    Vue.prototype.showAlert("访问权限不足，请联系管理员");
    return;
  } else if(resp.data.status === 5000){
    Vue.prototype.showAlert("服务器内部错误，请联系管理员");
    return;
  }
  return resp;
})

let boxInstance;

Vue.prototype.showAlert = function(tip, callback){
  if(boxInstance === undefined){
    boxInstance = new MessageBox({
      title: '提示信息',
      message: tip
    }).then(action => {
      if(typeof callback === 'function'){
        callback()
      }
      boxInstance = undefined;
    }).catch(_ =>{
      boxInstance = undefined;
    });
  }
}

Vue.prototype.showConfirm = function(tip,callback){
  if(boxInstance === undefined){
    boxInstance = new MessageBox({
      title: '提示信息',
      message: tip,
      type: 'danger',
      showCancelButton: true
    }).then(action=>{
        if(typeof callback === 'function'){
          callback()
        }
        boxInstance = undefined;
    }).catch(_=>{
      boxInstance = undefined;
    })
  }
}


//Vue实例中的方法都可以通过this.进行调用。如果我们也能够在Vue实例中添加方法，那么使用起来就很方便
//如何在Vue实例中创建方法呢？
//可以通过prototype属性进行方法的封装，prototype可以理解为在这个Vue类库中添加方法
Vue.prototype.$get = function(url, data, callback){//这就相当于在Vue类库中添加了一个$get()方法
  axios({
    url: url,
    method: 'get',
    params: data //params属性表示参数在url地址后面，SpringMvc可以通过RequestParam指定参数名进行读取
  }).then(function (resp) {
    if(typeof callback === 'function'){
      callback(resp.data)
    }
  }).catch(error => {})
}

Vue.prototype.$post = function(url, data, callback){//这就相当于在Vue类库中添加了一个$get()方法
  axios({
    url: url,
    method: 'post',
    data: data //data表示参数在请求（body）中，后端可以通过流直接读取，SpringMvc可以使用RequestBody注解获取
  }).then(function (resp) {
    if(typeof callback === 'function'){
      callback(resp.data)
    }
  }).catch(error => {})
}

Vue.prototype.$put = function(url, data, callback){//这就相当于在Vue类库中添加了一个$get()方法
  axios({
    url: url,
    method: 'put',
    data: data
  }).then(function (resp) {
    if(typeof callback === 'function'){
      callback(resp.data)
    }
  }).catch(error => {})
}

Vue.prototype.$del = function(url, data, callback){//这就相当于在Vue类库中添加了一个$get()方法
  axios({
    url: url,
    method: 'delete',
    data: data
  }).then(function (resp) {
    if(typeof callback === 'function'){
      callback(resp.data)
    }
  }).catch(error => {})
}

Vue.prototype.$ajax = function(url, method, data, callback){
  axios({
    url: url,
    method: method,
    params: method.toLowerCase() === 'get' ? data : {},
    data: method.toLowerCase() !== 'get' ? data: {}
  }).then(function (resp) {
    if(typeof callback === 'function'){
      callback(resp.data)
    }
  }).catch(error => {})
}

Vue.prototype.$upload = function(url, files, callback){
  let formData = new FormData(); //构建了一个表单数据，主要就是用来模拟表单数据的提交
  for(let i=0; i<files.length; i++){
    formData.append("files", files[i]);
  }
  axios({
    url: url,
    method: 'post',
    data: formData,
    headers:{
      'Content-Type':'multipart/form-data'
    }
  }).then(function (resp) {
    if(typeof callback === 'function'){
      callback(resp.data)
    }
  }).catch(error=>{ });
}

Vue.directive('file',{
  bind: function (el, binding, vnode) {
    el.innerHTML = '<input type="text">\n' +
      '            <i style="position: relative;">\n' +
      '                <input type="button" value="浏览...">\n' +
      '                <input type="file">\n' +
      '            </i>';
    let children = el.children;
    children[0].style.backgroundColor = "#FFF";
    children[0].style.backgroundImage = "none";
    children[0].style.borderRadius = "4px";
    children[0].style.border = "1px solid #DCDFE6";
    children[0].style.boxSizing = "border-box";
    children[0].style.color = "#606266";
    children[0].style.height = "28px";
    children[0].style.lineHeight = "28px";
    children[0].style.outline = "0";
    children[0].style.padding = "0 15px";
    children[0].onfocus = function(){
      children[0].style.borderColor = "#409EFF";
    }
    children[0].onblur = function(){
      children[0].style.borderColor = "#DCDFE6";
    }

    let button = children[1].children[0];
    button.style.lineHeight = '1';
    button.style.whitespace = 'nowrap';
    button.style.cursor = 'pointer';
    button.style.backgroundColor = '#409EFF';
    button.style.border = '1px solid #DCDFE6';
    button.style.textAlign = 'center';
    button.style.boxSizing = 'border-box';
    button.style.outline = '0';
    button.style.margin = '0';
    button.style.transition = '.1s';
    button.style.fontWeight = '500';
    button.style.color = '#FFF';
    button.style.fontSize = '12px';
    button.style.borderRadius = '3px';
    button.style.padding = '7px 15px';
    let fileInput = children[1].children[1];
    fileInput.style.position = 'absolute';
    fileInput.style.left = '0';
    fileInput.style.top = '-5px';
    fileInput.style.width = '65px';
    fileInput.style.height = '26px';
    fileInput.style.opacity = '0';
    //给文件域添加一个改变事件
    fileInput.onchange = function () {
      if(fileInput.files !== null && fileInput.files.length > 0){
        let file = fileInput.files[0];
        children[0].value = file.name
        //将数据与变量绑定
        vnode.context[binding.expression] = file;
      } else {
        //将数据与变量绑定
        vnode.context[binding.expression] = ''
        children[0].value = ''
      }
    }
  }
});


Vue.config.productionTip = false
//安装ElementUI插件
Vue.use(ElementUI)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  render: h => h(App)
})
