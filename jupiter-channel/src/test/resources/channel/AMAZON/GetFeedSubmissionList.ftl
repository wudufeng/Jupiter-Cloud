<request description='查询submit处理结果'>
	<#include "common.ftl"/>
	<parameter key='Action' value='GetFeedSubmissionList' />
	<#list feedSubmissionIdList as u>
	<parameter key='FeedSubmissionIdList.ID.${u?index + 1}' value='${u}' />
	</#list>
</request>