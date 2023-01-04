package org.ylzl.eden.spring.framework.json

import com.alibaba.fastjson.annotation.JSONType
import org.ylzl.eden.spring.framework.json.fastjson.Fastjson
import org.ylzl.eden.spring.framework.json.fastjson2.Fastjson2
import org.ylzl.eden.spring.framework.json.jackson.Jackson
import org.ylzl.eden.spring.framework.json.support.JSONHelper
import spock.lang.Specification

class JSONTest extends Specification {

	@JSONType(orders = ["id", "username", "actived"]) // fastjson 默认按字母排序
	@com.alibaba.fastjson2.annotation.JSONType(orders = ["id", "username", "actived"])
	static class User {

		private Long id;

		private String username;

		private Boolean actived;

		Long getId() {
			return id
		}

		void setId(Long id) {
			this.id = id
		}

		String getUsername() {
			return username
		}

		void setUsername(String username) {
			this.username = username
		}

		Boolean getActived() {
			return actived
		}

		void setActived(Boolean actived) {
			this.actived = actived
		}

		boolean equals(o) {
			if (this.is(o)) return true
			if (!(o instanceof User)) return false

			User user = (User) o

			if (actived != user.actived) return false
			if (id != user.id) return false
			if (username != user.username) return false

			return true
		}

		int hashCode() {
			int result
			result = (id != null ? id.hashCode() : 0)
			result = 31 * result + (username != null ? username.hashCode() : 0)
			result = 31 * result + (actived != null ? actived.hashCode() : 0)
			return result
		}
	}

	def "test extensions"() {
		expect:
		extension == JSONHelper.json(spi).getClass()

		where:
		spi         || extension
		"jackson"   || Jackson.class
		"fastjson"  || Fastjson.class
		"fastjson2" || Fastjson2.class
	}

	def "test toJSONString"() {
		given:
		User user = new User()
		user.setId(1L)
		user.setUsername("test")
		user.setActived(true)

		expect:
		json == JSONHelper.json(spi).toJSONString(user)

		where:
		spi         || json
		"jackson"   || "{\"id\":1,\"username\":\"test\",\"actived\":true}"
		"fastjson"  || "{\"id\":1,\"username\":\"test\",\"actived\":true}"
		"fastjson2" || "{\"id\":1,\"username\":\"test\",\"actived\":true}"
	}

	def "test parseObject"() {
		given:
		User user = new User()
		user.setId(1L)
		user.setUsername("test")
		user.setActived(true)

		expect:
		user == JSONHelper.json(spi).parseObject(json, User.class)

		where:
		spi         || json
		"jackson"   || "{\"id\":1,\"username\":\"test\",\"actived\":true}"
		"fastjson"  || "{\"id\":1,\"username\":\"test\",\"actived\":true}"
		"fastjson2" || "{\"id\":1,\"username\":\"test\",\"actived\":true}"
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
