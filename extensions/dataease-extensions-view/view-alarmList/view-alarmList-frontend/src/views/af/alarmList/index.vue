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
    <div class="table">
      <span v-for="(item, index) in zhCN" :key="index" :style="titleStyle">{{item}}</span>
    </div>
    <div class="valueClass" v-for="(item, index) in data" :style="valueDivStyle">
      <span v-for="key in Object.keys(zhCN)" :key="key" :class="getColumnClass(key)" :style="valueStyle">
        {{ item[key] + value(key)}}
      </span>
    </div>
  </div>
</template>

<script>

import viewTrackBar from '../../../components/views/ViewTrackBar'

export default {
  name: 'index',
  components: {
    viewTrackBar
  },
  data() {
    return {
      zhCN: {
        T1: "名称",
        T2: "气体浓度"
      },
      data: [
        {"T1": "控制器A", "T2": "121"},
        {"T1": "控制器B", "T2": "121"},
        {"T1": "控制器C", "T2": "121"},
        {"T1": "控制器D", "T2": "121"},
        {"T1": "控制器A", "T2": "121"}
      ]
    };
  },
  mounted() {
    this.highlightRows();
  },
  methods: {
    getColumnClass(key) {
      // 根据列名判断是否为气体浓度列
      if (this.zhCN[key] === '气体浓度') {
        return ['gas-concentration'];
      }
      return ['white'];
    },
    value(key) {
      if(this.zhCN[key] === '气体浓度')
        return ' (ppm)'
      return ''
    },
    highlightRows() {
      const rows = document.querySelectorAll('.custom-table tbody tr');
      rows.forEach((row, index) => {
        if (index % 2 === 0) {
          row.classList.add('highlighted-row');
        }
      });
    },
    trackClick(trackAction) {
      console.log('trackClick_', trackAction)

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
    titleStyle() {
      console.log('计算属性: ', this.chart)
      console.log('计算属性: ', JSON.parse(this.chart.customAttr))
      return {
        fontSize: JSON.parse(this.chart.customAttr).titleFontSize,
      }
    },
    valueStyle() {
      return {
        fontSize: JSON.parse(this.chart.customAttr).valueFontSize,
      }
    },
    valueDivStyle() {
      return {
        marginBottom: JSON.parse(this.chart.customAttr).valueMarginBottom,
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
        console.log('alarmList.obj', JSON.stringify(this.obj))
        if (this.chart) {
          this.zhCN = JSON.parse(this.chart.data.x[0].substring(6)).zhCN
          this.data = JSON.parse(this.chart.data.x[0].substring(6)).data
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


.table {
  display: flex;
  margin-bottom: 3px;
  margin-top: 15px;
}
.table span {
  color: #ffffff;
  flex: 1;
  text-align: center;
}

.valueClass {
  background: linear-gradient(270deg, rgba(24,254,254,0.25) 0%, rgba(24,254,254,0.05) 100%);
  display: flex;
}
.valueClass span {
  flex: 1;
  text-align: center;
}

.gas-concentration {
  font-family: hanziti, serif;
  color: #18FEFE;
}
.white {
  color: #ffffff;
}

</style>
