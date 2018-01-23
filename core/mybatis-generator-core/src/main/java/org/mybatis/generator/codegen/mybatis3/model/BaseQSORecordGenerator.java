/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.model;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable.InternalAttribute;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.RootClassInfo;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansField;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * 产生DTO class <br/>
 * 类BaseDTORecordGenerator.java的实现描述：TODO 类实现描述
 * 
 * @author yuanpingan 2016年9月10日 下午5:49:28
 */
public class BaseQSORecordGenerator extends AbstractJavaGenerator {

    public BaseQSORecordGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.8", table.toString())); //$NON-NLS-1$
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_DTO_TYPE));
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addAnnotation("@lombok.Getter");
        topLevelClass.addAnnotation("@lombok.Setter");
        AbstractJavaGenerator.addUID(topLevelClass);
        commentGenerator.addJavaFileComment(topLevelClass);

        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }
        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);

        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

        if (introspectedTable.isConstructorBased()) {
            addParameterizedConstructor(topLevelClass);

            if (!introspectedTable.isImmutable()) {
                addDefaultConstructor(topLevelClass);
            }
        }

        String rootClass = getDTORootClass();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
                continue;
            }

            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            topLevelClass.addField(field);
            topLevelClass.addImportedType(field.getType());

            //            Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            //            topLevelClass.addMethod(method);
            //
            //            method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            //            topLevelClass.addMethod(method);
        }
        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelClass);
        return answer;
    }

    private FullyQualifiedJavaType getSuperClass() {
        FullyQualifiedJavaType superClass;

        String rootClass = getDTORootClass();
        if (rootClass != null) {
            superClass = new FullyQualifiedJavaType(rootClass);
        } else {
            superClass = null;
        }

        return superClass;
    }

    private boolean includePrimaryKeyColumns() {
        return !introspectedTable.getRules().generatePrimaryKeyClass() && introspectedTable.hasPrimaryKeyColumns();
    }

    private boolean includeBLOBColumns() {
        return !introspectedTable.getRules().generateRecordWithBLOBsClass() && introspectedTable.hasBLOBColumns();
    }

    private void addParameterizedConstructor(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        List<IntrospectedColumn> constructorColumns = includeBLOBColumns() ? introspectedTable.getAllColumns()
                : introspectedTable.getNonBLOBColumns();

        for (IntrospectedColumn introspectedColumn : constructorColumns) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(),
                    introspectedColumn.getJavaProperty()));
            topLevelClass.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
        }

        StringBuilder sb = new StringBuilder();
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            boolean comma = false;
            sb.append("super("); //$NON-NLS-1$
            for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }
                sb.append(introspectedColumn.getJavaProperty());
            }
            sb.append(");"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
        }

        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            sb.setLength(0);
            sb.append("this."); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(';');
            method.addBodyLine(sb.toString());
        }

        topLevelClass.addMethod(method);
    }

    private List<IntrospectedColumn> getColumnsInThisClass() {
        List<IntrospectedColumn> introspectedColumns;
        if (includePrimaryKeyColumns()) {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable.getAllColumns();
            } else {
                introspectedColumns = introspectedTable.getNonBLOBColumns();
            }
        } else {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable.getNonPrimaryKeyColumns();
            } else {
                introspectedColumns = introspectedTable.getBaseColumns();
            }
        }

        return introspectedColumns;
    }
}
