<template>
  <div class="content-box">
    <el-table :data="dataList1" border style="width: 100%;" height="calc(100% - 80px)" size="mini">
      <el-table-column prop="id" label="任务编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="name" label="任务名称"></el-table-column>
      <el-table-column prop="lineId" label="线路编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="lineName" label="线路名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column prop="defectUserName" label="消缺员"></el-table-column>
      <el-table-column prop="stateText" label="任务状态"></el-table-column>
      <el-table-column label="操作" width="240">
        <!--这个scope就是代表当前循环的范围，scope.row就表示当前行使用的数据，这里表示一个user对象-->
        <template slot-scope="scope">
          <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
          <el-button type="text" size="small" @click="dispatch(scope.row)" v-show="scope.row.state === 0">分配</el-button>
          <el-button type="text" size="small" @click="changeState(scope.row.id)" v-show="scope.row.state === 1">执行
          </el-button>
          <el-button type="text" size="small" @click="addReception(scope.row)" v-show="scope.row.state === 2">添加回执
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="height: 20px"></div>
    <el-table :data="dataList2" border style="width: 100%;" height="calc(100% - 80px)" size="mini">
      <el-table-column prop="id" label="任务编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="name" label="任务名称"></el-table-column>
      <el-table-column prop="lineId" label="线路编号" show-overflow-tooltip></el-table-column>
      <el-table-column prop="lineName" label="线路名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column prop="defectUserName" label="消缺员"></el-table-column>
      <el-table-column prop="stateText" label="任务状态"></el-table-column>
      <el-table-column label="操作" width="240">
        <!--这个scope就是代表当前循环的范围，scope.row就表示当前行使用的数据，这里表示一个user对象-->
        <template slot-scope="scope">
          <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
          <el-button type="text" size="small" @click="dispatch(scope.row)" v-show="scope.row.state === 0">分配</el-button>
          <el-button type="text" size="small" @click="changeState(scope.row.id)" v-show="scope.row.state === 1">执行
          </el-button>
          <el-button type="text" size="small" @click="addReception(scope.row)" v-show="scope.row.state === 2">添加回执
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: "List",
    data() {
      return {
        dataList1: [],
        dataList2: []
      }
    }
  }
</script>

<style scoped>
  .content-box {
    width: calc(100% - 10px);
    height: 100%;
    display: flex;
    flex-direction: column;
  }
</style>
