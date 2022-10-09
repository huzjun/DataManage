<template>
    <el-input v-model="filename" placeholder="导出文件名" prefix-icon="el-icon-document" style="width: 120px"/>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  computed: {
    filename: {
      get() {
        return this.value
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  }
}
</script>
