package com.test.app.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.app.common.error.AppError
import com.test.app.common.result.fold
import com.test.app.domain.GetCodeChallengeByIdUseCase
import com.test.app.model.data.CodeChallengeDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CodeChallengeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCodeChallengeByIdUseCase: GetCodeChallengeByIdUseCase,
) : ViewModel() {

    private val args: CodeChallengesDetailArgs = CodeChallengesDetailArgs(savedStateHandle)

    @VisibleForTesting
    val codeChallengeId = args.codeChallengeId

    private val _uiState = MutableStateFlow(State(codeChallengeState = CodeChallengeState.Loading))
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun getCodeChallengeById() {
        viewModelScope.launch {
            getCodeChallengeByIdUseCase.launch(codeChallengeId).onEach { result ->
                result.fold(
                    onSuccess = { codeChallengeDetail ->
                        _uiState.update {
                            it.copy(
                                codeChallengeState = CodeChallengeState.Success(
                                    codeChallengeDetail = codeChallengeDetail
                                )
                            )
                        }
                    }, onFailure = { error ->
                        _uiState.update {
                            it.copy(codeChallengeState = CodeChallengeState.Error(error))
                        }
                    },
                    onLoading = {
                        _uiState.update {
                            it.copy(codeChallengeState = CodeChallengeState.Loading)
                        }
                    })
            }.launchIn(viewModelScope)
        }
    }

    data class State(
        val codeChallengeState: CodeChallengeState,
    )

    sealed interface CodeChallengeState {
        data class Success(val codeChallengeDetail: CodeChallengeDetail) : CodeChallengeState

        data class Error(val error: AppError) : CodeChallengeState

        data object Loading : CodeChallengeState
    }

//    val codeChallengeUiState: StateFlow<CodeChallengeState> = codeChallengeState(codeChallengeId)
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = CodeChallengeState.Loading
//        )
//
//    private fun codeChallengeState(challengeId: String): Flow<CodeChallengeState> {
//        return getCodeChallengeByIdUseCase.launch(challengeId).map { result ->
//            when (result) {
//                is DataResult.Success -> CodeChallengeState.Success(codeChallengeDetail = result.data)
//                is DataResult.Failure -> CodeChallengeState.Error(result.error)
//                is DataResult.Loading -> CodeChallengeState.Loading
//            }
//        }
//    }

    init {
        getCodeChallengeById()
    }

    companion object {
        const val CODE_CHALLENGE_ID_ARG = "code_challenge_id_arg"
    }

    internal class CodeChallengesDetailArgs(val codeChallengeId: String) {
        constructor(savedStateHandle: SavedStateHandle) : this(
            checkNotNull(
                savedStateHandle.get<String>(
                    CODE_CHALLENGE_ID_ARG
                )
            )
        )
    }
}
