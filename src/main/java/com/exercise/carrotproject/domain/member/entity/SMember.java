package com.exercise.carrotproject.domain.member.entity;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.RelationalPathBase;

public class SMember extends RelationalPathBase<QMember> {
    public SMember (String path) {
        super(QMember.class, PathMetadataFactory.forVariable(path), "", "member");
    }
    public final StringPath memId = createString("mem_id");

}
