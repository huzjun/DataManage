<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.url" placeholder="链接" style="width: 500px;" class="filter-item"
                @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.method" placeholder="方法" clearable filterable style="width: 90px"
                 class="filter-item">
        <el-option v-for="item in methodOptions" :key="item" :label="item" :value="item"/>
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit"
                 @click="handleCreate">
        添加
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;">
      <el-table-column label="序号" prop="id" align="center" width="80">
        <template slot-scope="{$index}">
          <span>{{ $index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="链接" align="center">
        <template slot-scope="{row}">
          <span>{{ row.url }}</span>
        </template>
      </el-table-column>
      <el-table-column label="方法" align="center" width="80px">
        <template slot-scope="{row}">
          <span>{{ row.method }}</span>
        </template>
      </el-table-column>
      <!--      <el-table-column label="参数" width="110px" align="center">-->
      <!--        <template slot-scope="{row}">-->
      <!--          <span>{{ row.parameter }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>


    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
                @pagination="getList"/>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="70%">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px"
               style="width: 90%; margin:0px auto;">
        <el-form-item label="链接" prop="url">
          <el-input v-model="temp.url"/>
        </el-form-item>
        <el-form-item label="方法" prop="method">
          <el-select v-model="temp.method" class="filter-item" placeholder="请选择">
            <el-option v-for="item in methodOptions" :key="item.key" :label="item" :value="item"/>
          </el-select>
        </el-form-item>
        <el-form-item label="参数 {'字段名': '值'|null }" prop="parameter">
          <json-editor ref="jsonEditor" v-model="temp.parameter"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?insertData():updateData()">
          提交
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {fetchList, add, save, del} from '@/api/api'
import waves from '@/directive/waves' // waves directive
import JsonEditor from '@/components/JsonEditor'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'api',
  directives: {waves},
  components: {Pagination, JsonEditor},
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      total: 10,
      listQuery: {
        method: undefined,
        url: undefined,
        page: 1,
        limit: 20,
      },
      methodOptions: ['GET', 'POST', 'PUT', 'DELETE'],
      temp: {
        id: undefined,
        url: undefined,
        method: undefined,
        parameter: undefined,
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      rules: {
        url: [{required: true, message: '链接是必填的', trigger: 'blur'}],
        method: [{required: true, message: '请选择方法', trigger: 'change'}],
        parameter: [{required: true, message: '请填写参数', trigger: 'change'}],
      },
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      if (this.listQuery.method === '')
        this.listQuery.method = undefined
      fetchList(this.listQuery).then(response => {
        this.list = response.records
        this.total = response.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        url: undefined,
        method: undefined,
        parameter: {},
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.parameter = JSON.parse(this.temp.parameter)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    insertData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.parameter = this.formatJson(tempData.parameter)
          add(tempData).then(() => {
            this.dialogFormVisible = false
            this.getList();
            this.$notify({
              title: '成功',
              message: '添加成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    updateData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.parameter = this.formatJson(tempData.parameter)
          save(tempData).then(() => {
            this.dialogFormVisible = false
            this.getList();
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('确定删除此接口?', '提示', {
        confirmButtonText: '确定',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        del(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.list.splice(index, 1)
        }).catch(() => {
          this.$message.error("删除失败")
        })
      })
    },
    formatJson(json) {
      let temp = typeof json == 'string' ? JSON.parse(json) : json;
      return JSON.stringify(temp);
    },
  }
}
</script>

