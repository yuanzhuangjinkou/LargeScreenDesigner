<template>
  <div id="homeWindowInfo" :class="backgroundImg">

    <div class="test">
      <h1 class="title">{{ this.info.text }}</h1>
      <span class="span-test">检测时间: {{ currentDateTime }}</span>
      <br>
      <span class="span-test">检测指标: {{ this.info.value }} </span>
      <br>
      <span class="span-test">报警状态: {{ this.info.status }}</span>
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
        if(newVal.status === '未报警')
          this.backgroundImg = 'backgroundImg_1'
        else
          this.backgroundImg = 'backgroundImg_2'
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
  background-image: url('../../../assets/yuncheng/组4031@2x.png');
}
#homeWindowInfo {
  width: 400px;
  height: 380px;

  background-size: contain;
  background-repeat: no-repeat; // 确保背景图像不重复
  background-position: center; // 将背景图像居中
  color: #FFFFFF;
  margin-bottom: -10px;

  .test {
    padding-top: 6px;

    .title {
      font-size: 28px;
      color: #FFFFFF;
      margin-top: 70px;
      margin-left: 22px;
    }

    .span-test {
      //width: 521px;
      //height: 9px;
      font-size: 25px;
      //font-weight: 400;
      color: #FFFFFF;
      margin-left: 30px;
      //line-height: 8px;
    }
  }


}
</style>
