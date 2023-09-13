/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */
package cn.newstrength.flow.sample.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.newstrength.flowable.core.ActionContext;
import cn.newstrength.flowable.service.FlowableService;
import cn.newstrength.wtdf.plugin.result.SuccessTranResult;
import cn.newstrength.wtdf.plugin.result.TranResult;
/**
 * 自定义Service sample
 * <ol>
 * <li>nsms-flowable内置了流程常用的接口（<a href="http://121.52.220.132:8181/login">详见接口文档</a>），如无法满足项目组内需求，可以自定义接口实现</li>
 * <li>Service由Spring管理，所以可注入Flowable开放API；如 RuntimeService、TaskService等</li>
 * <li>FlowableService接口为泛型，返回值依据项目需求自定义</li>
 * @author Xd
 * 
 * @see //ServiceConfiguration.java
 *
 */
@Service(value = "sampleService")
public class SampleService implements FlowableService<Map<String, Object>> {
	@Override
	public TranResult<Map<String, Object>> excute(ActionContext actionContext) {
		return new SuccessTranResult<>(actionContext.getVariables());
	}
}
