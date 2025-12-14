package com.example.headlines.data.model

import kotlin.random.Random

// 辅助函数：将 NewsType 映射到 NewsDetailType
fun NewsType.toDetailType(): NewsDetailType {
    return when (this) {
        NewsType.TEXT -> NewsDetailType.TEXT
        NewsType.IMAGE -> NewsDetailType.IMAGE
        NewsType.VIDEO -> NewsDetailType.VIDEO
        NewsType.LONG_IMAGE -> NewsDetailType.LONG_IMAGE
    }
}

object MockData {
    // 保存首页新闻列表与详情页的映射关系 (只用于静态 ID 1-8)
    private val newsIdToDetailMap = mutableMapOf<Int, NewsDetail>()

    // 首页新闻列表数据 (静态 ID 1-8，用于初始化)
    val newsList = listOf(
        // 🚨 修正：将 id = X 改为 order = X
        createNewsWithOrder(order = 1, refreshCount = 0, type = NewsType.TEXT, isTop = true),
        createNewsWithOrder(order = 2, refreshCount = 0, type = NewsType.TEXT, isTop = true),
        createNewsWithOrder(order = 3, refreshCount = 0, type = NewsType.TEXT, isTop = true),
        createNewsWithOrder(order = 4, refreshCount = 0, type = NewsType.TEXT, isTop = false),
        createNewsWithOrder(order = 5, refreshCount = 0, type = NewsType.TEXT, isTop = false),
        createNewsWithOrder(order = 6, refreshCount = 0, type = NewsType.IMAGE, isTop = false),
        createNewsWithOrder(order = 7, refreshCount = 0, type = NewsType.VIDEO, isTop = false),
        createNewsWithOrder(order = 8, refreshCount = 0, type = NewsType.LONG_IMAGE, isTop = false)
    )

    init {
        initializeDetailMap()
    }

    private fun initializeDetailMap() {
        newsList.forEach { news ->
            newsIdToDetailMap[news.id] = createNewsDetail(news.id, news)
        }
    }

    // 统一的详情创建入口
    private fun createNewsDetail(id: Int, news: News): NewsDetail {
        return when (news.type) {
            NewsType.TEXT -> createMockTextDetail(id, news)
            NewsType.IMAGE -> createMockImageDetail(id, news)
            NewsType.VIDEO -> createMockVideoDetail(id, news)
            NewsType.LONG_IMAGE -> createMockLongImageDetail(id, news)
        }
    }

    // =========================================================================
    // 接口 1: 首页列表获取 (NewsViewModel 调用)
    // =========================================================================

    /**
     * 根据分类和刷新次数获取首页新闻列表
     */
    fun getNewsListByCategory(category: String, refreshCount: Int): List<News> {
        return when (category) {
            "推荐", "热榜", "新时代", "小说", "关注" -> getMixedNews(refreshCount)
            "视频" -> getVideoNews(refreshCount)
            else -> getMixedNews(refreshCount)
        }
    }

    // 根据 query 搜索（简单模拟）
    fun getSearchNewsData(refreshCount: Int): List<News> {
        return listOf(
            createDynamicNews(id = 1, refreshCount = refreshCount, type = NewsType.TEXT),
            createDynamicNews(id = 2, refreshCount = refreshCount, type = NewsType.IMAGE),
            createDynamicNews(id = 3, refreshCount = refreshCount, type = NewsType.VIDEO),
            createDynamicNews(id = 4, refreshCount = refreshCount, type = NewsType.LONG_IMAGE)
        )
    }

    // =========================================================================
    // 接口 2: 详情获取 (NewsDetailViewModel 调用)
    // =========================================================================

    /**
     * 根据新闻ID获取对应的新闻详情 (支持静态和动态 ID)
     */
    fun getNewsDetailById(id: Int): NewsDetail? {
        // 1. 尝试从已初始化的静态 map 中获取 (用于 ID 1-8)
        if (newsIdToDetailMap.containsKey(id)) {
            return newsIdToDetailMap[id]
        }

        // 2. 处理动态 ID (如 303, 206, 107)
        if (id >= 101 && id <= 808) { // 限制 ID 范围
            val order = id % 100
            val refreshCount = id / 100

            // 确定新闻类型 (根据 NewsViewModel 的 getMixedNews 逻辑确定)
            val newsType = when (order) {
                in 1..5 -> NewsType.TEXT
                6 -> NewsType.IMAGE
                7, in 101..108 -> NewsType.VIDEO // 视频标签页 ID 范围修正
                8 -> NewsType.LONG_IMAGE
                else -> return null
            }

            // 动态生成 News 对象
            val dynamicNews = createNewsWithOrder(
                order = order,
                refreshCount = refreshCount,
                type = newsType,
                isTop = false
            )

            // 调用修正后的详情创建函数
            return createNewsDetail(id, dynamicNews)
        }

        return null
    }

    // =========================================================================
    // 列表生成逻辑 (原 NewsViewModel)
    // =========================================================================

