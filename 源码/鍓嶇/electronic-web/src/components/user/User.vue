<template>
    <div class="content-box">
      <div class="condition-box">
        <span>账号</span>
        <el-input v-model="condition.username" clearable placeholder="请输入账号" size="mini"></el-input>
        <span>姓名</span>
        <el-input v-model="condition.name" clearable placeholder="请输入姓名" size="mini"></el-input>
        <span>手机号码</span>
        <el-input v-model.number="condition.phone" clearable placeholder="请输入手机号码" size="mini"></el-input>
        <span>账号状态</span>
        <el-select v-model="condition.state" placeholder="请选择账号状态" clearable size="mini">
          <el-option v-for="s in states" :key="s.value" :label="s.text":value="s.value"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="search">查询</el-button>
        <el-button type="default" icon="el-icon-refresh-left" size="mini" @click="reset">重置</el-button>
      </div>
      <div class="button-box">
        <el-button type="danger" icon="el-icon-circle-plus-outline" size="mini" @click="showAddDialog">新增</el-button>
        <el-button type="success" icon="el-icon-upload2" size="mini" @click="showImportDialog">导入</el-button>
        <el-link :href="getExportUrl()">
          <el-button type="success" icon="el-icon-download" size="mini">导出</el-button>
        </el-link>
        <el-dropdown @command="handleCommand">
          <el-button type="primary" size="mini">
            批量操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <!--如果变量需要绑定一个值，而这个值又可以从定义的方法中的获取，那么此时可以直接调用这个方法获取值，调用的时候必须要在方法后面加上()-->
            <el-dropdown-item command="unfrozen" icon="el-icon-circle-check" :disabled="isBatchButtonDisabled()">解冻</el-dropdown-item>
            <el-dropdown-item command="frozen" icon="el-icon-discover" :disabled="isBatchButtonDisabled()">冻结</el-dropdown-item>
            <el-dropdown-item command="delete" icon="el-icon-circle-close" :disabled="isBatchButtonDisabled()">删除</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <el-table :data="users" border @selection-change="handleSelectionChange" style="width: 100%;" height="calc(100% - 120px)" size="mini">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="username" label="账号"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="sexText" label="性别"></el-table-column>
        <el-table-column prop="age" label="年龄"></el-table-column>
        <el-table-column prop="phone" label="手机号码" show-overflow-tooltip></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="stateText" label="状态"></el-table-column>
        <el-table-column prop="entryTime" label="入职时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="leaveTime" label="离职时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="roleName" label="角色"></el-table-column>
        <el-table-column label="操作" width="200">
          <!--这个scope就是代表当前循环的范围，scope.row就表示当前行使用的数据，这里表示一个user对象-->
          <template slot-scope="scope">
            <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
            <el-button type="text" size="small" @click="editUser(scope.row)" v-show="scope.row.username !== 'admin'">编辑</el-button>
            <el-button type="text" size="small" @click="dispatchRole(scope.row)" v-show="scope.row.username !== 'admin' && scope.row.state === 1">分配角色</el-button>
            <el-button type="text" size="small" @click="deleteUser([scope.row.username])" v-show="scope.row.username !== 'admin'">删除</el-button>
            <el-button type="text" size="small" @click="changeState([scope.row.username], 0)" v-show="scope.row.state === 1 && scope.row.username !== 'admin'">冻结</el-button>
            <el-button type="text" size="small" @click="changeState([scope.row.username], 1)" v-show="scope.row.state === 0 && scope.row.username !== 'admin'">解冻</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination small background :page-sizes="[10, 50, 80, 100]"
                     :current-page="currentPage"
                     :page-size="displayCount"
                     :total="total"
                     @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     layout="total, sizes, prev, pager, next" v-show="total > 0" align="right"></el-pagination>
      <!--引用组件UserDialog，被引用的组件称为子组件，当前组件称为子组件的父组件，
        子组件可以无条件的访问父组件中定义的数据和方法-->
      <user-dialog :dialog-data="dialogData"></user-dialog>
      <user-info :update-data="updateData"></user-info>
      <user-import-dialog :import-data="importData"></user-import-dialog>
      <dispatch-role-dialog :dialog-data="dispatchData"></dispatch-role-dialog>
    </div>
</template>

