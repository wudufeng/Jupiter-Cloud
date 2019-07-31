<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
    </resultMap>

</#if>
	<sql id="Table_Name">${table.name}</sql>

<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
		<#list table.commonFields as field><#if field.name == field.propertyName>${field.name}<#else>${field.name}</#if>,</#list>
        <#list table.fields as field>${field.name}<#if field_has_next>,</#if></#list>
    </sql>
</#if>

	<sql id="Base_Where">
	  <#list table.fields as field>
	  <if test="ew.${field.propertyName} != null<#if field.propertyType == 'String'> and ew.${field.propertyName} != ''</#if>">${field.name}=${r'#{ew.'}${field.propertyName}${r'}'} AND</if>
	  </#list>
	  <#list table.commonFields as field>
	  <if test="ew.${field.propertyName} != null<#if field.propertyType == 'String'> and ew.${field.propertyName} != ''</#if>">${field.name}=${r'#{ew.'}${field.propertyName}${r'}'} AND</if>
	  </#list>
	</sql>

	<select id="selectPageList" resultMap="BaseResultMap">
		SELECT <include refid="Base_Column_List" /> 
		FROM <include refid="Table_Name" />
		<where> 
		<trim suffixOverrides="AND">
		  <include refid="Base_Where" />
		  <if test="queryBeginTime != null">create_time&gt;=${r'#{'}queryBeginTime${r'}'} AND</if>
		  <if test="queryEndTime != null">create_time&lt;=${r'#{'}queryEndTime${r'}'} AND</if>
		</trim>
		</where>
	</select>

</mapper>
