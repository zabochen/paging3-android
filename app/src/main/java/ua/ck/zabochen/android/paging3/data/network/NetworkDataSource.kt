package ua.ck.zabochen.android.paging3.data.network

import ua.ck.zabochen.android.paging3.model.RepoSearchResponse

interface NetworkDataSource {

    suspend fun fetchGithubRepos(
        query: String, page: Int, itemPerPage: Int
    ): RepoSearchResponse
}