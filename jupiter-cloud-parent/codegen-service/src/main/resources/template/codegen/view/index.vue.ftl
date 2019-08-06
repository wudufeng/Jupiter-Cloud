<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.condition.tenantId" placeholder="租户" style="width: 200px;" class="filter-item" clearable />
      <el-date-picker v-model="listQuery.queryBeginTime" type="datetime" format="yyyy-MM-dd HH:mm:ss" placeholder="开始时间" clearable />
      <el-date-picker v-model="listQuery.queryEndTime" type="datetime" format="yyyy-MM-dd HH:mm:ss" placeholder="结束时间" clearable />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleDetail('add', {})">
        新增
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%"
    >
<#list table.fields as field>
      <el-table-column label="${field.comment}" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.${field.propertyName} }}</span>
        </template>
      </el-table-column>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
      <el-table-column label="${field.comment}" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.${field.propertyName} }}</span>
        </template>
      </el-table-column>
</#list>
      <el-table-column label="操作" align="center" width="240">
        <template slot-scope="{row}">
          <el-button size="mini" @click="handleDetail('view',row)">查看</el-button>
          <el-button size="mini" @click="handleDetail('update',row)">编辑</el-button>
          <el-button size="mini" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="${entity?uncap_first}Form" label-position="right" label-width="46%">
        <el-row>
<#list table.fields as field>
          <el-col style="width:400px;"><el-form-item prop="${field.propertyName}" label="${field.comment}"><el-input v-model="${entity?uncap_first}Form.${field.propertyName}" :readonly="dialogStatus==='view'" clearable /></el-form-item></el-col>
<#if field?has_next && (field?index+1)%2==0>
        </el-row>
        <el-row>
</#if>
</#list>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button v-if="dialogStatus!=='view'" v-loading="loading" style="margin-left: 10px;" type="primary" @click="submitForm">
          Submit
        </el-button>
        <el-button @click="dialogFormVisible = false">
          Close
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { get${entity}List, add${entity}, get${entity}Detail, update${entity}, remove${entity} } from '@/api/${package.ModuleName}'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

const defaultForm = {
}

export default {
  name: '${entity}',
  components: {
    Pagination },
  directives: { waves },
  data() {
    return {
      ${entity?uncap_first}Form: Object.assign({}, defaultForm),
      loading: false,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,
      listQuery: {
        'current': 1,
        'size': 20,
        'condition': {},
        'extra': {}
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        view: '查看',
        update: '编辑',
        add: '新增'
      },
      rules: {
<#list table.fields as field>
        ${field.propertyName}: [{ required: true, message: '请输入${field.comment}', trigger: 'blur' }]<#if field?has_next>,</#if>
</#list>
      }
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
    handleDetail(type, data) {
      this.dialogStatus = type
      this.${entity?uncap_first}Form = Object.assign({}, data)
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      this.dialogFormVisible = true
    },
    fetchData(id) {
      const params = {
<#list table.fields as field>
<#if field.keyFlag>
        ${field.name}: id
</#if>
</#list>
      }
      get${entity}Detail(params).then(response => {
        this.${entity?uncap_first}Form = response.data
      }).catch(err => {
        console.log(err)
      })
    },
    submitForm() {
      console.log(this.${entity?uncap_first}Form)
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.dialogStatus === 'add') {
            add${entity}(this.${entity?uncap_first}Form).then(response => {
              this.$notify({
                title: '成功',
                message: '新增${table.comment}成功',
                type: 'success'
              })
              this.list.unshift(this.${entity?uncap_first}Form)
              this.loading = false
              this.dialogFormVisible = false
              this.getList()
            })
          } else {
            update${entity}(this.${entity?uncap_first}Form).then(response => {
              this.$notify({
                title: '成功',
                message: '修改${table.comment}成功',
                type: 'success'
              })
              this.loading = false
              this.dialogFormVisible = false
              this.getList()
            })
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    remove(row, index) {
      const _this = this
      this.$confirm('是否确认删除记录', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          const params = {
<#list table.fields as field>
<#if field.keyFlag>
            ${field.name}: row.${field.name}
</#if>
</#list>
          }
          return remove${entity}(params)
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
