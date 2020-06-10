package com.ql.util.express.instruction;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;
import com.ql.util.express.exception.QLException;
import com.ql.util.express.instruction.detail.Instruction;
import com.ql.util.express.instruction.detail.InstructionCallMacro;
import com.ql.util.express.instruction.detail.InstructionLoadAttr;
import com.ql.util.express.parse.ExpressNode;

import java.util.Stack;


public class LoadAttrInstructionFactory extends InstructionFactory {
    public boolean createInstruction(ExpressRunner aCompile, InstructionSet result,
                                     Stack<ForRelBreakContinue> forStack, ExpressNode node, boolean isRoot)
            throws Exception {
        FunctionInstructionSet functionSet = result.getMacroDefine(node.getValue());

        if (functionSet != null) {//是宏定义
			Instruction instruction = new InstructionCallMacro(node.getValue()).setLine(node.getLine()).setLine(node.getLine());
			result.insertInstruction(result.getCurrentPoint() + 1, instruction);
        } else {
            result.addInstruction(new InstructionLoadAttr(node.getValue()).setLine(node.getLine()));
            if (node.getChildren().length > 0) {
                throw new QLException("表达式设置错误");
            }
        }
        return false;
    }
}
