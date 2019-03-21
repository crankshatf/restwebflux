package ng.cranshaft.restwebflux.controllers;

import ng.cranshaft.restwebflux.domain.Vendor;
import ng.cranshaft.restwebflux.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        BDDMockito.given(vendorRepository.findAll()).willReturn(Flux.just(
                Vendor.builder().firstName("Jimmy").lastName("Fallon").build(),
                Vendor.builder().firstName("Jimmy").lastName("Kimmel").build()
        ));
        webTestClient.get().uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Fallon").build()));
        webTestClient.get().uri("/api/v1/vendors/afakeuuid")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void create() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));
        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Foo").lastName("Bar").build());
        webTestClient.post().uri("/api/v1/vendors")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void update() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));
        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Foo").lastName("Bar").build());
        webTestClient.put().uri("/api/v1/vendors/dummyuuid")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void patchWithChanges() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));
        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Foo").lastName("Bar").build());
        webTestClient.patch().uri("/api/v1/vendors/dummyuuid")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();
        BDDMockito.verify(vendorRepository).save(any());
    }

    @Test
    public void patchWithoutChanges() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Foo").build()));
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));
        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Foo").build());
        webTestClient.patch().uri("/api/v1/vendors/dummyuuid")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();
        BDDMockito.verify(vendorRepository, never()).save(any());
    }
}