<template>
  <div class="content-box">
    <div class="condition-box">
      <span>操作用户</span>
      <el-input v-model="condition.user" clearable placeholder="请输入操作用户" size="mini"></el-input>
      <span>操作时间</span>
      <el-date-picker v-model="condition.startTime" type="date" value-format="yyyy-MM-dd" size="mini" placeholder="请选择开始日期"></el-date-picker>
      <span>至</span>
      <el-date-picker v-model="condition.endTime" type="date" value-format="yyyy-MM-dd" size="mini"  placeholder="请选择结束日期"></el-date-picker>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="search">查询</el-button>
      <el-button type="default" icon="el-icon-refresh-left" size="mini" @click="reset">重置</el-button>
    </div>
    <el-table :data="logs" border style="width: 100%;" height="calc(100% - 120px)" size="mini">
      <el-table-column prop="id" label="日志编号"></el-table-column>
      <el-table-column prop="url" label="请求地址"></el-table-column>
      <el-table-column prop="method" label="处理方法" show-overflow-tooltip></el-table-column>
      <el-table-column prop="operationTime" label="操作时间"></el-table-column>
      <el-table-column prop="userName" label="操作员"></el-table-column>
      <el-table-column prop="hasError" label="是否存在错误"></el-table-column>
    </el-table>
    <el-pagination small background :page-sizes="[30, 50, 80, 100]"
                   :current-page="currentPage"
                   :page-size="displayCount"
                   :total="total"
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   layout="total, sizes, prev, pager, next" v-show="total > 0" align="right"></el-pagination>
  </div>
</template>

<script>
    export default {
        name: "Log",
      data(){
          return {
            condition:{
              user: '',
              startTime: '',
              endTime: ''
            },
            logs: [],
            currentPage: 1,
            displayCount: 30,
            total: 0
          }
      },
      methods:{
        search: function () {
          this.$get("/log",{
            user: this.condition.user,
            startTime:  this.condition.startTime,
            endTime:  this.condition.endTime,
            currentPage: this.currentPage,
            displayCount: this.displayCount
          }, resp=>{
            this.logs = resp.data.list;
            this.total = resp.data.total;
          })
        },
        reset: function () {
          this.condition = {
            user: '',
            startTime: '',
            endTime: ''
          }
          this.currentPage = 1;
          this.search();
        },
        handleSizeChange: function(val) {
          this.displayCount = val;
          this.currentPage = 1;
          this.search();
        },
        handleCurrentChange: function (val) {
          this.currentPage = val;
          this.search();
        },
      },
      mounted() {
          this.search();
      }
    }
</script>

<style scoped>
  .content-box{
    width: calc(100% - 10px);
    height: 100%;
  }
  .condition-box, .button-box{
    height: 40px;
    line-height: 40px;
  }
  .condition-box{
    color: #909399;
    font-size: 12px;
  }
  .el-pagination{
    height: 30px;
    padding-top: 10px;
  }
  .el-input, .el-select{
    width: 120px;
    margin-right: 10px;
  }
</style>
