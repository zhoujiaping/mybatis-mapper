package cn.zhou.mapper.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EciLocationExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public EciLocationExample() {
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

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andEciIsNull() {
            addCriterion("eci is null");
            return (Criteria) this;
        }

        public Criteria andEciIsNotNull() {
            addCriterion("eci is not null");
            return (Criteria) this;
        }

        public Criteria andEciEqualTo(Long value) {
            addCriterion("eci =", value, "eci");
            return (Criteria) this;
        }

        public Criteria andEciNotEqualTo(Long value) {
            addCriterion("eci <>", value, "eci");
            return (Criteria) this;
        }

        public Criteria andEciGreaterThan(Long value) {
            addCriterion("eci >", value, "eci");
            return (Criteria) this;
        }

        public Criteria andEciGreaterThanOrEqualTo(Long value) {
            addCriterion("eci >=", value, "eci");
            return (Criteria) this;
        }

        public Criteria andEciLessThan(Long value) {
            addCriterion("eci <", value, "eci");
            return (Criteria) this;
        }

        public Criteria andEciLessThanOrEqualTo(Long value) {
            addCriterion("eci <=", value, "eci");
            return (Criteria) this;
        }

        public Criteria andEciIn(List<Long> values) {
            addCriterion("eci in", values, "eci");
            return (Criteria) this;
        }

        public Criteria andEciNotIn(List<Long> values) {
            addCriterion("eci not in", values, "eci");
            return (Criteria) this;
        }

        public Criteria andEciBetween(Long value1, Long value2) {
            addCriterion("eci between", value1, value2, "eci");
            return (Criteria) this;
        }

        public Criteria andEciNotBetween(Long value1, Long value2) {
            addCriterion("eci not between", value1, value2, "eci");
            return (Criteria) this;
        }

        public Criteria andEciNameIsNull() {
            addCriterion("eci_name is null");
            return (Criteria) this;
        }

        public Criteria andEciNameIsNotNull() {
            addCriterion("eci_name is not null");
            return (Criteria) this;
        }

        public Criteria andEciNameEqualTo(String value) {
            addCriterion("eci_name =", value, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameNotEqualTo(String value) {
            addCriterion("eci_name <>", value, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameGreaterThan(String value) {
            addCriterion("eci_name >", value, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameGreaterThanOrEqualTo(String value) {
            addCriterion("eci_name >=", value, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameLessThan(String value) {
            addCriterion("eci_name <", value, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameLessThanOrEqualTo(String value) {
            addCriterion("eci_name <=", value, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameLike(String value) {
            addCriterion("eci_name like", value, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameNotLike(String value) {
            addCriterion("eci_name not like", value, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameIn(List<String> values) {
            addCriterion("eci_name in", values, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameNotIn(List<String> values) {
            addCriterion("eci_name not in", values, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameBetween(String value1, String value2) {
            addCriterion("eci_name between", value1, value2, "eciName");
            return (Criteria) this;
        }

        public Criteria andEciNameNotBetween(String value1, String value2) {
            addCriterion("eci_name not between", value1, value2, "eciName");
            return (Criteria) this;
        }

        public Criteria andCellNameIsNull() {
            addCriterion("cell_name is null");
            return (Criteria) this;
        }

        public Criteria andCellNameIsNotNull() {
            addCriterion("cell_name is not null");
            return (Criteria) this;
        }

        public Criteria andCellNameEqualTo(String value) {
            addCriterion("cell_name =", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameNotEqualTo(String value) {
            addCriterion("cell_name <>", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameGreaterThan(String value) {
            addCriterion("cell_name >", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameGreaterThanOrEqualTo(String value) {
            addCriterion("cell_name >=", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameLessThan(String value) {
            addCriterion("cell_name <", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameLessThanOrEqualTo(String value) {
            addCriterion("cell_name <=", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameLike(String value) {
            addCriterion("cell_name like", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameNotLike(String value) {
            addCriterion("cell_name not like", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameIn(List<String> values) {
            addCriterion("cell_name in", values, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameNotIn(List<String> values) {
            addCriterion("cell_name not in", values, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameBetween(String value1, String value2) {
            addCriterion("cell_name between", value1, value2, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameNotBetween(String value1, String value2) {
            addCriterion("cell_name not between", value1, value2, "cellName");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNull() {
            addCriterion("longitude is null");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNotNull() {
            addCriterion("longitude is not null");
            return (Criteria) this;
        }

        public Criteria andLongitudeEqualTo(BigDecimal value) {
            addCriterion("longitude =", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotEqualTo(BigDecimal value) {
            addCriterion("longitude <>", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThan(BigDecimal value) {
            addCriterion("longitude >", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("longitude >=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThan(BigDecimal value) {
            addCriterion("longitude <", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("longitude <=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIn(List<BigDecimal> values) {
            addCriterion("longitude in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotIn(List<BigDecimal> values) {
            addCriterion("longitude not in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("longitude between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("longitude not between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNull() {
            addCriterion("latitude is null");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNotNull() {
            addCriterion("latitude is not null");
            return (Criteria) this;
        }

        public Criteria andLatitudeEqualTo(BigDecimal value) {
            addCriterion("latitude =", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotEqualTo(BigDecimal value) {
            addCriterion("latitude <>", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThan(BigDecimal value) {
            addCriterion("latitude >", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("latitude >=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThan(BigDecimal value) {
            addCriterion("latitude <", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("latitude <=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIn(List<BigDecimal> values) {
            addCriterion("latitude in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotIn(List<BigDecimal> values) {
            addCriterion("latitude not in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("latitude between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("latitude not between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andCoordXIsNull() {
            addCriterion("coord_x is null");
            return (Criteria) this;
        }

        public Criteria andCoordXIsNotNull() {
            addCriterion("coord_x is not null");
            return (Criteria) this;
        }

        public Criteria andCoordXEqualTo(Integer value) {
            addCriterion("coord_x =", value, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordXNotEqualTo(Integer value) {
            addCriterion("coord_x <>", value, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordXGreaterThan(Integer value) {
            addCriterion("coord_x >", value, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordXGreaterThanOrEqualTo(Integer value) {
            addCriterion("coord_x >=", value, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordXLessThan(Integer value) {
            addCriterion("coord_x <", value, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordXLessThanOrEqualTo(Integer value) {
            addCriterion("coord_x <=", value, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordXIn(List<Integer> values) {
            addCriterion("coord_x in", values, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordXNotIn(List<Integer> values) {
            addCriterion("coord_x not in", values, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordXBetween(Integer value1, Integer value2) {
            addCriterion("coord_x between", value1, value2, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordXNotBetween(Integer value1, Integer value2) {
            addCriterion("coord_x not between", value1, value2, "coordX");
            return (Criteria) this;
        }

        public Criteria andCoordYIsNull() {
            addCriterion("coord_y is null");
            return (Criteria) this;
        }

        public Criteria andCoordYIsNotNull() {
            addCriterion("coord_y is not null");
            return (Criteria) this;
        }

        public Criteria andCoordYEqualTo(Integer value) {
            addCriterion("coord_y =", value, "coordY");
            return (Criteria) this;
        }

        public Criteria andCoordYNotEqualTo(Integer value) {
            addCriterion("coord_y <>", value, "coordY");
            return (Criteria) this;
        }

        public Criteria andCoordYGreaterThan(Integer value) {
            addCriterion("coord_y >", value, "coordY");
            return (Criteria) this;
        }

        public Criteria andCoordYGreaterThanOrEqualTo(Integer value) {
            addCriterion("coord_y >=", value, "coordY");
            return (Criteria) this;
        }

        public Criteria andCoordYLessThan(Integer value) {
            addCriterion("coord_y <", value, "coordY");
            return (Criteria) this;
        }

        public Criteria andCoordYLessThanOrEqualTo(Integer value) {
            addCriterion("coord_y <=", value, "coordY");
            return (Criteria) this;
        }

        public Criteria andCoordYIn(List<Integer> values) {
            addCriterion("coord_y in", values, "coordY");
            return (Criteria) this;
        }

        public Criteria andCoordYNotIn(List<Integer> values) {
            addCriterion("coord_y not in", values, "coordY");
            return (Criteria) this;
        }

        public Criteria andCoordYBetween(Integer value1, Integer value2) {
            addCriterion("coord_y between", value1, value2, "coordY");
            return (Criteria) this;
        }

        public Criteria andCoordYNotBetween(Integer value1, Integer value2) {
            addCriterion("coord_y not between", value1, value2, "coordY");
            return (Criteria) this;
        }

        public Criteria andAzimuthIsNull() {
            addCriterion("azimuth is null");
            return (Criteria) this;
        }

        public Criteria andAzimuthIsNotNull() {
            addCriterion("azimuth is not null");
            return (Criteria) this;
        }

        public Criteria andAzimuthEqualTo(String value) {
            addCriterion("azimuth =", value, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthNotEqualTo(String value) {
            addCriterion("azimuth <>", value, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthGreaterThan(String value) {
            addCriterion("azimuth >", value, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthGreaterThanOrEqualTo(String value) {
            addCriterion("azimuth >=", value, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthLessThan(String value) {
            addCriterion("azimuth <", value, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthLessThanOrEqualTo(String value) {
            addCriterion("azimuth <=", value, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthLike(String value) {
            addCriterion("azimuth like", value, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthNotLike(String value) {
            addCriterion("azimuth not like", value, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthIn(List<String> values) {
            addCriterion("azimuth in", values, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthNotIn(List<String> values) {
            addCriterion("azimuth not in", values, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthBetween(String value1, String value2) {
            addCriterion("azimuth between", value1, value2, "azimuth");
            return (Criteria) this;
        }

        public Criteria andAzimuthNotBetween(String value1, String value2) {
            addCriterion("azimuth not between", value1, value2, "azimuth");
            return (Criteria) this;
        }

        public Criteria andLacNameIsNull() {
            addCriterion("lac_name is null");
            return (Criteria) this;
        }

        public Criteria andLacNameIsNotNull() {
            addCriterion("lac_name is not null");
            return (Criteria) this;
        }

        public Criteria andLacNameEqualTo(String value) {
            addCriterion("lac_name =", value, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameNotEqualTo(String value) {
            addCriterion("lac_name <>", value, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameGreaterThan(String value) {
            addCriterion("lac_name >", value, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameGreaterThanOrEqualTo(String value) {
            addCriterion("lac_name >=", value, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameLessThan(String value) {
            addCriterion("lac_name <", value, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameLessThanOrEqualTo(String value) {
            addCriterion("lac_name <=", value, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameLike(String value) {
            addCriterion("lac_name like", value, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameNotLike(String value) {
            addCriterion("lac_name not like", value, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameIn(List<String> values) {
            addCriterion("lac_name in", values, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameNotIn(List<String> values) {
            addCriterion("lac_name not in", values, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameBetween(String value1, String value2) {
            addCriterion("lac_name between", value1, value2, "lacName");
            return (Criteria) this;
        }

        public Criteria andLacNameNotBetween(String value1, String value2) {
            addCriterion("lac_name not between", value1, value2, "lacName");
            return (Criteria) this;
        }

        public Criteria andCoverareaIsNull() {
            addCriterion("coverarea is null");
            return (Criteria) this;
        }

        public Criteria andCoverareaIsNotNull() {
            addCriterion("coverarea is not null");
            return (Criteria) this;
        }

        public Criteria andCoverareaEqualTo(String value) {
            addCriterion("coverarea =", value, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaNotEqualTo(String value) {
            addCriterion("coverarea <>", value, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaGreaterThan(String value) {
            addCriterion("coverarea >", value, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaGreaterThanOrEqualTo(String value) {
            addCriterion("coverarea >=", value, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaLessThan(String value) {
            addCriterion("coverarea <", value, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaLessThanOrEqualTo(String value) {
            addCriterion("coverarea <=", value, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaLike(String value) {
            addCriterion("coverarea like", value, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaNotLike(String value) {
            addCriterion("coverarea not like", value, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaIn(List<String> values) {
            addCriterion("coverarea in", values, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaNotIn(List<String> values) {
            addCriterion("coverarea not in", values, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaBetween(String value1, String value2) {
            addCriterion("coverarea between", value1, value2, "coverarea");
            return (Criteria) this;
        }

        public Criteria andCoverareaNotBetween(String value1, String value2) {
            addCriterion("coverarea not between", value1, value2, "coverarea");
            return (Criteria) this;
        }

        public Criteria andStreetIsNull() {
            addCriterion("street is null");
            return (Criteria) this;
        }

        public Criteria andStreetIsNotNull() {
            addCriterion("street is not null");
            return (Criteria) this;
        }

        public Criteria andStreetEqualTo(String value) {
            addCriterion("street =", value, "street");
            return (Criteria) this;
        }

        public Criteria andStreetNotEqualTo(String value) {
            addCriterion("street <>", value, "street");
            return (Criteria) this;
        }

        public Criteria andStreetGreaterThan(String value) {
            addCriterion("street >", value, "street");
            return (Criteria) this;
        }

        public Criteria andStreetGreaterThanOrEqualTo(String value) {
            addCriterion("street >=", value, "street");
            return (Criteria) this;
        }

        public Criteria andStreetLessThan(String value) {
            addCriterion("street <", value, "street");
            return (Criteria) this;
        }

        public Criteria andStreetLessThanOrEqualTo(String value) {
            addCriterion("street <=", value, "street");
            return (Criteria) this;
        }

        public Criteria andStreetLike(String value) {
            addCriterion("street like", value, "street");
            return (Criteria) this;
        }

        public Criteria andStreetNotLike(String value) {
            addCriterion("street not like", value, "street");
            return (Criteria) this;
        }

        public Criteria andStreetIn(List<String> values) {
            addCriterion("street in", values, "street");
            return (Criteria) this;
        }

        public Criteria andStreetNotIn(List<String> values) {
            addCriterion("street not in", values, "street");
            return (Criteria) this;
        }

        public Criteria andStreetBetween(String value1, String value2) {
            addCriterion("street between", value1, value2, "street");
            return (Criteria) this;
        }

        public Criteria andStreetNotBetween(String value1, String value2) {
            addCriterion("street not between", value1, value2, "street");
            return (Criteria) this;
        }

        public Criteria andLacIsNull() {
            addCriterion("lac is null");
            return (Criteria) this;
        }

        public Criteria andLacIsNotNull() {
            addCriterion("lac is not null");
            return (Criteria) this;
        }

        public Criteria andLacEqualTo(String value) {
            addCriterion("lac =", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacNotEqualTo(String value) {
            addCriterion("lac <>", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacGreaterThan(String value) {
            addCriterion("lac >", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacGreaterThanOrEqualTo(String value) {
            addCriterion("lac >=", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacLessThan(String value) {
            addCriterion("lac <", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacLessThanOrEqualTo(String value) {
            addCriterion("lac <=", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacLike(String value) {
            addCriterion("lac like", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacNotLike(String value) {
            addCriterion("lac not like", value, "lac");
            return (Criteria) this;
        }

        public Criteria andLacIn(List<String> values) {
            addCriterion("lac in", values, "lac");
            return (Criteria) this;
        }

        public Criteria andLacNotIn(List<String> values) {
            addCriterion("lac not in", values, "lac");
            return (Criteria) this;
        }

        public Criteria andLacBetween(String value1, String value2) {
            addCriterion("lac between", value1, value2, "lac");
            return (Criteria) this;
        }

        public Criteria andLacNotBetween(String value1, String value2) {
            addCriterion("lac not between", value1, value2, "lac");
            return (Criteria) this;
        }

        public Criteria andCiIsNull() {
            addCriterion("ci is null");
            return (Criteria) this;
        }

        public Criteria andCiIsNotNull() {
            addCriterion("ci is not null");
            return (Criteria) this;
        }

        public Criteria andCiEqualTo(String value) {
            addCriterion("ci =", value, "ci");
            return (Criteria) this;
        }

        public Criteria andCiNotEqualTo(String value) {
            addCriterion("ci <>", value, "ci");
            return (Criteria) this;
        }

        public Criteria andCiGreaterThan(String value) {
            addCriterion("ci >", value, "ci");
            return (Criteria) this;
        }

        public Criteria andCiGreaterThanOrEqualTo(String value) {
            addCriterion("ci >=", value, "ci");
            return (Criteria) this;
        }

        public Criteria andCiLessThan(String value) {
            addCriterion("ci <", value, "ci");
            return (Criteria) this;
        }

        public Criteria andCiLessThanOrEqualTo(String value) {
            addCriterion("ci <=", value, "ci");
            return (Criteria) this;
        }

        public Criteria andCiLike(String value) {
            addCriterion("ci like", value, "ci");
            return (Criteria) this;
        }

        public Criteria andCiNotLike(String value) {
            addCriterion("ci not like", value, "ci");
            return (Criteria) this;
        }

        public Criteria andCiIn(List<String> values) {
            addCriterion("ci in", values, "ci");
            return (Criteria) this;
        }

        public Criteria andCiNotIn(List<String> values) {
            addCriterion("ci not in", values, "ci");
            return (Criteria) this;
        }

        public Criteria andCiBetween(String value1, String value2) {
            addCriterion("ci between", value1, value2, "ci");
            return (Criteria) this;
        }

        public Criteria andCiNotBetween(String value1, String value2) {
            addCriterion("ci not between", value1, value2, "ci");
            return (Criteria) this;
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

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}