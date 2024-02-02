<template>
<!--  <div style="display: flex;position:relative" class="chart-class">-->
  <div style="display: flex;position:relative" class="chart-class">
    <view-track-bar
      ref="viewTrack"
      :track-menu="trackMenu"
      class="track-bar"
      :style="trackBarStyleTime"
      @trackClick="trackClick"
    />

<!--    <div @click='clickButton' :class="['cl', buttonClass]">-->
    <div @click='clickButton' class="buttonClass text">
      {{ buttonValue }}
    </div>

  </div>
</template>


<script>

import ViewTrackBar from '../../../components/views/ViewTrackBar'

export default {
  name: 'test',
  components: {
    ViewTrackBar
  },
  data() {
    return {
      buttonValue: '按钮过滤',
      text: 'text'
    };
  },
  methods: {
    clickButton() {
      // 触发联动事件
      this.trackClick('linkage')
    },
    trackClick(trackAction) {

      let id = this.chart.data.fields[0].id
      const linkageParam = {
        option: 'linkage',
        viewId: this.chart.id,
        dimensionList: [{
          id: id,
          value: this.buttonValue
        }],
        quotaList: []
      }
      switch (trackAction) {
        case 'drill':
          // 联动, 看afamap
          this.$emit('onChartClick', this.pointParam)
          break
        case 'linkage':
          this.$emit('plugin-call-back', {
            eventName: 'plugin-add-view-track-filter',
            eventParam: linkageParam
          })
          break
        case 'jump':
          this.$emit('plugin-call-back', {
            eventName: 'plugin-jump-click',
            eventParam: jumpParam
          })
          break
        default:
          break
      }
    },
  },
  props: {
    obj: {
      type: Object,
      required: true
    }
  },
  computed: {
    trackBarStyleTime() {
      return this.trackBarStyle
    },
    active() {
      return this.obj.active
    },
    chart() {
      return this.obj.chart
    },
    filter() {
      return this.obj.filter || {}
    },
    trackMenu() {
      return this.obj.trackMenu || ['drill']
    },
    searchCount() {
      return this.obj.searchCount || 0
    },
    terminalType() {
      return this.obj.terminalType || 'pc'
    }

  },
  watch: {
    active: {
      handler(newVal, oldVla) {
      }
    },
    chart: {
      handler(newVal, oldVal) {
        if (this.chart) {
          const val = this.chart.data.x[0];
          this.buttonValue = val
        }
      },
      deep: true,
      immediate: true, // 首次加载的时候执行函数
    }
  },
};
</script>

<style lang="scss" scoped>
.chart-class {
  height: 100%;
  width: 100%;
  padding: 10px;
  //background-color: red;
}

.buttonClass {
  width: 100%;
  height: 100%;
  //background-color: pink;
  cursor: pointer; /* 设置鼠标指针为默认样式（通常是小手） */
  display: flex;
  align-items: center; /* 垂直居中 */
  font-size: 20px;
  color: #FFFFFF;
}
.text {
  transform: translateX(49%); /* 水平移动相对于自身宽度的30% */
}

</style>
