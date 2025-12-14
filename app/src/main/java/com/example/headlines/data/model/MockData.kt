package com.example.headlines.data.model

import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.*

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
    // 保持 newsIdToDetailMap 仅用于 ID 1-8 的静态初始化
    private val newsIdToDetailMap = mutableMapOf<Int, NewsDetail>()

    // 首页新闻列表数据 (静态 ID 1-8，用于初始化)
    val newsList = listOf(
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
        // 为静态 ID 1-8 预生成详情
        newsList.forEach { news ->
            newsIdToDetailMap[news.id] = createNewsDetail(news.id, news)
        }
    }

    private fun createNewsDetail(id: Int, news: News): NewsDetail {
        return when (news.type) {
            NewsType.TEXT -> createMockTextDetail(id, news)
            NewsType.IMAGE -> createMockImageDetail(id, news)
            NewsType.VIDEO -> createMockVideoDetail(id, news)
            NewsType.LONG_IMAGE -> createMockLongImageDetail(id, news)
        }
    }

    // =========================================================================
    // 接口 1: 首页列表获取
    // =========================================================================

    fun getNewsListByCategory(category: String, refreshCount: Int): List<News> {
        return when (category) {
            "推荐", "热榜", "新时代", "小说", "关注" -> getMixedNews(refreshCount)
            "视频" -> getVideoNews(refreshCount)
            else -> getMixedNews(refreshCount)
        }
    }

    fun getSearchNewsData(refreshCount: Int): List<News> {
        return listOf(
            createDynamicNews(id = 1, refreshCount = refreshCount, type = NewsType.TEXT),
            createDynamicNews(id = 2, refreshCount = refreshCount, type = NewsType.IMAGE),
            createDynamicNews(id = 3, refreshCount = refreshCount, type = NewsType.VIDEO),
            createDynamicNews(id = 4, refreshCount = refreshCount, type = NewsType.LONG_IMAGE)
        )
    }

    // =========================================================================
    // 接口 2: 详情获取 (动态 ID 修复核心)
    // =========================================================================

    /**
     * 根据新闻ID获取对应的新闻详情 (支持静态和动态 ID)
     */
    fun getNewsDetailById(id: Int): NewsDetail? {
        // 1. 静态 ID 1-8 直接返回预生成的详情
        if (newsIdToDetailMap.containsKey(id)) {
            return newsIdToDetailMap[id]
        }

        // 2. 🚨 动态 ID 处理 (ID > 100)
        if (id > 100) {
            // 解析 ID: 307 -> order=7, refreshCount=3
            // 解析 ID: 405 -> order=5, refreshCount=4
            val order = id % 100
            val refreshCount = id / 100

            // 2a. 确定新闻类型 based on order
            val newsType = when {
                order in 1..5 -> NewsType.TEXT
                order == 6 -> NewsType.IMAGE
                order == 7 -> NewsType.VIDEO
                order == 8 -> NewsType.LONG_IMAGE
                order in 101..108 -> NewsType.VIDEO // 视频标签页的 order 范围 (101-108)
                else -> return null // ID 格式不匹配
            }

            // 2b. 重新生成 News 对象 (使用相同的 order 和 refreshCount)
            val dynamicNews = createNewsWithOrder(
                order = order,
                refreshCount = refreshCount,
                type = newsType,
                isTop = false // 动态生成的数据默认非置顶
            )

            // 2c. 实时生成 NewsDetail
            return createNewsDetail(id, dynamicNews)
        }

        return null
    }

    // =========================================================================
    // 列表生成逻辑
    // =========================================================================

    private fun getMixedNews(refreshCount: Int): List<News> {
        val newsList = mutableListOf<News>()
        for (i in 1..5) {
            val isTop = i <= 3
            newsList.add(createNewsWithOrder(i, refreshCount, NewsType.TEXT, isTop))
        }
        newsList.add(createNewsWithOrder(6, refreshCount, NewsType.IMAGE, false))
        newsList.add(createNewsWithOrder(7, refreshCount, NewsType.VIDEO, false))
        newsList.add(createNewsWithOrder(8, refreshCount, NewsType.LONG_IMAGE, false))
        return newsList
    }

    private fun getVideoNews(refreshCount: Int): List<News> {
        val videoList = mutableListOf<News>()
        for (i in 1..8) {
            // 使用 order = 100 + i (例如 101, 102...)，确保 ID 不与主页列表冲突
            videoList.add(
                createNewsWithOrder(
                    order = i + 100,
                    refreshCount = refreshCount,
                    type = NewsType.VIDEO,
                    isTop = i <= 2
                )
            )
        }
        return videoList
    }

    private fun createDynamicNews(id: Int, refreshCount: Int, type: NewsType): News {
        val newsTemplates = getNewsTemplates()
        val templateIndex = (id + refreshCount) % newsTemplates.size
        val (title, source, content) = newsTemplates[templateIndex]

        val finalId = id + refreshCount * 100

        return News(
            id = finalId,
            title = "【搜索结果】$title",
            content = "搜索匹配：$content",
            type = type,
            source = source,
            commentCount = Random.nextInt(100, 1000),
            publishTime = getRandomTime(refreshCount),
            isTop = false,
            imageUrl = getImageUrlDynamic(type, id, refreshCount)
        )
    }

    // =========================================================================
    // 数据生成辅助函数 (含数据真实性优化)
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
        val dynamicTitle = getTextTitleDynamic(title, order, refreshCount)

        return when (type) {
            NewsType.TEXT -> News(
                id = finalId, title = dynamicTitle, content = getTextContentDynamic(content, order, refreshCount),
                type = type, source = source, commentCount = commentCount, publishTime = publishTime, isTop = isTop
            )
            NewsType.IMAGE -> News(
                id = finalId, title = "【图文特辑】$dynamicTitle", content = "$content。点击查看精彩瞬间。",
                type = type, source = source, commentCount = commentCount, publishTime = publishTime,
                imageUrl = getImageUrlDynamic(type, order, refreshCount), isTop = isTop
            )
            NewsType.VIDEO -> News(
                id = finalId, title = "【独家视频】$dynamicTitle", content = "深度解读$title，不容错过。",
                type = type, source = source, commentCount = commentCount, publishTime = publishTime,
                imageUrl = getImageUrlDynamic(type, order, refreshCount), isTop = isTop,
                videoUrl = "https://example.com/video${order % 5 + 1}.mp4",
                videoDuration = "${order % 4 + 1}:${String.format("%02d", order * 10 % 60)}"
            )
            NewsType.LONG_IMAGE -> {
                // 列表页横向多图约定
                val baseSeed = order * 300 + refreshCount
                val url1 = getImageUrlDynamic(type, order, baseSeed)
                val url2 = getImageUrlDynamic(type, order, baseSeed + 1)
                val url3 = getImageUrlDynamic(type, order, baseSeed + 2)
                val combinedUrl = "$url1,$url2,$url3"

                News(
                    id = finalId, title = "【长图详解】$dynamicTitle", content = "$content，一张图看清核心要点。",
                    type = type, source = source, commentCount = commentCount, publishTime = publishTime,
                    imageUrl = combinedUrl, isTop = isTop
                )
            }
        }
    }

    /** * 优化后的新闻模板，更具真实感和信息量
     */
    private fun getNewsTemplates(): List<Triple<String, String, String>> {
        return listOf(
            Triple("全球半导体市场格局深度分析：AI芯片需求爆发式增长", "华尔街日报中文网", "随着生成式AI技术的广泛应用，高性能AI芯片成为推动半导体产业增长的核心动力，预计市场规模将在五年内翻番。"),
            Triple("某城市出台公租房新政：面向新市民家庭优先配租", "本地民生报", "为解决新市民住房问题，市政府决定对符合条件的家庭提供差异化公租房补贴和优先配租机制，缓解大城市居住压力。"),
            Triple("国家天文台公布猎户座星云最新观测数据，发现新行星形成迹象", "中国科学院", "通过FAST望远镜的最新观测，科学家们在猎户座星云的尘埃云深处捕捉到了多颗原行星盘，为研究行星诞生过程提供了关键证据。"),
            Triple("深度游成新风尚：游客偏爱小众景点和文化体验", "旅游研究中心", "数据显示，传统热门景点热度下降，游客更倾向于选择具有独特地域文化和深度体验的小众旅游目的地。"),
            Triple("新能源车企年终大战：价格补贴与技术革新并驾齐驱", "汽车行业观察", "各大新能源车企在年末集中推出新款车型，通过加大价格补贴和引入最新的电池续航技术，争夺市场份额。"),
            Triple("健康饮食趋势：植物基蛋白产品迅速占领年轻市场", "食品工业杂志", "年轻一代消费者对健康和环保的关注，推动了植物基蛋白饮品、人造肉等创新型食品的销量持续走高。"),
            Triple("文化遗产数字化保护项目启动：运用3D扫描技术永久留存古建筑群", "文物保护局", "国家级文化遗产将全面进行高精度3D数字化采集，构建数字档案，以应对自然灾害和时间侵蚀的风险。"),
            Triple("金融科技创新：数字人民币在跨境支付领域试运行成功", "中国人民银行", "数字人民币在多个跨境交易场景中完成了小范围测试，标志着我国在数字货币国际化应用迈出了重要一步。"),
            Triple("教育部门规范K12教育：严禁超纲教学和变相补课行为", "教育部官网", "针对校外培训机构乱象，教育部门联合多方力量，出台严格管理措施，旨在减轻学生课业负担，回归素质教育。")
        )
    }

    private fun getTextTitleDynamic(baseTitle: String, order: Int, refreshCount: Int): String {
        val prefixes = listOf("深度", "独家", "快讯", "重磅", "热点")
        val prefix = prefixes[(order + refreshCount) % prefixes.size]
        return "$prefix | $baseTitle"
    }

    private fun getTextContentDynamic(baseContent: String, order: Int, refreshCount: Int): String {
        val suffix = when (order % 4) {
            0 -> "专家分析认为，这预示着市场将迎来新一轮变革。"
            1 -> "相关政策的细节正在紧张制定中，预计下周公布。"
            2 -> "此事件引起社会广泛关注，当地部门已介入调查。"
            else -> "更多深度报道和评论，请持续关注本频道。"
        }
        return "$baseContent。$suffix"
    }

    private fun getCommentCountDynamic(order: Int, refreshCount: Int): Int {
        val base = Random.nextInt(200, 1500)
        return base + refreshCount * 50 + order * 10
    }

    private fun getPublishTimeDynamic(order: Int, refreshCount: Int): String {
        val seconds = (order + refreshCount) * 60 + Random.nextInt(0, 3600)

        return when {
            seconds < 60 -> "刚刚"
            seconds < 3600 -> "${seconds / 60}分钟前"
            seconds < 86400 -> "${seconds / 3600}小时前"
            else -> {
                val cal = Calendar.getInstance()
                cal.add(Calendar.DAY_OF_YEAR, -(seconds / 86400))
                SimpleDateFormat("MM-dd", Locale.getDefault()).format(cal.time)
            }
        }
    }

    private fun getImageUrlDynamic(type: NewsType, order: Int, refreshCount: Int): String {
        val seed = order * 300 + refreshCount + Random.nextInt(0, 100)
        return when (type) {
            NewsType.IMAGE -> "https://picsum.photos/400/300?random=$seed"
            NewsType.VIDEO -> "https://picsum.photos/400/250?random=$seed"
            NewsType.LONG_IMAGE -> "https://picsum.photos/120/120?random=$seed"
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

    // 作者和头像数据不变...
    private fun getAuthorName(id: Int): String {
        val authors = listOf("科技日报", "新华社", "金融时报", "央视新闻", "汽车之家", "本地民生报", "科技前沿", "环球时报")
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

    // 详情创建函数保持与上一个版本一致，因为它已经基于 News 对象生成了内容
    private fun createMockTextDetail(id: Int, news: News): NewsDetail { /* ... 保持不变 ... */
        return NewsDetail(
            id = id, type = news.type.toDetailType(), title = news.title, author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id), publishTime = System.currentTimeMillis() - (id * 86400000L),
            viewCount = 10000 + id * 100, commentCount = news.commentCount, likeCount = 800 + id * 30,
            content = """
                ## ${news.title}
                
                这是新闻 ID $id 的动态生成详情。核心内容如下：
                
                ### 核心内容
                ${news.content}
                
                专家们指出，随着技术的迭代，预计未来三年内相关产业将实现质的飞跃，带来新的就业机会和经济增长点。
                
                *来源: ${news.source}, 发布时间: ${news.publishTime}*
            """.trimIndent()
        )
    }

    private fun createMockImageDetail(id: Int, news: News): NewsDetail { /* ... 保持不变 ... */
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

    private fun createMockVideoDetail(id: Int, news: News): NewsDetail { /* ... 保持不变 ... */
        return NewsDetail(
            id = id, type = news.type.toDetailType(), title = news.title, author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id), publishTime = System.currentTimeMillis() - (id * 259200000L),
            viewCount = 15000 + id * 200, commentCount = news.commentCount, likeCount = 1200 + id * 40,
            videoUrl = news.videoUrl, content = news.content
        )
    }

    private fun createMockLongImageDetail(id: Int, news: News): NewsDetail { /* ... 保持不变 ... */
        return NewsDetail(
            id = id, type = news.type.toDetailType(), title = news.title, author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id), publishTime = System.currentTimeMillis() - (id * 345600000L),
            viewCount = 11000 + id * 120, commentCount = news.commentCount, likeCount = 750 + id * 35,
            images = listOf(
                "https://picsum.photos/800/1200?random=${400 + id}",
                "https://picsum.photos/800/1200?random=${500 + id}",
                "https://picsum.photos/800/1200?random=${600 + id}"
            ), content = news.content
        )
    }
}