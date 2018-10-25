package cn.sirenia.mybatis.plugin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 根据mybatis-generator自动生成的XXXExample修改而来。 由于每次修改表结构都需要重新生成一遍，十分不方便。
 *              但是不用example，又要对基本的增删查改每次实现一遍，或者在代码中手写sql，还需要适配java对象类型和jdbc字段类型。
 *              这里采取折中方案，任何表都用这一个example类。字段名用字符串，值用object类型，放弃一些静态特性。
 *              如此，既可以利用example自动对javatype到jdbctype的转换，又不用每次改完表结构之后重新生成一遍example。
 *              当然，如果要使用自动生成的example也是可以的，将接口参数类型修改一下，然后将service中对example使用的代码改一下就行了。
 * Example => Criteria or Criteria or Criteria orderByClause
 * Criteria => Criterion and Criterion and Criterion
 * Criterion => field >/</>=/<=/is null/is not null/in/not in value
 * @author zhoujiaping
 * @Date 2017年10月17日 下午5:56:24
 * @version 1.0.0
 */
public class Example {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public Example() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        public Criterion and(String field) {
            Criteria criteria = (Criteria) this;
            Criterion criterion = new Criterion(criteria, field);
            this.criteria.add(criterion);
            return criterion;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {

        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;
        private String field;
        private Criteria criteria;

        public Criteria isNull() {
            this.typeHandler = null;
            this.noValue = true;
            this.condition = field + " is null";
            return this.criteria;
        }

        public Criteria isNotNull() {
            this.typeHandler = null;
            this.noValue = true;
            this.condition = field + " is not null";
            return this.criteria;
        }

        public Criteria equalTo(Object value) {
            this.condition = field + "=";
            this.value = value;
            this.singleValue = true;
            return this.criteria;
        }

        public Criteria notEqualTo(Object value) {
            this.condition = field + "<>";
            this.value = value;
            this.singleValue = true;
            return this.criteria;
        }

        public Criteria greaterThan(Object value) {
            this.condition = field + ">";
            this.value = value;
            this.singleValue = true;
            return this.criteria;
        }

        public Criteria greaterThanOrEqualTo(Object value) {
            this.condition = field + ">=";
            this.value = value;
            this.singleValue = true;
            return this.criteria;
        }

        public Criteria lessThan(Object value) {
            this.condition = field + "<";
            this.value = value;
            this.singleValue = true;
            return this.criteria;
        }

        public Criteria lessThanOrEqualTo(Object value) {
            this.condition = field + "<";
            this.value = value;
            this.singleValue = true;
            return this.criteria;
        }

        public Criteria like(String value) {
            this.condition = field + " like";
            this.value = value;
            this.singleValue = true;
            return this.criteria;
        }
        public Criteria notLike(String value) {
            this.condition = field + " not like";
            this.value = value;
            this.singleValue = true;
            return this.criteria;
        }
        public Criteria in(List<?> values) {
            this.condition = field + " in ";
            this.value = values;
            this.listValue = true;
            return this.criteria;
        }

        public Criteria notIn(List<Object> values) {
            this.condition = field + " not in ";
            this.value = values;
            this.listValue = true;
            return this.criteria;
        }

        public Criteria between(Object value1, Object value2) {
            this.condition = field + " between";
            this.value = value1;
            this.secondValue = value2;
            this.betweenValue = true;
            return this.criteria;
        }

        public Criteria notBetween(Object value1, Object value2) {
            this.condition = field + " not between";
            this.value = value1;
            this.secondValue = value2;
            this.betweenValue = true;
            return this.criteria;
        }

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(Criteria criteria, String field) {
            super();
            this.criteria = criteria;
            this.field = field;
        }

    }
}
