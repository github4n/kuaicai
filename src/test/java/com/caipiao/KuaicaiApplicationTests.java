package com.caipiao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KuaicaiApplicationTests {

	@Test
	public void contextLoads() {
	}


	/**
	 *
	 *   一副1-108 的数字扑克   执行如下操作
		 1.取最上面一张放到最低下
		 2.取最上面一张放到另一边
		 3.重复1.2的动作 直到纸牌全部放到了另一边，要求取完后纸牌顺序按1-108顺序排好
		 问   原纸牌的排放顺序   怎么用程序实现
	 */
	@Test
	public void test(){
		List<Integer> result = new ArrayList<>();
		for(int i = 1; i <= 108; ++i){
			result.add(i);
		}

	}

}
