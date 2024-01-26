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
            <img class="custom-title-img" :style="titleImg" src="../../../assets/yuncheng/组3908@2x.png" alt="">
            <span :style="titleStyle">{{ item.name }}</span>
          </a-row>

          <a-row>
            <a-col align="left" :span="12" v-for="(value, key, index) in item" :key="key" v-if="key !== 'name'" style="margin-bottom: 10px">
              <div class="data-column" v-if="orientation === '1'">
                <!-- 点 -->
                  <span class="dot" :style="dot"></span>
                  <span class="custom-key" :style="customKey">{{ zhCN[key] }} &nbsp &nbsp</span>
                  <span class="custom-value" :style="customValue">{{ value }}</span>
                  <!-- Unit -->
                  <span v-if="showUnit(zhCN[key])" class="custom-unit" :style="customUnit">{{ getUnit(zhCN[key]) }}</span>
              </div>
              <div class="data-column" v-if="orientation === '2'">
                <!-- 点 -->
                <div>
                  <span class="dot" :style="dot"></span>
                  <span class="custom-key" :style="customKey">{{ zhCN[key] }} &nbsp &nbsp</span>
                </div>
                <div>
                  <span class="custom-value" :style="customValue">{{ value }}</span>
                  <!-- Unit -->
                  <span v-if="showUnit(zhCN[key])" class="custom-unit" :style="customUnit">{{ getUnit(zhCN[key]) }}</span>
                </div>
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
    };
  },
  methods: {
    showUnit(key) {
      return key.includes('温度') || key.includes('流量') || key.includes('压力');
    },
    getUnit(key) {
      if (key.includes('温度')) return '(℃)';
      if (key.includes('流量')) return '(m³/h)';
      if (key.includes('压力')) return '(mpa)';
      return '';
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
      console.log('trackMenu_', this.obj.trackMenu)
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
      return {
        color: JSON.parse(this.chart.customAttr).titleColor,
        fontSize: JSON.parse(this.chart.customAttr).titleFontSize,
      }
    },
    titleImg() {
      return {
        width: JSON.parse(this.chart.customAttr).titleImgSize,
        height: JSON.parse(this.chart.customAttr).titleImgSize
      }
    },
    dot() {
      return {
        width: JSON.parse(this.chart.customAttr).botFontSIze,
        height: JSON.parse(this.chart.customAttr).botFontSIze,
        background: JSON.parse(this.chart.customAttr).botColor,
      }
    },
    customKey() {
      return {
        fontSize: JSON.parse(this.chart.customAttr).customKeyFontSize,
        fontWeight: JSON.parse(this.chart.customAttr).customKeyFontWeight,
        color: JSON.parse(this.chart.customAttr).customKeyColor,
      }
    },
    customValue() {
      return {
        fontSize: JSON.parse(this.chart.customAttr).customValueFontSize,
        fontWeight: JSON.parse(this.chart.customAttr).customValueFontWeight,
        color: JSON.parse(this.chart.customAttr).customValueColor,
      }
    },
    customUnit() {
      return {
        fontSize: JSON.parse(this.chart.customAttr).customUnitFontSize,
        fontWeight: JSON.parse(this.chart.customAttr).customUnitFontWeight,
        color: JSON.parse(this.chart.customAttr).customUnitColor,
      }
    },
    orientation() {
      return JSON.parse(this.chart.customAttr).orientation
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
          this.zhCN = JSON.parse(this.chart.data.x[0]).zhCN
          this.data = JSON.parse(this.chart.data.x[0]).data
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

.custom-col {
  display: inline-block;
  margin-left: 0; /* 尝试将这个值设置为0 */
}

.scroll-container {
  //--title-font-size: 30px;
  //--title-color: #FFF;
  //--title-img-size: 50px;
  //
  //
  //--bot-font-size: 13px;
  //--bot-color: #18FEFE;
  //
  //--custom-key-font-size: 25px;
  //--custom-key-font-weight: 800;
  //--custom-key-color: #FFF;
  //
  //--custom-value-font-size: 25px;
  //--custom-value-font-weight: 800;
  //--custom-value-color: #18FEFE;

  max-height: 99%;
  width: 100%;
  overflow-y: auto;

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

        .custom-title-img {
          //width: var(--title-img-size);
          //height: var(----title-img-size);
        }

        .custom-title-span {
          //font-size: var(--title-font-size);
          color: var(--title-color);
        }

      }

      .data-column {
        display: inline-block;
        margin-left: 10px;

        .dot {
          //width: var(--bot-font-size);
          //height: var(--bot-font-size);
          //background: var(--bot-color);
          display: inline-block;
          opacity: 1;
          margin-right: 5px;
        }

        .custom-key {
          //width: 69px;
          //height: 40px;
          //font-size: var(--custom-key-font-size);
          //font-weight: var(--custom-key-font-weight);
          //color: var(--custom-key-color);
          line-height: 40px;
        }

        .custom-value {
          //width: 34px;
          //height: 30px;
          //font-size: var(--custom-key-font-size);
          //font-weight: var(--custom-key-font-weight);
          //color: var(--custom-value-color)
        }
      }
    }
  }
}

</style>
