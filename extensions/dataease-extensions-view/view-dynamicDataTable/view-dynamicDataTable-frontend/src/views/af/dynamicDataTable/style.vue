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
<!--    <color-selector-->
<!--      :param="param"-->
<!--      class="attr-selector"-->
<!--      :chart="chart"-->
<!--    />-->
    <el-form ref="form" :model="attrStyle" label-width="80px">
      <el-form-item label="标题字体">
        <el-input v-model="attrStyle.titleFontSize"></el-input>
      </el-form-item>
      <el-form-item label="标题颜色">
        <el-input v-model="attrStyle.titleColor"></el-input>
      </el-form-item>
      <span>------------------------------------</span>
      <el-form-item label="标题图片大小">
        <el-input v-model="attrStyle.titleImgSize"></el-input>
      </el-form-item>
      <span>------------------------------------</span>
      <el-form-item label="文本图例大小">
        <el-input v-model="attrStyle.botFontSIze"></el-input>
      </el-form-item>
      <el-form-item label="文本图例颜色">
        <el-input v-model="attrStyle.botColor"></el-input>
      </el-form-item>
      <span>------------------------------------</span>
      <el-form-item label="文本字体">
        <el-input v-model="attrStyle.customKeyFontSize"></el-input>
      </el-form-item>
      <el-form-item label="文本加粗">
        <el-input v-model="attrStyle.customKeyFontWeight"></el-input>
      </el-form-item>
      <el-form-item label="文本颜色">
        <el-input v-model="attrStyle.customKeyColor"></el-input>
      </el-form-item>
      <span>------------------------------------</span>
      <el-form-item label="文本值字体大小">
        <el-input v-model="attrStyle.customValueFontSize"></el-input>
      </el-form-item>
      <el-form-item label="文本值加粗">
        <el-input v-model="attrStyle.customValueFontWeight"></el-input>
      </el-form-item>
      <el-form-item label="文本值颜色">
        <el-input v-model="attrStyle.customValueColor"></el-input>
      </el-form-item>
      <span>------------------------------------</span>
      <el-form-item label="单位字体大小">
        <el-input v-model="attrStyle.customUnitFontSize"></el-input>
      </el-form-item>
      <el-form-item label="单位加粗">
        <el-input v-model="attrStyle.customUnitFontWeight"></el-input>
      </el-form-item>
      <el-form-item label="单位颜色">
        <el-input v-model="attrStyle.customUnitColor"></el-input>
      </el-form-item>
      <el-form-item>
        <el-radio v-model="attrStyle.orientation" label="1">水平</el-radio>
        <el-radio v-model="attrStyle.orientation" label="2">垂直</el-radio>
      </el-form-item>

      <el-form-item>
        <el-button @click="calcStyle">确认</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {STYLE_PARAM} from '../../../utils/dynamic'
// import ColorSelector from "../../../components/selector/ColorSelector";

export default {
  components: {
    // ColorSelector
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
    console.log('dunamic_style_created: ', this.obj)
    this.attrStyle = JSON.parse(this.obj.chart.customAttr)
  },
  computed: {
    param() {
      console.log('dynamic_param_: ', JSON.stringify(this.obj))
      return this.obj.param;
    },
    view() {
      console.log('dynamic_view_: ', JSON.stringify(this.obj))
      // this.attrStyle = JSON.parse(this.obj.chart.customAttr)
      return this.obj.view;
    },
    chart() {
      console.log('dynamic_chart_: ', JSON.stringify(this.obj))
      return this.obj.chart;
    },
  },
  methods: {
    calcStyle() {
      console.log('dynamic_style_view', this.view)
      console.log('dynamic_style_attrStyle', this.attrStyle)
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
