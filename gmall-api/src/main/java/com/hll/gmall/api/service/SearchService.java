package com.hll.gmall.api.service;

import com.hll.gmall.api.bean.PmsSearchParam;
import com.hll.gmall.api.bean.PmsSearchSkuInfo;

import java.util.List;

/**
 * 搜索服务
 *
 * @author 我
 */
public interface SearchService {
    /**
     * 引入 mysql 中的 sku 到 indices 索引库
     *
     * @return 返回成功与否
     */
    String importSkuToIndices();

    String delSkuFromIndices();

    /**
     * 根据参数从 indices 索引库搜索 sku
     *
     * @param pmsSearchParam 搜索参数
     * @return 返回搜索的 sku 数据
     */
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);

}
