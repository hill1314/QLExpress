package com.ql.util.express.parse;

import com.ql.util.express.context.InstructionSetContext;
import com.ql.util.express.instruction.op.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class追加的字段 管理
 *
 * Created by tianqiao on 16/10/16.
 */
public class AppendingClassFieldManager {

    public class AppendingField {
        public String name;

        public Class<?> bindingClass;

        public Operator op;

        public Class<?> returnType;

        public AppendingField(String name, Class<?> bindingClass, Class<?> returnType, Operator op) {
            this.name = name;
            this.bindingClass = bindingClass;
            this.op = op;
            this.returnType = returnType;
        }
    }

    private List<AppendingField> Fields = new ArrayList<AppendingField>();

    public void addAppendingField(String name, Class<?> bindingClass, Class<?> returnType, Operator op) {
        Fields.add(new AppendingField(name, bindingClass, returnType, op));
    }

    public AppendingField getAppendingClassField(Object object, String FieldName) {
        for (AppendingField Field : Fields) {
            //object是定义类型的子类
            if (FieldName.equals(Field.name) && (object.getClass() == Field.bindingClass || Field.bindingClass.isAssignableFrom(object.getClass()))) {
                return Field;
            }
        }
        return null;

    }

    public Object invoke(AppendingField field, InstructionSetContext context, Object aFieldObject, List<String> errorList) throws Exception {
        Operator op = field.op;
        Object result = op.executeInner(new Object[]{aFieldObject});
        return result;
    }


}
