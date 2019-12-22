

package com.virugan.impl;

import com.virugan.utils.myLogger;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;

import java.util.ArrayList;
import java.util.List;
/**
 * 生成 mapper 接口 java 类实现方法
 * **/
public class myMapperGenerator extends AbstractJavaGenerator{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CompilationUnit> getCompilationUnits() {

	    String pack = this.context.getProperty("mapperTargetPackage");

	    myLogger.debug("mapperTargetPackage",pack);

	    FullyQualifiedJavaType type = new FullyQualifiedJavaType(pack + "." + this.introspectedTable.getFullyQualifiedTable().getDomainObjectName() + "Mapper");

	    Interface interfaze = new Interface(type);
	    interfaze.setVisibility(JavaVisibility.PUBLIC);

	    interfaze.addImportedType(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()));
		interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
		interfaze.addImportedType(new FullyQualifiedJavaType("com.virugan.context.myPage"));
		interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
		interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));

		//添加mybatis 注解
		interfaze.addAnnotation("@Mapper");

	    String paramterName = this.introspectedTable.getBaseRecordType();
	    paramterName=paramterName.substring(paramterName.lastIndexOf(".")+1,paramterName.length()).toLowerCase();

	    Method selectAll = new Method("mySelect");
		Parameter selectAllP = new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), paramterName);
		selectAll.addParameter(selectAllP);
		selectAll.setReturnType(new FullyQualifiedJavaType("List<"+this.introspectedTable.getBaseRecordType()+">"));
		interfaze.addMethod(selectAll);

		Method selectOneM = new Method("mySelectOne");
		Parameter selectOneP = new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), paramterName);
		selectOneM.addParameter(selectOneP);
		selectOneM.setReturnType(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()));
		interfaze.addMethod(selectOneM);

		Method selPageM = new Method("mySelectPage");
		Parameter selPageP = new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), paramterName);
		selPageP.addAnnotation("@Param(\"record\")");
		selPageM.addParameter(selPageP);
		Parameter pageP = new Parameter(new FullyQualifiedJavaType("myPage"), "mypage");
		pageP.addAnnotation("@Param(\"mypage\")");
		selPageM.addParameter(pageP);
		selPageM.setReturnType(new FullyQualifiedJavaType("List<"+this.introspectedTable.getBaseRecordType()+">"));
		interfaze.addMethod(selPageM);

		Method countM = new Method("myCount");
		Parameter countP = new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), paramterName);
		countM.addParameter(countP);
		countM.setReturnType(new FullyQualifiedJavaType("int"));
		interfaze.addMethod(countM);

		Method insertM = new Method("myInsert");
		Parameter insertP = new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), paramterName);
		insertM.addParameter(insertP);
		insertM.setReturnType(new FullyQualifiedJavaType("int"));
		interfaze.addMethod(insertM);

		Method updateM = new Method("myUpdateOne");
		Parameter updateP = new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), paramterName);
		updateM.addParameter(updateP);
		updateM.setReturnType(new FullyQualifiedJavaType("int"));
		interfaze.addMethod(updateM);

		Method deleteM = new Method("myDelete");
		Parameter deleteP = new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), paramterName);
		deleteM.addParameter(deleteP);
		deleteM.setReturnType(new FullyQualifiedJavaType("int"));
		interfaze.addMethod(deleteM);

		Method updByExmpM = new Method("myUpdateByExmp");
		Parameter updByExmpP1 = new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), "record");
		updByExmpP1.addAnnotation("@Param(\"record\")");
		updByExmpM.addParameter(updByExmpP1);
		Parameter updByExmpP2 = new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), "updkey");
		updByExmpP2.addAnnotation("@Param(\"updkey\")");
		updByExmpM.addParameter(updByExmpP2);
		updByExmpM.setReturnType(new FullyQualifiedJavaType("int"));
		interfaze.addMethod(updByExmpM);


	    List answer = new ArrayList();
	    answer.add(interfaze);
	    return answer;
	  }
}
