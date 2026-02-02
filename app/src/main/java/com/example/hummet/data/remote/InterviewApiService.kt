package com.example.hummet.data.remote

import com.example.hummet.data.model.InterviewTopic
import com.example.hummet.data.model.TopicDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface InterviewApiService {

    @GET("api/topics")
    suspend fun getTopics(): List<InterviewTopic>

    @GET("api/topics/{id}")
    suspend fun getTopicDetail(@Path("id") topicId: String): TopicDetail

    @GET("api/topics/{id}/questions")
    suspend fun getTopicQuestions(@Path("id") topicId: String): List<String>
}