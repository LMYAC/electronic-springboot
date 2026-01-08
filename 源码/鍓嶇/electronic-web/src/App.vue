<template>
  <router-view/>
</template>

<script>
  export default {
    name: 'App'
  }
</script>

<style>
  html, body {
    width: 100%;
    max-width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
    overflow: hidden;
  }

  body {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    background-image: url("./assets/bg.jpg");
    background-size: cover;
    color: #2c3e50;
  }

  ::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 6px; /*高宽分别对应横竖滚动条的尺寸*/
    height: 1px;
  }

  ::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 4px;
    box-shadow: inset 0 0 4px rgba(0, 0, 0, 0.2);
    background: #535353;
  }

  ::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    box-shadow: inset 0 0 4px rgba(0, 0, 0, 0.2);
    /*border-radius: 10px;*/
    background: #ededed;
  }


  .el-dialog__header {
    padding: 20px 20px 10px;
    background-color: #35dba2;
    border-top-left-radius: 2px;
    border-top-right-radius: 2px;
  }

  .el-dialog__footer{
    border-top: 1px solid #ddd;
  }

  .el-dialog__title{
    color: white;
  }

  .el-dialog__body .el-input,
  .el-dialog__body .el-select{
    width: 190px;
  }
</style>
