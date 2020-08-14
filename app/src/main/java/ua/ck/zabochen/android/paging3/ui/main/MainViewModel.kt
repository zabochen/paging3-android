package ua.ck.zabochen.android.paging3.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ua.ck.zabochen.android.paging3.repository.MainRepository

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private var queryLiveData = MutableLiveData<String>()
    val repoResult = queryLiveData.switchMap { query ->
        liveData {
            val repos = mainRepository.getSearchResultStream(query).asLiveData(Dispatchers.Main)
            emitSource(repos)
        }
    }

    fun searchRepo(query: String) {
        queryLiveData.postValue(query)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = queryLiveData.value
            if (immutableQuery != null) {
                viewModelScope.launch {
                    mainRepository.requestMore(immutableQuery)
                }
            }
        }
    }

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }
}