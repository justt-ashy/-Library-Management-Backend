package com.example.LMS_Backend;


import com.example.LMS_Backend.common.Constants;
import com.example.LMS_Backend.model.User;
import com.example.LMS_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ApplicationEvent event){
        initDatabaseEntities();
    }

    private void initDatabaseEntities() {
        if(userService.getAll().isEmpty()){
            userService.addNew(new User("Mr. Admin", "admin", "admin", Constants.ROLE_ADMIN));
            userService.addNew(new User("Mr. Librarian", "librarian", "librarian", Constants.ROLE_LIBRARIAN));
        }
    }
}
