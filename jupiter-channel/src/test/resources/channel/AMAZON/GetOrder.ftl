<request path='/Orders/2013-09-01' description='获取订单'>
	<#include "common.ftl"/>
	<parameter key='Action' value='GetOrder' />
	<parameter key='AmazonOrderId.Id.1' value='${p.orderId}' />
	<parameter key='LastUpdatedAfter' value='${p.lastUpdateAfter?string["yyyy-MM-dd'T'HH:mm:ss"]}' />
</request>