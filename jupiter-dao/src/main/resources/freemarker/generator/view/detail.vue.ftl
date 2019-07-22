<template>
  <div class="create${entity}-container">
    <el-form ref="${entity?uncap_first}Form" :model="${entity?uncap_first}Form" :rules="rules" class="form-container">
      <div class="create${entity}-main-container">
        <el-row>
          <el-col :span="24">
            <div class="${entity?uncap_first}Info-container">
              <el-row>
<#list table.fields as field>
                <el-col :span="8">
                  <el-form-item label-width="60px" label="${field.comment}" class="${entity?uncap_first}Info-container-item">
                    <el-input v-model="${entity?uncap_first}${r'Form.'}${field.propertyName}" />
                  </el-form-item>
                </el-col>
<#if field?has_next && (field?index+1)%3==0>
                </el-row>
              <el-row>
</#if>
</#list>
              </el-row>
            </div>
          </el-col>
        </el-row>
        <el-form-item style="margin-bottom: 30px;">
          <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">
              Submit
          </el-button>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import { add${entity}, get${entity}Detail, update${entity} } from '@/api/${package.ModuleName}'

const defaultForm = {
}

export default {
  name: '${entity}Detail',
  components: { },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const validateRequire = (rule, value, callback) => {
      if (value === '') {
        this.$message({
          message: rule.field + '为必传项',
          type: 'error'
        })
        callback(new Error(rule.field + '为必传项'))
      } else {
        callback()
      }
    }
    return {
      ${entity?uncap_first}Form: Object.assign({}, defaultForm),
      loading: false,
      rules: {
<#list table.fields as field>
        ${field.propertyName}: [{ validator: validateRequire }]<#if field?has_next>,</#if>
</#list>
      },
      tempRoute: {}
    }
  },
  computed: {
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.fetchData(id)
    } else {
      this.${entity?uncap_first}Form = Object.assign({}, defaultForm)
    }

    this.tempRoute = Object.assign({}, this.$route)
  },
  methods: {
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
        this.setTagsViewTitle()
        this.setPageTitle()
      }).catch(err => {
        console.log(err)
      })
    },
    setTagsViewTitle() {
      const title = 'Edit ${entity}'
      const route = Object.assign({}, this.tempRoute, { title: `${r'${title}-${this.'}${entity?uncap_first}Form.id}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = 'Edit ${entity}'
      document.title = `${r'${title} - ${this.'}postForm.id}`
    },
    submitForm() {
      console.log(this.${entity?uncap_first}Form)
      this.$refs.${entity?uncap_first}Form.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.isEdit) {
            add${entity}(this.${entity?uncap_first}Form).then(response => {
              this.$notify({
                title: '成功',
                message: 'Add ${entity} Success',
                type: 'success'
              })
              this.loading = false
            })
          } else {
            update${entity}(this.${entity?uncap_first}Form).then(response => {
              this.$notify({
                title: '成功',
                message: 'Update ${entity} Success',
                type: 'success'
              })
              this.loading = false
            })
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/mixin.scss";

.create${entity}-container {
  position: relative;

  .create${entity}-main-container {
    padding: 40px 45px 20px 50px;

    .${entity?uncap_first}Info-container {
      position: relative;
      @include clearfix;
      margin-bottom: 10px;

      .${entity?uncap_first}Info-container-item {
        float: left;
      }
    }
  }
}
</style>
