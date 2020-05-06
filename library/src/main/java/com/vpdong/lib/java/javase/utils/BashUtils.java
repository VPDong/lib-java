package com.vpdong.lib.java.javase.utils;

import java.io.*;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class BashUtils {
	private BashUtils() throws Exception {
		throw new IllegalAccessException("can not to init instance for bash utils");
	}
	
	public static Runner execute(Command command) {
		return (new Runner()).execute(command);
	}
	
	public static class Runner {
		private volatile AtomicInteger deep = new AtomicInteger(0);
		private volatile Integer code = -1;
		private volatile boolean next = true;
		private volatile Command cmd;
		
		public int getDeep() {
			return deep.get();
		}
		
		public int getCode() {
			return code;
		}
		
		public Runner execute(Command command) {
			if (command == null) throw new Error("command can not be null");
			cmd = command;
			
			// 准备运行
			if (next) {
				// 准备参数
				final File work = cmd.getWork();
				final String[] envp = cmd.getEnvp();
				final String[] cmds = cmd.getCmds();
				if (cmds == null || cmds.length == 0) return this;
				
				code = execute(work, envp, cmds, cmd);
				next = cmd.getNext();
				deep.incrementAndGet();
			}
			
			return this;
		}
		
		private int execute(File work, String[] envp, String[] cmds, Command command) {
			if (cmds == null || cmds.length == 0) return -1;
			
			String cmdStr = "";
			Process process = null;
			BufferedWriter telWriter = null;// 终端输入流
			BufferedReader stdReader = null;// 标准输出流
			BufferedReader errReader = null;// 错误输出流
			try {
				// 命令配置
				StringBuilder cmdBuilder = new StringBuilder();
				for (String param : cmds) {
					cmdBuilder.append(param);
					cmdBuilder.append(" ");
				}
				cmdStr = cmdBuilder.toString();
				
				// 开始准备
				if (command != null) {
					try {
						command.onPrepare(cmdStr);
						if (!command.getNext()) return -1;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				// 开始执行:命令运行
				if (cmds.length == 1) {
					process = Runtime.getRuntime().exec(cmds[0], envp, work);
				} else {
					process = Runtime.getRuntime().exec(cmds, envp, work);
				}
				telWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
				stdReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				
				// 开始执行:输入输出
				String line;
				while ((line = stdReader.readLine()) != null) {
					if (command != null) {
						try {
							command.onOutput(cmdStr, 1, line, telWriter);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				while ((line = errReader.readLine()) != null) {
					if (command != null) {
						try {
							command.onOutput(cmdStr, 2, line, telWriter);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				// 结束执行
				int result = -1;
				try {
					// 阻塞等待结束
					result = process.waitFor();
				} catch (Exception ignored) {
					// ignored
				} finally {
					if (command != null) {
						try {
							command.onFinish(cmdStr, result, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				return result;
			} catch (Exception except) {
				if (command != null) {
					try {
						command.onFinish(cmdStr, -1, except);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return -1;
			} finally {
				try {
					if (telWriter != null) telWriter.close();
				} catch (Exception ignored) {
					// ignored
				}
				try {
					if (stdReader != null) stdReader.close();
				} catch (Exception ignored) {
					// ignored
				}
				try {
					if (errReader != null) errReader.close();
				} catch (Exception ignored) {
					// ignored
				}
				try {
					if (process != null) process.destroy();
				} catch (Exception ignored) {
					// ignored
				}
			}
		}
	}
	
	public static abstract class Command {
		private volatile boolean next = true;
		
		protected String[] buildCmds(String cmd, String pwd, String bash) {
			if (pwd == null) {
				return new String[]{cmd};
			} else {
				cmd = String.format(Locale.CHINA, "echo \"%s\" | sudo -S %s", pwd, cmd);
				bash = bash == null ? "/bin/sh" : bash;
				return new String[]{bash, "-c", cmd};
			}
		}
		
		protected String[] buildCmds(String cmd, String pwd) {
			return buildCmds(cmd, pwd, null);
		}
		
		protected String[] buildCmds(String cmd) {
			return buildCmds(cmd, null, null);
		}
		
		protected void setNext(boolean next) {
			this.next = next;
		}
		
		public boolean getNext() {
			return next;
		}
		
		public File getWork() {
			return null;
		}
		
		public String[] getEnvp() {
			return null;
		}
		
		public abstract String[] getCmds();
		
		public void onPrepare(String cmd) throws Exception {
			// todo
		}
		
		public void onOutput(String cmd, int type, String msg, BufferedWriter writer) throws Exception {
			// todo
		}
		
		public void onFinish(String cmd, int code, Exception except) throws Exception {
			// todo
		}
		
		protected void doDefaultOutput(int type, String msg) {
			if (msg == null) return;
			switch (type) {
				case 1:
					System.out.println(msg);
					break;
				case 2:
					System.err.println(msg);
					break;
			}
		}
	}
}
