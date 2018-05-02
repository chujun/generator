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

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable.InternalAttribute;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author Jeff Butler
 */
public class JavaServiceInterfaceGenerator extends AbstractJavaGenerator {

    public JavaServiceInterfaceGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.8", table.toString())); //$NON-NLS-1$

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_SERVICE_INTERFACE_TYPE));
        //        TopLevelClass topLevelClass = new TopLevelClass(type);
        //        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        Interface serviceInterface = new Interface(type);
        serviceInterface.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType boType = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_BASE_RECORD_TYPE));
        FullyQualifiedJavaType dtoType = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_DTO_TYPE));
        FullyQualifiedJavaType qdtoType = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_QDTO_TYPE));
        serviceInterface
                .addImportedType(FullyQualifiedJavaType.from(PathVariable.class));

        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        serviceInterface.addImportedType(dtoType);
        serviceInterface.addImportedType(listType);
        serviceInterface.addImportedType(qdtoType);

        boolean microService = microEnable();

        if (microService) {
            addMicroAnnotation(serviceInterface);
        }
        /**
         * 插入
         */
        if (introspectedTable.getRules().generateInsert()) {
            Method method = new Method("create");
            method.setReturnType(FullyQualifiedJavaType.getPrimitiveLongInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(dtoType, "dto");
            method.addParameter(param);
            if (microService) {
                annotationRequestMapping(method, method.getName(), null, null, RequestMethod.POST);
                annotationRequestBody(param);
            }
            serviceInterface.addMethod(method);
        }

        /**
         * 可选插入
         */
        //        if (introspectedTable.getRules().generateInsertSelective()) {
        //            Method method = new Method(introspectedTable.getAttr(InternalAttribute.ATTR_INSERT_SELECTIVE_STATEMENT_ID));
        //            method.setReturnType(FullyQualifiedJavaType.getPrimitiveLongInstance());
        //            method.setVisibility(JavaVisibility.PUBLIC);
        //            Parameter param = new Parameter(dtoType, "dto");
        //            method.addParameter(param);
        //            if (microService) {
        //                annotationRequestMapping(method, method.getName(), null, null, RequestMethod.POST);
        //                annotationRequestBody(param);
        //            }
        //            serviceInterface.addMethod(method);
        //        }

        /**
         * 根据key进行更新
         */
        //        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
        //            Method method = new Method("update");
        //            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        //            method.setVisibility(JavaVisibility.PUBLIC);
        //            Parameter param = new Parameter(dtoType, "dto");
        //            method.addParameter(param);
        //            if (microService) {
        //                annotationRequestMapping(method, method.getName(), null, null, RequestMethod.POST);
        //                annotationRequestBody(param);
        //            }
        //            serviceInterface.addMethod(method);
        //        }

        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            Method method = new Method("delete");
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(FullyQualifiedJavaType.getPrimitiveLongInstance(), "id");
            method.addParameter(param);
            if (microService) {
                annotationRequestMapping(method, "delete/{id}", null, null, RequestMethod.DELETE);
                annotationDelete(param, "id");
            }
            serviceInterface.addMethod(method);
        }

        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            Method method = new Method("update");
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(dtoType, "dto");
            method.addParameter(param);
            if (microService) {
                annotationRequestMapping(method, method.getName(), null, null, RequestMethod.POST);
                annotationRequestBody(param);
            }
            serviceInterface.addMethod(method);
        }

        if (introspectedTable.getRules().generateSelectByCond()) {
            Method method = new Method("list");
            FullyQualifiedJavaType list = FullyQualifiedJavaType.getNewListInstance();
            list.addTypeArgument(dtoType);
            method.setReturnType(list);
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(qdtoType, "qso");
            method.addParameter(param);
            if (microService) {
                annotationRequestMapping(method, method.getName(), null, null, RequestMethod.POST);
                annotationRequestBody(param);
            }
            serviceInterface.addMethod(method);
        }

        if (introspectedTable.getRules().generateCountByCond()) {
            Method method = new Method("count");
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(qdtoType, "qso");
            method.addParameter(param);
            if (microService) {
                annotationRequestMapping(method, method.getName(), null, null, RequestMethod.POST);
                annotationRequestBody(param);
            }
            serviceInterface.addMethod(method);
        }

        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            Method method = new Method("get");
            method.setReturnType(dtoType);
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(FullyQualifiedJavaType.getPrimitiveLongInstance(), "id");
            method.addParameter(param);
            if (microService) {
                annotationRequestMapping(method, "get/{id}", null, null, RequestMethod.GET);
                annotationGet(param, "id");
            }
            serviceInterface.addMethod(method);
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(serviceInterface);

        return answer;
    }

    private void addMicroAnnotation(Interface serviceInterface) {
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(RequestBody.class));
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(RequestParam.class));
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(RequestMethod.class));
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(FeignClient.class));
        serviceInterface.addAnnotation(String.format("@FeignClient(name = \"%s\")", getAppName()));
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(RequestMapping.class));
        serviceInterface.addAnnotation(String.format("@RequestMapping(path = \"%s\")", microFullPath()));

    }

    private boolean microEnable() {
        return "true".equals(introspectedTable.getContext().getProperty("micro.service"));
    }

    /**
     * 微服务全路径
     * 
     * @return
     */
    protected String microFullPath() {
        String version = introspectedTable.getTableConfigurationProperty("micro.version");
        String path = introspectedTable.getTableConfigurationProperty("micro.path");
        return "/int/" + (StringUtils.isEmpty(version) ? "v1" : version) + "/" + (StringUtils.isEmpty(path)
                ? introspectedTable.getFullyQualifiedTable().getBusinessObjectName() : path);
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
