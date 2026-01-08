<template>
  <el-dialog :title="dialogData.title" :visible.sync="dialogData.visible" width="30%" @close="closeDialog">
    <el-form :model="dialogData.data" :rules="defectDataRules" :inline="true" ref="defectDataForm">
      <el-form-item :label="dialogData.labelPrefix + '编号'" label-width="110px" v-show="dialogData.flag % 2 === 0">
        <el-input v-model="dialogData.data.id" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item :label="dialogData.labelPrefix + '名称'" prop="name" label-width="110px">
        <el-input v-model="dialogData.data.name" size="mini" clearable></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="saveDefectData" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "DefectDialog",
    props: ['dialogData'],
    data(){
      return {
        defectDataRules: {
          name: [
            { validator: this.checkName, trigger: 'blur'}
          ]
        }
      }
    },
    methods:{
      checkName: function(rule, value, callback){
        if(value === undefined || value.trim() === ''){
          callback(new Error('请填写' + this.dialogData.labelPrefix + '名称'))
        } else {
          callback();
        }
      },
      saveDefectData: function(){
        this.$refs['defectDataForm'].validate(result=>{
          if(result){
            let data = {
              name: this.dialogData.data.name
            }
            let method = 'post';
            if(this.dialogData.flag % 2 === 0){ //修改
              data = {
                id: this.dialogData.data.id,
                name: this.dialogData.data.name
              }
              method = 'put';
            }
            let url = '/defectData/defectType';
            if(this.dialogData.flag > 2){
              url = '/defectData/defectLevel'
            }
            this.$ajax(url, method, data, resp=>{
              if(resp.data === 1){
                this.$message({
                  message: '保存成功',
                  type: 'success'
                })
                let flag = this.dialogData.flag;
                this.closeDialog();
                this.$parent.search(flag > 2 ? 1 : 0);
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
        this.dialogData.title = '';
        this.dialogData.flag = '';
        this.dialogData.labelPrefix = '';
        this.dialogData.data.id = '';
        this.dialogData.data.name = '';
        this.$refs['defectDataForm'].resetFields();
        this.dialogData.visible = false;
      }
    }
  }
</script>

<style scoped>

</style>
