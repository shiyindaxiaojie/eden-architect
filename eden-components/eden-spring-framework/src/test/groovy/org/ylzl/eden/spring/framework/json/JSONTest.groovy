package org.ylzl.eden.spring.framework.json


import org.ylzl.eden.spring.framework.json.fastjson.Fastjson
import org.ylzl.eden.spring.framework.json.fastjson2.Fastjson2
import org.ylzl.eden.spring.framework.json.gson.Gson
import org.ylzl.eden.spring.framework.json.jackson.Jackson
import org.ylzl.eden.spring.framework.json.support.JSONHelper
import spock.lang.Specification

class JSONTest extends Specification {

	TestCase testCase = TestCase.builder()
		.id(1L)
		.username("mengxiangge")
		.actived(true)
		.build()

	static testJson = "{\"id\":1,\"username\":\"mengxiangge\",\"actived\":true}"

	def "test extensions"() {
		expect:
		extension == JSONHelper.json(spi).getClass()

		where:
		spi         || extension
		"jackson"   || Jackson.class
		"fastjson"  || Fastjson.class
		"fastjson2" || Fastjson2.class
		"gson" 		|| Gson.class
	}

	def "test toJSONString"() {
		expect:
		json == JSONHelper.json(spi).toJSONString(testCase)

		where:
		spi         || json
		"jackson"   || testJson
		"fastjson"  || testJson
		"fastjson2" || testJson
		"gson" 		|| testJson
	}

	def "test parseObject"() {
		expect:
		testCase == JSONHelper.json(spi).parseObject(json, TestCase.class)

		where:
		spi         || json
		"jackson"   || testJson
		"fastjson"  || testJson
		"fastjson2" || testJson
		"gson" 		|| testJson
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
