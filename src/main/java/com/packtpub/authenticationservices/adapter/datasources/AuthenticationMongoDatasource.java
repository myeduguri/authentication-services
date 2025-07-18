package com.packtpub.authenticationservices.adapter.datasources;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AuthenticationMongoDatasource extends ReactiveCrudRepository<AuthenticationDocument, String> {
    Mono<AuthenticationDocument> findByUsername(String username);
}
