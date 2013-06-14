package com.novel.subscription;

public class ConstDefinition {
	public static final String BOOK_LIST_URL = "http://serviceinterface.sinaapp.com/book_list.php";
	public static final String NEED_UPDATE_URL = "http://serviceinterface.sinaapp.com/update.php";
	public static final String RECOMMEND_LIST_URL = "http://serviceinterface.sinaapp.com/recommend_list.php";
	
	public static final String MBAIDU_STRING = "http://m.baidu.com/ssid=0/from=0/bd_page_type=1/";
	public static final String MBAIDU_QUERY = "http://m.baidu.com/ssid=0/from=0/bd_page_type=1/s?word=";
	
	public static final String PATTERN_QUERY = "srcid=\"wise_novel_book\">([\\s\\S]*)<div class=\"resitem\"";
	public static final String[] INVALID_CONTENT = {"<a [\\s\\S]*?</a>", "��ӭ������UC������,7��24Сʱ����ϳ���С˵���£�"};
	
	public static final String JSON_SOURCE_KEY = "source";
	public static final String JSON_TYPE_KEY = "type";
	public static final String JSON_LIST_KEY = "list";
	
	public static final String KEY_VERSION = "version";
	public static final int CURRENT_VERSION = 1;
}
