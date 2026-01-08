<template>
  <el-dialog title="巡检回执审核" :visible.sync="dialogData.visible" width="50%" @close="closeDialog">
    <el-descriptions>
      <el-descriptions-item label="回执编号">{{dialogData.data.id}}</el-descriptions-item>
      <el-descriptions-item label="任务编号">{{dialogData.data.taskId}}</el-descriptions-item>
      <el-descriptions-item label="任务名称">{{dialogData.data.taskName}}</el-descriptions-item>
      <el-descriptions-item label="线路编号">{{dialogData.data.lineId}}</el-descriptions-item>
      <el-descriptions-item label="线路名称">{{dialogData.data.lineName}}</el-descriptions-item>
      <el-descriptions-item label="缺陷类型">{{dialogData.data.defectTypeName}}</el-descriptions-item>
      <el-descriptions-item label="缺陷等级">{{dialogData.data.defectLevelName}}</el-descriptions-item>
      <el-descriptions-item label="完好率">{{dialogData.data.rate}}</el-descriptions-item>
      <el-descriptions-item label="操作员">{{dialogData.data.operationUserName}}</el-descriptions-item>
    </el-descriptions>
    <el-divider></el-divider>
    <el-form :model="dialogData.data" :rules="dataRules" :inline="true" ref="auditForm">
      <el-form-item label="审核状态" prop="state" label-width="80px">
        <el-select v-model="dialogData.data.state" size="mini" placeholder="请选择审核状态" clearable>
          <el-option v-for="s in states" :label="s.name" :value="s.value"></el-option>
        </el-select>
      </el-form-item>
      <div v-show="dialogData.data.state === 2">
        <el-form-item label="任务名称" prop="defectTaskName" label-width="80px">
          <el-input v-model="dialogData.data.defectTaskName" size="mini" clearable placeholder="请输入消缺任务名称"></el-input>
        </el-form-item>
        <el-form-item label="任务描述" prop="defectTaskDesc" label-width="80px">
          <el-input v-model="dialogData.data.defectTaskDesc" size="mini" clearable placeholder="请输入消缺任务描述"></el-input>
        </el-form-item>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="saveAudit" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "InspectionReceptionAuditDialog",
    props: ['dialogData'],
    data() {
      return {
        states: [{
          name: '审核通过',
          value: 2
        }, {
          name: '驳回',
          value: 3
        }],
        dataRules: {
          state: [
            {required: true, message: '请选择审核状态', trigger: 'blur'}
          ],
          defectTaskName: [
            {validator: this.checkTaskName, trigger: 'blur'}
          ],
          defectTaskDesc: [
            {validator: this.checkTaskDesc,trigger: 'blur'}
          ]
        }
      }
    },
    methods:{
      checkTaskName: function(rule, value, callback){
        if(this.dialogData.data.state === 2){
          if(value === ''){
            callback(new Error("请输入消缺任务名称"));
            return;
          }
        }
        callback();
      },
      checkTaskDesc: function(rule, value, callback){
        if(this.dialogData.data.state === 2){
          if(value === ''){
            callback(new Error("请输入消缺任务描述"));
            return;
          }
        }
        callback();
      },
      saveAudit: function(){
        this.$refs['auditForm'].validate(result => {
          if(result){
            let data = {
              id: this.dialogData.data.id,
              state: this.dialogData.data.state
            }
            if(this.dialogData.data.state === 2){
              data = {
                id: this.dialogData.data.id,
                state: this.dialogData.data.state,
                defectTaskName: this.dialogData.data.defectTaskName,
                defectTaskDesc: this.dialogData.data.defectTaskDesc
              }
            }
            this.$put("/inspectionReception", data, resp=>{
              if(resp.data === 1){
                this.$message({
                  message: this.dialogData.data.state === 2 ? '审核成功' : '驳回成功',
                  type: 'success'
                })
                this.closeDialog();
                this.$parent.search()
              } else {
                this.$message({
                  message: this.dialogData.data.state === 2 ? '审核失败' : '驳回失败',
                  type: 'error'
                })
              }
            })
          } else {
            return false;
          }
        });
      },
      closeDialog: function () {
        this.dialogData.data = {
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
        }
        this.$refs['auditForm'].resetFields();
        this.dialogData.visible = false;
      }
    }
  }
</script>

<style scoped>

</style>
