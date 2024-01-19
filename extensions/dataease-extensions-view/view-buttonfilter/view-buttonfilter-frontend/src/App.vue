<template>
  <div id="app">
    <buttonfilter/>
  </div>
</template>

<script>
import Buttonfilter from "./views/af/buttonfilter";

export default {
  name: 'App',
  components: {
    Buttonfilter
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
