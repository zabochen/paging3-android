package ua.ck.zabochen.android.paging3.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import retrofit2.HttpException
import ua.ck.zabochen.android.paging3.common.state.ResultState
import ua.ck.zabochen.android.paging3.data.network.service.NetworkService
import ua.ck.zabochen.android.paging3.model.Repo
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainRepository @Inject constructor(
    private val networkService: NetworkService
) {
    // Keep the list of all results received
    private val inMemoryCache: MutableList<Repo> = mutableListOf()

    // Keep channel of results
    // The channel allows us to broadcast updates so the subscriber will have the latest data
    private val searchResults = ConflatedBroadcastChannel<ResultState<List<Repo>>>()

    // Keep the last requested page
    // When the request is successful, increment the page number.
    private var lastRequestedPage = GITHUB_STARTING_PAGE

    // Avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    suspend fun getSearchResultStream(query: String): Flow<ResultState<List<Repo>>> {
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query)
        return searchResults.asFlow()
    }

    suspend fun requestMore(query: String) {
        if (isRequestInProgress) return
        val successful = requestAndSaveData(query)
        if (successful) {
            lastRequestedPage++
        }
    }

    suspend fun retry(query: String) {
        if (isRequestInProgress) return
        requestAndSaveData(query)
    }

    private suspend fun requestAndSaveData(query: String): Boolean {
        isRequestInProgress = true
        var successful = false
        try {
            val response = networkService.fetchGithubRepos(query, lastRequestedPage, ITEMS_PER_PAGE)
            val repos = response.items ?: emptyList()
            inMemoryCache.addAll(repos)
            val reposByName = reposByName(query)
            searchResults.offer(ResultState.Success(reposByName))
            successful = true
        } catch (exception: IOException) {
            searchResults.offer(ResultState.Error(exception))
        } catch (exception: HttpException) {
            searchResults.offer(ResultState.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

    private fun reposByName(query: String): List<Repo> {
        // from the in memory cache select only the repos whose name or description matches
        // the query. Then order the results.
        return inMemoryCache.filter {
            it.name.contains(query, true) ||
                    (it.description != null && it.description.contains(query, true))
        }.sortedWith(compareByDescending<Repo> { it.stars }.thenBy { it.name })
    }

    companion object {
        private const val GITHUB_STARTING_PAGE = 1
        private const val ITEMS_PER_PAGE = 50
    }
}