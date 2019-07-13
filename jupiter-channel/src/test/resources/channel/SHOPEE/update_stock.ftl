<request path='/items/update/vars_stock' description='shopee更新库存'>
	<parameter key='' request-type='BODY'  >
		<value>
{
  <#include "common.ftl"/>
  "variations":[
  <#list p as u>
	{"variation_id":${u.platformSku},"item_id":${u.platformProductId},"stock":${u.updateStock}}<#if u_has_next>,</#if>
  </#list>
  ]
}
		</value>
	</parameter>
</request>