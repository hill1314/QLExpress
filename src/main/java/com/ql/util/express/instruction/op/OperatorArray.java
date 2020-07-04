package com.ql.util.express.instruction.op;

import com.ql.util.express.instruction.opdata.ArraySwap;
import com.ql.util.express.context.InstructionSetContext;
import com.ql.util.express.instruction.opdata.OperateData;
import com.ql.util.express.exception.QLException;
import com.ql.util.express.instruction.OperateDataCacheManager;

public class OperatorArray extends OperatorBase {
	public OperatorArray(String aName) {
		this.name = aName;
	}
	public OperatorArray(String aAliasName, String aName, String aErrorInfo) {
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}

	public OperateData executeInner(InstructionSetContext context, ArraySwap list) throws Exception {
		OperateData p0 = list.get(0);
		if(p0 == null || p0.getObject(context) == null){
			throw new QLException("对象为null,不能执行数组相关操作");
		}
		Object tmpObject = p0.getObject(context);
	    if( tmpObject.getClass().isArray() == false){
			throw new QLException("对象:"+ tmpObject.getClass() +"不是数组,不能执行相关操作" );
		}
	    int index = ((Number)list.get(1).getObject(context)).intValue();
	    OperateData result  = OperateDataCacheManager.fetchOperateDataArrayItem((OperateData)p0,index);
		return result;
	}
}
