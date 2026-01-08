<template>
  <div class="manage-container">
    <aside :style="isCollapse ? 'width:64px' : 'width: 160px'">
      <div class="title">电力巡检系统</div>
      <el-menu :collapse="isCollapse" :router="true" :default-active="getActivePath()">
        <el-menu-item v-for="menu in menus" :index="menu.route">
          <i :class="menu.icon"></i>
          <span slot="title">{{menu.name}}</span>
        </el-menu-item>
      </el-menu>
    </aside>
    <main>
        <header>
          <i :class="isCollapse ? 'el-icon-s-unfold' :'el-icon-s-fold'" @click="changeMenuState" style="cursor: pointer"></i>
          <el-dropdown v-show="currentUser !== ''" @command="handleCommand">
            <span class="el-dropdown-link" v-text="currentUser.name"></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="info">个人信息</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </header>
        <section>
          <nav>
            <ul class="router-box">
              <li v-for="r in keepAliveRoutes" class="route-item" :class="r.active ? 'route-active' : ''" @contextmenu.prevent="openMenu($event,r)">
                <router-link :to="{path : r.path}" v-text="r.title"></router-link>
                <i class="el-icon-close" @click="removeRoute(r)"></i>
              </li>
            </ul>
          </nav>
          <article>
            <keep-alive>
              <router-view v-if="$route.meta.keepAlive"></router-view>
            </keep-alive>
          </article>
          <footer style="line-height: 40px;text-indent: 20px;font-size: 14px">
            &copy;2022-2030 千锋-成都-JavaEE 2111
          </footer>
        </section>
    </main>
    <ul v-show="showRightMenu" :style="{left:left+'px',top:top+'px'}" class="context-menu">
      <li @click="closeRoute(-1)">
        <i class="el-icon-back"></i>
        <span>关闭左侧</span>
      </li>
      <li @click="closeRoute(1)">
        <i class="el-icon-right"></i>
        <span>关闭右侧</span>
      </li>
      <li @click="closeRoute(0)">
        <i class="el-icon-close"></i>
        <span>关闭其他</span>
      </li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "Manage",
    data() {
      return {
        menus: [],
        isCollapse: false,
        currentUser: {},
        keepAliveRoutes: [],
        showRightMenu: false,
        rightMenuItem: '',
        left: 0,
        top: 0,
      }
    },
    methods: {
      changeMenuState: function () {
        this.isCollapse = !this.isCollapse;
      },
      handleCommand: function (command) {
        if(command === 'info'){

        } else if(command === 'logout'){
          this.$put("/user/logout",null, resp => {
            if(resp.data){
              this.$router.push({path: '/'})
            } else {
              this.$message.error('退出登录失败');
            }
          })
        }
      },
      getActivePath: function(){
        let routes = this.keepAliveRoutes.filter(r => r.active);
        if(routes.length === 0) return '-1';
        return routes[0].path;
      },
      openMenu: function (e, item) {
        this.rightMenuItem = item;
        let x = e.clientX;
        let y = e.clientY;
        this.top = y;
        this.left = x;
        this.showRightMenu = true;
      },
      closeMenu: function(){
        this.showRightMenu = false
      },
      removeRoute: function(route){
        if(route.path === '/list') return;
        let index = -1;
        for(let i=0; i<this.keepAliveRoutes.length; i++){
          if(this.keepAliveRoutes[i] === route){
            index = i;
            break;
          }
        }
        this.keepAliveRoutes.splice(index, 1);
        if(this.keepAliveRoutes.length === 0) return;
        let activeRoutes = this.keepAliveRoutes.filter(r=>r.active === true)
        if(activeRoutes.length === 0){
          let activeIndex = index === 0 ? 0 : index-1;
          this.keepAliveRoutes[activeIndex].active = true;
          this.$router.push({path: this.keepAliveRoutes[activeIndex].path})
        }
      },
      closeRoute: function (flag) {
        let index = -1;
        for(let i=0; i<this.keepAliveRoutes.length; i++){
          if(this.keepAliveRoutes[i] === this.rightMenuItem){
            index = i;
            break;
          }
        }

        switch (flag) {
          case 1: //关闭右侧
            this.keepAliveRoutes.splice(index+1, this.keepAliveRoutes.length - index - 1);
            break;
          case 0: //关闭其他
            this.keepAliveRoutes = [this.keepAliveRoutes[0], this.rightMenuItem];
            break;
          case -1: //关闭左侧
            if(index > 0)
              this.keepAliveRoutes.splice(1, index-1);
        }
        let activeIndex = 0;
        for(let i=0; i<this.keepAliveRoutes.length; i++){
          if(this.keepAliveRoutes[i].active){
            activeIndex = i;
            break;
          }
        }
        this.keepAliveRoutes[activeIndex].active = true;
      }
    },
    watch: {
      showRightMenu(value) {
        if (value) {
          document.body.addEventListener('click', this.closeMenu)
        } else {
          document.body.removeEventListener('click', this.closeMenu)
        }
      }
    },
    created() {
      this.keepAliveRoutes.forEach(r=> r.active = false)
      let existRoute = this.keepAliveRoutes.filter(r=>r.path === this.$router.currentRoute.fullPath);
      if(existRoute.length === 0){
        this.keepAliveRoutes.push({
          title: this.$router.currentRoute.meta.title,
          path: this.$router.currentRoute.fullPath,
          active: true
        });
      } else {
        existRoute[0].active = true;
      }
    },
    mounted() {//在页面挂载之前执行的钩子函数
      this.$get("/user/init",null, resp=>{
        this.menus = resp.data.menus;
        this.currentUser = resp.data.user;
      })
      //路由的后置守卫，只要路由发生变化，守卫即可感知
      this.$router.afterEach((to,from) => {
        if(to.name === 'Login' || to.name === 'Manage'){
          return;
        }
        this.keepAliveRoutes.forEach(r=> r.active = false)
        let existRoute = this.keepAliveRoutes.filter(r=>r.path === to.path);
        if(existRoute.length === 0){
          this.keepAliveRoutes.push({
            title: to.meta.title,
            path: to.path,
            active: true
          });
        } else {
          existRoute[0].active = true
        }
      })
    }
  }
