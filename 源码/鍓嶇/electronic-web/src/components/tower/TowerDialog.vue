<template>
  <el-dialog :title="dialogData.title" :visible.sync="dialogData.visible" width="45%" @close="closeDialog">
    <el-form :model="dialogData.tower" :rules="towerRules" :inline="true" ref="towerForm">
      <el-form-item label="杆塔编号" label-width="80px" v-show="dialogData.isUpdate">
        <el-input v-model="dialogData.tower.id" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="杆塔序号" prop="orders" label-width="80px">
        <el-input v-model="dialogData.tower.orders" size="mini" clearable placeholder="请输入杆塔序号"></el-input>
      </el-form-item>
      <el-form-item label="杆塔位置" prop="position" label-width="80px">
        <el-input v-model="dialogData.tower.position" size="mini" clearable placeholder="请输入杆塔位置"></el-input>
      </el-form-item>
      <el-form-item label="启用状态" prop="state" label-width="80px">
        <el-select v-model="dialogData.tower.state" size="mini" clearable placeholder="请选择启用状态">
          <el-option v-for="s in $parent.states" :key="s.value" :label="s.text" :value="s.value"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="saveTower" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "TowerDialog",
    props: ['dialogData'],
    data() {
      return {
        towerRules: {
          orders: [
            {required: true, message: '请输入杆塔序号', trigger: 'blur'}
          ],
          position: [
            {required: true, message: '请输入杆塔位置', trigger: 'blur'}
          ],
          state: [
            {required: true, message: '请选择启用状态', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      saveTower: function () {
        this.$refs['towerForm'].validate(result => {
          if (result) {
            let method = 'post'
            let data = this.dialogData.tower;
            if(this.dialogData.isUpdate){
              method = 'put';
              data = {
                towerIds: [this.dialogData.tower.id],
                orders: this.dialogData.tower.orders,
                state: this.dialogData.tower.state,
                position: this.dialogData.tower.position
              }
            }
            this.$ajax("/tower", method, data, resp=>{
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
            });
          } else {
            return false;
          }
        });
      },
      closeDialog: function () {
        this.dialogData.tower = {
          id: '',
          orders: '',
          position: '',
          state: '',
          stateText: ''
        }
        this.$refs['towerForm'].resetFields();
        this.dialogData.visible = false;
      }
    }
  }
</script>

<style scoped>

</style>
