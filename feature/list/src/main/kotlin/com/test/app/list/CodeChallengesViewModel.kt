package com.test.app.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.test.app.domain.GetCodeChallengesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CodeChallengesViewModel @Inject constructor(
    getCodeChallengesUseCase: GetCodeChallengesUseCase
) : ViewModel() {

    val codeChallenges = getCodeChallengesUseCase.launch()
        .cachedIn(viewModelScope)

}
