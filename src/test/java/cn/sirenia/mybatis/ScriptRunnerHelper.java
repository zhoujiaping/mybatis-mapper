package cn.sirenia.mybatis;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
/**
 * 可以用于运行一个sql文件，测试的时候方便初始化数据。
 * @author 01375156 
 */
public class ScriptRunnerHelper {
	private String username;
	private String password;
	private String driver;
	private String url;
	private Connection connection;
	private ScriptRunner runner;
	private String charset = "utf-8";
	public ScriptRunnerHelper configConnect(String url,String driver,String username,String password){
		this.url = url;
		this.driver = driver;
		this.username = username;
		this.password = password;
		try{
			connection = DriverManager.getConnection(url, username, password);
			runner = new ScriptRunner(connection);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return this;
	}
	public ScriptRunnerHelper withCharset(String charset){
		this.charset = charset;
		return this;
	}
	public ScriptRunnerHelper configRunner(FunctionIn<ScriptRunner> fun){
		fun.apply(runner);
		return this;
	}
	public ScriptRunnerHelper runFileScript(String uri){
		try{
			File file = ResourceUtils.getFile(uri);
			byte[] buf = FileCopyUtils.copyToByteArray(file);
			Reader reader = new StringReader(new String(buf,this.charset));
			runner.runScript(reader );
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	public ScriptRunnerHelper runScript(String text){
		try{
			Reader reader = new StringReader(text);
			runner.runScript(reader );
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	public void detroy(){
		try {
			runner=null;
			connection.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public String getUrl() {
		return url;
	}
	public String getUsername() {
		return username;
	}
	public String getCharset() {
		return charset;
	}
	public String getDriver() {
		return driver;
	}
	public String getPassword() {
		return password;
	}
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8";
		String driver = "com.mysql.jdbc.Driver";
		String username = "root";
		String password = "";
		
		String text = "show tables;111\nselect * from\n sys_user;\nselect * from sys_user;";
		ScriptRunnerHelper helper = new ScriptRunnerHelper()
				.configConnect(url, driver, username, password)
				.configRunner(runner->{
					runner.setSendFullScript(false);
				})
				.runScript(text );
		helper.detroy();
	}
}
