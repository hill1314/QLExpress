package com.ql.util.express.instruction.op;

import com.ql.util.express.context.InstructionSetContext;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.opdata.ArraySwap;
import com.ql.util.express.instruction.opdata.OperateData;
import com.ql.util.express.utils.ExpressUtil;

public class OperatorCast extends OperatorBase {
    public OperatorCast(String aName) {
        this.name = aName;
    }

    public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
        Class<?> tmpClass = (Class<?>) list.get(0).getObject(parent);
        Object castObj = ExpressUtil.castObject(list.get(1).getObject(parent), tmpClass, true);
        OperateData result = OperateDataCacheManager.fetchOperateData(castObj, tmpClass);
        return result;
    }
}
