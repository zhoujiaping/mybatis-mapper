# mybatis-mapper
## 项目背景：
本人一开始是用hibernate做开发的。换了公司之后，公司内部用mybatis，于是开始使用mybatis。在使用mybatis的过程中，
发现一些通用的操作，比如单表的CURD，在每一个xml文件中都需要定义一遍。而且每个人写的风格又不统一。于是寻找了一个解决
办法，使用maven插件mybatis-generator。使用一段时间之后，发现还是不能令人满意。每次修改表结构，都要重新生成代码。
还要把自定义的代码和自动生成的代码手工合并到一起。并且自动生成的代码体积庞大，结构相同。开始怀念hibernate，but，问题
还是需要解决的。于是阅读mybatis的源码，设计一个能够自动生成通用sql、又能够将通用sql和手写的sql分离的解决方案。经历过好
几个版本的迭代，当前版本已经在生产环境可用了。(sirenia是一个金属乐队，是海妖的意思)
## 该项目主要提供：

1. mybatis分页查询插件以及分页参数和结果的封装
    ```
	public PageRes<User> queryUserByPage(User condition,Pageable page) {
        //...
        List<User> rows = UserMapper.queryUserByPage(condition ,page);
        return PageRes.of(rows,page);
    }
	```
    Pageable有两个子类LimitPage和IndexPage，分别用于limit-offset方式的分页和index-size方式的分页。

2. mybatis单表CURD通用dao
    * int countByExample(EXAMPLE example);//example是查询条件的封装，支持mybatis-generator的Example以及该项目中自定义的Example。
    * int countByExample(EXAMPLE example);
    * int deleteByExample(EXAMPLE example);
    * int insert(ENTITY record);
    * int insertSelective(ENTITY record);
    * List<ENTITY> selectByExample(EXAMPLE example);
    * ENTITY selectUniqueByExample(EXAMPLE example);
    * int updateByExampleSelective(@Param("record") ENTITY record, @Param("example") EXAMPLE example);
    * int updateByExample(@Param("record") ENTITY record, @Param("example") EXAMPLE example);
    * List<ENTITY> selectByExampleByPage(@Param("example") EXAMPLE example, @Param("page") Pageable page);
    * int batchInsert(@Param("recordList") List<ENTITY> recordList);
    * int batchInsertSelective(@Param("recordList") List<ENTITY> recordList);
    * int deleteByPrimaryKey(@Param("id") PK id);// @Param("id")不要删除，约定名称为传参名为id，这样简单方便。
    * int deleteByPrimaryKeyAndOptiVersion(@Param("id") PK id,@Param("optiVersion")long optiVersion);// @Param("id")不要删除，约定名称为传参名为id，这样简单方便。
    
    * ENTITY selectByPrimaryKey(@Param("id") PK id);// @Param("id")不要删除，约定名称为传参名为id，这样简单方便。
    * int updateByPrimaryKeySelective(ENTITY record);
    * int updateByPrimaryKeyAndOptiVersionSelective(ENTITY record);
    * int updateByPrimaryKey(ENTITY record);
	  * int updateByPrimaryKeyAndOptiVersion(ENTITY record);
    * PK selectKey();
	
3. 通用dao支持两种查询条件的封装方式；

    1. 支持mybatis-generator的Example以及该项目中自定义的Example。
    mybatis-generator的XXXExample，每个实体类都需要一个XXXExample，每次修改表结构都需要重新生成XXXExample，
    缺点是重构麻烦，优点是享受编译器静态语法检查，适合需要稳步前进的大项目。
	
    2. 项目中定义的通用Example，代码简单。比如构建一个查询条件
	```
    Example example = new Example();
    example.createCriteria().and("scene_id").equalTo(sceneId)
    .and("datetime").equalTo(datetime);
    return sceneEciCountMapper.selectByExample(example );
	```
    缺点是基于字符串，不能享受静态检查，优点是重构时不需要重新生成代码，适合快速开发小项目。
    
    3. 两种Example方式可以很容易的相互转换。因为通用Example就是基于mybatis-generator插件生成的XXXExample设计的。

4. 对乐观锁的支持；
   
    支持乐观锁，不过有些要求，乐观锁列名必须为opti_version，对应的java类型必须为Integer或者Long。
    乐观锁的使用方式，仅支持  开启事务->查询->比较版本->更新 方式，不支持 开启事务->更新（已版本作为更新条件之一）。
    需要数据库设置乐观锁字段的默认值。
    如果需要对某个表使用乐观锁功能，在对应的Mapper.java上加上@OptimistionLock注解。

## 项目局限
仅仅解决了单表的CURD问题，没有对表关联的问题提出解决方案。并且对于单表的CURD，也不是完全支持，比如不支持SUM等聚合操作；
项目中有些强制约定，比如主键名字必须是id；

## 使用方式
* 参考<https://github.com/zhoujiaping/mybatis-mapper/blob/master/doc/README.txt>
* 参考<https://github.com/zhoujiaping/my-seed>
