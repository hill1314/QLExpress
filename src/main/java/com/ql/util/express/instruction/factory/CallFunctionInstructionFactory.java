package com.ql.util.express.instruction.factory;

import java.util.Stack;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;
import com.ql.util.express.instruction.ForRelBreakContinue;
import com.ql.util.express.instruction.detail.InstructionCallSelfDefineFunction;
import com.ql.util.express.instruction.detail.InstructionOperator;
import com.ql.util.express.instruction.op.OperatorBase;
import com.ql.util.express.parse.ExpressNode;


public class CallFunctionInstructionFactory extends InstructionFactory{
	public boolean createInstruction(ExpressRunner aCompile,
			InstructionSet result, Stack<ForRelBreakContinue> forStack,
			ExpressNode node, boolean isRoot) throws Exception {
		ExpressNode[] children = node.getChildren();
		String functionName = children[0].getValue();
		boolean returnVal = false;
		children = node.getChildren();
		for (int i = 1; i < children.length; i++) {
			boolean tmpHas = aCompile.createInstructionSetPrivate(result,forStack, children[i], false);
			returnVal = returnVal || tmpHas;
		}

		OperatorBase op = aCompile.getOperatorFactory().getOperator(
				functionName);
		int opNum = children.length -1;
		if (op != null) {
			result.addInstruction(new InstructionOperator(op,opNum ).setLine(node.getLine()));
		} else {
			InstructionCallSelfDefineFunction function = new InstructionCallSelfDefineFunction(functionName,opNum);
			result.addInstruction(function.setLine(children[0].getLine()));
		}
		return returnVal;
	}
}


