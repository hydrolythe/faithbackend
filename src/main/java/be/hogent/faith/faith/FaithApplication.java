package be.hogent.faith.faith;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class FaithApplication {
    public static void main(String[] args){
        SpringApplication.run(FaithApplication.class, args);
    }


}
