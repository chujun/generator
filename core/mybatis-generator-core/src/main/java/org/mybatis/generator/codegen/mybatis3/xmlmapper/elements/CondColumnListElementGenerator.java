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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable.InternalAttribute;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * cond list
 */
public class CondColumnListElementGenerator extends AbstractXmlElementGenerator {

    public CondColumnListElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                introspectedTable.getAttr(InternalAttribute.ATTR_BASE_COND_LIST)));

        context.getCommentGenerator().addComment(answer);
        XmlElement where = new XmlElement("where");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            sb.setLength(0);

            XmlElement ifE = new XmlElement("if");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            ifE.addAttribute(new Attribute("test", sb.toString()));
            sb.setLength(0);
            if (i > 0) {
                sb.append(" AND ");
            }
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            ifE.addElement(new TextElement(sb.toString()));
            where.addElement(ifE);
            i++;
        }
        answer.addElement(where);
        parentElement.addElement(answer);
    }
}
