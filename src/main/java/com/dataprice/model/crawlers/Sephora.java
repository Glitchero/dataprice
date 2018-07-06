package com.dataprice.model.crawlers;

import java.util.List;

import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

public class Sephora extends AbstractCrawler{

	@Override
	public List<CrawlInfo> getUrlsFromTask(Task taskDAO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product parseProductFromURL(CrawlInfo crawlInfo, Task taskDAO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCrawlingStrategy() {
		return "Sephora";
	}

}
