/*@(#)SimpleSelectAllElementGenerator.java   2016-6-3 
 * Copy Right 2016 Bank of Communications Co.Ltd.
 * All Copyright Reserved
 */

package com.virugan.impl;

import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * TODO Document SimpleSelectAllElementGenerator
 * <p>
 * @author saiyizero
 * @since 1.0.0
 */
public class SimpleSelectAllElementGenerator extends AbstractXmlElementGenerator{

	public SimpleSelectAllElementGenerator() {
	}

	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("select");
		answer.addAttribute(new Attribute("id", introspectedTable.getSelectAllStatementId()));
		answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
		context.getCommentGenerator().addComment(answer);
		answer.addElement(new TextElement("select "));
		answer.addElement(getBaseColumnListElement());
		StringBuilder sb = new StringBuilder();
		sb.setLength(0);
		sb.append("from ");
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));
		answer.addElement(buildFindWhereClause(introspectedTable));
		parentElement.addElement(answer);
	}


    @SuppressWarnings("rawtypes")
	private XmlElement buildFindWhereClause(IntrospectedTable introspectedTable)
    {
        XmlElement where = new XmlElement("trim");
        where.addAttribute(new Attribute("prefix", "WHERE"));
        where.addAttribute(new Attribute("prefixOverrides", "AND"));
        for(Iterator iterator = introspectedTable.getAllColumns().iterator(); iterator.hasNext();)
        {
            IntrospectedColumn column = (IntrospectedColumn)iterator.next();
            if(!column.isBLOBColumn())
            {
                XmlElement ifClause = new XmlElement("if");
                ifClause.addAttribute(new Attribute("test", (new StringBuilder("record.").append(String.valueOf(column.getJavaProperty()))).append("!=null").toString()));
                ifClause.addElement(new TextElement((new StringBuilder("AND  ")).append(column.getActualColumnName()).append("=#{").append("record.").append(column.getJavaProperty()).append(",jdbcType=").append(column.getJdbcTypeName()).append("}").toString()));
                where.addElement(ifClause);
            }
        }

        return where;
    }
    
}