</script>

<style scoped>
  .manage-container{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: row;
  }
  aside{
    background-color: white;
    transition: width 0.5s;
  }
  main{
    flex-grow: 1.5;
    flex-shrink: 0.5;
    background-color: white;
  }
  header{
    padding: 10px;
    height: 40px;
    line-height: 40px;
    background-color: black;
    color: #d7d5d5;
  }
  section{
    height: calc(100% - 60px);
    border-left: 1px solid #ddd;
  }
  nav,footer{
    height: 40px;
  }
  nav{
    border-bottom: 1px solid #ddd;
  }
  footer{
    border-top: 1px solid #ddd;
  }

  article{
    height: calc(100% - 100px);
    padding: 10px;
  }

  .title{
    height: 60px;
    line-height: 60px;
    background-color: black;
    color: #d7d5d5;
    text-indent: 20px;
  }

  .el-dropdown{
    color: #d7d5d5;
    cursor: pointer;
    float: right;
  }

  .el-menu{
    border-right: none;
  }
  .context-menu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: fixed;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
  }
  .context-menu li {
    margin: 0;
    padding: 7px 16px;
    cursor: pointer;
  }

  .context-menu li:hover {
    background-color: rgb(3, 125, 243);;
    color: white;
  }
  .router-box{
    margin: 0;
    padding: 0 0 0 18px;
  }
  .route-item{
    list-style: none;
    display: inline-block;
    font-size: 14px;
    padding: 10px 0 10px 12px;
  }
  .route-active{
    border-bottom: 1px solid #409EFF;
  }
  .route-active > a{
    color: #409EFF !important;
  }
  .route-item > a {
    text-decoration: none;
  }
  .route-item > a:link,
  .route-item > a:visited {
    color: inherit;
  }
  .route-item > i{
    color: white;
    cursor: pointer;
    display: inline-block;
  }
  .route-item > i:hover{
    color: inherit;
  }
  .el-menu{
    height:calc(100% - 60px);
    overflow-y: auto
  }
  .el-menu::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 2px; /*高宽分别对应横竖滚动条的尺寸*/
    height: 1px;
  }

  .el-menu::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 2px;
    box-shadow: inset 0 0 2px rgba(0, 0, 0, 0.2);
    background: #409EFF;
  }

  .el-menu::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    box-shadow: inset 0 0 2px rgba(0, 0, 0, 0.2);
    /*border-radius: 10px;*/
    background: #ededed;
  }
</style>
