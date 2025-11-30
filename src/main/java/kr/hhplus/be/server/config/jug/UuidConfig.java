package kr.hhplus.be.server.config.jug;

import kr.hhplus.be.server.common.UUIDGenerator;
import kr.hhplus.be.server.concert.infra.jug.UuidV7Generator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UuidConfig {

    @Bean
    public UUIDGenerator idGenerator(){
        return new UuidV7Generator();
    }
}
