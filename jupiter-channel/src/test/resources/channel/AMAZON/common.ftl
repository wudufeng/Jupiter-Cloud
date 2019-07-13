<#setting time_zone="UTC/GMT">
	<parameter key='AWSAccessKeyId' value='${auth.accessKey}' />
	<parameter key='MWSAuthToken' value='<#if auth.accessToken?? && auth.accessToken != "">${auth.accessToken}<#else>${auth.securetKey}</#if>' />
	<parameter key='SellerId' value='${auth.appKey}' />
	<parameter key='SignatureMethod' value='HmacSHA256' />
	<parameter key='SignatureVersion' value='2' />
	<parameter key='Timestamp' value='${.now?string["yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"]}' />
	<parameter key='Version' value='2009-01-01' />
