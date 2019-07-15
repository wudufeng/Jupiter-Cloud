package com.jupiterframework.mybatis;

import java.util.List;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import com.baomidou.mybatisplus.entity.TableFieldInfo;
import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.mapper.AutoSqlInjector;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.toolkit.TableInfoHelper;


public class CustomSqlInjector extends AutoSqlInjector {

	@Override
	public void inject(Configuration configuration, MapperBuilderAssistant builderAssistant, Class<?> mapperClass,
			Class<?> modelClass, TableInfo table) {
		insertList(mapperClass, modelClass, table);
	}

	private void insertList(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {

		KeyGenerator keyGenerator = new NoKeyGenerator();
		StringBuilder fieldBuilder = new StringBuilder();
		StringBuilder placeholderBuilder = new StringBuilder();
		CustomSqlMethod sqlMethod = CustomSqlMethod.INSERT_LIST;

		placeholderBuilder.append("<foreach collection=\"data\" item=\"item\" index=\"index\" separator=\" ,\" >")
			.append("(");
		fieldBuilder.append("\n<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
		placeholderBuilder.append("\n<trim suffixOverrides=\",\">\n");
		String keyProperty = null;
		String keyColumn = null;

		// 表包含主键处理逻辑,如果不包含主键当普通字段处理
		if (StringUtils.isNotEmpty(table.getKeyProperty())) {
			if (table.getIdType() == IdType.AUTO) {
				/** 自增主键 */
				keyGenerator = new Jdbc3KeyGenerator();
				keyProperty = table.getKeyProperty();
				keyColumn = table.getKeyColumn();
			} else {
				if (null != table.getKeySequence()) {
					keyGenerator = TableInfoHelper.genKeyGenerator(table, builderAssistant, sqlMethod.getMethod(),
						languageDriver);
					keyProperty = table.getKeyProperty();
					keyColumn = table.getKeyColumn();
					fieldBuilder.append(table.getKeyColumn()).append(",");
					placeholderBuilder.append("#{").append(table.getKeyProperty()).append("},");
				} else {
					/** 用户输入自定义ID */
					fieldBuilder.append(table.getKeyColumn()).append(",");
					// 正常自定义主键策略
					placeholderBuilder.append("#{").append("item.").append(table.getKeyProperty()).append("},");
				}
			}
		}

		// 是否 IF 标签判断
		List<TableFieldInfo> fieldList = table.getFieldList();
		for (TableFieldInfo fieldInfo : fieldList) {
			// 在FieldIgnore,INSERT_UPDATE,INSERT 时设置为false
			fieldBuilder.append(fieldInfo.getColumn()).append(",");
			placeholderBuilder.append("#{").append("item.").append(fieldInfo.getEl()).append("},");
		}
		fieldBuilder.append("\n</trim>");
		placeholderBuilder.append("\n</trim>");
		placeholderBuilder.append(")").append("</foreach>");
		String sql = String.format(sqlMethod.getSql(), table.getTableName(), fieldBuilder.toString(),
			placeholderBuilder.toString());
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		this.addInsertMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource, keyGenerator,
			keyProperty, keyColumn);

	}
}
