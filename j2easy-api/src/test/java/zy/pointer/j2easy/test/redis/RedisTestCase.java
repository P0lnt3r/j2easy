package zy.pointer.j2easy.test.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import zy.pointer.j2easy.test.SpringTestCase;

import java.util.List;

public class RedisTestCase extends SpringTestCase {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test(){


    }

}
