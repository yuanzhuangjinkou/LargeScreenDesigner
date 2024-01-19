<template>
  <div id="app">
<!--    <plugin-demo/>-->
    <Index/>
<!--    <Test/>-->
  </div>
</template>

<script>
import PluginDemo from "./views/PluginDemo";
import Index from "./views/echarts/map/buddle/index";
import Style from "./views/echarts/map/buddle/style";
import Type from "./views/echarts/map/buddle/type";
import Data from "./views/echarts/map/buddle/data";
import Test from "./views/test/test";

export default {
  name: 'App',
  components: {
    PluginDemo,
    Index,
    Style,
    Type,
    Data,
    Test
  }
}
</script>

<style>
#app {
  /*font-family: 'Avenir', Helvetica, Arial, sans-serif;*/
  /*-webkit-font-smoothing: antialiased;*/
  /*-moz-osx-font-smoothing: grayscale;*/
  /*text-align: center;*/
  /*color: #2c3e50;*/
  /*margin-top: 60px;*/
}
</style>
