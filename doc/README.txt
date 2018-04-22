项目简介
以前写过一个版本的mybatis单表操作的通用dao，实现需要修改mybatis的源代码。
但是修改mybatis源码容易引入风险。
本项目的目的是在不修改源码的情况下，实现通用dao。
实现方式是基于mybatis插件的方式,参考cn.howso.mybatis.plugin.ExecutorPlugin。
主要目的是将XXXMapper.xml中对于单表增删改查的配置去掉，使配置文件简化，模板化的代码尽可能不重复出现。
好处：
一致的mapper接口。
增强的mapper功能（整合了分页功能，并且提供了springmvc注入分页参数的两种方式-基于limit-offset和index-size方式）。
基本的单表操作不需要手写和自动生成的大量相似的sql，支持省略写ResultMap，特殊情况可省略XXXMapper.xml。
重构方便（例如添加或删除了字段、修改了字段类型、修改了表名）。
可维护性增强（例如找sql语句时，不会被大量模板sql干扰，方便看出自定义sql的数量，找出自定义sql的相同相似之处）。
缺点与局限：
通用的Example基于文本字符串，并不提供静态检查功能。
不支持单表的聚合操作，即即使是单表查询，如果包含groupby，则还是需要手写sql。

下面我们通过例子来了解它的使用方法：
假设数据库中有一张系统用户表sys_user。
1、创建sys_user的model。
可以通过mybatis-generator插件根据表自动生成，也可以手写，或者通过其他工具生成，不管怎么样，反正需要一个对应的model类。
当然也有人成为pojo、bean，不论它叫什么，它的字段和表字段对应，然后有对应的getters和setters就够了。
如
package cn.howso.deeplan.perm.model;
public class User{
	...字段
	...getters
	...setters
}

2、创建UserMapper.xml。
其中的内容如下
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="cn.howso.deeplan.perm.mapper.UserMapper" >
  <resultMap id="BaseResultMap" type="cn.howso.deeplan.perm.model.User" >
    <id column="id" property="id" jdbcType="INTEGER" />
    <result column="name" property="name" jdbcType="VARCHAR" />
    <result column="password" property="password" jdbcType="VARCHAR" />
    <result column="nick" property="nick" jdbcType="VARCHAR" />
    <result column="valid" property="valid" jdbcType="BIT" />
  </resultMap>
  <!--不需要写select、delete、update、insert -->
</mapper>
3、创建UserMapper.java，继承BaseMapper接口、添加Table注解。
其泛型参数分别为
接口操作的表对应的实体类、
使用单表操作方法时需要的样例类（支持mybatis-generator生成的XXXExample，也支持该项目提供的通用Example）、
主键的类型（如果没有主键，就随便给个类型，一般没有主键就用Object）。
Table注解，指明了该Mapper操作的表名。（为什么不加在实体类中？我们待会儿解释）
import cn.howso.deeplan.perm.model.User;
import cn.howso.mybatis.anno.Table;
import cn.howso.mybatis.mapper.BaseMapper;
import cn.howso.mybatis.model.Example;
@Table(name="sys_user")
public interface UserMapper extends BaseMapper<User,Example,Integer>{
	//对于基本的单表CURD，不需要写方法。
}
你可能在想，还不如使用maven的mybatis-generator插件。别急，先跟着把例子做完。
4、配置mybatis单表操作的插件。可以参考mybatis官网。
<plugins>
	<plugin interceptor="cn.howso.mybatis.plugin.ExecutorPlugin">
	</plugin>
	<!--<plugin interceptor="cn.howso.mybatis.plugin.PagePlugin">
		<property name="dialect" value="postgre" />
		<property name="pageSqlId" value=".*ByPage.*" />
	</plugin>-->
</plugins>
5、创建UserService，注入UserMapper。
6、UserService中调用UserMapper接口实现单表CURD功能。
比如删除用户（由于是逻辑删除，所以实际上是update）
public Integer delete(Integer id) {
    User user = new User();
    user.setId(id);
    user.setValid(false);
    Example example = new Example();
    example.createCriteria().and("id").equalTo(id);
    return userMapper.updateByExampleSelective(user, example );
}
至此，您已经掌握了该项目的基本用法。

接下来解答之前的疑问。

疑问1：为什么本项目的单表CURD方式比maven的mybatis-generator插件好？
因为本人在使用mybatis-generator插件的时候，发现重复代码非常多、
项目前期表结构改动（如修改字段名、字段类型、表名）时，需要重新生成model、XXXExample、XXXMapper.xml。
特别是XXXMapper.xml，其中单表CURD的配置和自己手写的sql混合在一起，rebuild时需要手工把手写的sql移植到新的自动生成
的XXXMapper.xml中，使用起来颇为麻烦。需要从一大堆的配置中找到手写的sql，对于阅读、维护不友好不方便。
而XXXExample，并不是所有model通用的，每一个model都对应一个XXXExample，大量的XXXExample增加了类的个数，
也就成倍的增加了model包的体积，对于追求代码简洁的人，犹如眼中沙（其实XXXExample的问题影响并不大，毕竟全部用自动生成的替换掉就行了，
不需要手工维护其中的内容）。
软件设计，通常会将变化的部分和不变的部分进行分离。个人认为，项目的目录结构，遵循maven的约定，它是相对不变的，可以通过工具生成。
而model、XXXExample、XXXMapper.java、XXXMapper.xml则与业务相关、是相对变化的部分，不太适合使用自动生成工具。
所以就寻求一种将单表基本的CURD和其他业务相关的配置、代码分离的方式。
本项目，就实现了这种方式。

