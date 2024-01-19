<template>
<!--  <div style="display: flex;position:relative" class="chart-class">-->
  <div>
<!--    <view-track-bar-->
<!--      ref="viewTrack"-->
<!--      :track-menu="trackMenu"-->
<!--      class="track-bar"-->
<!--      :style="trackBarStyleTime"-->
<!--      @trackClick="trackClick"-->
<!--    />-->
    <div class="scroll-container">
      <!-- 遍历数据 -->
      <div v-for="(item, index) in data" :key="index" class="data-row">
        <!-- 动态展示数据 -->
        <div class="back">
          <a-row class="custom-row">
            <img class="custom-img" src="../../../assets/yuncheng/组3908@2x.png" alt="">
            <span class="custom-span">{{ item.name }}</span>
          </a-row>

          <a-row >
            <a-col align="left" :span="12" v-for="(value, key, index) in item" :key="key" v-if="key !== 'name'">
              <div class="data-column">
                <!-- 点 -->
                <div class="dot"></div>
                <span class="custom-key">{{ zhCN[key] }}</span>
                <span class="custom-value">{{ value }}</span>
                <!-- 温度 -->
                <span v-if="zhCN[key].includes('温度')" class="custom-value"> (℃) </span>
                <!-- 流量 -->
                <span v-if="zhCN[key].includes('流量')" class="custom-value"> (m³/h) </span>
                <!-- 压力 -->
                <span v-if="zhCN[key].includes('压力')" class="custom-value"> (mpa) </span>
              </div>
            </a-col>
          </a-row>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import viewTrackBar from '../../../components/views/ViewTrackBar'

export default {
  // import vie
  name: 'test',
  components: {
    viewTrackBar
  },
  data() {
    return {
      zhCN: {
        T1: "温度",
        T2: "瞬时流量",
        T3: "压力",
        T4: "累计流量",
      },
      data: [
        {
          name: "667场站",
          T1: 1,
          T2: 2,
          T3: 3,
          T4: 4,
        },
        {
          name: "668场站",
          T1: 1,
          T2: 2,
          T3: 3,
          T4: 4,
        },
        {
          name: "669场站",
          T1: 1,
          T2: 2,
          T3: 3,
          T4: 4,
        },
        {
          name: "700场站",
          T1: 1,
          T2: 2,
          T3: 3,
          T4: 4,
        },
        {
          name: "770场站",
          T1: 1,
          T2: 2,
          T3: 3,
          T4: 4,
        },
        {
          name: "771场站",
          T1: 1,
          T2: 2,
          T3: 3,
          T4: 4,
        },
      ],
      size: '40px'
    };
  },
  methods: {
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
        console.log('dynamicDataTable_this.active', JSON.stringify(this.active))
      }
    },
    chart: {
      handler(newVal, oldVal) {
        console.log('this.obj', JSON.stringify(this.obj))
        console.log('dynamicDataTable_this.chart', JSON.stringify(this.chart))
        if (this.chart) {
          this.zhCN = JSON.parse(this.chart.data.x[0]).zhCN
          this.data = JSON.parse(this.chart.data.x[0]).data
          // console.log('this.zhCN', this.zhCN)
          // console.log('this.data', this.data)
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
.chart-class {
  height: 100%;
  width: 100%;
  padding: 10px;
}

.scroll-container {
  max-height: 99%;
  width: 100%;
  overflow-y: auto;
  //background-color: black;
  .data-row {
    margin-bottom: 10px;

    .back {
      width: 100%;
      height: 100%;
      background: linear-gradient(270deg, rgba(24, 254, 254, 0.1) 0%, rgba(24, 254, 254, 0.03) 100%);
      border-radius: 5px;
      opacity: 1;
      border: 2px solid;
      border-image: linear-gradient(270deg, rgba(24, 254, 254, 0.4), rgba(24, 254, 254, 0)) 2 2;

      .custom-row {
        display: flex;
        align-items: center;

        .custom-img {
          width: 51px;
          height: 51px;
        }

        .custom-span {
          font-size: 26px;
          color: #FFF;
        }
      }
    }
  }
}

.data-column {
  display: inline-block;
  margin-left: 20px;

  .dot {
    width: 13px;
    height: 13px;
    background: #18FEFE;
    display: inline-block;
    opacity: 1;
    margin-right: 5px;
  }

  .custom-key {
    width: 69px;
    height: 40px;
    font-size: 25px;
    font-weight: 500;
    color: #FFF;
    line-height: 40px;
  }

  .custom-value {
    width: 34px;
    height: 30px;
    font-size: 25px;
    font-weight: 400;
    color: #18FEFE;
  }
}
</style>
