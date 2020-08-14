package ua.ck.zabochen.android.paging3.data.network

import ua.ck.zabochen.android.paging3.data.network.service.NetworkService
import ua.ck.zabochen.android.paging3.model.RepoSearchResponse

class NetworkDataSourceImpl(
    private val networkService: NetworkService
) : NetworkDataSource {

    override suspend fun fetchGithubRepos(
        query: String, page: Int, itemPerPage: Int
    ): RepoSearchResponse {
        return networkService.fetchGithubRepos(query, page, itemPerPage)
    }
}