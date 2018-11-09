package cn.sirenia.mybatis;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Test;

public class ScriptRunnerTest {
	/**
	 * ScriptRunner可以用于测试时初始化系统数据
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception{
		String username = "root";
		String password = "";
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8";
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(url, username, password);
		ScriptRunner sr = new ScriptRunner(connection);
		sr.setSendFullScript(false);//整个文件是否为一个sql语句。
		//当前行，分号之后的内容将被忽略
		StringReader reader = new StringReader("show tables;111\nselect * from\n sys_user;\nselect * from sys_user;");
		sr.runScript(reader);
		Period p = Period.between(LocalDate.now(), LocalDate.now().plusDays(234));
		System.out.println(p.getDays());
		System.out.println(p.getMonths());
		System.out.println(p.get(ChronoUnit.DAYS));
	}
}
