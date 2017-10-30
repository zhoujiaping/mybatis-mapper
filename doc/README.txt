项目简介
之前的mybatis通用dao实现需要修改mybatis的源代码。
修改源码容易引入风险。
本项目的目的是在不修改源码的情况下，实现通用dao。
当然有另一种方式实现-基于mybatis插件的方式,参考ExecutorPlugin。
但是这里给出基于生成mapper.xml的方式。因为基于插件的方式，不支持在sql语句中写include和selectKey。
基于maven的mybatis-generator插件自动生成的model，example。
使用方法：
1、mybatis-generator自动生成model和example，mapper.xml，mapper.java。
2、删除mapper.xml中除BaseResultMap之外的部分。删除mapper.java中自动生成的接口。
让mapper.java继承BaseMapper.java。
3、给mapper.java加上Table注解。
4、在classpath下新建mapper.properties,内容参考src/test/resources下的mapper.properties。
5、在项目中新建一个main方法，执行MapperMaker的make方法。参考src/test/java中的MapperMakerTest.java
6、重复执行make方法，不会覆盖已有文件。如果需要重新生成，必须先手动删除旧文件。
tip：make方法利用了java8的并行流，执行速度取决于未处理的mapper.xml的数量，以及硬件配置。
baseMapper解决的问题主要是：一致的mapper接口，增强的mapper功能，
重构方便（例如添加或删除了字段），可维护性增强（例如找sql语句时，不会被大量模板sql干扰，方便看出自定义sql的数量，找出自定义sql的相同相似之处）。
可以把该项目做成maven插件。


一对一，多对一关联查询最佳实践
之前一直使用ResultMap方式配置，但是这样很麻烦，现在已经找到了一种方便的方式。
使用resultType而不是resultMap，启用驼峰转换。
java类中的嵌套对象，在sql中对应 字段名.字段名的方式（字段别名，需要用双引号包含起来，mybatis会自动处理嵌套对象）。
是不是很叼？
但是，对于一对多，多对多查询，还是要配置ResultMap。mybatis还不支持 字段名.字段名 的方式。