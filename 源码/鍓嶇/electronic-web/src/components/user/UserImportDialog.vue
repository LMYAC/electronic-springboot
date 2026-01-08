<template>
  <el-dialog title="导入用户" :visible.sync="importData.visible"
             width="45%" @close="closeDialog">
    <div v-file="file"></div>
    <div></div>
    <div>导入的账号默认密码是：123456</div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="importUser" size="small">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "UserImportDialog",
    props: ['importData'],
    data() {
      return {
        file: ''
      }
    },
    methods:{
      importUser: function(){
        this.$upload("/user/file/import", [this.file], resp=>{
          if(resp.data){
            this.$message({
              message: '导入成功',
              type: 'success'
            })
            this.closeDialog();
            this.$parent.search();
          } else {
            this.$message({
              message: '导入失败',
              type: 'error'
            })
          }
        })
      },
      closeDialog: function () {
        this.importData.visible = false;
        this.file = ''
      }
    }
  }
</script>

<style scoped>

</style>
