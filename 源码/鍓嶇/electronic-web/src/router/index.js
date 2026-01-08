import Vue from 'vue'
import Router from 'vue-router'
//安装路由插件
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      redirect: '/login'
    },{
      path: '/login',
      name: 'Login',
      //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
      component: _ => import('../components/Login')
    },{
      path: '/manage',
      name: 'Manage',
      //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
      component: _ => import('../components/Manage'),
      children:[{
        path: '/tower',
        name: 'Tower',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/tower/Tower'),
        meta:{
          title: '杆塔管理',
          keepAlive: true
        }
      },{
        path: '/lines',
        name: 'Lines',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/line/Lines'),
        meta:{
          title: '线路管理',
          keepAlive: true
        }
      },{
        path: '/inspectionTask',
        name: 'InspectionTask',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/inspection/InspectionTask'),
        meta:{
          title: '巡检任务管理',
          keepAlive: true
        }
      },{
        path: '/inspectionReception',
        name: 'InspectionReception',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/inspectionReception/InspectionReception'),
        meta:{
          title: '巡检回执管理',
          keepAlive: true
        }
      },{
        path: '/defectTask',
        name: 'DefectTask',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/defectTask/DefectTask'),
        meta:{
          title: '消缺任务管理',
          keepAlive: true
        }
      },{
        path: '/defectReception',
        name: 'DefectReception',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/defectReception/DefectReception'),
        meta:{
          title: '消缺回执管理',
          keepAlive: true
        }
      },{
        path: '/defect',
        name: 'Defect',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/defect/Defect'),
        meta:{
          title: '缺陷管理',
          keepAlive: true
        }
      },{
        path: '/user',
        name: 'User',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/user/User'),
        meta:{
          title: '用户管理',
          keepAlive: true
        }
      },{
        path: '/role',
        name: 'Role',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/role/Role'),
        meta:{
          title: '角色管理',
          keepAlive: true
        }
      },{
        path: '/log',
        name: 'Log',
        //导入组件的时候，可以省略组件的后缀名，省略的后缀名可以是js,css,vue等
        component: _ => import('../components/log/Log'),
        meta:{
          title: '日志管理',
          keepAlive: true
        }
      }]
    }
  ]
})
