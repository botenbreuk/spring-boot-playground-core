package nl.rdb.springbootplayground._testdata;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import nl.rdb.springbootplayground._testdata.fixtures.UserFixtures;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test-data")
@RequiredArgsConstructor
public class LocalDataLoader {

    private final UserFixtures userFixtures;

    @PostConstruct
    public void init() {
        // Users
        userFixtures.sjonnyb();
    }
}
