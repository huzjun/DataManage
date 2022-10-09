<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="模块名" style="width: 500px;" class="filter-item"
                @keyup.enter.native="handleFilter"/>
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

      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" class="demo-table-expand">
            <el-divider content-position="left">查询接口： {{ apiUrl(props.row.selectApi) }}</el-divider>
            <el-divider content-position="left">添加接口： {{ apiUrl(props.row.insertApi) }}</el-divider>
            <el-divider content-position="left">修改接口： {{ apiUrl(props.row.updateApi) }}</el-divider>
            <el-divider content-position="left">删除接口： {{ apiUrl(props.row.deleteApi) }}</el-divider>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column label="序号" prop="id" align="center" width="80">
        <template slot-scope="{$index}">
          <span>{{ $index+1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="标签名" align="center">
        <template slot-scope="{row}">
          <span>{{ row.label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="功能名" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="查询接口" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <el-tag v-if="apiStatus(row.selectApi)" type="info">未绑定</el-tag>
          <el-tag v-if="!apiStatus(row.selectApi)" type="success">已绑定</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="添加接口" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <el-tag v-if="apiStatus(row.insertApi)" type="info">未绑定</el-tag>
          <el-tag v-if="!apiStatus(row.insertApi)" type="success">已绑定</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="修改接口" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <el-tag v-if="apiStatus(row.updateApi)" type="info">未绑定</el-tag>
          <el-tag v-if="!apiStatus(row.updateApi)" type="success">已绑定</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="删除接口" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <el-tag v-if="apiStatus(row.deleteApi)" type="info">未绑定</el-tag>
          <el-tag v-if="!apiStatus(row.deleteApi)" type="success">已绑定</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="状态" align="center">
        <template slot-scope="{row,$index}">
          <el-switch
            active-color="#67C23A"
            inactive-color="#909399"
            v-model="row.status"
            :active-value="1"
            :inactive-value="2"
            @change="handleChangeStatus(row,$index)">
          </el-switch>
        </template>
      </el-table-column>

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
        <el-form-item label="标签名" prop="label">
          <el-select v-model="temp.label" class="filter-item" placeholder="请选择" style="width:100%">
            <el-option v-for="item in labelOptions" :key="item.key" :label="item" :value="item"/>
          </el-select>
        </el-form-item>
        <el-form-item label="模块名" prop="name">
          <el-input v-model="temp.name"/>
        </el-form-item>
        <el-form-item label="查询接口" prop="selectApi">
          <el-select v-model="temp.selectApi" class="filter-item" placeholder="无" style="width:100%">
            <el-option v-for="item in filterApiByMethod('GET')" :key="item.key" :label="item.url" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="添加接口" prop="insertApi">
          <el-select v-model="temp.insertApi" class="filter-item" placeholder="无" style="width:100%">
            <el-option v-for="item in filterApiByMethod('POST')" :key="item.key" :label="item.url" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="修改接口" prop="updateApi">
          <el-select v-model="temp.updateApi" class="filter-item" placeholder="无" style="width:100%">
            <el-option v-for="item in filterApiByMethod('PUT')" :key="item.key" :label="item.url" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="删除接口" prop="deleteApi">
          <el-select v-model="temp.deleteApi" class="filter-item" placeholder="无" style="width:100%">
            <el-option v-for="item in filterApiByMethod('DELETE')" :key="item.key" :label="item.url" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="请求参数映射 {'字段名': '关联键' }" prop="request">
          <json-editor ref="jsonEditor" v-model="temp.request"/>
        </el-form-item>
        <el-form-item label="响应参数映射 {'字段名': '含义' }" prop="response">
          <json-editor ref="jsonEditor" v-model="temp.response"/>
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
import {fetchList, add, save, del} from '@/api/module'
import {gets} from '@/api/api'
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
      apis: [],
      list: null,
      listLoading: true,
      total: 10,
      listQuery: {
        name: undefined,
        page: 1,
        limit: 20,
      },
      labelOptions: ['核心业务','业务模块'],
      temp: {
        id: undefined,
        label: undefined,
        name: undefined,
        selectApi: undefined,
        insertApi: undefined,
        updateApi: undefined,
        deleteApi: undefined,
        request: {
          "name": "title",
          "size": "size",
          "current": "current",
          "coreBusinessId": "coreBusinessId"
        },
        response: {},
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新建'
      },
      rules: {
        labell: [{required: true, message: '链接是必填的', trigger: 'blur'}],
        method: [{required: true, message: '请选择方法', trigger: 'change'}],
        parameter: [{required: true, message: '请填写参数', trigger: 'change'}],
      },
    }
  },
  created() {
    this.getApis()
    this.getList()
  },
  methods: {
    getApis() {
      this.listLoading = true
      gets().then(response => {
        this.apis = response
        this.listLoading = false
      })
    },
    apiStatus(id) {
      return id == undefined || id == null;
    },
    apiUrl(id) {
      if (id == undefined || id == null) return "未绑定";
      let api = this.apis.find((item) => item.id === id)
      return api.url
    },
    getList() {
      this.listLoading = true
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
        label: undefined,
        name: undefined,
        selectApi: undefined,
        insertApi: undefined,
        updateApi: undefined,
        deleteApi: undefined,
        request: {
          "name": "title",
          "size": "size",
          "current": "current",
          "coreBusinessId": "coreBusinessId"
        },
        response: {},
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
      this.temp.request = JSON.parse(this.temp.request)
      this.temp.response = JSON.parse(this.temp.response)
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
          tempData.request = this.formatJson(tempData.request)
          tempData.response = this.formatJson(tempData.response)
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
          tempData.request = this.formatJson(tempData.request)
          tempData.response = this.formatJson(tempData.response)
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
    handleChangeStatus(row) {
      let data = {
        id: row.id,
        status: row.status
      }
      save(data).then(() => {
        this.getList();
        this.$notify({
          title: '成功',
          message: '修改成功',
          type: 'success',
          duration: 2000
        })
      }).catch(() => {
        this.$message.error("修改失败")
      })
    },
    handleDelete(row, index) {
      this.$confirm('确定删除此模块?', '提示', {
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
    filterApiByMethod(method) {
      return this.apis.filter(api => api.method == method);
    },
  }
}
</script>
