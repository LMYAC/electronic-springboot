<template>
  <el-dialog title="分配角色" :visible.sync="dialogData.visible"
             width="45%" @close="closeDialog">
    <el-form :model="dialogData" :rules="formRules" :inline="true" ref="dispatchForm">
      <el-form-item label="账号" label-width="80px">
        <el-input v-model="dialogData.username" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="roleId" label-width="80px">
        <el-select v-model="dialogData.roleId" size="mini" clearable placeholder="请选择角色">
          <el-option v-for="r in roles" :key="r.id" :label="r.name" :value="r.id"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="dispatchRole" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
    export default {
        name: "DispatchRoleDialog",
      props: ['dialogData'],
      data(){
        return {
          roles: [],
          formRules:{
            roleId: [
              {required: true, message: '请选择授予的角色', trigger: 'blur'}
            ]
          }
        }
      },
      methods:{
        dispatchRole:function(){
          this.$refs['dispatchForm'].validate(result => {
            if(result){
              this.$put("/user/role/dispatch", {
                username: this.dialogData.username,
                roleId: this.dialogData.roleId
              },resp => {
                if(resp.data === 1){
                  this.$message({
                    message: '分配成功',
                    type: 'success'
                  })
                  this.closeDialog()
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
          });
        },
        closeDialog: function () {
            this.dialogData.username = '';
            this.dialogData.roleId = '';
            this.$refs['dispatchForm'].resetFields();
            this.dialogData.visible = false;
        }
      },
      mounted() {
          this.$get("/role", {
            status: 1
          }, resp=>{
            this.roles = resp.data;
          })
      }
    }
</script>

<style scoped>

</style>
