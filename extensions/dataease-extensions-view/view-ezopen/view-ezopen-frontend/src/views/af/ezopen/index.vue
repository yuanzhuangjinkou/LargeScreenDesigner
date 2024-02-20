<template>
  <!--  <div class="div">-->
  <div style="position:relative" class="div">
    <view-track-bar
      ref="viewTrack"
      :track-menu="trackMenu"
      class="track-bar"
      :style="trackBarStyleTime"
      @trackClick="trackClick"
    />
    <button class="hand-cursor" @click="showModal">
      11111111111
    </button>
    <a-modal
      :width="width"
      :visible="visible"
      :footer="null"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <div  v-if="visible" class="video-box" v-for="(video,index) in videoList" :key="index">
        <video :style="ezStyle" :id="'video'+index" ref="videoElement" controls autoplay muted></video>
      </div>
    </a-modal>
  </div>
</template>

<script>

import viewTrackBar from '../../../components/views/ViewTrackBar'
import flvjs from "flv.js";

export default {
  // import vie
  name: 'index',
  components: {
    viewTrackBar
  },
  data() {
    return {
      player: null,
      videoPlayer: null,
      videoList: [
        {
          // src: 'https://rtmp01open.ys7.com:9188/v3/openlive/J96844961_1_2.flv?expire=1739516876&id=680068128723537920&t=74e9aa66edaeefc2a83c0ed93cd21b594860aad04939e1c48229c2c9333251e1&ev=100'
          src: ''
        }
      ],
      visible: false,
      width: '760px',
    };
  },
  methods: {
    playVideo(){
      this.vloading = true;
      console.log("videoList", this.videoList)
      this.videoList.forEach((item,index) => {
        if (flvjs.isSupported()) {
          let videoElement = document.getElementById("video" + index);
          this.player = flvjs.createPlayer({
            type: "flv", //=> 媒体类型 flv 或 mp4
            isLive: true, //=> 是否为直播流
            hasAudio: false, //=> 是否开启声音
            url: item.src, //=> 视频流地址
          });
          this.player.attachMediaElement(videoElement); //=> 绑DOM
          this.player.load();
          this.player.play();

        } else {
          this.$message.error('不支持flv格式视频')
        }
        this.vloading = false;
      })
    },
    stopVideos() {
        if (!this.player) return
        this.player.pause()
        this.player.unload()
        this.player.detachMediaElement()
        this.player.destroy()
        this.player = null
    },
    showModal() {
      this.visible = true;
      this.$nextTick(() => {
        this.playVideo();
      });
    },
    handleOk(e) {
      setTimeout(() => {
        this.visible = false;
      }, 2000);
    },
    handleCancel(e) {
      console.log('Clicked cancel button');
      this.visible = false;
      this.stopVideos();
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
    add50px(value) {
      // 去除单位部分
      if(value == null)
        return '600px'
      const numString = value.replace('px', '');

      // 将字符串转换为数字
      const num = parseInt(numString);

      // 检查转换后的值是否为有效数字
      if (!isNaN(num)) {
        // 计算加上 50 后的结果并返回
        const result = num + 50;
        return result.toString() + 'px';
      }

      // 默认返回空字符串
      return '600px';
    }
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

    ezStyle() {
      this.width = this.add50px(JSON.parse(this.chart.customAttr).ezWidth)
      return {
        height: JSON.parse(this.chart.customAttr).ezHeight,
        width: JSON.parse(this.chart.customAttr).ezWidth
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
        console.log('ezopen_this.chart', JSON.stringify(this.obj))
        if (this.chart) {
          this.videoList[0].src = this.chart.data.x[0]
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
  height: 100%;
  width: 100%;
}

.hand-cursor {
  color: transparent;
  //z-index: -9999;
  background-color: transparent;
  border: 0;
  height: 100%;
  width: 100%;
  cursor: pointer;
}

.span {
  font-family: 'hanziti', sans-serif;
  -webkit-background-clip: text;
}

</style>
