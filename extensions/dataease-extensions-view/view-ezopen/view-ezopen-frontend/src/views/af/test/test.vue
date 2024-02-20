<template>
  <div>

<!--    <div id="video-container">-->
<!--    <el-dialog v-model="dialogVisible" :show-close="showclose" width="60%" draggable style="margin-top: 360px">-->
      <div class="video-box" v-for="(video,index) in videoList" :key="index">
        <video :id="'video'+index" ref="videoElement" controls autoplay muted></video>
      </div>
  </div>
</template>

<script>

import flvjs from 'flv.js'

export default {
  name: "test",
  data() {
    return {
      dialogVisible: false,
      videoPlayer: null,
      videoList: [
        {
          src: 'https://rtmp01open.ys7.com:9188/v3/openlive/J96844961_1_2.flv?expire=1739516876&id=680068128723537920&t=74e9aa66edaeefc2a83c0ed93cd21b594860aad04939e1c48229c2c9333251e1&ev=100'
        }
      ],
    }
  },
  mounted() {
    this.playVideo()
  },
  methods: {
    cli() {
      this.dialogVisible = true
    },
    playVideo(){
      this.vloading = true;
      this.videoList.forEach((item,index) => {
        if (flvjs.isSupported()) {
          let player = null;
          let videoElement = document.getElementById("video" + index);
          player = flvjs.createPlayer({
            type: "flv", //=> 媒体类型 flv 或 mp4
            isLive: true, //=> 是否为直播流
            hasAudio: false, //=> 是否开启声音
            url: item.src, //=> 视频流地址
          });
          player.attachMediaElement(videoElement); //=> 绑DOM
          player.load();
          player.play();

        } else {
          this.$message.error('不支持flv格式视频')
        }
        this.vloading = false;
      })
    },
  },
}
</script>

<style scoped>

</style>
