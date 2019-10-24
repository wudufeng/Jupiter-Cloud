<template>
  <div class="app-container">
    <avue-crud
      ref="crud"
      v-model="data"
      :option="option"
      :data="datas"
      :table-loading="loading"
      :page="page"
      @search-change="handleSearch"
      @row-save="handleAdd"
      @row-update="handleUpdate"
      @row-del="handleDel"
      @refresh-change="handleGetList"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    >
      <!--
      <template slot="searchMenu" slot-scope="scope">
        <el-button size="small" icon="el-icon-refresh" @click="refresh(scope)">自定义搜索按钮</el-button>
      </template>
      <template slot="menu" slot-scope="scope">
        <router-link :to="/url">
          <el-button icon="el-icon-refresh" class="el-button el-button--text el-button--small">自定义操作按钮</el-button>
        </router-link>
      </template>
      <template slot="menuForm">
        <el-button type="info" icon="el-icon-check" size="small">自定义表单按钮</el-button>
      </template>
      -->
    </avue-crud>
  </div>
</template>

<script>
import { getList, add, update, remove } from '@/api/crud'

export default {
  name: '${entity}',
  components: { },
  props: {
  },
  data() {
    return {
      routerVal: '',
      data: {},
      loading: false,
      query: {
        current: 1,
        size: 100,
        condition: { }
      },
      page: {
        total: 0,
        currentPage: 1,
        pageSize: 100
      },
      datas: [],
      option: {
        border: true,
        searchResetBtn: false,
        viewBtn: true,
        delBtn: true,
        index: true,
        headerAlign: 'center',
        align: 'center',
        labelWidth: '42%',
        dialogType: 'drawer',
        indexLabel: '序号',
        column: [
<#list table.fields as field>
          { label: '${field.comment}', prop: '${field.propertyName}',<#if field.keyFlag> addDisplay: false, addDisabled: true, editDisabled: true, hide: true,</#if> rules: [{ required: true, message: '${field.comment}不能为空', trigger: 'blur' }]<#if field.comment?index_of(":") != -1>, type: 'select', dicData: [<#list field.comment?split(":")[1]?split(",") as item>{ value: '${item?split("-")[0]}', label: '${item?split("-")[1]}' }<#if item?has_next>, </#if></#list>]</#if> }<#if field?has_next>,</#if>
</#list>
<#list table.commonFields as field>
          { label: '${field.comment}', prop: '${field.propertyName}', addDisplay: false, addDisabled: true, editDisplay: false, editDisabled: true }<#if field?has_next>,</#if>
</#list>

        ]
      }
    }
  },
  created() {
    this.routerVal = this.$route.path
  },
  methods: {
    handleGetList() {
      this.query.current = this.page.currentPage
      this.query.size = this.page.pageSize
      this.loading = true
      getList(this.routerVal, this.query).then(res => {
        this.datas = res.data.records
        this.page.total = res.data.total
      })
      this.loading = false
    },
    handleSearch(params) {
      this.page.currentPage = 1
      this.query.condition = params
      // this.query.condition = this.$refs['crud'].$refs['headerSearch'].searchForm
      this.handleGetList()
    },
    handleCurrentChange(currentPage) {
      this.page.currentPage = currentPage
      this.handleGetList()
    },
    handleSizeChange(pageSize) {
      this.page.pageSize = pageSize
      this.handleGetList()
    },
    handleAdd(row, done, loading) {
      this.data.id = ''
      add(this.routerVal, this.data).then(() => {
        this.loading = false
        this.$notify({
          title: 'Success',
          message: '新增成功!',
          type: 'success'
        })
        loading()
        setTimeout(() => {
          done()
        }, 3000)
      })
    },
    handleUpdate(row, index, done, loading) {
      update(this.routerVal, this.data).then(() => {
        this.loading = false
        this.$notify({
          title: 'Success',
          message: '更新成功!',
          type: 'success'
        })
        loading()
      })
    },
    handleDel(scope) {
      const _this = this
      this.$confirm('是否确认删除记录', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return remove(_this.routerVal, <#list table.fields as field><#if field.keyFlag>scope.${field.propertyName}</#if></#list>)
        })
        .then(data => {
          _this.$message({
            showClose: true,
            message: '删除成功',
            type: 'success'
          })
          this.getList()
        })
    }
  }
}
</script>
