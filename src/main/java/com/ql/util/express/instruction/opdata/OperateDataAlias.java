package com.ql.util.express.instruction.opdata;

import com.ql.util.express.context.InstructionSetContext;

/**
 * 别名数据类型
 */
public class OperateDataAlias extends OperateDataAttr {
    OperateDataAttr realAttr;

    public OperateDataAlias(String aName, OperateDataAttr aRealAttr) {
        super(aName, null);
        this.realAttr = aRealAttr;
    }

    public String toString() {
        try {
            return this.name + "[alias=" + this.realAttr.name + "]";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public Object getObjectInner(InstructionSetContext context) {
        try {
            return realAttr.getObject(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> getType(InstructionSetContext context) throws Exception {
        return realAttr.getType(context);
    }

    public void setObject(InstructionSetContext context, Object object) {
        try {
            realAttr.setObject(context, object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
