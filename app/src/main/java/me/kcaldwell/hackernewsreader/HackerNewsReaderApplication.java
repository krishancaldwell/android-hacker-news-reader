package me.kcaldwell.hackernewsreader;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class HackerNewsReaderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder()
                        .name("HackerNewsReader.realm")
                        .deleteRealmIfMigrationNeeded()
                        .build()
        );
    }
}
