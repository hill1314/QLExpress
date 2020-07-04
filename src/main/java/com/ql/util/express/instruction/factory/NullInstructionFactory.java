package com.ql.util.express.instruction.factory;

import java.util.Stack;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;
import com.ql.util.express.instruction.ForRelBreakContinue;
import com.ql.util.express.parse.ExpressNode;

public class NullInstructionFactory extends InstructionFactory {
	public boolean createInstruction(ExpressRunner aCompile, InstructionSet result,
                                     Stack<ForRelBreakContinue> forStack, ExpressNode node, boolean isRoot)
			throws Exception {
		  return false;
	}
}
