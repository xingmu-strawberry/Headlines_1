package com.example.headlines.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headlines.data.model.News
import com.example.headlines.data.model.NewsType
import kotlinx.coroutines.launch
import kotlin.random.Random

class NewsViewModel : ViewModel() {

    // LiveData 变量
    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> = _newsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    // 添加一个随机数生成器
    private val random = Random(System.currentTimeMillis())
    // 添加一个计数器来生成不同的时间
    private var refreshCount = 0

    private var currentCategory = "推荐"

    init {
        // 初始化时加载推荐新闻
        fetchNews("推荐")
    }

    fun fetchNews(category: String) {
        currentCategory = category
        viewModelScope.launch { // 协程作用域
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
        refreshCount ++ // 让每次返回不同的数据
        return when (category) {
            "推荐" -> getMixedNews(refreshCount)
            "热榜" -> getMixedNews(refreshCount)
            "新时代" -> getMixedNews(refreshCount)
            "小说" -> getMixedNews(refreshCount)
            "视频" -> getVideoNews(refreshCount) // 确保视频类别是视频新闻
            "关注" -> getMixedNews(refreshCount)
            else -> getMixedNews(refreshCount)
        }
    }


    // 修改 getMixedNews 函数
    private fun getMixedNews(refreshCount: Int): List<News> {
        val newsList = mutableListOf<News>()

        // 1. 先添加5条文字新闻，前3条置顶
        for (i in 1..5) {
            val isTop = i <= 3  // 前3条置顶
            val textNews = createNewsWithOrder(
                order = i,
                refreshCount = refreshCount,
                type = NewsType.TEXT,
                isTop = isTop
            )
            newsList.add(textNews)
        }

        // 2. 添加1条图文新闻
        val imageNews = createNewsWithOrder(
            order = 6,
            refreshCount = refreshCount,
            type = NewsType.IMAGE,
            isTop = false
        )
        newsList.add(imageNews)

        // 3. 添加1条视频新闻
        val videoNews = createNewsWithOrder(
            order = 7,
            refreshCount = refreshCount,
            type = NewsType.VIDEO,
            isTop = false
        )
        newsList.add(videoNews)

        // 4. 添加1条长图新闻
        val longImageNews = createNewsWithOrder(
            order = 8,
            refreshCount = refreshCount,
            type = NewsType.LONG_IMAGE,
            isTop = false
        )
        newsList.add(longImageNews)

        return newsList
    }

    // 新增：按顺序创建新闻的辅助函数
    private fun createNewsWithOrder(
        order: Int,
        refreshCount: Int,
        type: NewsType,
        isTop: Boolean = false
    ): News {
        // 根据顺序选择不同的新闻模板
        val newsTemplates = getNewsTemplates()
        val templateIndex = (order + refreshCount) % newsTemplates.size
        val (title, source, content) = newsTemplates[templateIndex]

        return when (type) {
            NewsType.TEXT -> News(
                id = order + refreshCount * 100,
                title = getTextTitle(title, order, refreshCount),
                content = getTextContent(content, order, refreshCount),
                type = type,
                source = source,
                commentCount = getCommentCount(order, refreshCount),
                publishTime = getPublishTime(order, refreshCount),
                isTop = isTop
            )

            NewsType.IMAGE -> News(
                id = order + refreshCount * 100,
                title = "【图文】$title",
                content = "$content，详情请查看图片。",
                type = type,
                source = source,
                commentCount = getCommentCount(order, refreshCount),
                publishTime = getPublishTime(order, refreshCount),
                imageUrl = getImageUrl(type, order, refreshCount),
                isTop = isTop
            )

            NewsType.VIDEO -> News(
                id = order + refreshCount * 100,
                title = "【视频】$title",
                content = "$content，点击观看详细视频报道。",
                type = type,
                source = source,
                commentCount = getCommentCount(order, refreshCount),
                publishTime = getPublishTime(order, refreshCount),
                imageUrl = getImageUrl(type, order, refreshCount),
                videoUrl = "https://example.com/video${order % 5 + 1}.mp4",
                videoDuration = "${order % 4 + 1}:${String.format("%02d", order * 10 % 60)}",
                isTop = isTop
            )

            NewsType.LONG_IMAGE -> News(
                id = order + refreshCount * 100,
                title = "【长图】$title",
                content = "$content，一图看懂完整内容。",
                type = type,
                source = source,
                commentCount = getCommentCount(order, refreshCount),
                publishTime = getPublishTime(order, refreshCount),
                imageUrl = getImageUrl(type, order, refreshCount),
                isTop = isTop
            )
        }
    }

    // 新增：获取新闻模板
    private fun getNewsTemplates(): List<Triple<String, String, String>> {
        return listOf(
            Triple("中国新能源汽车出口量跃居全球第一", "新华社", "今年以来，我国新能源汽车出口持续增长，首次成为全球新能源汽车出口第一大国。"),
            Triple("全国多地迎来新一轮降雪天气", "央视新闻", "中央气象台预报，受冷空气影响，华北、东北等地将迎来大范围降雪。"),
            Triple("人工智能助力医疗诊断新突破", "科技日报", "国内科研团队研发出新型AI医疗诊断系统，准确率达98%。"),
            Triple("5G用户突破10亿大关", "科技前沿", "全球5G用户数量持续快速增长，中国5G发展领跑全球。"),
            Triple("央行降准释放长期资金", "金融时报", "中国人民银行宣布下调金融机构存款准备金率0.5个百分点。"),
            Triple("CBA常规赛进入白热化阶段", "篮球先锋报", "本赛季CBA常规赛竞争激烈，多支球队为季后赛席位展开激烈争夺。"),
            Triple("春节档电影预售票房破亿", "影视快讯", "2024年春节档电影预售火热开启，多部影片备受期待。"),
            Triple("高速铁路350公里时速常态化运营", "新华社", "我国已有近320公里高铁线路实现350公里/小时常态化高标运营。"),
            Triple("城乡居民医保待遇持续提高", "人民健康报", "国家医保局发布通知，进一步优化医保待遇保障机制。")
        )
    }

    // 新增：获取标题（根据顺序和类型）
    private fun getTextTitle(baseTitle: String, order: Int, refreshCount: Int): String {
        val prefixes = listOf("快讯", "要闻", "热点", "关注", "最新")
        val prefix = prefixes[(order + refreshCount) % prefixes.size]
        return "$prefix：$baseTitle"
    }

    // 新增：获取内容
    private fun getTextContent(baseContent: String, order: Int, refreshCount: Int): String {
        val suffix = when (order % 4) {
            0 -> "详情请查看后续报道。"
            1 -> "相关部门正在进一步核实信息。"
            2 -> "更多信息将持续更新。"
            else -> "请关注官方发布的最新消息。"
        }
        return "$baseContent $suffix"
    }

    // 新增：获取评论数（根据顺序）
    private fun getCommentCount(order: Int, refreshCount: Int): Int {
        val baseCount = when (order % 5) {
            0 -> 128
            1 -> 256
            2 -> 342
            3 -> 456
            else -> 567
        }
        return baseCount + refreshCount * 10 + order * 5
    }

    // 新增：获取发布时间
    private fun getPublishTime(order: Int, refreshCount: Int): String {
        return when ((order + refreshCount) % 6) {
            0 -> "刚刚"
            1 -> "${5 + order % 10}分钟前"
            2 -> "${1 + order % 5}小时前"
            3 -> "今天 ${8 + order % 10}:${String.format("%02d", order * 7 % 60)}"
            4 -> "昨天 ${14 + order % 6}:${String.format("%02d", order * 3 % 60)}"
            else -> "${1 + order % 7}天前"
        }
    }

    // 新增：获取图片URL
    private fun getImageUrl(type: NewsType, order: Int, refreshCount: Int): String {
        return when (type) {
            NewsType.IMAGE -> "https://picsum.photos/400/300?random=${order * 100 + refreshCount}"
            NewsType.VIDEO -> "https://picsum.photos/400/250?random=${order * 200 + refreshCount}"
            NewsType.LONG_IMAGE -> "https://picsum.photos/400/600?random=${order * 300 + refreshCount}"
            else -> ""  // TEXT类型没有图片
        }
    }

    // 修改视频新闻函数，也按照顺序
    private fun getVideoNews(refreshCount: Int): List<News> {
        val videoList = mutableListOf<News>()

        // 为视频标签页专门生成视频新闻
        for (i in 1..8) {
            val videoNews = createNewsWithOrder(
                order = i + 100,  // 使用不同的ID范围
                refreshCount = refreshCount,
                type = NewsType.VIDEO,
                isTop = i <= 2  // 前2个视频置顶
            )
            videoList.add(videoNews)
        }

        return videoList
    }

    // 创建动态新闻的辅助函数
    private fun createDynamicNews(id: Int, refreshCount: Int, type: NewsType): News {
        // 基础数据模板
        val newsTemplates = listOf(
            Triple("新能源汽车销量再创新高", "新华社", "我国新能源汽车市场持续快速增长"),
            Triple("人工智能助力产业升级", "科技日报", "AI技术在各行业应用广泛"),
            Triple("全国天气变化趋势", "气象局", "受冷空气影响多地降温"),
            Triple("数字经济蓬勃发展", "经济时报", "数字化转型加速推进"),
            Triple("健康生活新理念", "健康报", "科学养生方法受关注"),
            Triple("教育政策新变化", "教育新闻", "教育改革持续推进")
        )

        // 根据刷新次数选择不同的模板
        val templateIndex = (id + refreshCount) % newsTemplates.size
        val (title, source, content) = newsTemplates[templateIndex]

        // 根据类型添加特定信息
        return when (type) {
            NewsType.TEXT -> News(
                id = id + refreshCount * 100,  // 确保ID不同
                title = "${title}",
                content = "$content",
                type = type,
                source = source,
                commentCount = random.nextInt(100, 1000),
                publishTime = getRandomTime(refreshCount),
                isTop = (refreshCount % 3 == 0)
            )

            NewsType.IMAGE -> News(
                id = id + refreshCount * 100,
                title = "${title}",
                content = "$content",
                type = type,
                source = source,
                commentCount = random.nextInt(100, 1000),
                publishTime = getRandomTime(refreshCount),
                imageUrl = "https://picsum.photos/400/300?random=${random.nextInt(10000)}",
                isTop = random.nextBoolean()  // 随机是否为置顶
            )

            NewsType.VIDEO -> News(
                id = id + refreshCount * 100,
                title = "${title}视频报道",
                content = "$content，点击观看详细视频。",
                type = type,
                source = source,
                commentCount = random.nextInt(100, 1000),
                publishTime = getRandomTime(refreshCount),
                imageUrl = "https://picsum.photos/400/300?random=${random.nextInt(10000)}",
                videoUrl = "https://example.com/video${random.nextInt(10)}.mp4",
                videoDuration = "${random.nextInt(1, 5)}:${random.nextInt(10, 60).toString().padStart(2, '0')}"
            )

            NewsType.LONG_IMAGE -> News(
                id = id + refreshCount * 100,
                title = "${title}长图解析",
                content = "$content，一图看懂所有内容。",
                type = type,
                source = source,
                commentCount = random.nextInt(100, 1000),
                publishTime = getRandomTime(refreshCount),
                imageUrl = "https://picsum.photos/400/${600 + random.nextInt(400)}?random=${random.nextInt(10000)}"
            )
        }
    }

    // 生成随机时间
    private fun getRandomTime(refreshCount: Int): String {
        val minutesAgo = random.nextInt(1, 60)
        val hoursAgo = random.nextInt(1, 24)
        val daysAgo = random.nextInt(1, 7)

        return when (refreshCount % 3) {
            0 -> "${minutesAgo}分钟前"
            1 -> "${hoursAgo}小时前"
            else -> "${daysAgo}天前"
        }
    }

    // 搜索功能
    fun searchNews(query: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // 生成一组模拟数据用于搜索
            val searchData = listOf(
                createDynamicNews(id = 1, refreshCount = refreshCount, type = NewsType.TEXT),
                createDynamicNews(id = 2, refreshCount = refreshCount, type = NewsType.IMAGE),
                createDynamicNews(id = 3, refreshCount = refreshCount, type = NewsType.VIDEO),
                createDynamicNews(id = 4, refreshCount = refreshCount, type = NewsType.LONG_IMAGE)
            )

            val filteredNews = searchData.filter {
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