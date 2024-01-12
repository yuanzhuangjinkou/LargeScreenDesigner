<template>
  <div style="display: flex;position:relative" class="chart-class">
    <view-track-bar ref="viewTrack" :track-menu="trackMenu" class="track-bar" :style="trackBarStyleTime"
                    @trackClick="trackClick"/>
    <div class="main-background">
      <div class="main-map">
        <div class="map-content">
          <b-map
            ref="bmap"
            :polylines="polylinePath"
            :marker="elasticMarker"
            :iconSize="iconSize"
            :icon-sizemax="iconSizemax"
            :show-pipe="showPipe"
            :center="this.center"
            :map-zooms="[10, 16]"
            :map-zoom="mapZoom"
          ></b-map>

          <!-- 将按钮放在地图上面，z-index 设置为较大的值 -->
          <div
            style="position: absolute; top: 0; left: 0; background-color: pink; width: 100px; height: 50px; z-index: 100;">
            <select v-model="selectedOption" @change="handleSelectionChange">
              <option value="" disabled selected>请选择</option>
              <option value="营业厅">营业厅</option>
              <option value="气源点">气源点</option>
              <option value="分输站">分输站</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  uuid
} from '@/utils/map'
import BMap from '../BMap';
import ViewTrackBar from '@/components/views/ViewTrackBar'