<script>
  import UserDialog from "./UserDialog";
  import UserInfo from "./UserInfo";
  import UserImportDialog from "./UserImportDialog";
  import DispatchRoleDialog from "./DispatchRoleDialog";
  export default {
    name: "User",
    components: {DispatchRoleDialog, UserImportDialog, UserInfo, UserDialog},
    data() {
      return {
        condition: {
          username: '',
          name: '',
          phone: '',
          state: ''
        },
        states: [{
          value: 0,
          text: '冻结'
        }, {
          value: 1,
          text: '正常'
        }],
        users: [],
        selectedUsers: [],
        currentPage: 1,
        displayCount: 10,
        total: 0,
        dialogData:{
          visible: false
        },
        updateData: {
          visible: false,
          user: {
            username: '',
            name: '',
            sex: '',
            age: '',
            phone: '',
            email: '',
            entryTime: '',
            leaveTime: ''
          }
        },
        importData: {
          visible: false
        },
        dispatchData: {
          visible: false,
          username: '',
          roleId: ''
        }
      }
    },
    methods:{
      search: function(){
        this.$get("/user", {
          username: this.condition.username,
          name: this.condition.name,
          phone: this.condition.phone,
          state: this.condition.state,
          currentPage: this.currentPage,
          displayCount: this.displayCount
        }, resp => {
          this.users = resp.data.list;
          this.total = resp.data.total;
        })
      },
      reset: function(){
        this.condition = {
            username: '',
            name: '',
            phone: '',
            state: ''
        }
        this.search()
      },
      showAddDialog: function(){
        this.dialogData.visible = true;
      },
      editUser: function (user) {
        Object.keys(user).forEach(key => {
          this.updateData.user[key] = user[key]
        })
        this.updateData.visible = true;
      },
      deleteUser: function(usernames){
        this.showConfirm("确定要删除该用户信息吗？", _ =>{
          this.$del("/user", {
            usernames: usernames
          }, resp => {
            if(resp.data === usernames.length){
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.search();
            } else {
              this.$message({
                message: '删除失败',
                type: 'error'
              })
            }
          })
        })
      },
      changeState: function(usernames, state){
        this.$put("/user/state",{
          usernames: usernames,
          state: state
        }, resp =>{
          if(resp.data === usernames.length){
            this.$message({
              message: state === 1 ? '解冻成功' : '冻结成功',
              type: 'success'
            })
            this.search();
          } else {
            this.$message({
              message: state === 1 ? '解冻失败' : '冻结失败',
              type: 'error'
            })
          }
        })
      },
      handleSelectionChange: function (val) {
        this.selectedUsers = val;
      },
      handleSizeChange: function(val) {
        this.displayCount = val;
        this.currentPage = 1;
        this.search();
      },
      handleCurrentChange: function (val) {
        this.currentPage = val;
        this.search();
      },
      handleCommand: function (command) {
        if(command === 'frozen'){
            this.changeState(this.selectedUsers.map(user=>user.username), 0);
        } else if(command === 'unfrozen'){
            this.changeState(this.selectedUsers.map(user=>user.username), 1);
        } else if(command === 'delete'){
            this.deleteUser(this.selectedUsers.map(user=>user.username))
        }
      },
      isBatchButtonDisabled: function () {
        if(this.selectedUsers.length === 0) return true;
        return this.selectedUsers.filter(user => user.username === 'admin').length > 0;
      },
      getExportUrl: function () {
        //?username=admin&password=123456
        let url = "/electronic/user/condition/export?";
        //Object.keys方法就是用来获取对象中的属性集合，forEach方法就是用来遍历集合的
        Object.keys(this.condition).forEach(key => url += key + "=" + this.condition[key] + "&");
        return url.substring(0, url.length - 1);
      },
      showImportDialog: function () {
        this.importData.visible = true;
      },
      dispatchRole: function (user) {
        this.dispatchData.username = user.username;
        this.dispatchData.roleId = user.roleId;
        this.dispatchData.visible = true;
      }
    },
    mounted() {//初始化页面后进行查询，需要再mounted函数中执行
      this.search();
    }
  }
</script>

<style scoped>
  .content-box{
    width: calc(100% - 10px);
    height: 100%;
  }
  .condition-box, .button-box{
    height: 40px;
    line-height: 40px;
  }
  .condition-box{
    color: #909399;
    font-size: 12px;
  }
  .el-pagination{
    height: 30px;
    padding-top: 10px;
  }
  .el-input, .el-select{
    width: 120px;
    margin-right: 10px;
  }
</style>
<style>
  .el-pagination .el-select .el-input > input{
    height: 22px;
  }

  .el-pagination > span{
    float: left;
  }
</style>
