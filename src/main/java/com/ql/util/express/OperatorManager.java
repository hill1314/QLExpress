package com.ql.util.express;

import com.ql.util.express.exception.QLCompileException;
import com.ql.util.express.exception.QLException;
import com.ql.util.express.instruction.op.*;
import com.ql.util.express.parse.ExpressNode;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;


/**
 * 指令工厂（包含框架自带的指令，以及自定义添加的方法）
 */
public class OperatorManager {

    /**
     * 是否需要高精度计算
     */
    protected boolean isPrecise = false;
    /**
     * 所有支持的操作集合
     */
    private Map<String, OperatorBase> operatorMap = new HashMap<String, OperatorBase>();

    public OperatorManager(boolean aIsPrecise) {
        this.isPrecise = aIsPrecise;
        addOperator("new", new OperatorNew("new"));
        addOperator("anonymousNewArray", new OperatorAnonymousNewArray("anonymousNewArray"));
        addOperator("NewList", new OperatorAnonymousNewList("NewList"));
        addOperator(":", new OperatorKeyValue(":"));
        addOperator("NewMap", new OperatorAnonymousNewMap("NewMap"));
        addOperator("def", new OperatorDef("def"));
        addOperator("exportDef", new OperatorExportDef("exportDef"));
        addOperator("!", new OperatorNot("!"));
        addOperator("*", new OperatorMultiDiv("*"));
        addOperator("/", new OperatorMultiDiv("/"));
        addOperator("%", new OperatorMultiDiv("%"));
        addOperator("mod", new OperatorMultiDiv("mod"));
        addOperator("+", new OperatorAdd("+"));
        addOperator("-", new OperatorReduce("-"));
        addOperator("<", new OperatorEqualsLessMore("<"));
        addOperator(">", new OperatorEqualsLessMore(">"));
        addOperator("<=", new OperatorEqualsLessMore("<="));
        addOperator(">=", new OperatorEqualsLessMore(">="));
        addOperator("==", new OperatorEqualsLessMore("=="));
        addOperator("!=", new OperatorEqualsLessMore("!="));
        addOperator("<>", new OperatorEqualsLessMore("<>"));

        addOperator("&&", new OperatorAnd("&&"));
        addOperator("||", new OperatorOr("||"));
        addOperator("nor", new OperatorNor("nor"));
        addOperator("=", new OperatorEvaluate("="));
        addOperator("exportAlias", new OperatorExportAlias("exportAlias"));
        addOperator("alias", new OperatorAlias("alias"));
//        addOperator("break", new OperatorBreak("break")); //还没有实现
//        addOperator("continue", new OperatorContinue("continue"));//还没有实现
//        addOperator("return", new OperatorReturn("return"));      //还没有实现
        addOperator("ARRAY_CALL", new OperatorArray("ARRAY_CALL"));
        addOperator("++", new OperatorDoubleAddReduce("++"));
        addOperator("--", new OperatorDoubleAddReduce("--"));
        addOperator("cast", new OperatorCast("cast"));
        addOperator("macro", new OperatorMacro("macro"));
//        addOperator("function", new OperatorFunction("function")); //还没有实现
        addOperator("in", new OperatorIn("in"));
        addOperator("like", new OperatorLike("like"));
        //bit operator
        addOperator("&", new OperatorBit("&"));
        addOperator("|", new OperatorBit("|"));
        addOperator("^", new OperatorBit("^"));
        addOperator("~", new OperatorBit("~"));
        addOperator("<<", new OperatorBit("<<"));
        addOperator(">>", new OperatorBit(">>"));
    }

    public void addOperator(String name, OperatorBase op) {
        OperatorBase oldOp = operatorMap.get(name);
        if (oldOp != null) {
            throw new RuntimeException("重复定义操作符：" + name + "定义1：" + oldOp.getClass() + " 定义2：" + op.getClass());
        }
        op.setPrecise(this.isPrecise);
        op.setAliasName(name);
        operatorMap.put(name, op);
    }

    public OperatorBase replaceOperator(String name, OperatorBase op) {
        OperatorBase old = this.operatorMap.remove(name);
        this.addOperator(name, op);
        return old;
    }

    @SuppressWarnings("unchecked")
    public void addOperatorWithAlias(String aAliasName, String name, String errorInfo) throws Exception {
        if (this.operatorMap.containsKey(name) == false) {
            throw new QLException(name + " 不是系统级别的操作符号，不能设置别名");
        } else {
            OperatorBase orgiOperator = this.operatorMap.get(name);
            if (orgiOperator == null) {
                throw new QLException(name + " 不能被设置别名");
            }
            OperatorBase destOperator = null;
            if (orgiOperator instanceof CanClone) {
                destOperator = ((CanClone) orgiOperator).cloneMe(aAliasName, errorInfo);
            } else {
                Class<OperatorBase> opClass = (Class<OperatorBase>) orgiOperator.getClass();
                Constructor<OperatorBase> constructor = null;
                try {
                    constructor = opClass.getConstructor(String.class, String.class, String.class);
                } catch (Exception e) {
                    throw new QLException(name + " 不能被设置别名:" + e.getMessage());
                }
                if (constructor == null) {
                    throw new QLException(name + " 不能被设置别名");
                }
                destOperator = constructor.newInstance(aAliasName, name, errorInfo);
            }
            if (this.operatorMap.containsKey(aAliasName)) {
                throw new RuntimeException("操作符号：\"" + aAliasName + "\" 已经存在");
            }
            this.addOperator(aAliasName, destOperator);
        }
    }

    public boolean isExistOperator(String operName) {
        return operatorMap.containsKey(operName);
    }

    public OperatorBase getOperator(String aOperName) {
        return this.operatorMap.get(aOperName);
    }

    /**
     * 创建一个新的操作符实例
     */
    public OperatorBase newInstance(ExpressNode opItem) throws Exception {
        OperatorBase op = operatorMap.get(opItem.getNodeType().getName());
        if (op == null) {
            op = operatorMap.get(opItem.getTreeType().getName());
        }
        if (op == null) {
            op = operatorMap.get(opItem.getValue());
        }
        if (op == null)
            throw new QLCompileException("没有为\"" + opItem.getValue() + "\"定义操作符处理对象");
        return op;
    }

    public OperatorBase newInstance(String opName) throws Exception {
        OperatorBase op = operatorMap.get(opName);
        if (op == null) {
            throw new QLCompileException("没有为\"" + opName + "\"定义操作符处理对象");
        }
        return op;
    }
}
