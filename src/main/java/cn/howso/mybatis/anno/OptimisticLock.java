/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.howso.mybatis.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 用于支持乐观锁,约定乐观锁字段名使用XMLMapperConf.OPTI_FIELD_NAME,这样既简单又不容易冲突，可读性也比使用version字段好。
 * 乐观锁字段的初始值，需要在数据库中指定。
 * 乐观锁的使用方式，一种是开启事务后，先执行查询，判断版本号是否符合预期。符合则执行更新。
 * 另一种是在更新时，将版本号作为更新条件之一，根据更新的记录数判断是否更新成功。
 * 本项目只支持第一种方式，因为性能上也很接近，第二种只支持一个事物内的一次更新，如果有多次更新，中间会有多次查询。
 * 读多写少适合用乐观锁，读少写多适合用悲观锁。
 * @author:zhoujiaping
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface OptimisticLock {
	//String value() default "version";
}