疑问2：指定XXXMapper.java操作的表名，为什么不在model类上加注解而是在XXXMapper.java文件中？
我们通常说模块要高内聚低耦合。本人认为，模块又大小之分、层次之分。一个XXXMapper.xml也是一个模块。
XXXMapper.xml中写的sql，尽可能面向某一张表。不要在其中查询各种表。
需要关联查询的时候，我们要先确定驱动表（主表），根据驱动表将sql放到对应的XXXMapper.xml中。
这样当需要查找某个sql时，有一定的规则可循，比较方便，不容易耦合其他表。
注解加在XXXMapper.java中，表达了这种思想，提醒使用者一个Mapper有它的主要操作对象。
另一个原因，是考虑到使用者可能会用工具生成model。工具生成新model、覆盖就的model，不需要修改model中的内容，
对于使用者更友好。如果注解加在model中，则反之。


本项目还提供了更加高级的功能。
1、XXXMapper.xml中省略BaseResultMap。
需要在对应的XXXMapper类上加上AutoBaseResultMap注解、并且启用mybatis的下划线命名法自动映射转驼峰命名法。
在mybatis的配置文件中
<settings>
	<setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
如果你的需求只有基本的单表CURD，那么XXXMapper.xml的内容只剩下
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="cn.howso.deeplan.perm.mapper.UserMapper" >
</mapper>
然而基本的CURD功能却依然可以用。

2、分页功能。
分页插件是从网上找的，后来自己进行了一些改造。
要使用分页功能，需要在mybatis的配置文件中配置
<plugins>
	<plugin interceptor="cn.howso.mybatis.plugin.PagePlugin">
		<property name="dialect" value="postgre" />
		<property name="pageSqlId" value=".*ByPage.*" />
	</plugin>
</plugins>
分页功能是单独的，和用不用BaseMapper无关。
某个XXXMapper的方法需要使用分页功能，方法名必须正则匹配于配置的pageSqlId。
分页支持oracle、mysql以及postgresql数据库，需要配置对应的dialect。
项目中还封装了两种接收分页参数的model，IndexPage和limitPage。他们分别用于基于limit-offset和index-size的分页。
如果您的项目使用springmvc，则在controller的方法中加上一个分页参数的model。
例如
public PageRes<User> queryUsers(String q,LimitPage page){
	...
}
XXXMapper中推荐的实践是如下（当然单表的我们就不需要写接口了）
List<User> queryUsers(@Param("q")String q,@Param("page")Pageable page);
而不是
List<User> queryUsers(Map map);//map.put("q",q);map.put("page",page);
Pageable是LimitPage和IndexPage的抽象父类（面向抽象编程）。
在XXXService中使用
PageRes<User> res = PageRes.of(userMapper.selectByExampleByPage(example,page),page);
PageRes是封装分页结果的model。提供了两个静态方法方便使用：
public static <T> PageRes<T> of(List<T> rows,Pageable page);
public static <T> PageRes<T> of(List<T> rows,int size);

BaseMapper中的*ByPage方法需要分页插件的支持。

3、特殊情况省略XXXMapper.xml。
对于希望快速开发的人来说，用mybatis太烦了。有时候仅仅需要基本的CURD，却不得不创建XXXMapper.xml。
即使我们可以用之前介绍过的方法，不用写BaseResultMap，但是还是要创建xml文件。
这个支持，是基于和spring整合的。
需要修改application-mybatis.xml中的SqlSessionFactoryBean和MapperScannerConfigurer配置。
如下
<bean id="sqlSessionFactory" class="cn.howso.mybatis.builder.MySqlSessionFactoryBean">
	<property name="dataSource" ref="dataSrouce" />
	<property name="configLocation" value="classpath:sql-map-config.xml"/>
	<property name="mapperLocations">
		<list>
			<!-- 自动匹配Mapper映射文件 -->
			<value>classpath*:**/*Mapper.xml</value>
		</list>
	</property>
</bean>
<!-- 扫描 basePackage下所有的接口，根据对应的mapper.xml为其生成代理类 -->
<bean class="cn.howso.mybatis.builder.MyMapperScannerConfigurer">
	<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
	<property name="annotationClass" value="cn.howso.mybatis.anno.Table"></property>
	<property name="basePackage" value="cn.howso.**.mapper" />
</bean>
这样，在mybatis启动的时候，就会在XXXMapper.xml不存在的时候，自动使用默认的内容提供给mybatis。
当您有个性化的需求，需要自己写sql时，可以把XXXMapper.xml补上，而不需要修改配置和代码。

4、基于通用Example的动态查询条件构建。
对于单表操作，如果我们手写sql，一般不会去兼容各种各样的情况。
mybatis-generator生成的XXXExample很好的解决了我们的问题。
但是，各个model的Example都不一样。本项目提供了一种通用的Example，可以用于所有的model。
比如根据姓名模糊匹配查询用户，得到分页结果
XXXExample方式如下
UserExample example = new UserExample();
example.createCriteria().andNameLike("%"+q+"%");
PageRes<User> res = PageRes.of(userMapper.selectByExampleByPage(example,page),page);
通用Example方式如下
Example example = new Example();
example.createCriteria().and("name").like("%"+q+"%");
PageRes<User> res = PageRes.of(userMapper.selectByExampleByPage(example,page),page);
XXXMapper支持两种方式。
XXXExample需要mybatis-generator插件生成。
通用的Example则没有静态检查。
对于快速开发来说，用通用Example很适合。

实际应用，可以参考my-seed项目。