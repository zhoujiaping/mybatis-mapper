package cn.zhou.mapper;

import java.io.FileNotFoundException;

import org.junit.Test;

import cn.zhou.mapper.MapperMaker;

public class MapperMakerTest {
    @Test
    public void test() throws FileNotFoundException {
        MapperMaker m = new MapperMaker();
        long begin = System.currentTimeMillis();
        m.make();
        long end = System.currentTimeMillis();
        System.out.println(end - begin);// 并行：1216，串行：6065
    }
}
