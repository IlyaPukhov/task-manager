package com.ilyap.productivityservice.repository;

import com.ilyap.productivityservice.model.entity.Productivity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface ProductivityRepository extends ReactiveMongoRepository<Productivity, UUID> {

    @Query("""
            {
                "username" : :#{#username},
                "date" : {
                    "$gte" : :#{#startOfMonth},
                    "$lte" : :#{#endOfMonth}
                }
            }
            """)
    Flux<Productivity> findAllByUsername(String username, LocalDate startOfMonth, LocalDate endOfMonth);

    Flux<Productivity> findAllByUsername(String username);

    Mono<Productivity> findByUsernameAndDate(String username, LocalDate date);

    Mono<Void> deleteAllByUsername(String username);
}
