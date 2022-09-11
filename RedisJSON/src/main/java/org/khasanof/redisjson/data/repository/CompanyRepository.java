package org.khasanof.redisjson.data.repository;

import com.redis.om.spring.repository.RedisDocumentRepository;
import org.khasanof.redisjson.data.entity.Company;

public interface CompanyRepository extends RedisDocumentRepository<Company, String> {
}
