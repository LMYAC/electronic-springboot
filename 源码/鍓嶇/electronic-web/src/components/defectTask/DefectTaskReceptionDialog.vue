<template>
  <el-dialog :title="dialogData.isUpdate ? '编辑消缺任务回执' : '新增消缺任务回执'" :visible.sync="dialogData.visible"
             width="60%" @close="closeDialog">
    <el-descriptions size="mini">
      <el-descriptions-item label="回执编号" v-if="dialogData.isUpdate">{{dialogData.reception.id}}</el-descriptions-item>
      <el-descriptions-item label="任务编号">{{dialogData.reception.taskId}}</el-descriptions-item>
      <el-descriptions-item label="任务名称">{{dialogData.reception.taskName}}</el-descriptions-item>
      <el-descriptions-item label="线路编号">{{dialogData.reception.lineId}}</el-descriptions-item>
      <el-descriptions-item label="线路名称">{{dialogData.reception.lineName}}</el-descriptions-item>
      <el-descriptions-item label="任务描述">{{dialogData.reception.description}}</el-descriptions-item>
      <el-descriptions-item label="消缺员">{{dialogData.reception.defectUserName + "(" + dialogData.reception.defectUser +")"}}</el-descriptions-item>
    </el-descriptions>
    <el-divider></el-divider>
    <el-form :model="dialogData.reception" :rules="receptionRules" ref="receptionForm">
      <el-form-item label="完成情况" prop="finishInfo" label-width="80px">
        <el-input type="textarea" v-model="dialogData.reception.finishInfo" placeholder="请输入完成情况" resize="none"></el-input>
      </el-form-item>
      <el-form-item label="延期记录" label-width="80px">
        <el-input type="textarea" v-model="dialogData.reception.pauseRecord" placeholder="请输入延期记录" resize="none"></el-input>
      </el-form-item>
      <el-form-item label="终结报告" prop="report" label-width="80px">
        <el-input type="textarea" v-model="dialogData.reception.report" placeholder="请输入终结报告" resize="none"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="addReception" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "DefectTaskReceptionDialog",
    props: ['dialogData'],
    data() {
      return {
        receptionRules: {
          finishInfo: [
            {required: true, message: '请输入完成情况', trigger: 'blur'}
          ],
          report: [
            {required: true, message: '请输入终结报告', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      addReception: function () {
        this.$refs['receptionForm'].validate(result => {
          if (result) {
            let method = this.dialogData.isUpdate ? "put" : "post";
            this.$ajax("/defectReception", method, {
              id: this.dialogData.reception.id,
              finishInfo: this.dialogData.reception.finishInfo,
              pauseRecord: this.dialogData.reception.pauseRecord,
              report: this.dialogData.reception.report,
              taskId: this.dialogData.reception.taskId
            }, resp => {
              if (resp.data === 1) {
                this.$message({
                  message: '保存成功',
                  type: 'success'
                })
                this.closeDialog();
                this.$parent.search();
              } else {
                this.$message({
                  message: '保存失败',
                  type: 'error'
                })
              }
            })
          } else {
            return false;
          }
        })
      },
      closeDialog: function () {
        this.dialogData.reception = {
          lineId: '',
          lineName: '',
          taskId: '',
          taskName: '',
          defectTypeId: '',
          defectLevelId: '',
          rate: ''
        }
        this.dialogData.isUpdate = false
        this.dialogData.visible = false;
      }
    }
  }
</script>

<style scoped>
.el-divider{
  margin: 10px 0;
}
</style>
