<template>
  <el-dialog title="编辑用户" :visible.sync="updateData.visible" width="45%" @close="closeDialog">
    <el-form :model="updateData.user" :rules="userRules" :inline="true" ref="updateUserForm">
      <el-form-item label="账号" label-width="80px">
        <el-input v-model="updateData.user.username" size="mini" clearable placeholder="请输入账号" readonly></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="name" label-width="80px">
        <el-input v-model="updateData.user.name" size="mini" clearable placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex" label-width="80px">
        <el-select v-model="updateData.user.sex" size="mini" clearable placeholder="请选择性别">
          <el-option v-for="s in sexList" :key="s.value" :label="s.name" :value="s.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="年龄" prop="age" label-width="80px">
        <el-input v-model.number="updateData.user.age" size="mini" clearable placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="手机号码" prop="phone" label-width="80px">
        <el-input v-model="updateData.user.phone" size="mini" clearable placeholder="请输入手机号码"></el-input>
      </el-form-item>
      <el-form-item label="电子邮箱" prop="email" label-width="80px">
        <el-input v-model="updateData.user.email" size="mini" clearable placeholder="请输入电子邮箱"></el-input>
      </el-form-item>
      <el-form-item label="入职日期" prop="entryTime" label-width="80px">
        <el-date-picker v-model="updateData.user.entryTime" type="date" size="mini" placeholder="请选择入职日期"
                        value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item label="离职日期" prop="leaveTime" label-width="80px">
        <el-date-picker v-model="updateData.user.leaveTime" type="date" size="mini" placeholder="请选择离职日期"
                        value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="saveUser" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "UserInfo",
    props: ["updateData"],
    data(){
      return {
        userRules:{
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 6, max: 20, message: '密码长度只能在6-20个字符之间', trigger: 'blur'},
            {validator: this.checkPassword, trigger: 'blur'}
          ],
          name: [
            {required: true, message: '请输入姓名', trigger: 'blur'},
            {min: 2, max: 20, message: '姓名长度只能在2-20个字符之间', trigger: 'blur'}
          ],
          sex: [
            {required: true, message: '请选择性别', trigger: 'blur'}
          ],
          age: [
            {validator: this.checkAge, trigger: 'blur'}
          ],
          phone: [
            {required: true, message: '请输入手机号码', trigger: 'blur'},
            {validator: this.checkPhone, trigger: 'blur'}
          ],
          email: [],
          entryTime: [
            {required: true, message: '请选择入职日期', trigger: 'blur'}
          ]
        },
        sexList: [{
          name: '男',
          value: 0
        },{
          name: '女',
          value: 1
        },{
          name: '其他',
          value: 2
        }]
      }
    },
    methods:{
      saveUser: function () {
        this.$refs['updateUserForm'].validate(result =>{
          if(result){
            //进行表单数据的提交
            this.$put("/user", this.updateData.user, resp=>{
              if(resp.data === 1){
                this.$message({
                  message: '修改成功',
                  type: 'success'
                })
                this.closeDialog();
                this.$parent.search();
              } else {
                this.$message({
                  message: '修改失败',
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
        this.user = {
          username: '',
          password: '',
          name: '',
          sex: '',
          age: '',
          phone: '',
          email: '',
          entryTime: ''
        }
        this.$refs['updateUserForm'].resetFields();
        this.updateData.visible = false;
      },
      checkAge: function (rule, value, callback) {
        if(value === '') {
          callback();
          return;
        }
        if(!Number.isInteger(value)){//不是数字
          callback(new Error("年龄必须是整数"))
        } else {
          if(value < 18){
            callback(new Error("年龄必须满18周岁"))
          } else {
            callback();
          }
        }
      },
      checkPhone: function (rule, value, callback) {
        let regex = /1[3-9][0-9]{9}/;
        if(regex.test(value)){
          callback()
        } else {
          callback(new Error("手机号码格式不正确"))
        }
      }
    }
  }
</script>

<style scoped>

</style>
