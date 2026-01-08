<template>
  <div class="content-box">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>缺陷类型</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="search(0)">查询</el-button>
      </div>
      <el-button type="danger" icon="el-icon-circle-plus-outline" size="mini" @click="showDialog(1)" style="margin-bottom: 10px">新增</el-button>
      <el-table :data="defectTypes" border style="width: 100%;" height="calc(100% - 78px)" size="mini">
        <el-table-column prop="id" label="缺陷类型编号"></el-table-column>
        <el-table-column prop="name" label="缺陷类型名称"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
            <el-button type="text" size="small" @click="showDialog(2, scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="deleteDefect(0,scope.row, '确定要删除该缺陷类型吗？')">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>缺陷等级</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="search(1)">查询</el-button>
      </div>
      <el-button type="danger" icon="el-icon-circle-plus-outline" size="mini" @click="showDialog(3)" style="margin-bottom: 10px">新增</el-button>
      <el-table :data="defectLevels" border style="width: 100%;" height="calc(100% - 78px)" size="mini">
        <el-table-column prop="id" label="缺陷等级编号"></el-table-column>
        <el-table-column prop="name" label="缺陷等级名称"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <!--admin这个账号是不允许被修改和删除的，也不允许被冻结-->
            <el-button type="text" size="small" @click="showDialog(4, scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="deleteDefect(1,scope.row, '确定要删除该缺陷等级吗？')">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <defect-dialog :dialog-data="defectData" />
  </div>
</template>

<script>
  import DefectDialog from "./DefectDialog";
  export default {
    name: "Defect",
    components: {DefectDialog},
    data() {
      return {
        defectTypes: [],
        defectLevels: [],
        defectData: {
          title: '',
          flag: '',
          labelPrefix: '',
          data:{
            id: '',
            name: '',
          },
          visible: false
        }
      }
    },
    methods:{
      search: function(flag){
        let url = flag === 0 ? '/defectData/defectType' : '/defectData/defectLevel'
        this.$get(url, null, resp=>{
          if(flag === 0){
            this.defectTypes = resp.data;
          } else {
            this.defectLevels = resp.data;
          }
        })
      },
      showDialog: function (flag, data) {
        this.defectData.flag = flag;
        if(flag === 1){
          this.defectData.title = "新增缺陷类型";
          this.defectData.labelPrefix = "缺陷类型";
        } else if(flag === 2){
          this.defectData.title = "编辑缺陷类型";
          this.defectData.labelPrefix = "缺陷类型";
          this.defectData.data.id = data.id;
          this.defectData.data.name = data.name;
        } else if(flag === 3){
          this.defectData.title = "新增缺陷等级";
          this.defectData.labelPrefix = "缺陷等级";
        } else if(flag === 4){
          this.defectData.title = "编辑缺陷等级";
          this.defectData.labelPrefix = "缺陷等级";
          this.defectData.data.id = data.id;
          this.defectData.data.name = data.name;
        }
        this.defectData.visible = true;
      },
      deleteDefect: function (flag, data, tip) {
        let urlPrefix = flag === 0 ? '/defectData/defectType' : '/defectData/defectLevel';
        this.showConfirm(tip, _ =>{
          this.$del(urlPrefix + "/" + data.id, null, resp=>{
            if(resp.data === 1){
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.search(flag);
            } else {
              this.$message({
                message: '删除失败',
                type: 'error'
              })
            }
          })
        })
      }
    },
    mounted() {
      this.search(0)
      this.search(1)
    }
  }
</script>

<style scoped>
  .content-box {
    width: calc(100% - 10px);
    height: 100%;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    grid-column-gap: 20px;
  }

  .el-card {
    flex-grow: 1.5;
    flex-shrink: 0.5;
  }
</style>

<style>
  .el-card__body{
    height: calc(100% - 60px);
  }
</style>
