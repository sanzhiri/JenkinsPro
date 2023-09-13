package cn.newstrength.wcms.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageRowBounds;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import cn.newstrength.wtdf.plugin.result.FailureTranResult;
import cn.newstrength.wtdf.plugin.result.PageableResult;
import cn.newstrength.wtdf.plugin.result.SuccessTranResult;
import cn.newstrength.wtdf.plugin.result.TranResult;
import cn.newstrength.wtdf.plugin.service.RedisService;
import cn.newstrength.wtdf.plugin.util.TranUtils;
import cn.newstrength.nsms.exception.ServiceException;
import cn.newstrength.wcms.content.Info;
import cn.newstrength.wcms.content.api.constant.FileType;
import cn.newstrength.wcms.content.api.dto.Content;
import cn.newstrength.wcms.core.constant.BizType;
import cn.newstrength.wcms.core.constant.Constants;
import cn.newstrength.wcms.d2s.api.helper.D2sHelper;
import cn.newstrength.wcms.info.api.service.InfoService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Count;
import io.searchbox.core.CountResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.ValueCountAggregation;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;

@Service
public class EsInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(EsInfoService.class);

	@Autowired
	public JestClient jestClient;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private InfoService infoService;
	
	private static String indexName = "wcms_info";
	private static String typeName = "_doc";
	private static LinkedTreeMap<String, String> formatKeyMap = null;
	
	
	/**
	 * 查询ES数据，原始内容
	 * 
	 * @param queryMap 条件
	 * @param columns  列
	 * @return
	 */
	public TranResult<PageableResult<Map<String,Object>>> getDocument(Map queryMap) {

		
		
		Map<String, String> inputQuery = new HashMap();
		inputQuery.putAll(queryMap);
		System.out.println("列表查询入参：" + inputQuery);


		List<Info> resList = new ArrayList<>();
		long total = 0;

		String token = MapUtils.getString(inputQuery, "token", "");// token
		String searchKey = MapUtils.getString(inputQuery, "searchKey", "");// 指定搜索字段
		String searchText = MapUtils.getString(inputQuery, "searchText", "");// 搜索 关键字查询
		Integer siteId = MapUtils.getInteger(inputQuery, "siteId", -1);
		if(siteId == -1) {
			return new FailureTranResult<>("查询失败");
		}
		
		int pageSize = MapUtils.getIntValue(inputQuery, "pageSize", 30);// 搜索 关键字查询
		int currentPage = MapUtils.getIntValue(inputQuery, "currentPage", 1);// 搜索 关键字查询

		inputQuery.remove("token");
		inputQuery.remove("searchText");
		inputQuery.remove("numPerPage");
		inputQuery.remove("page");

		List<Map> channelGroup = new ArrayList<>();
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("list", resList);
		resMap.put("group", channelGroup);
		
		PageableResult<Map<String,Object>> pr = new PageableResult<>();
		pr.setCurrentPage(currentPage);// 当前页
		pr.setPageSize(pageSize);// 每页显示
		pr.setRowCount(total);
		pr.setPageCount(0);
		pr.setData(resMap);// 当前页结果集
		
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();// 总查询
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();// 其它条件查询

		// 构造搜索查询，多字段or
		BoolQueryBuilder searchTextQueryBuilder = QueryBuilders.boolQuery();// 搜索-多字段
		
		if(StringUtils.isNotEmpty(searchKey)) {
			if(searchKey.equals("id")) {
				searchTextQueryBuilder.must(QueryBuilders.termQuery("id", searchText));//信息id
			}else {
				searchTextQueryBuilder.must(QueryBuilders.matchQuery(searchKey, searchText).operator(Operator.AND));// 指定字段-全文检索
			}
		}else {
			if (StringUtils.isNotEmpty(searchText)) {
				searchTextQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));// 正文-全文检索
				searchTextQueryBuilder.should(QueryBuilders.matchQuery("attachment", searchText).operator(Operator.AND));//附件-全文检索
				searchTextQueryBuilder.should(QueryBuilders.matchQuery("name_path", searchText).operator(Operator.AND));//栏目-全文检索
				searchTextQueryBuilder.should(QueryBuilders.matchQuery("keyword", searchText).operator(Operator.AND));//keyword-全文检索
				searchTextQueryBuilder.should(QueryBuilders.matchQuery("subtitle", searchText).operator(Operator.AND));//subtitle-全文检索
				searchTextQueryBuilder.should(QueryBuilders.matchQuery("title", searchText).operator(Operator.AND));//title-全文检索
				if (NumberUtils.isCreatable(searchText)) {
					searchTextQueryBuilder.should(QueryBuilders.termQuery("id", searchText));//信息id
				}
			}
		}
		
		queryBuilder.must(QueryBuilders.termQuery("site_id", siteId));
		
		//TODO 默认查询密级为0的，可根据token进行扩展
		queryBuilder.must(QueryBuilders.termQuery("security_level", 0));
		
		//装接总filter
		BoolQueryBuilder postFilterBool = QueryBuilders.boolQuery().must(searchTextQueryBuilder).must(queryBuilder);
		
		searchSourceBuilder.query(postFilterBool);
		
		
	
		logger.info("count查询ES条件index:" + searchSourceBuilder.toString());
		
		// 获取总数
		try {
			total = this.queryCount(jestClient, searchSourceBuilder);
		} catch (Exception e1) {
			logger.error("ES库中没有要查询的索引库或数据：" + indexName);
			return new SuccessTranResult<>(pr);
		}

		//---------------处理栏目分组后的数量-begin---------------
		AggregationBuilder aggregationBuilder = 
				AggregationBuilders.terms("parent_id").field("parent_id").size(Integer.MAX_VALUE) //1
				     .subAggregation(AggregationBuilders.terms("channel_id").field("channel_id").size(Integer.MAX_VALUE)); //2
		searchSourceBuilder.aggregation(aggregationBuilder);
		//---------------处理栏目分组后的数量-end---------------
			
		
		String[] includes = {};// 指定查询列
		String[] excludes = {};// 指定排除列

		searchSourceBuilder.fetchSource(includes, excludes).from(pageSize * (currentPage - 1)).size(pageSize);
		searchSourceBuilder.sort("update_time", SortOrder.DESC);
		
		
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("name_path");//高亮name_path
		highlightBuilder.field("keyword");
		highlightBuilder.field("content");
		highlightBuilder.field("attachment");
		highlightBuilder.field("title");
		highlightBuilder.field("subtitle");
		highlightBuilder.preTags("<font color='red'>").postTags("</font>");//高亮标签
		highlightBuilder.fragmentSize(500);//高亮内容长度
		
		searchSourceBuilder.highlighter(highlightBuilder);
		logger.info("列表查询ES条件 filter:" + searchSourceBuilder.toString());
		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(indexName)
				.addType(typeName)
				.build();
		try {

			SearchResult result = jestClient.execute(search);

			// 判断返回结果状态
			if (result.getResponseCode() != 200) {
				logger.error(result.getErrorMessage());
				return new FailureTranResult<>("查询失败");
			}
			
			GsonBuilder builder = new GsonBuilder();
	        builder.registerTypeAdapter(new TypeToken<Map<String,Object>>(){}.getType(),new MapTypeAdapter());
	        Gson gson = builder.create();
	        this.getKeyMap();
		    //System.out.println("result.getSourceAsStringList():" + result.getSourceAsStringList().size());
	        List<String> rowJsonList = result.getSourceAsStringList();
	        
	        List<Hit<Info,Void>> hits = result.getHits(Info.class);
	        
	        for (Hit<Info, Void> hit : hits) {
				
	        	Info source = hit.source;
				//获取高亮后的内容
				Map<String, List<String>> highlight = hit.highlight;
				
				if(highlight != null) {
					List<String> views = highlight.get("name_path");//高亮后的name_path
					if(views!=null){
						source.setName_path(views.get(0));
					}
					
					List<String> keywordViews = highlight.get("keyword");//高亮后的keyword
					if(keywordViews!=null){
						source.setKeyword(keywordViews.get(0));
					}
					
					List<String> contentViews = highlight.get("content");//高亮后的content
					if(contentViews!=null){
						source.setContent(contentViews.get(0));
					}
					
					List<String> attachmentViews = highlight.get("attachment");//高亮后的attachment
					if(attachmentViews!=null){
						source.setAttachment(attachmentViews.get(0));
					}
					
					List<String> titleViews = highlight.get("title");//高亮后的title
					if(titleViews!=null){
						source.setTitle(titleViews.get(0));
					}
					
					List<String> subtitleViews = highlight.get("subtitle");//高亮后的subtitle
					if(attachmentViews!=null){
						source.setSubtitle(subtitleViews.get(0));
					}
				}
			
				
				
				
				if(source.getKeyword() == null) {
					source.setKeyword("--");
				}
				if(source.getContent() == null) {
					source.setContent("暂无");
				}else {
					String content = source.getContent();
					if(content.length() > 200) {
						source.setContent(source.getContent().substring(0,200) + "...");
					}
					
				}
				//TODO 需优化
//				String staticPath = D2sHelper.getInfoStaticPath(source.getSite_id(), source.getChannel_id(), source.getId(), source.getCreate_time());
//				source.setStatic_path(staticPath);
//				System.out.println("标题："+source.getTitle());
//				System.out.println("内容："+source.getContent());
//				System.out.println("栏目："+source.getName_path());
				resList.add(source);
			}

	        /*
	         * 处理栏目分组结果
	         */
	        List<TermsAggregation.Entry> channelAgg = 
					  result.getAggregations().getTermsAggregation("parent_id").getBuckets();//首先取最外层的聚合，拿到桶
			
			
			
			for (TermsAggregation.Entry entry : channelAgg) { //循环第一层的每一个桶，拿到里面的聚合结果
				//System.out.println(entry.getKeyAsString() + "==>" + entry.getCount());
				
				String parentChannelId = entry.getKeyAsString();//分组的key值
				Long parentCount = entry.getCount();//自带的count值
				
				Map parentMap = new HashMap();
				parentMap.put("id", parentChannelId);
				//TODO 待优化
				parentMap.put("name", "**");
				parentMap.put("count", parentCount);
			
				List<TermsAggregation.Entry> childAgg = entry.getTermsAggregation("channel_id").getBuckets();
				List<Map<String,Object>> childList = new ArrayList<>();
				for (TermsAggregation.Entry childEntry : childAgg) { //循环第二层的每一个桶，拿到里面的聚合结果
					Map chlidMap = new HashMap();
					String childChannelId = childEntry.getKeyAsString();//分组的key值
					Long childCount = childEntry.getCount();
					chlidMap.put("id", childChannelId);
					chlidMap.put("name", "**");
					chlidMap.put("count", childCount);
					childList.add(chlidMap);
					//System.out.println("	" + childChannelId + "-->" + childCount);
			   }    
			   parentMap.put("child", childList);
			   channelGroup.add(parentMap);
			}
	        
			
			
			int pageCount = (int) ((total - 1) / pageSize + 1);// 总页数
			// 将全部分页信息，重新设置到结果集中
			pr.setCurrentPage(currentPage);// 当前页
			pr.setPageSize(pageSize);// 每页显示
			pr.setRowCount(total);// 总记录数
			pr.setPageCount(pageCount);// 总页数
			pr.setData(resMap);// 当前页结果集
			System.out.println(TranUtils.toJson(resList));

		} catch (IOException e) {
			return new FailureTranResult<PageableResult<Map<String,Object>>>("查询失败");
		}
		logger.info("查询获得数据：" + total + ",当前页数据条数：" + resList.size());
		
		return new SuccessTranResult<>(pr);

	}
 
	/**
	 * 根据组合条件查询ES记录数
	 * 
	 * @param searchSourceBuilder
	 * @return
	 * @throws IOException
	 */
	public static long queryCount(JestClient jestClient, SearchSourceBuilder searchSourceBuilder)  {

		Count count = new Count.Builder().query(searchSourceBuilder.toString()).addIndex(indexName).build();
		CountResult result;
		try {
			result = jestClient.execute(count);
			return result.getCount().longValue();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}

	//测试方法，不提供外部接口调用
	public void getChannelGroup() throws IOException {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
//		        .must(QueryBuilders.matchQuery("title", "产品"));
//		searchSourceBuilder.query(queryBuilder);
		AggregationBuilder aggregationBuilder = 
				AggregationBuilders.terms("parent_id").field("parent_id").size(Integer.MAX_VALUE) //1
				     .subAggregation(AggregationBuilders.terms("channel_id").field("channel_id").size(Integer.MAX_VALUE)); //2
				         // .subAggregation(AggregationBuilders.avg("ageAgg").field("age")) //3
				          //)     .subAggregation(AggregationBuilders.count("totalNum").field("channel_id"))); //4
		searchSourceBuilder.aggregation(aggregationBuilder);
		
		//ValueCountAggregationBuilder valueCountAggregationBuilder = AggregationBuilders.count("channel_id").field("channel_id");
        //searchSourceBuilder.aggregation(valueCountAggregationBuilder);
		
		
		String query = searchSourceBuilder.toString();
		Search search = new Search.Builder(query).addIndex("wcms_info").build();
		SearchResult result = jestClient.execute(search);
		System.out.println("----------------------------------------------------");
		List<TermsAggregation.Entry> channelAgg = 
				  result.getAggregations().getTermsAggregation("parent_id").getBuckets();//首先取最外层的聚合，拿到桶
		
		List<Map> channelGroup = new ArrayList<>();
		
		for (TermsAggregation.Entry entry : channelAgg) { //循环第一层的每一个桶，拿到里面的聚合结果
			//System.out.println(entry.getKeyAsString() + "==>" + entry.getCount());
			
			String parentChannelId = entry.getKeyAsString();//分组的key值
			Long parentCount = entry.getCount();//自带的count值
			
			Map parentMap = new HashMap();
			parentMap.put("id", parentChannelId);
			parentMap.put("name", "**");
			parentMap.put("count", parentCount);
		
			List<TermsAggregation.Entry> childAgg = entry.getTermsAggregation("channel_id").getBuckets();
			List<Map<String,Object>> childList = new ArrayList<>();
			for (TermsAggregation.Entry childEntry : childAgg) { //循环第二层的每一个桶，拿到里面的聚合结果
				Map chlidMap = new HashMap();
				String childChannelId = childEntry.getKeyAsString();//分组的key值
				Long childCount = childEntry.getCount();
				chlidMap.put("id", childChannelId);
				chlidMap.put("name", "**");
				chlidMap.put("count", childCount);
				childList.add(chlidMap);
				//System.out.println("	" + childChannelId + "-->" + childCount);
		   }    
		   parentMap.put("child", childList);
		   channelGroup.add(parentMap);
		}
		
		Gson gson = new Gson();
		String channelGroupJson = gson.toJson(channelGroup);
		System.out.println(channelGroupJson);
	}
	
	public TranResult<Boolean> addDocument(Map inputQuery){
		
		String type = MapUtils.getString(inputQuery, "type", "increment");// 指定搜索字段,默认增量
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String queryStr = sf.format(new Date());
		Map queryMap = new HashMap();
		Date endTime = new Date();
		
		if("increment".equals(type)) {
			//增量，获取增量时间
			Map map = sqlSessionTemplate.selectOne("wcms.searcher.time.query");
			Date updateTime = (Date)MapUtils.getObject(map, "update_time", null);//如果没有给当前时间
			
			queryMap.put("endTime", endTime);
			//第一次没有时间，放入当前时间
			if(map == null) {
				sqlSessionTemplate.insert("wcms.searcher.time.create",queryMap);
			}else {
				//startTime有值时，才进行时间范围查询
				queryMap.put("startTime", updateTime);
				//更新时间
				sqlSessionTemplate.update("wcms.searcher.time.update",queryMap);
			}
		}else {
			Map map = sqlSessionTemplate.selectOne("wcms.searcher.time.query");
			queryMap.put("endTime", endTime);
			if(map == null) {
				//创建时间
				sqlSessionTemplate.insert("wcms.searcher.time.create",queryMap);
			}else {
				//更新时间
				sqlSessionTemplate.update("wcms.searcher.time.update",queryMap);
			}
			
		}
		
		int count = sqlSessionTemplate.selectOne("wcms.searcher.info.queryPublishedInfoCount", queryMap);
		//System.out.println("===>" + count);
		
		
		Integer currentPage = 1;
	    Integer pageSize = 1000;
		
	    int total = 0;
	    int succTotal = 0;
	    int pageCount = 10;
		while(pageCount >= currentPage) {
			int offset = (currentPage.intValue() - 1) * pageSize.intValue();
		    PageRowBounds prb = new PageRowBounds(offset, pageSize.intValue());
		    List<Info> list = this.sqlSessionTemplate.selectList("wcms.searcher.info.queryPublishedInfo", queryMap, prb);
		    for(Info info : list) {
		    	total ++;
		    	// API获取：附件列表，正文内容
 		    	cn.newstrength.wcms.info.api.dto.Info infoSrc = null;
				try {
					infoSrc = infoService.createInfoQuery().infoId(info.getId()).securityLevel(info.getSecurity_level()).singleResult();
					info.setDynamic_path(infoSrc.getUrl());
					info.setStatic_path(infoSrc.getUrl());
			    	List<Content> attachments = infoSrc.getAttachments();
			    	List<String> strs = new ArrayList<>();
			    	for(Content attachment : attachments) {
			    		if(attachment.getFileType().equals(FileType.ATTACHMENT.getType())) {
			    			strs.add(attachment.getOriginalName());
			    		}
			    	}
			    	info.setAttachment(StringUtils.join(strs, ","));
			    	String content = this.delHTMLTag(infoSrc.getContent());
			    	info.setContent(content);
			    	info.setUpdate_time(infoSrc.getUpdateTime());
			    	info.setCreate_time(infoSrc.getCreateTime());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					logger.error("获取信息API出错：channelId：{},infoId:{}",info.getChannel_id(),info.getId());
					//e1.printStackTrace();
				}
				//info.setStatic_path(infoSrc.getStaticPath());
				
		    	logger.info("开始创建信息，ID:{},标题:{}",info.getId(),info.getTitle());
				Index action = new Index.Builder(info)
		    			.id(info.getSite_id() + "_" + info.getId())//设置主键
		    			.index(indexName).type(typeName).build();
		    	try {
		    		JestResult jestResult  = jestClient.execute(action);
		    		succTotal ++;
		    		logger.info("createIndex:{}" + jestResult.isSucceeded());
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		    pageCount = (int)((prb.getTotal().longValue() - 1L) / pageSize.intValue() + 1L);
		    currentPage ++;
		}
		
		logger.info("增量创建索引完成，共{}条，成功{}条",total,succTotal);
		
		return new SuccessTranResult<>(true);
	}
	
	public TranResult<Boolean> deleteDocument(){
		
		String deleteQueueKey = "cn:newstrength:wcms:info:delete";
		for (int i = 0; i < 5000; i++) {//这里的1000是每次POP的数量
		    Object object = redisService.rightPop(Constants.RedisKey.SEARCHER_QUEUE);//这里看王瑞存入Redis的数据对象是什么
		    if(object!=null) {
		        Map<String,Object> data = (Map<String, Object>) object;
		        //ES 逻辑
		        Object siteId = data.get("siteId");
		        Object infoId = data.get("infoId");
		        
		        String delId = siteId + "_" + infoId;
		    	Delete delete = new Delete.Builder(delId).index(indexName).type(typeName).build();
		    	try {
		    		JestResult jestResult  = jestClient.execute(delete);
		    		logger.info("deleteIndex:{},result:{}", delId, jestResult.isSucceeded());
		    		if (jestResult != null && jestResult.isSucceeded()) {
					}else {
						
					}
		        } catch (IOException e) {
		        	logger.error("删除索引单条记录失败：{}", delId); 
					redisService.rightPush(Constants.RedisKey.SEARCHER_QUEUE, data);
		        }
		    }
		}
		
		
			    
//	    List<Info> list = this.sqlSessionTemplate.selectList("wcms.searcher.info.queryPublishedInfo");
//	    for(Info info : list) {
//	    	System.out.println(info.getTitle());
//	    	String delId = info.getSite_id() + "_" + info.getId();
//	    	Delete delete = new Delete.Builder(delId).index(indexName).type(typeName).build();
//	    	try {
//	    		JestResult jestResult  = jestClient.execute(delete);
//	    		logger.info("deleteIndex:{},result:{}", delId, jestResult.isSucceeded());
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
		
		return new SuccessTranResult<>(true);
	}
	
	
	
	
	
	/**
	 * 创建索引
	 *
	 * @param index 索引名称
	 * @throws IOException 
	 */
	public TranResult<Boolean> createIndex()  {
		boolean result = false;
		Map<String,Object> mappings = new HashMap<>();
		try {
			
			Map map = new HashMap();
			String mappingsString = "{\r\n" + 
					"      \"properties\": {\r\n" + 
					"      	\"id\": {\r\n" + 
					"          \"type\": \"long\"\r\n" + 
					"        },\r\n" + 
					"        \"site_id\": {\r\n" + 
					"          \"type\": \"long\"\r\n" + 
					"        },\r\n" + 
					"        \"channel_id\": {\r\n" + 
					"          \"type\": \"long\"\r\n" + 
					"        },\r\n" + 
					"        \"title\": {\r\n" + 
					"          \"type\": \"text\",\r\n" + 
					"          \"analyzer\": \"ik_max_word\"\r\n" + 
					"        },\r\n" + 
					"        \"subtitle\": {\r\n" + 
					"          \"type\": \"text\",\r\n" + 
					"          \"analyzer\": \"ik_max_word\"\r\n" + 
					"        },\r\n" + 
					"        \"name_path\": {\r\n" + 
					"          \"type\": \"text\"\r\n" + 
					"        },\r\n" + 
					"        \"security_level\": {\r\n" + 
					"          \"type\": \"text\"\r\n" + 
					"        },\r\n" + 
					"        \"static_path\": {\r\n" + 
					"          \"type\": \"text\"\r\n" + 
					"        },\r\n" + 
					"        \"dynamic_path\": {\r\n" + 
					"          \"type\": \"text\"\r\n" + 
					"        },\r\n" + 
					"        \"content\": {\r\n" + 
					"          \"type\": \"text\",\r\n" + 
					"          \"analyzer\": \"ik_max_word\"\r\n" + 
					"        },\r\n" + 
					"        \"keyword\": {\r\n" + 
					"          \"type\": \"text\",\r\n" + 
					"          \"analyzer\": \"ik_max_word\"\r\n" + 
					"        },\r\n" + 
					"        \"attachment\": {\r\n" + 
					"          \"type\": \"text\",\r\n" + 
					"          \"analyzer\": \"ik_max_word\"\r\n" + 
					"        },\r\n" + 
					"        \"update_time\": {\r\n" + 
					"          \"type\":   \"date\",\r\n" + 
					"          \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\r\n" + 
					"        },\r\n" + 
					"        \"create_time\": {\r\n" + 
					"          \"type\":   \"date\",\r\n" + 
					"          \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\r\n" + 
					"        }\r\n" + 
					"      }\r\n" + 
					"    }";
		

			
		
			JestResult jestResult = jestClient.execute(new CreateIndex.Builder(indexName).build());
			if (jestResult != null && jestResult.isSucceeded()) {
				result = true;
			}else {
				return new FailureTranResult<>("索引创建失败"); 
			}
			//
			String str = "{\"properties\":{\"id\":{\"type\":\"long\"},\"site_id\":{\"type\":\"long\"},\"channel_id\":{\"type\":\"long\"},\"title\":{\"type\":\"text\"},\"subtitle\":{\"type\":\"text\"},\"name_path\":{\"type\":\"text\"},\"security_level\":{\"type\":\"text\"},\"static_path\":{\"type\":\"text\"},\"dynamic_path\":{\"type\":\"text\"},\"content\":{\"type\":\"text\"},\"keyword\":{\"type\":\"text\"},\"attachment\":{\"type\":\"text\"},\"update_time\":{\"type\":\"date\",\"format\":\"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"},\"create_time\":{\"type\":\"date\",\"format\":\"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"}}}";
			PutMapping.Builder builder = new PutMapping.Builder(indexName, typeName, str).setParameter("include_type_name", "true");
	        //builder.setHeader(PWDKEY, getSecret());
	        //builder.refresh(true);
			JestResult createJestResult = jestClient.execute(builder.build());
			
			if (createJestResult != null && createJestResult.isSucceeded()) {
				result = true;
			}else {
				return new FailureTranResult<>("索引创建mapping失败");
			}
			
		} catch (IOException e) {
			logger.error("EsJestClient-createIndex error", e);
			return new FailureTranResult<>("索引创建失败");
		}
		return new SuccessTranResult<>(result);
	}

	/**
     * 获取指定索引信息
     *
     * @param indexName
     * @param typeName
     * @return
     */
    public TranResult<String> getMapping(String indexName) {
        GetMapping.Builder builder = new GetMapping.Builder();
        builder.addIndex(indexName).setParameter("include_type_name", "true");
        String res = null;
        try {
            JestResult result = jestClient.execute(builder.build());
            if (result != null && result.isSucceeded()) {
                res = result.getSourceAsObject(JsonObject.class).toString();
            }
        } catch (Exception e) {
            logger.error("es get mapping Exception ", e);
            throw new ServiceException("获取索引信息失败");
        }
        return new SuccessTranResult<>(res);
    }

    
    public TranResult<Boolean> deleteIndex() {
        try {
            JestResult jestResult = jestClient.execute(new DeleteIndex.Builder(indexName).build());
            System.out.println("deleteIndex result:{}" + jestResult.isSucceeded());
        } catch (IOException e) {
            e.printStackTrace();
            return new FailureTranResult("删除索引失败:" + indexName);
        }
        sqlSessionTemplate.insert("wcms.searcher.time.delete");
        return new SuccessTranResult<>(true);
    }
    
    private Map<String,String> getKeyMap(){
    	if(formatKeyMap == null) {
    		formatKeyMap = new LinkedTreeMap();
    		formatKeyMap.put("id", "序号");
    		formatKeyMap.put("channel_id", "频道序号");
    		formatKeyMap.put("site_id", "站点");
    		formatKeyMap.put("name_path", "频道路径");
    		formatKeyMap.put("title", "标题");
    		formatKeyMap.put("subtitle", "副标题");
    		formatKeyMap.put("keyword", "关键字");
    		formatKeyMap.put("update_time", "更新时间");
    		formatKeyMap.put("content", "正文");
    		formatKeyMap.put("attachment", "附件");
    	}
    	
    	return formatKeyMap;
    }
    
    public  String delHTMLTag(String htmlStr){ 
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); //过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); //过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); //过滤html标签 
 
        return htmlStr.trim(); //返回文本字符串 
    } 

}
