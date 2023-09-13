package cn.newstrength.wcms.sysadmin.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.core.constant.Action;
import cn.newstrength.wcms.core.constant.WorkFlowType;
import cn.newstrength.wcms.core.task.Queue;
import cn.newstrength.wcms.sysadmin.flow.model.WorkflowQueueDefinition;
import cn.newstrength.wtdf.plugin.result.TranResult;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WorkflowServiceTests {
	@Autowired
	private Queue<WorkflowQueueDefinition> startWorkflowQueue;

	@Test
	public void config() {
		WorkflowQueueDefinition workflowQueueDefinition = new WorkflowQueueDefinition(2l,WorkFlowType.CHANNEL_ADD_UPDATE,Action.ADD);
		TranResult<Boolean> tranResult = startWorkflowQueue.send("root",workflowQueueDefinition);
		Assert.assertTrue(tranResult.getErrCode()==0);
	}
	
}
