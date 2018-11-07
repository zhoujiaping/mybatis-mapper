package cn.sirenia.mybatis

import groovy.xml.MarkupBuilder


def testXml(){
	def columns = 'name,id'
    def tablename = 'sys_user'
    def idColumn = 'id'
    def idJdbcType = 'Integer'
	
	def writer = new StringWriter()
	def xml = new MarkupBuilder(writer)
	def sql = xml.script("""
			select ${columns} 
			from ${tablename} 
			where ${idColumn}=#{id,jdbcType=${idJdbcType}}
			""")
	println(writer)
	writer
}
testXml()