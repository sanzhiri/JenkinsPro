package cn.newstrength.wcms.fe.template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SampleController {

	@RequestMapping("/sample/dic")
	public ModelAndView dicTag() {
		ModelAndView mv = new ModelAndView("dic/index");
		return mv;
	}
	
	@RequestMapping("/sample")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("sample/index");
		return mv;
	}
	
	@RequestMapping("/sample/info/list/{channelId}")
	public ModelAndView infolist(@PathVariable Long channelId) {
		ModelAndView mv = new ModelAndView("info/list");
		mv.addObject("channelId", channelId);
		return mv;
	}	
}
