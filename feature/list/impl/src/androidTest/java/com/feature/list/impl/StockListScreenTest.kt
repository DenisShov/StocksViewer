package com.feature.list.impl

class StockListScreenTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    private lateinit var retry: String
//    private lateinit var appName: String
//
//    @Before
//    fun setup() {
//        composeTestRule.activity.apply {
//            retry = getString(com.test.app.commonresources.R.string.retry)
//            appName = getString(com.test.app.commonresources.R.string.app_name)
//        }
//    }
//
//    @Test
//    fun test_CodeChallengesScreen_title_is_displayed() {
//        composeTestRule.setContent {
//            val codeChallenges = testFlowPagingData.collectAsLazyPagingItems()
//            val snackBarHostState = remember { SnackbarHostState() }
//            val pullToRefreshState = rememberPullToRefreshState()
//
//            StocksListScreen(
//                stocksPaging = codeChallenges,
//                snackBarHostState = snackBarHostState,
//                pullToRefreshState = pullToRefreshState,
//            )
//        }
//
//        composeTestRule
//            .onNodeWithText(appName)
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun test_CodeChallengesScreen_displays_loading() {
//        composeTestRule.setContent {
//            val codeChallenges = testFlowPagingData.collectAsLazyPagingItems()
//            val snackBarHostState = remember { SnackbarHostState() }
//            val pullToRefreshState = rememberPullToRefreshState()
//
//            StocksListScreen(
//                stocksPaging = codeChallenges,
//                snackBarHostState = snackBarHostState,
//                pullToRefreshState = pullToRefreshState,
//            )
//        }
//
//        composeTestRule
//            .onNodeWithTag("CircularProgressIndicator")
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun test_CodeChallengesScreen_displays_items() {
//        composeTestRule.setContent {
//            val codeChallenges = testFlowPagingDataNotLoading.collectAsLazyPagingItems()
//            val snackBarHostState = remember { SnackbarHostState() }
//            val pullToRefreshState = rememberPullToRefreshState()
//
//            StocksListScreen(
//                stocksPaging = codeChallenges,
//                snackBarHostState = snackBarHostState,
//                pullToRefreshState = pullToRefreshState,
//            )
//        }
//
//        val scrollableNode = composeTestRule
//            .onAllNodes(hasScrollToNodeAction())
//            .onFirst()
//
//        testStockOverviewLists.forEachIndexed { index, codeChallenge ->
//            scrollableNode.performScrollToIndex(index)
//
//            composeTestRule
//                .onNodeWithText(codeChallenge.name)
//                .assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun test_CodeChallengesScreen_displays_append_loading() {
//        composeTestRule.setContent {
//            val codeChallenges = testFlowPagingDataAppendLoading.collectAsLazyPagingItems()
//            val snackBarHostState = remember { SnackbarHostState() }
//            val pullToRefreshState = rememberPullToRefreshState()
//
//            StocksListScreen(
//                stocksPaging = codeChallenges,
//                snackBarHostState = snackBarHostState,
//                pullToRefreshState = pullToRefreshState,
//            )
//        }
//
//        composeTestRule
//            .onAllNodes(hasScrollToNodeAction())
//            .onFirst()
//            .performScrollToNode(hasTestTag("AppendLoading"))
//    }
//
//    @Test
//    fun test_CodeChallengesScreen_displays_append_error() {
//        composeTestRule.setContent {
//            val codeChallenges = testFlowPagingDataAppendError.collectAsLazyPagingItems()
//            val snackBarHostState = remember { SnackbarHostState() }
//            val pullToRefreshState = rememberPullToRefreshState()
//
//            StocksListScreen(
//                stocksPaging = codeChallenges,
//                snackBarHostState = snackBarHostState,
//                pullToRefreshState = pullToRefreshState,
//            )
//        }
//
//        composeTestRule
//            .onAllNodes(hasScrollToNodeAction())
//            .onFirst()
//            .performScrollToNode(hasText(testErrorMessage))
//            .performScrollToNode(hasText(retry))
//    }

}