package ng.cranshaft.restwebflux.repositories;

import ng.cranshaft.restwebflux.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
