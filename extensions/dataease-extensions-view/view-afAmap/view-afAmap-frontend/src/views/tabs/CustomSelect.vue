<template>
  <div class="custom-select" :style="{ width: selectWidth, backgroundColor: selectBackground }">
    <div class="selected-item" @click="toggleDropdown">
      <div class="item-icon"></div>
      {{ selectedOption }}
    </div>
    <div v-show="isOpen" class="dropdown" :style="{ backgroundColor: dropdownBackground, width: selectWidth }">
      <div v-for="option in options" :key="option" @click="selectOption(option)">
        <div class="item-icon"></div>
        {{ option }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isOpen: false,
      selectedOption: '',
      options: ['Option 1', 'Option 2', 'Option 3'],
      selectWidth: '200px',
      selectBackground: 'lightgray',
      dropdownBackground: 'lightblue'
    };
  },
  created() {
    // Set the default selectedOption to the first option
    this.selectedOption = this.options[0];
  },
  methods: {
    toggleDropdown() {
      this.isOpen = !this.isOpen;
    },
    selectOption(option) {
      this.selectedOption = option;
      this.isOpen = false;
    }
  }
};
</script>

<style scoped>
.item-icon {
  width: 20px;
  height: 20px;
  background: url('../../assets/Vector136@2x.png') no-repeat;
  background-size: contain;
  display: inline-block;
  margin-right: 5px;
}
.custom-select {
  border: 1px solid;
  position: relative;
  font-family: Arial, sans-serif;
}

.selected-item {
  padding: 10px;
  cursor: pointer;
}

.dropdown {
  margin-top: 1px;
  position: absolute;
  top: 100%;
  left: 0;
  border: 1px solid #ccc;
}

.dropdown div {
  padding: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

</style>
