package com.ql.util.express.instruction.op;

import com.ql.util.express.config.QLExpressRunStrategy;
import com.ql.util.express.context.InstructionSetContext;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.opdata.ArraySwap;
import com.ql.util.express.instruction.opdata.OperateData;

public class OperatorField extends OperatorBase {
    String filedName;

    public OperatorField() {
        this.name = "FieldCall";
    }

    public OperatorField(String aFieldName) {
        this.name = "FieldCall";
        this.filedName = aFieldName;

    }

    public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
        OperateData operateData = list.get(0);
        if (operateData == null && QLExpressRunStrategy.isAvoidNullPointer()) {
            return null;
        }
        Object obj = operateData.getObject(parent);
        return OperateDataCacheManager.fetchOperateDataField(obj, this.filedName);
    }

    public String toString() {
        return this.name + ":" + this.filedName;
    }
}
