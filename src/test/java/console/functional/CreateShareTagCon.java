package console.functional;

import console.dao.ShareDao;
import console.dao.ShareDaoImpl;
import console.dao.TagDao;
import console.dao.TagDaoImpl;
import console.entities.Share;
import console.entities.Tag;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Random;

public class CreateShareTagCon {


    public static void main(String[] args) {
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        ShareDao shareDao = new ShareDaoImpl(entityManager);
        TagDao tagDao = new TagDaoImpl(entityManager);
        Random rand = new Random();
        ArrayList<Tag> tags = new ArrayList<>(tagDao.getAll());
        ArrayList<Share> shares = new ArrayList<>(shareDao.getAll());
        shares.forEach(share -> {
            int amountOfTags = rand.nextInt(3);
            for (int i = 0; i < amountOfTags; i++) {
                Tag tag = tags.get(rand.nextInt(tags.size() - 1));
                share.getTags().add(tag);
            }
            shareDao.update(share);
        });

    }


}
