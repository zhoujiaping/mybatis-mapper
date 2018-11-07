package cn.sirenia.mybatis.sql.provider

import cn.sirenia.mybatis.util.XMLMapperConf
import org.apache.ibatis.mapping.ResultMapping

def selectByPrimaryKey(conf){
	def columns = conf.mappedColumns.join(',')
    def tablename = conf.tablename
    def idColumn = conf.idResultMapping.column
    def idJdbcType = conf.idResultMapping.jdbcType
	"""
	<script>
	select ${columns} 
	from ${tablename} 
	where ${idColumn}=#{id,jdbcType=${idJdbcType}}
	</script>
	""".trim()
}
def countByExample(conf) {
	def tablename = conf.tablename
	def whereClause = whereClause()
	"""
	<script>
	select count(*) from ${tablename}
	<if test="_parameter != null">
	${whereClause}
	</if>
	</script>
	""".trim()
}

def deleteByExample(conf) {
	def tablename = conf.tablename
	def whereClause = whereClause()
	"""<script>
	delete from ${tablename}
	<if test="_parameter != null">
		${whereClause}
	</if>
	</script>
	""".trim()
}

def deleteByPrimaryKey(conf) {
    def tablename = conf.tablename
    def idColumn = conf.idResultMapping.column
    def idJdbcType = conf.idResultMapping.jdbcType
    """
    delete from ${tablename} 
   where ${idColumn}=#{id,jdbcType=${idJdbcType}}
    """.trim()
}
def deleteByPrimaryKeyAndVersion(conf) {
	def tablename = conf.tablename
    def idColumn = conf.idResultMapping.column
    def idJdbcType = conf.idResultMapping.jdbcType
	def optiColumn = conf.optiLockResultMapping.column
	"""
	<script>
	delete from ${tablename} 
	where ${idColumn}=#{id,jdbcType=${idJdbcType}}
	and ${optiColumn}=${optiColumn}+1
	</script>
	""".trim()
}

def insert(conf) {
    //getMappedColumns和getResultMappings的字段顺序不是一致的。
    def tablename = conf.tablename
    def columns = conf.resultMappings.collect({it.column}).join(',')
    def values = conf.resultMappings.collect({"""#{${it.property},jdbcType=${it.jdbcType}}"""}).join(',')
    """
	<script>
    insert into ${tablename}(${columns})values(${values})
	</script>
	""".trim()
}

def insertSelective(conf) {
	def tablename = conf.tablename
    def columns = conf.resultMappings.collect({"""<if test="${it.property} != null">${it.column},</if>"""}).join('')
    def values = conf.resultMappings.collect({"""<if test="${it.property} != null">#{${it.column},jdbcType=${it.jdbcType}},</if>"""}).join('')
    """
	<script>
    insert into ${tablename}
   <trim prefix="(" suffix=")" suffixOverrides=",">
   </trim>
   <trim prefix="values (" suffix=")" suffixOverrides=",">
   </trim>
	</script>
	""".trim()
}

def selectByExample(conf) {
	def tablename = conf.tablename
    def columns = conf.resultMappings.collect({it.column}).join(',')
    def values = conf.resultMappings.collect({"""<if test="${it.property} != null">#{${it.column},jdbcType=${it.jdbcType}},</if>"""}).join('')
    def whereClause = whereClause()
    """
	<script>
	select
	<if test="distinct" >
	distinct
	</if>
	'false' as QUERYID,
	${columns}
	from ${tablename}
	<if test="_parameter != null" >
	</if>
	<if test="orderByClause != null">
	order by \${orderByClause}
	</if>
	</script>
	""".trim()
}
def selectUniqueByExample(conf) {
	selectByExample(conf)
}

def updateByExampleSelective(conf) {
	def tablename = conf.tablename
    def setters = conf.resultMappings.collect({"""<if test="record.${it.property} != null">${it.column}=#{record.${it.property},jdbcType=${it.jdbcType}},</if>"""}).join('')
    def exampleWhereClause = exampleWhereClause()
    """
	<script>
	update ${tablename}
	<set>
	${setters}
	</set>
	<if test="_parameter != null">
	${exampleWhereClause}
	</if>
	</script>
	""".trim()
}

def updateByExample(conf) {
	def tablename = conf.tablename
    def setters = conf.resultMappings.collect({"""${it.column} = #{record.${it.property},jdbcType=${it.jdbcType}}"""}).join(',')
    def exampleWhereClause = exampleWhereClause()
    """
	<script>
	update ${tablename}
	set
	${setters}
	<if test="_parameter != null">
	${exampleWhereClause}
	</if>
	</script>
	""".trim()
}

