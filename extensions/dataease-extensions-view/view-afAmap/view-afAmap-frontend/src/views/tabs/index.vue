<template>
  <div class="select_main">
    <!-- 下拉框 -->
    <el-select v-model="selectedTab" placeholder="选择标签页" @change="handleTabChange">
      <el-option
        v-for="tab in tabs"
        :key="tab.name"
        :label="tab.title"
        :value="tab.name"
      ></el-option>
    </el-select>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tabs: [
        {title: 'Tab 1', name: '1', content: 'Tab 1 content'},
        {title: 'Tab 2', name: '2', content: 'Tab 2 content'},
        {title: 'Tab 3', name: '3', content: 'Tab 3 content'},
        {title: 'Tab 4', name: '4', content: 'Tab 4 content'},
        // Add more tabs if needed
      ],
      selectedTab: '1',
      activeTab: '1'
    };
  },
  methods: {
    handleTabChange(value) {
      this.activeTab = value;
    }
  }
};
</script>

<style scoped>
::v-deep .el-select-dropdown{
  background: pink;
  border: 0;
}
/*!*选择框内的背景*!*/
::v-deep .el-input__inner {
  /*height: 400px;*/
  width: 200px;
  background-color: rgba(21, 60, 116, 0.7);
  background-image: url("../../assets/select.png");
  background-size: 100% 100%;
  padding-left: 25px;
  font-size: 22px;
  font-weight: bold;
  color: #FFF;
  text-align: center;
  border: 2px solid #18AEC8;
  border-radius: 3px; /* 边框 */
}

/* 未选中状态下拉框的字体样式 */
/*.el-select-dropdown .el-select-dropdown__item {*/
.el-select-dropdown__item {
  margin-bottom: 1px;
  border: 1px solid #18AEC8;
  border-radius: 1px;
  width: 200px;
  font-size: 22px;
  color: #FFF;
  background-color: rgba(40, 80, 130, 0.9);
  background-size: 100% 100%;
  display: flex;
  justify-content: center;
  /*align-items: center;*/
}

/* 选中状态下拉框中的选项的字体样式 */
.el-select-dropdown__item.selected {
  color: #FFF;
  /*background-color: rgba(40, 80, 130, 0.9);*/
}

/* 下拉框光标选中的样式 */
.el-select-dropdown__item.hover {
  color: #FFF;
  background-color: rgba(40, 80, 130, 1);
}


</style>
