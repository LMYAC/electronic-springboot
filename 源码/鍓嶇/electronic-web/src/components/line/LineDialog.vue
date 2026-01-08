<template>
  <el-dialog :title="dialogData.title" :visible.sync="dialogData.visible" width="67%" @close="closeDialog">
    <el-form :model="dialogData.line" :rules="lineRules" :inline="true" ref="lineForm">
      <el-form-item label="线路编号" label-width="80px" v-show="dialogData.isUpdate">
        <el-input v-model="dialogData.line.id" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="线路名称" prop="name" label-width="80px">
        <el-input v-model="dialogData.line.name" size="mini" clearable placeholder="请输入线路名称"></el-input>
      </el-form-item>
      <el-form-item label="杆塔数量" prop="towerNum"  label-width="80px">
        <el-input v-model.number="dialogData.line.towerNum" size="mini" clearable placeholder="请输入杆塔数量" @blur="fillEndNumber"></el-input>
      </el-form-item>
      <el-form-item label="起始杆号" prop="startNumber" label-width="80px">
        <el-autocomplete v-model="dialogData.line.startNumber" size="mini" :fetch-suggestions="getTowerInfo" placeholder="请输入起始杆号" @select="handleSelect"></el-autocomplete>
      </el-form-item>
      <el-form-item label="结束杆号" prop="endNumber" label-width="80px">
        <el-input v-model="dialogData.line.endNumber" size="mini" readonly></el-input>
      </el-form-item>
      <el-form-item label="线路长度" prop="length" label-width="80px">
        <el-input v-model="dialogData.line.length" size="mini" clearable placeholder="请输入线路长度"></el-input>
      </el-form-item>
      <el-form-item label="回路长度" prop="backLength" label-width="80px">
        <el-input v-model="dialogData.line.backLength" size="mini" clearable placeholder="请输入回路长度"></el-input>
      </el-form-item>
      <el-form-item label="电压等级" prop="level" label-width="80px">
        <el-input v-model="dialogData.line.level" size="mini" clearable placeholder="请输入电压等级"></el-input>
      </el-form-item>
      <el-form-item label="营运日期" prop="operationTime" label-width="80px">
        <el-date-picker v-model="dialogData.line.operationTime" type="date" size="mini" placeholder="请输入营运日期" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item label="启用状态" prop="enableStatus" label-width="80px" clearable>
        <el-select v-model="dialogData.line.enableStatus" size="mini" clearable placeholder="请选择启用状态">
          <el-option v-for="s in $parent.states" :key="s.value" :label="s.text" :value="s.value"></el-option>
        </el-select>
      </el-form-item>
      <div class="remark-box">
        <el-form-item label="备注" label-width="80px" style="width: calc(100% - 34px)">
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" :columns="40" v-model="dialogData.line.remark" size="mini" placeholder="请填写备注"></el-input>
        </el-form-item>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog" size="small">取 消</el-button>
      <el-button type="primary" @click="saveLine" size="small">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "LineDialog",
    props: ['dialogData'],
    data() {
      return {
        lineRules: {
          name: [
            {required: true, message: '请输入线路名称', trigger: 'blur'}
          ],
          towerNum: [
            {required: true, message: '请输入杆塔数量', trigger: 'blur'}
          ],
          startNumber: [
            {required: true, message: '请输入起始杆号', trigger: 'blur'}
          ],
          endNumber: [
            {required: true, message: '请输入结束杆号', trigger: 'blur'}
          ],
          length: [
            {required: true, message: '请输入线路长度', trigger: 'blur'}
          ],
          backLength: [
            {required: true, message: '请输入回路长度', trigger: 'blur'}
          ],
          level: [
            {required: true, message: '请输入电压等级', trigger: 'blur'}
          ],
          enableStatus: [
            {required: true, message: '请选择线路状态', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      saveLine: function () {
        this.$refs['lineForm'].validate(result => {
          if(result){
              let method = this.dialogData.isUpdate ? "put" : "post";
              this.$ajax("/line", method, this.dialogData.line, resp=>{
                if(resp.data === 1){
                  this.$message({
                    message: '修改成功',
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
        })
      },
      getTowerInfo: function(content, callback){
        this.$get("/tower",{
          queryString: this.dialogData.line.startNumber,
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
      handleSelect: function(item){
        this.dialogData.line.startNumber = item.id;
        let prefix = item.orders.substring(0, item.orders.length - 4);
        if(this.dialogData.line.towerNum !== ''){
          let end = parseInt(item.orders.substring(item.orders.length - 4)) + this.dialogData.line.towerNum;
          end = end + "";
          while (end.length < 4){
            end = "0" + end;
          }
          let endOrders = prefix + end;
          this.$get("/tower/" + endOrders, null,resp=>{
              this.dialogData.line.endNumber = resp.data;
          })
        }
      },
      fillEndNumber: function(){
        let startNumber = this.dialogData.line.startNumber;
        if(startNumber === '') return;
        this.$get("/tower/" + startNumber + "/" + this.dialogData.line.towerNum, null,resp=>{
          this.dialogData.line.endNumber = resp.data;
        })
      },
      closeDialog: function () {
        this.dialogData.line = {
          id: '',
          name: '',
          towerNum: '',
          startNumber: '',
          endNumber: '',
          length: '',
          backLength: '',
          level: '',
          operationTime: '',
          enableStatus: '',
          remark: ''
        }
        this.$refs['lineForm'].resetFields();
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
