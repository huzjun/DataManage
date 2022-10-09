<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="5">
        <div class="el-card is-always-shadow" style="padding: 10px;">
          <el-tree
            ref="menu"
            class="filter-tree"
            accordion
            default-expand-all
            node-key="id"
            :data="menuData"
            :props="defaultProps"
            :filter-node-method="filterNode"
            @node-click="handleMenuClick"/>
        </div>
      </el-col>

      <el-col :span="19">
        <TableExport :check-menu="checkMenu"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {coreBusinessMenu} from '@/api/business'
import TableExport from "@/views/excel/TableExport";

export default {
  name: 'tree',
  components: {TableExport},
  data() {
    return {
      filterText: '',
      menuData: [],
      checkMenu: {},
      defaultProps: {
        children: 'children',
        label: 'label'
      },
    }
  },
  created() {
    this.getMenu()
  },
  watch: {
    filterText(val) {
      this.$refs.menu.filter(val)
    }
  },

  methods: {
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    handleMenuClick(data, path) {
      if (path.isLeaf) //是子菜单
        this.checkMenu = data
    },
    getMenu() {
      this.listLoading = true;
      coreBusinessMenu()
        .then(data => {
          this.listLoading = false;
          this.menuData = data;
          this.checkFist();
        })
    },
    checkFist() {
      this.checkMenu = this.menuData[0];
      while (this.checkMenu.children) {
        this.checkMenu = this.checkMenu.children[0]
      }
    }
  }
}
</script>

