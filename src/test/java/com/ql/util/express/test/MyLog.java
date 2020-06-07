package com.ql.util.express.test;

import org.apache.commons.logging.Log;

public  class MyLog implements Log{
		String name ;
		public MyLog(String aName){
			this.name = aName;
		}
		public boolean isDebugEnabled() {

			return false;
		}

		public boolean isErrorEnabled() {

			return false;
		}

		public boolean isFatalEnabled() {

			return false;
		}

		public boolean isInfoEnabled() {

			return false;
		}

		public boolean isTraceEnabled() {

			return false;
		}

		public boolean isWarnEnabled() {

			return false;
		}

		public void trace(Object message) {


		}

		public void trace(Object message, Throwable t) {


		}

		public void debug(Object message) {


		}

		public void debug(Object message, Throwable t) {


		}

		public void info(Object message) {


		}

		public void info(Object message, Throwable t) {


		}

		public void warn(Object message) {


		}

		public void warn(Object message, Throwable t) {


		}

		public void error(Object message) {


		}

		public void error(Object message, Throwable t) {


		}

		public void fatal(Object message) {


		}

		public void fatal(Object message, Throwable t) {


		}
	}
