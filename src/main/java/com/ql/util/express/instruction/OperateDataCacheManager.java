package com.ql.util.express.instruction;

import com.ql.util.express.*;
import com.ql.util.express.context.IExpressContext;
import com.ql.util.express.context.InstructionSetContext;
import com.ql.util.express.instruction.opdata.*;
import com.ql.util.express.loader.ExpressLoader;

import java.util.Stack;

/**
 * 指令执行运行过程中用到的数据寄存器
 */
public class OperateDataCacheManager {

    /**
     * 指令执行运行过程中用到的数据寄存器 （用 ThreadLocal 来保证线程安全）
     */
    private static ThreadLocal<RunnerDataCache> m_OperateDataObjectCache = new ThreadLocal<RunnerDataCache>() {
        protected RunnerDataCache initialValue() {
            return new RunnerDataCache();
        }
    };

    public static void push(ExpressRunner aRunner) {
        m_OperateDataObjectCache.get().push(aRunner);
    }

    /**
     * 获取运行时数据
     * @return
     */
    public static IOperateDataCache getOperateDataCache() {
        return m_OperateDataObjectCache.get().cache;
    }

    /**
     * 获取运行时数据
     * @param obj
     * @param aType
     * @return
     */
    public static OperateData fetchOperateData(Object obj, Class<?> aType) {
        return getOperateDataCache().fetchOperateData(obj, aType);
    }

    public static OperateDataAttr fetchOperateDataAttr(String name, Class<?> aType) {
        return getOperateDataCache().fetchOperateDataAttr(name, aType);
    }

    public static OperateDataLocalVar fetchOperateDataLocalVar(String name, Class<?> aType) {
        return getOperateDataCache().fetchOperateDataLocalVar(name, aType);
    }

    public static OperateDataField fetchOperateDataField(Object aFieldObject, String aFieldName) {
        return getOperateDataCache().fetchOperateDataField(aFieldObject, aFieldName);
    }

    public static OperateDataArrayItem fetchOperateDataArrayItem(OperateData aArrayObject, int aIndex) {
        return getOperateDataCache().fetchOperateDataArrayItem(aArrayObject, aIndex);
    }

    public static OperateDataKeyValue fetchOperateDataKeyValue(OperateData aKey, OperateData aValue) {
        return getOperateDataCache().fetchOperateDataKeyValue(aKey, aValue);
    }

    /**
     * 获取运行时环境
     * @param aInstructionSet
     * @param aContext
     * @param aIsTrace
     * @return
     */
    public static RunEnvironment fetRunEnvironment(InstructionSet aInstructionSet, InstructionSetContext aContext, boolean aIsTrace) {
        return getOperateDataCache().fetRunEnvironment(aInstructionSet, aContext, aIsTrace);
    }

    /**
     * 获取执行结果
     * @param aReturnValue
     * @param aIsExit
     * @return
     */
    public static CallResult fetchCallResult(Object aReturnValue, boolean aIsExit) {
        return getOperateDataCache().fetchCallResult(aReturnValue, aIsExit);
    }

    /**
     * 获取指令集上下文
     * @param aIsExpandToParent
     * @param aRunner
     * @param aParent
     * @param aExpressLoader
     * @param aIsSupportDynamicFieldName
     * @return
     */
    public static InstructionSetContext fetchInstructionSetContext(boolean aIsExpandToParent, ExpressRunner aRunner, IExpressContext<String, Object> aParent, ExpressLoader aExpressLoader, boolean aIsSupportDynamicFieldName) {
        return getOperateDataCache().fetchInstructionSetContext(aIsExpandToParent, aRunner, aParent, aExpressLoader, aIsSupportDynamicFieldName);
    }

    public static long getFetchCount() {
        return getOperateDataCache().getFetchCount();
    }

    /**
     * 清除本地缓存
     * @param aRunner
     */
    public static void resetCache(ExpressRunner aRunner) {
        //清除所有运行时数据
        getOperateDataCache().resetCache();
        //弹出执行器
        m_OperateDataObjectCache.get().pop(aRunner);
    }

}

/**
 * 运行时数据缓存（包含数据 和 执行堆栈）
 */
class RunnerDataCache {
    IOperateDataCache cache;

    Stack<ExpressRunner> stack = new Stack<ExpressRunner>();

    public void push(ExpressRunner aRunner) {
        this.cache = aRunner.getOperateDataCache();
        this.stack.push(aRunner);
    }

    public IOperateDataCache getOperateDataCache() {
        return this.cache;
    }


    public void pop(ExpressRunner aRunner) {
//	    原有的逻辑
//		this.cache = this.stack.pop().getOperateDataCache();

        //bugfix处理ExpressRunner嵌套情况下，cache还原的问题
        this.stack.pop();
        if (!this.stack.isEmpty()) {
            this.cache = stack.peek().getOperateDataCache();
        } else {
            this.cache = null;
        }
    }

}
