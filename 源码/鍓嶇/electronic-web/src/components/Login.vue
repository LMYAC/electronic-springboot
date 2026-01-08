<!--template就是Vue组件使用的html内容-->
<template>
  <!--:gutter="20" 表示行中的每一列之间的间距-->
  <el-row :gutter="20">
    <!--:span="6"表示该列占用6个单位，一行总共有24个单位  :offset="8"表示需要偏移8个单位-->
    <el-col :span="8" :offset="8">
      <h1>电力巡检系统</h1>
      <!--:model就是用来绑定表单数据的对象 :rules就是用来验证表单数据的规则 ref就是用来定位表单的
      label-width就是用来表示标签元素前面的label标签的宽度-->
      <el-form :model="user" :rules="userRules" ref="loginForm" class="login-form">
        <el-tabs type="border-card"  v-model="flag">
          <el-tab-pane label="账号登录" :key="0">
            <!--label属性表示对该表单项的注释说明  prop属性表示用于验证的表单规则的属性-->
            <el-form-item prop="username">
              <!--clearable就表示这个表单元素中输入的内容可以清除-->
              <el-input v-model="user.username" placeholder="请输入账号" clearable prefix-icon="el-icon-user"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input type="password" v-model="user.password" placeholder="请输入密码" clearable prefix-icon="el-icon-lock"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="login" style="width: 100%">登录</el-button>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="手机登录" :key="1">
<!--            label属性表示对该表单项的注释说明  prop属性表示用于验证的表单规则的属性-->
            <el-form-item prop="mobile">
              <!--clearable就表示这个表单元素中输入的内容可以清除-->
              <el-input v-model="user.mobile" placeholder="请输入手机号码" clearable prefix-icon="el-icon-mobile"></el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input v-model="user.code" placeholder="请输入验证码" clearable prefix-icon="el-icon-lock" style="width: calc(100% - 146px);"></el-input>
              <el-button type="success" @click="getSmsCode" :disabled="codeButtonState" style="width: 140px">{{codeButtonText}}</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="login" style="width: 100%">登录</el-button>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-col>
  </el-row>
</template>

<!--script就是Vue组件中涉及到的数据和方法定义-->
<script>
export default {
  name: 'Login',
  data () {
    return {
      user:{
        username: '',
        password: '',
        mobile: '',
        code: ''
      },
      userRules: {//这个表示登录表单的验证规则
        username: [//这个属性可能会有多条验证规则，因此定义为数组
          {validator: this.checkUsername, trigger: 'blur'}
        ],
        password: [
          {validator: this.checkPassword, trigger: 'blur'}
        ],
        mobile: [
          {validator: this.checkPhone, trigger: 'blur'}
        ],
        code: [
          {validator: this.checkCode, trigger: 'blur'}
        ]
      },
      flag: '0',
      timer: '',
      codeButtonState: false,
      secondsInterval: 60,
      codeButtonText: '获取验证码'
    }
  },
  methods:{
    checkUsername: function(rule, value, callback){
      if(value === '' && this.flag === '0'){
        callback(new Error("请输入账号"));
      } else {
        callback();
      }
    },
    checkPassword: function(rule, value, callback){
      if(value === '' && this.flag === '0'){
        callback(new Error("请输入密码"));
      } else {
        callback();
      }
    },
    checkPhone: function(rule, value, callback){
      if(this.flag === '1'){
        if(value === ''){
          callback(new Error("请输入手机号"));
          return;
        } else if(!/1[3-9][0-9]{9}/.test(value)){
          callback(new Error("手机号码格式不正确"));
          return;
        }
      }
      callback();
    },
    checkCode: function(rule, value, callback){
      if(this.flag === '1'){
        if(value === ''){
          callback(new Error("请输入验证码"));
          return;
        } else if(!/[0-9]{6}/.test(value)){
          callback(new Error("验证码格式不正确"));
          return;
        }
      }
      callback();
    },
    login: function () {
      //$refs这个属性就可以用来引用标签上具有ref属性的元素
      //validate方法就是用来校验表单元素的
      this.$refs['loginForm'].validate( result =>{
        if(result){//如果校验结果为真
          //校验通过了，需要发送信息至服务器端，进行登录
          let data = "";
          Object.keys(this.user).forEach(key => data += key + "=" + this.user[key] + "&");
          data = data.substring(0, data.length - 1);
          let url = this.flag === '0' ? '/login' : '/sms';
          this.$post(url, data, resp => {
            if(resp.data === 1){
              //跳转路由
              this.$router.push({name: 'Manage'})
            } else if(resp.data === 0){
              this.$message.error('账号或密码错误');
            } else if(resp.data === -1){
              this.$message.error('账号不存在');
            } else if(resp.data === -2){
              this.$message.error('账号被冻结');
            }
          })
        } else {
          return false; //校验不通过，表单不用处理
        }
      });
    },
    getSmsCode: function () {
      this.$get("/user/code/" + this.user.phone, null);
      this.codeButtonState = true
      this.timer = setInterval(_ => {
        this.secondsInterval -= 1;
        if(this.secondsInterval === 0){
          this.codeButtonText = "获取验证码";
          this.codeButtonState = false
          clearInterval(this.timer);
          this.timer = '';
        } else {
          this.codeButtonText = this.secondsInterval + "秒后重新获取";
        }
      }, 1000)
    }
  }
}
</script>

<!--style表示组件使用到的css样式  scoped表示style中的定义的css样式只有当前组件有用
  如果不加scoped属性，表示这里面的css样式针对所有的页面都生效-->
<style scoped>
  .login-form{
    box-shadow: 2px 2px 2px #000;
  }
  h1{
    font-family: 楷体,serif;
    font-size: 3em;
    text-align: center;
    text-shadow: 2px 2px 2px #d9d9d9;
    color: white;
  }
  .el-tab-pane{
    padding: 20px 20px 10px 20px;
  }
</style>
<style>
  .el-tabs__item{
    width: 115%;
    height: 50px;
    line-height: 50px;
    text-align: center;
  }

</style>
