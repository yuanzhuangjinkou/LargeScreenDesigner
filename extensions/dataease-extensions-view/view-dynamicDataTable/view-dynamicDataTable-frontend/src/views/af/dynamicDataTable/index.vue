<template>
  <div>
    <view-track-bar
      ref="viewTrack"
      :track-menu="trackMenu"
      class="track-bar"
      :style="trackBarStyleTime"
      @trackClick="trackClick"
    />
    <div class="scroll-container">
      <!-- 遍历数据 -->
      <div v-for="(item, index) in p" :key="index" class="data-row">
        <!-- 动态展示数据 -->
        <div class="back">
          <a-row class="custom-row">
            <img class="custom-img image_21" src="../../../assets/组3908@2x.png" alt="">
            <span class="custom-span">{{ item.name }}</span>
          </a-row>

          <a-row :gutter="20">
            <a-col :align="'left'" :span="12" v-for="(value, key, index) in item" :key="key" v-if="key !== 'name'">
              <div class="data-column">
                <div class="dot"></div>
                <span class="custom-key">{{ param[key] }}</span>
                <span class="custom-value">{{ value }}</span>
                <!-- 温度 -->
                <span v-if="param[key].includes('温度')" class="custom-value"> (℃) </span>
                <!-- 流量 -->
                <span v-if="param[key].includes('流量')" class="custom-value"> (m³/h) </span>
                <!-- 压力 -->
                <span v-if="param[key].includes('压力')" class="custom-value"> (mpa) </span>
              </div>
            </a-col>
          </a-row>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'test',
  data() {
    return {
      param: {
        T1: "温度",
        T2: "瞬时流量",
        T3: "压力",
        T4: "累计流量",
      },
      p: [
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
        console.log('this.active', JSON.stringify(this.active))
        this.scrollStatusChange(newVal)
      }
    },
    chart: {
      handler(newVal, oldVal) {
        console.log('this.chart', JSON.stringify(this.chart))
        if (this.chart) {
          // const val = this.chart.data.x;
          // this.stationClick(val)
        }
      },
      deep: true
    }
  },
};
</script>

<style lang="scss" scoped>
.chart-class {
  height: 100%;
  padding: 10px;
}
.scroll-container {
  width: 100%;
  max-height: 99%; /* Set a maximum height for the container */
  overflow-y: auto; /* Enable vertical scrollbar when content overflows */
}

.custom-row {
  display: flex;
  align-items: center;
}

.group_20 {
  background-color: rgba(24, 254, 254, 1);
  border-radius: 3px;
  width: 26px;
  height: 26px;
  margin-top: 33px;
}

.dot {
  width: 13px;
  height: 13px;
  background: #18FEFE;
  display: inline-block;
  opacity: 1;
  margin-right: 5px; /* Optional: Add margin to create space between the dot and text */
}

.image_21 {
  width: 80px;
  height: 80px;
}

.custom-span {
  font-size: 40px;
}

.data-row {
  margin-bottom: 10px;
  width: 100%;
}

.back {
  width: 727px;
  height: 210px;
  background: linear-gradient(270deg, rgba(24, 254, 254, 0.1) 0%, rgba(24, 254, 254, 0.03) 100%);
  border-radius: 5px 5px 5px 5px;
  opacity: 1;
  border: 2px solid;
  border-image: linear-gradient(270deg, rgba(24.000000469386578, 254.00000005960464, 254.00000005960464, 0.4000000059604645), rgba(24.000000469386578, 254.00000005960464, 254.00000005960464, 0)) 2 2;
}

.data-column {
  display: inline-block;
  margin-left: 20px;
}

.custom-key {
  width: 69px;
  height: 40px;
  font-size: 35px;
  font-weight: 500;
  color: #FFFFFF;
  line-height: 40px;
}

.custom-value {
  width: 34px;
  height: 30px;
  font-size: 45px;
  font-weight: 400;
  color: #18FEFE;
}
</style>
