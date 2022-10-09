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
import TableExport from "../excel/TableExport";

export default {
  name: 'tree',
  components: {TableExport},
  data() {
    return {
      filterText: '',
      checkMenu: {},
      menuData: [],
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
      //TODO 获得菜单数据
      this.menuData = [
        {
          label: '区域发展',
          children: [{
            label: '推进长三角一体化发展',
            children: [{
              coreBusiness: 1,
              business: 1,
              module: 1,
              label: '工作体系'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 2,
              label: '政策体系'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 3,
              label: '评价体系'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 4,
              label: '评价报告'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 5,
              label: '最佳实践'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 6,
              label: '需求清单'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 7,
              label: '改革清单'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 8,
              label: '子应用场景'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 9,
              label: '协同区'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 10,
              label: '战术任务落实'
            }, {
              coreBusiness: 1,
              business: 1,
              module: 11,
              label: '数据资源'
            }]
          }, {
            label: '推进大湾区建设'
          }, {
            label: '推进大花园建设'
          }]
        }, {
          label: '经济运行',
          children: [{
            label: '经济调节'
          }, {
            label: '价格管理'
          }, {
            label: '应急物资保障综合管理'
          }]
        }, {
          label: '社会建设'
        }]
      this.checkMenu = {
        id: 1,
        coreBusiness: 1,
        business: 1,
        module: 1,
        label: '概览'
      }
    },
  }
}
</script>

