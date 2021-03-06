package com.teamdonut.eatto.common.helper;

import com.teamdonut.eatto.data.Keyword;
import com.teamdonut.eatto.data.User;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmDataHelper {

    /**
     * get user
     *
     * @return User
     */
    public static User getUser() {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        User copyUser;

        if (user != null) {
            copyUser = realm.copyFromRealm(user);
            realm.close();

            return copyUser;
        } else {
            realm.close();
            throw new NullPointerException("There is no user data.");
        }
    }

    /**
     * Insert keyword
     *
     * @param realm
     * @param content keyword
     */
    public static void insertKeyword(Realm realm, final String content) {
        realm.executeTransactionAsync(r -> {
            RealmResults<Keyword> keywords = r.where(Keyword.class).findAll();
            Keyword duplicateKeyword = r.where(Keyword.class).equalTo("content", content).findFirst();

            if(duplicateKeyword !=null) {
                duplicateKeyword.setSearchDate(new Date());
                return;
            }

            if (keywords.size() == 17) {
                Keyword oldKeyword = r.where(Keyword.class).findFirst();
                oldKeyword.deleteFromRealm();
            }
            Keyword keyword = r.createObject(Keyword.class);

            keyword.setContent(content);
            keyword.setSearchDate(new Date());

        });
    }

    /**
     * Remove All keyword.
     *
     * @param realm Realm
     */
    public static void removeAllKeyword(Realm realm) {
        realm.executeTransactionAsync(r -> {
            RealmResults<Keyword> keywords = r.where(Keyword.class).findAll();
            keywords.deleteAllFromRealm();
        });
    }

    /**
     * Insert user.
     *
     * @param kakaoId  user's kakao id
     * @param nickName user's nickName
     * @param photo    user's profile image url
     */
    public static void insertUser(final long kakaoId, final String nickName, final String photo) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(r -> {
            User user = r.createObject(User.class, kakaoId);

            user.setNickName(nickName);
            user.setPhoto(photo);
        });
        realm.close();
    }

    /**
     * update user.
     *
     * @param nickName user's nickName
     * @param sex      user's sex
     * @param photo    user's profile image url
     */
    public static void updateUser(final String nickName, final int sex, final String photo) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(r -> {
            final User user = r.where(User.class).findFirst();
            user.setNickName(nickName);
            user.setSex(sex);
            user.setPhoto(photo);
        });
        realm.close();
    }

    /**
     * update user
     * @param u     user
     */
    public static void updateUser(final User u) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(r -> {
            final User user = r.where(User.class).findFirst();

            user.setNickName(u.getNickName());
            user.setSex(u.getSex());
            user.setPhoto(u.getPhoto());
            user.setAge(u.getAge());
        });
        realm.close();
    }
}
