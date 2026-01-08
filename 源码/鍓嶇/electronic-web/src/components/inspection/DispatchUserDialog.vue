<template>
  <el-dialog :title="dialogData.title" :visible.sync="dialogData.visible" width="45%" @close="closeDialog">
    <el-form :model="dialogData.dispatchData" :rules="dispatchDataRules" :inline="true" ref="dispatchForm">
      <el-form-item label="任务编号" label-width="80px">
        <el-input v-model="dialogData.dispatchData.taskId" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="操作员" prop="operationUser" label-width="80px">
        <el-autocomplete v-model="dialogData.dispatchData.operationUser" size="mini"
                         :fetch-suggestions="getInspectionUsers"
                         placeholder="请输入操作员信息" @select="handleSelect"></el-autocomplete>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="saveDispatch" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "DispatchUserDialog",
    props: ['dialogData'],
    data() {
      return {
        dispatchDataRules:{
          operationUser:[
            {required: true, message: '请输入操作员', trigger: 'change'}
          ]
        }
      }
    },
    methods:{
      saveDispatch: function () {
        this.$refs['dispatchForm'].validate(result =>{
          if(result){
            let url = this.dialogData.roleId === 3 ? '/inspectionTask' : this.dialogData.roleId === 4 ? "/defectTask" : "";
            this.$put(url,{
              id: this.dialogData.dispatchData.taskId,
              operationUser: this.dialogData.dispatchData.operationUser,
            }, resp=>{
              if(resp.data === 1){
                this.$message({
                  message: '分配成功',
                  type: 'success'
                })
                this.closeDialog();
                this.$parent.search();
              } else {
                this.$message({
                  message: '分配失败',
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
        this.dialogData.dispatchData = {
          taskId: '',
          operationUser: ''
        }
        this.$refs['dispatchForm'].resetFields();
        this.dialogData.title = '';
        this.dialogData.visible = false;
      },
      getInspectionUsers: function (content, callback) {
        this.$get("/user/item/query",{
          queryString: content,
          state: 1,
          roleId: this.dialogData.roleId,
          currentPage: 1,
          displayCount: 30
        }, resp=>{
          callback(resp.data.list.map(u =>  {
            return {
              value: u.username + "(" + u.name + ")",
              id: u.username
            }
          }))
        })
      },
      handleSelect: function(item){
        this.dialogData.dispatchData.operationUser = item.id;
      }
    }
  }
</script>

<style scoped>

</style>
