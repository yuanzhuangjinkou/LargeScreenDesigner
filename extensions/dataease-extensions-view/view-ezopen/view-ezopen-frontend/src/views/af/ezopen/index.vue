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
          111
      </button>
    <a-modal
      :width="width"
      :visible="visible"
      :footer="null"
      @ok="handleOk"
      @cancel="handleCancel"
    >
<!--      现在设置iframe的-->
      <iframe
        :src=url
        :style="ezStyle"
        id="ysOpenDevice"
        allowfullscreen
      >
      </iframe>
    </a-modal>
  </div>
</template>

<script>

import viewTrackBar from '../../../components/views/ViewTrackBar'

export default {
  // import vie
  name: 'index',
  components: {
    viewTrackBar
  },
  data() {
    return {
      url: '',
      visible: false,
      confirmLoading: false,
      width: '100px',
    };
  },
  methods: {
    showModal() {
      this.visible = true;
    },
    handleOk(e) {
      this.confirmLoading = true;
      setTimeout(() => {
        this.visible = false;
        this.confirmLoading = false;
      }, 2000);
    },
    handleCancel(e) {
      console.log('Clicked cancel button');
      this.visible = false;
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
          this.url = this.chart.data.x[0]
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
  //height: 30px;
  //padding: 10px;
  height: 100%;
  width: 100%;
}
.ddd {
  //background-color: pink;
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
  //width: 325px;
  //height: 25px;
  //font-size: 60px;
  font-family: 'hanziti', sans-serif;
  //font-weight: 400;
  //color: #FFEC49;
  //line-height: 25px;
  //letter-spacing: 5px;
  -webkit-background-clip: text;
  //-webkit-text-fill-color: transparent;
}

.unit {
  //font-size: 40px;
}

</style>
