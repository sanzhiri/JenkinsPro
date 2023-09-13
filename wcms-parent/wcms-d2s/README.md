#  WCMS 动转静
##  动转静说明
CMS在操作如下功能时会产生动转静任务和生成模板规则:
    (1)添加栏目     栏目首页、栏目列表、站点首页、全站其它      
    (2)复制栏目     栏目首页、栏目列表、站点首页、全站其它
    (3)删除栏目     栏目首页、栏目列表、站点首页、全站其它  回收：删除栏目的首页、列表、信息、其它静态页面
    (4)修改栏目     栏目首页、栏目列表、站点首页、全站其它
    (5)已生效栏目   栏目首页、栏目列表、站点首页、全站其它     
    (6)引用栏目     栏目首页、栏目列表、站点首页、全站其它
    (7)移动栏目     栏目A移动到栏目B  1.生成栏目B：站点首页、全站其它、栏目首页、栏目列表  2.被移动栏目的栏目首页、列表、详情 3.删除被移动栏目下的所有静态页面
    (8)已失效栏目   全站其它  删除失效栏目下所有目录和文件
    (9)添加信息     栏目首页、栏目列表、站点首页、全站其它
    (10)复制信息    栏目首页、栏目列表、站点首页、全站其它
    (11)删除信息     栏目首页、栏目列表、站点首页、全站其它
    (12)已失效信息   栏目首页&栏目列表   删除失效信息html
    (13)移动信息     栏目首页、栏目列表、站点首页、全站其它  重新生成移动的信息,删除原栏目下的信息
    (14)下线信息     栏目首页、栏目列表、站点首页、全站其它  删除栏目下信息html
    (15)上线信息     栏目首页、栏目列表、站点首页、全站其它
    (16)引用信息      栏目首页、栏目列表、站点首页、全站其它、被引用信息
    (17)置顶失效信息   栏目首页&栏目列表&站点首页&站点所有其它
    (18)置顶生效信息   栏目首页&栏目列表  站点首页&站点所有其它
    (19)修改信息      栏目首页、栏目列表、站点首页、全站其它  删除当前栏目信息静态页面   
    (20)已生效信息    站点首页、全站其它
## 步骤一 cms在对栏目和信息操作时（参考上面动转静说明）会在redis队列中存储需要生成动转静任务数据如下:
        redis key: cn:newstrength:wcms:sysadmin:queue:d2s    
                例如：添加信息value值如下:
                value:[
                    "java.util.HashMap",
                    {
                        "infoId": [
                            "java.lang.Long",
                            33906
                        ],
                        "loginId": "root",
                        "siteId": [
                            "java.lang.Long",
                            1
                        ],
                        "eventType": "channel_info_add",
                        "taskId": "59ee9def20a2429dbd1c1524e3619e17",
                        "channelId": [
                            "java.lang.Long",
                            48
                        ]
                    }
                ]
## 步骤二:  动转静事件定时任务-拆包（d2sEventJobHandler）每15秒(可配置)轮流获取redis key:cn:newstrength:wcms:sysadmin:queue:d2s 队列中10条生成动转的事件消息
    定时任务执行器类: 
        cn.newstrength.wcms.job.executor.jobhandler.D2sEventJob  方法：d2sEventJobHandler
    定时任务执行器说明:根据动转静事件消息存储的策略（redis、 kafka）不同处理方式不一样 
      (1）获取redis消息队列中动转静事件消息:  类：cn.newstrength.wcms.d2s.strategy.service.impl.StorageRedis   方法：d2sEventExecute
     （2）获取kafka消息队列中动转静事件消息： 类：cn.newstrength.wcms.d2s.strategy.service.impl.StorageKafka    方法：d2sEventExecute
    定时器执行任务详细步骤:
      (1)将消费的动转静事件消息(Map)，根据事件消息类型转换为DTO对象 DTO对象包路径：cn.newstrength.wcms.d2s.dto.channel、cn.newstrength.wcms.d2s.dto.info
      (2)根据动转静事件类型拆分需要生成哪些模板文件(策略模式),并转化为D2sMessage消息并打上优先级别 比如：添加栏目事件 需要生成：栏目首页、栏目列表、站点首页、全站其它模板,对应的拆分逻辑类：cn.newstrength.wcms.d2s.strategy.service.impl.ChannelAddUnpack
          优先级别说明：信息详情>其它模板>栏目列表>栏目首页>站点首页
          AOP切面：cn.newstrength.wcms.d2s.strategy.handler.D2sEventStatisticfsHandler  
              拆包前：1.将动转静事件消息insert->tbl_wcms_d2s_event_log 2.设置动转事件ID
              拆包后：1.插入动转静事件: 创建人、任务类型、状态(未执行)、创建时间  2.更新动转静事件状态为:执行中  3.将此次动转静事件拆包的的消息加上开始和结束标识
      (3)将动转静D2sMessage消息转化为生成文件的原数据Message
          存入redis文件消息池中(set集合): key:cn:newstrength:wcms:d2s:pool:[D2sMessge.getKey]       value:Message 对象  有效时间:24 小时
          存入redis文件消息优先级对列中（zset） key:newstrength:wcms:d2s:priority     value：Message.getId   score:Message.getPriority
## 步骤三:  监控动转静批量处理的频率（d2sFileMonitorJobHandler）定时任务，每10秒轮询执行
     定时任务执行器类：
       cn.newstrength.wcms.job.executor.jobhandler.D2sFileMonitorJob   方法：d2sFileMonitorJobHandler
     定时器执行任务详细步骤:
        每次轮询将优先级消息：newstrength:wcms:d2s:priority 放入等待生成文件队列：cn:newstrength:wcms:d2s:waitconsume （队列）中每次放入200条
## 步骤四：生成静态页面消息（d2sFileJobHandler）定时任务，每10秒轮询执行
     定时任务执行器类：
        cn.newstrength.wcms.job.executor.jobhandler.D2sFileJob   方法：d2sFileJobHandler
    定时器执行任务详细步骤:
        （1）获取等待生成文件队列：cn:newstrength:wcms:d2s:waitconsume中的全部消息数据
         (2) 通过获取等待队列的key 从redis消息池（cn:newstrength:wcms:d2s:pool）中获取D2sMessage，并返回D2sMessage消息集合
        （3） 循环处理需要生成的静态页面消息：
               不是结束消息：调用GenerateHtmlServiceImpl.d2sMessageConvertHtml  生成静态页面， 将此次事件错误数累加存储redis：cn:newstrength:wcms:d2s:error （increment）
               是此次事件结束消息：更新对应的动转静事件信息状态为已结束
        (4) 批量插入生成文件记录：tbl_wcms_d2s_file_log
                