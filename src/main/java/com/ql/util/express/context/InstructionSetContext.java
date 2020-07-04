package com.ql.util.express.context;

import com.ql.util.express.loader.ExpressLoader;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.exception.QLException;
import com.ql.util.express.instruction.OperateDataCacheManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令集运行时的数据上下文
 */
public class InstructionSetContext implements IExpressContext<String, Object> {
    /**
     * 没有指定数据类型的变量定义 是否传递到最外层的Context  （是否集成 parent 中的变量）
     */
    private boolean isExpandToParent = true;

    /**
     * 父 上下文
     */
    private IExpressContext<String, Object> parent = null;

    /**
     * 存储变量参数
     */
    private Map<String, Object> content;
    /**
     * 符号表（别名？）
     */
    private Map<String, Object> symbolTable = new HashMap<String, Object>();

    private ExpressLoader expressLoader;

    /**
     * 是否支持动态字段
     */
    private boolean isSupportDynamicFieldName = false;

    public ExpressRunner getRunner() {
        return runner;
    }

    private ExpressRunner runner;

    public InstructionSetContext(boolean aIsExpandToParent, ExpressRunner aRunner, IExpressContext<String, Object> aParent, ExpressLoader aExpressLoader, boolean aIsSupportDynamicFieldName) {
        this.initial(aIsExpandToParent, aRunner, aParent, aExpressLoader, aIsSupportDynamicFieldName);
    }

    public void initial(boolean aIsExpandToParent, ExpressRunner aRunner, IExpressContext<String, Object> aParent, ExpressLoader aExpressLoader, boolean aIsSupportDynamicFieldName) {
        this.isExpandToParent = aIsExpandToParent;
        this.runner = aRunner;
        this.parent = aParent;
        this.expressLoader = aExpressLoader;
        this.isSupportDynamicFieldName = aIsSupportDynamicFieldName;
    }

    public void clear() {
        isExpandToParent = true;
        parent = null;
        content = null;
        expressLoader = null;
        isSupportDynamicFieldName = false;
        runner = null;
        symbolTable.clear();

    }

    /**
     * 添加 符号
     * @param varName
     * @param aliasNameObject
     * @throws Exception
     */
    public void exportSymbol(String varName, Object aliasNameObject) throws Exception {
        if (this.parent != null && this.parent instanceof InstructionSetContext) {
            ((InstructionSetContext) this.parent).exportSymbol(varName, aliasNameObject);
        } else {
            this.addSymbol(varName, aliasNameObject);
        }
    }

    public void addSymbol(String varName, Object aliasNameObject) throws Exception {
        if (this.symbolTable.containsKey(varName)) {
            throw new QLException("变量" + varName + "已经存在，不能重复定义，也不能再从函数内部 export ");
        }
        this.symbolTable.put(varName, aliasNameObject);
    }

    public void addSymbol(Map<String, Object> aliasNameObjects) throws Exception {
        this.symbolTable.putAll(aliasNameObjects);
    }

    public void setSupportDynamicFieldName(boolean isSupportDynamicFieldName) {
        this.isSupportDynamicFieldName = isSupportDynamicFieldName;
    }

    public boolean isSupportDynamicFieldName() {
        return this.isSupportDynamicFieldName;
    }

    public ExpressRunner getExpressRunner() {
        return this.runner;
    }

    /**
     * 找出符号
     * @param varName
     * @return
     * @throws Exception
     */
    public Object findAliasOrDefSymbol(String varName) throws Exception {
        Object result = this.symbolTable.get(varName);
        if (result == null) {
            if (this.parent != null && this.parent instanceof InstructionSetContext) {
                result = ((InstructionSetContext) this.parent).findAliasOrDefSymbol(varName);
            } else {
                result = null;
            }
        }
        return result;
    }

    /**
     * 获取符号 （本地没有，会通过CacheManager创建，并设置到本地）
     * @param varName
     * @return
     * @throws Exception
     */
    public Object getSymbol(String varName) throws Exception {
        Object result = this.symbolTable.get(varName);
        if (result == null && this.expressLoader != null) {
            result = this.expressLoader.getInstructionSet(varName);
        }
        if (result == null) {
            if (isExpandToParent && this.parent != null && this.parent instanceof InstructionSetContext) {
                result = ((InstructionSetContext) this.parent).getSymbol(varName);
            } else {
                result = OperateDataCacheManager.fetchOperateDataAttr(varName, null);
                this.addSymbol(varName, result);
            }
        }
        return result;
    }

    public ExpressLoader getExpressLoader() {
        return expressLoader;
    }

    public IExpressContext<String, Object> getParent() {
        return this.parent;
    }

    /**
     * 获取变量
     * @param key 属性名称
     * @return
     */
    public Object get(Object key) {
        if (this.content != null && this.content.containsKey(key)) {
            return this.content.get(key);
        } else if (isExpandToParent && this.parent != null) {
            return this.parent.get(key);
        }
        return null;
    }

    /**
     * 设置变量
     * @param key
     * @param value
     * @return
     */
    public Object put(String key, Object value) {
        if (this.content != null && this.content.containsKey(key)) {
            return this.content.put(key, value);
        } else if (this.isExpandToParent == false) {
            if (this.content == null) {
                this.content = new HashMap<String, Object>();
            }
            return this.content.put(key, value);
        } else if (this.parent != null) {
            return this.parent.put(key, value);
        } else {
            throw new RuntimeException("没有定义局部变量：" + key + ",而且没有全局上下文");
        }
    }

}
