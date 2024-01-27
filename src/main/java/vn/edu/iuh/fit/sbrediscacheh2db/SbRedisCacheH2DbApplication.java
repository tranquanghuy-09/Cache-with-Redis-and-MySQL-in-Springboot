package vn.edu.iuh.fit.sbrediscacheh2db;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import vn.edu.iuh.fit.sbrediscacheh2db.models.User;
import vn.edu.iuh.fit.sbrediscacheh2db.repositories.UserRepository;

@SpringBootApplication
@EnableCaching
public class SbRedisCacheH2DbApplication {
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SbRedisCacheH2DbApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Faker faker = new Faker();
                for (int i = 0; i<10; i++){
                    userRepository.save(new User(i+1, faker.name().fullName()));
                }
            }
        };
    }
}
