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
    <el-form ref="form" :model="attrStyle" label-width="80px">
      <!--一级弹窗在 map 中-->
<!--      <el-form-item label="一级弹窗宽">-->
<!--        <el-input v-model="attrStyle.oneWidth"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="一级弹窗高">-->
<!--        <el-input v-model="attrStyle.oneHeight"></el-input>-->
<!--      </el-form-item>-->
      <!--二级弹窗在 windowinfo 中-->
      <el-form-item label="windowinfo弹窗宽">
        <el-input v-model="attrStyle.twoWidth"></el-input>
      </el-form-item>
      <el-form-item label="windowinfo弹窗高">
        <el-input v-model="attrStyle.twoHeight"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="calcStyle">确认</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {STYLE_PARAM} from '../../../../utils/style'

export default {
  components: {},
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
    param() {
      return this.obj.param;
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
      console.log('afamap_attrStyle', JSON.stringify(this.attrStyle))
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
