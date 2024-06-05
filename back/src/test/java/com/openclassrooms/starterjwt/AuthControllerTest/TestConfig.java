//package com.openclassrooms.starterjwt.AuthControllerTest;
//
//
//import com.openclassrooms.starterjwt.repository.SessionRepository;
//import com.openclassrooms.starterjwt.repository.TeacherRepository;
//import com.openclassrooms.starterjwt.repository.UserRepository;
//import com.openclassrooms.starterjwt.security.jwt.services.UserDetailsServiceImpl;
//import org.mockito.Mock;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.test.context.TestPropertySource;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import static org.mockito.Mockito.mock;
//
//@Configuration
//@EnableJpaRepositories(basePackages = "com.openclassrooms.starterjwt.repository")
//@TestPropertySource(locations = "application-test.properties")
//public class TestConfig {
//
//
//    @Bean
//    public UserRepository returnBeanUser(){
//        UserRepository userRepository = mock(UserRepository.class);
//
//        return  userRepository ;
//    }
//
//    @Bean
//    public SessionRepository returnBeanSession(){
//        SessionRepository sessionRepository = mock(SessionRepository.class);
//
//        return  sessionRepository ;
//    }
//
//    @Bean
//    public TeacherRepository returnBeanTeacher(){
//        TeacherRepository teacherRepository = mock(TeacherRepository.class);
//
//        return  teacherRepository ;
//    }
//
//    @Bean
//    public EntityManagerFactory returnBeanEntity(){
//        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
//
//        return  entityManagerFactory ;
//    }
//
//    @Bean
//    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource);
//        // configure other properties of EntityManagerFactory if needed
//        emf.afterPropertiesSet();
//        return emf.getObject();
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        return DataSourceBuilder.create()
//                .url("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
//                .username("sa")
//                .password("")
//                .driverClassName("org.h2.Driver")
//                .build();
//    }
//}
