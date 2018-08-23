package com.caipiao.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caipiao.contants.ConfigProperties;
import com.caipiao.entity.KuaiCai;
import com.caipiao.entity.Plan;
import com.caipiao.entity.Programme;
import com.caipiao.mapper.KuaiCaiMapper;
import com.caipiao.mapper.PlanMapper;
import com.caipiao.mapper.ProgrammeMapper;
import com.caipiao.math.combinationUtil;
import com.caipiao.model.CPDataModel;
import com.caipiao.model.OmissionModel;
import com.caipiao.service.KuaiCaiService;
import com.caipiao.strategy.ConservativeStrategy;
import com.caipiao.strategy.IStrategy;
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

    @Resource
    private ProgrammeMapper programmeMapper;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private KuaiCaiMapper kuaiCaiMapper;

    @Resource(name="radicalStrategy")
    private IStrategy playStrategy;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("开始抓取。。。");
        try {
            ResponseEntity<String> entity = restOperations.getForEntity(configProperties.getGd11x5Url(), String.class);
            CPDataModel cpDataModel = JSONObject.parseObject(entity.getBody(), CPDataModel.class);

            log.info(JSON.toJSONString(cpDataModel, true));

            List<KuaiCai> data = cpDataModel.getData();
            for(KuaiCai kuaiCai : data){
                KuaiCai temp = kuaiCaiMapper.selectByExpect(kuaiCai.getExpect());
                if(temp == null){
                    log.info("新开号：{}", JSON.toJSONString(kuaiCai, true));
                    createStrategy(kuaiCai);//生成新策略

                    adjust(kuaiCai);//计算旧策略
                }
                kuaiCaiService.add(kuaiCai);
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    private void createStrategy(KuaiCai kuaiCai){
        //生成新策略
        List<OmissionModel> omissionModels = kuaiCaiService.omissionTop(kuaiCaiService.listAll(false), 3, 10, false);
        for(OmissionModel omissionModel : omissionModels){
            if(omissionModel.getOmissionNum() > 100){
                Programme programme = programmeMapper.find(omissionModel.getCombination());
                if(programme == null){//还未加入计划
                    log.info("有新预期遗漏号：{}", JSON.toJSONString(omissionModel, true));
                    Programme pp = new Programme();
                    pp.setChasecode(omissionModel.getCombination());
                    pp.setChasestart(omissionModel.getOmissionNum());
                    pp.setExpect(kuaiCai.getExpect());
                    pp.setProfit(0);
                    pp.setState(0);
                    pp.setStoploss(10);
                    pp.setType(3);
                    programmeMapper.insert(pp);

                    Plan plan = Plan.getPre(pp.getId());
                    for(int i =0; i < pp.getStoploss(); ++i){
                        plan = playStrategy.nextStep(plan);
                        planMapper.insert(plan);
                    }
                    log.info("新增追号策略：{}", JSON.toJSONString(pp, true));
                }
            }
        }
    }

    private void adjust(KuaiCai kuaiCai){
        //计算旧策略
        List<Programme> programmes = programmeMapper.findAll();//所有进行中的策略
        for(Programme pro : programmes){
            String[] codes = pro.getChasecode().split(",");
            Plan lastPlan = planMapper.findLastPlan(pro.getId());//当前一步
            if(contains(kuaiCai.getOpencode(), Arrays.asList(codes))){//开出
                log.info("追号开出：{}", JSON.toJSONString(kuaiCai, true));
                lastPlan.setExpect(kuaiCai.getExpect());
                lastPlan.setState(1);
                lastPlan.setOpencode(kuaiCai.getOpencode());
                planMapper.updateByPrimaryKey(lastPlan);

                pro.setState(1);
                pro.setProfit(lastPlan.getOpenamount() - lastPlan.getTotalamount());
                programmeMapper.updateByPrimaryKey(pro);
            }else{//未开出
                if(lastPlan == null){//止损
                    Plan endPlan = planMapper.findEndPlan(pro.getId());//最后一步计划
                    pro.setState(2);
                    pro.setProfit(0 - endPlan.getTotalamount());
                    log.info("止损：{}", JSON.toJSONString(pro, true));
                    programmeMapper.updateByPrimaryKey(pro);
                }else{//更新计划
                    lastPlan.setExpect(kuaiCai.getExpect());
                    lastPlan.setState(1);
                    lastPlan.setOpencode(kuaiCai.getOpencode());
                    planMapper.updateByPrimaryKey(lastPlan);
                }
            }
        }
    }

    private boolean contains(String opencode, List<String> combination){
        for(String com : combination){
            if(!opencode.contains(com)){
                return false;
            }
        }
        return true;
    }
}























