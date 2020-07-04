package com.ql.util.express.parse;

import com.ql.util.express.instruction.opdata.ArraySwap;
import com.ql.util.express.context.InstructionSetContext;
import com.ql.util.express.instruction.opdata.OperateData;
import com.ql.util.express.instruction.op.OperatorBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianqiao on 16/10/16.
 */
public class AppendingClassMethodManager {

    public class AppendingMethod {
        public String name;

        public Class<?> bindingClass;

        public OperatorBase op;

        public AppendingMethod(String name, Class<?> bindingClass, OperatorBase op) {
            this.name = name;
            this.bindingClass = bindingClass;
            this.op = op;
        }
    }

    private List<AppendingMethod> methods = new ArrayList<AppendingMethod>();

    public void addAppendingMethod(String name, Class<?> bindingClass, OperatorBase op) {
        methods.add(new AppendingMethod(name, bindingClass, op));
    }

    public AppendingMethod getAppendingClassMethod(Object object, String methodName) {
        for (AppendingMethod method : methods) {
            //object是定义类型的子类
            if (methodName.equals(method.name) && (object.getClass() == method.bindingClass || method.bindingClass.isAssignableFrom(object.getClass()))) {
                return method;
            }
        }
        return null;

    }

    /**
     * 回调方式 类的方法
     * @param method
     * @param context
     * @param list
     * @param errorList
     * @return
     * @throws Exception
     */
    public OperateData invoke(AppendingMethod method, InstructionSetContext context, ArraySwap list, List<String> errorList) throws Exception {
        OperatorBase op = method.op;
        return op.execute(context, list, errorList);
    }


}
