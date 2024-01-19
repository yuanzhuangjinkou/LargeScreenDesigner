<template>
  <div id="homeWindowInfo" :class="backgroundImg">

    <div class="test">
      <h1 class="title">{{ this.info.name }}</h1>
      <span class="span-test">检测时间: {{ currentDateTime }}</span>
      <br>
      <span class="span-test">检测指标: {{  }} </span>
      <br>
      <span class="span-test">检测指标: {{  }}</span>
      <br>
      <span class="span-test">检测指标: {{  }}</span>
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
    return {
      currentDateTime: '',
      backgroundImg: 'backgroundImg_1'
    }
  },
  computed: {},
  watch: {
    info: {
      handler(newVal) {
        let n = Math.floor(Math.random() * 2 + 1)
        console.log('>>> WindowsInfo --> ', newVal, n)
        if(newVal.name === '场站')
          this.backgroundImg = 'backgroundImg_1'
        else if(newVal.name === '调压箱')
          this.backgroundImg = 'backgroundImg_2'
        else
          this.backgroundImg = 'backgroundImg_' + n;
      },
      deep: true
    }
  },
  methods: {
    updateDateTime() {
      const now = new Date();
      const year = now.getFullYear();
      const month = (now.getMonth() + 1).toString().padStart(2, '0');
      const day = now.getDate().toString().padStart(2, '0');
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');

      this.currentDateTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }
  },
  mounted() {
    this.updateDateTime();
  },
}
</script>

<style lang="scss">
.backgroundImg_1 {
  background-image: url('../../../assets/yuncheng/组4032@2x.png');
}
.backgroundImg_2 {
  background-image: url('../../../assets/组4031@2x.png');
}
#homeWindowInfo {
  width: 430px;
  height: 300px;

  background-size: contain;
  background-repeat: no-repeat; // 确保背景图像不重复
  background-position: center; // 将背景图像居中
  color: #FFFFFF;
  margin-bottom: -10px;

  .test {
    padding-top: 6px;

    .title {
      font-size: 30px;
      color: #FFFFFF;
      margin-top: 30px;
      margin-left: 30px;
    }

    .span-test {
      //width: 521px;
      //height: 9px;
      font-size: 28px;
      //font-weight: 400;
      color: #FFFFFF;
      margin-left: 30px;
      //line-height: 8px;
    }
  }


}
</style>
