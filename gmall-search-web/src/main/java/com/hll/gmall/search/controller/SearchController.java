package com.hll.gmall.search.controller;

import com.hll.gmall.api.bean.*;
import com.hll.gmall.api.service.AttrService;
import com.hll.gmall.api.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class SearchController {
    @DubboReference
    SearchService searchService;

    @DubboReference
    AttrService attrService;

    @RequestMapping("index.html")
    public String index() {
        return "index";
    }

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        if (pmsSearchSkuInfos.size() <= 0) {
            return "list";
        }
        modelMap.put("skuLsInfoList", pmsSearchSkuInfos);

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = getBaseAttrBySearchSkuInfos(pmsSearchSkuInfos);
        List<PmsSearchCrumb> pmsSearchCrumbs = getPmsSearchCrumbsAndSetAttrList(pmsBaseAttrInfos, pmsSearchParam);

        modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        modelMap.put("attrList", pmsBaseAttrInfos);

        String urlParam = getUrlParam(pmsSearchParam, null);
        modelMap.put("urlParam", urlParam);

        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            modelMap.put("keyword", keyword);
        }

        return "list";
    }

    private List<PmsSearchCrumb> getPmsSearchCrumbsAndSetAttrList(List<PmsBaseAttrInfo> pmsBaseAttrInfos, PmsSearchParam pmsSearchParam) {
        String[] valueIds = pmsSearchParam.getValueId();
        if (valueIds == null || valueIds.length <= 0) {
            return null;
        }

        List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
        for (String crumbId : valueIds) {
            PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
            pmsSearchCrumb.setValueId(crumbId);

            Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
            while (iterator.hasNext()) {
                PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                for (PmsBaseAttrValue pmsBaseAttrValue : pmsBaseAttrInfo.getAttrValueList()) {
                    if (crumbId.equals(pmsBaseAttrValue.getId())) {
                        pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                        iterator.remove();
                    }
                }
            }

            pmsSearchCrumb.setUrlParam(getUrlParam(pmsSearchParam, crumbId));
            pmsSearchCrumbs.add(pmsSearchCrumb);
        }

        return pmsSearchCrumbs;
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam, String crumbId) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] valueIds = pmsSearchParam.getValueId();

        String urlParam = "";
        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isBlank(urlParam)) {
                urlParam += "keyword=" + keyword;
            } else {
                urlParam += "&keyword=" + keyword;
            }
        }

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isBlank(urlParam)) {
                urlParam += "catalog3Id=" + catalog3Id;
            } else {
                urlParam += "&catalog3Id=" + catalog3Id;
            }
        }

        if (valueIds != null && valueIds.length > 0) {
            for (String valueId : valueIds) {
                if (valueId.equals(crumbId)) {
                    continue;
                }

                if (StringUtils.isBlank(urlParam)) {
                    urlParam += "valueId=" + valueId;
                } else {
                    urlParam += "&valueId=" + valueId;
                }
            }
        }

        return urlParam;
    }

    private List<PmsBaseAttrInfo> getBaseAttrBySearchSkuInfos(List<PmsSearchSkuInfo> pmsSearchSkuInfos) {
        Set<String> valueIdsSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            for (PmsSkuAttrValue pmsSkuAttrValue : pmsSearchSkuInfo.getSkuAttrValueList()) {
                valueIdsSet.add(pmsSkuAttrValue.getValueId());
            }
        }

        return attrService.getBaseAttrByValueIds(valueIdsSet);
    }

    @RequestMapping("importSkuToIndices.html")
    @ResponseBody
    public String importSkuToIndices() {
        return searchService.importSkuToIndices();
    }

    @RequestMapping("delSkuFromIndices.html")
    @ResponseBody
    public String delSkuFromIndices() {
        return searchService.delSkuFromIndices();
    }
}
