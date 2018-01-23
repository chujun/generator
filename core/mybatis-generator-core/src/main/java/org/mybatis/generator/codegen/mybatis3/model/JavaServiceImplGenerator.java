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

import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.InternalAttribute;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author Jeff Butler
 */
public class JavaServiceImplGenerator extends AbstractJavaGenerator {

    public JavaServiceImplGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.8", table.toString())); //$NON-NLS-1$

        TopLevelClass serviceInterface = new TopLevelClass(
                new FullyQualifiedJavaType(introspectedTable.getAttr(InternalAttribute.ATTR_SERVICE_IMPL_TYPE)));
        FullyQualifiedJavaType inter = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_SERVICE_INTERFACE_TYPE));
        serviceInterface.addImportedType(inter);
        serviceInterface.addSuperInterface(inter);

        FullyQualifiedJavaType daoType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        Field daoField = new Field("dao", daoType);
        daoField.addAnnotation("@Autowired");
        serviceInterface.addField(daoField);
        serviceInterface.addImportedType(daoType);
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(Autowired.class));
        serviceInterface
                .addImportedType(FullyQualifiedJavaType.from(PathVariable.class));
        FullyQualifiedJavaType boType = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_BASE_RECORD_TYPE));
        FullyQualifiedJavaType dtoType = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_DTO_TYPE));
        FullyQualifiedJavaType qsoType = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_QSO_TYPE));
        FullyQualifiedJavaType qdoType = new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_QDO_TYPE));
        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        serviceInterface.addImportedType(boType);
        serviceInterface.addImportedType(dtoType);
        serviceInterface.addImportedType(qsoType);
        serviceInterface.addImportedType(qdoType);
        serviceInterface.addImportedType(listType);

        serviceInterface.addImportedType(FullyQualifiedJavaType.from(Preconditions.class));
        //        serviceInterface.addImportedType(FullyQualifiedJavaType.from(BeanUtils.class));
        serviceInterface.addImportedType(
                FullyQualifiedJavaType.from(com.zhongan.health.common.persistence.CommonFieldUtils.class));
        serviceInterface.addImportedType("com.zhongan.health.common.persistence.SequenceFactory");
        serviceInterface.addImportedType("com.zhongan.health.common.share.enm.YesOrNo");

        boolean microService = microEnable();
        boolean cache = introspectedTable.getRules().enableCache();
        if (microService) {
            addMicroAnnotation(serviceInterface);
        }



        if (cache) {
            /**
             * 默认生成cacheKey
             */
            Method cacheMethod = new Method("cacheKey");
            cacheMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
            Parameter para = new Parameter(boType, "dataobject");
            cacheMethod.addParameter(para);
            cacheMethod.addBodyLine("return \"" + table.getBusinessObjectName() + ".\"+dataobject.getId();");
            serviceInterface.addMethod(cacheMethod);

            cacheMethod = new Method("cacheKey");
            cacheMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
            para = new Parameter(FullyQualifiedJavaType.getPrimitiveLongInstance(), "id");
            cacheMethod.addParameter(para);
            cacheMethod.addBodyLine("return \"" + table.getBusinessObjectName() + ".\"+id;");
            serviceInterface.addMethod(cacheMethod);
            //            serviceInterface.addImportedType("com.zhongan.icare.common.cache.redis.client.JedisTemplate");

            Field jedis = new Field();
            jedis.setName("jedis");
            jedis.setType(new FullyQualifiedJavaType("com.zhongan.icare.common.cache.redis.client.JedisTemplate"));
            jedis.addAnnotation("@Resource");
            serviceInterface.addField(jedis);
        }

        /**
         * 插入
         */
        if (introspectedTable.getRules().generateInsert()) {
            Method method = new Method("create");
            method.addAnnotation("@Override");
            method.setReturnType(FullyQualifiedJavaType.getPrimitiveLongInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(dtoType, "dto");
            method.addParameter(param);
            if (microService) {
                annotationRequestBody(param);
            }
            method.addBodyLine("Preconditions.checkArgument(dto != null,\"dto不能为空.\");");
            method.addBodyLine(doFromTransferDTO());
            method.addBodyLine("Long id=dataobject.getId();");
            method.addBodyLine("if(id==null)");
            method.addBodyLine("{");
            method.addBodyLine("id=SequenceFactory.nextId(" + table.getDomainObjectName() + ".class);");
            method.addBodyLine("dataobject.setId(id);");
            method.addBodyLine("}");
            method.addBodyLine("CommonFieldUtils.populate(dataobject, true);");
            method.addBodyLine(
                    "dao." + introspectedTable.getAttr(InternalAttribute.ATTR_INSERT_SELECTIVE_STATEMENT_ID) + "(dataobject);");
            method.addBodyLine("return id;");
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
        //                annotationRequestBody(param);
        //            }
        //            method.addBodyLine("Preconditions.checkArgument(dto != null,\"dto不能为空.\");");
        //            method.addBodyLine(doFromTransferDTO());
        //            method.addBodyLine("Long id=dataobject.getId();");
        //            method.addBodyLine("if(id==null)");
        //            method.addBodyLine("{");
        //            method.addBodyLine("id=SequenceFactory.nextId(" + table.getDomainObjectName() + ".class);");
        //            method.addBodyLine("dataobject.setId(id);");
        //            method.addBodyLine("}");
        //            method.addBodyLine("CommonFieldUtils.populate(dataobject, true);");
        //            method.addBodyLine("dao." + method.getName() + "(dataobject);");
        //            method.addBodyLine("return id;");
        //            serviceInterface.addMethod(method);
        //        }

        //        /**
        //         * 根据key进行更新
        //         */
        //        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
        //            Method method = new Method(
        //                    introspectedTable.getAttr(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID));
        //            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        //            method.setVisibility(JavaVisibility.PUBLIC);
        //            Parameter param = new Parameter(dtoType, "dto");
        //            method.addParameter(param);
        //            if (microService) {
        //                annotationRequestBody(param);
        //            }
        //            method.addBodyLine("Preconditions.checkArgument(dto != null&&dto.getId()!=null,\"Id不能为空.\");");
        //            method.addBodyLine(doFromTransferDTO());
        //            method.addBodyLine("CommonFieldUtils.populate(dataobject, false);");
        //            method.addBodyLine("int cnt = dao." + method.getName() + "(dataobject);");
        //            if (cache) {
        //                method.addBodyLine("if(cnt>0)");
        //                method.addBodyLine("{");
        //                method.addBodyLine("RedisUtils.remove(cacheKey(dataobject));");
        //                method.addBodyLine("}");
        //            }
        //            method.addBodyLine("return cnt;");
        //            serviceInterface.addMethod(method);
        //        }

        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            Method method = new Method("delete");
            method.addAnnotation("@Override");
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(FullyQualifiedJavaType.getPrimitiveLongInstance(), "id");
            method.addParameter(param);
            if (microService) {
                annotationDelete(param, "id");
            }

            method.addBodyLine("Preconditions.checkArgument(id>0,\"Id必须大于0\");");
            method.addBodyLine("int cnt = dao."
                    + introspectedTable.getAttr(InternalAttribute.ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID) + "(id);");
            if (cache) {
                method.addBodyLine("if(cnt>0)");
                method.addBodyLine("{");
                method.addBodyLine("jedis.removeExceptionOk(cacheKey(id));");
                method.addBodyLine("}");
            }
            method.addBodyLine("return cnt;");
            serviceInterface.addMethod(method);
        }

        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            Method method = new Method("update");
            method.addAnnotation("@Override");
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(dtoType, "dto");
            method.addParameter(param);
            if (microService) {
                annotationRequestBody(param);
            }

            method.addBodyLine("Preconditions.checkArgument(dto != null&&dto.getId()!=null,\"Id不能为空.\");");
            method.addBodyLine(doFromTransferDTO());
            method.addBodyLine("CommonFieldUtils.populate(dataobject, false);");
            method.addBodyLine("int cnt = dao."
                    + introspectedTable.getAttr(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID)
                    + "(dataobject);");
            if (cache) {
                method.addBodyLine("if(cnt>0)");
                method.addBodyLine("{");
                method.addBodyLine("jedis.remove(cacheKey(dataobject));");
                method.addBodyLine("}");
            }
            method.addBodyLine("return cnt;");
            serviceInterface.addMethod(method);
        }

        if (introspectedTable.getRules().generateSelectByCond()) {
            Method method = new Method("list");
            method.addAnnotation("@Override");
            FullyQualifiedJavaType list = FullyQualifiedJavaType.getNewListInstance();
            list.addTypeArgument(dtoType);
            method.setReturnType(list);
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(qsoType, "qso");
            method.addParameter(param);
            if (microService) {
                annotationRequestBody(param);
            }

            method.addBodyLine("Preconditions.checkArgument(qso != null,\"查询条件不能为空.\");");
            //TODO:cj to do
            method.addBodyLine(qsoToqdo());
            method.addBodyLine("qdo.setIsDeleted(YesOrNo.NO.getValue());");
            method.addBodyLine("List<" + table.getDomainObjectName() + "> dataobjects =  dao."
                    + introspectedTable.getAttr(InternalAttribute.ATTR_SELECT_BY_COND) + "(qdo);");
            //            method.addBodyLine("return BeanUtils.simpleDOAndBOConvert(dataobjects, "
            //                    + table.getDomainTransferObjectName() + ".class,null);");

            method.addBodyLine("return to(dataobjects);");

            serviceInterface.addMethod(method);
        }

        if (introspectedTable.getRules().generateCountByCond()) {
            Method method = new Method("count");
            method.addAnnotation("@Override");
            method.setReturnType(FullyQualifiedJavaType.getIntInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(qsoType, "qso");
            method.addParameter(param);
            if (microService) {
                annotationRequestBody(param);
            }

            method.addBodyLine("Preconditions.checkArgument(qso != null,\"查询条件不能为空.\");");
            method.addBodyLine(qsoToqdo());
            method.addBodyLine("qdo.setIsDeleted(YesOrNo.NO.getValue());");
            method.addBodyLine("int cnt = dao." + introspectedTable.getAttr(InternalAttribute.ATTR_COUNT_BY_COND)
                    + "(qdo);");
            method.addBodyLine("return cnt;");
            serviceInterface.addMethod(method);
        }

        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            Method method = new Method("get");
            method.addAnnotation("@Override");
            method.setReturnType(dtoType);
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter param = new Parameter(FullyQualifiedJavaType.getPrimitiveLongInstance(), "id");
            method.addParameter(param);
            if (microService) {
                annotationGet(param, "id");
            }
            method.addBodyLine("Preconditions.checkArgument(id >0,\"id必须大于0\");");
            if (cache) {
                method.addBodyLine("String cacheKey = cacheKey(id);");
                method.addBodyLine(cacheGet());
                method.addBodyLine("if(dataobject==null)");
                method.addBodyLine("{");
                method.addBodyLine("dataobject =  dao."
                        + introspectedTable.getAttr(InternalAttribute.ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID)
                        + "(id);");
                method.addBodyLine("if(dataobject!=null)");
                method.addBodyLine("{");
                method.addBodyLine("{");
                method.addBodyLine("jedis.putExceptionOk(cacheKey, dataobject, " + cacheTime() + ");");
                method.addBodyLine("}");
                method.addBodyLine("}");
                method.addBodyLine("}");

            } else {
                method.addBodyLine(table.getDomainObjectName() + " dataobject =  dao."
                        + introspectedTable.getAttr(InternalAttribute.ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID)
                        + "(id);");
            }
            method.addBodyLine("return to(dataobject);");

            serviceInterface.addMethod(method);
        }
        generateDtoDoMapper(introspectedTable, serviceInterface, boType, dtoType,qsoType,qdoType);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(serviceInterface);

        return answer;
    }

    private void generateDtoDoMapper(IntrospectedTable introspectedTable, TopLevelClass serviceInterface,
                                     FullyQualifiedJavaType doType, FullyQualifiedJavaType dtoType,
                                     FullyQualifiedJavaType qsoType,FullyQualifiedJavaType qdoType) {
        Method do2dto = new Method("to");
        do2dto.setVisibility(JavaVisibility.PRIVATE);
        do2dto.setStatic(false);
        do2dto.setReturnType(dtoType);
        do2dto.addParameter(new Parameter(doType, "d"));
        do2dto.addBodyLine("if(null == d){");
        do2dto.addBodyLine("return null;");
        do2dto.addBodyLine("}");
        StringBuilder sb = new StringBuilder();
        sb.append(dtoType.getShortName());
        sb.append(" t ").append(" = ").append("new").append(" ").append(dtoType.getShortName()).append("();");
        do2dto.addBodyLine(sb.toString());
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            boolean isDeleted = column.getJavaProperty().equalsIgnoreCase("isDeleted");
            String setMethod = setMethodName(column);
            String getMethod = getMethodName(column);
            StringBuilder tl = new StringBuilder();
            if (isDeleted) {
                do2dto.addBodyLine("if(StringUtils.isNotEmpty(d.getIsDeleted())){");
            }
            tl.append("t.").append(setMethod).append('(');
            if (isDeleted) {
                tl.append("com.zhongan.health.common.utils.bean.enm.EnumUtils.byValue(");
            }
            tl.append("d").append('.').append(getMethod).append("()");
            if (isDeleted) {
                tl.append(',');
                tl.append("com.zhongan.health.common.share.enm.YesOrNo.class");
                tl.append(')');
            }
            tl.append(')').append(';');
            do2dto.addBodyLine(tl.toString());
            if(isDeleted){
                do2dto.addBodyLine("}");
            }
        }
        do2dto.addBodyLine("return t;");
        serviceInterface.addMethod(do2dto);

        Method dto2do = generateDTO2DOMethod(doType, dtoType);
        serviceInterface.addMethod(dto2do);

        //populate()
        Method populate = generatePopulateMethod(introspectedTable, doType, dtoType);
        serviceInterface.addMethod(populate);

        serviceInterface.addImportedType("com.google.common.collect.Lists");
        serviceInterface.addImportedType("org.apache.commons.lang3.StringUtils");
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(Collections.class));
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(CollectionUtils.class));

        Method do2dtos = new Method("to");
        serviceInterface.addMethod(do2dtos);
        do2dtos.setVisibility(JavaVisibility.PRIVATE);
        do2dtos.setStatic(false);
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(dtoType);
        do2dtos.setReturnType(returnType);
        FullyQualifiedJavaType paramsType = FullyQualifiedJavaType.getNewListInstance();
        paramsType.addTypeArgument(doType);
        do2dtos.addParameter(new Parameter(paramsType, "dataobjects"));

        do2dtos.addBodyLine("if(CollectionUtils.isEmpty(dataobjects)){");
        do2dtos.addBodyLine("return Collections.emptyList();");
        do2dtos.addBodyLine("}");
        StringBuilder dtos = new StringBuilder();
        dtos.append("List<").append(dtoType.getShortName()).append(">").append(' ').append("dtos=")
                .append("Lists.newArrayListWithCapacity(dataobjects.size());");
        do2dtos.addBodyLine(dtos.toString());
        StringBuilder iterLine = new StringBuilder();

        iterLine.append("for(").append(doType.getShortName()).append(" dataobject:dataobjects){")
                .append("dtos.add(to(dataobject));").append('}');
        do2dtos.addBodyLine(iterLine.toString());
        do2dtos.addBodyLine("return dtos;");

        //buildXXXQDO
        Method qso2qdo = generateBuildQDOFromQSOMethod(qsoType, qdoType);
        serviceInterface.addMethod(qso2qdo);
    }

    private Method generateDTO2DOMethod(FullyQualifiedJavaType doType, FullyQualifiedJavaType dtoType) {
        Method dto2do = new Method("to");
        dto2do.setVisibility(JavaVisibility.PRIVATE);
        dto2do.setStatic(false);
        dto2do.setReturnType(doType);
        dto2do.addParameter(new Parameter(dtoType,"t"));
        dto2do.addBodyLine(doType.getShortName()+" d = new "+doType.getShortName()+"();");
        dto2do.addBodyLine("populate(d,t);");
        dto2do.addBodyLine("return d;");
        return dto2do;
    }

    private Method generatePopulateMethod(IntrospectedTable introspectedTable, FullyQualifiedJavaType doType, FullyQualifiedJavaType dtoType) {
        Method dto2do = new Method("populate");
        dto2do.setVisibility(JavaVisibility.PRIVATE);
        dto2do.setStatic(false);
        dto2do.addParameter(new Parameter(doType, "d"));
        dto2do.addParameter(new Parameter(dtoType, "t"));
        dto2do.addBodyLine("if(null == d){");
        dto2do.addBodyLine("d = new "+doType.getShortName()+"();");
        dto2do.addBodyLine("}");
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            boolean isDeleted = column.getJavaProperty().equalsIgnoreCase("isDeleted");

            String setMethod = setMethodName(column);
            String getMethod = getMethodName(column);
            StringBuilder tl = new StringBuilder();
            if (isDeleted) {
                dto2do.addBodyLine("if (t.getIsDeleted() != null){");
            }
            tl.append("d.").append(setMethod).append('(');
            tl.append("t").append('.').append(getMethod).append("()");
            if (isDeleted) {
                tl.append(".getValue()");
            }
            tl.append(')').append(';');
            dto2do.addBodyLine(tl.toString());
            if(isDeleted){
                dto2do.addBodyLine("}");
            }
        }
        return dto2do;
    }

    private Method generateBuildQDOFromQSOMethod(FullyQualifiedJavaType qsoType, FullyQualifiedJavaType qdoType) {
        Method qso2qdo = new Method("build"+getDomainObjectName()+"QDO");
        qso2qdo.setVisibility(JavaVisibility.PRIVATE);
        qso2qdo.setStatic(false);
        qso2qdo.setReturnType(qdoType);
        qso2qdo.addParameter(new Parameter(qsoType,"qso"));
        qso2qdo.addBodyLine("if(null == qso){");
        qso2qdo.addBodyLine("return null;");
        qso2qdo.addBodyLine("}");
        qso2qdo.addBodyLine(qdoType.getShortName()+" result = new "+qdoType.getShortName()+"();");
        qso2qdo.addBodyLine("populate(result,qso);");
        qso2qdo.addBodyLine("return result;");
        return qso2qdo;
    }

    public static String getMethodName(IntrospectedColumn column) {
        String property = column.getJavaProperty();
        return "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
    }

    public static String setMethodName(IntrospectedColumn column) {
        String property = column.getJavaProperty();
        return "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
    }

    private void addMicroAnnotation(TopLevelClass serviceInterface) {
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(RequestBody.class));
        //        serviceInterface.addImportedType(FullyQualifiedJavaType.from(RequestParam.class));
        serviceInterface.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        serviceInterface.addImportedType(FullyQualifiedJavaType.from(RestController.class));
        //        serviceInterface.addImportedType(FullyQualifiedJavaType.from(RequestMapping.class));
        serviceInterface.addAnnotation("@Service");
        serviceInterface.addAnnotation("@RestController");
    }

    private String qsoToqdo(){
        StringBuffer sb = new StringBuffer(getQDOType().getShortName());

        sb.append(" qdo = build").append(getDomainObjectName()).append("QDO(qso);");
        return sb.toString();
    }

    public String doFromTransferDTO() {
        String doName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        StringBuffer sb = new StringBuffer(doName);
        sb.append(" ");
        sb.append("dataobject");
        sb.append('=');
        //        sb.append("BeanUtils.simpleDOAndBOConvert(");
        sb.append("to(");

        sb.append("dto");
        //        sb.append(",");
        //        sb.append(doName);
        //        sb.append(".class");
        sb.append(')');
        sb.append(';');
        return sb.toString();
    }

    public String cacheGet() {
        String doName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        StringBuffer sb = new StringBuffer(doName);
        sb.append(" ");
        sb.append("dataobject");
        sb.append('=');
        sb.append(" jedis.getExceptionAsNull(cacheKey,");
        sb.append(doName);
        sb.append(".class");
        sb.append(')');
        sb.append(';');
        return sb.toString();
    }

    private boolean microEnable() {
        return "true".equals(introspectedTable.getContext().getProperty("micro.service"));
    }

    private String cacheTime() {
        return introspectedTable.getRules().cacheTime();
        //        String expireTimeInSeconds = introspectedTable.getContext().getProperty("cache.expireInSeconds");
        //        if (StringUtils.isEmpty(expireTimeInSeconds)) {
        //            expireTimeInSeconds = "86400";
        //        }
        //        return expireTimeInSeconds;
    }

    /**
     * 微服务全路径
     *
     * @return
     */
    protected String microFullPath() {
        String version = introspectedTable.getContext().getProperty("micro.version");
        String path = introspectedTable.getContext().getProperty("micro.path");
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

    private String getDomainObjectName(){
        String domainObjectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        if(StringUtils.isNotEmpty(domainObjectName) && domainObjectName.endsWith("DO")){
            return domainObjectName.substring(0,domainObjectName.length()-2);
        }
        return domainObjectName;
    }

    private FullyQualifiedJavaType getQDOType(){
        return new FullyQualifiedJavaType(
                introspectedTable.getAttr(InternalAttribute.ATTR_QDO_TYPE));
    }
}
