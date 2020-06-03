package com.ql.util.express.test.spring;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * 融合了spring上下文的 QLExpress上下文
 */

@SuppressWarnings("serial")
public class QLExpressApplicationContext extends HashMap<String, Object> implements ApplicationContextAware,
        IExpressContext<String, Object> {

	private static ExpressRunner runner;
	static {
		runner = new ExpressRunner();
	}
	private static boolean isInitialRunner = false;
	private ApplicationContext applicationContext;

	public QLExpressApplicationContext() {
		super();
	}

	public QLExpressApplicationContext(Map<String, Object> aProperties, ApplicationContext aContext) {
		super(aProperties);
		this.applicationContext = aContext;
	}

	/**
	 *
	 * @param statement
	 *            执行语句
	 * @param context
	 *            上下文
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object execute(String statement, Map<String, Object> context)
			throws Exception {
		initRunner(runner);
		IExpressContext expressContext = new QLExpressContext(context,applicationContext);
		return runner.execute(statement, expressContext, null, true, false);
	}

	private void initRunner(ExpressRunner runner) {
		if (isInitialRunner == true) {
			return;
		}
		synchronized (runner) {
			if (isInitialRunner == true) {
				return;
			}
			try {
				runner.addFunctionOfServiceMethod("读取用户信息",applicationContext.getBean("bizLogicBean"), "getUserInfo", new Class[] {String.class}, null);
				runner.addMacro("判定用户是否vip","userDO.salary>200000");

			} catch (Exception e) {
				throw new RuntimeException("初始化失败表达式", e);
			}
		}
		isInitialRunner = true;
	}

	/**
	 * 抽象方法：根据名称从属性列表中提取属性值
	 */
	public Object get(Object name) {
		Object result = null;
		result = super.get(name);
		try {
			if (result == null && applicationContext != null && applicationContext.containsBean((String) name)) {
				// 如果在Spring容器中包含bean，则返回String的Bean
				result = applicationContext.getBean((String) name);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public Object put(String name, Object object) {
		return super.put(name, object);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}
}