    private fun getMixedNews(refreshCount: Int): List<News> {
        val newsList = mutableListOf<News>()

        // 1. 先添加5条文字新闻，前3条置顶
        for (i in 1..5) {
            val isTop = i <= 3  // 前3条置顶
            newsList.add(createNewsWithOrder(i, refreshCount, NewsType.TEXT, isTop))
        }

        // 2. 添加1条图文新闻
        newsList.add(createNewsWithOrder(6, refreshCount, NewsType.IMAGE, false))

        // 3. 添加1条视频新闻
        newsList.add(createNewsWithOrder(7, refreshCount, NewsType.VIDEO, false))

        // 4. 添加1条长图新闻
        newsList.add(createNewsWithOrder(8, refreshCount, NewsType.LONG_IMAGE, false))

        return newsList
    }

    private fun getVideoNews(refreshCount: Int): List<News> {
        val videoList = mutableListOf<News>()

        // 为视频标签页专门生成视频新闻
        for (i in 1..8) {
            videoList.add(
                createNewsWithOrder(
                    order = i + 100,  // 使用不同的 ID 范围 (如 101, 102...)
                    refreshCount = refreshCount,
                    type = NewsType.VIDEO,
                    isTop = i <= 2
                )
            )
        }
        return videoList
    }

    // 创建动态新闻的辅助函数 (用于搜索功能)
    private fun createDynamicNews(id: Int, refreshCount: Int, type: NewsType): News {
        val newsTemplates = getNewsTemplates()
        val templateIndex = (id + refreshCount) % newsTemplates.size
        val (title, source, content) = newsTemplates[templateIndex]

        return News(
            id = id + refreshCount * 100,
            title = "【搜索】${title}",
            content = "搜索结果：$content",
            type = type,
            source = source,
            commentCount = Random.nextInt(100, 1000),
            publishTime = getRandomTime(refreshCount),
            isTop = false,
            imageUrl = getImageUrlDynamic(type, id, refreshCount)
        )
    }

    // =========================================================================
    // 详情创建实现 (修正签名)
    // =========================================================================

