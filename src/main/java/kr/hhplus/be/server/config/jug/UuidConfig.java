package kr.hhplus.be.server.config.jug;

import kr.hhplus.be.server.common.domain.UUIDGenerator;
import kr.hhplus.be.server.common.infra.jug.UuidV7Generator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UuidConfig {

    @Bean
    public UUIDGenerator idGenerator(){
        return new UuidV7Generator();
    }
}
