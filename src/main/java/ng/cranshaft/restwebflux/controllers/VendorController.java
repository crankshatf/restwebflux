package ng.cranshaft.restwebflux.controllers;

import ng.cranshaft.restwebflux.domain.Vendor;
import ng.cranshaft.restwebflux.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/{id}")
    Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor vendorFound = vendorRepository.findById(id).block();

        boolean toSave = false;

        if (vendor.getFirstName() != null) {
            if (vendorFound.getFirstName() != null) {
                if (vendor.getFirstName().compareTo(vendorFound.getFirstName()) != 0) {
                    toSave = true;
                    vendorFound.setFirstName(vendor.getFirstName());
                }
            } else {
                toSave = true;
                vendorFound.setFirstName(vendor.getFirstName());
            }
        }

        if (vendor.getLastName() != null) {
            if (vendorFound.getLastName() != null) {
                if (vendor.getLastName().compareTo(vendorFound.getLastName()) != 0) {
                    toSave = true;
                    vendorFound.setLastName(vendor.getLastName());
                }
            } else {
                toSave = true;
                vendorFound.setLastName(vendor.getLastName());
            }
        }

        if (toSave) {
            return vendorRepository.save(vendorFound);
        }
        return Mono.just(vendorFound);
    }

}
