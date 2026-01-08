<template>
  <div class="content-box">
    <div class="condition-box">
      <span>线路编号</span>
      <el-input v-model="condition.lineId" clearable placeholder="请输入线路编号" size="mini"></el-input>
      <span>线路名称</span>
      <el-input v-model="condition.lineName" clearable placeholder="请输入线路名称" size="mini"></el-input>
      <span>消缺员</span>
      <el-input v-model="condition.user" clearable placeholder="请输入消缺员" size="mini"></el-input>
      <span>任务状态</span>
      <el-select v-model="condition.state" placeholder="请选择任务状态" clearable size="mini">
        <el-option v-for="s in states" :key="s.value" :label="s.text" :value="s.value"></el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="search">查询</el-button>
      <el-button type="default" icon="el-icon-refresh-left" size="mini" @click="reset">重置</el-button>
    </div>
    <el-table :data="defectTasks" border style="width: 100%;" height="calc(100% - 80px)" size="mini">
      <el-table-column prop="id" label="任务编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="name" label="任务名称"></el-table-column>
      <el-table-column prop="lineId" label="线路编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="lineName" label="线路名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column prop="defectUserName" label="消缺员"></el-table-column>
      <el-table-column prop="stateText" label="任务状态"></el-table-column>
      <el-table-column label="操作" width="240">
        <!--这个scope就是代表当前循环的范围，scope.row就表示当前行使用的数据，这里表示一个user对象-->
        <template slot-scope="scope">
          <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
          <el-button type="text" size="small" @click="dispatch(scope.row)" v-show="scope.row.state === 0">分配</el-button>
          <el-button type="text" size="small" @click="changeState(scope.row.id)" v-show="scope.row.state === 1">执行
          </el-button>
          <el-button type="text" size="small" @click="addReception(scope.row)" v-show="scope.row.state === 2">添加回执
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination small background :page-sizes="[30, 50, 80, 100]"
                   :current-page="currentPage" :page-size="displayCount" :total="total"
                   @size-change="handleSizeChange" @current-change="handleCurrentChange"
                   layout="total, sizes, prev, pager, next" v-show="total > 0" align="right"></el-pagination>
    <dispatch-user-dialog :dialog-data="dispatchDialogData"/>
    <defect-task-reception-dialog :dialog-data="receptionData" />
  </div>
</template>

<script>
  import DispatchUserDialog from "../inspection/DispatchUserDialog";
  import DefectTaskReceptionDialog from "./DefectTaskReceptionDialog";

  export default {
    name: "DefectTask",
    components: {DefectTaskReceptionDialog, DispatchUserDialog},
    data() {
      return {
        condition: {
          lineId: '',
          lineName: '',
          user: '',
          state: ''
        },
        states: [],
        defectTasks: [],
        selectedDefectTasks: [],
        currentPage: 1,
        displayCount: 30,
        total: 0,
        defectDialogData: {
          defectTask: {},
          visible: false
        },
        dispatchDialogData: {
          dispatchData: {
            taskId: '',
            operationUser: ''
          },
          title: '消缺员分配',
          roleId: 4,
          visible: false
        },
        receptionData: {
          reception: {
            lineId: '',
            lineName: '',
            taskId: '',
            taskName: '',
            description: '',
            defectUser: '',
            defectUserName: '',
            finishInfo: '',
            pauseRecord: '',
            report: ''
          },
          isUpdate: false,
          visible: false
        }
      }
    },
    methods: {
      search: function () {
        this.$get("/defectTask", {
          lineId: this.condition.lineId,
          lineName: this.condition.lineName,
          user: this.condition.user,
          state: this.condition.state,
          currentPage: this.currentPage,
          displayCount: this.displayCount
        }, resp => {
          this.defectTasks = resp.data.list;
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
      editTask: function (task) {
        Object.keys(task).forEach(key => {
          this.defectDialogData.defectTask[key] = task[key]
        })
        this.defectDialogData.visible = true;
      },
      dispatch: function (task) {
        this.dispatchDialogData.dispatchData.taskId = task.id;
        this.dispatchDialogData.visible = true;
      },
      changeState: function (id) {
        this.$put("/defectTask", {
          id: id,
          state: 2
        }, resp => {
          if (resp.data === 1) {
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
      deleteTask: function (id, tip) {
        this.showConfirm(tip, _ => {
          this.$del("/defectTask/" + id, null, resp => {
            if (resp.data === 1) {
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
      addReception: function (task) {
        this.receptionData.reception.lineId = task.lineId;
        this.receptionData.reception.lineName = task.lineName;
        this.receptionData.reception.taskId = task.id;
        this.receptionData.reception.taskName = task.name;
        this.receptionData.reception.description = task.description;
        this.receptionData.reception.defectUser = task.defectUser;
        this.receptionData.reception.defectUserName = task.defectUserName;
        this.receptionData.visible = true;
      },
      handleSizeChange: function (val) {
        this.displayCount = val;
        this.currentPage = 1;
        this.search();
      },
      handleCurrentChange: function (val) {
        this.currentPage = val;
        this.search();
      },
    },
    mounted() {
      this.$get("/item/task_state", null, resp => {
        this.states = resp.data;
      })
      this.search();
    }
  }
</script>

<style scoped>
  .content-box {
    width: calc(100% - 10px);
    height: 100%;
  }

  .condition-box, .button-box {
    height: 40px;
    line-height: 40px;
  }

  .condition-box {
    color: #909399;
    font-size: 12px;
  }

  .el-pagination {
    height: 30px;
    padding-top: 10px;
  }

  .el-input, .el-select {
    width: 120px;
    margin-right: 10px;
  }
</style>
