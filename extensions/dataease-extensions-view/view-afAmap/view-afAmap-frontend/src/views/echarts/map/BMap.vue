<template>
  <div id='mymap'></div>
</template>

<script>

export default {
  title: "高德地图",
  props: {
    // 显示哪些管线
    showPipeLayer: {
      type: Array,
      default: []
    },
    // 轨迹
    // pathSimplifier: {
    //   type: Array,
    //   default: [],
    // },
    // 场站标记点
    markerList: {
      type: Array,
      default: [],
    },
    // 高亮分公司名称
    highlightCompanyName: {
      type: String,
      default: ''
    },
    // 巡检人员轨迹点
    coordinatesOfInspectorsList: {
      type: Object,
      default: function () {
        return { /* your default object here */};
      }
    },
  },
  data() {
    return {
      // 地图对象
      map: null,
      // 高压管线对象
      pipeHp: null,
      // 轨迹对象
      simplifier: null,
      // 当前屏幕尺寸
      pageWidth: null,
      // 所有polygon对象
      allPolygon: [],
      // 已激活高亮的对象
      highlightPolygon: null,
      markers: [],
      personnel: [],
      infoWindow: null
    }
  },
  methods: {
    // 初始化地图
    initMap() {
      this.map = new AMap.Map('mymap', {
        zooms: [11, 20],
        zoom: 13,
        showBuildingBlock: false,
        center: [111.01, 35.03],
        mapStyle: 'amap://styles/3356249ed35bc7ebc6c673c57461fc86',
        showLabel: true,
      });
      this.map.on("click", (e) => {
        this.$emit("map-click", e);
      });
    },
    // 初始化管线
    initPipe() {
      console.log('initpipe')
      this.pipeHp = new AMap.TileLayer.WMS({
        url: "http://120.46.92.69:8765/geoserver/yuncheng/wms",
        blend: false,
        tileSize: 512,
        zIndex: 999999,
        visible: true,
        zooms: [0, 20],
        params: {
          LAYERS: "yuncheng:yuncheng",
          VERSION: "1.1.1",
          FORMAT: "image/png",
          SRS: "EPSG:2382",
        },
      });
      this.pipeHp.setMap(this.map)
      this.pipeHp.hide();
      // this.pipeHp.show();
    },
    // 创建标记点
    createMarkers() {
      let _this = this;

      if (!this.map) {
        this.initMap();
      }

      AMap.plugin('AMap.Marker', () => {
        // 如果不将数组倒转，总公司图标就会在最底层，被遮住
        let reverseMarkerList = [];

        console.warn(">>> 标记点个数：", this.markerList.length)

        for (let i = this.markerList.length - 1; i >= 0; i--) {
          let markerItem = this.markerList[i];
          reverseMarkerList.push(markerItem);
        }
        reverseMarkerList.forEach(position => {
          if (position.isShow) {
            let marker = new AMap.Marker({
              position: position.position,
              title: position.title,
              text: position.text,
              deviceId: position.deviceId,
              icon: '//webapi.amap.com/images/0.png',
              content: '<div class="marker bg-marker' + position.type + ' ' + position.icon + '"><div class="text">' + position.text + '</div></div>',
              offset: new AMap.Pixel(0, 0),
            });

            marker.on("click", (e) => {
              console.log('>>> 标记点被点击 -> ', e)
              _this.$emit("marker-click", e)
            });

            marker.setMap(_this.map);
            this.markers.push(marker)
          }
        })
      });
    },
    // 清空地图标记点
    removeMarkers() {
      this.markers.forEach(marker => {
        marker.setMap(null); // 将标记点从地图上移除
      });
      this.markers = []; // 清空存储标记点的数组
    },
    // 初始化范围
    initBoarder() {
    },
    // 初始化其他公司边界
    initOtherCompanyBoarder() {
      if (this.otherCompanyBoarder.length > 0) {
        let boarder = [];
        let _this = this;
        for (let i = 0; i < this.otherCompanyBoarder.length; i++) {
          // 初始化矢量图形
          let polygon = new AMap.Polygon({
            path: this.otherCompanyBoarder[i],
            strokeColor: '#0f714e',
            fillColor: '#002e2e',
            fillOpacity: 0.5,
            zIndex: 5
          });

          // 绑定鼠标单击事件
          polygon.on("click", function (e) {
            _this.$emit("polygon-click", e);
          })

          boarder.push(polygon)
        }
        // 地图绑定
        this.map.add(boarder)
      }
    }
  },
  mounted() {
    let _this = this;
    this.initMap();
    this.initPipe();
    this.createMarkers()
  },
  created() {
  },
  watch: {
    // 监控展示哪些管线的数组
    showPipeLayer: {
      handler(newVal) {
        if(newVal.length > 0) {
          this.pipeHp.show();
        } else {
          this.pipeHp.hide();
        }
      },
      deep: true
    },
    markerList: {
      handler(newVal) {
        if (this.infoWindow !== null) {
          this.infoWindow.close()
        }
        this.removeMarkers()
        // 重新创建
        this.createMarkers()
      },
      deep: true
    },
  }
};
</script>

<style lang="scss">
#mymap {
  width: 1920px;
  height: 1080px;

  // 点击密闭空间时的弹窗
  .amap-info-content {
    background: transparent;

    // 关闭按钮
    .amap-info-close {
      display: none;
    }

    // 窗口样式
    .custom-window {
      width: 168px;
      padding: 6px 2px 6px 8px;
      background-color: rgba(0, 41, 47, .9);
      border-bottom: 1px solid #00FF90;
      border-top: 1px solid #00FF90;
      border-radius: 6px;
      font-size: 16px;
    }
  }

  .personnel-trajectory {
    width: 26px;
    height: 26px;
    background-size: 26px 26px;
  }

  .marker {
    width: 220px;
    height: 60px;
    color: #fff;
    display: flex;
    white-space: nowrap;
    align-items: center;
    background-size: 36px;
    background-repeat: no-repeat;
    background-position: left center;
    .text {
      margin-left: 70px;
      margin-bottom: 20px;
      font-size: 20px;
    }
  }

  // 首页分公司
  .bg-marker6 {
    background-image: url("../../../assets/yuncheng/组3248@2x.png");
    background-size: 100% 100%;
  }

  .bg-marker1 {
    background-image: url("../../../assets/yuncheng/组4019@2x.png");
    background-size: 100% 100%
  }

  .bg-marker2 {
    background-image: url("../../../assets/yuncheng/组4085@2x.png");
    background-size: 100% 100%;
  }

}
</style>
