package com.hll.gmall.search;

import com.hll.gmall.api.bean.PmsSearchParam;
import com.hll.gmall.api.bean.PmsSearchSkuInfo;
import com.hll.gmall.api.service.SearchService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @RunWith 是一个运行器，实质上是一个接口，是用来扩展 Junit 的 Test Runner 的。
 * Suite , Parameterized 以及 SpringTest 都是 Test Runner ,他们都是 org.junit.runner.Runner 的子类。
 * @RunWith(JUnit4.class) 指用 JUnit4 来运行
 * @RunWith(SpringJUnit4ClassRunner.class) 让测试运行于 Spring 测试环境。
 * 在测试开始的时候自动创建 Spring 的应用上下文。
 * @RunWith(Suite.class) 是一套测试集合
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class GmallSearchServiceApplicationTests {
    @DubboReference
    SearchService searchService;

    @Test
    void textJedis() throws IOException {

    }

    @Test
    void importSkuToIndices() throws IOException {
        String result = searchService.importSkuToIndices();
        System.out.println(result);
    }

    @Test
    void getSku() throws IOException {
        PmsSearchParam pmsSearchParam = new PmsSearchParam();
        pmsSearchParam.setCatalog3Id("61");
        pmsSearchParam.setKeyword("手机");
        pmsSearchParam.setValueId(new String[]{"41"});

        List<PmsSearchSkuInfo> list = searchService.list(pmsSearchParam);
        System.out.println(list.size());
    }

}
