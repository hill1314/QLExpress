package com.ql.util.express.loader;

import com.ql.util.express.instruction.ExportItem;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;
import com.ql.util.express.exception.QLException;
import com.ql.util.express.instruction.FunctionInstructionSet;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 表达式装载器
 *
 * @author xuannan
 */
public class ExpressLoader {
    /**
     * 表达式指令集缓存
     */
    private ConcurrentHashMap<String, InstructionSet> expressInstructionSetCache = new ConcurrentHashMap<String, InstructionSet>();

    ExpressRunner creator;

    public ExpressLoader(ExpressRunner aCreator) {
        this.creator = aCreator;
    }

    /**
     * 使用资源加载器 加载规则文件 并 解析为指令
     *
     * @param expressName
     * @return
     * @throws Exception
     */
    public InstructionSet loadExpress(String expressName) throws Exception {
        return parseInstructionSet(expressName, this.creator.getExpressResourceLoader().loadExpress(expressName));
    }

    /**
     * 设置指令集 到本地缓存
     *
     * @param expressName
     * @param expressString
     * @return
     * @throws Exception
     */
    public InstructionSet parseInstructionSet(String expressName, String expressString) throws Exception {
        InstructionSet parseResult = null;
        if (expressInstructionSetCache.containsKey(expressName)) {
            throw new QLException("表达式定义重复：" + expressName);
        }

        synchronized (expressInstructionSetCache) {
            parseResult = this.creator.parseInstructionSet(expressString);
            parseResult.setName(expressName);
            parseResult.setGlobeName(expressName);

            // 需要将函数和宏定义都提取出来
            for (FunctionInstructionSet item : parseResult.getFunctionInstructionSets()) {
                addInstructionSet(item.name, item.instructionSet);
                item.instructionSet.setName(item.name);
                item.instructionSet.setGlobeName(expressName + "." + item.name);
            }
            if (parseResult.hasMain()) {
                addInstructionSet(expressName, parseResult);
            }
        }
        return parseResult;
    }

    /**
     * 添加指令
     *
     * @param expressName
     * @param set
     * @throws Exception
     */
    public void addInstructionSet(String expressName, InstructionSet set)
            throws Exception {
        synchronized (expressInstructionSetCache) {
            // 存在时抛异常，即 新的表达式不能覆盖老的
            if (expressInstructionSetCache.containsKey(expressName)) {
                throw new QLException("表达式定义重复：" + expressName);
            }
            expressInstructionSetCache.put(expressName, set);
        }
    }

    /**
     * 获取指令
     *
     * @param expressName
     * @return
     */
    public InstructionSet getInstructionSet(String expressName) {
        return expressInstructionSetCache.get(expressName);
    }

    /**
     * 清空
     */
    public void clear() {
        this.expressInstructionSetCache.clear();
    }

    /**
     * 获取 对外的变量声明 （还没实现）
     *
     * @return
     */
    public ExportItem[] getExportInfo() {
        Map<String, ExportItem> result = new TreeMap<String, ExportItem>();
        for (InstructionSet item : expressInstructionSetCache.values()) {
            for (ExportItem var : item.getExportDef()) {
                var.setGlobeName(item.getGlobeName() + "." + var.name);
                result.put(var.getGlobeName(), var);
            }
            result.put(item.getGlobeName(), new ExportItem(item.getGlobeName(), item.getName(), item.getType(), item.toString()));
        }
        return result.values().toArray(new ExportItem[0]);
    }
}
