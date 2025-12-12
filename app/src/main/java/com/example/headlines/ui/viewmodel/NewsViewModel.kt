package com.example.headlines.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headlines.data.model.News
import com.example.headlines.data.model.NewsType
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    // LiveData 变量
    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> = _newsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentCategory = "推荐"

    init {
        // 初始化时加载推荐新闻
        fetchNews("推荐")
    }

    fun fetchNews(category: String) {
        currentCategory = category
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // 使用模拟数据
            _newsList.value = createMockNews(category)

            _isLoading.value = false
        }
    }

    fun refreshNews() {
        fetchNews(currentCategory)
    }

    // 修改 createMockNews 函数，确保包含所有类型
    private fun createMockNews(category: String): List<News> {
        return when (category) {
            "推荐" -> getTopNews() + getVideoNews() + getLongImageNews() // 添加视频和长图
            "热榜" -> getSportsNews()
            "新时代" -> getEntertainmentNews()
            "小说" -> getTechNews()
            "视频" -> getVideoNews() // 确保视频类别包含视频新闻
            "关注" -> getGeneralNews() + getLongImageNews() // 添加长图新闻
            else -> getGeneralNews()
        }
    }

    // 添加视频新闻函数
    private fun getVideoNews(): List<News> {
        return listOf(
            News(
                id = 15,
                title = "高速铁路350公里时速常态化运营",
                content = "我国已有近320公里高铁线路实现350公里/小时常态化高标运营，运行效率大幅提升。",
                type = NewsType.VIDEO,
                source = "新华社",
                commentCount = 456,
                publishTime = "3小时前",
                imageUrl = "https://picsum.photos/400/300?random=9",
                videoUrl = "https://example.com/video1.mp4",
                videoDuration = "02:30"
            ),
            News(
                id = 16,
                title = "人工智能最新应用展示",
                content = "最新AI技术在各行业的创新应用，展示了科技发展的新成果。",
                type = NewsType.VIDEO,
                source = "科技日报",
                commentCount = 289,
                publishTime = "5小时前",
                imageUrl = "https://picsum.photos/400/300?random=10",
                videoUrl = "https://example.com/video2.mp4",
                videoDuration = "03:15"
            )
        )
    }

    // 添加长图新闻函数
    private fun getLongImageNews(): List<News> {
        return listOf(
            News(
                id = 17,
                title = "腾讯老干妈事件后续发展",
                content = "腾讯与老干妈合同纠纷事件引发全网关注，最新进展一览。",
                type = NewsType.LONG_IMAGE,
                source = "互联网快讯",
                commentCount = 892,
                publishTime = "1小时前",
                imageUrl = "https://picsum.photos/400/600?random=11" // 长图比例
            ),
            News(
                id = 18,
                title = "年度科技产品长图汇总",
                content = "2024年最值得关注的科技产品汇总，一图看懂所有新品。",
                type = NewsType.LONG_IMAGE,
                source = "科技前沿",
                commentCount = 567,
                publishTime = "4小时前",
                imageUrl = "https://picsum.photos/400/800?random=12" // 更长的图片
            )
        )
    }

    private fun getTopNews(): List<News> {
        return listOf(
            News(
                id = 1,
                title = "中国新能源汽车出口量跃居全球第一",
                content = "今年以来，我国新能源汽车出口持续增长，首次成为全球新能源汽车出口第一大国。据海关总署数据显示，今年前10个月，我国新能源汽车出口量同比增长超过100%，成为外贸出口的新亮点。",
                type = NewsType.IMAGE,
                source = "新华社",
                commentCount = 342,
                publishTime = "10:30",
                imageUrl = "https://picsum.photos/400/300?random=1",
                isTop = true
            ),
            News(
                id = 2,
                title = "全国多地迎来新一轮降雪天气",
                content = "中央气象台预报，受冷空气影响，华北、东北等地将迎来大范围降雪。北京、天津、河北等地将有小到中雪，局部地区有大雪。气象部门提醒市民注意防寒保暖，出行注意安全。",
                type = NewsType.TEXT,
                source = "央视新闻",
                commentCount = 128,
                publishTime = "09:15"
            ),
            News(
                id = 3,
                title = "人工智能助力医疗诊断新突破",
                content = "国内科研团队研发出新型AI医疗诊断系统，准确率达98%。该系统能够快速识别多种疾病特征，大大提高了诊断效率和准确性，有望在未来广泛应用于基层医疗机构。",
                type = NewsType.IMAGE,
                source = "科技日报",
                commentCount = 256,
                publishTime = "16:20",
                imageUrl = "https://picsum.photos/400/300?random=2"
            )
        )
    }

    private fun getSportsNews(): List<News> {
        return listOf(
            News(
                id = 4,
                title = "国足亚洲杯首战告捷",
                content = "在刚刚结束的亚洲杯小组赛中，中国男足以2:1战胜对手，取得开门红。比赛中，武磊梅开二度，成为球队获胜的最大功臣。下一场比赛将对阵小组最强对手日本队。",
                type = NewsType.IMAGE,
                source = "体育周刊",
                commentCount = 1542,
                publishTime = "22:45",
                imageUrl = "https://picsum.photos/400/300?random=3",
                isTop = true
            ),
            News(
                id = 5,
                title = "CBA常规赛进入白热化阶段",
                content = "本赛季CBA常规赛竞争激烈，多支球队为季后赛席位展开激烈争夺。目前排名前四的球队积分差距很小，每一场比赛都可能影响最终的排名格局。",
                type = NewsType.TEXT,
                source = "篮球先锋报",
                commentCount = 623,
                publishTime = "18:30"
            ),
            News(
                id = 6,
                title = "冬奥场馆赛后利用成效显著",
                content = "北京冬奥会结束后，各比赛场馆持续向社会开放，举办多场体育赛事和群众性冰雪活动。据统计，冬奥场馆累计接待游客超过500万人次，冰雪运动参与人数大幅增长。",
                type = NewsType.IMAGE,
                source = "体育日报",
                commentCount = 342,
                publishTime = "14:20",
                imageUrl = "https://picsum.photos/400/300?random=4"
            )
        )
    }

    private fun getEntertainmentNews(): List<News> {
        return listOf(
            News(
                id = 7,
                title = "春节档电影预售票房破亿",
                content = "2024年春节档电影预售火热开启，多部影片备受期待。目前已有三部影片预售票房突破5000万元，市场预测今年春节档总票房有望再创新高。",
                type = NewsType.IMAGE,
                source = "影视快讯",
                commentCount = 892,
                publishTime = "14:20",
                imageUrl = "https://picsum.photos/400/300?random=5"
            ),
            News(
                id = 8,
                title = "热门电视剧刷新收视纪录",
                content = "近期播出的古装剧《长安十二时辰》收视率持续攀升，单集最高收视率达到3.5%，创下今年电视剧收视新高。该剧制作精良，演员演技在线，获得观众一致好评。",
                type = NewsType.TEXT,
                source = "娱乐周刊",
                commentCount = 567,
                publishTime = "11:30"
            )
        )
    }

    private fun getTechNews(): List<News> {
        return listOf(
            News(
                id = 9,
                title = "5G用户突破10亿大关",
                content = "全球5G用户数量持续快速增长，中国5G发展领跑全球。截至目前，我国5G基站总数已超过300万个，5G移动电话用户达7.5亿户，占全球总数的60%以上。",
                type = NewsType.IMAGE,
                source = "科技前沿",
                commentCount = 421,
                publishTime = "11:30",
                imageUrl = "https://picsum.photos/400/300?random=6"
            ),
            News(
                id = 10,
                title = "国产操作系统发布新版本",
                content = "国内自主开发的鸿蒙操作系统发布4.0版本，新增多项创新功能。新系统在性能、安全性和生态建设方面都有显著提升，已有超过3亿台设备搭载该系统。",
                type = NewsType.TEXT,
                source = "IT时报",
                commentCount = 289,
                publishTime = "09:45"
            )
        )
    }

    private fun getFinanceNews(): List<News> {
        return listOf(
            News(
                id = 11,
                title = "A股市场迎来开门红",
                content = "新年首个交易日，A股三大指数集体上涨，市场信心逐步恢复。上证指数上涨1.2%，深证成指上涨1.8%，创业板指上涨2.3%。专家分析认为，宏观经济基本面持续向好。",
                type = NewsType.IMAGE,
                source = "财经日报",
                commentCount = 734,
                publishTime = "15:45",
                imageUrl = "https://picsum.photos/400/300?random=7"
            ),
            News(
                id = 12,
                title = "央行降准释放长期资金",
                content = "中国人民银行宣布下调金融机构存款准备金率0.5个百分点，释放长期资金约1万亿元。此次降准有助于保持流动性合理充裕，支持实体经济发展。",
                type = NewsType.TEXT,
                source = "金融时报",
                commentCount = 312,
                publishTime = "10:00"
            )
        )
    }

    private fun getGeneralNews(): List<News> {
        return listOf(
            News(
                id = 13,
                title = "城乡居民医保待遇持续提高",
                content = "国家医保局发布通知，进一步优化医保待遇保障机制。2024年起，城乡居民医保人均财政补助标准提高30元，大病保险报销比例也相应提高。",
                type = NewsType.TEXT,
                source = "人民健康报",
                commentCount = 156,
                publishTime = "09:00"
            ),
            News(
                id = 14,
                title = "全国快递业务量再创新高",
                content = "2023年全国快递业务量完成1320亿件，同比增长19.4%。快递业务收入完成1.2万亿元，同比增长14.3%。快递服务满意度持续提升。",
                type = NewsType.IMAGE,
                source = "物流时报",
                commentCount = 289,
                publishTime = "16:30",
                imageUrl = "https://picsum.photos/400/300?random=8"
            )
        )
    }

    // 搜索功能
    fun searchNews(query: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // 从所有模拟数据中搜索
            val allNews = getTopNews() + getSportsNews() + getEntertainmentNews() +
                    getTechNews() + getFinanceNews() + getGeneralNews()

            val filteredNews = allNews.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true)
            }

            _newsList.value = filteredNews
            _isLoading.value = false

            if (filteredNews.isEmpty()) {
                _errorMessage.value = "未找到相关新闻"
            }
        }
    }
}