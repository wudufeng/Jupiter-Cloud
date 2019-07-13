<request description='查询submit处理结果'>
	<#include "common.ftl"/>
	<parameter key='Action' value='GetFeedSubmissionResult' />
	<parameter key='FeedSubmissionId' value='${p.feedSubmissionId}' />
</request>