export default {
  name: "ChartComponent",
  components: {
    BMap,
    ViewTrackBar
  },
  data() {
    return {
      selectedOption: '',
      iconSize: [83, 64],
      iconSizemax: [83, 64],
      elasticMarker: [],
      polylinePath: [],
      showPipe: ['HP'],
      center: [108.63, 34.46],
      offtakeStation: [
        {
          position: [108.32678, 35.090998],
          icon: require("@/assets/images/分输站标点.png"),
          title: "旬邑站",
        },
        {
          position: [108.563096, 34.811142],
          icon: require("@/assets/images/分输站标点.png"),
          title: "淳化站",
        },
        {
          position: [108.619046, 34.44315],
          icon: require("@/assets/images/分输站标点.png"),
          title: "北塬站",
          titleicon: require("@/assets/images/totleicon1.png"),
        },
        {
          position: [108.1547644, 34.6690777],
          icon: require("@/assets/images/分输站标点.png"),
          title: "永寿站",
        },
        {
          position: [108.285781, 34.518688],
          icon: require("@/assets/images/分输站标点.png"),
          title: "乾县站",
        },
        {
          position: [108.134594, 35.008761],
          icon: require("@/assets/images/分输站标点.png"),
          title: "彬州站",
        },
        {
          position: [107.793126, 35.224517],
          icon: require("@/assets/images/分输站标点.png"),
          title: "长武站",
        },
        {
          position: [108.718432, 34.363404],
          icon: require("@/assets/images/分输站标点.png"),
          title: "储配站",
        },
        {
          position: [108.60408335924149, 34.4232274209223],
          icon: require("@/assets/images/分输站标点.png"),
          title: "LNG",
        },
        {
          position: [108.669922, 34.263817],
          icon: require("@/assets/images/分输站标点.png"),
          title: "沣西站",
        },
        {
          position: [108.949354, 34.546865],
          icon: require("@/assets/images/分输站标点.png"),
          title: "永乐站",
        },
        {
          position: [108.854638, 34.536209],
          icon: require("@/assets/images/分输站标点.png"),
          title: "泾阳站",
        },
      ],
      serviceHallLocation: [
        {
          address: "玉泉西路5号",
          phone: 33560005,
          latitude: 34.344288,
          name: "天然气营业大厅",
          longitude: 108.688721,
        },
        {
          address: "中华路（中华小区北门西100米处）",
          phone: 33344112,
          latitude: 34.323527,
          name: "中华路营业厅",
          longitude: 108.685436,
        },
        {
          address: "东风路名仕雅居东门",
          phone: 33237826,
          latitude: 34.3479,
          name: "东风路营业厅",
          longitude: 108.747181,
        },
        {
          address: "民生路国棉一厂大门东侧",
          phone: 33277339,
          latitude: 34.346919,
          name: "民生路营业厅",
          longitude: 108.726409,
        },
        {
          address: "西橡职工活动中心一楼",
          phone: 33627336,
          latitude: 34.316977,
          name: "西橡营业厅",
          longitude: 108.653045,
        },
        {
          address: "宝泉路44号宝泉路加气站",
          phone: 33351879,
          latitude: 34.329115,
          name: "宝泉路营业厅",
          longitude: 108.664636,
        },
        {
          address: "文苑路（咸阳师范学院西门对面）",
          phone: 33351878,
          latitude: 34.367708,
          name: "文苑路营业厅",
          longitude: 108.730673,
        },
        {
          address: "秦皇南路（政务大厅一楼14号柜）",
          phone: 32080096,
          latitude: 34.333184,
          name: "政务大厅专柜",
          longitude: 108.710821,
        },
        {
          address: "文林路9号（社会服务中心一楼9号柜10号柜）",
          phone: 32080096,
          latitude: 34.36832,
          name: "渭城区专柜",
          longitude: 108.743481,
        },
        {
          address: "世纪大道加气站旁边（海天制药对面）",
          phone: 32086305,
          latitude: 34.321763,
          name: "世纪大道营业厅",
          longitude: 108.772445,
        },
        {
          address: "西咸新区沣泾大道西一路1号政务服务中心（118号柜)",
          phone: 33186820,
          latitude: 34.339567,
          name: "西咸新区专柜",
          longitude: 108.788118,
        },
        {
          address: "西咸新区沣西新城政务服务中心（77号柜）",
          phone: 38020944,
          latitude: 34.298194,
          name: "沣西专柜",
          longitude: 108.72196,
        },
        {
          address: "兰池二路秦汉新城政务服务中心（120号柜）",
          phone: null,
          latitude: 34.400168,
          name: "秦汉新城专柜",
          longitude: 108.862261,
        },
      ],
      mapZoom: 11.21,

      myChart: {},
      chartId: uuid(),
      pointParam: null,
    }
  },
  methods: {
    // 测试下拉框
    handleSelectionChange() {
      console.log('select_', this.selectedOption)
      if (this.trackMenu.length < 2) {
        console.log('trackMenu_1', this.trackMenu)
        console.log('< 2')
        // this.trackClick(this.trackMenu[0])
        this.trackClick('linkage')
      }
      // else {
      //   console.log('> 2')
      //   that.trackBarStyle.left = param.event.offsetX + 'px'
      //   that.trackBarStyle.top = (param.event.offsetY - 15) + 'px'
      //   that.$refs.viewTrack.trackButtonClick()
      // }
    },
    // 营业厅坐标
    yytPush() {
      for (let i = 0; i < this.serviceHallLocation.length; i++) {
        let temp = {
          type: "营业厅",
          position: [
            this.serviceHallLocation[i].longitude,
            this.serviceHallLocation[i].latitude,
          ],
          title: this.serviceHallLocation[i].name,
          icon: require("@/assets/images/营业厅地图标点.png"),
          titleicon: require("@/assets/images/营业厅地图标点2.png"),
          labelOffset: {x: 30, y: -62},
          width: 68,
          height: 140,
        };
        this.elasticMarker.push(temp);
      }
    },
    //分输站点坐标
    fszPush() {
      for (let i = 0; i < this.offtakeStation.length; i++) {
        let temp = {
          type: "分输站",
          position: this.offtakeStation[i].position,
          title: this.offtakeStation[i].title,
          icon: require("@/assets/images/分输站地图标点.png"),
          titleicon: require("@/assets/images/分输站地图标点2.png"),
          labelOffset: {x: 30, y: -62},
          width: 68,
          height: 140,
        };
        this.elasticMarker.push(temp);
      }
    },
    starPush() {
      //天然气公司标点
      let star = {
        type: "star",
        position: [108.65, 34.33],
        icon: require("@/assets/star.png"),
        title: "咸阳市天然气总公司",
        titleicon: require("@/assets/标注点标题golden.png"),
      };
      this.elasticMarker.push(star);
    },
    stationClick(val) {
      console.log("首页地图要显示的坐标", val);
      this.elasticMarker = [];
      this.starPush();
      for (let i = 0; i < val.length; i++) {
        if (val[i] === "分输站") {
          console.log("首页地图要显示坐标", val[i]);
          this.fszPush();
          this.center = [108.6688, 34.5658];
          this.mapZoom = 10;
        } else if (val[i] === "营业厅") {
          this.yytPush();
          this.center = [108.6512, 34.3363];
          this.mapZoom = 14.22;
        }
      }
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
  async mounted() {
    var map = this.$refs.bmap.map;
    console.log('map', map)
    // map.setPitch(60);
    map.setZoom(11.0);
    map.setCenter(this.center);
    // this.map.setMapStyle("amap://styles/dfb4b8d0a013071e535e04f4eaf96ce4");
    this.elasticMarker = [];
    this.starPush();
    // this.fszPush()

    this.showPipe = ["HP"];
  },
  // 启动项目需要注释 props, computed
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
          const val = this.chart.data.x;
          this.stationClick(val)
        }
      },
      deep: true
    }
  },
}
</script>

