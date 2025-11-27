package kr.hhplus.be.server.config.jug;

import kr.hhplus.be.server.concert.domain.IdGenerator;
import kr.hhplus.be.server.concert.infra.UuidV7Generator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UuidConfig {

    @Bean
    public IdGenerator idGenerator(){
        return new UuidV7Generator();
    }
}
