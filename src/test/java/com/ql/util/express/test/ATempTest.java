package com.ql.util.express.test;

import org.junit.Test;

import com.ql.util.express.context.DefaultContext;
import com.ql.util.express.ExpressRunner;

public class ATempTest {
	@Test
	public void test2Java() throws Exception {
		//String express = "2 in (1,2,3)";
		//String express = "include Test; max(1,2,3)";
		String express = "when 1==2 then println(100000)";
		ExpressRunner runner = new ExpressRunner(false,true);
		DefaultContext<String, Object> context = new DefaultContext<String, Object>();
		Object r =   runner.execute(express, context, null, false,false);
		System.out.println(r);
	}

}
