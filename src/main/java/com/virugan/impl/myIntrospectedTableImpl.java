/*@(#)myIntrospectedTableImpl.java   2016-6-3 
 * Copy Right 2016 Bank of Communications Co.Ltd.
 * All Copyright Reserved
 */

package com.virugan.impl;

import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

/**
 * TODO Document myIntrospectedTableImpl
 * <p>
 * @version 1.0.0,2016-6-3
 * @author saiyizero
 * @since 1.0.0
 */
public class myIntrospectedTableImpl  extends IntrospectedTableMyBatis3Impl{

    public myIntrospectedTableImpl(){
    
    }
    
    protected void calculateXmlAttributes(){
        setIbatis2SqlMapPackage(calculateSqlMapPackage());
        setIbatis2SqlMapFileName(calculateIbatis2SqlMapFileName());
        setMyBatis3XmlMapperFileName(calculateMyBatis3XmlMapperFileName());
        setMyBatis3XmlMapperPackage(calculateSqlMapPackage());
        setIbatis2SqlMapNamespace(calculateIbatis2SqlMapNamespace());
        setMyBatis3FallbackSqlMapNamespace(calculateMyBatis3FallbackSqlMapNamespace());
        setSqlMapFullyQualifiedRuntimeTableName(calculateSqlMapFullyQualifiedRuntimeTableName());
        setSqlMapAliasedFullyQualifiedRuntimeTableName(calculateSqlMapAliasedFullyQualifiedRuntimeTableName());

        customerCalculateXmlAttributes();
    }

    protected String calculateMyBatis3XmlMapperFileName(){
        StringBuilder sb = new StringBuilder();
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Mapper.xml");
        return sb.toString();
    }

    private void customerCalculateXmlAttributes(){

        //设置生成xml对应id名称
        setBaseResultMapId("basicResultMap");
        setResultMapWithBLOBsId("resultMapWithBLOBs");
        setBlobColumnListId("blobColumnList");
        setBaseColumnListId("baseColumnList");
        setSelectByExampleStatementId("mySelect");
        setCountByExampleStatementId("myCount");
        setInsertSelectiveStatementId("myInsert");
        setUpdateByPrimaryKeySelectiveStatementId("myUpdateOne");
        setDeleteByExampleStatementId("myDelete");
        setSelectAllStatementId("mySelectPage");
        setUpdateByExampleSelectiveStatementId("myUpdateByExmp");
        setSelectByExampleWithBLOBsStatementId("mySelectOne");
    }

    public String getAliasedFullyQualifiedTableNameAtRuntime(){
        String schemaTable = super.getAliasedFullyQualifiedTableNameAtRuntime();
        String tableNames[] = schemaTable.split("\\.");
        if(tableNames.length == 1)
            return tableNames[0];
        if(tableNames.length == 2)
            return (new StringBuilder("")).append(tableNames[0].toUpperCase()).append(tableNames[1]).toString();
        else
            throw new RuntimeException((new StringBuilder("schemaTable Name invalid [")).append(schemaTable).append("]").toString());
    }

    public String getFullyQualifiedTableNameAtRuntime(){
        String schemaTable = super.getFullyQualifiedTableNameAtRuntime();
        String tableNames[] = schemaTable.split("\\.");
        if(tableNames.length == 1)
            return tableNames[0];
        if(tableNames.length == 2)
        	return (new StringBuilder("")).append(tableNames[0].toUpperCase()).append(tableNames[1]).toString();
        else
            throw new RuntimeException((new StringBuilder("schemaTable Name invalid [")).append(schemaTable).append("]").toString());
    }
    
}
