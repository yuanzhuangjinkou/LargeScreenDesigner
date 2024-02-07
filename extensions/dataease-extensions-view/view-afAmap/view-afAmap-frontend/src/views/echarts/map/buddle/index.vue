<template>
  <div id="left-of-top" style="display: flex;position:relative" class="chart-class">
    <!-- 地图 -->
    <div class="mapbox">
      <div class="blur-left blur"/>
      <div class="blur-right blur"/>
      <BMap class="map"
            :show-pipe-layer="showPipeLayer"
            :marker-list="markerList"
            @marker-click="markerClick"
            @map-click="windowClose"
            @polygon-click="windowClose"
            :highlight-company-name="highlightCompanyName"
      />
    </div>

    <!-- 弹窗 -->
    <window-info :info="infoWindow.info"></window-info>

  </div>

</template>

<script>
import {
  uuid
} from '../../../../utils/map'
import BMap from '../BMap';
import WindowInfo from "../WindowInfo.vue";
import ViewTrackBar from '../../../../components/views/ViewTrackBar'
import {getNumber} from '../../../../api'
import axios from "axios";

export default {
  name: "ChartComponent",
  components: {
    BMap,
    ViewTrackBar,
    WindowInfo
  },
  data() {
    return {
      // 需要高亮显示的公司名
      highlightCompanyName: "",
      showPipeLayer: [],
      // 分公司范围中心和同行公司标记点
      // position: 坐标, type: 展示图片样式, title: 分类, text: 名称, isShow: 是否展示
      markerList: [
        {position: [110.97, 35.05], type: 1, title: '场站', text: '永济民生门站', isShow: true},
        {position: [111.00, 35.08], type: 1, title: '场站', text: '运城民生北门站', isShow: true},
        {position: [111.01, 35.06], type: 1, title: '场站', text: '运城民生东门站', isShow: true},
        // {position: [111.04,35.06], type: 2, title: '调压箱', text: '调压箱', isShow: false},
        // {position: [110.98, 35.07], type: 6, title: '分输站', text: '分输站', isShow: false},
        {position: [111.02, 35.03], type: 6, title: '营业厅', text: '营业厅', isShow: false},
        // {position: [111.02, 35.03], type: 6, title: '气源点', text: '气源点', isShow: false},
        {position: [111.02, 35.03], type: 6, title: '加气站', text: '加气站', isShow: false},
      ],
      infoWindow: {
        window: new AMap.InfoWindow({
          isCustom: true,
          autoMove: false,
          offset: new AMap.Pixel(56, 90),
          closeWhenClickMap: false
        }),
        info: {
          branchName: '',
          residentsCount: 0,
          nonResidentCount: 0,
          pipelineLength: 0
        },
      },


      selectedOption: '',
      iconSize: [83, 64],
      iconSizemax: [83, 64],
      elasticMarker: [],
      polylinePath: [],
      showPipe: ['HP'],
      center: [111.01, 35.03],
      mapZoom: 11.21,

      myChart: {},
      chartId: uuid(),
      pointParam: null,
    }
  },
  methods: {
    async initData() {
      // 天津智动 调压箱坐标数据
      await axios.post('/logic/deviceApi', {})
        .then(res => {
          const resultArray = res.data.map(item => {
            return {
              deviceId: item.deviceId,
              position: [
                parseFloat(item.coordinate[0]),
                parseFloat(item.coordinate[1])
              ],
              type: 2,
              title: '调压箱',
              text: item.deviceName,
              isShow: false
            };
          });
          this.markerList = this.markerList.concat(resultArray);
        })
        .catch(err => {
          console.log('err: ', err)
        })
    },

    async markerClick(e) {
      // 获取点击的点的名称,用来判断是否需要显示弹窗
      let title = e.target.De.title
      let text = e.target.De.text
      let deviceId = e.target.De.deviceId

      if(title === '调压箱') {
        // 展示数据: 名称, 时间, 状态,
        await axios.post('/logic/deviceApi', {"deviceId": deviceId})
          .then(res => {
            this.infoWindow.info =
              {
                'title': title,
                'text': res.data.name,
                'data': {
                  '报警状态': res.data.status,
                  '检测指标': res.data.value
                }
              };
          })
      } else if(title === '场站') {
        await axios.post('/logic/stationDataApi', {"stationName": text})
          .then(res => {
            console.log('res: ', res)
            this.infoWindow.info =
              {
                'title': title,
                'text': res.data[0].name,
                'data': {
                  '温度': res.data[0].temperature,
                  '压力': res.data[0].pressure,
                  '累计流量': res.data[0].gkljll
                }
              };
          })
      }

      // 遍历标记点隐藏被点击的
      this.markerList.forEach(item => item.isShow = item.title === title)

      // 打开弹窗
      this.infoWindow.window.setContent(
        document.getElementById("homeWindowInfo")
      );
      this.infoWindow.window.open(
        e.target.getMap(),
        e.target.getPosition()
      );
      e.target.getMap().setCenter(e.lnglat.offset(0, 0))
    },
    // 地图点击事件
    windowClose() {
      // 遍历标记点显示被隐藏的
      // this.markerList.forEach(item => item.isShow = true)
      // 关闭弹窗
      this.infoWindow.window.close()
    },

    // 测试下拉框
    handleSelectionChange() {
      if (this.trackMenu.length < 2) {
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
    stationClick(val) {
      if(val[0] === '管线') {
        this.showPipeLayer = ['HP']
      }
      else {
        this.showPipeLayer = []
      }
      this.markerList = this.markerList.map(obj => {
        if (obj.title === val[0]) {
          return {...obj, isShow: true};
        } else {
          return {...obj, isShow: false};
        }
      });
    },

    trackClick(trackAction) {

      let id = null;
      this.chart.data.sourceFields.forEach(field => {
        if (field.originName === 'province') {
          id = field.id;
        }
      });

      // let id = this.chart.data.fields[0].id

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
  mounted() {
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
      async handler(newVal, oldVal) {
        // console.log('afamap.chart', this.chart)
        if (this.chart) {
          const val = this.chart.data.x;
          await this.initData()
          this.stationClick(val)
        }
      },
      deep: true,
      immediate: true
    }
  },
}
</script>

<style lang="scss" scoped>
#left-of-top {
  width: 1920px;
  height: 1080px;
  overflow: hidden;
  // margin-left: -68px;

  .mapbox {
    .map-mask {
      //height: 4000px;
      //width: 1000px;
      left: 56%;
      top: -20%;
      position: absolute;
      z-index: 2;
      filter: blur(200px);
      background-color: rgb(4, 20, 25);
    }

    .map {
      z-index: 1;
    }
  }
}

.blur {
  width: 20%;
  height: 100%;
  z-index: 2;
  position: absolute;
  //left: 0%;
  //top: 0%;
  backdrop-filter: blur(6px);
  background-color: rgba(0, 254, 254, 0);
}

.blur-left {
}

.blur-right {
  right: 0;
}

</style>

