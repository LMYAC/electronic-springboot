<template>
  <div class="content-box">
    <div class="condition-box">
      <span>角色名</span>
      <el-input v-model="condition.name" clearable placeholder="请输入角色名" size="mini"></el-input>
      <span>启用状态</span>
      <el-select v-model="condition.status" placeholder="请选择启用状态" clearable size="mini">
        <el-option v-for="s in enableStatus" :key="s.value" :label="s.text":value="s.value"></el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="search">查询</el-button>
      <el-button type="default" icon="el-icon-refresh-left" size="mini" @click="reset">重置</el-button>
    </div>
    <div class="button-box">
      <el-dropdown @command="handleCommand">
        <el-button type="primary" size="mini">
          批量操作<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <!--如果变量需要绑定一个值，而这个值又可以从定义的方法中的获取，那么此时可以直接调用这个方法获取值，调用的时候必须要在方法后面加上()-->
          <el-dropdown-item command="enabled" icon="el-icon-circle-check" :disabled="isBatchButtonDisabled()">启用</el-dropdown-item>
          <el-dropdown-item command="disabled" icon="el-icon-discover" :disabled="isBatchButtonDisabled()">禁用</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-table :data="roles" border @selection-change="handleSelectionChange" style="width: 100%;" height="calc(100% - 120px)" size="mini">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="角色编号"></el-table-column>
      <el-table-column prop="name" label="角色名称"></el-table-column>
      <el-table-column prop="enName" label="英文名称"></el-table-column>
      <el-table-column prop="enableStatusText" label="启用状态"></el-table-column>
      <el-table-column label="操作" width="200">
        <!--这个scope就是代表当前循环的范围，scope.row就表示当前行使用的数据，这里表示一个user对象-->
        <template slot-scope="scope">
          <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
          <el-button type="text" size="small" @click="editRole(scope.row)" v-show="scope.row.id !== 1">编辑</el-button>
          <el-button type="text" size="small" @click="changeState([scope.row.id], 1)" v-show="scope.row.enableStatus === 0 && scope.row.id !== 1">启用</el-button>
          <el-button type="text" size="small" @click="changeState([scope.row.id], 0)" v-show="scope.row.enableStatus === 1 && scope.row.id !== 1">禁用</el-button>
        </template>
      </el-table-column>
    </el-table>
    <role-dialog :dialog-data="updateData" />
  </div>
</template>

<script>
    import RoleDialog from "./RoleDialog";
    export default {
        name: "Role",
      components: {RoleDialog},
      data(){
          return {
            condition:{
              name: '',
              status: ''
            },
            enableStatus: [{
              value: 1,
              text: '启用'
            },{
              value: 0,
              text: '禁用'
            }],
            roles: [],
            selectedRoles: [],
            updateData: {
              role: {
                id: '',
                name: '',
                enName: '',
                enableStatus: ''
              },
              visible: false
            }
          }
      },
      methods:{
        search: function(){
          this.$get("/role", {
            name: this.condition.name,
            status: this.condition.status
          }, resp => {
            this.roles = resp.data;
          })
        },
        reset: function(){
          this.condition = {
            name: '',
            status: ''
          }
          this.search()
        },
        editRole: function (role) {
          Object.keys(role).forEach(key => {
            this.updateData.role[key] = role[key]
          })
          this.updateData.visible = true;
        },
        changeState: function(roleIds, status){
          this.$put("/role",{
            roleIds: roleIds,
            enableStatus: status
          }, resp =>{
            if(resp.data === roleIds.length){
              this.$message({
                message: status === 1 ? '启用成功' : '禁用成功',
                type: 'success'
              })
              this.search();
            } else {
              this.$message({
                message: status === 1 ? '启用失败' : '禁用失败',
                type: 'error'
              })
            }
          })
        },
        handleSelectionChange: function (val) {
          this.selectedRoles = val;
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
          if(command === 'enabled'){
            this.changeState(this.selectedRoles.map(role=>role.id), 1);
          } else if(command === 'disabled'){
            this.changeState(this.selectedRoles.map(role=>role.id), 0);
          }
        },
        isBatchButtonDisabled: function () {
          if(this.selectedRoles.length === 0) return true;
          return this.selectedRoles.filter(role => role.id === 1).length > 0;
        }
      },
      mounted() {
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
