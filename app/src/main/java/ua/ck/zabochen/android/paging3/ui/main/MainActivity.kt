package ua.ck.zabochen.android.paging3.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ua.ck.zabochen.android.paging3.common.extension.showToast
import ua.ck.zabochen.android.paging3.common.state.ResultState
import ua.ck.zabochen.android.paging3.databinding.ActivityMainBinding

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        initUi()
    }

    private fun bind() {
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initUi() {
        initRepoList()
        mainViewModel.searchRepo("Android")
    }

    private fun initRepoList() {
        val repoAdapter = MainAdapter(
            onClick = {}
        )

        binding.rvRepos.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        mainViewModel.repoResult.observe(this, Observer { result ->
            when (result) {
                is ResultState.Success -> {
                    repoAdapter.submitList(result.data)
                }
                is ResultState.Error -> {
                    this@MainActivity.showToast(result.exception.message ?: "")
                }
            }
        })
    }
}