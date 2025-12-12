package com.example.headlines.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headlines.databinding.FragmentSearchResultBinding
import com.example.headlines.data.model.News
import com.example.headlines.data.model.NewsType
import com.example.headlines.ui.adapters.NewsAdapter

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NewsAdapter
    private var resultType: String = "all"

    companion object {
        private const val ARG_RESULT_TYPE = "result_type"

        fun newInstance(resultType: String): SearchResultFragment {
            val fragment = SearchResultFragment()
            val args = Bundle()
            args.putString(ARG_RESULT_TYPE, resultType)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            resultType = it.getString(ARG_RESULT_TYPE, "all")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadSearchResults()
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        adapter.setOnNewsClickListener { news ->
            // 处理新闻点击
            // 可以跳转到新闻详情页
            println("点击搜索结果: ${news.title}")
        }
    }

    private fun loadSearchResults() {
        // 模拟搜索结果
        val results = when (resultType) {
            "article" -> getArticleResults()
            "video" -> getVideoResults()
            "user" -> emptyList() // 用户结果需要不同的适配器
            "topic" -> emptyList() // 话题结果需要不同的适配器
            else -> getAllResults()
        }

        adapter.submitList(results?.toMutableList())

        // 更新UI状态
        if (results.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
            binding.emptyView.text = when (resultType) {
                "article" -> "暂无相关文章"
                "video" -> "暂无相关视频"
                "user" -> "暂无相关用户"
                "topic" -> "暂无相关话题"
                else -> "暂无搜索结果"
            }
        } else {
            binding.emptyView.visibility = View.GONE
        }
    }

    private fun getAllResults(): List<News> {
        return listOf(
            News(
                id = 101,
                title = "习近平主席关于中非合作的重要论述引发国际关注",
                content = "习近平主席在中非合作论坛上发表重要讲话，强调要坚持真实亲诚理念和正确义利观，推动中非合作不断迈上新台阶。国际社会对此高度关注，认为这将为全球南南合作注入新动力。",
                type = NewsType.TEXT,
                source = "人民日报",
                commentCount = 256,
                publishTime = "3小时前",
                isTop = true
            ),
            News(
                id = 102,
                title = "乌拉圭队世界杯备战情况分析",
                content = "距离世界杯开幕还有两个月，南美劲旅乌拉圭队正在积极备战。主教练迭戈·阿隆索表示，球队状态良好，核心球员苏亚雷斯和卡瓦尼仍然保持着高水平竞技状态。",
                type = NewsType.IMAGE,
                source = "体育新闻",
                commentCount = 128,
                publishTime = "5小时前",
                imageUrl = "https://picsum.photos/400/300?random=101"
            ),
            News(
                id = 103,
                title = "新时代中国高铁发展成就展示",
                content = "我国高铁运营里程已超过4万公里，稳居世界第一。从自主研发的复兴号到智能高铁，中国高铁技术不断创新，为经济社会发展提供了有力支撑。",
                type = NewsType.VIDEO,
                source = "新华社",
                commentCount = 512,
                publishTime = "8小时前",
                imageUrl = "https://picsum.photos/400/300?random=102",
                videoUrl = "https://example.com/video.mp4",
                videoDuration = "03:15"
            ),
            News(
                id = 104,
                title = "人工智能助力医疗诊断精度提升",
                content = "近期，多家医院引进AI辅助诊断系统，大大提高了疾病诊断的准确性和效率。特别是在影像诊断领域，AI系统能够快速识别病灶，辅助医生做出更精准的判断。",
                type = NewsType.IMAGE,
                source = "科技日报",
                commentCount = 189,
                publishTime = "2小时前",
                imageUrl = "https://picsum.photos/400/300?random=103"
            ),
            News(
                id = 105,
                title = "春节旅游市场迎来预订高峰",
                content = "距离春节还有一个月，各大旅游平台数据显示，春节假期旅游产品预订量同比增长超过200%。海南、云南、东北等地成为热门目的地，冰雪游、海岛游备受青睐。",
                type = NewsType.TEXT,
                source = "旅游时报",
                commentCount = 76,
                publishTime = "6小时前"
            ),
            News(
                id = 106,
                title = "新能源汽车销量再创新高",
                content = "根据最新统计数据，11月我国新能源汽车销量同比增长45%，市场渗透率达到36%。专家预测，全年新能源汽车销量有望突破900万辆，继续保持全球领先地位。",
                type = NewsType.LONG_IMAGE,
                source = "经济观察报",
                commentCount = 321,
                publishTime = "4小时前",
                imageUrl = "https://picsum.photos/400/600?random=104"
            ),
            News(
                id = 107,
                title = "电影《长安三万里》票房突破18亿",
                content = "国产动画电影《长安三万里》上映以来口碑票房双丰收，目前累计票房已突破18亿元。影片通过高适、李白等诗人的故事，展现了盛唐气象和文化自信。",
                type = NewsType.IMAGE,
                source = "影视娱乐",
                commentCount = 1452,
                publishTime = "1小时前",
                imageUrl = "https://picsum.photos/400/300?random=105",
                isTop = true
            ),
            News(
                id = 108,
                title = "全国多地迎来今冬首场降雪",
                content = "受冷空气影响，北京、天津、河北等地迎来今冬首场明显降雪。气象部门发布道路结冰黄色预警，提醒市民注意出行安全，做好防寒保暖措施。",
                type = NewsType.TEXT,
                source = "央视新闻",
                commentCount = 89,
                publishTime = "7小时前"
            ),
            News(
                id = 109,
                title = "CBA常规赛进入关键阶段",
                content = "CBA常规赛赛程过半，各队为季后赛席位展开激烈争夺。辽宁、新疆、广东等传统强队表现出色，年轻球员也在赛场上崭露头角，展现了良好的竞技状态。",
                type = NewsType.VIDEO,
                source = "体育周刊",
                commentCount = 467,
                publishTime = "3小时前",
                imageUrl = "https://picsum.photos/400/300?random=106",
                videoUrl = "https://example.com/basketball.mp4",
                videoDuration = "02:45"
            ),
            News(
                id = 110,
                title = "5G用户规模持续扩大",
                content = "工信部数据显示，截至11月底，我国5G移动电话用户达7.71亿户，占移动电话用户的44.7%。5G网络建设持续推进，已建成开通5G基站328.2万个。",
                type = NewsType.IMAGE,
                source = "通信产业报",
                commentCount = 123,
                publishTime = "9小时前",
                imageUrl = "https://picsum.photos/400/300?random=107"
            )
        )
    }

    private fun getArticleResults(): List<News> {
        return getAllResults().filter { it.type != NewsType.VIDEO }
    }

    private fun getVideoResults(): List<News> {
        return getAllResults().filter { it.type == NewsType.VIDEO }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}