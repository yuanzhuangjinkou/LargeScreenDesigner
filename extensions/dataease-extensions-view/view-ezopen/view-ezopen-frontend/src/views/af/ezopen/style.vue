<template>
  <div
    style="
      overflow: auto;
      border-right: 1px solid #e6e6e6;
      height: 100%;
      width: 100%;
    "
    class="attr-style theme-border-class"
  >
<!--    <el-form ref="form" :model="attrStyle" label-width="80px">-->
<!--      <el-form-item label="标题字体大小">-->
<!--        <el-input v-model="attrStyle.titleFontSize"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button @click="calcStyle">确认</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
  </div>
</template>

<script>
import {STYLE_PARAM} from '../../../utils/style'

export default {
  components: {
  },
  data() {
    return {
      attrStyle: JSON.parse(JSON.stringify(STYLE_PARAM)),
    };
  },
  props: {
    obj: {
      type: Object,
      default: () => {
      },
    },
  },
  created() {
    console.log('style_style_created: ', this.obj)
    this.attrStyle = JSON.parse(this.obj.chart.customAttr)
  },
  computed: {
    param() {      return this.obj.param;
    },
    view() {
      return this.obj.view;
    },
    chart() {
      return this.obj.chart;
    },
  },
  methods: {
    calcStyle() {
      this.view.customAttr = this.attrStyle
      this.$emit("plugin-call-back", {
        eventName: "plugins-calc-style",
        eventParam: this.view,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.padding-lr {
  padding: 0 6px;
}

span {
  font-size: 12px;
}

.el-radio {
  margin: 5px;
}

.radio-span ::v-deep .el-radio__label {
  margin-left: 2px;
}
</style>