<style lang="scss" scoped>
.main-background,
.main-map,
.main-navigation,
.main-content,
.main-title {
  width: 100vw /* 2603/80 */
;
  height: 100vh /* 679/80 */
;
  position: absolute;
  overflow: hidden;
}

.main-content,
.main-title,
.main-navigation {
  pointer-events: none;
}

.main-background {
  z-index: 10;
  /*  背景图定位 / 背景图尺寸  cover 完全铺满容器  contain 完整显示在容器内 */
  /*background: url(../../assets/images/test/底层背景.png) no-repeat;*/
  /*background-size: 100% 100%;*/
}

.main-map {
  z-index: 50;
  padding: 10px;
  display: flex;
  justify-content: center;

  .map-content {
    width: 100vw;
    height: 100vh;
    position: absolute;
    z-index: 50;
    pointer-events: auto;
  }

  .map-mask-left {
    width: 30vw;
    height: 100vh;
    position: absolute;
    z-index: 1000;
    background-color: #020546;
    filter: blur(6vw);
    left: -4vw;
  }

  .map-mask-right {
    width: 30vw;
    height: 100vh;
    position: absolute;
    z-index: 1000;
    background-color: #020546;
    filter: blur(6vw);
    left: 74vw;
  }

  .map-mask {
    width: 100vw;
    height: 100vh;
    position: absolute;
    z-index: 1000;
    background: url(../../../../assets/mapMask.png) no-repeat;
    background-size: 100% 100%;
    pointer-events: none;
  }

  .map-sky-blur {
    width: 90vw;
    height: 120vh;
    position: absolute;
    z-index: 99;
    background-size: 100% 100%;
    background: url(../../../../assets/mapSkyBlur1.png) no-repeat;
    background-size: 100% 100%;
    /*background: linear-gradient(180deg, #141993 99%, #109AEA 0%);*/
    pointer-events: none;
  }

  .map-sky-blur2 {
    width: 100vw;
    height: 120vh;
    position: absolute;
    z-index: 98;
    background-size: 100% 100%;
    background: url(../../../../assets/mapSkyBlur2.png) no-repeat;
    background-size: 100% 100%;
    /*background: linear-gradient(180deg, #141993 99%, #109AEA 0%);*/
    pointer-events: none;
  }
}

.main-navigation {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  z-index: 151;

  .navigation-left,
  .navigation-center,
  .navigation-right {
    height: 100vh;
  }

  .navigation-left {
    .navigation-left-navbar {
      margin-left: 1.191vw;
      margin-top: 2.06vh;
      width: 8vw;
      height: 5.5vh;
      pointer-events: auto;
    }
  }

  .navigation-center {
  }

  .navigation-right {
    display: flex;
    flex-direction: column;
    align-items: flex-end;

    .navigation-right-navbar {
      width: 15.611vw /* 354.3/26.03 */
    ;
      height: 3.65vh /* 24.8/6.79 */
    ;
      margin-top: 2.06vh /* 14/6.79 */
    ;
      margin-right: 0.3vw;
    }
  }
}

.main-content {
  z-index: 150;
  display: flex;
  flex-direction: column;
  align-items: center;

  .content-header,
  .content-body {
    width: 97.3vw;
  }

  .content-header {
    height: 10vh;
  }

  .content-body {
    width: 98vw;
    height: 90.24vh;
    margin-bottom: 2vh;
    display: flex;
    flex-direction: row;
    justify-content: space-between;

    .left,
    .center,
    .right {
      height: 100%;
    }

    .left {
      width: 18.632vw /* 485/26.03 */
    ;
    }

    .center {
      width: 50%;
      height: 100%;
    }

    .right {
      width: 18.632vw /* 485/26.03 */
    ;
      /*margin-right: vw(34);*/
    }
  }
}

.main-title {
  display: flex;

  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  z-index: 200;

  .title-body {
    width: 100vw;
    height: 84vh;
    display: flex;
    flex-direction: column;
    align-items: center;

    .safe-count {
      margin-right: 4vw;
      width: 19vw /* 300/26.03 */
    ;
      height: 10vh /* 100/6.79 */
    ;
    }
  }
}
.chart-class {
  height: 100%;
  padding: 10px;
}
</style>

