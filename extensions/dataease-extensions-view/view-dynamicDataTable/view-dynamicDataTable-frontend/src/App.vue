<template>
  <div id="app">
    <dynamicDataTable/>
  </div>
</template>

<script>
import DynamicDataTable from "./views/af/dynamicDataTable";

export default {
  name: 'App',
  components: {
    DynamicDataTable
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
