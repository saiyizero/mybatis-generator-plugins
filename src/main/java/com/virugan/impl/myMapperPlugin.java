/*@(#)TdMapperPlugin.java   2015-11-6 
 * Copy Right 2015 Bank of Communications Co.Ltd.
 * All Copyright Reserved
 */

package com.virugan.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.virugan.myMethod.mySelectOneGenertor;
import com.virugan.myMethod.mySelectPageGenertor;
import com.virugan.utils.myBeanUtils;
import com.virugan.utils.myLogger;
import org.mybatis.generator.api.*;
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
        this.getPrimaryKeyGenerated(introspectedTable, topLevelClass);
        topLevelClass.setSuperClass("myComponent");
        topLevelClass.addImportedType("com.virugan.interfac.myComponent");
        topLevelClass.addImportedType("java.util.LinkedHashMap");

        return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,IntrospectedTable introspectedTable) {
		return false;
	}

	public boolean modelFieldGenerated(Field field,TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,IntrospectedTable introspectedTable,org.mybatis.generator.api.Plugin.ModelClassType modelClassType) {
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

    private void getPrimaryKeyGenerated(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
	    if(introspectedTable.getPrimaryKeyColumns().size()<=0){
            return;
        }
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("LinkedHashMap"));
        method.setName("getPrimaryKey");

        method.addBodyLine("if(primkeyMap==null){");
        method.addBodyLine("   primkeyMap=new LinkedHashMap<String,Object>();");
        method.addBodyLine("}");


        this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        for(IntrospectedColumn column:primaryKeyColumns){
            StringBuilder sb = new StringBuilder();
            sb.append("primkeyMap.put(\"");
            sb.append(column.getActualColumnName());
            sb.append("\",this.");
            sb.append(column.getActualColumnName());
            sb.append(");");
            method.addBodyLine(sb.toString());

            Field field = new Field();
            field.setName(column.getActualColumnName());
            field.setVisibility(JavaVisibility.PRIVATE);
            field.setType(column.getFullyQualifiedJavaType());
            field.addJavaDocLine("/** "+column.getRemarks()+" **/");
            topLevelClass.addField(field);

            //添加get方法
            Method getmethod = new Method();
            getmethod.setVisibility(JavaVisibility.PUBLIC);
            getmethod.setReturnType(column.getFullyQualifiedJavaType());
            getmethod.setName("get"+column.getActualColumnName().substring(0, 1).toUpperCase() + column.getActualColumnName().substring(1));
            getmethod.addBodyLine("return "+column.getActualColumnName()+";");
            topLevelClass.addMethod(getmethod);

            //添加set方法
            Method setmethod = new Method();
            setmethod.setVisibility(JavaVisibility.PUBLIC);
            setmethod.addParameter(new Parameter(column.getFullyQualifiedJavaType(), column.getActualColumnName()));
            setmethod.setName("set"+column.getActualColumnName().substring(0, 1).toUpperCase() + column.getActualColumnName().substring(1));
            setmethod.addBodyLine("this."+column.getActualColumnName()+" = "+column.getActualColumnName()+
            " == null ? null : "+column.getActualColumnName()+".trim();");
            topLevelClass.addMethod(setmethod);
        }
        method.addBodyLine("return primkeyMap;");

        topLevelClass.addMethod(method);
    }

    //定义生成全局的xml
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable){
        String pack = this.context.getProperty("mapperTargetPackage");
        document.getRootElement().getAttributes().clear();
        document.getRootElement().addAttribute(new Attribute("namespace", pack + "." +introspectedTable.getFullyQualifiedTable().getDomainObjectName()+"Mapper"));

        //新增自定义方法

        /** mySelectOne xml配置 **/
        mySelectOneGenertor selectonegenertor = new mySelectOneGenertor();
        selectonegenertor.setContext(this.context);
        selectonegenertor.setIntrospectedTable(introspectedTable);
        selectonegenertor.addElements(document.getRootElement());

        /** mySelectPage xml配置 **/
        mySelectPageGenertor selectpagegenertor = new mySelectPageGenertor();
        selectpagegenertor.setContext(this.context);
        selectpagegenertor.setIntrospectedTable(introspectedTable);
        selectpagegenertor.addElements(document.getRootElement());

        return true;
    }

    //调用方法返回true 则调用生成对应 id 的xml start
    /** myCount xml配置**/
    public boolean sqlMapCountByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        preProcessFindClause(element);
        element.addElement(buildFindWhereClause(introspectedTable));
        element.addAttribute(new Attribute("parameterType",introspectedTable.getBaseRecordType()));
        List<Attribute> attributes = element.getAttributes();
        for(Attribute attr:attributes){
            if(attr.getName().equals("resultType")){
                element.getAttributes().remove(attr);
            }
        }
        element.addAttribute(new Attribute("resultType","int"));
        return true;
    }

    /** myUpdateOne xml配置**/
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    /** mySelect xml配置**/
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        preProcessFindClause(element);
        element.addElement(buildFindWhereClause(introspectedTable));
        element.addAttribute(new Attribute("parameterType",introspectedTable.getBaseRecordType()));
        return true;
    }

    /** myDelete xml配置**/
    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        preProcessFindClause(element);
        element.addElement(buildFindWhereClause(introspectedTable));
        element.addAttribute(new Attribute("parameterType",introspectedTable.getBaseRecordType()));
        return true;
    }

    /** myInsert xml配置**/
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    /** myUpdateByExmp xml配置**/
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        //不去指定参数
        Attribute needDelete = null;
        List<Attribute> attributes = element.getAttributes();
        for(Attribute att:attributes){
            if(att.getName().equals("parameterType")){
                needDelete=att;
            }
        }
        element.getAttributes().remove(needDelete);

        //遍历元素，删除 if _parameter 节点
        Element delelem=null;
        List<Element> elements = element.getElements();
        for(Element ele:elements){
            if(ele instanceof XmlElement) {
                XmlElement xmlEle = (XmlElement)ele;
                if(xmlEle.getName().equals("if")){
                    delelem=ele;
                }
            }
        }
        element.getElements().remove(delelem);
        element.addElement(buildUpdateWhereClause(introspectedTable));
        return true;
    }

    /** mySelectPage xml配置**/
    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /** mySelectOne xml配置**/
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    //调用方法返回true 则调用生成对应 id 的xml end

    //处理xml中参数
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void preProcessFindClause(XmlElement element) {

        Attribute needDelete = null;
        for(Iterator iterator = element.getAttributes().iterator(); iterator.hasNext();)
        {
            Attribute att = (Attribute)iterator.next();
            if(att.getName().equals("parameterType"))
                needDelete = att;
        }

        element.getAttributes().remove(needDelete);
        List needDeleteList = new ArrayList();
        for(Iterator iterator1 = element.getElements().iterator(); iterator1.hasNext();) {
            Element ele = (Element)iterator1.next();
            if(ele instanceof XmlElement) {
                XmlElement xmlEle = (XmlElement)ele;
                if(xmlEle.getName().equals("if"))
                    needDeleteList.add(ele);
            }
        }

        Element delete;
        for(Iterator iterator2 = needDeleteList.iterator(); iterator2.hasNext(); element.getElements().remove(delete))
            delete = (Element)iterator2.next();

    }


    //处理xml中 where 输入参数
    @SuppressWarnings("rawtypes")
    private XmlElement buildFindWhereClause(IntrospectedTable introspectedTable) {
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

    //构建 指定键值的 update 参数
    private XmlElement buildUpdateWhereClause(IntrospectedTable introspectedTable) {
        XmlElement where = new XmlElement("trim");
        where.addAttribute(new Attribute("prefix", "WHERE"));
        where.addAttribute(new Attribute("prefixOverrides", "AND"));
        for(Iterator iterator = introspectedTable.getAllColumns().iterator(); iterator.hasNext();)
        {
            IntrospectedColumn column = (IntrospectedColumn)iterator.next();
            if(!column.isBLOBColumn())
            {
                XmlElement ifClause = new XmlElement("if");
                ifClause.addAttribute(new Attribute("test", (new StringBuilder(String.valueOf("updkey."+column.getJavaProperty()))).append("!=null").toString()));
                ifClause.addElement(new TextElement((new StringBuilder("AND  ")).append(column.getActualColumnName()).append("=#{").append("updkey."+column.getJavaProperty()).append(",jdbcType=").append(column.getJdbcTypeName()).append("}").toString()));
                where.addElement(ifClause);
            }
        }

        return where;
    }


    //无需调用方法返回false 则不去调用
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientInsertMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectAllMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }


}