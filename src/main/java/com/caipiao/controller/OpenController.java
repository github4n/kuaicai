package com.caipiao.controller;

import com.alibaba.fastjson.JSONObject;
import com.caipiao.entity.KuaiCai;
import com.caipiao.model.OmissionModel;
import com.caipiao.model.SingleFrequencyModel;
import com.caipiao.service.KuaiCaiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangjj
 * @create 2018-08-17 16:59
 **/
@RestController
@RequestMapping("/open/")
@Slf4j
public class OpenController {

    @Resource
    private KuaiCaiService kuaiCaiService;

    /**
     * 
     * @param type 任3 4 5
     * @param top 前多少名
     * @author zhangjj
     * @Date 2018/8/17 17:40
     * @return 
     * @exception 
     * 
     */
    @GetMapping("/top")
    public ResponseEntity<JSONObject> top(@RequestParam(required = false, defaultValue = "3") Integer type,
                                          @RequestParam(required = false, defaultValue = "10") Integer top,
                                          @RequestParam(required = false, defaultValue = "false") Boolean today){
        List<KuaiCai> allRespo = kuaiCaiService.listAll(today);
        List<OmissionModel> omissionTop = kuaiCaiService.omissionTop(allRespo, type, top, today);
        for(OmissionModel model : omissionTop){
            log.info("组合{} 遗漏 {} 次", model.getCombination(), model.getOmissionNum());
        }

        JSONObject result = new JSONObject();
        result.put("type", String.format("广东11选%d统计遗漏, today=true表示只统计今天的", type));
        result.put("top", String.format("获取前%d名", top));
        result.put("list", omissionTop);
        return ResponseEntity.ok(result);
    }

    /**
     * 单个数字出现频率由底到高
     * @param
     * @author zhangjj
     * @Date 2018/8/20 14:59
     * @return
     * @exception
     *
     */
    @GetMapping("/singleFrequencyTop")
    public ResponseEntity<JSONObject> singleFrequencyTop(@RequestParam(required = false, defaultValue = "false") Boolean today){
        List<KuaiCai> allRespo = kuaiCaiService.listAll(today);
        int total = allRespo.size() * 5;
        List<SingleFrequencyModel> singleFrequencyTop = kuaiCaiService.singleFrequencyTop(allRespo, today);

        JSONObject result = new JSONObject();
        result.put("total", String.format("基数:%d期，共%d*5=%d个数字", allRespo.size(), allRespo.size(), total));
        result.put("desc", "单个数字出现频率由底到高, today=true表示只统计今天的");
        result.put("list", singleFrequencyTop);
        return ResponseEntity.ok(result);
    }
}
