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
import org.apache.shardingsphere.core.parse.sql.segment.dml.assignment.AssignmentSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.assignment.SetAssignmentsSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.expr.simple.ParameterMarkerExpressionSegment;
import org.apache.shardingsphere.core.parse.sql.statement.SQLStatement;
import org.apache.shardingsphere.core.parse.sql.statement.dml.InsertStatement;
import org.apache.shardingsphere.core.rewrite.token.pojo.InsertSetEncryptValueToken;
import org.apache.shardingsphere.core.rule.EncryptRule;
import org.apache.shardingsphere.spi.encrypt.ShardingEncryptor;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Insert set encrypt value token generator.
 *
 * @author panjuan
 */
public final class InsertSetEncryptValueTokenGenerator implements CollectionSQLTokenGenerator<EncryptRule> {
    
    @Override
    public Collection<InsertSetEncryptValueToken> generateSQLTokens(final SQLStatement sqlStatement, final List<Object> parameters, final EncryptRule encryptRule) {
        Optional<SetAssignmentsSegment> setAssignmentsSegment = sqlStatement.findSQLSegment(SetAssignmentsSegment.class);
        if (!(sqlStatement instanceof InsertStatement && setAssignmentsSegment.isPresent())) {
            return Collections.emptyList();
        }
        return createInsertSetEncryptValueTokens((InsertStatement) sqlStatement, encryptRule, setAssignmentsSegment.get());
    }
    
    private Collection<InsertSetEncryptValueToken> createInsertSetEncryptValueTokens(final InsertStatement insertStatement, final EncryptRule encryptRule, final SetAssignmentsSegment segment) {
        Collection<InsertSetEncryptValueToken> result = new LinkedList<>();
        for (AssignmentSegment each : segment.getAssignments()) {
            Optional<InsertSetEncryptValueToken> insertSetEncryptValueToken = getInsertSetEncryptValueToken(insertStatement, encryptRule, each);
            if (insertSetEncryptValueToken.isPresent()) {
                result.add(insertSetEncryptValueToken.get());
            }
        }
        return result;
    }
    
    private Optional<InsertSetEncryptValueToken> getInsertSetEncryptValueToken(final InsertStatement insertStatement, final EncryptRule encryptRule, final AssignmentSegment segment) {
        Optional<ShardingEncryptor> shardingEncryptor = encryptRule.getEncryptorEngine().getShardingEncryptor(insertStatement.getTables().getSingleTableName(), segment.getColumn().getName());
        if (shardingEncryptor.isPresent() && !(segment.getValue() instanceof ParameterMarkerExpressionSegment)) {
            return Optional.of(new InsertSetEncryptValueToken(segment.getValue().getStartIndex(), segment.getValue().getStopIndex(), segment.getColumn().getName()));
        }
        return Optional.absent();
    }
}
