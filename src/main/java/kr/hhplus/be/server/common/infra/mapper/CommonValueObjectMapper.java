package kr.hhplus.be.server.common.infra.mapper;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CommonValueObjectMapper {

    default Money map(long amount){
        return Money.wons(amount);
    }

    default Long map(Money money){
        return money.value();
    }

    default UserId map(UUID userId){
        return new UserId(userId);
    }

    default UUID toId(UserId userId){
        return userId.value();
    }

}
