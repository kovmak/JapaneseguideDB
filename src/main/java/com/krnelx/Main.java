package com.krnelx;

import com.krnelx.persistence.ApplicationConfig;
import com.krnelx.persistence.context.factory.PersistenceContext;
import com.krnelx.persistence.entity.Users;
import com.krnelx.persistence.entity.Users.UsersRole;
import com.krnelx.persistence.util.ConnectionManager;
import com.krnelx.persistence.util.DatabaseInitializer;
import java.util.UUID;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        var connectionManager = context.getBean(ConnectionManager.class);
        var databaseInitializer = context.getBean(DatabaseInitializer.class);

        try {
            databaseInitializer.init();
            var persistenceContext = context.getBean(PersistenceContext.class);
            persistenceContext.users.registerNew(
                new Users(
                    null, // UUID тепер на першому місці
                    "krnelx",
                    "password",
                    UsersRole.ADMIN,
                    "Name"));

            persistenceContext.users.registerNew(
                new Users(
                    null,
                    "k1fl1k",
                    "password2",
                    UsersRole.ADMIN,
                    "ім'я"));

            persistenceContext.users.commit();

            persistenceContext.users.registerModified(
                new Users(UUID.fromString("018f39f9-1826-7cb9-9775-feee72794e6a"),
                    "mike_wilson1",
                    "mike.wilson@gmail.com",
                    UsersRole.CLIENT,
                    "password5")
            );

            persistenceContext.users.registerModified(
                new Users(UUID.fromString("018f39f9-05de-704c-bdc4-9fb5e1432e19"),
                    "emily_brown2",
                    "emily.brown2@gmail.com",
                    UsersRole.MODER,
                    "password5")
            );

            persistenceContext.users.commit();

            persistenceContext.users.registerDeleted(
                UUID.fromString("018f39f8-6850-75d3-b6b1-be865de4d061"));
            persistenceContext.users.registerDeleted(
                UUID.fromString("018f39f8-8697-7a1a-93ab-5d42d7b42dd5"));
            persistenceContext.users.commit();
        } finally {
            connectionManager.closePool();
        }
    }
}
