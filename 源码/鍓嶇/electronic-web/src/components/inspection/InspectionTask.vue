<template>
  <div class="content-box">
    <div class="condition-box">
      <span>线路编号</span>
      <el-input v-model="condition.lineId" clearable placeholder="请输入线路编号" size="mini"></el-input>
      <span>线路名称</span>
      <el-input v-model="condition.lineName" clearable placeholder="请输入线路名称" size="mini"></el-input>
      <span>巡检员</span>
      <el-input v-model="condition.user" clearable placeholder="请输入巡检员" size="mini"></el-input>
      <span>任务状态</span>
      <el-select v-model="condition.state" placeholder="请选择任务状态" clearable size="mini">
        <el-option v-for="s in states" :key="s.value" :label="s.text" :value="s.value"></el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="search">查询</el-button>
      <el-button type="default" icon="el-icon-refresh-left" size="mini" @click="reset">重置</el-button>
    </div>
    <el-table :data="inspectionTasks" border @selection-change="handleSelectionChange" style="width: 100%;"
              height="calc(100% - 80px)" size="mini">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="任务编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="name" label="任务名称"></el-table-column>
      <el-table-column prop="lineId" label="线路编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="lineName" label="线路名称"></el-table-column>
      <el-table-column prop="startNumber" label="起始杆号"></el-table-column>
      <el-table-column prop="endNumber" label="结束杆号"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column prop="inspectionUserName" label="巡检员"></el-table-column>
      <el-table-column prop="stateText" label="任务状态"></el-table-column>
      <el-table-column label="操作" width="240">
        <!--这个scope就是代表当前循环的范围，scope.row就表示当前行使用的数据，这里表示一个user对象-->
        <template slot-scope="scope">
          <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
          <el-button type="text" size="small" @click="editTask(scope.row)" v-show="scope.row.state === 0">编辑</el-button>
          <el-button type="text" size="small" @click="dispatch(scope.row)" v-show="scope.row.state === 0">分配</el-button>
          <el-button type="text" size="small" @click="changeState(scope.row.id)" v-show="scope.row.state === 1">执行</el-button>
          <el-button type="text" size="small" @click="addReception(scope.row)" v-show="scope.row.state === 2">添加回执</el-button>
          <el-button type="text" size="small" @click="deleteTask(scope.row.id, '确定要删除该巡检任务吗？')" v-show="scope.row.state < 2">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination small background :page-sizes="[30, 50, 80, 100]"
                   :current-page="currentPage" :page-size="displayCount" :total="total"
                   @size-change="handleSizeChange" @current-change="handleCurrentChange"
                   layout="total, sizes, prev, pager, next" v-show="total > 0" align="right"></el-pagination>
    <inspection-task-dialog :dialog-data="inspectionData" />
    <dispatch-user-dialog :dialog-data="dispatchDialogData" />
    <inspection-reception-dialog :dialog-data="receptionData" />
  </div>
</template>

<script>
  import InspectionReceptionDialog from "./InspectionReceptionDialog";
  import InspectionTaskDialog from "../line/InspectionTaskDialog";
  import DispatchUserDialog from "./DispatchUserDialog";
  export default {
    name: "InspectionTask",
    components: {DispatchUserDialog, InspectionTaskDialog, InspectionReceptionDialog},
    data() {
      return {
        condition: {
          lineId: '',
          lineName: '',
          user: '',
          state: ''
        },
        states: [],
        inspectionTasks: [],
        selectedInspectionTasks: [],
        currentPage: 1,
        displayCount: 30,
        total: 0,
        receptionData: {
          reception:{
            lineId: '',
            lineName: '',
            taskId: '',
            taskName: '',
            defectTypeId: '',
            defectLevelId: '',
            rate: ''
          },
          visible: false,
          isUpdate: false
        },
        inspectionData: {
          inspectionTask: {
            id: '',
            name: '',
            lineId: '',
            lineName: '',
            startNumber: '',
            endNumber: '',
            remark: '',
            inspectionUser: '',
            inspectionUserName: '',
            state: '',
            stateText: ''
          },
          title: '编辑巡检任务',
          visible: false,
          isUpdate: true
        },
        dispatchDialogData: {
          dispatchData: {
            taskId: '',
            operationUser: ''
          },
          title: '巡检员分配',
          roleId: 3,
          visible: false
        }
      }
    },
    methods: {
      search: function () {
        this.$get("/inspectionTask", {
          lineId: this.condition.lineId,
          lineName: this.condition.lineName,
          user: this.condition.user,
          state: this.condition.state,
          currentPage: this.currentPage,
          displayCount: this.displayCount
        }, resp=>{
          this.inspectionTasks = resp.data.list;
          this.total = resp.data.total;
        })
      },
      reset: function () {
        this.condition = {
          lineId: '',
          lineName: '',
          user: '',
          state: ''
        }
        this.search()
      },
      editTask: function(inspectionTask){
        Object.keys(inspectionTask).forEach(key => {
          this.inspectionData.inspectionTask[key] = inspectionTask[key]
        })
        this.inspectionData.visible = true;
      },
      dispatch: function(inspectionTask){
        this.dispatchDialogData.dispatchData.taskId = inspectionTask.id;
        this.dispatchDialogData.visible = true;
      },
      changeState: function(id){
        this.$put("/inspectionTask",{
          id: id,
          state: 2
        }, resp=>{
          if(resp.data === 1){
            this.$message({
              message: '执行成功',
              type: 'success'
            })
            this.search();
          } else {
            this.$message({
              message: '执行失败',
              type: 'error'
            })
          }
        })
      },
      deleteTask: function(id, tip){
        this.showConfirm(tip, _=>{
          this.$del("/inspectionTask/" + id,null, resp =>{
            if(resp.data === 1){
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.search()
            } else {
              this.$message({
                message: '删除失败',
                type: 'error'
              })
            }
          });
        })
      },
      addReception: function(task){
        this.receptionData.reception.lineId = task.lineId;
        this.receptionData.reception.lineName = task.lineName;
        this.receptionData.reception.taskId = task.id;
        this.receptionData.reception.taskName = task.name;
        this.receptionData.visible = true;
      },
      handleSelectionChange: function (val) {
        this.selectedInspectionTasks = val;
      },
      handleSizeChange: function(val) {
        this.displayCount = val;
        this.currentPage = 1;
        this.search();
      },
      handleCurrentChange: function (val) {
        this.currentPage = val;
        this.search();
      }
    },
    mounted() {
      this.$get("/item/task_state", null, resp =>{
        this.states = resp.data;
      })
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