def updateByPrimaryKeySelective(conf) {
	def tablename = conf.tablename
	def idColumn = conf.idResultMapping.column
	def idProperty = conf.idResultMapping.property
	def idJdbcType = conf.idResultMapping.jdbcType
    def setters = conf.resultMappings.collect({"""<if test="${it.property} != null">${it.column} = #{${it.property},jdbcType=${it.jdbcType}},</if>"""}).join('')
    """
	<script>
	update ${tablename}
	<set>
	${setters}
	</set>
	where ${idColumn}=#{${idProperty},jdbcType=${idJdbcType}}
	</script>
	""".trim()
}
def updateByPrimaryKeySelectiveAndVersion(conf) {
	def tablename = conf.tablename
	def idColumn = conf.idResultMapping.column
	def idProperty = conf.idResultMapping.property
	def idJdbcType = conf.idResultMapping.jdbcType
	def optiRm = conf.optiLockResultMapping
    def setters = conf.resultMappings.findAll({
    	it.column!=idColumn
    }).collect({
    	if(conf.isOptiMapping(it)){
    		"${it.column}=${it.column}+1,"
    	}else{
    		"""<if test="${it.property} != null">${it.column} = #{${it.property},jdbcType=${it.jdbcType}},</if>"""
    	}
    }).join('')
    """
	<script>
	update ${tablename}
	<set>
	${setters}
	</set>
	where ${idColumn}=#{${idProperty},jdbcType=${idJdbcType}}
	and ${optiRm.column}=#{${optiRm.property},jdbcType=${optiRm.jdbcType}}
	</script>
	""".trim()
}
def updateByPrimaryKey(XMLMapperConf helper) {
	def tablename = conf.tablename
	def idColumn = conf.idResultMapping.column
	def idProperty = conf.idResultMapping.property
	def idJdbcType = conf.idResultMapping.jdbcType
    def setters = conf.resultMappings.collect({"""${it.column} = #{${it.property},jdbcType=${it.jdbcType}}"""}).join(',')
    """
	<script>
	update ${tablename}
	set
	${setters}
	</set>
	where ${idColumn}=#{${idProperty},jdbcType=${idJdbcType}}
	</script>
	""".trim()
}
def updateByPrimaryKeyAndVersion(conf) {
	def tablename = conf.tablename
	def idColumn = conf.idResultMapping.column
	def idProperty = conf.idResultMapping.property
	def idJdbcType = conf.idResultMapping.jdbcType
	def optiRm = conf.optiLockResultMapping
    def setters = conf.resultMappings.findAll({
    	it.column!=idColumn
    }).collect({
    	if(conf.isOptiMapping(it)){
    		"${it.column}=${it.column}+1,"
    	}else{
    		"""${it.column} = #{${it.property},jdbcType=${it.jdbcType}}"""
    	}
    }).join(',')
    """
	<script>
	update ${tablename}
	set
	${setters}
	where ${idColumn}=#{${idProperty},jdbcType=${idJdbcType}}
	and ${optiRm.column}=#{${optiRm.property},jdbcType=${optiRm.jdbcType}}
	</script>
	""".trim()
}

def batchInsert(conf) {
	def tablename = conf.tablename
	def columns = conf.resultMappings.collect({it.column	}).join(',')
	def values = conf.resultMappings.collect({"#{record.${it.property},jdbcType=${it.jdbcType}}"}).join(',')
	"""
	<script>
	insert into ${tablename}(${columns})values
	<foreach collection="recordList" item="record" separator=",">
		(${values})
	</foreach>
	</script>
	""".trim()
}

/**
 * 根据第一条记录判断插入哪些字段。
 */

def batchInsertSelective(conf) {
	def tablename = conf.tablename
	def columns = conf.resultMappings.collect({"""<if test="recordList[0].${it.property} != null">${it.column},</if>"""}).join('')
	def values = conf.resultMappings.collect({"""<if test="recordList[0].${it.property} != null">#{record.${it.property},jdbcType=${it.jdbcType}},</if>"""}).join('')
	"""
	<script>
	insert into ${tablename}
	<trim prefix="(" suffix=")" suffixOverrides=",">
	</trim>
	values
	<foreach collection="recordList" item="record" separator=",">
		<trim prefix="(" suffix=")" suffixOverrides=",">
			${values}
		</trim>
	</foreach>
	</script>
	""".trim()
}

def selectKey(conf){
	def sql = ""
	if(conf.dialect.startsWith('postgre')){
		def tablename = conf.tablename
		def pkType = conf.idResultMapping.javaType.simpleName
		if(pkType=='Integer'){
			sql = "select nextVal('${tablename}_id_seq')::int4"
		}else if(pkType=='Long'){
			sql = "select nextVal('${tablename}_id_seq')"
		}else{
			throw new RuntimeException("primary key type ${pkType} not be supported in selectKey");
		}
	}else if(conf.dialect.startsWith('mysql')){
		sql = "select LAST_INSERT_ID();"
	}else{
		throw new RuntimeException("dialect[${conf.dialect}] not be supported");
	}
	sql
}
def exampleWhereClause() {
	'''
	<where>
	    <foreach collection="example.oredCriteria" item="criteria" separator="or">
	    	<if test="criteria.valid">
	    		<trim prefix="(" prefixOverrides="and" suffix=")">
	    			<foreach collection="criteria.criteria" item="criterion">
	    				<choose>
	    					<when test="criterion.noValue">
	    						and ${criterion.condition}
	    					</when>
	    					<when test="criterion.singleValue">
	    						and ${criterion.condition} #{criterion.value}
	    					</when>
	    					<when test="criterion.betweenValue">
	    						and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}
	    					</when>
	    					<when test="criterion.listValue">
	    						and ${criterion.condition}
	    						<foreach close=")" collection="criterion.value" item="listItem" open="(" separator=",">
	    							#{listItem}
	    						</foreach>
	    					</when>
	    				</choose>
	    			</foreach>
	    		</trim>
	    	</if>
	    </foreach>
    </where>
    '''
}
def whereClause(){
	'''
	<where>
		<foreach collection="oredCriteria" item="criteria" separator="or">
			<if test="criteria.valid">
				<trim prefix="(" prefixOverrides="and" suffix=")">
					<foreach collection="criteria.criteria" item="criterion">
						<choose>
							<when test="criterion.noValue">
								and ${criterion.condition}
							</when>
							<when test="criterion.singleValue">
								and ${criterion.condition} #{criterion.value}
							</when>
							<when test="criterion.betweenValue">
								and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}
							</when>
							<when test="criterion.listValue">
								<foreach close=")" collection="criterion.value" item="listItem" open="(" separator=",">
									#{listItem}
								</foreach>
							</when>
						</choose>
					</foreach>
				</trim>
			</if>
		</foreach>
	</where>
	'''
}