package com.virugan.impl;

import com.virugan.utils.myLogger;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class myCommentGenerator extends DefaultCommentGenerator {

    private Properties properties;
    private Properties systemPro;
    private boolean suppressDate;
    private boolean suppressAllComments;
    private String currentDateStr;

    public myCommentGenerator() {
        super();
        properties = new Properties();
        systemPro = System.getProperties();
        suppressDate = false;
        suppressAllComments = false;
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }


    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("/** ");
        sb.append(introspectedColumn.getRemarks());
        sb.append(" **/");
        field.addJavaDocLine(sb.toString());
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {

    }

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {

    }

    public void addGetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {

    }

    public void addSetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {

    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        myLogger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<addClassComment1<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        StringBuilder sb = new StringBuilder();
        sb.append("/** ");
        sb.append("\n  * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append("\n");
        sb.append(" **/");
        innerClass.addJavaDocLine(sb.toString());

    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        myLogger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<addClassComment2<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("/** ");
        sb.append("\n  * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append("\n");
        sb.append(" **/");
        innerClass.addJavaDocLine(sb.toString());
    }
}
