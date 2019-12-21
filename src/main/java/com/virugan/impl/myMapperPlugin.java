/*@(#)TdMapperPlugin.java   2015-11-6 
 * Copy Right 2015 Bank of Communications Co.Ltd.
 * All Copyright Reserved
 */

package com.virugan.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.virugan.utils.myBeanUtils;
import com.virugan.utils.myLogger;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;


public class myMapperPlugin extends PluginAdapter{

	public boolean validate(List<String> warnings){
	    return true;
	}
	
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,IntrospectedTable introspectedTable) {
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,IntrospectedTable introspectedTable) {
        myLogger.debug("modelBaseRecordClassGenerated —— topLevelClass");
        this.getPrimaryKeyGenerated(introspectedTable, topLevelClass);

//        myLogger.debugToObject("introspectedTable",introspectedTable);

//		if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
//			List oldFields = new ArrayList(topLevelClass.getFields());
//			topLevelClass.getFields().clear();
//			Field f;
//			for (Iterator iterator = oldFields.iterator(); iterator.hasNext(); topLevelClass.addField(f))
//				f = (Field) iterator.next();
//
//		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,IntrospectedTable introspectedTable) {
//        this.getPrimaryKeyGenerated(introspectedTable, topLevelClass);
		return false;
	}

	public boolean modelFieldGenerated(Field field,TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,IntrospectedTable introspectedTable,org.mybatis.generator.api.Plugin.ModelClassType modelClassType) {
        myLogger.debug("modelFieldGenerated —— topLevelClass");
        int size = introspectedTable.getPrimaryKeyColumns().size();
		if (introspectedTable.getPrimaryKeyColumns().contains(introspectedColumn)) {
			if (size > 1) {
				StringBuilder sb = new StringBuilder();
				sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
				for (int i = 0; i < field.getName().getBytes().length; i++)
					if (i == 0)
						sb.append(Character.toUpperCase((char) field.getName().getBytes()[i]));
					else
						sb.append((char) field.getName().getBytes()[i]);
			}
		}
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable){
		myMapperGenerator generator = new myMapperGenerator();
	    generator.setContext(this.context);
	    generator.setIntrospectedTable(introspectedTable);
	    List<CompilationUnit> units = generator.getCompilationUnits();
	    List generatedFile = new ArrayList();
	    for (CompilationUnit unit : units) {
	      GeneratedJavaFile gjf = new GeneratedJavaFile(unit, this.context.getJavaModelGeneratorConfiguration().getTargetProject(), 
	        this.context.getProperty("javaFileEncoding"), this.context.getJavaFormatter());
	      generatedFile.add(gjf);
	    }
	    return generatedFile;
	}
	
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable){
		String pack = this.context.getProperty("mapperTargetPackage");
	    document.getRootElement().getAttributes().clear();
	    document.getRootElement().addAttribute(new Attribute("namespace", pack + "." +introspectedTable.getFullyQualifiedTable().getDomainObjectName()+"Mapper"));
	    SimpleSelectAllElementGenerator generator = new SimpleSelectAllElementGenerator();
	    generator.setContext(this.context);
	    generator.setIntrospectedTable(introspectedTable);
	    generator.addElements(document.getRootElement());
	    return true;
	}

    private void getPrimaryKeyGenerated(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
        if(introspectedTable.getPrimaryKeyColumns().size()<=0){
            return;
        }
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<String>"));
        method.setName("getPrimaryKey");
        StringBuilder sb = new StringBuilder();
        sb.append("return Arrays.asList(");
        this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        for(IntrospectedColumn column:primaryKeyColumns){
            sb.append("\"");
            sb.append(column.getActualColumnName());
            sb.append("\",");
        }
        String methodBody = sb.toString();
        methodBody=methodBody.substring(0,methodBody.lastIndexOf(","))+");";

        method.addBodyLine(methodBody);
        topLevelClass.addMethod(method);
    }
	
    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        preProcessFindClause(element);
        element.addElement(buildFindWhereClause(introspectedTable));
        return true;
    }
    
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return true;
    }

    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        preProcessFindClause(element);
        element.addElement(buildFindWhereClause(introspectedTable));
        return true;
    }

    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return true;
    }

    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean sqlMapCountByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return true;
    }

    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }
    
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientInsertMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable)
    {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void preProcessFindClause(XmlElement element)
    {
        Attribute needDelete = null;
        for(Iterator iterator = element.getAttributes().iterator(); iterator.hasNext();)
        {
            Attribute att = (Attribute)iterator.next();
            if(att.getName().equals("parameterType"))
                needDelete = att;
        }

        element.getAttributes().remove(needDelete);
        List needDeleteList = new ArrayList();
        for(Iterator iterator1 = element.getElements().iterator(); iterator1.hasNext();)
        {
            Element ele = (Element)iterator1.next();
            if(ele instanceof XmlElement)
            {
                XmlElement xmlEle = (XmlElement)ele;
                if(xmlEle.getName().equals("if"))
                    needDeleteList.add(ele);
            }
        }

        Element delete;
        for(Iterator iterator2 = needDeleteList.iterator(); iterator2.hasNext(); element.getElements().remove(delete))
            delete = (Element)iterator2.next();

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
                ifClause.addAttribute(new Attribute("test", (new StringBuilder(String.valueOf(column.getJavaProperty()))).append("!=null").toString()));
                ifClause.addElement(new TextElement((new StringBuilder("AND  ")).append(column.getActualColumnName()).append("=#{").append(column.getJavaProperty()).append(",jdbcType=").append(column.getJdbcTypeName()).append("}").toString()));
                where.addElement(ifClause);
            }
        }

        return where;
    }


}