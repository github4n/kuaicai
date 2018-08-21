package com.caipiao.service;

import com.caipiao.KuaicaiApplication;
import com.caipiao.entity.KuaiCai;
import com.caipiao.mapper.KuaiCaiMapper;
import com.caipiao.math.combinationUtil;
import com.caipiao.model.OmissionModel;
import com.caipiao.model.SingleFrequencyModel;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangjj
 * @create 2018-08-13 14:10
 **/
@Service
public class KuaiCaiService {

    private final int OPEN_SUM_DAY = 84;//一天开奖次数

    @Resource
    private KuaiCaiMapper kuaiCaiMapper;

    private final static List<List<String>> ALL_LIST_3;

    private final static List<List<String>> ALL_LIST_4;

    private final static List<List<String>> ALL_LIST_5;

    private final static List<List<String>> ALL_LIST_6;

    private final static List<List<String>> ALL_LIST_7;

    private final static List<List<String>> ALL_LIST_8;

    private final static List<List<String>> ALL_LIST_9;

    static{
        //11选3
        ALL_LIST_3 = combinationUtil.combinationSelect(new String[]{"01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10", "11"}, 3);

        //11选4
        ALL_LIST_4 = combinationUtil.combinationSelect(new String[]{"01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10", "11"}, 4);

        //11选5
        ALL_LIST_5 = combinationUtil.combinationSelect(new String[]{"01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10", "11"}, 5);

        //11选6
        ALL_LIST_6 = combinationUtil.combinationSelect(new String[]{"01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10", "11"}, 6);

        //11选7
        ALL_LIST_7 = combinationUtil.combinationSelect(new String[]{"01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10", "11"}, 7);

        //11选8
        ALL_LIST_8 = combinationUtil.combinationSelect(new String[]{"01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10", "11"}, 8);

        //11选9
        ALL_LIST_9 = combinationUtil.combinationSelect(new String[]{"01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10", "11"}, 9);
    }

    @Transactional
    public void add(KuaiCai kuaicai){
        KuaiCai temp = kuaiCaiMapper.selectByExpect(kuaicai.getExpect());
        if(temp == null){
            kuaicai.setOpencodesort(getSortResult(kuaicai.getOpencode()));
            kuaiCaiMapper.insert(kuaicai);
        }
    }

    public List<KuaiCai> listAll(Boolean today){
        String todayStr = null;
        if(today){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            todayStr = sdf.format(new Date());
        }
        return kuaiCaiMapper.selectAll(todayStr);
    }

    public void updateOpencodesort(KuaiCai kuaiCai) {
        kuaiCaiMapper.updateByPrimaryKey(kuaiCai);
    }

    /**
     * 遗漏次数
     * @param allRespo 抓下来的所有历史
     * @param combination 目标组合
     * @author zhangjj
     * @Date 2018/8/13 17:16
     * @return
     * @exception
     *
     */
    private Integer omissionNum(List<KuaiCai> allRespo, List<String> combination){
        Integer num = 0;
        for(KuaiCai com : allRespo){
            if(contains(com.getOpencode(), combination)){
                return num;
            }
            num++;
        }
        if(allRespo.size() < OPEN_SUM_DAY){//基数少于84期则不算
            return 0;
        }
        return num;
    }

    private boolean contains(String opencode, List<String> combination){
        for(String com : combination){
            if(!opencode.contains(com)){
                return false;
            }
        }
        return true;
    }


    public List<OmissionModel> omissionTop(List<KuaiCai> allRespo, Integer type, Integer top, Boolean today){
        List<OmissionModel> list = new ArrayList<>();
        OmissionModel model;
        List<List<String>> listList = null;
        switch (type){
            case 3:
                listList = ALL_LIST_3;
                break;
            case 4:
                listList = ALL_LIST_4;
                break;
            case 5:
                listList = ALL_LIST_5;
                break;
            case 6:
                listList = ALL_LIST_6;
                break;
            case 7:
                listList = ALL_LIST_7;
                break;
            case 8:
                listList = ALL_LIST_8;
                break;
            case 9:
                listList = ALL_LIST_9;
                break;
        }
        for(List<String> combination : listList){
            model = new OmissionModel();
            model.setCombination(String.join(",", combination));
            model.setOmissionNum(omissionNum(allRespo, combination));
            list.add(model);
        }
        Collections.sort(list);
        return list.subList(0, top);
    }


    private String getSortResult(String opencode){
        String[] split = opencode.split(",");
        List<String> codeList = Arrays.asList(split);
        Collections.sort(codeList);
        return String.join(",", codeList);
    }

    public List<SingleFrequencyModel> singleFrequencyTop(List<KuaiCai> allRespo, Boolean today) {
        List<SingleFrequencyModel> topList = new ArrayList<>();
        float total = allRespo.size() * 5;
        List<String> allNumList = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11");
        for(String num : allNumList){
            int count = 0;
            for(KuaiCai kuaiCai : allRespo){
                if(kuaiCai.getOpencode().contains("," + num) ||
                        kuaiCai.getOpencode().contains(num + ",")){
                    count++;
                }
            }
            SingleFrequencyModel singleFrequencyModel = new SingleFrequencyModel();
            singleFrequencyModel.setNumber(num);
            singleFrequencyModel.setCount(count);
            singleFrequencyModel.setPercentage((count / total) * 100 + "%");
            topList.add(singleFrequencyModel);
        }
        Collections.sort(topList);
        return topList;
    }

    public static void main(String[] args) {
        System.out.println((2 / 3F) * 100 + "%");
    }
}
