package ng.cranshaft.restwebflux.bootstrap;

import ng.cranshaft.restwebflux.domain.Category;
import ng.cranshaft.restwebflux.domain.Vendor;
import ng.cranshaft.restwebflux.repositories.CategoryRepository;
import ng.cranshaft.restwebflux.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("###### Loading Data on Bootstrap ######");

        if (categoryRepository.count().block() == 0) {
            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Bread").build()).block();
            categoryRepository.save(Category.builder().description("Meat").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();
            System.out.println("###### Loaded Categories: " + categoryRepository.count().block() + " ######");
        }

        if (vendorRepository.count().block() == 0) {
            vendorRepository.save(Vendor.builder().firstName("Kim").lastName("Kardashian").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Kanye").lastName("West").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Jimmy").lastName("Fallon").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Jimmy").lastName("Kimmel").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Joe").lastName("Biden").build()).block();
            System.out.println("###### Loaded Vendors: " + vendorRepository.count().block() + " ######");
        }

    }
}
