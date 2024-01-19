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
      console.log('点击了')
      // this.buttonClass = '_default_51'
      // 触发联动事件
      this.trackClick('linkage')
    },
    trackClick(trackAction) {
      console.log('trackClick_', trackAction)

      // 每个视图 此处组织联动数据数据各不相同
      // this.chart.data.sourceFields.forEach(field => {
      //   if (field.originName === '图层1') {
      //     id = field.id;
      //   }
      // });
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
      console.log('trackMenu_', this.obj.trackMenu)
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
        console.log('this.active', JSON.stringify(this.active))
      }
    },
    chart: {
      handler(newVal, oldVal) {
        console.log('this.obj_buttonfilter', this.obj)
        console.log('this.chart_buttonfilter', JSON.stringify(this.chart))
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
