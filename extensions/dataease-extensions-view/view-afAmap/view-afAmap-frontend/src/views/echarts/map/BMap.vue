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
      default: ['HP']
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
    // 秦华各分公司区域
    companyBoarder: {
      type: Array,
      default: [],
    },
    // 其它周边天然气公司区域
    otherCompanyBoarder: {
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
      default:  function() {
        return { /* your default object here */ };
      }
    },
  },
  data() {
    return {
      // 地图对象
      map: null,
      // 高压管线对象
      pipeHp: null,
      // 中压管线对象
      pipeMp: null,
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
    // 将地图动态缩放回原始大小
    // resize() {
    //   this.pageWidth = document.documentElement.clientWidth;
    //   const mapNode = document.getElementById('mymap');
    //
    //   let scale = 1;
    //   scale = this.pageWidth / 5760;
    //
    //   console.warn('已检测到大小变化，已动态调整地图大小，当前缩放：', scale)
    //
    //   mapNode.style.transform = `scale(${1 / scale})`;
    // },
    // 初始化地图
    initMap() {
      this.map = new AMap.Map('mymap', {
        zooms: [11, 20],
        zoom: 13,
        showBuildingBlock: false,
        center: [111.01, 35.03],
        // mapStyle: 'amap://styles/dfb4b8d0a013071e535e04f4eaf96ce4',
        mapStyle: 'amap://styles/3356249ed35bc7ebc6c673c57461fc86',
        showLabel: true,
      });
      console.warn('map123', this.map)
      this.map.on("click", (e) => {
        this.$emit("map-click", e);
      });
    },
    // 初始化管线
    initPipe() {
      this.pipeHp = new AMap.TileLayer.WMS({
        url: "http://10.10.90.191:8787/geoserver/qinhua/wms",
        blend: false,
        tileSize: 512,
        zIndex: 999999,
        visible: true,
        zooms: [0, 20],
        params: {
          LAYERS: "qinhua:HP",
          VERSION: "1.1.0",
          FORMAT: "image/png",
          SRS: "EPSG:3857",
        },
      });
      this.pipeMp = new AMap.TileLayer.WMS({
        url: "http://10.10.90.191:8787/geoserver/qinhua/wms",
        blend: false,
        tileSize: 512,
        zIndex: 999999,
        visible: true,
        zooms: [0, 20],
        params: {
          LAYERS: "qinhua:MP",
          VERSION: "1.1.0",
          FORMAT: "image/png",
          SRS: "EPSG:3857",
        },
      });
      this.pipeHp.setMap(this.map)
      this.pipeMp.setMap(this.map)
      this.pipeHp.hide();
      this.pipeMp.hide();
    },
    // 创建标记点
    createMarkers() {
      let _this = this;

      if(!this.map) {
        this.initMap();
      }

      AMap.plugin('AMap.Marker', () => {
        // 如果不将数组倒转，总公司图标就会在最底层，被遮住
        let reverseMarkerList = [];

        console.warn(">>> 标记点个数：", this.markerList.length)

        for(let i = this.markerList.length - 1; i >= 0; i--) {
          let markerItem = this.markerList[i];
          reverseMarkerList.push(markerItem);
        }
        console.log('reverseMarkerList_', reverseMarkerList)
        reverseMarkerList.forEach(position => {
          if(position.isShow) {
            let marker = new AMap.Marker({
              position: position.position,
              title: position.title,
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
      if(this.companyBoarder.length > 0) {
        let boarder = [];
        let _this = this;
        for(let i = 0; i < this.companyBoarder.length; i++) {
          // 初始化矢量图形
          // let polygon = new AMap.Polygon({
          //   path: this.companyBoarder[i].path,
          //   strokeColor: '#00feab',
          //   fillColor: '#99ff99',
          //   fillOpacity: 0.2
          // });
          // polygon.companyName = this.companyBoarder[i].name;
          // 绑定鼠标移入高亮事件
          polygon.on("mouseover", function(e) {
            let polygon = e.target;
            _this.highlightPolygon = polygon;
            polygon.setOptions({
              strokeColor: '#fde311',
              fillColor: '#ffff99',
              fillOpacity: 0.5,
              zIndex: 20
            })
          });
          // 绑定鼠标移出还原高亮事件
          polygon.on("mouseout", function(e) {
            let polygon = e.target;
            _this.highlightPolygon = null;
            polygon.setOptions({
              strokeColor: '#00feab',
              fillColor: '#99ff99',
              fillOpacity: .2,
              zIndex: 10
            })
          })
          // 绑定鼠标单击事件
          polygon.on("click", function(e) {
            _this.$emit("polygon-click", e);
          })
          this.allPolygon.push(polygon)
          boarder.push(polygon)
        }
        // 地图绑定
        this.map.add(boarder)
      }
    },
    // 初始化其他公司边界
    initOtherCompanyBoarder() {
      if(this.otherCompanyBoarder.length > 0) {
        let boarder = [];
        let _this = this;
        for(let i = 0; i < this.otherCompanyBoarder.length; i++) {
          // 初始化矢量图形
          let polygon = new AMap.Polygon({
            path: this.otherCompanyBoarder[i],
            strokeColor: '#0f714e',
            fillColor: '#002e2e',
            fillOpacity: 0.5,
            zIndex: 5
          });

          // 绑定鼠标单击事件
          polygon.on("click", function(e) {
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
    console.log('bmap_mounted')
    let _this = this;
    this.initMap();
    // 缩放回原始大小
    // this.resize();
    // // 绑定事件，根据屏幕比例，重新定义大小
    // window.addEventListener('resize', this.resize)
    this.initPipe();
    // 根据默认值来初始化管线显示
    for(let i = 0; i < this.showPipeLayer.length; i++) {
      let temp = this.showPipeLayer[i];
      if(temp === 'HP') {
        this.pipeHp.show();
      }
      if(temp === 'MP') {
        this.pipeMp.show();
      }
    }
    this.createMarkers()
    // this.initBoarder()
    // this.initOtherCompanyBoarder()
    // this.createCoordinatesOfInspectors()
    // 轨迹展示初始化
    // AMapUI.loadUI(["misc/PathSimplifier"], function(PathSimplifier) {
    //   if(!PathSimplifier.supportCanvas) {
    //     alert("当前环境不支持轨迹展示！");
    //     return;
    //   }
      //创建组件实例
      // _this.simplifier = new PathSimplifier({
      //   zIndex: 103,
      //   map: _this.map, //所属的地图实例
      //   autoSetFitView: false,
      //   getPath: function(pathData, pathIndex) {
      //     //返回轨迹数据中的节点坐标信息，[AMap.LngLat, AMap.LngLat...] 或者 [[lng|number,lat|number],...]
      //     return pathData.path;
      //   },
      //   getHoverTitle: function(pathData, pathIndex, pointIndex) {
      //     //返回鼠标悬停时显示的信息
      //     if(pointIndex >= 0) {
      //       //鼠标悬停在某个轨迹节点上
      //       return (
      //         pathData.name +
      //         "，点:" +
      //         (pointIndex + 1) +
      //         "/" +
      //         pathData.path.length
      //       );
      //     }
      //     //鼠标悬停在节点之间的连线上
      //     return pathData.name + "，点数量" + pathData.path.length;
      //   },
      //   renderOptions: {
      //     // 轨迹线样式
      //     pathLineStyle: {
      //       strokeStyle: "#52C4FF",
      //       lineWidth: 5,
      //       borderStyle: "#52C4FF",
      //       borderWidth: 0,
      //       dirArrowStyle: true,
      //     },
      //     // 轨迹线悬停样式
      //     pathLineHoverStyle: {
      //       strokeStyle: "#52C4FF",
      //       lineWidth: 8,
      //       borderStyle: "#52C4FF",
      //       borderWidth: 0,
      //       dirArrowStyle: true,
      //     },
      //     // 轨迹线选中样式
      //     pathLineSelectedStyle: {
      //       strokeStyle: "#52C4FF",
      //       lineWidth: 5,
      //       borderStyle: "#52C4FF",
      //       borderWidth: 0,
      //       dirArrowStyle: true,
      //     },
      //     // 起点样式
      //     startPointStyle: {
      //       radius: 0,
      //     },
      //     // 终点样式
      //     endPointStyle: {
      //       radius: 0,
      //     },
      //     // 显示文字样式
      //     hoverTitleStyle: {
      //       classNames: "path-simplifier-title",
      //     },
      //     // 巡航器样式
      //     // pathNavigatorStyle: {
      //     //   width: 120,
      //     //   height: 200,
      //     //   autoRotate: false,
      //     //   content: PathSimplifier.Render.Canvas.getImageContent(
      //     //     './public/巡检.png',
      //     //     onload,
      //     //     onerror
      //     //   ),
      //     //   // 经过路线的样式
      //     //   pathLinePassedStyle: {
      //     //     strokeStyle: "#00fff6",
      //     //     lineWidth: 5,
      //     //     borderStyle: "#52C4FF",
      //     //     borderWidth: 0,
      //     //     dirArrowStyle: true,
      //     //   },
      //     // },
      //   },
      // });
    // });
  },
  created() {
  },
  watch: {
    // 监控展示哪些管线的数组
    showPipeLayer: {
      handler(newVal) {
        this.pipeHp.hide();
        this.pipeMp.hide();
        for(let i = 0; i < newVal.length; i++) {
          let temp = newVal[i];
          if(temp === 'HP') {
            this.pipeHp.show();
          }
          if(temp === 'MP') {
            this.pipeMp.show();
          }
        }
      },
      deep: true
    },
    // 轨迹
    // pathSimplifier: {
    //   handler(newVal) {
    //     this.simplifier.clearPathNavigators();
    //     if(newVal.length <= 0) {
    //       this.simplifier.setData([]);
    //       return
    //     }
    //     this.simplifier.setData(newVal);
    //     newVal.forEach((temp, index) => {
    //       this.simplifier.toggleTopOfPath(index, true);
    //       if(temp.range) {
    //         // 创建一个巡航器
    //         let pathNavigator = this.simplifier.createPathNavigator(index, {
    //           // 循环播放
    //           loop: false,
    //           // 巡航速度，单位 千米/小时。默认 1000
    //           speed: 1000,
    //           // 巡航起始、截止索引
    //           range: temp.range,
    //         });
    //         pathNavigator.start();
    //       }
    //     });
    //     // this.map.setZoom(14)
    //     // this.simplifier.setFitView(-1)
    //     this.map.setZoomAndCenter(12, newVal[0].path[0]);
    //   },
    //   deep: true
    // },
    // 秦华分公司边界
    companyBoarder: function(newVal) {
      if(newVal.length > 0) {
        let boarder = [];
        for(let i = 0; i < newVal.length; i++) {
          let polygon = new AMap.Polygon({
            path: newVal[i].path
          })
          boarder.push(polygon)
        }
        this.map.add(boarder)
      }
    },
    // 其它周边天然气公司区域
    otherCompanyBoarder: function(newVal) {
    },
    // 标记点
    markerList: {
      handler(newVal) {
        if(this.infoWindow !== null) {
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
    // width: 236px;
    height: 36px;
    color: #fff;
    display: flex;
    white-space: nowrap;
    padding-left: 28px;
    align-items: center;
    background-size: 36px;
    background-repeat: no-repeat;
    background-position: left center;

    .text {
      //height: 16px;
      //display: flex;
      //align-items: center;
      //padding: 0 6px;
      //line-height: 32px;
      //font-size: 10px;
      //font-family: medium;
      //background-color: rgba(0, 0, 0, .26);
    }
  }

  // 首页分公司
  .bg-marker6 {
    width: 220px;
    height: 60px;
    background-image: url("../../../assets/yuncheng/组3248@2x.png");
    background-size: 100% 100%;

    .text {
      transform: translate(120%, -40%);
      font-size: 20px;
    }
  }

  //// 密闭空间报警
  //.bg-marker7 {
  //  background-image: url("../../../assets/切图/运行/巡检页面/组3598.png");
  //
  //  .text {
  //    display: none;
  //  }
  //}
  //
  //// 密闭空间正常
  //.bg-marker71 {
  //  background-image: url("../../../assets/切图/运行/巡检页面/组39461.png");
  //
  //  .text {
  //    display: none;
  //  }
  //}
  //
  //// 密闭空间离线
  //.bg-marker72 {
  //  background-image: url("../../../assets/切图/运行/巡检页面/组39462.png");
  //
  //  .text {
  //    display: none;
  //  }
  //}
  //
  //// 泄露空间报警
  //.bg-marker8 {
  //  background-image: url("../../../assets/切图/运行/巡检页面/组3288.png");
  //
  //  .text {
  //    display: none;
  //  }
  //}
  //
  //// 泄露空间报警正常
  //.bg-marker81 {
  //  background-image: url("../../../assets/切图/运行/巡检页面/组3289.png");
  //
  //  .text {
  //    display: none;
  //  }
  //}
  //
  //// 泄露空间报警离线
  //.bg-marker82 {
  //  background-image: url("../../../assets/切图/运行/巡检页面/组32892.png");
  //
  //  .text {
  //    display: none;
  //  }
  //}

}
</style>
