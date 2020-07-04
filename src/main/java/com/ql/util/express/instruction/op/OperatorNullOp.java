package com.ql.util.express.instruction.op;

import com.ql.util.express.instruction.opdata.ArraySwap;
import com.ql.util.express.context.IExpressContext;
import com.ql.util.express.context.InstructionSetContext;
import com.ql.util.express.instruction.opdata.OperateData;


/**
 * 处理 ",","(",")",";"
 */

public class OperatorNullOp extends OperatorBase {
	public OperatorNullOp(String name) {
		this.name = name;
	}
	public OperatorNullOp(String aAliasName, String aName, String aErrorInfo) {
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}
	public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
		return executeInner(parent);
	}

	public OperateData executeInner(IExpressContext<String,Object> parent) throws Exception {
		return null;
	}
}
