<template>
  <el-dialog :title="dialogData.title" :visible.sync="dialogData.visible" width="67%" @close="closeDialog">
    <el-form :model="dialogData.inspectionTask" :rules="inspectionTaskRules" :inline="true" ref="taskForm">
      <el-form-item label="任务编号" label-width="80px" v-show="dialogData.isUpdate">
        <el-input v-model="dialogData.inspectionTask.id" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="线路编号" label-width="80px">
        <el-input v-model="dialogData.inspectionTask.lineId" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="线路名称" label-width="80px">
        <el-input v-model="dialogData.inspectionTask.lineName" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="任务名称" prop="name" label-width="80px">
        <el-input v-model="dialogData.inspectionTask.name" size="mini" clearable placeholder="请输入任务名称"></el-input>
      </el-form-item>
      <el-form-item label="起始杆号" prop="startNumber" label-width="80px">
        <el-autocomplete v-model="dialogData.inspectionTask.startNumber" size="mini" :fetch-suggestions="getTowerInfo"
                         placeholder="请输入起始杆号" @select="handleStartNumberSelect"></el-autocomplete>
      </el-form-item>
      <el-form-item label="结束杆号" prop="endNumber" label-width="80px">
        <el-autocomplete v-model="dialogData.inspectionTask.endNumber" size="mini" :fetch-suggestions="getTowerInfo"
                         placeholder="请输入结束杆号" @select="handleEndNumberSelect"></el-autocomplete>
      </el-form-item>
      <el-form-item label="巡检员" label-width="80px">
        <el-autocomplete v-model="dialogData.inspectionTask.inspectionUser" size="mini" :fetch-suggestions="getInspectionUsers"
                         placeholder="请输入巡检员信息" @select="handleSelect"></el-autocomplete>
      </el-form-item>
      <div class="remark-box">
        <el-form-item label="备注" label-width="80px" style="width: calc(100% - 34px)">
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" :columns="40" v-model="dialogData.inspectionTask.remark"
                    size="mini" placeholder="请填写备注"></el-input>
        </el-form-item>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="addInspectionTask" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "InspectionTaskDialog",
    props: ['dialogData'],
    data() {
      return {
        startTower: "",
        endTower: "",
        inspectionTaskRules: {
          name: [
            {required: true, message: '任务名称不能为空', trigger: 'blur'}
          ],
          startNumber: [
            {required: true, message: '请输入起始杆号', trigger: 'blur'},
            {validator: this.startNumberCheck, trigger: 'change'}
          ],
          endNumber: [
            {required: true, message: '请输入结束杆号', trigger: 'blur'},
            {validator: this.endNumberCheck, trigger: 'change'}
          ]
        }
      }
    },
    methods:{
      startNumberCheck: function(rule, value, callback){
        if(this.endTower === ''){
          callback();
        } else {
          let orders1 = this.startTower.orders
          let orders2 = this.endTower.orders
          let prefix1 = orders1.substring(0, orders1.length - 4);
          let prefix2 = orders2.substring(0, orders2.length - 4);
          if(prefix1 !== prefix2){
            callback(new Error("起始杆号和结束杆号不属于同一线路"));
          } else {
            let orderNum1 = parseInt(orders1.substring(orders1.length - 4))
            let orderNum2 = parseInt(orders2.substring(orders2.length - 4))
            if(orderNum2 - orderNum1 > 20){
              callback(new Error("单人巡检杆塔数量不能超过20"));
            } else {
              callback()
            }
          }
        }
      },
      endNumberCheck: function(rule, value, callback){
        if(this.startTower === ''){
          callback();
        } else {
          let orders1 = this.startTower.orders
          let orders2 = this.endTower.orders
          let prefix1 = orders1.substring(0, orders1.length - 4);
          let prefix2 = orders2.substring(0, orders2.length - 4);
          if(prefix1 !== prefix2){
            callback(new Error("起始杆号和结束杆号不属于同一线路"));
          } else {
            let orderNum1 = parseInt(orders1.substring(orders1.length - 4))
            let orderNum2 = parseInt(orders2.substring(orders2.length - 4))
            if(orderNum2 - orderNum1 > 20){
              callback(new Error("单人巡检杆塔数量不能超过20"));
            } else {
              callback()
            }
          }
        }
      },
      addInspectionTask: function(){
        this.$refs['taskForm'].validate(result =>{
          if(result){
            let method = this.dialogData.isUpdate ? "put" : 'post';
            this.$ajax('/inspectionTask', method, this.dialogData.inspectionTask, resp=>{
              if(resp.data === 1){
                this.$message({
                  message: '保存成功',
                  type: 'success'
                })
                this.closeDialog();
              } else {
                this.$message({
                  message: '保存失败',
                  type: 'error'
                })
              }
            })
          } else {
            return false
          }
        })
      },
      getTowerInfo: function(content, callback){
        this.$get("/tower",{
          queryString: content,
          state: 1,
          currentPage: 1,
          displayCount: 30
        }, resp=>{
          callback(resp.data.list.map(t =>  {
            return {
              value: t.position + "(" + t.orders + ")",
              orders: t.orders,
              id: t.id
            }
          }))
        })
      },
      handleStartNumberSelect: function(item){
        this.dialogData.inspectionTask.startNumber = item.id;
        this.startTower = item;
      },
      handleEndNumberSelect: function(item){
        this.dialogData.inspectionTask.endNumber = item.id;
        this.endTower = item;
      },
      getInspectionUsers: function (content, callback) {
        this.$get("/user/item/query",{
          queryString: content,
          state: 1,
          roleId: 3,
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
        this.dialogData.inspectionTask.inspectionUser = item.id;
      },
      closeDialog: function () {
        this.dialogData.inspectionTask = {
          id: '',
          lineId: '',
          lineName: '',
          name: '',
          startNumber: '',
          endNumber: '',
          remark: '',
          inspectionUser: ''
        }
        this.$refs['taskForm'].resetFields();
        this.dialogData.visible = false;
      }
    }
  }
</script>

<style>
  .remark-box .el-form-item__content{
    width: calc(100% - 80px);
  }
</style>
