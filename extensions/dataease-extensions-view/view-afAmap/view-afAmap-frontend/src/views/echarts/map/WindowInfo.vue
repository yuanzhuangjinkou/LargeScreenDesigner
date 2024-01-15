<template>
  <div id="homeWindowInfo">
    <div class="title">{{ info.branchName }}</div>
    <div class="content">
      <div class="item">
        <div class="dot"></div>
        <div class="text">检测时间: </div>
        <div class="number">{{ info.f_input_date }}</div>
      </div>

      <div class="item">
        <div class="dot"></div>
        <div class="text">检测指标: </div>
        <div class="number">{{ info.f_low_pressure }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WindowInfo',
  props: {
    info: Object
  },
  components: {},
  data() {
    return {}
  },
  computed: {},
  watch: {
    info: {
      handler(newVal) {
      },
      deep: true
    }
  },
  methods: {},
  mounted() {},
}
</script>

<style lang="scss">
#homeWindowInfo {
  width: 260px;
  height: 126px;
  background-color: rgba(0, 0, 0, .7);
  border-left: #01EA84 5px solid;
  border-right: #01EA84 5px solid;
  border-radius: 3px;
  font-family: medium;
  color: #FFFFFF;

  .title {
    font-size: 16px;
    text-align: center;
    margin-bottom: 6px;
    margin-left: 0px
  }

  .content {
    .item {
      font-size: 16px;
      display: flex;
      flex-direction: row;
      align-items: center;
      position: relative;
      margin: 6px 0;

      .dot {
        width: 10px;
        height: 10px;
        margin: 0 6px;
        background-color: #01EA84;
      }

      .number {
        width: 60px;
        font-family: ysbth;
        color: #01EA84;
        position: absolute;
        right: 60px;
      }
    }
  }
}
</style>
