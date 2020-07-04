package com.ql.util.express.instruction.op;

public   class OperatorReduce extends Operator {
	public OperatorReduce(String name) {
		this.name = name;
	}
	public OperatorReduce(String aAliasName, String aName, String aErrorInfo) {
		this.name = aName;
		this.aliasName = aAliasName;
		this.errorInfo = aErrorInfo;
	}
	public Object executeInner(Object[] list) throws Exception {
		return OperatorOfNumber.subtract(list[0], list[1],this.isPrecise);
	}
}
