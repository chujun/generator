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
package org.mybatis.generator.codegen;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.PropertyRegistry;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;

/**
 * 
 * @author Jeff Butler
 * 
 */
public abstract class AbstractJavaGenerator extends AbstractGenerator {
    public abstract List<CompilationUnit> getCompilationUnits();

    public static Method getGetter(Field field) {
        Method method = new Method();
        method.setName(getGetterMethodName(field.getName(), field
                .getType()));
        method.setReturnType(field.getType());
        method.setVisibility(JavaVisibility.PUBLIC);
        StringBuilder sb = new StringBuilder();
        sb.append("return "); //$NON-NLS-1$
        sb.append(field.getName());
        sb.append(';');
        method.addBodyLine(sb.toString());
        return method;
    }

    //long serialVersionUID
    public static void addUID(TopLevelClass clzz) {
        Field uid = new Field("serialVersionUID", FullyQualifiedJavaType.getPrimitiveLongInstance());
        uid.setFinal(true);
        uid.setStatic(true);
        uid.setVisibility(JavaVisibility.PRIVATE);
        uid.setInitializationString(System.currentTimeMillis() + "L");
        clzz.addField(uid);

    }

    public String getRootClass() {
        String rootClass = introspectedTable
                .getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_CLASS);
        if (rootClass == null) {
            Properties properties = context
                    .getJavaModelGeneratorConfiguration().getProperties();
            rootClass = properties.getProperty(PropertyRegistry.ANY_ROOT_CLASS);
        }

        return rootClass;
    }

    public String getDTORootClass() {
        String rootClass = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_DTO_ROOT_CLASS);
        if (rootClass == null) {
            Properties properties = context.getJavaModelGeneratorConfiguration().getProperties();
            rootClass = properties.getProperty(PropertyRegistry.ANY_DTO_ROOT_CLASS);
        }

        return rootClass;
    }

    public String getAppName() {
        String appName = introspectedTable.getContext().getProperty("appName");
        if (appName != null) {
            return appName.trim();
        }

        return introspectedTable.getContext().getId();
        //
        //        String rootPackge = introspectedTable.getContext().getProperty("rootPackage");
        //        if (rootPackage != null) {
        //
        //        }
    }

    public static void annotationRequestBody(Parameter para) {
        para.addAnnotation("@RequestBody");
    }

    public static void annotationRequestMapping(Method method, String path, String consume, String produce,
                                                RequestMethod... reuqestMethods) {
        StringBuffer sb = new StringBuffer();
        sb.append("@RequestMapping(path = \"");
        sb.append(path == null ? method.getName() : path);
        sb.append("\"");
        if (consume != null) {
            sb.append(",");
            sb.append("consumes=");
            sb.append('"');
            sb.append(consume);
            sb.append('"');
        }

        if (produce != null) {
            sb.append(",");
            sb.append("produces=");
            sb.append('"');
            sb.append(produce);
            sb.append('"');
        }

        if (reuqestMethods != null && reuqestMethods.length > 0) {
            sb.append(",");
            sb.append("method=");
            sb.append('{');
            for (RequestMethod rm : reuqestMethods) {
                sb.append("RequestMethod.").append(rm.name());
                sb.append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append('}');
        }
        sb.append(')');

        method.addAnnotation(sb.toString());
    }

    public static void annotationRequestParam(Parameter para, String name) {
        para.addAnnotation(String.format("@RequestParam(\"%s\")", name));
    }

    public static void annotationDelete(Parameter para, String name) {
        para.addAnnotation(String.format("@PathVariable(\"%s\")", name));
    }

    public static void annotationGet(Parameter para, String name) {
        para.addAnnotation(String.format("@PathVariable(\"%s\")", name));
    }

    protected void addDefaultConstructor(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        method.addBodyLine("super();"); //$NON-NLS-1$
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }
}
