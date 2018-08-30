package com.caipiao.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caipiao.contants.ConfigProperties;
import com.caipiao.entity.KuaiCai;
import com.caipiao.entity.OmissionPeak;
import com.caipiao.entity.Plan;
import com.caipiao.entity.Programme;
import com.caipiao.mapper.KuaiCaiMapper;
import com.caipiao.mapper.OmissionPeakMapper;
import com.caipiao.mapper.PlanMapper;
import com.caipiao.mapper.ProgrammeMapper;
import com.caipiao.model.CPDataModel;
import com.caipiao.model.OmissionModel;
import com.caipiao.service.KuaiCaiService;
import com.caipiao.strategy.IStrategy;
import com.caipiao.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangjj
 * @create 2018-08-13 11:52
 **/
@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
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

    @Resource
    private OmissionPeakMapper omissionPeakMapper;

    @Resource(name="radicalStrategy")
    private IStrategy playStrategy;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("开始抓取。。。");
        try {
            ResponseEntity<String> entity = restOperations.getForEntity(configProperties.getGd11x5Url(), String.class);
            log.info("body -------> " + entity.getBody());
            CPDataModel cpDataModel = JSONObject.parseObject(entity.getBody(), CPDataModel.class);

            log.info(JSON.toJSONString(cpDataModel, true));

            List<KuaiCai> data = cpDataModel.getData();
            for(KuaiCai kuaiCai : data){
                KuaiCai temp = kuaiCaiMapper.selectByExpect(kuaiCai.getExpect());
                if(temp == null){
                    log.info("新开号：{}", JSON.toJSONString(kuaiCai, true));
                    calcuOmissionPeak(kuaiCai, 3, 100);
                    calcuOmissionPeak(kuaiCai, 4, 400);
                    createStrategy(kuaiCai, 3, 100);//生成新策略
                    createStrategy(kuaiCai, 4, 500);//生成新策略

                    adjust(kuaiCai);//计算旧策略

                    kuaiCaiService.add(kuaiCai);
                }
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算峰值
     * @param
     * @author zhangjj
     * @Date 2018/8/24 10:40
     * @return
     * @exception
     *
     */
    private void calcuOmissionPeak(KuaiCai kuaiCai, Integer type, Integer obPoint){
        List<OmissionModel> omissionModels = kuaiCaiService.
                omissionTop(kuaiCaiService.listAll(false), type, 10, false);
        if(omissionModels == null || omissionModels.isEmpty()) return ;
        for(OmissionModel omissionModel : omissionModels){
            if(omissionModel.getOmissionNum() > obPoint){
                log.info("{} : 符合条件的峰值，任{}, 大于观察值{}", omissionModel.getCombination(), type, obPoint);
                OmissionPeak omissionPeak = omissionPeakMapper.find(omissionModel.getCombination(), 0);
                if(omissionPeak == null &&
                        !contains(kuaiCai.getOpencode(),
                                Arrays.asList(omissionModel.getCombination().split(",")))){//未记录，并且当期未开出
                    log.info("组合{}，新加入", omissionModel.getCombination());
                    OmissionPeak op = new OmissionPeak();
                    op.setCurrentomissionnum(omissionModel.getOmissionNum() + 1);//+1为加上本次开出未中
                    op.setOmissioncode(omissionModel.getCombination());
                    op.setStartexpect(kuaiCai.getExpect());
                    op.setStartomissionnum(omissionModel.getOmissionNum());
                    op.setState(0);
                    op.setType(type);
                    op.setUpdatetime(DateTimeUtil.currentDateTime());
                    omissionPeakMapper.insert(op);
                }else{
                    log.info("更新组合{}，观察情况", omissionModel.getCombination());
                    if(contains(kuaiCai.getOpencode(), Arrays.asList(omissionPeak.getOmissioncode().split(",")))){//开出
                        log.info("观察的组合{}，已经在第{}期开出，开出号为{}",
                                omissionModel.getCombination(), kuaiCai.getExpect(), kuaiCai.getOpencode());
                        omissionPeak.setUpdatetime(DateTimeUtil.currentDateTime());
                        omissionPeak.setOpenExpect(kuaiCai.getExpect());
                        omissionPeak.setState(1);
                        omissionPeakMapper.updateByPrimaryKey(omissionPeak);
                    }else{//未开出
                        log.info("更新组合{}，未开出", omissionModel.getCombination());
                        omissionPeak.setCurrentomissionnum(omissionPeak.getCurrentomissionnum() + 1);
                        omissionPeak.setUpdatetime(DateTimeUtil.currentDateTime());
                        omissionPeakMapper.updateByPrimaryKey(omissionPeak);
                    }
                }
            }
        }
    }

    private void createStrategy(KuaiCai kuaiCai, Integer type, Integer obPoint){
        List<OmissionModel> omissionModels = kuaiCaiService.
                omissionTop(kuaiCaiService.listAll(false), type, 10, false);
        //生成新策略
        for(OmissionModel omissionModel : omissionModels){
            if(omissionModel.getOmissionNum() > obPoint){
                Programme programme = programmeMapper.find(omissionModel.getCombination());
                if(programme == null &&
                        !contains(kuaiCai.getOpencode(),
                                Arrays.asList(omissionModel.getCombination().split(",")))){//还未加入计划并且当期未开出
                    log.info("有新预期遗漏号：{}", JSON.toJSONString(omissionModel, true));
                    Programme pp = new Programme();
                    pp.setChasecode(omissionModel.getCombination());
                    pp.setChasestart(omissionModel.getOmissionNum());
                    pp.setExpect(kuaiCai.getExpect());
                    pp.setProfit(0);
                    pp.setState(0);
                    pp.setStoploss(10);
                    pp.setType(type);
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























