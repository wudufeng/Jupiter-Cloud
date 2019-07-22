<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.accountId" placeholder="账号" style="width: 200px;" class="filter-item" clearable />
      <el-date-picker v-model="listQuery.queryBeginTime" type="datetime" format="yyyy-MM-dd HH:mm:ss" placeholder="开始时间" clearable />
      <el-date-picker v-model="listQuery.queryEndTime" type="datetime" format="yyyy-MM-dd HH:mm:ss" placeholder="结束时间" clearable />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
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
<#list table.fields as field>
      <el-table-column label="${field.comment}" width="120px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.${field.propertyName} }}</span>
        </template>
      </el-table-column>
</#list>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="success" size="mini" @click="handleDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog title="详情" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="right" label-width="46%">
        <el-row>
<#list table.fields as field>
          <el-col style="width:400px;"><el-form-item label="${field.comment}"><el-input v-model="temp.${field.propertyName}" /></el-form-item></el-col>
<#if field?has_next && (field?index+1)%2==0>
        </el-row>
        <el-row>
</#if>
</#list>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Close
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { get${entity}List, get${entity}Detail } from '@/api/${package.ModuleName}'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: '${entity}List',
  components: {
    Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        'current': 1,
        'size': 20,
        'condition': {},
        'extra': {},
        'queryBeginTime': null,
        'queryEndTime': null
      },
      temp: {},
      dialogFormVisible: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      get${entity}List(this.listQuery).then(response => {
        this.list = response.data.records
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getList()
    },
    handleDetail(data) {
      this.temp = Object.assign({}, data) // copy obj
      this.dialogFormVisible = true
    }
  }
}
</script>