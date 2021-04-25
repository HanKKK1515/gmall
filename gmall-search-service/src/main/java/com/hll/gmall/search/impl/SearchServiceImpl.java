package com.hll.gmall.search.impl;

import com.alibaba.fastjson.JSONObject;
import com.hll.gmall.api.bean.PmsSearchParam;
import com.hll.gmall.api.bean.PmsSearchSkuInfo;
import com.hll.gmall.api.bean.PmsSkuAttrValue;
import com.hll.gmall.api.bean.PmsSkuInfo;
import com.hll.gmall.api.constant.Constants;
import com.hll.gmall.api.service.SearchService;
import com.hll.gmall.search.mapper.PmsSkuAttrValueMapper;
import com.hll.gmall.search.mapper.PmsSkuInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DubboService
public class SearchServiceImpl implements SearchService {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Override
    public String importSkuToIndices() {
        IndexRequest indexRequest = new IndexRequest(Constants.ES_INDICES, Constants.ES_TYPE);
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);

            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo, pmsSearchSkuInfo);

            indexRequest.id(pmsSkuInfo.getId()).source(JSONObject.toJSONString(pmsSearchSkuInfo), XContentType.JSON);
            try {
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
                return "Import False!";
            }
        }
        return "Import Success";
    }

    @Override
    public String delSkuFromIndices() {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(Constants.ES_INDICES);
        deleteByQueryRequest.setQuery(QueryBuilders.matchAllQuery());
        try {
            restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return "Delete False!";
        }

        return "Delete Success!";
    }

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {
        SearchSourceBuilder searchSourceBuilder = getSearchSourceBuilder(pmsSearchParam);
        setHighlight(searchSourceBuilder);
        setSortAndLimit(searchSourceBuilder);
        SearchRequest searchRequest = getSearchRequest(searchSourceBuilder);
        return getPmsSearchSkuInfos(searchRequest);
    }

    private List<PmsSearchSkuInfo> getPmsSearchSkuInfos(SearchRequest searchRequest) {
        SearchResponse searchResponse;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        ArrayList<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit hit : searchHits.getHits()) {
            String sourceAsString = hit.getSourceAsString();
            PmsSearchSkuInfo pmsSearchSkuInfo = JSONObject.parseObject(sourceAsString, PmsSearchSkuInfo.class);

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.size() > 0) {
                HighlightField highlightField = highlightFields.get("skuName");
                Text[] fragments = highlightField.getFragments();
                pmsSearchSkuInfo.setSkuName(fragments[0].string());
            }

            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        return pmsSearchSkuInfos;
    }

    private SearchRequest getSearchRequest(SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(Constants.ES_INDICES);
        searchRequest.types(Constants.ES_TYPE);
        searchRequest.source(searchSourceBuilder);

        return searchRequest;
    }

    private void setSortAndLimit(SearchSourceBuilder searchSourceBuilder) {
        searchSourceBuilder.sort("id", SortOrder.DESC);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);
    }

    private void setHighlight(SearchSourceBuilder searchSourceBuilder) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
    }

    private SearchSourceBuilder getSearchSourceBuilder(PmsSearchParam pmsSearchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] valueId = pmsSearchParam.getValueId();
        if (StringUtils.isNotBlank(keyword)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuName", keyword));
        }
        if (StringUtils.isNotBlank(catalog3Id)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalog3Id", catalog3Id));
        }
        if (valueId != null) {
            for (String id : valueId) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("skuAttrValueList.valueId", id));
            }
        }

        return searchSourceBuilder.query(boolQueryBuilder);
    }
}
