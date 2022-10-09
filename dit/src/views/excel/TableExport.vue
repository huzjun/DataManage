<template>
  <div class="el-card is-always-shadow" style="padding: 10px;">
    <el-row>
      <div style="display: inline-block;padding-bottom: 10px; margin-right: 20px">
        <el-input v-model="listQuery.title" placeholder="搜索关键词" style="width:320px;"
                  class="filter-item" @keyup.enter.native="handleFilter"/>
        <el-button v-waves class="filter-item" type="primary"
                   icon="el-icon-search" @click="handleFilter">
          搜索
        </el-button>
      </div>
      <div style="display: inline-block;">
        <FilenameOption v-model="filename"/>
        <BookTypeOption v-model="bookType"/>
        <el-button :loading="downloadLoading" style="margin-bottom:20px" type="primary" icon="el-icon-document"
                   @click="handleDownload">
          {{ exportTitle }}
        </el-button>
        <el-button style="margin-bottom:20px" type="success" @click="handleEITask">
          导入
        </el-button>
      </div>
    </el-row>


    <el-table
      ref="multipleTable"
      v-loading="listLoading"
      :key="tableKey"
      :data="list"
      element-loading-text="拼命加载中"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick">
      <el-table-column type="selection" align="center" fixed/>

      <el-table-column align="center" v-for="(item,index) in checkMenu.head"
                       :key="index" :prop="item" :label="item"
                       show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row[index] | format }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!--    分页-->
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size"
                @pagination="getTableData"/>

    <!--    导入对话框-->
    <el-dialog title="导出任务" :visible.sync="dialogImportVisible">
      <!--      导出任务表-->
      <el-table :data="eitaskList">
        <el-table-column align="center" property="name" label="导出文件名"></el-table-column>
        <el-table-column align="center" :formatter="dateFormat" property="createTime" label="下载时间" width="170"/>
        <el-table-column align="center" fixed="right" label="操作" width="240">
          <template slot-scope="scope">
            <el-button type="primary" @click="handleEITaskDetail(scope.row)">
              <i class="el-icon-upload el-icon--right"></i> 上传
            </el-button>
            <el-button type="danger" icon="el-icon-delete" @click="handleEITaskDelete(scope)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--      导入详细内容-->
      <el-dialog
        title="导出详细"
        append-to-body
        width="80%"
        v-loading="importLoading"
        :before-close="handleClose"
        :visible.sync="dialogImportItemVisible">
        <upload-excel-component :on-success="handleSuccess" :before-upload="beforeUpload" style="margin-bottom: 10px"/>
        <!--        导出表-->
        <div v-show="!resultVisible">
          <el-tag>导出内容</el-tag>
          <el-table
            :data="eitask.list"
            element-loading-text="拼命加载中"
            height="250"
            border
            style="width: 100%;"
            @selection-change="handleSelectionChange">

            <el-table-column align="center" v-for="(item,index) in eitask.head"
                             :key="index" :prop="item" :label="item">
              <template slot-scope="scope">
                <span>{{ scope.row[index] }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!--        导入结果表-->

        <div v-show="resultVisible">
          <el-tag>导入结果</el-tag>
          <el-table
            :data="eitaskResult.list"
            :cell-class-name="cell"
            element-loading-text="拼命加载中"
            height="360"
            border
            style="width: 100%;"
            @selection-change="handleSelectionChange">

            <el-table-column align="center" v-for="(item,index) in eitaskResult.head"
                             :key="index" :prop="item" :label="item">
              <template slot-scope="scope">
                <span>{{ scope.row[index].value }}</span>
              </template>
            </el-table-column>
          </el-table>
          <el-row style="margin-top: 10px">
            <el-col :span="16">
              <el-tag type="success" effect="dark">新增数据</el-tag>
              <el-tag type="warning" effect="dark">修改数据</el-tag>
              <el-tag type="info" effect="dark">删除数据</el-tag>
              <el-tag type="info" effect="plain">无变化数据</el-tag>
            </el-col>
            <el-col :span="8" align="right">
              <el-button
                type="primary"
                :disabled="!resultVisible"
                :loading="importing"
                @click="executeImport">
                导入数据
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-dialog>
    </el-dialog>
  </div>

</template>

<script>

import moment from "moment";  // 时间格式化
import waves from '@/directive/waves' // waves directive
import _ from 'underscore'
import {parseTime} from '@/utils'
import {fetchList} from '@/api/business'
import {exportList, eiTaskList, eiTaskDelete, importList} from '@/api/eitask'

import FilenameOption from '@/views/excel/components/FilenameOption'
import BookTypeOption from '@/views/excel/components/BookTypeOption'
import Pagination from '@/components/Pagination'
import UploadExcelComponent from '@/components/UploadExcel/index.vue'


export default {
  components: {Pagination, FilenameOption, BookTypeOption, UploadExcelComponent},
  directives: {waves},
  props: {
    checkMenu: {
      type: Object,
      required: true
    },
  },
  data() {
    return {
      filename: '',
      bookType: 'xlsx',
      downloadLoading: false,
      multipleSelection: [],
      // 表格
      exportTitle: '下载导入模板',
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        current: 1,
        size: 20,
        title: undefined
      },
      // 导入
      dialogImportVisible: false,
      dialogImportItemVisible: false,
      eitaskList: [],
      eitask: {},
      eitaskResult: {},
      resultVisible: false,
      loading: false,
      importing: false,
      importLoading: false,
      excelData: {
        header: null,
        results: null
      },
      rules: {
        timestamp: [{type: 'date', required: true, message: 'timestamp is required', trigger: 'change'}],
        title: [{required: true, message: 'title is required', trigger: 'blur'}]
      }
    }
  },
  watch: {
    checkMenu(newValue, oldValue) {
      this.getTableData()
      this.filename = ''  //清空文件名
    }
  },
  filters: {
    format(data) {
      return data
    }
  },
  methods: {
    // 表格
    getTableData() {
      this.listLoading = true
      let param = {
        page: this.listQuery,
        menu: _.omit(this.checkMenu, ['head']),
      }
      fetchList(param)
        .then(response => {
          this.list = response.data.records
          this.total = response.data.total
          this.listQuery.current = response.data.current
          this.listQuery.size = response.data.size

          console.log("getTableData menu data", this.checkMenu, this.list)
          // Just to simulate the time of the request
          setTimeout(() => {
            this.listLoading = false
          }, 0.5 * 1000)
        })
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getTableData()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
      this.exportTitle = this.multipleSelection.length ? '导出选中项目' : '下载导入模板';
    },
    handleRowClick(row,column,event) {
      const selected = this.multipleSelection.some(item => item.id === row.id)  // 是取消选择还是选中
      this.$refs.multipleTable.toggleRowSelection(row, !selected);
    },
    // 导出
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = Object.values(this.checkMenu.head); //表头
        const filterVal = Object.keys(this.checkMenu.head); //表属性
        const list = this.multipleSelection       // 选中项
        const data = this.formatJson(filterVal, list)     // 导出项

        if (this.filename == null || this.filename == '') {
          this.filename = this.checkMenu.label + '-'
            + moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
        }
        const param = {
          name: this.filename,
          menu: _.omit(this.checkMenu, ['head']),
          head: _.pick(this.checkMenu, ['head']),
          list: list,
        }
        exportList(param)
          .then(result => {
            if (result) {
              excel.export_json_to_excel({
                header: tHeader,
                data,
                filename: this.filename,
                autoWidth: true,
                bookType: this.bookType
              })
              this.$refs.multipleTable.clearSelection()
              this.downloadLoading = false
            } else {
              this.downloadLoading = false
              this.$message({
                message: '导出失败',
                type: 'warning'
              })
            }
          });
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => v[j]))
    },
    // 导入
    handleEITask() {
      this.dialogImportVisible = true;
      eiTaskList(_.omit(this.checkMenu, ['head']))
        .then(data => {
          this.eitaskList = data
        })
    },
    handleEITaskDetail(data) {
      this.dialogImportItemVisible = true;
      this.eitask = data
      if (typeof (data.head) == "string")
        this.eitask.head = JSON.parse(this.eitask.head).head
      if (typeof (data.list) == "string")
        this.eitask.list = JSON.parse(this.eitask.list);
    },
    handleEITaskDelete(scope) {
      this.$confirm('此操作将永久删除该任务,相对应导出文件将失效或混乱，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        eiTaskDelete(scope.row.id).then(result => {
          if (result) {
            this.$delete(this.eitaskList, scope.$index)
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          } else {
            this.$message.error('删除失败!');
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    dateFormat(row, column) {
      let dt = new Date(row.createTime);//row 表示一行数据, dateTime 表示要格式化的字段名称
      return moment(dt).format('YYYY-MM-DD HH:mm:ss')
    },
    // 上传
    beforeUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (isLt10M) return true
      this.$message({
        message: '上传文件不能大于10MB',
        type: 'warning'
      })
      return false
    },
    handleClose(done) {
      if (this.resultVisible) {
        this.$confirm('关闭后文件需重新上传，确认关闭？')
          .then(_ => {
            done();
            // 初始化
            this.resultVisible = false;
            this.eitaskResult = {}; //清空
          })
          .catch(_ => {
          });
      } else
        done();
    },
    handleSuccess({results, header}) {
      // let importData = this.formatObjectList(results);
      this.resultVisible = true // 结果显示
      this.compare(this.eitask.list, results, this.eitask.head)
    },
    compare(exportData, importData, head) {
      console.log(exportData, importData)
      this.eitask.context = {};  // 导入结果
      let add = [];   // 新增
      exportData.forEach(item => this.eitask.context[item.id] = item)
      let ids = [];
      let list = [];
      // 处理导入数据
      importData.forEach(item => {
        let obj = {};
        let id = item[head.id];
        if (this.eitask.context[id]) {// 存在
          ids.push(id);  //出现的id
          let d = this.eitask.context[id];
          let modify = false; // 整条记录是否有修改
          Object.keys(head).forEach(key => {
            let mod = d[key] != item[head[key]]; // 单元格属性是否修改
            d[key] = item[head[key]]
            obj[key] = {
              value: d[key],
              status: (mod ? "Warning" : "")
            }
            if(d[key]==undefined||d[key]==null)
              d[key] = '';
            modify |= mod;
          })
          d.status = modify ? "修改" : "未改动";
        } else {//新增
          let o = {};
          Object.keys(head).forEach(key => {
            obj[key] = {
              value: item[head[key]],
              status: "Success"
            }
            o[key] = item[head[key]]
            if(o[key]==undefined||o[key]==null)
              o[key] = '';
          })
          add.push(o);  //添加新增项目
        }
        list.push(obj);
      })
      // 寻找被删除数据
      Object.keys(this.eitask.context).forEach(id => {
        if (ids.indexOf(Number(id)) == -1) {// 没找到，被删除
          let obj = {};
          let d = this.eitask.context[id];
          Object.keys(head).forEach(key => {
            obj[key] = {
              value: d[key],
              status: "Info"
            }
          })
          list.push(obj);
          d.status = "删除";  //标记为删除数据
        }
      })
      this.eitask.context["add"] = add;  //添加数据
      console.log("eitask.context", this.eitask.context)
      this.eitaskResult.list = list;
      this.eitaskResult.head = head;
    },
    // 单元格变色
    cell({row, column, rowIndex, columnIndex}) {
      // console.log(row, column, rowIndex, columnIndex);
      let cls = "";
      Object.keys(row).forEach((key) => {
        if (this.eitask.head[key] == column.property) {
          if (rowIndex === rowIndex && columnIndex === columnIndex) {
            cls = row[key].status;
          }
        }
      })
      return cls + " cell-height";
    },
    // 执行导入
    executeImport() {
      this.importLoading = true;
      importList(this.eitask)
        .then(result => {
          this.importLoading = false;
          if (result) {
            this.dialogImportItemVisible = false
            this.dialogImportVisible = false
            this.$message.success("导入成功");
            this.getTableData();  //重新加载表格数据
            this.resultVisible = false;
            this.eitaskResult = {}; //清空
          } else {
            this.$message.error("导入失败");
          }
        })
    }
  }
}
</script>

<style>
.cell-height {
  max-height: 40px;
}

.Warning {
  background: #E6A23C;
}

.Success {
  background: #67C23A;
}

.Info {
  background: #909399;
}

.Danger {
  background: #F56C6C;
}
</style>
