<request path='/orders/detail' description='获取订单详情'>
	<parameter key='' request-type='BODY'  >
		<value>
{
	<#include "common.ftl"/>
    "ordersn_list":[<#list p as u>"${u}"<#if u?has_next>,</#if></#list>]
}
		</value>
	</parameter>
</request>