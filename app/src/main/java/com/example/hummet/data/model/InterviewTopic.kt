package com.example.hummet.data.model

data class InterviewTopic(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: String,
    val category: String,
    val questionsCount: Int,
    val estimatedTime: String,
    val icon: String,
    val objectives: List<String>,
    val prerequisites: List<String>
)

data class TopicDetail(
    val topic: InterviewTopic,
    val sampleQuestions: List<String>,
    val skillsCovered: List<String>,
    val completionRate: Float
)
