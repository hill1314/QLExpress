package com.ql.util.express.instruction;

import com.ql.util.express.instruction.detail.InstructionGoTo;

import java.util.ArrayList;
import java.util.List;

/**
 * break 和 continue 指令
 */
public class ForRelBreakContinue {

    /**
     * break 指令集合
     */
    public List<InstructionGoTo> breakList = new ArrayList<InstructionGoTo>();

    /**
     * continue 指令集合
     */
    public List<InstructionGoTo> continueList = new ArrayList<InstructionGoTo>();

}
