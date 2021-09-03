<template>
  <div class="app">
    <div class="app-menu">
      <router-link to="/chapter01">
        第1章 入门
      </router-link>
      <router-link to="/chapter02">
        第2章 Schema和类型
      </router-link>
      <router-link to="/chapter03">
        第3章 查询和变量
      </router-link>
      <router-link to="/chapter04">
        第4章 验证
      </router-link>
      <router-link to="/chapter05">
        第5章 执行
      </router-link>
      <router-link to="/chapter06">
        第6章 内省
      </router-link>
      <router-link to="/chapter07">
        第7章 最佳实践
      </router-link>
      <router-link to="/chapter99">
        第X章 实践
      </router-link>
      
    </div>
    <div class="app-content">
      <router-view />
    </div>
  </div>
</template>

<style lang="less" scoped>
@import "assets/common.less";
.app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: left;
  color: #2c3e50;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  justify-content: flex-start;
  align-items: flex-start;
  width: 100%;
  *{
    margin: 8px;
  }

  .app-content {
    line-height: 1.5;
    width: 80%;
  }

  .app-menu {
    padding-left: 8px;
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    justify-content: flex-start;
    align-items: flex-start;
    background: #84a7cf0a;
    min-width: 125px;
    width: 30%;
    font-size: 18px;

    a:hover {
      color: gray;
    }
    a {
      color: gray;
    }
  }
}
</style>
