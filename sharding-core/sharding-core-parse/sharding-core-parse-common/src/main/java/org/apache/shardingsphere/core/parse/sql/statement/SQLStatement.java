/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core.parse.sql.statement;

import com.google.common.base.Optional;
import org.apache.shardingsphere.core.constant.SQLType;
import org.apache.shardingsphere.core.parse.sql.context.condition.Conditions;
import org.apache.shardingsphere.core.parse.sql.context.table.Tables;
import org.apache.shardingsphere.core.parse.sql.segment.SQLSegment;

import java.util.Collection;

/**
 * SQL statement.
 *
 * @author zhangliang
 */
public interface SQLStatement {
    
    /**
     * Get SQL type.
     *
     * @return SQL type
     */
    SQLType getType();
    
    /**
     * Get SQL segments.
     * 
     * @return SQL segments
     */
    Collection<SQLSegment> getSqlSegments();
    
    /**
     * Get tables.
     * 
     * @return tables
     */
    Tables getTables();
    
    /**
     * Get sharding conditions.
     *
     * @return conditions
     */
    Conditions getShardingConditions();
    
    /**
     * Get encrypt conditions.
     *
     * @return conditions
     */
    Conditions getEncryptConditions();
    
    /**
     * Get index of parameters.
     *
     * @return index of parameters
     */
    int getParametersIndex();
    
    /**
     * Set parameters index.
     * 
     * @param parametersIndex parameters index
     */
    void setParametersIndex(int parametersIndex);
    
    /**
     * Get logic SQL.
     * 
     * @return logic SQL
     */
    String getLogicSQL();
    
    /**
     * Set logic SQL.
     * 
     * @param logicTable logic SQL
     */
    void setLogicSQL(String logicTable);
    
    /**
     * Find SQL segment.
     *
     * @param sqlSegmentType SQL segment type
     * @param <T> type of SQL segment
     * @return SQL segment
     */
    <T extends SQLSegment> Optional<T> findSQLSegment(Class<T> sqlSegmentType);
    
    /**
     * Find SQL segment.
     *
     * @param sqlSegmentType SQL segment type
     * @param <T> type of SQL segment
     * @return SQL segments
     */
    <T extends SQLSegment> Collection<T> findSQLSegments(Class<T> sqlSegmentType);
}