    private fun createMockTextDetail(id: Int, news: News): NewsDetail {
        return NewsDetail(
            id = id, type = news.type.toDetailType(), title = news.title, author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id), publishTime = System.currentTimeMillis() - (id * 86400000L),
            viewCount = 10000 + id * 100, commentCount = news.commentCount, likeCount = 800 + id * 30,
            content = """
                ## ${news.title}
                
                这是新闻 ID $id 的动态生成详情。核心内容如下：
                
                ### 核心内容
                ${news.content}

                ### 延伸阅读
                ...
                
                *来源: ${news.source}, 发布时间: ${news.publishTime}*
            """.trimIndent()
        )
    }

    private fun createMockImageDetail(id: Int, news: News): NewsDetail {
        return NewsDetail(
            id = id, type = news.type.toDetailType(), title = news.title, author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id), publishTime = System.currentTimeMillis() - (id * 172800000L),
            viewCount = 8000 + id * 150, commentCount = news.commentCount, likeCount = 400 + id * 25,
            images = listOf(
                "https://picsum.photos/800/600?image=${100 + id}",
                "https://picsum.photos/800/600?image=${200 + id}",
                "https://picsum.photos/800/600?image=${300 + id}"
            ), content = news.content
        )
    }

    private fun createMockVideoDetail(id: Int, news: News): NewsDetail {
        return NewsDetail(
            id = id, type = news.type.toDetailType(), title = news.title, author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id), publishTime = System.currentTimeMillis() - (id * 259200000L),
            viewCount = 15000 + id * 200, commentCount = news.commentCount, likeCount = 1200 + id * 40,
            videoUrl = news.videoUrl, content = news.content
        )
    }

    private fun createMockLongImageDetail(id: Int, news: News): NewsDetail {
        return NewsDetail(
            id = id, type = news.type.toDetailType(), title = news.title, author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id), publishTime = System.currentTimeMillis() - (id * 345600000L),
            viewCount = 11000 + id * 120, commentCount = news.commentCount, likeCount = 750 + id * 35,
            // 详情页的 images 仍需要独立生成列表，以提供高分辨率的大图
            images = listOf(
                "https://picsum.photos/800/1200?random=${400 + id}",
                "https://picsum.photos/800/1200?random=${500 + id}",
                "https://picsum.photos/800/1200?random=${600 + id}"
            ), content = news.content
        )
    }

    // =========================================================================
    // 数据生成辅助函数 (原 NewsViewModel)
    // =========================================================================

    private fun createNewsWithOrder(
        order: Int, refreshCount: Int, type: NewsType, isTop: Boolean = false
    ): News {
        val newsTemplates = getNewsTemplates()
        val templateIndex = (order + refreshCount) % newsTemplates.size
        val (title, source, content) = newsTemplates[templateIndex]

        val finalId = order + refreshCount * 100
        val commentCount = getCommentCountDynamic(order, refreshCount)
        val publishTime = getPublishTimeDynamic(order, refreshCount)

        return when (type) {
            NewsType.TEXT -> News(
                id = finalId, title = getTextTitleDynamic(title, order, refreshCount), content = getTextContentDynamic(content, order, refreshCount),
                type = type, source = source, commentCount = commentCount, publishTime = publishTime, isTop = isTop
            )
            NewsType.IMAGE -> News(
                id = finalId, title = "【图文】$title", content = "$content，详情请查看图片。",
                type = type, source = source, commentCount = commentCount, publishTime = publishTime,
                imageUrl = getImageUrlDynamic(type, order, refreshCount), isTop = isTop
            )
            NewsType.VIDEO -> News(
                id = finalId, title = "【视频】$title", content = "$content，点击观看详细视频报道。",
                type = type, source = source, commentCount = commentCount, publishTime = publishTime,
                imageUrl = getImageUrlDynamic(type, order, refreshCount), isTop = isTop,
                videoUrl = "https://example.com/video${order % 5 + 1}.mp4",
                videoDuration = "${order % 4 + 1}:${String.format("%02d", order * 10 % 60)}"
            )
            NewsType.LONG_IMAGE -> {
                // 🚨 核心修改：为列表页生成 3 个 URL，并用逗号分隔
                val baseSeed = order * 300 + refreshCount
                val url1 = getImageUrlDynamic(type, order, baseSeed)
                val url2 = getImageUrlDynamic(type, order, baseSeed + 1) // 保证图片不同
                val url3 = getImageUrlDynamic(type, order, baseSeed + 2) // 保证图片不同
                val combinedUrl = "$url1,$url2,$url3"

                News(
                    id = finalId, title = "【长图】$title", content = "$content，一图看懂完整内容。",
                    type = type, source = source, commentCount = commentCount, publishTime = publishTime,
                    // 列表页的 imageUrl 字段现在包含 3 个用逗号分隔的 URL
                    imageUrl = combinedUrl, isTop = isTop
                )
            }
        }
    }

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

    private fun getTextTitleDynamic(baseTitle: String, order: Int, refreshCount: Int): String {
        val prefixes = listOf("快讯", "要闻", "热点", "关注", "最新")
        val prefix = prefixes[(order + refreshCount) % prefixes.size]
        return "$prefix：$baseTitle"
    }

    private fun getTextContentDynamic(baseContent: String, order: Int, refreshCount: Int): String {
        val suffix = when (order % 4) {
            0 -> "详情请查看后续报道。"
            1 -> "相关部门正在进一步核实信息。"
            2 -> "更多信息将持续更新。"
            else -> "请关注官方发布的最新消息。"
        }
        return "$baseContent $suffix"
    }

    private fun getCommentCountDynamic(order: Int, refreshCount: Int): Int {
        val baseCount = when (order % 5) {
            0 -> 128
            1 -> 256
            2 -> 342
            3 -> 456
            else -> 567
        }
        return baseCount + refreshCount * 10 + order * 5
    }

    private fun getPublishTimeDynamic(order: Int, refreshCount: Int): String {
        return when ((order + refreshCount) % 6) {
            0 -> "刚刚"
            1 -> "${5 + order % 10}分钟前"
            2 -> "${1 + order % 5}小时前"
            3 -> "今天 ${8 + order % 10}:${String.format("%02d", order * 7 % 60)}"
            4 -> "昨天 ${14 + order % 6}:${String.format("%02d", order * 3 % 60)}"
            else -> "${1 + order % 7}天前"
        }
    }

    private fun getImageUrlDynamic(type: NewsType, order: Int, refreshCount: Int): String {
        // 使用 refreshCount 作为随机种子，确保每次调用 URL 不同
        return when (type) {
            NewsType.IMAGE -> "https://picsum.photos/400/300?random=${order * 100 + refreshCount}"
            NewsType.VIDEO -> "https://picsum.photos/400/250?random=${order * 200 + refreshCount}"
            // LONG_IMAGE 列表图使用更小的尺寸（方便横向排列）
            NewsType.LONG_IMAGE -> "https://picsum.photos/120/120?random=${order * 300 + refreshCount}"
            else -> ""
        }
    }

    private fun getRandomTime(refreshCount: Int): String {
        val minutesAgo = Random.nextInt(1, 60)
        val hoursAgo = Random.nextInt(1, 24)
        val daysAgo = Random.nextInt(1, 7)

        return when (refreshCount % 3) {
            0 -> "${minutesAgo}分钟前"
            1 -> "${hoursAgo}小时前"
            else -> "${daysAgo}天前"
        }
    }

    private fun getAuthorName(id: Int): String {
        val authors = listOf("科技日报", "摄影中国", "汽车之家", "数码评测", "新华社", "央视新闻", "科技前沿")
        return authors[id % authors.size]
    }

    private fun getAuthorAvatar(id: Int): String {
        val avatars = listOf(
            "https://randomuser.me/api/portraits/men/32.jpg", "https://randomuser.me/api/portraits/women/44.jpg",
            "https://randomuser.me/api/portraits/men/67.jpg", "https://randomuser.me/api/portraits/men/22.jpg",
            "https://randomuser.me/api/portraits/women/32.jpg", "https://randomuser.me/api/portraits/men/45.jpg"
        )
        return avatars[id % avatars.size]
    }
}