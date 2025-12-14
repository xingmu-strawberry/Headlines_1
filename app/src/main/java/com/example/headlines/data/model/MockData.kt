package com.example.headlines.data.model

object MockData {
    // 保存首页新闻列表与详情页的映射关系
    private val newsIdToDetailMap = mutableMapOf<Int, NewsDetail>()

    // 首页新闻列表数据 - 与NewsViewModel中的逻辑保持一致
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
        // 初始化时将首页新闻映射到对应的详情
        initializeDetailMap()
    }

    private fun initializeDetailMap() {
        // 为每个首页新闻创建对应的详情
        newsList.forEach { news ->
            when (news.type) {
                NewsType.TEXT -> {
                    newsIdToDetailMap[news.id] = createMockTextDetail(news.id)
                }
                NewsType.IMAGE -> {
                    newsIdToDetailMap[news.id] = createMockImageDetail(news.id)
                }
                NewsType.VIDEO -> {
                    newsIdToDetailMap[news.id] = createMockVideoDetail(news.id)
                }
                NewsType.LONG_IMAGE -> {
                    newsIdToDetailMap[news.id] = createMockLongImageDetail(news.id)
                }
            }
        }
    }

    // 根据新闻ID获取对应的新闻详情
    fun getNewsDetailById(id: Int): NewsDetail? {
        return newsIdToDetailMap[id]
    }

    // 创建首页新闻的辅助函数
    private fun createMockNews(id: Int, type: NewsType, isTop: Boolean): News {
        val newsTemplates = getNewsTemplates()
        val templateIndex = id % newsTemplates.size
        val (title, source, content) = newsTemplates[templateIndex]

        return when (type) {
            NewsType.TEXT -> News(
                id = id,
                title = getTextTitle(title, id),
                content = getTextContent(content, id),
                type = type,
                source = source,
                commentCount = getCommentCount(id),
                publishTime = getPublishTime(id),
                isTop = isTop
            )

            NewsType.IMAGE -> News(
                id = id,
                title = "【图文】$title",
                content = "$content，详情请查看图片。",
                type = type,
                source = source,
                commentCount = getCommentCount(id),
                publishTime = getPublishTime(id),
                imageUrl = getImageUrl(type, id),
                isTop = isTop
            )

            NewsType.VIDEO -> News(
                id = id,
                title = "【视频】$title",
                content = "$content，点击观看详细视频报道。",
                type = type,
                source = source,
                commentCount = getCommentCount(id),
                publishTime = getPublishTime(id),
                imageUrl = getImageUrl(type, id),
                videoUrl = "https://example.com/video${id % 5 + 1}.mp4",
                videoDuration = "${id % 4 + 1}:${String.format("%02d", id * 10 % 60)}",
                isTop = isTop
            )

            NewsType.LONG_IMAGE -> News(
                id = id,
                title = "【长图】$title",
                content = "$content，一图看懂完整内容。",
                type = type,
                source = source,
                commentCount = getCommentCount(id),
                publishTime = getPublishTime(id),
                imageUrl = getImageUrl(type, id),
                isTop = isTop
            )
        }
    }

    // 以下是原有的详情页创建函数，现在会根据ID生成对应的详情
    private fun createMockTextDetail(id: Int): NewsDetail {
        // 获取对应的首页新闻数据
        val news = newsList.firstOrNull { it.id == id }

        return NewsDetail(
            id = id,
            type = NewsDetailType.TEXT,
            title = news?.title ?: "深度分析：人工智能对未来就业的影响",
            author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id),
            publishTime = System.currentTimeMillis() - (id * 86400000L), // 按ID生成不同时间
            viewCount = 10000 + id * 100,
            commentCount = 300 + id * 20,
            likeCount = 800 + id * 30,
            content = """
                ## ${news?.title ?: "AI时代：机遇与挑战并存"}

                随着人工智能技术的飞速发展，关于AI对就业市场影响的讨论日益热烈。本文将从多个角度分析AI技术可能带来的就业变革。

                ### 1. AI将替代部分重复性工作

                在制造业、客服、数据录入等领域，AI已经展现出强大的替代能力。据统计，全球约有30%的工作岗位面临自动化风险。

                - 制造业：智能机器人将替代流水线工人
                - 客服行业：智能客服系统24小时在线
                - 数据录入：OCR和NLP技术大幅提升效率

                ### 2. AI创造新的就业机会

                尽管部分岗位被替代，但AI也会创造新的就业机会。数据科学家、AI伦理师、机器学习工程师等新兴职业正在崛起。同时，传统职业也需要升级技能，与AI协作。

                ### 3. 应对策略

                政府和企业应采取积极措施：加强职业培训、完善社会保障体系、推动教育体系改革。只有这样才能在AI时代实现平稳转型。

                ### 结论

                总的来说，AI既是挑战也是机遇。关键在于我们如何应对和适应这场技术革命。未来属于那些能够与AI协作的人。

                *本文观点仅供参考，投资需谨慎*
            """.trimIndent()
        )
    }

    private fun createMockImageDetail(id: Int): NewsDetail {
        val news = newsList.firstOrNull { it.id == id }

        return NewsDetail(
            id = id,
            type = NewsDetailType.IMAGE,
            title = news?.title ?: "城市风光：夜幕下的上海外滩",
            author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id),
            publishTime = System.currentTimeMillis() - (id * 172800000L),
            viewCount = 8000 + id * 150,
            commentCount = 150 + id * 15,
            likeCount = 400 + id * 25,
            images = listOf(
                "https://picsum.photos/800/600?image=${100 + id}",
                "https://picsum.photos/800/600?image=${200 + id}",
                "https://picsum.photos/800/600?image=${300 + id}"
            ),
            content = news?.content ?: "上海外滩的夜景一直以来都是摄影爱好者的热门拍摄地。华灯初上，万国建筑博览群在灯光映照下更显辉煌，与黄浦江对岸的陆家嘴现代建筑群形成鲜明对比。夜幕下的外滩，既有历史的厚重感，又有现代的时尚气息。"
        )
    }

    private fun createMockVideoDetail(id: Int): NewsDetail {
        val news = newsList.firstOrNull { it.id == id }

        return NewsDetail(
            id = id,
            type = NewsDetailType.VIDEO,
            title = news?.title ?: "电动汽车新技术：续航突破1000公里",
            author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id),
            publishTime = System.currentTimeMillis() - (id * 259200000L),
            viewCount = 15000 + id * 200,
            commentCount = 400 + id * 30,
            likeCount = 1200 + id * 40,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            content = news?.content ?: "最新发布的电动汽车搭载了革命性的固态电池技术，实测续航里程突破1000公里，充电时间缩短至15分钟。这款车型采用流线型设计，风阻系数仅为0.21，内饰配备智能座舱系统，支持L4级自动驾驶。专家表示，这标志着电动汽车技术进入新纪元。"
        )
    }

    private fun createMockLongImageDetail(id: Int): NewsDetail {
        val news = newsList.firstOrNull { it.id == id }

        return NewsDetail(
            id = id,
            type = NewsDetailType.LONG_IMAGE,
            title = news?.title ?: "三款旗舰手机横向对比：摄影性能大比拼",
            author = getAuthorName(id),
            authorAvatar = getAuthorAvatar(id),
            publishTime = System.currentTimeMillis() - (id * 345600000L),
            viewCount = 11000 + id * 120,
            commentCount = 280 + id * 18,
            likeCount = 750 + id * 35,
            images = listOf(
                "https://picsum.photos/400/600?image=${400 + id}",
                "https://picsum.photos/400/600?image=${500 + id}",
                "https://picsum.photos/400/600?image=${600 + id}"
            ),
            content = news?.content ?: "我们对当前市场上三款旗舰手机的摄影功能进行了全面对比测试，包括夜景、人像、广角等不同场景。从左到右分别是：\n\n1. **手机A**：主摄5000万像素，夜景模式表现出色\n2. **手机B**：配备潜望式长焦，10倍混合变焦\n3. **手机C**：超广角镜头畸变控制优秀\n\n经过测试，每款手机在不同场景下各有优势，用户可根据自己的摄影需求选择。"
        )
    }

    // 辅助函数 - 与NewsViewModel中的保持一致
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

    private fun getTextTitle(baseTitle: String, id: Int): String {
        val prefixes = listOf("快讯", "要闻", "热点", "关注", "最新")
        val prefix = prefixes[id % prefixes.size]
        return "$prefix：$baseTitle"
    }

    private fun getTextContent(baseContent: String, id: Int): String {
        val suffix = when (id % 4) {
            0 -> "详情请查看后续报道。"
            1 -> "相关部门正在进一步核实信息。"
            2 -> "更多信息将持续更新。"
            else -> "请关注官方发布的最新消息。"
        }
        return "$baseContent $suffix"
    }

    private fun getCommentCount(id: Int): Int {
        val baseCount = when (id % 5) {
            0 -> 128
            1 -> 256
            2 -> 342
            3 -> 456
            else -> 567
        }
        return baseCount + id * 5
    }

    private fun getPublishTime(id: Int): String {
        return when (id % 6) {
            0 -> "刚刚"
            1 -> "${5 + id % 10}分钟前"
            2 -> "${1 + id % 5}小时前"
            3 -> "今天 ${8 + id % 10}:${String.format("%02d", id * 7 % 60)}"
            4 -> "昨天 ${14 + id % 6}:${String.format("%02d", id * 3 % 60)}"
            else -> "${1 + id % 7}天前"
        }
    }

    private fun getImageUrl(type: NewsType, id: Int): String {
        return when (type) {
            NewsType.IMAGE -> "https://picsum.photos/400/300?random=${id * 100}"
            NewsType.VIDEO -> "https://picsum.photos/400/250?random=${id * 200}"
            NewsType.LONG_IMAGE -> "https://picsum.photos/400/600?random=${id * 300}"
            else -> ""
        }
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