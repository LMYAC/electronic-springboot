<template>
  <div class="content-box">
    <div class="condition-box">
      <span>杆塔编号</span>
      <el-input v-model="condition.id" clearable placeholder="请输入杆塔编号" size="mini"></el-input>
      <span>启用状态</span>
      <el-select v-model="condition.state" placeholder="请选择启用状态" clearable size="mini">
        <el-option v-for="s in states" :key="s.value" :label="s.text":value="s.value"></el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="search">查询</el-button>
      <el-button type="default" icon="el-icon-refresh-left" size="mini" @click="reset">重置</el-button>
    </div>
    <div class="button-box">
      <el-button type="danger" icon="el-icon-circle-plus-outline" size="mini" @click="addTower">新增</el-button>
      <el-dropdown @command="handleCommand">
        <el-button type="primary" size="mini">
          批量操作<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <!--如果变量需要绑定一个值，而这个值又可以从定义的方法中的获取，那么此时可以直接调用这个方法获取值，调用的时候必须要在方法后面加上()-->
          <el-dropdown-item command="enabled" icon="el-icon-circle-check" :disabled="isBatchButtonDisabled()">启用</el-dropdown-item>
          <el-dropdown-item command="disabled" icon="el-icon-discover" :disabled="isBatchButtonDisabled()">禁用</el-dropdown-item>
          <el-dropdown-item command="delete" icon="el-icon-circle-close" :disabled="isBatchButtonDisabled()">删除</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-table :data="towers" border @selection-change="handleSelectionChange" style="width: 100%;" height="calc(100% - 120px)" size="mini">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="杆塔编号"></el-table-column>
      <el-table-column prop="orders" label="杆塔序号"></el-table-column>
      <el-table-column prop="position" label="杆塔位置"></el-table-column>
      <el-table-column prop="stateText" label="启用状态"></el-table-column>
      <el-table-column label="操作" width="200">
        <!--这个scope就是代表当前循环的范围，scope.row就表示当前行使用的数据，这里表示一个user对象-->
        <template slot-scope="scope">
          <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
          <el-button type="text" size="small" @click="editTower(scope.row)">编辑</el-button>
          <el-button type="text" size="small" @click="changeState([scope.row.id], 1)" v-show="scope.row.state === 0 ">启用</el-button>
          <el-button type="text" size="small" @click="changeState([scope.row.id], 0)" v-show="scope.row.state === 1">禁用</el-button>
          <el-button type="text" size="small" @click="deleteTower([scope.row.id], '确定要删除该杆塔信息吗？')">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination small background :page-sizes="[30, 50, 80, 100]"
                   :current-page="currentPage"
                   :page-size="displayCount"
                   :total="total"
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   layout="total, sizes, prev, pager, next" v-show="total > 0" align="right"></el-pagination>
    <tower-dialog :dialog-data="updateData" />
  </div>
</template>

<script>
  import TowerDialog from "./TowerDialog";
  export default {
    name: "Tower",
    components: {TowerDialog},
    data(){
      return {
        condition: {
          id: '',
          state: ''
        },
        states: [{
          value: 1,
          text: '启用'
        }, {
          value: 0,
          text: '禁用'
        }],
        towers: [],
        selectedTowers: [],
        currentPage: 1,
        displayCount: 30,
        total: 0,
        updateData: {
          tower: {
            id: '',
            orders: '',
            position: '',
            state: '',
            stateText: ''
          },
          title: '',
          visible: false,
          isUpdate: false
        }
      }
    },
    methods:{
      search: function(){
        this.$get("/tower", {
          id: this.condition.id,
          state: this.condition.state,
          currentPage: this.currentPage,
          displayCount: this.displayCount
        }, resp => {
          this.towers = resp.data.list;
          this.total = resp.data.total;
        })
      },
      reset: function(){
        this.condition = {
          id: '',
          state: ''
        }
        this.search()
      },
      addTower: function(){
        this.updateData.isUpdate = false;
        this.updateData.title = '新增杆塔';
        this.updateData.visible = true;
      },
      editTower: function(tower){
        Object.keys(tower).forEach(key => {
          this.updateData.tower[key] = tower[key]
        })
        this.updateData.isUpdate = true;
        this.updateData.title = '编辑杆塔';
        this.updateData.visible = true;
      },
      deleteTower: function(towerIds, tip){
        this.showConfirm(tip, _=>{
          this.$del("/tower",{
            towerIds: towerIds
          }, resp =>{
            if(resp.data === towerIds.length){
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.search()
            } else if(resp.data === 0) {
              this.$message({
                message: '删除失败',
                type: 'error'
              })
            } else {
              this.$message({
                message: '部分杆塔仍在线路中使用，删除失败',
                type: 'success'
              })
              this.search()
            }
          });
        })
      },
      changeState: function(towerIds, state){
        this.$put("/tower",{
          towerIds: towerIds,
          state: state
        }, resp =>{
          if(resp.data === towerIds.length){
            this.$message({
              message: state === 1 ? '启用成功' : '禁用成功',
              type: 'success'
            })
            this.search();
          } else {
            this.$message({
              message: state === 1 ? '启用失败' : '禁用失败',
              type: 'error'
            })
          }
        })
      },
      handleSelectionChange: function (val) {
        this.selectedTowers = val;
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
          this.changeState(this.selectedTowers.map(t=>t.id), 1);
        } else if(command === 'disabled'){
          this.changeState(this.selectedTowers.map(t=>t.id), 0);
        } else if(command === 'delete'){
          this.deleteTower(this.selectedTowers.map(t=>t.id), "确定要删除选择杆塔信息吗？");
        }
      },
      isBatchButtonDisabled: function () {
        if(this.selectedTowers.length === 0) return true;
        return false;
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
