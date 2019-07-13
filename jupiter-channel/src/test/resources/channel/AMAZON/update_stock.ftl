<#macro FeedContent>

<?xml version="1.0" encoding="UTF-8"?>
<AmazonEnvelope xsi:noNamespaceSchemaLocation="amzn-envelope.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<Header>
	<DocumentVersion>1.01</DocumentVersion>
	<MerchantIdentifier>${auth.appKey}</MerchantIdentifier>
</Header>
<MessageType>Inventory</MessageType>
<#list p.datas as u>
<Message>
	<MessageID>${u.messageId}</MessageID>
	<OperationType>Update</OperationType>
	<Inventory>
		<SKU>${u.platformSku}</SKU>
		<Quantity>${u.updateStock}</Quantity>
	</Inventory>
</Message>
</#list>
</AmazonEnvelope>

</#macro>

<request description='提交submit更新库存'>
	<#include "common.ftl"/>
	<parameter key='Action' value='SubmitFeed' />
	<parameter key='FeedType' value='_POST_INVENTORY_AVAILABILITY_DATA_' />
	<parameter key='Content-MD5'  request-type='HEADER' sign='false' resolver='base64Md5Resolver' >
		<value>
			<![CDATA[@Base64Md5<@FeedContent />]]>
		</value>
	</parameter>
	<parameter key='FeedContent' request-type='BODY' sign='false' >
		<value>
			<![CDATA[<@FeedContent />]]>
		</value>
	</parameter>
</request>