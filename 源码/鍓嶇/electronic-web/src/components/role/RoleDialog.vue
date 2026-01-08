<template>
  <el-dialog title="编辑角色" :visible.sync="dialogData.visible" width="45%" @close="closeDialog">
    <el-form :model="dialogData.role" :rules="roleRules" :inline="true" ref="roleForm">
      <el-form-item label="角色编号" label-width="80px">
        <el-input v-model="dialogData.role.id" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="角色名称" prop="name" label-width="80px">
        <el-input v-model="dialogData.role.name" size="mini" clearable placeholder="请输入角色名称"></el-input>
      </el-form-item>
      <el-form-item label="英文名称" label-width="80px">
        <el-input v-model="dialogData.role.enName" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="启用状态" prop="enableStatus" label-width="80px">
        <el-select v-model="dialogData.role.enableStatus" size="mini" clearable placeholder="请选择启用状态">
          <el-option v-for="s in $parent.enableStatus" :key="s.value" :label="s.text" :value="s.value"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="saveRole" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "RoleDialog",
    props: ['dialogData'],
    data() {
      return {
        roleRules: {
          name: [
            {required: true, message: '请输入角色名', trigger: 'blur'}
          ],
          enName: [
            {required: true, message: '请输入角色英文名称', trigger: 'blur'}
          ],
          enableStatus: [
            {required: true, message: '请选择启用状态', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      saveRole: function () {
        this.$refs['roleForm'].validate(result => {
          if (result) {
            this.$put('/role', {
              roleIds: [this.dialogData.role.id],
              name: this.dialogData.role.name,
              enableStatus: this.dialogData.role.enableStatus
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
        });
      },
      closeDialog: function () {
        this.dialogData.role = {
          id: '',
          name: '',
          enName: '',
          enableStatus: ''
        }
        this.$refs['roleForm'].resetFields();
        this.dialogData.visible = false;
      }
    }
  }
</script>

<style scoped>

</style>
