<template>
  <div class="content-box">
    <div class="condition-box">
      <span>线路编号</span>
      <el-input v-model="condition.lineId" clearable placeholder="请输入线路编号" size="mini"></el-input>
      <span>线路名称</span>
      <el-input v-model="condition.lineName" clearable placeholder="请输入线路名称" size="mini"></el-input>
      <span>巡检员</span>
      <el-input v-model="condition.user" clearable placeholder="请输入巡检员" size="mini"></el-input>
      <span>回执状态</span>
      <el-select v-model="condition.state" placeholder="请选择回执状态" clearable size="mini">
        <el-option v-for="s in states" :key="s.value" :label="s.text" :value="s.value"></el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="search">查询</el-button>
      <el-button type="default" icon="el-icon-refresh-left" size="mini" @click="reset">重置</el-button>
    </div>
    <el-table :data="receptions" border style="width: 100%;"  height="calc(100% - 80px)" size="mini">
      <el-table-column prop="id" label="回执编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="taskId" label="任务编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="taskName" label="任务名称"></el-table-column>
      <el-table-column prop="lineId" label="线路编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="lineName" label="线路名称"></el-table-column>
      <el-table-column prop="defectTypeName" label="缺陷类型"></el-table-column>
      <el-table-column prop="defectLevelName" label="缺陷等级"></el-table-column>
      <el-table-column prop="rate" label="完好率"></el-table-column>
      <el-table-column prop="operationUserName" label="操作员"></el-table-column>
      <el-table-column prop="stateText" label="回执状态"></el-table-column>
      <el-table-column label="操作" width="240">
        <!--这个scope就是代表当前循环的范围，scope.row就表示当前行使用的数据，这里表示一个user对象-->
        <template slot-scope="scope">
          <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
          <el-button type="text" size="small" @click="editReception(scope.row)" v-show="checkButtonState(scope.row)">编辑</el-button>
<!--          <el-button type="text" size="small" @click="changeState(scope.row)" v-show="scope.row.state === 0">提交</el-button>-->
          <el-button type="text" size="small" @click="changeState(scope.row)" v-show="scope.row.state === 1">审核</el-button>
          <el-button type="text" size="small" @click="deleteReception(scope.row.id, '确定要删除该回执信息吗？')" v-show="checkButtonState(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination small background :page-sizes="[30, 50, 80, 100]"
                   :current-page="currentPage" :page-size="displayCount" :total="total"
                   @size-change="handleSizeChange" @current-change="handleCurrentChange"
                   layout="total, sizes, prev, pager, next" v-show="total > 0" align="right"></el-pagination>
    <inspection-reception-dialog :dialog-data="receptionData" />
    <inspection-reception-audit-dialog :dialog-data="auditData" />
  </div>
</template>

<script>
  import InspectionReceptionDialog from "../inspection/InspectionReceptionDialog";
  import InspectionReceptionAuditDialog from "./InspectionReceptionAuditDialog";
  export default {
    name: "InspectionReception",
    components: {InspectionReceptionAuditDialog, InspectionReceptionDialog},
    data() {
      return {
        condition: {
          lineId: '',
          lineName: '',
          user: '',
          state: ''
        },
        states: [],
        receptions: [],
        currentPage: 1,
        displayCount: 30,
        total: 0,
        receptionData: {
          reception:{
            id: '',
            lineId: '',
            lineName: '',
            taskId: '',
            taskName: '',
            defectTypeId: '',
            defectLevelId: '',
            rate: ''
          },
          visible: false,
          isUpdate: true
        },
        auditData:{
          data: {
            id: '',
            lineId: '',
            lineName: '',
            taskId: '',
            taskName: '',
            defectTypeName: '',
            defectLevelName: '',
            operationUserName: '',
            rate: '',
            defectTaskName: '',
            defectTaskDesc: '',
            state: ''
          },
          visible: false
        }
      }
    },
    methods: {
      search: function(){
        this.$get("/inspectionReception", {
          lineId: this.condition.lineId,
          lineName: this.condition.lineName,
          user: this.condition.user,
          state: this.condition.state,
          currentPage: this.currentPage,
          displayCount: this.displayCount
        }, resp=>{
          this.receptions = resp.data.list;
          this.total = resp.data.total;
        })
      },
      reset: function(){
        this.condition = {
          lineId: '',
          lineName: '',
          user: '',
          state: ''
        }
        this.search()
      },
      editReception: function(reception){
        Object.keys(reception).forEach(key => {
          this.receptionData.reception[key] = reception[key]
        })
        this.receptionData.isUpdate = true;
        this.receptionData.visible = true;
      },
      changeState: function(reception){
        Object.keys(reception).forEach(key => {
          if(key !== 'state')
            this.auditData.data[key] = reception[key]
        })
        this.auditData.visible = true;
      },
      deleteReception: function(id, tip){
        this.showConfirm(tip, _=>{
          this.$del("/inspectionReception/" + id, null, resp =>{
            if(resp.data === 1){
              this.$message({
                message:'删除成功',
                type: 'success'
              })
              this.search()
            } else {
              this.$message({
                message: '删除失败',
                type: 'error'
              })
            }
          })
        })
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
      checkButtonState: function (reception) {
        return reception.state === 0 && this.$parent.currentUser.roleId === 3
      }
    },
    mounted() {
      this.$get("/item/reception_state", null, resp =>{
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
