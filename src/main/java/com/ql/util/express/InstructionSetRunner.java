package com.ql.util.express;

import com.ql.util.express.config.QLExpressTimer;
import com.ql.util.express.context.IExpressContext;
import com.ql.util.express.context.InstructionSetContext;
import com.ql.util.express.instruction.CallResult;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.loader.ExpressLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class InstructionSetRunner {
    private static final Log log = LogFactory.getLog(InstructionSetRunner.class);


    /**
     * 指令集执行过程：InstructionSet + context ->Object
     *
     * @param runner
     * @param sets
     * @param loader
     * @param aContext
     * @param errorList
     * @param isTrace
     * @param isCatchException
     * @param aLog
     * @param isSupportDynamicFieldName
     * @return
     * @throws Exception
     */
    public static Object executeOuter(ExpressRunner runner, InstructionSet sets, ExpressLoader loader,
                                      IExpressContext<String, Object> aContext, List<String> errorList,
                                      boolean isTrace, boolean isCatchException,
                                      Log aLog, boolean isSupportDynamicFieldName) throws Exception {
        try {
            //开始计时
            QLExpressTimer.startTimer();
            OperateDataCacheManager.push(runner);
            return execute(runner, sets, loader, aContext, errorList, isTrace, isCatchException, true, aLog, isSupportDynamicFieldName);
        } finally {
            OperateDataCacheManager.resetCache(runner);
        }
    }

    /**
     * 批量执行指令集合，指令集间可以共享 变量和函数
     *
     * @param runner
     * @param sets
     * @param loader
     * @param aContext
     * @param errorList
     * @param isTrace
     * @param isCatchException
     * @param isReturnLastData
     * @param aLog
     * @param isSupportDynamicFieldName
     * @return
     * @throws Exception
     */
    public static Object execute(ExpressRunner runner, InstructionSet sets, ExpressLoader loader,
                                 IExpressContext<String, Object> aContext, List<String> errorList,
                                 boolean isTrace, boolean isCatchException,
                                 boolean isReturnLastData, Log aLog, boolean isSupportDynamicFieldName)
            throws Exception {
        InstructionSetContext context = OperateDataCacheManager.fetchInstructionSetContext(true, runner, aContext, loader, isSupportDynamicFieldName);
        Object result = execute(sets, context, errorList, isTrace, isCatchException, isReturnLastData, aLog);
        return result;
    }

    public static Object execute(InstructionSet set,
                                 InstructionSetContext context, List<String> errorList, boolean isTrace, boolean isCatchException,
                                 boolean isReturnLastData, Log aLog) throws Exception {

        Object result = null;
        RunEnvironment environment = OperateDataCacheManager.fetRunEnvironment(set, context, isTrace);
        try {
            CallResult tempResult = set.execute(environment, context, errorList, isReturnLastData, aLog);
            if (tempResult.isExit() == true) {
                result = tempResult.getReturnValue();
            }
        } catch (Exception e) {
            if (isCatchException == true) {
                if (aLog != null) {
                    aLog.error(e.getMessage(), e);
                } else {
                    log.error(e.getMessage(), e);
                }
            } else {
                throw e;
            }
        }
        return result;

    }
}
