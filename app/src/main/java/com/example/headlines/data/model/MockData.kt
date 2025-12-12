package com.example.headlines.data.model

import java.util.UUID

object MockData {

    // 生成模拟的新闻详情数据
    fun generateMockDetail(newsId: String, type: String): NewsDetail {
        val baseId = newsId.ifEmpty { UUID.randomUUID().toString() }

        return when (type.uppercase()) {
            "TEXT" -> createMockTextDetail(baseId)
            "IMAGE" -> createMockImageDetail(baseId)
            "VIDEO" -> createMockVideoDetail(baseId)
            "LONG_IMAGE" -> createMockLongImageDetail(baseId)
            else -> createMockTextDetail(baseId)
        }
    }

    private fun createMockTextDetail(id: String): NewsDetail {
        return NewsDetail(
            id = id,
            type = NewsDetailType.TEXT,
            title = "深度分析：人工智能对未来就业的影响",
            author = "科技日报",
            authorAvatar = "https://randomuser.me/api/portraits/men/32.jpg",
            publishTime = System.currentTimeMillis() - 86400000,
            viewCount = 12500,
            commentCount = 342,
            likeCount = 890,
            content = """
                ## AI时代：机遇与挑战并存

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

    private fun createMockImageDetail(id: String): NewsDetail {
        return NewsDetail(
            id = id,
            type = NewsDetailType.IMAGE,
            title = "城市风光：夜幕下的上海外滩",
            author = "摄影中国",
            authorAvatar = "https://randomuser.me/api/portraits/women/44.jpg",
            publishTime = System.currentTimeMillis() - 172800000,
            viewCount = 8900,
            commentCount = 156,
            likeCount = 450,
            images = listOf(
                "https://picsum.photos/800/600?image=101",
                "https://picsum.photos/800/600?image=102",
                "https://picsum.photos/800/600?image=103"
            ),
            content = "上海外滩的夜景一直以来都是摄影爱好者的热门拍摄地。华灯初上，万国建筑博览群在灯光映照下更显辉煌，与黄浦江对岸的陆家嘴现代建筑群形成鲜明对比。夜幕下的外滩，既有历史的厚重感，又有现代的时尚气息。"
        )
    }

    private fun createMockVideoDetail(id: String): NewsDetail {
        return NewsDetail(
            id = id,
            type = NewsDetailType.VIDEO,
            title = "电动汽车新技术：续航突破1000公里",
            author = "汽车之家",
            authorAvatar = "https://randomuser.me/api/portraits/men/67.jpg",
            publishTime = System.currentTimeMillis() - 259200000,
            viewCount = 15600,
            commentCount = 432,
            likeCount = 1250,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            content = "最新发布的电动汽车搭载了革命性的固态电池技术，实测续航里程突破1000公里，充电时间缩短至15分钟。这款车型采用流线型设计，风阻系数仅为0.21，内饰配备智能座舱系统，支持L4级自动驾驶。专家表示，这标志着电动汽车技术进入新纪元。"
        )
    }

    private fun createMockLongImageDetail(id: String): NewsDetail {
        return NewsDetail(
            id = id,
            type = NewsDetailType.LONG_IMAGE,
            title = "三款旗舰手机横向对比：摄影性能大比拼",
            author = "数码评测",
            authorAvatar = "https://randomuser.me/api/portraits/men/22.jpg",
            publishTime = System.currentTimeMillis() - 345600000,
            viewCount = 11200,
            commentCount = 289,
            likeCount = 760,
            images = listOf(
                "https://picsum.photos/400/600?image=201",
                "https://picsum.photos/400/600?image=202",
                "https://picsum.photos/400/600?image=203"
            ),
            content = "我们对当前市场上三款旗舰手机的摄影功能进行了全面对比测试，包括夜景、人像、广角等不同场景。从左到右分别是：\n\n1. **手机A**：主摄5000万像素，夜景模式表现出色\n2. **手机B**：配备潜望式长焦，10倍混合变焦\n3. **手机C**：超广角镜头畸变控制优秀\n\n经过测试，每款手机在不同场景下各有优势，用户可根据自己的摄影需求选择。"
        )
    }

    // 生成更多类型的模拟数据（可选）
    fun generateAllMockDetails(): List<NewsDetail> {
        return listOf(
            createMockTextDetail("1"),
            createMockImageDetail("2"),
            createMockVideoDetail("3"),
            createMockLongImageDetail("4")
        )
    }
}