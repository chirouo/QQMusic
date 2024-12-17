import cn.hutool.core.util.HashUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import com.qqmusic.entity.Music;
import com.qqmusic.service.MusicService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

public class Hash {

    @Test
    public void mytesy()
    {
        Integer id = new Integer(0);
        id = 2;
        System.out.println(id);
        System.out.println(id);
    }
    @Test
    public void testhashcode()
    {
        Music music1 = new Music();
        Music music2 = new Music();
        music1.setMusicname("这是music1");
        music2.setMusicname("这是music1");
        music1.setMusicUrl("11111");
        music2.setMusicUrl("11111");
        if(music1.hashCode() == music2.hashCode()) System.out.println("这俩居然相等");
        else System.out.println("这两个不相等");
    }
    @Test
    public void testMD5()
    {
        String password = "12345";
        String s = SecureUtil.md5(password);
    }

}
