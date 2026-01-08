<template>
  <el-dialog title="增加用户" :visible.sync="dialogData.visible"
             width="45%" @close="closeDialog">
    <el-form :model="user" :rules="userRules" :inline="true" ref="addUserForm">
      <el-form-item label="账号" prop="username" label-width="80px">
        <el-input v-model="user.username" size="mini" clearable placeholder="请输入账号"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password" label-width="80px">
        <el-input v-model="user.password" size="mini" clearable placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="name" label-width="80px">
        <el-input v-model="user.name" size="mini" clearable placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex" label-width="80px">
        <el-select v-model="user.sex" size="mini" clearable placeholder="请选择性别">
          <el-option v-for="s in sexList" :key="s.value" :label="s.name" :value="s.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="年龄" prop="age" label-width="80px">
        <el-input v-model.number="user.age" size="mini" clearable placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="手机号码" prop="phone" label-width="80px">
        <el-input v-model="user.phone" size="mini" clearable placeholder="请输入手机号码"></el-input>
      </el-form-item>
      <el-form-item label="电子邮箱" prop="email" label-width="80px">
        <el-input v-model="user.email" size="mini" clearable placeholder="请输入电子邮箱"></el-input>
      </el-form-item>
      <el-form-item label="入职日期" prop="entryTime" label-width="80px">
        <el-date-picker v-model="user.entryTime" type="date" size="mini" placeholder="请选择入职日期" value-format="yyyy-MM-dd"></el-date-picker>
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
    name: "UserDialog",
    props: ['dialogData'],
    data() {
      return {
        user: {
          username: '',
          password: '',
          name: '',
          sex: '',
          age: '',
          phone: '',
          email: '',
          entryTime: ''
        },
        userRules:{
          username: [
            {required: true, message: '请输入账号', trigger: 'blur'},
            {min: 6, max: 20, message: '账号长度只能在6-20个字符之间', trigger: 'blur'},
            {validator: this.checkUsername, trigger: 'blur'}
          ],
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
        this.$refs['addUserForm'].validate(result =>{
          if(result){
            //进行表单数据的提交
            this.$post("/user", this.user, resp=>{
              if(resp.data === 1){
                this.$message({
                  message: '增加成功',
                  type: 'success'
                })
                this.closeDialog();
                this.$parent.search();
              } else {
                this.$message({
                  message: '增加失败',
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
        this.$refs['addUserForm'].resetFields();
        this.dialogData.visible = false;
      },
      checkPassword: function (rule, value, callback) {
        //value就是当前规则针对的表单元素的值
        //callback是一个回调函数，主要用于处理提示信息
        // & | ^ >> >>> << <<<
        // 2 | 7
        // 020
        // 111
        // 111
        let strong = 0; //密码强度值
        for(let i=0; i<value.length; i++){
          let c = value.charAt(i);
          if(/[0-9]/.test(c)){
            strong |= 2; // 000  010 => 010 = 2
          } else if(/[a-z]/.test(c)){
            strong |= 4;  // 000 100 => 100 = 4    010 100 => 110 = 6
          } else if(/[A-Z]/.test(c)){
            strong |= 8;  //1000 0110 => 1110 => 14
          } else if(/[~!@#$%_]/.test(c)){
            strong |= 16;
          }
        }
        if(strong === 30){//密码满足要求
          callback();//处理掉提示信息
        } else {
          //构建提示信息
          callback(new Error("密码必须包含小写母、大写字母、数字和特殊符号"))
        }
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
      },
      checkUsername: function (rule, value, callback) {
        this.$get("/user/" + value, null, resp => {
          if(resp.data){
            callback(new Error("该账号已被注册"))
          } else{
            callback()
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
