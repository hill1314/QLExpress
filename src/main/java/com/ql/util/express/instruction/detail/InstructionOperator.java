package com.ql.util.express.instruction.detail;

import com.ql.util.express.RunEnvironment;
import com.ql.util.express.exception.QLBizException;
import com.ql.util.express.exception.QLException;
import com.ql.util.express.instruction.op.OperatorBase;
import com.ql.util.express.instruction.opdata.ArraySwap;
import com.ql.util.express.instruction.opdata.OperateData;
import com.ql.util.express.instruction.opdata.OperateDataAttr;

import java.util.List;

/**
 * 针对方法等指令的执行
 */
public class InstructionOperator extends Instruction {
    private static final long serialVersionUID = -1217916524030161947L;
    OperatorBase operator;
    int opDataNumber;

    public InstructionOperator(OperatorBase aOperator, int aOpDataNumber) {
        this.operator = aOperator;
        this.opDataNumber = aOpDataNumber;
    }

    public OperatorBase getOperator() {
        return this.operator;
    }


    public void execute(RunEnvironment environment, List<String> errorList) throws Exception {
        //获取方法执行需要的参数
        ArraySwap parameters = environment.popArray(environment.getContext(), this.opDataNumber);
        //打印日志
        debugInfo(environment, parameters);

        try {
            OperateData result = operator.execute(environment.getContext(), parameters, errorList);
            environment.push(result);
            environment.programPointAddOne();
        } catch (QLException e) {
            throw new QLException(getExceptionPrefix(), e);
        } catch (Throwable t) {
            throw new QLBizException(getExceptionPrefix(), t);
        }
    }

    /**
     * 打印debug日志
     *
     * @param environment
     * @param parameters
     */
    private void debugInfo(RunEnvironment environment, ArraySwap parameters) throws Exception {
        if (environment.isTrace() && this.log.isDebugEnabled()) {
            String str = this.operator.toString() + "(";
            OperateData p = null;
            for (int i = 0; i < parameters.length; i++) {
                p = parameters.get(i);
                if (i > 0) {
                    str = str + ",";
                }
                if (p instanceof OperateDataAttr) {
                    str = str + p + ":" + p.getObject(environment.getContext());
                } else {
                    str = str + p;
                }
            }
            str = str + ")";
            this.log.debug(str);
        }
    }

    public String toString() {
        String result = "OP : " + this.operator.toString() + " OPNUMBER[" + this.opDataNumber + "]";
        return result;
    }
}
