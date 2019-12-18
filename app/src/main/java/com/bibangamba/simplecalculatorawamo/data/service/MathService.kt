package com.bibangamba.simplecalculatorawamo.data.service

import com.bibangamba.simplecalculatorawamo.data.model.CalculateNetworkResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.POST

/**
 * interface representation of the Math.js API POST endpoint
 */
interface MathService {

    /**
     * @param expression a string representation of the expression to be calculated e.g.
     * expression = "1 * 3". It is represented as `expr` in the JSON request body
     *
     */
    @POST("/v4")
    fun calculate(
        @Field("expr") expression: String
    ): Observable<CalculateNetworkResponse>

}