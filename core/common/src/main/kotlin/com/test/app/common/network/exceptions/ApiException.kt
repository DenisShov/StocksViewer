package com.test.app.common.network.exceptions

import com.test.app.common.network.model.ApiError
import java.io.IOException

class ApiException(val error: ApiError? = null) : IOException()
