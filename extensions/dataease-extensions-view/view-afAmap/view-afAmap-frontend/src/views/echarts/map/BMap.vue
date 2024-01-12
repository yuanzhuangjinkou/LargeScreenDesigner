<template>
  <div id="mymap"></div>
</template>

<script>
// import AMap from "AMap";
// import AMapUI from "AMapUI";
import de from "element-ui/src/locale/lang/de";

export default {
  title: "高德地图",
  props: {
    // 中心点坐标
    center: {
      type: Array,
      default: [108.68, 34.41],
    },
    mapZooms: {
      type: Array,
      default: [11, 16],
    },
    mapZoom: {
      type: Number,
      default: 11.21,
    },
    // 默认缩放比例
    zoom: {
      type: Number,
      default: 14,
    },
    // 点标记图标尺寸
    iconSize: {
      type: Array,
      default: [36, 36],
    },
    // 点标记图标尺寸(放大)
    iconSizemax: {
      type: Array,
      default: [36, 36],
    },
    // 点标记
    marker: {
      type: Array,
      default: [],
    },
    // 灵活点标记
    elasticMarker: {
      type: Array,
      default: function () {
        return []
      },
    },
    // 灵活点标记地图级别与styles中样式对应关系的映射
    zoomStyleMapping: {
      type: Object,
      default: null
    },
    isMarker: {
      type: Boolean,
      default: false,
    },
    // elasticMarker1: {
    //   type: Array,
    //   default: []
    // },
    // 多边形覆盖物
    polygon: {
      type: Object,
      default: function () {
        return {}
      },
    },
    // 多类型覆盖物（多边形、折线、文字）
    overlayGroup: {
      type: Array,
      default: function () {
        return []
      },
    },
    // 轨迹
    pathSimplifier: {
      type: Array,
      default: function () {
        return []
      },
    },
    // 所选公司名
    companyName: {
      type: String,
      required: true,
    },
    // 所选二级菜单分组名
    area: {
      type: String,
      default: "none",
    },
    // 线条
    polylines: {
      type: Array,
      default: function () {
        return []
      },
    },
    showPipe: {
      type: Array,
      default: function () {
        return []
      },
    },
    zooms: {
      type: Array,
      default: [12, 20],
    },
    viewMode: {
      type: String,
      default: "3D",
    },
    disableMouse: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      // 用于临时存储高亮区域，如果选择其他区域，便于将临时存储的区域取消高亮
      polygonHighLight: [],
      // 用于记录鼠标悬停区域高亮，便于在触发公司名传入时，将鼠标阻挡区域清空
      polygonMouseHoverHighLight: [],
      // 存储所有区域的副本，用于遍历，与传入的公司名做对比，快速获取需要高亮的区域对象
      polygonArr: [],
      showLabel: false,
      map: null,
      simplifier: null,
      zoomControl: 12,
      allPolygon: [],
      flag: true,
      allPolyline: [],
      // 高压管线瓦片
      pipeHP: null,
      // 高压管线虚线瓦片
      pipeHPCon: null,
      // 次高压管线瓦片
      pipeIP: null,
      // 中压管线瓦片
      pipeMP: null,
      // 低压管线瓦片
      pipeLP: null,
      // 架空管线瓦片
      pipeJK: null,
      mapMoveTimer: null,
    };
  },
  methods: {
    reClick() {
      this.$emit("re-click");
    },
    // 初始化地图
    initMap() {
      this.map = new AMap.Map("mymap", {

        showBuildingBlock: false,
        showLabel: true,

        center: this.center,
        expandZoomRange: true,
        zoom: this.mapZoom,
        zooms: this.mapZooms,
        pitch: 0,
        rotation: 0,
        mapStyle: 'amap://styles/dfb4b8d0a013071e535e04f4eaf96ce4',
        viewMode: this.viewMode, //开启3D视图,默认为关闭
      });
      window.map = this.map;
      let _this = this;
      _this.zoomControl = this.map.getZoom();
      this.map.on("click", function (e) {
        _this.$emit("map-click", e);
      });
      //加载轨迹组件，loadUI的路径参数为模块名中 'ui/' 之后的部分
      AMapUI.loadUI(["misc/PathSimplifier"], function (PathSimplifier) {
        if (!PathSimplifier.supportCanvas) {
          alert("当前环境不支持轨迹展示！");
          return;
        }
        //创建组件实例
        _this.simplifier = new PathSimplifier({
          zIndex: 103,
          map: _this.map, //所属的地图实例
          autoSetFitView: false,
          getPath: function (pathData, pathIndex) {
            //返回轨迹数据中的节点坐标信息，[AMap.LngLat, AMap.LngLat...] 或者 [[lng|number,lat|number],...]
            return pathData.path;
          },
          getHoverTitle: function (pathData, pathIndex, pointIndex) {
            //返回鼠标悬停时显示的信息
            if (pointIndex >= 0) {
              //鼠标悬停在某个轨迹节点上
              return (
                pathData.name +
                "，点:" +
                (pointIndex + 1) +
                "/" +
                pathData.path.length
              );
            }
            //鼠标悬停在节点之间的连线上
            return pathData.name + "，点数量" + pathData.path.length;
          },
          renderOptions: {
            // 轨迹线样式
            pathLineStyle: {
              strokeStyle: "#52C4FF",
              lineWidth: 5,
              borderStyle: "#52C4FF",
              borderWidth: 0,
              dirArrowStyle: true,
            },
            // 轨迹线悬停样式
            pathLineHoverStyle: {
              strokeStyle: "#52C4FF",
              lineWidth: 8,
              borderStyle: "#52C4FF",
              borderWidth: 0,
              dirArrowStyle: true,
            },
            // 轨迹线选中样式
            pathLineSelectedStyle: {
              strokeStyle: "#52C4FF",
              lineWidth: 5,
              borderStyle: "#52C4FF",
              borderWidth: 0,
              dirArrowStyle: true,
            },
            // 起点样式
            startPointStyle: {
              radius: 0,
            },
            // 终点样式
            endPointStyle: {
              radius: 0,
            },
            // 显示文字样式
            hoverTitleStyle: {
              classNames: "path-simplifier-title",
            },
            // 巡航器样式
            pathNavigatorStyle: {
              width: 120,
              height: 200,
              autoRotate: false,
              content: PathSimplifier.Render.Canvas.getImageContent(
                require("@/assets/巡检.png"),
                onload,
                onerror
              ),
              // 经过路线的样式
              pathLinePassedStyle: {
                strokeStyle: "#00fff6",
                lineWidth: 5,
                borderStyle: "#52C4FF",
                borderWidth: 0,
                dirArrowStyle: true,
              },
            },
          },
        });
      });

      // 初始化瓦片图
      this.pipeHP = new AMap.TileLayer.WMS({
        url: "/gis/geoserver/xianyang/wms",
        blend: false,
        tileSize: 512,
        zIndex: 999999,
        visible: true,
        zooms: [0, 20],
        params: {
          LAYERS: "xianyang:hp",
          VERSION: "1.1.0",
          FORMAT: "image/png",
          SRS: "EPSG:3857",
        },
      });

      this.pipeHPCon = new AMap.TileLayer.WMS({
        url: "/gis/geoserver/xianyang/wms",
        blend: false,
        tileSize: 512,
        zIndex: 999999,
        visible: true,
        zooms: [0, 20],
        params: {
          LAYERS: "xianyang:HPCon",
          VERSION: "1.1.0",
          FORMAT: "image/png",
          SRS: "EPSG:3857",
        },
      });

      this.pipeIP = new AMap.TileLayer.WMS({
        url: "/gis/geoserver/xianyang/wms",
        blend: false,
        tileSize: 512,
        zIndex: 999999,
        visible: true,
        zooms: [0, 20],
        params: {
          LAYERS: "xianyang:ip",
          VERSION: "1.1.0",
          FORMAT: "image/png",
          SRS: "EPSG:3857",
        },
      });

      this.pipeMP = new AMap.TileLayer.WMS({
        url: "/gis/geoserver/xianyang/wms",
        blend: false,
        tileSize: 512,
        zIndex: 999999,
        visible: true,
        zooms: [0, 20],
        params: {
          LAYERS: "xianyang:mp",
          VERSION: "1.1.0",
          FORMAT: "image/png",
          SRS: "EPSG:3857",
        },
      });

      this.pipeLP = new AMap.TileLayer.WMS({
        url: "/gis/geoserver/xianyang/wms",
        blend: false,
        tileSize: 512,
        zIndex: 999999,
        visible: true,
        zooms: [0, 20],
        params: {
          LAYERS: "xianyang:lp",
          VERSION: "1.1.0",
          FORMAT: "image/png",
          SRS: "EPSG:3857",
        },
      });

      this.pipeJK = new AMap.TileLayer.WMS({
        url: "/gis/geoserver/xianyang/wms",
        blend: false,
        tileSize: 512,
        zIndex: 999999,
        visible: true,
        zooms: [0, 20],
        params: {
          LAYERS: "xianyang:jk",
          VERSION: "1.1.0",
          FORMAT: "image/png",
          SRS: "EPSG:3857",
        },
      });

      this.pipeHP.setMap(this.map);
      this.pipeHPCon.setMap(this.map);
      this.pipeIP.setMap(this.map);
      this.pipeMP.setMap(this.map);
      this.pipeLP.setMap(this.map);
      this.pipeJK.setMap(this.map);

      this.pipeHP.hide();
      this.pipeHPCon.hide();
      this.pipeIP.hide();
      this.pipeMP.hide();
      this.pipeLP.hide();
      this.pipeJK.hide();

      // setInterval(function () {
      //   console.warn('当前中心',this.map.getCenter());
      // },3000)
    },
    clickFunction(e) {
      this.$emit("polygon-click", e);
      if (this.polygonHighLight.length > 0) {
        for (let i = 0; i < this.polygonHighLight.length; i++) {
          let item = this.polygonHighLight[i];
          item.setOptions({
            fillColor: "#057de2",
            // 轮廓线宽度
            strokeWeight: 3,
            // 轮廓线样式,实线:solid;虚线:dashed
            strokeStyle: "solid",
            // 轮廓线颜色
            strokeColor: "#00c6ff",
            fillOpacity: 0.15,
          });
        }
      }
      this.polygonHighLight = [];
      this.polygonHighLight.push(e.target);
      // _this.$emit('polygon-click', polygon.De.name)
      this.polygonHighLight[0].setOptions({
        // fillColor: '#7183ff',
        fillColor: "#52cbef",
        // 轮廓线颜色
        strokeColor: "#fff924",
        // 轮廓线宽度
        strokeWeight: 3,
        // 轮廓线样式,实线:solid;虚线:dashed
        strokeStyle: "solid",
        fillOpacity: 0.7,
      });
    },
    mouseoverFunction(e) {
      // this.map.setStatus({
      //   dragEnable: true,
      //   doubleClickZoom: true,
      //   zoomEnable: true,
      // });
      if (this.polygonMouseHoverHighLight.length > 0) {
        for (let i = 0; i < this.polygonMouseHoverHighLight.length; i++) {
          let item = this.polygonMouseHoverHighLight[i];
          item.setOptions({
            fillColor: "#057de2",
            // 轮廓线宽度
            strokeWeight: 3,
            // 轮廓线样式,实线:solid;虚线:dashed
            strokeStyle: "solid",
            // 轮廓线颜色
            strokeColor: "#00c6ff",
            fillOpacity: 0.15,
          });
        }
      }
      this.polygonMouseHoverHighLight = [];
      this.polygonMouseHoverHighLight.push(e.target);
      this.polygonMouseHoverHighLight[0].setOptions({
        // fillColor: '#7183ff',
        fillColor: "#52cbef",
        // 轮廓线颜色
        strokeColor: "#fff924",
        // 轮廓线宽度
        strokeWeight: 3,
        // 轮廓线样式,实线:solid;虚线:dashed
        strokeStyle: "solid",
        fillOpacity: 0.7,
      });
    },
    mouseoutFunction(e) {
      // this.map.setStatus({
      //   dragEnable: false,
      //   doubleClickZoom: false,
      //   zoomEnable: false,
      // });
      if (this.polygonMouseHoverHighLight.length === 0) {
        e.target.setOptions({
          // 填充颜色
          fillColor: "#057de2",
          // 轮廓线宽度
          strokeWeight: 3,
          // 轮廓线样式,实线:solid;虚线:dashed
          strokeStyle: "solid",
          // 轮廓线颜色
          strokeColor: "#00c6ff",
          fillOpacity: 0.15,
        });
      } else {
        for (let i = 0; i < this.polygonMouseHoverHighLight.length; i++) {
          let item = this.polygonMouseHoverHighLight[i];
          item.setOptions({
            fillColor: "#057de2",
            // 轮廓线宽度
            strokeWeight: 3,
            // 轮廓线样式,实线:solid;虚线:dashed
            strokeStyle: "solid",
            // 轮廓线颜色
            strokeColor: "#00c6ff",
            fillOpacity: 0.15,
          });
        }
      }
      if (this.polygonHighLight.length > 0) {
        for (let i = 0; i < this.polygonHighLight.length; i++) {
          let polygon = this.polygonHighLight[i];
          polygon.setOptions({
            fillColor: "#52cbef",
            strokeColor: "#fff924",
            strokeWeight: 3,
            strokeStyle: "solid",
            fillOpacity: 0.7,
          });
        }
      }
    },
  },
  mounted() {
    this.initMap();
    // this.map.setMapStyle("amap://styles/3356249ed35bc7ebc6c673c57461fc86");
    setTimeout(() => {
      this.flag = false;
    }, 1000);
    // setInterval(() => {
    //   console.warn('地图坐标',this.map.getZoom(),this.map.getCenter())
    // })
  },
  computed: {
    getZoom: {
      get() {
        if (this.flag) {
          return -1;
        }
        if (this.map) {
          return this.map.getZoom();
        } else {
          return 0;
        }
      },
    },
  },
  watch: {
    marker: function (nval, oval) {
      oval.forEach((temp) => {
        this.map.remove(temp.ui);
      });
      nval.forEach((temp) => {
        let mystyle = "";
        if (!temp.title) mystyle += "display: none;";
        if (temp.titleicon)
          mystyle += `background-image:url(${temp.titleicon});`;
        let _this = this;
        if (temp.type === "star") {
          mystyle += `font-size: 28px;`;
          mystyle += `pointer-events: auto;`;
          mystyle += `height: 6.94vh;`;
          let marker = new AMap.Marker({
            zIndex: 104,
            map: this.map,
            position: temp.position,
            anchor: "center",
            icon: new AMap.Icon({
              // 图标尺寸
              size: {
                width: 54,
                height: 49,
              },
              image: temp.icon,
              // 根据所设置的大小拉伸或压缩图片
              imageSize: {
                width: 54,
                height: 49,
              },
            }),
            label: {
              content: `<div class="markerLable" style="${mystyle}">${temp.title}</div>`,
              direction: "right",
              offset: temp.labelOffset || { x: 0, y: -28 },
            },
            extData: temp.data,
          });

          marker.on("click", function (e) {
            _this.$emit("marker-click", e);
          });
          temp.ui = marker;
        } else {
          mystyle += `font-family: 'PINGFANG-BOLD';`;
          mystyle += `font-size: 22px;`;
          mystyle += `pointer-events: auto;`;
          mystyle += `height: 1.45vw;`;
          mystyle += `width: 220px;`;
          let marker = new AMap.Marker({
            zIndex: 104,
            map: this.map,
            position: temp.position,
            anchor: "center",
            icon: new AMap.Icon({
              // 图标尺寸
              size: {
                width: temp.width || 83,
                height: temp.height || 64,
              },
              image: temp.icon,
              // 根据所设置的大小拉伸或压缩图片
              imageSize: {
                width: temp.width || 83,
                height: temp.height || 64,
              },
            }),
            label: {
              content: `<div class="markerLable" style="${mystyle}">${temp.title}</div>`,
              direction: "center",
              offset: temp.labelOffset || { x: 3, y: -62 },
            },
            extData: temp.data,
          });

          marker.on("click", function (e) {
            _this.$emit("marker-click", e);
          });
          temp.ui = marker;
        }
      });
    },
    elasticMarker: function (nval, oval) {
      const defStyleMapping = [
        {
          11: 0, 12: 0, 13: 0, 14: 0, 15: 0, 16: 0, 17: 0, 18: 0, 20: 0
        },
        {
          11: 1, 12: 1, 13: 1, 14: 1, 15: 1, 16: 0, 17: 0, 18: 0, 20: 0
        }
      ]
      oval.forEach((temp) => {
        this.map.remove(temp.ui);
      });
      nval.forEach((temp) => {
        // let markerLable =''
        // if(temp.title&&temp.title.length>6){
        //   markerLable='markerLable-over'
        // }else if(temp.title&&temp.title.length<=6){
        //   markerLable='markerLable'
        // }
        let mystyle = "";
        if (!temp.title) mystyle += "display: none;";
        if (temp.titleicon)
          mystyle += `background-image:url(${temp.titleicon});`;
        mystyle += `pointer-events: auto;`;
        let marker = new AMap.ElasticMarker({
          zooms: this.zooms,
          zIndex: 104,
          map: this.map,
          position: temp.position,
          // 该数值是一个数组，用于指定多种样式，动态获取
          styles: [
            {
              icon: {
                size: this.iconSizemax,
                img: temp.icon,
                ancher: [0, 0],
                imageSize: this.iconSizemax,
                // 图标最佳比例
                fitZoom: this.zoom,
                // 图标缩放比例
                scaleFactor: 1.3,
                maxScale: 1,
              },
              label: {
                content: `<div class="markerLables"><div class="markerLable" style="${mystyle}"><span>${temp.title}</span></div></div>`,
                // content: temp.title,
                position: "TM",
                offset: [-260, -50],
              },
            },
            {
              icon: {
                size: this.iconSize,
                img: temp.icon,
                ancher: [0, 0],
                imageSize: this.iconSize,
                // 图标最佳比例
                fitZoom: this.zoom,
                // 图标缩放比例
                scaleFactor: 1.3,
                maxScale: 1,
              },
            },
          ],
          // 指定不同缩放程度下，启用哪种样式，样式在style中定义
          zoomStyleMapping: this.zoomStyleMapping ? this.zoomStyleMapping : (this.isMarker ? defStyleMapping[0] : defStyleMapping[1]),
          extData: temp.data,
        });
        let _this = this;
        marker.on("click", function (e) {
          _this.$emit("marker-click", e);
        });
        temp.ui = marker;
      });
    },
    polygon: function (nval, oval) {
      if (oval.ui) this.map.remove(oval.ui);
      let polygon = new AMap.Polygon({
        zIndex: 102,
        map: this.map,
        path: nval.path,
        // 填充颜色
        fillColor: "#057de2",
        // 填充透明度
        fillOpacity: 0.48,
        // 轮廓线颜色
        strokeColor: "#ffd200",
        // 轮廓线透明度
        strokeOpacity: 1,
        // 轮廓线宽度
        strokeWeight: 4,
        // 轮廓线样式,实线:solid;虚线:dashed
        strokeStyle: "solid",
        extData: nval.data,
      });
      let _this = this;
      polygon.on("click", function (e) {
        _this.$emit("polygon-click", e);
      });
      nval.ui = polygon;
    },
    overlayGroup: function (nval, oval) {
      let _this_ = this;
      oval.forEach((temp) => {
        this.map.remove(temp.ui);
      });
      nval.forEach((temp) => {
        let _this = this;
        switch (temp.type) {
          case "Polygon":
            let polygon = new AMap.Polygon({
              name: temp.name,
              map: this.map,
              path: temp.path,
              // 填充颜色
              fillColor: "#057de2",
              // 填充透明度
              fillOpacity: 0.15,
              // 轮廓线颜色
              strokeColor: "#00c6ff",
              // 轮廓线透明度
              strokeOpacity: 1,
              // 轮廓线宽度
              strokeWeight: 3,
              // 轮廓线样式,实线:solid;虚线:dashed
              strokeStyle: "solid",
              extData: temp.data,
            });
            _this.allPolygon.push(polygon);
            // polygon.on('click', function (e) {
            //   _this.$emit('polygon-click', e)
            //   if (_this.polygonHighLight.length > 0) {
            //     for (let i = 0; i < _this.polygonHighLight.length; i++) {
            //       let item = _this.polygonHighLight[i];
            //       item.setOptions({
            //         fillColor: '#057de2',
            //         // 轮廓线宽度
            //         strokeWeight: 3,
            //         // 轮廓线样式,实线:solid;虚线:dashed
            //         strokeStyle: 'dashed',
            //         // 轮廓线颜色
            //         strokeColor: '#00c6ff',
            //         fillOpacity: 0.15
            //       })
            //     }
            //   }
            //   _this.polygonHighLight = [];
            //   _this.polygonHighLight.push(polygon)
            //   // _this.$emit('polygon-click', polygon.De.name)
            //   _this.polygonHighLight[0].setOptions({
            //     // fillColor: '#7183ff',
            //     fillColor: '#52cbef',
            //     // 轮廓线颜色
            //     strokeColor: '#fff924',
            //     // 轮廓线宽度
            //     strokeWeight: 3,
            //     // 轮廓线样式,实线:solid;虚线:dashed
            //     strokeStyle: 'solid',
            //     fillOpacity: 0.7
            //   })
            // })
            // //鼠标高亮效果
            // polygon.on('mouseover', function (e) {
            //   if (_this.polygonHighLight.length === 0) {
            //     _this.polygonMouseHoverHighLight = polygon
            //     _this.polygonMouseHoverHighLight.setOptions({
            //       // fillColor: '#7193ff',
            //       fillColor: '#52cbef',
            //       // 轮廓线颜色
            //       strokeColor: '#fff924',
            //       // 轮廓线宽度
            //       strokeWeight: 3,
            //       // 轮廓线样式,实线:solid;虚线:dashed
            //       strokeStyle: 'solid',
            //       fillOpacity: 0.7, //填充透明度
            //     })
            //   }
            // });
            // polygon.on('mouseout', function (e) {
            //   if (_this.polygonHighLight.length === 0) {
            //     polygon.setOptions({
            //       // 填充颜色
            //       fillColor: '#057de2',
            //       // 轮廓线宽度
            //       strokeWeight: 3,
            //       // 轮廓线样式,实线:solid;虚线:dashed
            //       strokeStyle: 'dashed',
            //       // 轮廓线颜色
            //       strokeColor: '#00c6ff',
            //       fillOpacity: 0.15,
            //     })
            //   }
            // });
            temp.ui = polygon;
            let polygonName = polygon.De.name;
            switch (polygonName) {
              case "城东1":
                polygonName = "市区-城东一组";
                break;
              case "城东2":
                polygonName = "市区-城东二组";
                break;
              case "城南":
                polygonName = "市区-城南";
                break;
              case "城西1":
                polygonName = "市区-城西一组";
                break;
              case "城西2":
                polygonName = "市区-城西二组";
                break;
              case "城中":
                polygonName = "市区-城中";
                break;
            }
            _this.polygonArr.push({
              polygon: polygon,
              name: polygonName,
            });
            break;
          case "Polyline":
            let polyline = new AMap.Polyline({
              zIndex: 103,
              map: this.map,
              path: temp.path,
              // 折线拐点样式,尖角:miter;圆角:round;斜角:bevel
              lineJoin: "round",
              // 折线两端样式,无头:butt;圆角:round;方头:square
              lineCap: "round",
              // 折线颜色
              strokeColor: "#00ff90",
              // 折线透明度
              strokeOpacity: 1,
              // 折线宽度
              strokeWeight: 12,
              // 折线样式,实线:solid;虚线:dashed
              strokeStyle: "solid",
              extData: temp.data,
            });
            polyline.on("click", function (e) {
              _this.$emit("polyline-click", e);
            });
            temp.ui = polyline;
            break;
          case "Text1":
            let polytext1 = new AMap.Text({
              zIndex: 100,
              map: this.map,
              position: temp.position,
              text: temp.text,
              style: {
                "font-size": "20px",
                color: "#FFFFFF",
                opacity: "1",
              },
              zooms: [11, 16],
            });
            polytext1.on("click", function (e) {
              _this.$emit("text-click", e);
            });
            polytext1.on("mouseover", function (e) {
              _this.$emit("text-click", e);
            });
            temp.ui = polytext1;
            break;
          //市区分公司区域名称地图放大后显示
          case "Text2":
            let polytext2 = new AMap.Text({
              zIndex: 100,
              map: this.map,
              position: temp.position,
              text: temp.text,
              style: {
                "font-size": "20px",
                color: "#FFFFFF",
                opacity: "1",
              },
              zooms: [12, 16],
            });
            polytext2.on("click", function (e) {
              _this.$emit("text-click", e);
            });
            polytext2.on("mouseover", function (e) {
              _this.$emit("text-click", e);
            });
            temp.ui = polytext2;
            break;
          case "Text3":
            let polytext3 = new AMap.Text({
              zIndex: 100,
              map: this.map,
              position: temp.position,
              text: temp.text,
              style: {
                "font-size": "20px",
                color: "#FFFFFF",
                opacity: "1",
              },
              zooms: [11, 12],
            });
            polytext3.on("click", function (e) {
              _this.$emit("text-click", e);
            });
            polytext3.on("mouseover", function (e) {
              _this.$emit("text-click", e);
            });
            temp.ui = polytext3;
            break;
        }
      });
    },
    pathSimplifier: function (nval) {
      this.simplifier.clearPathNavigators();
      this.simplifier.setData(nval);
      nval.forEach((temp, index) => {
        this.simplifier.toggleTopOfPath(index, true);
        if (temp.range) {
          // 创建一个巡航器
          let pathNavigator = this.simplifier.createPathNavigator(index, {
            // 循环播放
            loop: false,
            // 巡航速度，单位 千米/小时。默认 1000
            speed: 1000,
            // 巡航起始、截止索引
            range: temp.range,
          });
          pathNavigator.start();
        }
      });
      // this.map.setZoom(14)
      this.simplifier.setFitView(-1)
      // this.map.setZoomAndCenter(16, nval[0].path[0]);
    },
    companyName: function (newVal, oldVal) {
      if (this.polygonHighLight.CLASS_NAME) {
        this.polygonHighLight.setOptions({
          fillColor: "#057de2",
          strokeWeight: 3,
          strokeStyle: "solid",
          strokeColor: "#00c6ff",
          fillOpacity: 0.15,
        });
        this.polygonHighLight = {};
      }
      // 监视CompanyName的变化，如果变化证明外界选框点击
      // 如果新的数据是总公司
      if (newVal === "all") {
        // 判断当前有无高亮区域，如果polygonHighLight有CLASS_NAME，证明polygonHighLight不为空
        if (this.polygonHighLight.length > 0) {
          for (let i = 0; i < this.polygonHighLight.length; i++) {
            let item = this.polygonHighLight[i];
            // 将当前高亮区域取消高亮
            item.setOptions({
              fillColor: "#057de2",
              strokeWeight: 3,
              strokeStyle: "solid",
              strokeColor: "#00c6ff",
              fillOpacity: 0.15,
            });
          }
          // 将高亮区域设为空
          this.polygonHighLight = [];
        }
      } else {
        // 如果选择的不是总公司
        // 清除已经高亮的区域
        if (this.polygonHighLight.length > 0) {
          for (let i = 0; i < this.polygonHighLight.length; i++) {
            let item = this.polygonHighLight[i];
            item.setOptions({
              fillColor: "#057de2",
              strokeWeight: 3,
              strokeStyle: "solid",
              strokeColor: "#00c6ff",
              fillOpacity: 0.15,
            });
          }
          this.polygonHighLight = [];
        }
        let targetName = this.companyName.substring(
          0,
          this.companyName.length - 3
        );
        // 在区域的数组中进行遍历
        for (let i = 0; i < this.polygonArr.length; i++) {
          let item = this.polygonArr[i];
          if (item.name === targetName) {
            this.polygonHighLight.push(item.polygon);
          }
        }
        for (let i = 0; i < this.polygonHighLight.length; i++) {
          let item = this.polygonHighLight[i];
          item.setOptions({
            fillColor: "#52cbef",
            strokeColor: "#fff924",
            strokeWeight: 3,
            strokeStyle: "solid",
            fillOpacity: 0.7,
          });
        }
      }
    },
    area: function (newVal) {
      if (newVal === "hold") {
        return;
      }
      // 清除已经高亮的区域
      if (this.polygonHighLight.length > 0) {
        for (let i = 0; i < this.polygonHighLight.length; i++) {
          let item = this.polygonHighLight[i];
          item.setOptions({
            fillColor: "#057de2",
            strokeWeight: 3,
            strokeStyle: "solid",
            strokeColor: "#00c6ff",
            fillOpacity: 0.15,
          });
        }
        this.polygonHighLight = [];
      }
      // 在区域的数组中进行遍历
      if (newVal === "所有") {
        for (let i = 0; i < this.polygonArr.length; i++) {
          let item = this.polygonArr[i];
          if (item.name.indexOf("市区-") === 0) {
            this.polygonHighLight.push(item.polygon);
          }
        }
      } else {
        for (let i = 0; i < this.polygonArr.length; i++) {
          let item = this.polygonArr[i];
          if (item.name === newVal) {
            this.polygonHighLight.push(item.polygon);
          }
        }
      }
      // 遍历进行高亮
      for (let i = 0; i < this.polygonHighLight.length; i++) {
        let item = this.polygonHighLight[i];
        item.setOptions({
          fillColor: "#52cbef",
          strokeColor: "#fff924",
          strokeWeight: 3,
          strokeStyle: "solid",
          fillOpacity: 0.7,
        });
      }
    },
    getZoom: {
      immediate: true,
      handler: function (newVal) {
        let _this = this;
        if (_this.allPolygon.length > 0) {
          if (newVal < 16) {
            for (let i = 0; i < _this.allPolygon.length; i++) {
              let polygon = _this.allPolygon[i];
              polygon.off("click", _this.reClick);
              polygon.on("click", _this.clickFunction);
              //鼠标高亮效果
              polygon.on("mouseover", _this.mouseoverFunction);
              polygon.on("mouseout", _this.mouseoutFunction);
              if (_this.polygonHighLight.length > 0) {
                for (let i = 0; i < _this.polygonHighLight.length; i++) {
                  let polygon = _this.polygonHighLight[i];
                  polygon.setOptions({
                    fillColor: "#52cbef",
                    strokeColor: "#fff924",
                    strokeWeight: 3,
                    strokeStyle: "solid",
                    fillOpacity: 0.7,
                  });
                }
              }
              // polygon.show()
            }
          } else {
            for (let i = 0; i < _this.allPolygon.length; i++) {
              let polygon = _this.allPolygon[i];
              polygon.off("click", _this.clickFunction);
              polygon.off("mouseover", _this.mouseoverFunction);
              polygon.off("mouseout", _this.mouseoutFunction);
              polygon.on("click", _this.reClick);
              // polygon.hide()
            }
            if (_this.polygonHighLight.length > 0) {
              for (let i = 0; i < _this.polygonHighLight.length; i++) {
                let polygon = _this.polygonHighLight[i];
                polygon.setOptions({
                  fillColor: "#057de2",
                  // 轮廓线宽度
                  strokeWeight: 3,
                  // 轮廓线样式,实线:solid;虚线:dashed
                  strokeStyle: "solid",
                  // 轮廓线颜色
                  strokeColor: "#00c6ff",
                  fillOpacity: 0.15,
                });
              }
            }
            if (_this.polygonMouseHoverHighLight.length > 0) {
              for (
                let i = 0;
                i < _this.polygonMouseHoverHighLight.length;
                i++
              ) {
                let polygon = _this.polygonMouseHoverHighLight[i];
                polygon.setOptions({
                  fillColor: "#057de2",
                  // 轮廓线宽度
                  strokeWeight: 3,
                  // 轮廓线样式,实线:solid;虚线:dashed
                  strokeStyle: "solid",
                  // 轮廓线颜色
                  strokeColor: "#00c6ff",
                  fillOpacity: 0.15,
                });
              }
            }
          }
        }
      },
    },
    polylines: function (newVal) {
      this.map.remove(this.allPolyline);
      for (let i = 0; i < newVal.length; i++) {
        let tempPath = [];
        let pathArr = newVal[i];
        for (let j = 0; j < pathArr.length; j++) {
          let item = pathArr[j];
          let obj = new AMap.LngLat(item[0], item[1]);
          tempPath.push(obj);
        }

        let polyline = new AMap.Polyline({
          path: tempPath,
          borderWeight: 2, // 线条宽度，默认为 1
          strokeColor: "red", // 线条颜色
          lineJoin: "round", // 折线拐点连接处样式
        });
        this.allPolyline.push(polyline);
        this.map.add(polyline);
      }
    },
    showPipe: function (newVal) {
      this.pipeHP.hide();
      this.pipeHPCon.hide();
      this.pipeIP.hide();
      this.pipeMP.hide();
      this.pipeLP.hide();
      this.pipeJK.hide();

      for (let i = 0; i < newVal.length; i++) {
        let temp = newVal[i];
        if (temp === "HP") {
          this.pipeHP.show();
          this.pipeHPCon.show();
        }
        if (temp === "IP") {
          this.pipeIP.show();
        }
        if (temp === "MP") {
          this.pipeMP.show();
        }
        if (temp === "LP") {
          this.pipeLP.show();
        }
        if (temp === "JK") {
          this.pipeJK.show();
        }
      }
    },
    center: function () {
      this.map.panTo(this.center);
    },
    mapZoom: function()  {
      this.map.setZoom(this.mapZoom);
    },
    disableMouse: function (newVal) {
      this.map.setStatus({
        dragEnable: true,
        doubleClickZoom: true,
        zoomEnable: true,
      });
      if (newVal) {
        this.map.on("mapmove",() => {
          clearTimeout(this.mapMoveTimer)
          let center = this.map.getCenter();
          let lng = center.lng;
          let lat = center.lat;
          if ( lng <= 108.52 || lng >= 108.97 || lat >= 34.56 || lat <= 34.28 ){
            this.mapMoveTimer = setTimeout(() => {
              this.map.panTo([108.68, 34.41])
            },500)
          }
        })
      }
    },
  },
};
</script>

