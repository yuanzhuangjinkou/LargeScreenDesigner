<template>
  <!--  <div style="display: flex;position:relative" class="chart-class">-->
  <div class="div">
    <view-track-bar
      ref="viewTrack"
      :track-menu="trackMenu"
      class="track-bar"
      :style="trackBarStyleTime"
      @trackClick="trackClick"
    />
    <div>
      <span :style="textStyle" class="span">{{ text }}</span>
      <span v-if="unit !== null" :style="unitStyle" class="span unit">({{ unit }})</span>
    </div>

  </div>
</template>

<script>

import viewTrackBar from '../../../components/views/ViewTrackBar'

export default {
  // import vie
  name: 'index',
  components: {
    viewTrackBar
  },
  data() {
    return {
      text: "这是一个示例",
      unit: "单位"
    };
  },
  methods: {
    trackClick(trackAction) {

      let id = null;
      this.chart.data.sourceFields.forEach(field => {
        if (field.originName === 'province') {
          id = field.id;
        }
      });
      const linkageParam = {
        option: 'linkage',
        viewId: this.chart.id,
        dimensionList: [{
          id: id,
          value: this.selectedOption
        }],
        quotaList: []
      }
      switch (trackAction) {
        case 'drill':
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
    },

    // 样式计算
    textStyle() {
      return {
        fontSize: JSON.parse(this.chart.customAttr).fontSize,
        fontWeight: JSON.parse(this.chart.customAttr).fontWeight,
        color: JSON.parse(this.chart.customAttr).color,
        letterSpacing: JSON.parse(this.chart.customAttr).letterSpacing,
      }
    },
    unitStyle() {
      return {
        color: JSON.parse(this.chart.customAttr).color,
        fontSize: JSON.parse(this.chart.customAttr).unitFontSize,
      }
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
          console.log('1234')
          this.text = JSON.parse(this.chart.data.x[0].substring(6)).value
          this.unit = JSON.parse(this.chart.data.x[0].substring(6)).hasOwnProperty('unit') ? JSON.parse(this.chart.data.x[0].substring(6)).unit : null
          console.log('this.unit', this.unit)
          console.log('this.text', this.text)
          this.$forceUpdate();
        }
      },
      deep: true,
      immediate: true, // 首次加载的时候执行函数
    }
  },
};
</script>

<style lang="scss" scoped>
@font-face {
  font-family: 'hanziti';
  src: url('../../../utils/优设标题黑.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}
.div {
}

.span {
  //width: 325px;
  //height: 25px;
  //font-size: 60px;
  font-family: 'hanziti', sans-serif;
  //font-weight: 400;
  //color: #FFEC49;
  //line-height: 25px;
  //letter-spacing: 5px;
  -webkit-background-clip: text;
  //-webkit-text-fill-color: transparent;
}

.unit {
  //font-size: 40px;
}

</style>
