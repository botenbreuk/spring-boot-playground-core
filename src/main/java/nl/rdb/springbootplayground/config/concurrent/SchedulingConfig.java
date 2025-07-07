package nl.rdb.springbootplayground.config.concurrent;

import java.util.concurrent.Executors;

import nl.rdb.springbootplayground.config.security.AuthenticationAdapter;
import nl.rdb.springbootplayground.user.User;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.security.concurrent.DelegatingSecurityContextScheduledExecutorService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableScheduling
@Profile("!disable-scheduling")
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new AuthenticationAdapter(User.SYSTEM_USER));

        taskRegistrar.setScheduler(new DelegatingSecurityContextScheduledExecutorService(
                Executors.newScheduledThreadPool(100), securityContext));
    }
}