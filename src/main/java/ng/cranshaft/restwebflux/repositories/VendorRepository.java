package ng.cranshaft.restwebflux.repositories;

import ng.cranshaft.restwebflux.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
