package com.caipiao.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caipiao.contants.ConfigProperties;
import com.caipiao.entity.KuaiCai;
import com.caipiao.math.combinationUtil;
import com.caipiao.model.CPDataModel;
import com.caipiao.model.OmissionModel;
import com.caipiao.service.KuaiCaiService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangjj
 * @create 2018-08-13 11:52
 **/
@Slf4j
public class OpenRecordJob extends QuartzJobBean {

    @Resource
    private RestOperations restOperations;

    @Resource
    private ConfigProperties configProperties;

    @Resource
    private KuaiCaiService kuaiCaiService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("开始抓取。。。");
        try {
            ResponseEntity<String> entity = restOperations.getForEntity(configProperties.getGd11x5Url(), String.class);
            JSONObject jsonObject = JSONObject.parseObject(entity.getBody());
            CPDataModel cpDataModel = JSONObject.parseObject(entity.getBody(), CPDataModel.class);

            log.info(JSON.toJSONString(cpDataModel, true));

            List<KuaiCai> data = cpDataModel.getData();
            for(KuaiCai kuaiCai : data){
                kuaiCaiService.add(kuaiCai);
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

       /* List<KuaiCai> allRespo = kuaiCaiService.listAll(false);
        for(KuaiCai kuaiCai : allRespo){
            String opencode = kuaiCai.getOpencode();
            String[] split = opencode.split(",");
            List<String> codeList = Arrays.asList(split);
            Collections.sort(codeList);
            kuaiCai.setOpencodesort(String.join(",", codeList));
            kuaiCaiService.updateOpencodesort(kuaiCai);
            System.out.println(kuaiCai.getExpect());
        }
        System.out.println("---");*/
    }
}