<style lang="scss">
#mymap {
  width: 100%;
  height: 100%;
}
.amap-logo {
  visibility: hidden;
}
.amap-marker-label {
  border: none;
  background: none;
  pointer-events: none;
}
.markerLables {
  width: 20vw;
  height: 2.5vw;
  display: flex;
  justify-content: center;
  align-items: center;
}
.markerLable {
  background-size: 100% 100%;
  position: absolute;
  //left: vw(20);
  //top: vw(20);
  //width: vw(160);
  height: 1vw;
  /*text-align: center;*/
  line-height: 1vw;
  color: #ffffff;
  font-size: vw(20);
  font-family: PingFang SC;
  font-weight: bold;
  padding: 0 vw(20);
  display: flex;
  justify-content: center;
  align-items: center;
}
//.markerLable-over{
//  background-size: 100% 100%;
//  position: absolute;
//  left: vw(30);
//  top: vw(20);
//  height: vw(40);
//  text-align: center;
//  line-height: vw(40);
//  color: #FFFFFF;
//  font-size: vw(22);
//  font-family: PingFang SC;
//  font-weight: bold;
//}
.amap-overlay-text-container {
  background: none;
  border: none;
  color: #f7db35 !important;
  font-size: vw(26);
  font-family: PingFang SC;
  font-weight: 400;
  opacity: 0.63;
}
.path-simplifier-title {
  color: red !important;
}
</style>
