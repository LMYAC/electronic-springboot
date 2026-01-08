<template>
  <el-dialog :title="dialogData.isUpdate ? '编辑巡检任务回执' : '新增巡检任务回执'" :visible.sync="dialogData.visible" width="45%"
             @close="closeDialog">
    <el-descriptions :column="dialogData.isUpdate ? 3 : 2" size="mini">
      <el-descriptions-item label="回执编号" v-if="dialogData.isUpdate">{{dialogData.reception.id}}</el-descriptions-item>
      <el-descriptions-item label="任务编号">{{dialogData.reception.taskId}}</el-descriptions-item>
      <el-descriptions-item label="任务名称">{{dialogData.reception.taskName}}</el-descriptions-item>
      <el-descriptions-item label="线路编号">{{dialogData.reception.lineId}}</el-descriptions-item>
      <el-descriptions-item label="线路名称">{{dialogData.reception.lineName}}</el-descriptions-item>
    </el-descriptions>
    <el-divider></el-divider>
    <el-form :model="dialogData.reception" :rules="receptionRules" :inline="true" ref="receptionForm">
      <el-form-item label="缺陷类型" prop="defectTypeId" label-width="80px">
        <el-select v-model="dialogData.reception.defectTypeId" size="mini" clearable>
          <el-option v-for="t in defectTypes" :label="t.name" :value="t.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="缺陷等级" prop="defectLevelId" label-width="80px">
        <el-select v-model="dialogData.reception.defectLevelId" size="mini" clearable>
          <el-option v-for="level in defectLevels" :label="level.name" :value="level.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="完好率" prop="rate" label-width="80px">
        <el-input v-model="dialogData.reception.rate" size="mini" clearable placeholder="请输入完好率"></el-input>
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
    name: "InspectionReceptionDialog",
    props: ["dialogData"],
    data() {
      return {
        reception: {
          defectTypeId: '',
          defectLevelId: '',
          rate: ''
        },
        defectTypes: [],
        defectLevels: [],
        receptionRules: {
          rate: [
            {required: true, message: '请输入完好率', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      addReception: function () {
        this.$refs['receptionForm'].validate(result => {
          if (result) {
            let method = this.dialogData.isUpdate ? "put" : "post";
            this.$ajax("/inspectionReception", method, {
              id: this.dialogData.reception.id,
              defectTypeId: this.dialogData.reception.defectTypeId,
              defectLevelId: this.dialogData.reception.defectLevelId,
              rate: this.dialogData.reception.rate,
              inspectionId: this.dialogData.reception.taskId
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
    },
    mounted() {
      this.$get('/defectData/defectType', null, resp => {
        this.defectTypes = resp.data;
      })
      this.$get('/defectData/defectLevel', null, resp => {
        this.defectLevels = resp.data;
      })
    }
  }
</script>

<style scoped>

</style>
