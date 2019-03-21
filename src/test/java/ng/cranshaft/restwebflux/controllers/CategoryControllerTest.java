package ng.cranshaft.restwebflux.controllers;

import ng.cranshaft.restwebflux.domain.Category;
import ng.cranshaft.restwebflux.repositories.CategoryRepository;
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

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(
                        Category.builder().description("Nuts").build(),
                        Category.builder().description("Beans").build())
                );
        webTestClient.get().uri("/api/v1/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Pets").build()));
        webTestClient.get().uri("/api/v1/categores/randomuuidstring")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void create() {
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));
        Mono<Category> categoryMono = Mono.just(Category.builder().description("Dummy").build());
        webTestClient.post().uri("/api/v1/categories").body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void update() {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));
        Mono<Category> categoryMono = Mono.just(Category.builder().description("Dummy").build());
        webTestClient.put().uri("/api/v1/categories/dummyuuid")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void patchWithChanges() {
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));
        Mono<Category> categoryMono = Mono.just(Category.builder().description("Dummy").build());
        webTestClient.patch().uri("/api/v1/categories/dummyuuid")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isOk();
        BDDMockito.verify(categoryRepository).save(any());
    }

    @Test
    public void patchWithoutChanges() {
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));
        Mono<Category> categoryMono = Mono.just(Category.builder().build());
        webTestClient.patch().uri("/api/v1/categories/dummyuuid")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isOk();
        BDDMockito.verify(categoryRepository, never()).save(any());
    }
}