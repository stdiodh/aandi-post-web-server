package com.example.aandi_post_web_server.common.config

import com.mongodb.ConnectionString
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory

@TestConfiguration
class TestMongoConfig {
    @Bean
    fun reactiveMongoTemplate(): ReactiveMongoTemplate {
        val mongoUri = System.getenv("MONGO_DB_URL") ?: "mongodb://localhost:27017/test_db"
        val connectionString = ConnectionString(mongoUri)
        return ReactiveMongoTemplate(SimpleReactiveMongoDatabaseFactory(connectionString))
    }
}
