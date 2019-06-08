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

package org.apache.shardingsphere.core.rewrite.token.generator;

import com.google.common.base.Optional;
import org.apache.shardingsphere.core.optimize.pagination.Pagination;
import org.apache.shardingsphere.core.parse.sql.segment.dml.pagination.NumberLiteralPaginationValueSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.pagination.PaginationValueSegment;
import org.apache.shardingsphere.core.parse.sql.statement.SQLStatement;
import org.apache.shardingsphere.core.parse.sql.statement.dml.SelectStatement;
import org.apache.shardingsphere.core.rewrite.token.pojo.RowCountToken;
import org.apache.shardingsphere.core.rule.ShardingRule;

import java.util.List;

/**
 * Row count token generator.
 *
 * @author panjuan
 */
public final class RowCountTokenGenerator implements OptionalSQLTokenGenerator<ShardingRule>, IgnoreForSingleRoute {
    
    @Override
    public Optional<RowCountToken> generateSQLToken(final SQLStatement sqlStatement, final List<Object> parameters, final ShardingRule shardingRule) {
        if (!(sqlStatement instanceof SelectStatement)) {
            return Optional.absent();
        }
        Optional<PaginationValueSegment> rowCount = getLiteralRowCountSegment((SelectStatement) sqlStatement);
        return rowCount.isPresent()
                ? Optional.of(new RowCountToken(rowCount.get().getStartIndex(), rowCount.get().getStopIndex(), getRevisedRowCount((SelectStatement) sqlStatement, parameters, rowCount.get())))
                : Optional.<RowCountToken>absent();
    }
    
    private Optional<PaginationValueSegment> getLiteralRowCountSegment(final SelectStatement selectStatement) {
        return selectStatement.getRowCount() instanceof NumberLiteralPaginationValueSegment ? Optional.of(selectStatement.getRowCount()) : Optional.<PaginationValueSegment>absent();
    }
    
    private int getRevisedRowCount(final SelectStatement selectStatement, final List<Object> parameters, final PaginationValueSegment rowCountSegment) {
        return new Pagination(selectStatement.getOffset(), rowCountSegment, parameters).getRevisedRowCount(selectStatement);
    }
}
