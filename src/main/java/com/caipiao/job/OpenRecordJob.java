package com.caipiao.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caipiao.contants.ConfigProperties;
import com.caipiao.entity.KuaiCai;
import com.caipiao.entity.Plan;
import com.caipiao.entity.Programme;
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

    @Resource(name="conservativeStrategy")
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
                int row = kuaiCaiService.add(kuaiCai);
                System.out.println(row);
                if(row > 0){//触发追号策略
                    createStrategy(kuaiCai);//生成新策略

                    adjust(kuaiCai);//计算旧策略
                }
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    private void createStrategy(KuaiCai kuaiCai){
        //生成新策略
        List<OmissionModel> omissionModels = kuaiCaiService.omissionTop(kuaiCaiService.listAll(false), 3, 10, false);
        for(OmissionModel omissionModel : omissionModels){
            if(omissionModel.getOmissionNum() > 120){
                Programme programme = programmeMapper.find(omissionModel.getCombination());
                if(programme == null){//还未加入计划
                    Programme pp = new Programme();
                    pp.setChasecode(omissionModel.getCombination());
                    pp.setChasestart(omissionModel.getOmissionNum());
                    pp.setExpect(kuaiCai.getExpect());
                    pp.setProfit(0);
                    pp.setState(0);
                    pp.setStoploss(20);
                    pp.setType(3);
                    programmeMapper.insert(pp);

                    Plan plan = Plan.getPre(pp.getId());
                    for(int i =0; i < pp.getStoploss(); ++i){
                        plan = playStrategy.nextStep(plan);
                        planMapper.insert(plan);
                    }
                }
            }
        }
    }

    private void adjust(KuaiCai kuaiCai){
        //计算旧策略
        List<Programme> programmes = programmeMapper.findAll();//所在进行中的策略
        for(Programme pro : programmes){
            String[] codes = pro.getChasecode().split(",");
            List<Plan> plans = planMapper.findByProgramme(pro.getId());
            Plan nextPlan = planMapper.findNextPlan(pro.getId());
            if(contains(kuaiCai.getOpencode(), Arrays.asList(codes))){//开出
                nextPlan.setExpect(kuaiCai.getExpect());
                nextPlan.setState(1);
                nextPlan.setOpencode(kuaiCai.getOpencode());
                planMapper.updateByPrimaryKey(nextPlan);

                pro.setState(1);
                pro.setProfit(nextPlan.getOpenamount() - nextPlan.getTotalamount());
                programmeMapper.updateByPrimaryKey(pro);
            }else{//未开出
                if(nextPlan == null){//止损
                    pro.setState(2);
                    pro.setProfit(nextPlan.getTotalamount());
                    programmeMapper.updateByPrimaryKey(pro);
                }else{//生成下一步计划
                    nextPlan.setExpect(kuaiCai.getExpect());
                    nextPlan.setState(1);
                    nextPlan.setOpencode(kuaiCai.getOpencode());
                    planMapper.updateByPrimaryKey(nextPlan);
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























