/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.boot.support.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 抽象审计实体
 *
 * @author gyl
 * @since 0.0.1
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Audited
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1959121414265638507L;

    @ApiModelProperty(value = "创建帐号", hidden = true)
    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 20, updatable = false)
    private String createdBy;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @ApiModelProperty(value = "最后修改帐号", hidden = true)
    @LastModifiedBy
    @Column(name = "last_modified_by", length = 20)
    private String lastModifiedBy;

    @ApiModelProperty(value = "最后修改时间", hidden = true)
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
}
