package com.ql.util.express.hill;

import com.ql.util.express.context.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.context.IExpressContext;
import org.junit.Test;

/**
 * TODO
 *
 * @author huleilei9
 * @date 2020/6/9.
 */
public class ReplaceMethodTest {


    @Test
    public void testNullParameter() throws Exception {

        String express = "function calculate(int a){\n" +
                "  return a+1;\n" +
                "};\n" +
                "println calculate2(2);" +
                "return calculate(2);";

        ExpressRunner runner = new ExpressRunner(false, false);

        //TODO 表达式中已有的function，不能被重复定义，但提示的错误 有问题
//        runner.addFunctionOfClassMethod("calculate2", ReplaceMethodTest.class.getName(), "calculate", new Class[]{Integer.class}, null);

        IExpressContext<String, Object> expressContext = new DefaultContext<String, Object>();
        Object r = runner.execute(express, expressContext, null, false, false);
        System.out.println("r=" + r);
    }

    public int calculate(Integer num) {
        return num * 2;
    }


}
