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

    // 用于生成随机数的对象 (用于静态辅助函数)
    private val random = Random(System.currentTimeMillis())

    // 首页新闻列表数据 (静态 ID 1-8)
    val newsList = listOf(
        createMockNews(id = 1, type = NewsType.TEXT, isTop = true),
        createMockNews(id = 2, type = NewsType.TEXT, isTop = true),
        createMockNews(id = 3, type = NewsType.TEXT, isTop = true),
        createMockNews(id = 4, type = NewsType.TEXT, isTop = false),
        createMockNews(id = 5, type = NewsType.TEXT, isTop = false),
        createMockNews(id = 6, type = NewsType.IMAGE, isTop = false),
        createMockNews(id = 7, type = NewsType.VIDEO, isTop = false),
        createMockNews(id = 8, type = NewsType.LONG_IMAGE, isTop = false)
    )

    init {
        // 初始化时将静态首页新闻映射到对应的详情
        initializeDetailMap()
    }

    private fun initializeDetailMap() {
        // 为每个首页新闻创建对应的详情 (注意：这里创建的是原始静态详情，不涉及动态 ID)
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
    // 核心修正函数：支持动态 ID 查找
    // =========================================================================

    // 根据新闻ID获取对应的新闻详情
    fun getNewsDetailById(id: Int): NewsDetail? {
        // 1. 尝试从已初始化的静态 map 中获取 (用于 ID 1-8)
        if (newsIdToDetailMap.containsKey(id)) {
            return newsIdToDetailMap[id]
        }

        // 2. 🚨 处理动态 ID (如 303, 206, 107)
        if (id > 100) {
            val order = id % 100
            val refreshCount = id / 100

            // 排除无效的 order (order 必须在 1 到 8 之间)
            if (order < 1 || order > 8) return null

            // 3. 确定新闻类型 (根据 NewsViewModel 的 getMixedNews 逻辑确定)
            val newsType = when (order) {
                in 1..5 -> NewsType.TEXT
                6 -> NewsType.IMAGE
                7 -> NewsType.VIDEO
                8 -> NewsType.LONG_IMAGE
                else -> return null
            }

            // 4. 动态生成 News 对象 (使用复制过来的函数，确保数据一致性)
            val dynamicNews = createNewsWithOrder(
                order = order,
                refreshCount = refreshCount,
                type = newsType,
                isTop = false
            )

            // 5. 调用修正后的详情创建函数
            return createNewsDetail(id, dynamicNews)
        }

        return null
    }

    // =========================================================================
    // 修正后的详情创建函数 (接受 News 对象)
    // =========================================================================

    private fun createMockTextDetail(id: Int, news: News): NewsDetail {
        return NewsDetail(
            id = id,
            type = news.type.toDetailType(),
            title = news.title,
            author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id),
            publishTime = System.currentTimeMillis() - (id * 86400000L),
            viewCount = 10000 + id * 100,
            commentCount = news.commentCount, // 使用动态生成的评论数
            likeCount = 800 + id * 30,
            content = """
                ## ${news.title}
                
                随着人工智能技术的飞速发展，关于AI对就业市场影响的讨论日益热烈。本文将从多个角度分析AI技术可能带来的就业变革。
                
                ### 1. 核心内容
                ${news.content}

                ### 2. 拓展阅读
                在制造业、客服、数据录入等领域，AI已经展现出强大的替代能力。据统计，全球约有30%的工作岗位面临自动化风险。

                - 制造业：智能机器人将替代流水线工人
                - 客服行业：智能客服系统24小时在线
                - 数据录入：OCR和NLP技术大幅提升效率

                *本文观点仅供参考，ID: $id, 评论数: ${news.commentCount}*
            """.trimIndent()
        )
    }

    private fun createMockImageDetail(id: Int, news: News): NewsDetail {
        return NewsDetail(
            id = id,
            type = news.type.toDetailType(),
            title = news.title,
            author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id),
            publishTime = System.currentTimeMillis() - (id * 172800000L),
            viewCount = 8000 + id * 150,
            commentCount = news.commentCount,
            likeCount = 400 + id * 25,
            images = listOf(
                "https://picsum.photos/800/600?image=${100 + id}",
                "https://picsum.photos/800/600?image=${200 + id}",
                "https://picsum.photos/800/600?image=${300 + id}"
            ),
            content = news.content // 使用动态生成的简短内容作为摘要
        )
    }

    private fun createMockVideoDetail(id: Int, news: News): NewsDetail {
        return NewsDetail(
            id = id,
            type = news.type.toDetailType(),
            title = news.title,
            author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id),
            publishTime = System.currentTimeMillis() - (id * 259200000L),
            viewCount = 15000 + id * 200,
            commentCount = news.commentCount,
            likeCount = 1200 + id * 40,
            videoUrl = news.videoUrl, // 🚨 使用动态生成的 URL
            content = news.content // 使用动态生成的简短内容作为摘要
        )
    }

    private fun createMockLongImageDetail(id: Int, news: News): NewsDetail {
        return NewsDetail(
            id = id,
            type = news.type.toDetailType(),
            title = news.title,
            author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id),
            publishTime = System.currentTimeMillis() - (id * 345600000L),
            viewCount = 11000 + id * 120,
            commentCount = news.commentCount,
            likeCount = 750 + id * 35,
            images = listOf(
                "https://picsum.photos/400/600?image=${400 + id}",
                "https://picsum.photos/400/600?image=${500 + id}",
                "https://picsum.photos/400/600?image=${600 + id}"
            ),
            content = news.content
        )
    }

    // =========================================================================
    // NewsViewModel 辅助函数复制区 (用于动态 ID 生成)
    // =========================================================================

    // 复制：按顺序创建新闻的辅助函数 (核心 ID 生成逻辑)
    private fun createNewsWithOrder(
        order: Int,
        refreshCount: Int,
        type: NewsType,
        isTop: Boolean = false
    ): News {
        val newsTemplates = getNewsTemplates()
        val templateIndex = (order + refreshCount) % newsTemplates.size
        val (title, source, content) = newsTemplates[templateIndex]

        return when (type) {
            NewsType.TEXT -> News(
                id = order + refreshCount * 100,
                title = getTextTitleDynamic(title, order, refreshCount),
                content = getTextContentDynamic(content, order, refreshCount),
                type = type,
                source = source,
                commentCount = getCommentCountDynamic(order, refreshCount),
                publishTime = getPublishTimeDynamic(order, refreshCount),
                isTop = isTop
            )

            NewsType.IMAGE -> News(
                id = order + refreshCount * 100,
                title = "【图文】$title",
                content = "$content，详情请查看图片。",
                type = type,
                source = source,
                commentCount = getCommentCountDynamic(order, refreshCount),
                publishTime = getPublishTimeDynamic(order, refreshCount),
                imageUrl = getImageUrlDynamic(type, order, refreshCount),
                isTop = isTop
            )

            NewsType.VIDEO -> News(
                id = order + refreshCount * 100,
                title = "【视频】$title",
                content = "$content，点击观看详细视频报道。",
                type = type,
                source = source,
                commentCount = getCommentCountDynamic(order, refreshCount),
                publishTime = getPublishTimeDynamic(order, refreshCount),
                imageUrl = getImageUrlDynamic(type, order, refreshCount),
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
                commentCount = getCommentCountDynamic(order, refreshCount),
                publishTime = getPublishTimeDynamic(order, refreshCount),
                imageUrl = getImageUrlDynamic(type, order, refreshCount),
                isTop = isTop
            )
        }
    }

    // 复制：获取新闻模板
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

    // 复制：获取标题（Dynamic 避免冲突）
    private fun getTextTitleDynamic(baseTitle: String, order: Int, refreshCount: Int): String {
        val prefixes = listOf("快讯", "要闻", "热点", "关注", "最新")
        val prefix = prefixes[(order + refreshCount) % prefixes.size]
        return "$prefix：$baseTitle"
    }

    // 复制：获取内容（Dynamic 避免冲突）
    private fun getTextContentDynamic(baseContent: String, order: Int, refreshCount: Int): String {
        val suffix = when (order % 4) {
            0 -> "详情请查看后续报道。"
            1 -> "相关部门正在进一步核实信息。"
            2 -> "更多信息将持续更新。"
            else -> "请关注官方发布的最新消息。"
        }
        return "$baseContent $suffix"
    }

    // 复制：获取评论数（Dynamic 避免冲突）
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

    // 复制：获取发布时间（Dynamic 避免冲突）
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

    // 复制：获取图片URL（Dynamic 避免冲突）
    private fun getImageUrlDynamic(type: NewsType, order: Int, refreshCount: Int): String {
        return when (type) {
            NewsType.IMAGE -> "https://picsum.photos/400/300?random=${order * 100 + refreshCount}"
            NewsType.VIDEO -> "https://picsum.photos/400/250?random=${order * 200 + refreshCount}"
            NewsType.LONG_IMAGE -> "https://picsum.photos/400/600?random=${order * 300 + refreshCount}"
            else -> ""
        }
    }

    // =========================================================================
    // 静态 ID 1-8 辅助函数 (用于 init 块)
    // =========================================================================

    // 创建首页新闻的辅助函数 (用于静态列表)
    private fun createMockNews(id: Int, type: NewsType, isTop: Boolean): News {
        // 由于静态 ID 1-8 只需要使用 id 本身，这里我们复用动态逻辑，传入 refreshCount=0
        return createNewsWithOrder(
            order = id,
            refreshCount = 0, // 静态列表使用 refreshCount=0
            type = type,
            isTop = isTop
        )
    }

    private fun getAuthorName(id: Int): String {
        val authors = listOf("科技日报", "摄影中国", "汽车之家", "数码评测", "新华社", "央视新闻", "科技前沿")
        return authors[id % authors.size]
    }

    private fun getAuthorAvatar(id: Int): String {
        val avatars = listOf(
            "https://randomuser.me/api/portraits/men/32.jpg",
            "https://randomuser.me/api/portraits/women/44.jpg",
            "https://randomuser.me/api/portraits/men/67.jpg",
            "https://randomuser.me/api/portraits/men/22.jpg",
            "https://randomuser.me/api/portraits/women/32.jpg",
            "https://randomuser.me/api/portraits/men/45.jpg"
        )
        return avatars[id % avatars.size]
    }

    // 生成更多类型的模拟数据（可选）
    fun generateAllMockDetails(): List<NewsDetail> {
        return newsList.mapNotNull { getNewsDetailById(it.id) }
    }
}