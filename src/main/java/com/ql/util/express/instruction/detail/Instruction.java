package com.ql.util.express.instruction.detail;

import com.ql.util.express.RunEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * 指令基类 （具体的指令继承该类）
 */
public abstract class Instruction implements java.io.Serializable {

    private static final long serialVersionUID = 1361458333068300443L;
    protected static transient Log staticLog = LogFactory.getLog(Instruction.class);
    protected static transient Log log = staticLog;
    private Integer line = 0;

    public Instruction setLine(Integer line) {
        this.line = line;
        return this;
    }

    public Integer getLine() {
        return line;
    }

    public void setLog(Log aLog) {
        if (aLog != null) {
            this.log = aLog;
        }
    }

    public String getExceptionPrefix() {
        return "run QlExpress Exception at line " + line + " :";
    }

    /**
     * 执行指令
     * @param environment
     * @param errorList
     * @throws Exception
     */
    public abstract void execute(RunEnvironment environment, List<String> errorList) throws Exception;
}
