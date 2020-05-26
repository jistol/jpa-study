package io.jistol.github.jpademo.entity.oopquery.entity;

import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.dsl.BooleanExpression;

public class MemberExpression {
    @QueryDelegate(Member.class)
    public static BooleanExpression isOlder(QMember member, Integer age) {
        return member.age.gt(age);
    }

    @QueryDelegate(Member.class)
    public static BooleanExpression isOldAge(QMember member) {
        return member.age.gt(20);
    }
}
